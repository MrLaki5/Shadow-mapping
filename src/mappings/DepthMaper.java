package mappings;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import lights.Light;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class DepthMaper {

    private int depthMapFrameBufferObjectID;
    private int depthMapTexture;

    private int shadow_width = 1024;
    private int shadow_height = 1024;

    public DepthMaper(GL4 gl){
        //Create new frame buffer for depth map
        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGenFramebuffers(1, intBuffer);
        depthMapFrameBufferObjectID = intBuffer.get(0);
        //Create texture for depth buffer
        intBuffer.rewind();
        gl.glGenTextures(1, intBuffer);
        depthMapTexture = intBuffer.get(0);
        gl.glBindTexture(GL4.GL_TEXTURE_2D, depthMapTexture);
        gl.glTexImage2D(GL4.GL_TEXTURE_2D, 0, GL4.GL_DEPTH_COMPONENT,
                shadow_width, shadow_height, 0, GL4.GL_DEPTH_COMPONENT, GL4.GL_FLOAT, null);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_NEAREST);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_NEAREST);

        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_CLAMP_TO_BORDER);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_CLAMP_TO_BORDER);
        float borderColor[] = { 1.0f, 1.0f, 1.0f, 1.0f };
        FloatBuffer tempBuff = FloatBuffer.wrap(borderColor);
        gl.glTexParameterfv(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_BORDER_COLOR, tempBuff);

        //gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_REPEAT);
        //gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_REPEAT);
        //Bind created texture to created frame buffer
        gl.glBindFramebuffer(GL4.GL_FRAMEBUFFER, depthMapFrameBufferObjectID);
        gl.glFramebufferTexture2D(GL4.GL_FRAMEBUFFER, GL4.GL_DEPTH_ATTACHMENT, GL4.GL_TEXTURE_2D, depthMapTexture, 0);
        gl.glDrawBuffer(GL4.GL_NONE);
        gl.glReadBuffer(GL4.GL_NONE);
        gl.glBindFramebuffer(GL4.GL_FRAMEBUFFER, 0);
    }

    public void render(GL4 gl){
        gl.glViewport(0, 0, shadow_width, shadow_height);
        gl.glBindFramebuffer(GL4.GL_FRAMEBUFFER, depthMapFrameBufferObjectID);
        gl.glClear(GL4.GL_DEPTH_BUFFER_BIT);
    }

    public void dispose(GL4 gl){
        int tempArr[] = new int[1];
        tempArr[0] = depthMapTexture;
        gl.glDeleteTextures(tempArr.length, IntBuffer.wrap(tempArr));

        tempArr[0] = depthMapFrameBufferObjectID;
        gl.glDeleteFramebuffers(tempArr.length, IntBuffer.wrap(tempArr));
    }

    public int getDepthMapTexture(){
        return depthMapTexture;
    }

}
