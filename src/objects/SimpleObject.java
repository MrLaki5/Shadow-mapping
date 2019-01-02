package objects;

import cameras.Camera;
import com.jogamp.opengl.GL4;
import org.joml.Matrix4f;
import shaders.complete.CompleteShader;

public abstract class SimpleObject {
    protected CompleteShader shader;
    protected Matrix4f transform = new Matrix4f();
    public abstract void init(GL4 gl);
    public abstract void render(GL4 gl, Camera c);
    public abstract void destroy(GL4 gl);
    public abstract void update();

    public Matrix4f GetTransform() {
        return transform;
    }

    public void setShaderProgram(CompleteShader sp) {
        shader = sp;
    }

    public CompleteShader getShaderProgram() {
        return shader;
    }
}
