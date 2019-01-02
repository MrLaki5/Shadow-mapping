package shaders.complete;

import com.jogamp.opengl.GL4;
import shaders.base.FragmentShader;
import shaders.base.GeometryShader;
import shaders.base.ShaderProgram;
import shaders.base.VertexShader;

abstract public class CompleteShader {

    private String name;

    private String vertexShaderSrc;
    private String fragmentShaderSrc;
    private String geometryShaderSrc;

    private Boolean shouldVertex;
    private Boolean shouldFragment;
    private Boolean shouldGeometry;

    private VertexShader vertexShader;
    private FragmentShader fragmentShader;
    private GeometryShader geometryShader;
    private ShaderProgram shaderProgram;

    public CompleteShader(GL4 gl, String name, Boolean shouldVertex, Boolean shouldFragment, Boolean shouldGeometry, String vertexShaderSrc, String fragmentShaderSrc, String geometryShaderSrc){
        this.name = name;
        this.shouldVertex = shouldVertex;
        this.shouldFragment = shouldFragment;
        this.shouldGeometry = shouldGeometry;

        this.vertexShaderSrc = vertexShaderSrc;
        this.fragmentShaderSrc = fragmentShaderSrc;
        this.geometryShaderSrc = geometryShaderSrc;

        shaderProgram = new ShaderProgram(name + "Program");
        if(shouldVertex){
            vertexShader = new VertexShader(name + "VS");
            vertexShader.setSource(vertexShaderSrc);
            shaderProgram.addShader(vertexShader);
        }
        if(shouldFragment){
            fragmentShader = new FragmentShader(name + "FS");
            fragmentShader.setSource(fragmentShaderSrc);
            shaderProgram.addShader(fragmentShader);
        }
        if(shouldGeometry){
            geometryShader = new GeometryShader(name + "GS");
            geometryShader.setSource(geometryShaderSrc);
            shaderProgram.addShader(geometryShader);
        }

        shaderProgram.build(gl);
        System.out.println(shaderProgram.getLog());
    }

    public int getProgramID() {
        return shaderProgram.getID();
    }

    public void bindProgram(GL4 gl) {
        shaderProgram.activate(gl);
    }

    public void unbindProgram(GL4 gl){
        shaderProgram.deactivate(gl);
    }

    public void deleteShader(GL4 gl){
        shaderProgram.delete(gl);
    }
}
