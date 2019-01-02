package objects;

import cameras.Camera;
import com.jogamp.opengl.GL4;

import java.nio.IntBuffer;

public class Box extends SimpleObject{

    private int vertexArrayID;
    private int vertexBufferID;
    private int colorBufferID;
    private int normalBufferID;
    private int vertexIndexBufferID;

    @Override
    public void init(GL4 gl) {
        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGenVertexArrays(1, intBuffer);
        vertexArrayID = intBuffer.get(0);
        gl.glBindVertexArray(vertexArrayID);

        float a = 0.5f;
        float []vertexData = {
                0-a, 0-a, 0-a,
                0+a, 0-a, 0-a,
                0+a, 0+a, 0-a,
                0-a, 0+a, 0-a,
                0-a, 0-a, 0+a,
                0+a, 0-a, 0+a,
                0+a, 0+a, 0+a,
                0-a, 0+a, 0+a
        };

        float []vertexColor = {
                0.0f, 0.0f, 1.0f, 1.0f,
                0.0f, 1.0f, 0.0f, 1.0f,
                1.0f, 0.0f, 0.0f, 1.0f,
                1.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 1.0f, 1.0f, 1.0f,
                1.0f, 0.0f, 1.0f, 1.0f,
                0.5f, 0.5f, 0.5f, 1.0f,
                0.1f, 0.7f, 0.3f, 1.0f
        };

        int []indexArr = new int[] {
                0, 1, 2, 3,
                1, 5, 6, 2,
                5, 4, 7, 6,
                4, 0, 3, 7,
                3, 2, 6, 7,
                0, 4, 5, 1
        };
    }

    @Override
    public void render(GL4 gl, Camera c) {

    }

    @Override
    public void destroy(GL4 gl) {

    }

    @Override
    public void update() {

    }
}
