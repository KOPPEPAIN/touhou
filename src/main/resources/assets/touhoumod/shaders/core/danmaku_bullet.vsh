#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform float GameTime;

out vec4 vertexColor;
out vec2 texCoord0;
out float glowPulse;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexColor = Color;
    texCoord0 = UV0;
    // GameTime is a slowly increasing normalized clock; just use it as a
    // free-running phase for a subtle pulsing glow.
    glowPulse = 0.75 + 0.25 * sin(GameTime * 4000.0);
}
