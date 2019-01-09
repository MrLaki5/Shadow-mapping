package shaders.complete;

import com.jogamp.opengl.GL4;

public class ObjectShader extends CompleteShader {

    public static final String vertexSrc =
            "#version 410 core \n" +
            "layout(location = 0) in vec3 vertexPosition;\n" +
            "layout(location = 1) in vec2 vertexTexPos;\n" +
            "layout(location = 2) in vec3 vertexNormal;\n"+
            "out VS_OUT{\n" +
            "vec3 FragPos;\n"+
            "vec3 Normal;\n"+
            "vec2 TexCords;\n"+
            "vec4 FragPosLightSpace;\n"+
            "} vs_out;\n"+
            "uniform mat4 viewProjection;\n" +
            "uniform mat4 model;\n"+
            "uniform mat4 lightSpace;"+
            "void main()\n" +
            "{\n" +
            "vs_out.FragPos = vec3(model * vec4(vertexPosition, 1.0));\n"+
            "vs_out.Normal = transpose(inverse(mat3(model))) * vertexNormal;\n"+
            "vs_out.TexCords = vertexTexPos;\n"+
            "vs_out.FragPosLightSpace = lightSpace * vec4(vs_out.FragPos, 1.0);\n"+
            "gl_Position = viewProjection * model * vec4(vertexPosition, 1.0);\n"+
            "}\n";

    public static final String fragmentSrc =
            "#version 410\n" +
            "in VS_OUT{\n" +
            "vec3 FragPos;\n"+
            "vec3 Normal;\n"+
            "vec2 TexCords;\n"+
            "vec4 FragPosLightSpace;\n"+
            "} fs_in;\n"+
            "out vec4 FragColor;\n" +
            "uniform sampler2D texMap;\n" +
            "uniform sampler2D shadowMap;\n"+
            "uniform vec3 lightPos;\n"+
            "uniform vec3 viewPos;\n"+
            "void main()\n" +
            "{\n" +
            "outColor = texture(texMap, interpolatedTexPos).rgba;\n" +
            "}\n";

    public static final String geometrySrc = "";

    public ObjectShader(GL4 gl) {
        super(gl, "Object shader", true, true, false, vertexSrc, fragmentSrc, geometrySrc);
    }
}
