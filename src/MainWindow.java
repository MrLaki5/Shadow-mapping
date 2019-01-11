import cameras.Camera;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import image.PNGLoader;
import lights.Light;
import mappings.DepthMaper;
import objects.*;
import org.joml.Matrix4f;
import shaders.complete.DepthMapDrawShader;
import shaders.complete.LightDepthShader;
import shaders.complete.LightSpaceShader;
import shaders.complete.ObjectShader;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class MainWindow implements GLEventListener, KeyListener {

    private boolean rotateCorL = true;

    private FPSAnimator animator;

    private Camera camera;
    private Light light;
    private float width = 800;
    private float height = 600;

    private final int textureID[] = new int [2];

    private ObjectShader objectShader;
    private LightSpaceShader lightSpaceShader;
    private LightDepthShader lightDepthShader;
    private DepthMapDrawShader depthMapDrawShader;

    private DepthMaper depthMaper;

    private List<SimpleObject> objectList = new ArrayList<>();

    public MainWindow(){
        //GLProfile glp = GLProfile.getDefault();
        if(!GLProfile.isAvailable(GLProfile.GL4ES3)){
            System.out.println("OpenGL4 not found!");
            System.exit(2);
            return;
        }
        GLProfile glp = GLProfile.getGL4ES3();
        System.out.println(glp.getGLImplBaseClassName());
        System.out.println(glp.getImplName());
        System.out.println(glp.getName());
        System.out.println(glp.hasGLSL());

        GLCapabilities caps = new GLCapabilities(glp);
        caps.setAlphaBits(8);
        caps.setDepthBits(24);
        caps.setDoubleBuffered(true);
        GLWindow window = GLWindow.create(caps);

        camera = new Camera(width, height);
        light = new Light();

        animator = new FPSAnimator(window, 60, true);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent arg0) {
                animator.stop();
                System.exit(0);
            }
        });

        window.addGLEventListener(this);
        window.addKeyListener(this);
        window.setSize((int)width, (int)height);
        window.setTitle("Shadow mapping");
        window.setVisible(true);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();
        objectShader = new ObjectShader(gl);
        lightSpaceShader = new LightSpaceShader(gl);
        lightDepthShader = new LightDepthShader(gl);
        depthMapDrawShader = new DepthMapDrawShader(gl);

        PNGLoader pngLoader = new PNGLoader();
        pngLoader.LoadGLTexture(drawable, "img/wood.png", textureID, 0, GL.GL_LINEAR, GL.GL_LINEAR);
        pngLoader.LoadGLTexture(drawable, "img/sand.png", textureID, 1, GL.GL_LINEAR, GL.GL_LINEAR);

        depthMaper = new DepthMaper(gl);

        Box box1 = new Box();
        box1.setTextureID(textureID[0]);
        box1.setTextureDepthMapID(depthMaper.getDepthMapTexture());
        box1.setShaderProgram(objectShader);
        box1.setShadowDepthShader(lightDepthShader);
        box1.setTransform(new Matrix4f().identity().translate(1, 0.5f ,2));
        objectList.add(box1);

        Box box2 = new Box();
        box2.setTextureID(textureID[0]);
        box2.setTextureDepthMapID(depthMaper.getDepthMapTexture());
        box2.setShaderProgram(objectShader);
        box2.setShadowDepthShader(lightDepthShader);
        box2.setTransform(new Matrix4f().identity().translate(-1, 0.5f, 1).rotate((float)Math.toRadians(30), 0, 1, 0));
        objectList.add(box2);

        Box box3 = new Box();
        box3.setTextureID(textureID[0]);
        box3.setTextureDepthMapID(depthMaper.getDepthMapTexture());
        box3.setShaderProgram(objectShader);
        box3.setShadowDepthShader(lightDepthShader);
        box3.setTransform(new Matrix4f().identity().scale(0.8f, 0.8f, 0.8f).translate(2.5f, 1.5f, -2.5f).rotate((float)Math.toRadians(25), 0, 1, 0).rotate((float)Math.toRadians(45), 1, 0, 1));
        objectList.add(box3);

        Box box4 = new Box();
        box4.setTextureID(textureID[0]);
        box4.setTextureDepthMapID(depthMaper.getDepthMapTexture());
        box4.setShaderProgram(objectShader);
        box4.setShadowDepthShader(lightDepthShader);
        box4.setTransform(new Matrix4f().identity().translate(-2, 2f, -3).rotate((float)Math.toRadians(10), 0, 1, 0));
        objectList.add(box4);

        Floor floor = new Floor();
        floor.setTextureID(textureID[1]);
        floor.setTextureDepthMapID(depthMaper.getDepthMapTexture());
        floor.setShaderProgram(objectShader);
        floor.setShadowDepthShader(lightDepthShader);
        objectList.add(floor);

        LightSpace lightSpace = new LightSpace();
        lightSpace.setShaderProgram(lightSpaceShader);
        objectList.add(lightSpace);

        DepthMapBillboard depthMapBillboard = new DepthMapBillboard();
        depthMapBillboard.setShaderProgram(depthMapDrawShader);
        depthMapBillboard.setTextureID(depthMaper.getDepthMapTexture());
        objectList.add(depthMapBillboard);

        gl.glClearColor(0.85f, 0.85f, 0.85f, 1f);
        gl.glEnable(GL4.GL_DEPTH_TEST);

        for(int i=0; i<objectList.size(); i++){
            objectList.get(i).init(gl);
        }

        gl.glEnable(GL4.GL_CULL_FACE);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();
        for(int i=0; i<objectList.size(); i++){
            objectList.get(i).destroy(gl);
        }
        objectShader.deleteShader(gl);
        lightSpaceShader.deleteShader(gl);
        lightDepthShader.deleteShader(gl);
        depthMapDrawShader.deleteShader(gl);
        gl.glDeleteTextures(textureID.length, IntBuffer.wrap(textureID));
        depthMaper.dispose(gl);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4();

        depthMaper.render(gl);

        //gl.glCullFace(GL4.GL_FRONT);
        //gl.glCullFace(GL4.GL_CW);

        for(int i=0; i<objectList.size(); i++){
            objectList.get(i).renderLightPerspective(gl, light);
        }

        //gl.glCullFace(GL4.GL_BACK);


        gl.glBindFramebuffer(GL4.GL_FRAMEBUFFER, 0);

        gl.glViewport(0, 0, (int)width, (int)height);
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
        for(int i=0; i<objectList.size(); i++){
            objectList.get(i).render(gl, camera, light);
        }

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        drawable.getGL().glViewport(x, y, width, height);
        camera.resizePerspective(width, height);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()){
            case KeyEvent.VK_A:
                if(rotateCorL) {
                    camera.update(1, 0);
                }
                else{
                    light.update(1);
                }
                break;
            case KeyEvent.VK_D:
                if(rotateCorL) {
                    camera.update(-1, 0);
                }
                else{
                    light.update(-1);
                }
                break;
            case KeyEvent.VK_W:
                camera.zoom(-1, width, height);
                break;
            case KeyEvent.VK_S:
                camera.zoom(1, width, height);
                break;
            case KeyEvent.VK_Q:
                camera.update(0, 0.1f);
                break;
            case KeyEvent.VK_E:
                camera.update(0 , -0.1f);
                break;
            case KeyEvent.VK_ESCAPE:
                animator.stop();
                System.exit(0);
                break;
            case KeyEvent.VK_R:
                if(rotateCorL){
                    rotateCorL = false;
                }
                else{
                    rotateCorL = true;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
