package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TextureModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import shader.StaticShader;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class MasterRenderer {

    public static final float FOV = 70f;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 100000f;

    private static final float RED = 1f;
    private static final float GREEN = 1f;
    private static final float BLUE = 1f;

    private final StaticShader shader;
    private final EntityRenderer renderer;
    private final Matrix4f projectionMatrix;

    private final Map<TextureModel, List<Entity>> entities = new HashMap<>();

    /* ================= CONSTRUCTOR ================= */

    public MasterRenderer(Loader loader, int windowWidth, int windowHeight) {
        enableCulling();
        projectionMatrix = createProjectionMatrix(windowWidth, windowHeight);
        shader = new StaticShader();
        renderer = new EntityRenderer(shader, projectionMatrix);
    }

    /* ================= RENDER ================= */

    public void render(Light sun, Camera camera) {
        prepare();

        shader.start();
        shader.loadClipPlane(new Vector4f(0, -1, 0, 1));
        shader.connectTextureUnits();
        shader.loadSkyColour(RED, GREEN, BLUE);
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        shader.loadCameraPosition(camera);

        renderer.render(entities);

        shader.stop();
        entities.clear();
    }

    public void renderRaw(RawModel model) {
        shader.start();
        prepare();

        glBindVertexArray(model.getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.stop();
    }

    /* ================= ENTITY BATCHING ================= */

    public void processEntity(Entity entity) {
        TextureModel model = entity.getModel();
        entities.computeIfAbsent(model, k -> new ArrayList<>()).add(entity);
    }

    /* ================= PREPARE ================= */

    private void prepare() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glEnable(GL_DEPTH_CLAMP);

        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glActiveTexture(GL_TEXTURE5);
        glBindTexture(GL_TEXTURE_2D, getShadowMapTexture());
    }

    /* ================= PROJECTION ================= */

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

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    /* ================= OPENGL STATE ================= */

    public static void enableCulling() {
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    public static void disableCulling() {
        glDisable(GL_CULL_FACE);
    }

    /* ================= CLEANUP ================= */

    public void cleanUp() {
        shader.cleanUp();
    }

    /* ================= PLACEHOLDER ================= */
    // Replace with your real shadow system
    private int getShadowMapTexture() {
        return 0;
    }
}
