package fbx;

import org.joml.Matrix4f;

import shader.ShaderProgram;

public class AnimatedShader extends ShaderProgram {

    private static final int MAX_BONES = 100;

    private int location_projection;
    private int location_view;
    private int location_transformation;
    private int location_bones;

    public AnimatedShader() {
        super(
            "/fbx/animatedVertex.txt",
            "/fbx/animatedFragment.txt"
        );
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
        location_projection = getUniformLocation("projection");
        location_view = getUniformLocation("view");
        location_transformation = getUniformLocation("transformation");
        location_bones = getUniformLocation("bones");
    }

    public void loadProjection(Matrix4f matrix) {
        loadMatrix(location_projection, matrix);
    }

    public void loadView(Matrix4f matrix) {
        loadMatrix(location_view, matrix);
    }

    public void loadTransformation(Matrix4f matrix) {
        loadMatrix(location_transformation, matrix);
    }

    public void loadBones(Matrix4f[] bones) {
        for (int i = 0; i < bones.length; i++) {
            loadMatrix(location_bones + i, bones[i]);
        }
    }
}
