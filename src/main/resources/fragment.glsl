#version 330 core

//out vec4 fragColor;

//void main() {
    //fragColor = vec4(1.0, 0.5, 0.2, 1.0);
//}

in vec2 out_uv;
uniform sampler2D textureSampler;

out vec4 fragUV;

void main() {
    fragUV = texture(textureSampler, out_uv);
}