#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;
uniform float Saturation;
uniform float PixelSize;

out vec4 fragColor;

void main() {
    vec4 InTexel = texture(DiffuseSampler, floor(texCoord * InSize / PixelSize) / InSize * PixelSize);
    float RedValue = dot(InTexel.rgb, vec3(1.0, 0.0, 0.0));
    float GreenValue = dot(InTexel.rgb, vec3(0.0, 1.0, 0.0));
    float BlueValue = dot(InTexel.rgb, vec3(0.0, 0.0, 1.0));
    vec3 OutColor = vec3(RedValue, GreenValue, BlueValue);

    float Luma = dot(OutColor, vec3(0.3, 0.59, 0.11));
    vec3 Chroma = OutColor - Luma;
    OutColor = Chroma * Saturation + Luma;

    fragColor = vec4(OutColor, 1.0);
}
