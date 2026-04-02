#version 330 core

layout (location = 0) in vec3 position;
uniform vec2 offset;

out vec3 color;

void main() {
    vec2 finalPos = position.xy + offset;
    gl_Position = vec4(finalPos, 0.0, 1.0);
    color = vec3(1.0, 0.0, 0.0);
}