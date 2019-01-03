package cameras;

import org.joml.Matrix4f;

public class Camera {

    private Matrix4f view;
    private Matrix4f projection;
    private int angle;

    public Camera(float width, float height){
        view  = new Matrix4f();
        projection = new Matrix4f();
        resizePerspective(width, height);
        update(0);
    }

    public void update(int velocity){
        if((angle+velocity)<0){
            angle = 360;
        }
        else {
            angle = (angle + velocity) % 360;
        }
        view.identity().translate(0, 5, -20.0f)
                .rotate((float)Math.toRadians(30), 1, 0, 0)
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

    public void resizePerspective(float width, float height){
        projection.identity().perspective((float)Math.toRadians(45.0), width/height, 0.1f, 100.0f);
    }

}
