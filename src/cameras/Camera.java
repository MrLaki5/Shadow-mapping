package cameras;

import org.joml.Matrix4f;

public class Camera {

    private Matrix4f view;
    private Matrix4f projection;
    private int angle;
    private int zoomV = 45;
    private float cameraHeight;

    public Camera(float width, float height){
        view  = new Matrix4f();
        projection = new Matrix4f();
        resizePerspective(width, height);
        update(0, 0);
    }

    public void update(int velocity, float heightC){
        if((angle+velocity)<0){
            angle = 360;
        }
        else {
            angle = (angle + velocity) % 361;
        }
        cameraHeight += heightC;
        if((cameraHeight)<-5){
            cameraHeight = -5;
        }
        else{
            if(cameraHeight>5){
                cameraHeight = 5;
            }
        }
        view.identity().translate(0, cameraHeight, -10.0f)
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
        projection.identity().perspective((float)Math.toRadians(zoomV), width/height, 0.1f, 100.0f);
    }

    public void zoom(int velocity, float width, float height){
        zoomV += velocity;
        if(zoomV<10){
            zoomV = 10;
        }
        else{
            if(zoomV>45){
                zoomV = 45;
            }
        }
        projection.identity().perspective((float)Math.toRadians(zoomV), width/height, 0.1f, 100.0f);
    }

}
