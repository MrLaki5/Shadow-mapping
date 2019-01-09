package shaders.complete;

import com.jogamp.opengl.GL4;

public class DepthMapDrawShader extends CompleteShader{

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
            "out vec4 FragColor;"+
            "in vec2 interpolatedTexPos;"+
            "uniform sampler2D depthMap;"+
            "void main()\n" +
            "{\n" +
            "float depthValue = texture(depthMap, interpolatedTexPos).r;"+
            "FragColor = vec4(vec3(depthValue), 1.0);"+
            "}\n";

    public static final String geometrySrc = "";

    public DepthMapDrawShader(GL4 gl) {
        super(gl, "Depth map draw shader", true, true, false, vertexSrc, fragmentSrc, geometrySrc);
    }


}