#version 330 core

layout (location = 0) in vec3 position;
uniform vec2 offset;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

out vec3 color;

void main() {
    gl_Position = projectionMatrix * viewMatrix * vec4(position, 1);
}