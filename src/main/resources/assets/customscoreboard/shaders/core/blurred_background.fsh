#version 150

const float PI2 = 6.28318530718;

const float BLUR_DIRECTIONS = 32.0;
const float BLUR_QUALITY = 9.0;
const float BLUR_SIZE = 8.0;

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>


layout(std140) uniform RoundedTextureUniform {
    vec4 radius;
    vec2 size;
    vec2 center;
    float scaleFactor;
};

uniform sampler2D Sampler0;
out vec4 fragColor;

// From: https://iquilezles.org/articles/distfunctions2d/
float sdRoundedBox(vec2 p, vec2 b, vec4 r){
    r.xy = (p.x > 0.0) ? r.xy : r.zw;
    r.x  = (p.y > 0.0) ? r.x  : r.y;
    vec2 q = abs(p)-b+r.x;
    return min(max(q.x,q.y),0.0) + length(max(q,0.0)) - r.x;
}

void main() {
    vec2 halfSize = size / 2.0;
    float distance = sdRoundedBox(gl_FragCoord.xy - center, halfSize, radius * scaleFactor);

    if (distance > 0.0) {
        discard;
    } else {
        vec2 texCoord = gl_FragCoord.xy / ScreenSize;
        vec2 blurRadius = BLUR_SIZE / ScreenSize;
        vec4 color = texture(Sampler0, texCoord);

        for (float d = 0.0; d < PI2; d += PI2 / BLUR_DIRECTIONS) {
            for (float i = 1.0 / BLUR_QUALITY; i <= 1.0; i += 1.0 / BLUR_QUALITY) {
                color += texture(Sampler0, texCoord + vec2(cos(d), sin(d)) * blurRadius * i);
            }
        }

        fragColor = color / (BLUR_QUALITY * BLUR_DIRECTIONS - 15.0);
    }
}
