package objects;

import cameras.Camera;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import lights.Light;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Box extends SimpleObject{

    private int vertexArrayID;
    private int vertexBufferID;
    private int colorBufferID;
    private int normalBufferID;
    private int vertexIndexBufferID;
    private float VPMatrixArr[] = new float[16];
    private float modelMatixArr[] = new float[16];
    private float lightSpaceArr[] = new float[16];

    public Box(){
        super();
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
        for(int i=0; i<normals.length/18; i++){
            Vector3f vec1 = makeVector(vertexData, i*18, i*18+3);
            Vector3f vec2 = makeVector(vertexData, i*18, i*18+6);
            Vector3f normal = vec1.cross(vec2);
            for(int j=0; j<6; j++){
                normals[i*18 + j*3] = normal.x;
                normals[i*18 + j*3 + 1] = normal.y;
                normals[i*18 + j*3 + 2] = normal.z;
            }
        }
        return normals;
    }

    @Override
    public void render(GL4 gl, Camera camera, Light light) {
        if( shader == null ) {
            return;
        }

        shader.bindProgram(gl);
        gl.glBindVertexArray(vertexArrayID);
        gl.glActiveTexture(textureID);
        gl.glActiveTexture(textureDepthMapID);

        int modelLoc = gl.glGetUniformLocation(shader.getProgramID(), "model");
        int viewProjectionLoc = gl.glGetUniformLocation(shader.getProgramID(), "viewProjection");
        int lightSpaceLoc = gl.glGetUniformLocation(shader.getProgramID(), "lightSpace");
        int texMapLoc = gl.glGetUniformLocation(shader.getProgramID(), "texMap");
        int depthLoc = gl.glGetUniformLocation(shader.getProgramID(), "shadowMap");
        int lightPosLoc = gl.glGetUniformLocation(shader.getProgramID(), "lightPos");
        int viewPosLoc = gl.glGetUniformLocation(shader.getProgramID(), "viewPos");

        Matrix4f vpMat = camera.getViewProjection();
        Matrix4f lightSpaceMat = light.getViewProjection();

        Vector3f lightPos = light.GetPosition();
        Vector3f cameraPos = camera.GetPosition();

        vpMat.get(VPMatrixArr);
        transform.get(modelMatixArr);
        lightSpaceMat.get(lightSpaceArr);

        gl.glUniformMatrix4fv(viewProjectionLoc, 1, false, VPMatrixArr, 0);
        gl.glUniformMatrix4fv(modelLoc, 1, false, modelMatixArr, 0);
        gl.glUniformMatrix4fv(lightSpaceLoc, 1, false, lightSpaceArr, 0);
        gl.glUniform1i(texMapLoc, 0);
        gl.glUniform1i(depthLoc, 1);
        gl.glUniform3f(lightPosLoc, lightPos.x, lightPos.y, lightPos.z);
        gl.glUniform3f(viewPosLoc, cameraPos.x, cameraPos.y, cameraPos.z);

        gl.glActiveTexture(GL4.GL_TEXTURE0);
        gl.glBindTexture(GL4.GL_TEXTURE_2D, textureID);
        gl.glActiveTexture(GL4.GL_TEXTURE1);
        gl.glBindTexture(GL4.GL_TEXTURE_2D, textureDepthMapID);

        gl.glDrawElements(GL4.GL_TRIANGLES, 36, GL4.GL_UNSIGNED_INT, 0);

        shader.unbindProgram(gl);
        gl.glBindVertexArray(0);
    }

    @Override
    public void renderLightPerspective(GL4 gl, Light light) {
        if(shadowDepthShader == null){
            return;
        }

        gl.glCullFace(GL4.GL_FRONT);
        gl.glCullFace(GL4.GL_CW);


        shadowDepthShader.bindProgram(gl);
        gl.glBindVertexArray(vertexArrayID);
        Matrix4f vpMat = light.getViewProjection();
        vpMat.mul(transform);
        vpMat.get(VPMatrixArr);
        int VPMatrixLoc = gl.glGetUniformLocation(shadowDepthShader.getProgramID(), "transform");
        gl.glUniformMatrix4fv(VPMatrixLoc, 1, false, VPMatrixArr, 0);

        gl.glDrawElements(GL4.GL_TRIANGLES, 36, GL4.GL_UNSIGNED_INT, 0);
        shadowDepthShader.unbindProgram(gl);
        gl.glBindVertexArray(0);


        gl.glCullFace(GL4.GL_BACK);
    }

    @Override
    public void init(GL4 gl) {

        float a = 0.5f;
        /*float []vertexData = {
                0-a, 0-a, 0-a, //0
                0+a, 0-a, 0-a, //1
                0+a, 0+a, 0-a, //2
                0-a, 0+a, 0-a, //3
                0-a, 0-a, 0+a, //4
                0+a, 0-a, 0+a, //5
                0+a, 0+a, 0+a, //6
                0-a, 0+a, 0+a  //7
        };*/

        float []vertexData = {
                0+a, 0-a, 0+a, //1
                0+a, 0+a, 0+a, //2
                0-a, 0-a, 0+a, //0

                0-a, 0+a, 0+a, //3
                0-a, 0-a, 0+a, //0
                0+a, 0+a, 0+a, //2


                0+a, 0-a, 0-a, //5
                0+a, 0+a, 0-a, //6
                0+a, 0-a, 0+a, //1

                0+a, 0+a, 0+a, //2
                0+a, 0-a, 0+a, //1
                0+a, 0+a, 0-a, //6


                0-a, 0-a, 0-a, //4
                0-a, 0+a, 0-a, //7
                0+a, 0-a, 0-a, //5

                0+a, 0+a, 0-a, //6
                0+a, 0-a, 0-a, //5
                0-a, 0+a, 0-a, //7


                0-a, 0-a, 0+a, //0
                0-a, 0+a, 0+a, //3
                0-a, 0-a, 0-a, //4

                0-a, 0+a, 0-a, //7
                0-a, 0-a, 0-a, //4
                0-a, 0+a, 0+a, //3


                0+a, 0+a, 0+a, //2
                0+a, 0+a, 0-a, //6
                0-a, 0+a, 0+a, //3

                0-a, 0+a, 0-a, //7
                0-a, 0+a, 0+a, //3
                0+a, 0+a, 0-a, //6


                0-a, 0-a, 0-a, //4
                0+a, 0-a, 0-a, //5
                0-a, 0-a, 0+a, //0

                0+a, 0-a, 0+a, //1
                0-a, 0-a, 0+a, //0
                0+a, 0-a, 0-a, //5
        };

        /*float []vertexColor = {
                0.0f, 0.0f, 1.0f, 1.0f, //0
                0.0f, 1.0f, 0.0f, 1.0f, //1
                1.0f, 0.0f, 0.0f, 1.0f, //2
                1.0f, 1.0f, 0.0f, 1.0f, //3
                0.0f, 1.0f, 1.0f, 1.0f, //4
                1.0f, 0.0f, 1.0f, 1.0f, //5
                0.5f, 0.5f, 0.5f, 1.0f, //6
                0.1f, 0.7f, 0.3f, 1.0f  //7
        };*/

        float []vertexTexturePosArr = {
                1, 0,
                1, 1,
                0, 0,
                0, 1,
                0, 0,
                1, 1,

                1, 0,
                1, 1,
                0, 0,
                0, 1,
                0, 0,
                1, 1,

                1, 0,
                1, 1,
                0, 0,
                0, 1,
                0, 0,
                1, 1,

                1, 0,
                1, 1,
                0, 0,
                0, 1,
                0, 0,
                1, 1,

                1, 0,
                1, 1,
                0, 0,
                0, 1,
                0, 0,
                1, 1,

                1, 0,
                1, 1,
                0, 0,
                0, 1,
                0, 0,
                1, 1
        };

        /*int []indexArr = new int[] {
                0, 1, 2, 3,
                1, 5, 6, 2,
                5, 4, 7, 6,
                4, 0, 3, 7,
                3, 2, 6, 7,
                0, 4, 5, 1
        };*/

        int []indexArr = new int[] {
                0, 1, 2, 3, 4, 5,
                6, 7, 8, 9, 10, 11,
                12, 13, 14, 15, 16, 17,
                18, 19, 20, 21, 22, 23,
                24, 25, 26, 27, 28, 29,
                30, 31, 32, 33, 34, 35
        };

        float[] vertexNormal = computeNormals(vertexData);

        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGenVertexArrays(1, intBuffer);
        vertexArrayID = intBuffer.get(0);
        gl.glBindVertexArray(vertexArrayID);

        FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertexData, 0);
        FloatBuffer vertexTexPosBuffer = Buffers.newDirectFloatBuffer(vertexTexturePosArr, 0);
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
        gl.glBufferData(GL4.GL_ARRAY_BUFFER, vertexTexturePosArr.length * Float.BYTES, vertexTexPosBuffer, GL4.GL_STATIC_DRAW);
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 2, GL4.GL_FLOAT, false, 0, 0);

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
}
