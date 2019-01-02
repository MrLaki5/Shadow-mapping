package shaders.Base;


import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.jogamp.opengl.GL4;


public class ShaderProgram {

    private int programID;
    private ArrayList<Shader> shaders = new ArrayList<>();
    private String name;
    private StringBuilder buildLog = new StringBuilder();
    public enum Status { PROGRAM_NOT_COMPLETE, PROGRAM_COMPLETE };
    private Status programStatus = Status.PROGRAM_NOT_COMPLETE;


    public ShaderProgram(String _name) {
        name = _name;
    }

    public ShaderProgram addShader(Shader s) {
        if(programStatus == Status.PROGRAM_NOT_COMPLETE) {
            shaders.add(s);
        }
        return this;
    }

    public void build(GL4 gl) {
        if(programStatus != Status.PROGRAM_NOT_COMPLETE){
            return;
        }

        if( programID == 0 ) {
            programID = gl.glCreateProgram();
        }

        buildLog = new StringBuilder();
        buildLog.append("Shader program " + name + " build log:\n");

        boolean allSuccessful = true;
        for(Shader s : shaders)
        {
            if( s.getStatus() == Shader.Status.UNCOMPILED || s.getStatus() == Shader.Status.UNITINITALIZED ) {
                s.buildShader(gl);
            }
            if( s.getStatus() != Shader.Status.COMPILED_SUCCESS ) {
                buildLog.append(s.getCompilationInfo()).append("\n");
                allSuccessful = false;
            }
        }

        if( !allSuccessful ) {
            buildLog.append("Shader program " + name + " not created!\n");
            gl.glDeleteProgram(programID);
            return;
        }

        for(Shader s : shaders) {
            gl.glAttachShader(programID, s.getID());
        }

        gl.glLinkProgram(programID);

        int []params = new int[1];
        gl.glGetProgramiv(programID, gl.GL_LINK_STATUS, params, 0);
        buildLog.append("Program " + name + " link stauts: ");
        if( params[0] == 1 ) {
            buildLog.append("SUCCESS");
            programStatus = Status.PROGRAM_COMPLETE;
        }
        else {
            buildLog.append("FAILURE");
        }

        buildLog.append(".\n");

        gl.glGetProgramiv(programID, gl.GL_INFO_LOG_LENGTH, params, 0);
        int infoLogLen = params[0];
        ByteBuffer programLog = ByteBuffer.allocate(infoLogLen);
        gl.glGetProgramInfoLog(programID, infoLogLen, null, programLog);
        buildLog.append(programLog.array());
    }

    public void delete(GL4 gl) {
        if( getID() == 0 ) {
            return;
        }
        for(Shader s : shaders ) {
            gl.glDetachShader(programID, s.getID());
            s.delete(gl);
        }
        gl.glDeleteProgram(programID);
        programID = 0;
    }

    public void activate(GL4 gl) {
        gl.glUseProgram(programID);
    }

    public static void deactivate(GL4 gl) {
        gl.glUseProgram(0);
    }

    public int getID() {
        return programID;
    }

    public Status getStatus() {
        return programStatus;
    }

    public String getLog() {
        return buildLog.toString();
    }
}
