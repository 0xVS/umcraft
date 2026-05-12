#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 uv;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

//out vec3 color;
out vec2 out_uv;

void main() {
    // DIE VERSION FUNKTIONIERT
    //gl_Position = projectionMatrix * vec4(position.x, position.y, -1.0, 1.0);

    //gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
    //color = vec3(1.0, 0.5, 0.2);

    out_uv = uv;
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(position, 1.0);
}