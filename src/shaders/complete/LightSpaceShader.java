package shaders.complete;

import com.jogamp.opengl.GL4;

public class LightSpaceShader extends CompleteShader{

    public static final String vertexSrc =
            "#version 410 core \n" +
            "layout(location = 0) in vec3 vertexPosition;\n" +
            "uniform mat4 transform;\n" +
            "void main()\n" +
            "{\n" +
            "gl_Position = transform * vec4(vertexPosition, 1.0);\n" +
            "}\n";

    public static final String fragmentSrc =
            "#version 410\n" +
            "out vec4 outColor;\n" +
            "void main()\n" +
            "{\n" +
            "outColor = vec4(0.0, 0.6, 0.0, 1.0);\n" +
            "}\n";

    public static final String geometrySrc = "";

    public LightSpaceShader(GL4 gl) {
        super(gl, "Light space shader", true, true, false, vertexSrc, fragmentSrc, geometrySrc);
    }


}
