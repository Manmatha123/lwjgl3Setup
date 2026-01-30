package stripLine;


import entities.Camera;
import shader.ShaderProgram;
import toolbox.Maths;

import static org.lwjgl.opengl.GL20.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class DebugLineShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/stripLine/LineVertex.txt";
    private static final String FRAGMENT_FILE = "src/stripLine/LineFrag.txt";

    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_color;
private int dashSizeLoc;
private int gapSizeLoc;

    public DebugLineShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_color = super.getUniformLocation("color");

         dashSizeLoc = getUniformLocation("dashSize");
    gapSizeLoc  = getUniformLocation("gapSize");

    }

    // ================= LOAD METHODS =================

    public void loadDashSize(float size) {
    loadFloat(dashSizeLoc, size);
}

public void loadGapSize(float size) {
    loadFloat(gapSizeLoc, size);
}

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(location_projectionMatrix, projection);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadColor(Vector3f color) {
        super.loadVector(location_color, color);
    }
}
