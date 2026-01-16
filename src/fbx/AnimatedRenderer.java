package fbx;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entities.Camera;
import window.WindowManager;

public class AnimatedRenderer {

    private AnimatedShader shader;

    public AnimatedRenderer(Matrix4f projectionMatrix) {
        shader = new AnimatedShader();
        shader.start();
        shader.loadProjection(projectionMatrix);
        shader.stop();
    }

    public void render(AnimatedEntity entity, Camera camera) {

        shader.start();
        shader.loadView(camera.getViewMatrix());

        Animator animator = entity.getAnimator();
        animator.update(WindowManager.getDelta());

        shader.loadBones(animator.boneMatrices);

        for (AnimatedMesh mesh : entity.getModel().meshes) {

            Matrix4f transform =
                new Matrix4f(entity.getTransformationMatrix())
                    .mul(mesh.nodeTransform);

            shader.loadTransformation(transform);

            GL30.glBindVertexArray(mesh.rawModel.getVaoID());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);
            GL20.glEnableVertexAttribArray(3);
            GL20.glEnableVertexAttribArray(4);

            GL11.glDrawElements(
                GL11.GL_TRIANGLES,
                mesh.rawModel.getVertexCount(),
                GL11.GL_UNSIGNED_INT,
                0
            );
        }

        shader.stop();
    }
}
