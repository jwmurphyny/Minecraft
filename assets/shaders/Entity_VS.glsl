#version 130

in vec3 position;
in vec2 textureCoordinates;
in vec3 normal;

out vec2 pass_textureCoordinates;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(void){
	gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position,1.0);
	pass_textureCoordinates = textureCoordinates;
}
