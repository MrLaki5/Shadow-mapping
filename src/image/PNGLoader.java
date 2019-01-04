package image;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class PNGLoader {

    private ByteBuffer pixelData;
    private int imageWidth, imageHeight;

    private boolean LoadPNG(String filename){
        boolean status = false;
        InputStream in = null;
        try {
            in = new FileInputStream(filename);
            PNGDecoder decoder = new PNGDecoder(in);
            imageWidth = decoder.getWidth();
            imageHeight = decoder.getHeight();
            pixelData = ByteBuffer.allocate(4 * imageWidth * imageHeight);
            decoder.decodeFlipped(pixelData, imageWidth * 4, PNGDecoder.Format.RGBA);
            pixelData.flip();
            status = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return status;
    }

    public boolean LoadGLTexture(GLAutoDrawable drawable, String textureName, int[] storage, int ind, int minFilter, int magFilter){
        boolean status = false;
        GL2 gl = drawable.getGL().getGL2();
        if(LoadPNG(textureName)){
            gl.glGenTextures(1, storage, ind);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, storage[ind]);
            if(minFilter == GL.GL_NEAREST_MIPMAP_NEAREST || minFilter == GL.GL_NEAREST_MIPMAP_LINEAR
                    || minFilter == GL.GL_LINEAR_MIPMAP_NEAREST  || minFilter == GL.GL_LINEAR_MIPMAP_LINEAR){
                gl.glTexParameteri(GL.GL_TEXTURE_2D, GL2.GL_GENERATE_MIPMAP, GL.GL_TRUE); // Ako je GL_TRUE, onda ce se sve mipmap slike ponovo izgenerisati ukliko se promeni tekstura nivoa 0
            }
            gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGBA, imageWidth, imageHeight,
                    0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, pixelData);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, minFilter);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, magFilter);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
            gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
            status = true;
        }
        return status;
    }

}
