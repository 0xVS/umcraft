#version 330
in vec2 out_uv;
uniform sampler2D hudTexture;

out vec4 fragColor;

void main() {
    vec4 color = texture(hudTexture, out_uv);
    if (color.a < 0.1) discard; // transparente pixel skippen
    fragColor = color;
}