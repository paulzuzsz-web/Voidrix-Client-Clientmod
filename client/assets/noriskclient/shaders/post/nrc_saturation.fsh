#version 330

uniform sampler2D InSampler;
in vec2 texCoord;
out vec4 fragColor;

layout(std140) uniform ColorSaturation {
    float Hue;
    float Saturation;
    float Brightness;
    float Contrast;
};

vec3 rgb2hsv(vec3 c) {
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));
    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.w);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

void main() {
    vec4 color = texture(InSampler, texCoord);

    vec3 hsb = rgb2hsv(color.rgb);

    hsb.x += Hue;
    hsb.y *= Saturation;
    hsb.z *= Brightness;

    vec3 hsb_final = hsb;
    hsb_final.z = ((hsb_final.z - 0.5) * Contrast) + 0.5;

    hsb_final.x = mod(hsb_final.x, 1.0);
    hsb_final.y = clamp(hsb_final.y, 0.0, 1.0);
    hsb_final.z = clamp(hsb_final.z, 0.0, 1.0);

    vec3 final_rgb = hsv2rgb(hsb_final);

    fragColor = vec4(final_rgb, color.a);
}
