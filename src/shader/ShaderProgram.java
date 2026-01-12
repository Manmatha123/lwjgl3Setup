package shader;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class ShaderProgram {

    int vertexShaderID;
    int fragShaderID;
    int programID;
    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexFile, String fragmentFile){

         vertexShaderID=loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
         fragShaderID=loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
         programID=GL20.glCreateProgram();
         GL20.glAttachShader(programID,vertexShaderID);
         GL20.glAttachShader(programID,fragShaderID);
         bindAttributes();
         GL20.glLinkProgram(programID);
         GL20.glValidateProgram(programID);
         getAllUniformLocations();
    }


    public void start(){
        GL20.glUseProgram(programID);
    }

    public void stop(){
        GL20.glUseProgram(0);
    }

    public void cleanUp(){
        stop();
        GL20.glDetachShader(programID,vertexShaderID);
        GL20.glDetachShader(programID,fragShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragShaderID);
        GL20.glDeleteProgram(programID);
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName){
        return GL20.glGetUniformLocation(programID,uniformName);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName){
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    protected void loadInt(int location, int value){GL20.glUniform1i(location, value); }

    protected void loadFloat(int location, float value){
        GL20.glUniform1f(location, value);
    }


    protected void loadVector(int location, Vector4f vector) {
        GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
    }

    protected void loadVector(int location, Vector3f vector){
        GL20.glUniform3f(location,vector.x,vector.y,vector.z);
    }

    protected void load2DVector(int location, Vector2f vector){
        GL20.glUniform2f(location,vector.x,vector.y);
    }

    protected void loadBoolean(int location, boolean value){
        float toLoad = 0;
        if(value){
            toLoad = 1;
        }
        GL20.glUniform1f(location, toLoad);
    }

protected void loadMatrix(int location, Matrix4f matrix) {
    matrix.get(matrixBuffer);
    GL20.glUniformMatrix4fv(location, false, matrixBuffer);
}


    private static int loadShader(String file, int type) {

    String shaderSource;
    try {
        shaderSource = java.nio.file.Files.readString(
                java.nio.file.Paths.get(file),
                java.nio.charset.StandardCharsets.UTF_8
        );
    } catch (IOException e) {
        throw new RuntimeException("Failed to read shader file: " + file, e);
    }

    int shaderID = GL20.glCreateShader(type);
    GL20.glShaderSource(shaderID, shaderSource);
    GL20.glCompileShader(shaderID);

    if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
        System.err.println("❌ Shader compile error in: " + file);
        System.err.println(GL20.glGetShaderInfoLog(shaderID, 1024));
        throw new IllegalStateException("Shader compilation failed");
    }

    return shaderID;
}


}
