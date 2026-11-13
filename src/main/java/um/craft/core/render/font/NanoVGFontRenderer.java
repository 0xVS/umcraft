package um.craft.core.render.font;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.system.MemoryUtil;
import um.craft.core.util.Log;
import um.craft.core.util.Utils;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11C.*;

/**
 * NanoVG-basierter Font-Renderer.
 *
 * <p>Erwartet eine TTF/OTF-Datei im Classpath unter {@code resources/assets/}.
 * Die Metriken (Ascent, LineHeight) werden beim Erzeugen aus der Schrift gelesen,
 * damit {@link #drawString} weiterhin mit {@code y} als Oberkante arbeitet.</p>
 *
 * <p>NanoVG sollte am Ende eines Frames gerendert werden, da es den OpenGL-State
 * veraendert. {@link #drawString} oeffnet automatisch einen Frame, wenn keiner
 * ueber {@link #beginFrame} gestartet wurde.</p>
 */
public class NanoVGFontRenderer implements AutoCloseable {

    private static final float REFERENCE_SIZE = 12f;
    private static final float SHADOW_ALPHA = 0.5f;
    private static final float SHADOW_OFFSET = 1.25f;

    private final long vg;
    private final ByteBuffer ttf;
    private final int font;
    private final String fontName;

    private final NVGColor color = NVGColor.calloc();
    private final float[] bounds = new float[4];

    private float baseAscent;
    private float baseLineHeight;

    private int frameWidth = 1280;
    private int frameHeight = 720;
    private float pixelRatio = 1f;

    private boolean frameOpen;
    private boolean disposed;

    public NanoVGFontRenderer(String ttfFile) {
        this("minogram", ttfFile);
    }

    public NanoVGFontRenderer(String fontName, String ttfFile) {
        this.fontName = fontName;

        this.vg = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS | NanoVGGL3.NVG_STENCIL_STROKES);
        if (vg == MemoryUtil.NULL) {
            throw new IllegalStateException("NanoVG-Kontext konnte nicht erstellt werden");
        }

        byte[] bytes = Utils.loadResourceBytes("assets/" + ttfFile);
        this.ttf = MemoryUtil.memAlloc(bytes.length);
        this.ttf.put(bytes).flip();

        this.font = NanoVG.nvgCreateFontMem(vg, fontName, ttf, false);
        if (font == -1) {
            MemoryUtil.memFree(ttf);
            NanoVGGL3.nvgDelete(vg);
            throw new IllegalStateException("Schrift konnte nicht geladen werden: " + ttfFile);
        }

        measureMetrics();
    }

    private void measureMetrics() {
        NanoVG.nvgFontFaceId(vg, font);
        NanoVG.nvgFontSize(vg, REFERENCE_SIZE);

        float[] ascender = new float[1];
        float[] descender = new float[1];
        float[] lineHeight = new float[1];
        NanoVG.nvgTextMetrics(vg, ascender, descender, lineHeight);

        this.baseAscent = ascender[0] > 0f ? ascender[0] : REFERENCE_SIZE * 0.8f;
        this.baseLineHeight = lineHeight[0] > 0f ? lineHeight[0] : REFERENCE_SIZE;

        Log.debug("NanoVG Font '" + fontName + "': ascent=" + baseAscent
                + " descent=" + descender[0] + " lineHeight=" + baseLineHeight);
    }

    public void resize(int width, int height) {
        this.frameWidth = width;
        this.frameHeight = height;
    }

    public void beginFrame(int width, int height, float pixelRatio) {
        if (disposed) {
            throw new IllegalStateException("NanoVGFontRenderer wurde bereits freigegeben");
        }
        if (frameOpen) {
            throw new IllegalStateException("NanoVG-Frame ist bereits offen");
        }

        this.frameWidth = width;
        this.frameHeight = height;
        this.pixelRatio = pixelRatio;

        NanoVG.nvgBeginFrame(vg, width, height, pixelRatio);
        frameOpen = true;
    }

    public void endFrame() {
        if (!frameOpen) {
            throw new IllegalStateException("Kein NanoVG-Frame offen");
        }

        NanoVG.nvgEndFrame(vg);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glDisable(GL_BLEND);
        glDisable(GL_SCISSOR_TEST);
        frameOpen = false;
    }

    /**
     * Zeichnet Text mit der Ecke oben links bei (x, y).
     */
    public void drawString(String text, float x, float y, float scale, float r, float g, float b, float a) {
        if (text.isEmpty()) {
            return;
        }

        boolean autoFrame = !frameOpen;
        if (autoFrame) {
            beginFrame(frameWidth, frameHeight, pixelRatio);
        }

        try {
            applyFont(scale);

            float baseline = y + baseAscent * scale;
            float lineAdvance = baseLineHeight * scale;
            float shadowOffset = SHADOW_OFFSET * scale;

            // background text mit offset und alpha value
            color.r(0f).g(0f).b(0f).a(a * SHADOW_ALPHA);
            NanoVG.nvgFillColor(vg, color);
            drawLines(text, x + shadowOffset, baseline + shadowOffset, lineAdvance);

            // foreground text als weiße schrift
            color.r(r).g(g).b(b).a(a);
            NanoVG.nvgFillColor(vg, color);
            drawLines(text, x, baseline, lineAdvance);
        } finally {
            if (autoFrame) {
                endFrame();
            }
        }
    }

    private void drawLines(String text, float x, float baseline, float lineAdvance) {
        int lineStart = 0;

        for (int i = 0; i <= text.length(); i++) {
            if (i == text.length() || text.charAt(i) == '\n') {
                if (i > lineStart) {
                    NanoVG.nvgText(vg, x, baseline, text.substring(lineStart, i));
                }

                baseline += lineAdvance;
                lineStart = i + 1;
            }
        }
    }

    public float getWidth(String text, float scale) {
        boolean autoFrame = !frameOpen;
        if (autoFrame) {
            beginFrame(frameWidth, frameHeight, pixelRatio);
        }

        try {
            applyFont(scale);

            float maxWidth = 0f;
            int lineStart = 0;

            for (int i = 0; i <= text.length(); i++) {
                if (i == text.length() || text.charAt(i) == '\n') {
                    if (i > lineStart) {
                        float width = NanoVG.nvgTextBounds(vg, 0f, 0f, text.substring(lineStart, i), bounds);
                        maxWidth = Math.max(maxWidth, width);
                    }

                    lineStart = i + 1;
                }
            }

            return maxWidth;
        } finally {
            if (autoFrame) {
                endFrame();
            }
        }
    }

    public int getLineHeight(float scale) {
        return Math.round(baseLineHeight * scale);
    }

    private void applyFont(float scale) {
        NanoVG.nvgFontFaceId(vg, font);
        NanoVG.nvgFontSize(vg, REFERENCE_SIZE * scale);
        NanoVG.nvgFontBlur(vg, 0f);
        NanoVG.nvgTextAlign(vg, NanoVG.NVG_ALIGN_LEFT | NanoVG.NVG_ALIGN_BASELINE);
    }

    public void trash() {
        if (disposed) {
            return;
        }

        if (frameOpen) {
            endFrame();
        }

        NanoVGGL3.nvgDelete(vg);
        MemoryUtil.memFree(ttf);
        color.free();

        disposed = true;
    }

    public void cleanup() {
        trash();
    }

    @Override
    public void close() {
        trash();
    }
}
