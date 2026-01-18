package FBX3;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

import models.RawModel;
import textures.ModelTexture;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import entities.Camera;

public class AnimatedRenderer {

    private AnimatedShader shader;
    public static final float FOV = 70f;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 100000f;

    public AnimatedRenderer() {
        try {
            shader = new AnimatedShader();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void render(AnimatedModel model,
                       Camera camera,
                       ModelTexture modelTexture,
                       Matrix4f modelMatrix) {

        shader.start();
        Matrix4f projection=createProjectionMatrix(2080, 1080);
        shader.loadModelMarix(modelMatrix);
        shader.loadViewMatrix(camera);
        shader.loadProjectionMatrix(projection);

        // 🔴 upload bone matrices
        for (int i = 0; i < model.boneCount; i++) {
            shader.loadMatrix( i, model.bones[i].finalTransform);
        }

        // draw meshes
        for (RawModel raw : model.rawModels) {

            glBindVertexArray(raw.getVaoID());
            glEnableVertexAttribArray(0); // position
            glEnableVertexAttribArray(1); // texcoord
            glEnableVertexAttribArray(2); // normal
            glEnableVertexAttribArray(3); // bone IDs
            glEnableVertexAttribArray(4); // weights
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelTexture.getID());


            glDrawElements(
                    GL_TRIANGLES,
                    raw.getVertexCount(),
                    GL_UNSIGNED_INT,
                    0
            );

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glDisableVertexAttribArray(3);
            glDisableVertexAttribArray(4);

            glBindVertexArray(0);
        }

        shader.stop();
    }


        private Matrix4f createProjectionMatrix(int width, int height) {
        float aspectRatio = (float) width / (float) height;
        return new Matrix4f()
                .perspective(
                        (float) Math.toRadians(FOV),
                        aspectRatio,
                        NEAR_PLANE,
                        FAR_PLANE
                );
    }
}

