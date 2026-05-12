/* FUNKTIONIERT NICHT:
#version 330 core
in vec3 color;
out vec4 fragColor;

void main() {
    fragColor = vec4(color, 1.0);
}*/

// FUNKTIONIERT:
#version 330 core
layout (location = 0) in vec3 position;

void main() {
    gl_Position = vec4(position, 1.0);
}
