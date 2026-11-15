#version 330
layout(location = 0) in vec2 position;
layout(location = 1) in vec2 uv;

uniform vec2 screenSize;
out vec2 out_uv;

void main() {
    vec2 ndc = vec2(
            position.x / screenSize.x * 2.0 - 1.0,
            1.0 - position.y / screenSize.y * 2.0
    );
    gl_Position = vec4(ndc, 0.0, 1.0);
    out_uv = uv;
}