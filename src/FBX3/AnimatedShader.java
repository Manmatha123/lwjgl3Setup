package FBX3;

import org.joml.Matrix4f;

import entities.Camera;
import shader.ShaderProgram;
import toolbox.Maths;

public class AnimatedShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/FBX3/animationVertex.txt";
    private static final String FRAGMENT_FILE = "src/FBX3/animationfrag.txt";

    private int [] location_bones;
    private int location_projection;
    private int location_viewMatrix;
    private int location_modelMatrix;

    public AnimatedShader() throws Exception {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
        bindAttribute(1, "texCoord");
        bindAttribute(2, "normal");
        bindAttribute(3, "boneIds");
        bindAttribute(4, "weights");
    }

    @Override
    protected void getAllUniformLocations() {
       location_projection= getUniformLocation("projection");
        location_viewMatrix=getUniformLocation("view");
       location_modelMatrix= getUniformLocation("model");

        location_bones=new int[100];

        for (int i = 0; i < 100; i++) {
           location_bones[i]=getUniformLocation("bones[" + i + "]");
        }
    }

        public void loadProjectionMatrix(Matrix4f projMatrix){
        super.loadMatrix(location_projection, projMatrix);
    }
        public void loadModelMarix(Matrix4f modelMatrix){
        super.loadMatrix(location_modelMatrix, modelMatrix);
    }
    public void loadViewMatrix(Camera camera){
                Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadMatrix(int index, Matrix4f matrix) {
       super.loadMatrix(location_bones[index], matrix);
    }
}
