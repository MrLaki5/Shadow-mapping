package shaders.complete;

import com.jogamp.opengl.GL4;

public class ObjectShader extends CompleteShader {

    public static final String vertexSrc =
            "#version 410 core \n" +
            "layout(location = 0) in vec3 vertexPosition;\n" +
            "layout(location = 1) in vec2 vertexTexPos;\n" +
            "out vec2 interpolatedTexPos;\n" +
            "uniform mat4 transform;\n" +
            "void main()\n" +
            "{\n" +
            "interpolatedTexPos = vertexTexPos;" +
            "gl_Position = transform * vec4(vertexPosition, 1.0);\n" +
            "}\n";

    public static final String fragmentSrc =
            "#version 410\n" +
            "in vec2 interpolatedTexPos;\n" +
            "out vec4 outColor;\n" +
            "uniform sampler2D texMap;\n" +
            "void main()\n" +
            "{\n" +
            "outColor = texture(texMap, interpolatedTexPos).rgba;\n" +
            "}\n";

    public static final String geometrySrc = "";

    public ObjectShader(GL4 gl) {
        super(gl, "Object shader", true, true, false, vertexSrc, fragmentSrc, geometrySrc);
    }
}
