package shaders.Base;

import com.jogamp.opengl.GL4;

public class VertexShader extends Shader
{

    public VertexShader(String _shaderName)
    {
        super(GL4.GL_VERTEX_SHADER, _shaderName);
    }

}
