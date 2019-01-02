package shaders.Base;

import com.jogamp.opengl.GL4;


public class GeometryShader extends Shader
{

    public GeometryShader(String _shaderName)
    {
        super(GL4.GL_GEOMETRY_SHADER, _shaderName);
    }
}