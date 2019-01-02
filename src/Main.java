import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

public class Main {

    public static void main(String[] args) {
        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        capabilities.setAlphaBits(8);
        capabilities.setDepthBits(24);
        capabilities.setDoubleBuffered(true);

        GLWindow window = GLWindow.create(capabilities);

        FPSAnimator animator = new FPSAnimator(window, 60, false);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowDestroyNotify(WindowEvent e) {
                animator.stop();
                System.exit(0);
            }
        });




        window.setSize(600, 400);
        window.setTitle("Box");
        window.setVisible(true);
        animator.start();

    }
}
