#version 330

#include <minecraft:dynamictransforms.glsl>
#include <minecraft:projection.glsl>

#ifdef NO_LAYOUT
in vec3 Position;
#else
#extension GL_ARB_separate_shader_objects : require
layout(location = 0) in vec3 Position;
#endif

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
}
