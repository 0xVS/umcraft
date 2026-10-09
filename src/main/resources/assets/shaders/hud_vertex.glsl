#version 330
layout (location = 0) in vec2 position;
layout (location = 1) in vec2 uv;

uniform float aspect_r;
out vec2 out_uv;

void main() {
    out_uv = uv;
    gl_Position = vec4(position.x / aspect_r, position.y, 0.0, 1.0);
}
