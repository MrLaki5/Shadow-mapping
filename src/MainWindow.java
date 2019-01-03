import cameras.Camera;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import objects.Box;
import objects.SimpleObject;
import shaders.complete.ObjectShader;

import java.util.ArrayList;
import java.util.List;

public class MainWindow implements GLEventListener {

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

        final FPSAnimator animator = new FPSAnimator(window, 60, true);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent arg0) {
                animator.stop();
                System.exit(0);
            }
        });

        window.addGLEventListener(this);

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
        objectList.add(box1);
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

    public static void main(String[] args) {
        new MainWindow();
    }
}
