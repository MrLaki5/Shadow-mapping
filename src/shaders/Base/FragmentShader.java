package shaders.base;

import com.jogamp.opengl.GL4;


public class FragmentShader extends Shader
{
    public FragmentShader(String _shaderName)
    {
        super(GL4.GL_FRAGMENT_SHADER, _shaderName);
    }

}
