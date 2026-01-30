package stripLine.dashPath;


import org.joml.Matrix4f;
import org.joml.Vector3f;

import entities.Camera;
import shader.ShaderProgram;
import toolbox.Maths;

public class DashedShader extends ShaderProgram {

    private static final String VERTEX = "src/stripLine/dashPath/pathVert.txt";
    private static final String FRAGMENT = "src/stripLine/dashPath/pathFrag.txt";
    private int loc_projection;
    private int loc_view;
    private int loc_color;
    private int loc_dashSize;
    private int loc_gapSize;

    public DashedShader() {
        super(VERTEX, FRAGMENT);
    }

    @Override
    protected void getAllUniformLocations() {
        loc_projection = getUniformLocation("projection");
        loc_view = getUniformLocation("view");
        loc_color = getUniformLocation("color");
        loc_dashSize = getUniformLocation("dashSize");
        loc_gapSize = getUniformLocation("gapSize");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    public void loadProjection(Matrix4f m) {
        loadMatrix(loc_projection, m);
    }


        public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(loc_view, viewMatrix);
    }


    public void loadColor(Vector3f c) {
        loadVector(loc_color, c);
    }

    public void loadDash(float dash, float gap) {
        loadFloat(loc_dashSize, dash);
        loadFloat(loc_gapSize, gap);
    }
}
