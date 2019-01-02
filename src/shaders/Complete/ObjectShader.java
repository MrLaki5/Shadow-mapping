package shaders.complete;

import com.jogamp.opengl.GL4;

public class ObjectShader extends CompleteShader{

    public static final String vertexSrc =
            "#version 430 core \n" +
            "layout(location = 0) in vec3 vertexPosition;\n" +
            "layout(location = 1) in vec3 vertexColor;\n" +
            "out vec4 interpolatedVertexColor;\n" +
            "uniform mat4 transform;\n" +
            "void main()\n" +
            "{\n" +
            "interpolatedVertexColor = vec4(vertexColor, 1);" +
            "gl_Position = transform * vec4(vertexPosition, 1.0);\n" +
            "}\n";

    public static final String fragmentSrc =
            "#version 430\n" +
            "in vec4 interpolatedVertexColor;\n" +
            "out vec4 outColor;\n" +
            "void main()\n" +
            "{\n" +
            "outColor = interpolatedVertexColor;\n" +
            "}\n";

    public static final String geometrySrc = "";

    public ObjectShader(GL4 gl) {
        super(gl, "Object shader", true, true, false, vertexSrc, fragmentSrc, geometrySrc);
    }
}
