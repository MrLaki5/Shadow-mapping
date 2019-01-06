package lights;

import org.joml.Matrix4f;

public class Light {

    private Matrix4f view;
    private Matrix4f projection;

    private int angle;


    public Light(){
        view  = new Matrix4f();
        projection = new Matrix4f();
        projection.identity().ortho(-5, 5, -5, 5, -5f, 5);
        update(0);
    }

    public void update(int velocity){
        if((angle+velocity)<0){
            angle = 360;
        }
        else {
            angle = (angle + velocity) % 361;
        }
        view.identity().translate(0, -2, 0)
                .rotate((float)Math.toRadians(35), 1, 0, 0)
                .rotate((float)Math.toRadians(angle), 0, 1, 0);
    }

    public Matrix4f getView(){
        return view;
    }

    public Matrix4f getProjection(){
        return projection;
    }

    public Matrix4f getViewProjection(){
        return new Matrix4f().mul(projection).mul(view);
    }
}
