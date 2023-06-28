// SOURCE: https://www.shadertoy.com/view/MslyWr

#version 150
#define BS 8

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;
uniform float Saturation;
uniform float Quality = length(vec2(2));

out vec4 fragColor;

const float PI = radians(180.0);
const float BSf = float(BS);

float basis1D(int k, int i) {
    return k == 0 ? sqrt(1. / BSf) : sqrt(2. / BSf) * cos(float((2 * i + 1) * k) * PI / (2. * BSf));
}

float basis2D(ivec2 jk, ivec2 xy) {
    return basis1D(jk.x, xy.x) * basis1D(jk.y, xy.y);
}

vec4 getSaturation(vec4 InTexel) {
    float RedValue = dot(InTexel.rgb, vec3(1.0, 0.0, 0.0));
    float GreenValue = dot(InTexel.rgb, vec3(0.0, 1.0, 0.0));
    float BlueValue = dot(InTexel.rgb, vec3(0.0, 0.0, 1.0));
    vec3 OutColor = vec3(RedValue, GreenValue, BlueValue);
    float Luma = dot(OutColor, vec3(0.3, 0.59, 0.11));
    vec3 Chroma = OutColor - Luma;
    OutColor = Chroma * Saturation + Luma;
    return vec4(OutColor, 1.0);
}

void main() {
    if (Quality >= BSf) {
        fragColor = getSaturation(texture(DiffuseSampler, texCoord));
        return;
    }

    fragColor = vec4(0, 0, 0, 1.0);

    ivec2 coords = ivec2(texCoord * InSize);
    ivec2 inBlock = coords % BS;
    ivec2 block = coords - inBlock;

    if (length(vec2(inBlock)) > Quality) { return; }

    for (ivec2 xy = ivec2(0); xy.x < BS; xy.x++) {
        for (xy.y = 0; xy.y < BS; xy.y++) {
            vec4 InTexel = texelFetch(DiffuseSampler, block + xy, 0);
            fragColor += InTexel * basis2D(inBlock, xy) / BS;
        }
    }
}
