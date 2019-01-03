import cameras.Camera;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import objects.Box;
import objects.Floor;
import objects.SimpleObject;
import org.joml.Matrix4f;
import shaders.complete.ObjectShader;

import java.util.ArrayList;
import java.util.List;

public class MainWindow implements GLEventListener, KeyListener {

    private FPSAnimator animator;

    private Camera camera;
    private float width = 800;
    private float height = 600;

    private ObjectShader objectShader;

    private List<SimpleObject> objectList = new ArrayList<>();

    public MainWindow(){
        camera = new Camera(width, height);
        GLProfile glp = GLProfile.getDefault();
        System.out.println(glp.getGLImplBaseClassName());
        System.out.println(glp.getImplName());
        System.out.println(glp.getName());
        System.out.println(glp.hasGLSL());

        GLCapabilities caps = new GLCapabilities(glp);
        caps.setAlphaBits(8);
        caps.setDepthBits(24);
        GLWindow window = GLWindow.create(caps);

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
        GL4 gl = drawable.getGL().getGL4bc();
        objectShader = new ObjectShader(gl);

        Box box1 = new Box();
        box1.setShaderProgram(objectShader);
        box1.setTransform(new Matrix4f().identity().translate(1, 0.5f ,2));
        objectList.add(box1);

        Box box2 = new Box();
        box2.setShaderProgram(objectShader);
        box2.setTransform(new Matrix4f().identity().translate(-1, 0.5f, 1).rotate((float)Math.toRadians(30), 0, 1, 0));
        objectList.add(box2);

        Box box3 = new Box();
        box3.setShaderProgram(objectShader);
        box3.setTransform(new Matrix4f().identity().scale(0.8f, 0.8f, 0.8f).translate(2.5f, 1.5f, -2.5f).rotate((float)Math.toRadians(25), 0, 1, 0).rotate((float)Math.toRadians(45), 1, 0, 1));
        objectList.add(box3);

        Box box4 = new Box();
        box4.setShaderProgram(objectShader);
        box4.setTransform(new Matrix4f().identity().translate(-2, 2f, -3).rotate((float)Math.toRadians(10), 0, 1, 0));
        objectList.add(box4);

        Floor floor = new Floor();
        floor.setShaderProgram(objectShader);
        objectList.add(floor);

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glEnable(GL4.GL_DEPTH_TEST);

        for(int i=0; i<objectList.size(); i++){
            objectList.get(i).init(gl);
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4bc();
        for(int i=0; i<objectList.size(); i++){
            objectList.get(i).destroy(gl);
        }
        objectShader.deleteShader(gl);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL4 gl = drawable.getGL().getGL4bc();
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
        for(int i=0; i<objectList.size(); i++){
            objectList.get(i).render(gl, camera);
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
                camera.update(1, 0);
                break;
            case KeyEvent.VK_D:
                camera.update(-1, 0);
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
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
