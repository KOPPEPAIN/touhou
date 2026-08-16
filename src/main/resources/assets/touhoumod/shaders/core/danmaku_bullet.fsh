#version 150

uniform sampler2D Sampler0;
uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec2 texCoord0;
in float glowPulse;

out vec4 fragColor;

void main() {
    vec4 texColor = texture(Sampler0, texCoord0);
    if (texColor.a < 0.01) {
        discard;
    }

    // Emissive glow: brighten past 1.0 so additive blending gives a bright
    // bullet core with a softly fading rim, independent of scene lighting.
    vec3 glow = vertexColor.rgb * texColor.rgb * (1.3 + glowPulse * 0.7);
    fragColor = vec4(glow, texColor.a * vertexColor.a) * ColorModulator;
}
