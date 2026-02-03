package weather;



import models.RawModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;

import entities.Camera;
import renderEngine.Loader;
import toolbox.Maths;

import java.util.List;
import java.util.Map;

public class ParticleRenderer {

    private static final float[] VERTICES = {
            -0.5f,  0.5f,
            -0.5f, -0.5f,
             0.5f,  0.5f,
             0.5f, -0.5f
    };

    private RawModel quad;
    private ParticleShader shader;

    protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix) {
        quad = loader.loadToVAO(VERTICES, 2);
        shader = new ParticleShader();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    protected void render(Map<ParticleTexture, List<Particle>> particles,
                          Camera camera) {

        Matrix4f viewMatrix = Maths.createViewMatrix(camera);

        prepare();

        for (ParticleTexture texture : particles.keySet()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());

            for (Particle p : particles.get(texture)) {
                updateModelViewMatrix(p.getPosition(), p.getScale(), viewMatrix,WeatherSystem.isRainActive);
                GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
            }
        }

        finish();
    }



private void updateModelViewMatrix(Vector3f pos,
                                   float scale,
                                   Matrix4f viewMatrix,
                                   boolean stretch) {

    Matrix4f model = new Matrix4f()
            .translate(pos);

    if (stretch) {
        model.scale(scale * 0.6f, scale * 6.0f, scale); // rain
    } else {
        model.scale(scale); // snow
    }

    Matrix4f modelView = new Matrix4f(viewMatrix).mul(model);
    shader.loadModelViewMatrix(modelView);
}


    private void prepare() {
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        
    }

    private void finish() {
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    protected void cleanUp() {
        shader.cleanUp();
    }
}
