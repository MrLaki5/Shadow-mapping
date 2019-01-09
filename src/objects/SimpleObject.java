package objects;

import cameras.Camera;
import com.jogamp.opengl.GL4;
import lights.Light;
import org.joml.Matrix4f;
import shaders.complete.CompleteShader;

public abstract class SimpleObject {
    protected CompleteShader shader;
    protected CompleteShader shadowDepthShader;
    protected Matrix4f transform;
    protected int textureID;
    public abstract void init(GL4 gl);
    public abstract void render(GL4 gl, Camera camera, Light light);
    public abstract void renderLightPerspective(GL4 gl, Light light);
    public abstract void destroy(GL4 gl);
    public abstract void update();

    public SimpleObject(){
        transform = new Matrix4f().identity();
    }

    public Matrix4f GetTransform() {
        return transform;
    }

    public void setTransform(Matrix4f transform){
        this.transform = transform;
    }

    public void setShaderProgram(CompleteShader sp) {
        shader = sp;
    }

    public CompleteShader getShaderProgram() {
        return shader;
    }

    public void setTextureID(int textureID) {
        this.textureID = textureID;
    }

    public CompleteShader getShadowDepthShader() {
        return shadowDepthShader;
    }

    public void setShadowDepthShader(CompleteShader shadowDepthShader) {
        this.shadowDepthShader = shadowDepthShader;
    }
}
