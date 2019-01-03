package objects;

import cameras.Camera;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Floor extends SimpleObject {

    private int vertexArrayID;
    private int vertexBufferID;
    private int colorBufferID;
    private int normalBufferID;
    private int vertexIndexBufferID;
    private float VPMatrixArr[] = new float[16];

    public Floor(){
        super();
    }

    @Override
    public void init(GL4 gl) {
        float a = 5;
        float []vertexData = {
                0-a, 0, 0-a,
                0+a, 0, 0-a,
                0+a, 0, 0+a,
                0-a, 0, 0+a
        };

        float []vertexColor = {
                0.4f, 0.4f, 0.4f, 1.0f,
                0.4f, 0.4f, 0.4f, 1.0f,
                0.4f, 0.4f, 0.4f, 1.0f,
                0.4f, 0.4f, 0.4f, 1.0f
        };

        int []indexArr = new int[] {
                0, 1, 2, 3
        };

        float[] vertexNormal = computeNormals(vertexData);

        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGenVertexArrays(1, intBuffer);
        vertexArrayID = intBuffer.get(0);
        gl.glBindVertexArray(vertexArrayID);

        FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertexData, 0);
        FloatBuffer vertexColorBuffer = Buffers.newDirectFloatBuffer(vertexColor, 0);
        FloatBuffer vertexNormalBuffer = Buffers.newDirectFloatBuffer(vertexNormal, 0);
        IntBuffer vertexIndexBuffer = Buffers.newDirectIntBuffer(indexArr, 0);

        intBuffer.rewind();
        gl.glGenBuffers(1, intBuffer);
        vertexBufferID = intBuffer.get(0);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, vertexBufferID);
        gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertexData.length * Float.BYTES, vertexBuffer, GL4.GL_STATIC_DRAW);
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 0, 0);

        intBuffer.rewind();
        gl.glGenBuffers(1, intBuffer);
        colorBufferID = intBuffer.get(0);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, colorBufferID);
        gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertexColor.length * Float.BYTES, vertexColorBuffer, GL4.GL_STATIC_DRAW);
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 4, GL4.GL_FLOAT, false, 0, 0);

        intBuffer.rewind();
        gl.glGenBuffers(1, intBuffer);
        normalBufferID = intBuffer.get(0);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, normalBufferID);
        gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertexNormal.length * Float.BYTES, vertexNormalBuffer, GL4.GL_STATIC_DRAW);
        gl.glEnableVertexAttribArray(2);
        gl.glVertexAttribPointer(2, 3, GL4.GL_FLOAT, false, 0, 0);

        intBuffer.rewind();
        gl.glGenBuffers(1, intBuffer);
        vertexIndexBufferID = intBuffer.get(0);
        gl.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER, vertexIndexBufferID);
        gl.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, indexArr.length * Integer.BYTES, vertexIndexBuffer, GL4.GL_STATIC_DRAW);
    }

    @Override
    public void render(GL4 gl, Camera c) {
        if( shader == null ) {
            return;
        }

        shader.bindProgram(gl);
        gl.glBindVertexArray(vertexArrayID);

        int VPMatrixLoc = gl.glGetUniformLocation(shader.getProgramID(), "transform");
        Matrix4f vpMat = c.getViewProjection();
        vpMat.mul(transform);
        vpMat.get(VPMatrixArr);
        gl.glUniformMatrix4fv(VPMatrixLoc, 1, false, VPMatrixArr, 0);
        gl.glDrawElements(GL4.GL_QUADS, 4, GL4.GL_UNSIGNED_INT, 0);

        shader.unbindProgram(gl);
        gl.glBindVertexArray(0);
    }

    @Override
    public void destroy(GL4 gl) {
        IntBuffer buffer = Buffers.newDirectIntBuffer(3);
        buffer.put(vertexBufferID);
        buffer.put(colorBufferID);
        buffer.put(normalBufferID);
        buffer.put(vertexIndexBufferID);
        buffer.rewind();
        gl.glDeleteBuffers(4, buffer);

        buffer.rewind();
        buffer.put(vertexArrayID);
        buffer.rewind();
        gl.glDeleteVertexArrays(1, buffer);
    }

    @Override
    public void update() {

    }

    private Vector3f makeVector(float []vertexPositions, int idx1, int idx2)
    {
        Vector3f vec = new Vector3f();
        vec.x = vertexPositions[idx2] - vertexPositions[idx1];
        vec.y = vertexPositions[idx2+1] - vertexPositions[idx1+1];
        vec.z = vertexPositions[idx2+2] - vertexPositions[idx1+2];

        return vec;
    }

    private float[] computeNormals(float[] vertexData){
        float[] normals = new float[vertexData.length];
        for(int i=0; i<normals.length/12; i++){
            Vector3f vec1 = makeVector(vertexData, i*12, i*12+3);
            Vector3f vec2 = makeVector(vertexData, i*12, i*12+9);
            Vector3f normal = vec1.cross(vec2);
            for(int j=0; j<3; j++){
                normals[i*12 + j*3] = normal.x;
                normals[i*12 + j*3 + 1] = normal.y;
                normals[i*12 + j*3 + 2] = normal.z;
            }
        }
        return normals;
    }
}
