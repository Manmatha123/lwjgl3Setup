package weather;



import org.joml.Matrix4f;

import shader.ShaderProgram;

public class ParticleShader extends ShaderProgram {

    private static final String VERTEX = "src/weather/particleVertex.txt";
    private static final String FRAGMENT = "src/weather/particleFragment.txt";

    private int location_modelViewMatrix;
    private int location_projectionMatrix;

    public ParticleShader() {
        super(VERTEX, FRAGMENT);
    }

    @Override
    protected void getAllUniformLocations() {
        location_modelViewMatrix = getUniformLocation("modelViewMatrix");
        location_projectionMatrix = getUniformLocation("projectionMatrix");
    }

    public void loadModelViewMatrix(Matrix4f matrix) {
        loadMatrix(location_modelViewMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        loadMatrix(location_projectionMatrix, matrix);
    }

    @Override
    protected void bindAttributes() {

    }
}
