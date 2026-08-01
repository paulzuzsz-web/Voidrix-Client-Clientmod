#version 330

uniform sampler2D DiffuseSampler;
uniform sampler2D PrevSampler;

in vec2 texCoord;

layout(std140) uniform MotionBlurConfig {
    float BlendFactor;
};

out vec4 fragColor;

void main() {
    vec4 currentFrameColor = texture(DiffuseSampler, texCoord);
    vec4 prevFrameColor = texture(PrevSampler, texCoord);
    vec4 blurredColor = mix(currentFrameColor, prevFrameColor, BlendFactor);
    fragColor = blurredColor;
}
