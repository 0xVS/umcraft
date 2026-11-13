package um.craft.core.render.hud;

import um.craft.core.render.Shader;
import um.craft.core.render.Texture;
import um.craft.core.render.font.NanoVGFontRenderer;

import java.util.EnumSet;

public class DebugOverlay implements HudElement {
    private static final float PANEL_X = -0.98f;
    private static final float PANEL_Y = 0.95f;
    private static final float PANEL_W = 0.5f;
    private static final float PANEL_H = 0.4f;


    private static final float TEXT_SCALE = 2.5f;


    private static final float TEXT_PADDING = 4f;

    private boolean visible = false;
    // private final HudMesh backgroundMesh;
    //private final Texture backgroundTexture;

    private int width;
    private int height;

    private final EnumSet<DebugInfo> enabled = EnumSet.of(
            DebugInfo.VERSION,
            DebugInfo.POSITION,
            DebugInfo.ROTATION,
            DebugInfo.FPS,
            DebugInfo.RAM,
            DebugInfo.CHUNK_POS,
            DebugInfo.LOADED_CHUNKS
    );

    public DebugOverlay(int windowWidth, int windowHeight) {
        this.width = windowWidth;
        this.height = windowHeight;

        float x = PANEL_X;
        float y = PANEL_Y;
        float w = PANEL_W;
        float h = PANEL_H;

        float[] vertices = {
                x,     y - h,   0f, 0f,
                x + w, y - h,   1f, 0f,
                x + w, y,       1f, 1f,
                x,     y,       0f, 1f,
        };
        int[] indices = { 0, 1, 2, 0, 2, 3 };

        //backgroundMesh    = new HudMesh(vertices, indices);
        //backgroundTexture = new Texture("textures/gui/debug_bg.png");
    }

    public void add(DebugInfo info) {
        enabled.add(info);
    }

    public void remove(DebugInfo info) {
        enabled.remove(info);
    }

    public void setEnabled(DebugInfo info, boolean enabled) {
        if (enabled) {
            this.enabled.add(info);
        } else {
            this.enabled.remove(info);
        }
    }

    public boolean isEnabled(DebugInfo info) {
        return enabled.contains(info);
    }
/*
    @Override
    public void render(Shader hudShader) {
        //backgroundTexture.bind();
        hudShader.setInt("hudTexture", 0);
        backgroundMesh.render();
    }
*/
    @Override
    public void render(Shader hudShader) {
        //
    }

    @Override
    public void renderText(NanoVGFontRenderer font) {
        float x = (PANEL_X + 1f) * 0.5f * width + TEXT_PADDING;
        float y = (1f - PANEL_Y) * 0.5f * height + TEXT_PADDING;
        float lineAdvance = font.getLineHeight(TEXT_SCALE);

        for (DebugInfo info : enabled) {
            font.drawString(info.get(), x, y, TEXT_SCALE, 1f, 1f, 1f, 1f);
            y += lineAdvance;
        }
    }

    @Override
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void cleanup() {
        //backgroundMesh.cleanup();
    }

    @Override public boolean isVisible()        { return visible; }
    @Override public void setVisible(boolean v) { visible = v;    }
}