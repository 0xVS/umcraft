#version 330
in vec2 out_uv;
uniform sampler2D uiTexture;
uniform vec4 tint;
out vec4 fragColor;

void main() {
    fragColor = texture(uiTexture, out_uv) * tint;
    if (fragColor.a < 0.004) discard;
}