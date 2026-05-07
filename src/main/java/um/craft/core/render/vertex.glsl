#version 330 core

layout (location = 0) in vec3 position;
uniform vec2 offset;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out vec3 color;
// TODO: neue projMatrix und viewMatrix Implementation
void main() {
    vec3 finalPos = position + vec4(offset, 0.0, 0.0).xyz;
    gl_Position = projectionMatrix * viewMatrix * vec4(finalPos, 1.0);
    color = vec3(1.0, 0.5, 0.2);
}