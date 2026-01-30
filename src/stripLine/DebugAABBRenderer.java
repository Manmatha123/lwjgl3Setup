package stripLine;


import entities.Camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL15C.*;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.*;

public class DebugAABBRenderer {

    public static final float FOV = 70f;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 100000f;


    private final int vao;
    private final int vbo;
    private final DebugLineShader shader;

    public DebugAABBRenderer() {

        shader = new DebugLineShader();

        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        // max vertices used
        glBufferData(GL_ARRAY_BUFFER, 24 * 3 * Float.BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);
    }



    public void render(AABB box, Camera camera) {

      

        shader.start();

        Matrix4f projection=createProjectionMatrix(2080, 1080);
        shader.loadProjectionMatrix(projection);
        shader.loadViewMatrix(camera);
        shader.loadColor(new Vector3f(1, 0, 0)); // RED


        // DASH SETTINGS
shader.loadDashSize(0.25f); // dash length
shader.loadGapSize(0.25f);  // gap length

glLineWidth(5.0f);
glBindVertexArray(vao);

// bottom
uploadVertices(bottomLoop(box));
glDrawArrays(GL_LINE_LOOP, 0, 4);

// top
uploadVertices(topLoop(box));
glDrawArrays(GL_LINE_LOOP, 0, 4);

// vertical
uploadVertices(verticalLines(box));
glDrawArrays(GL_LINES, 0, 8);

glBindVertexArray(0);
shader.stop();

    }

    private void uploadVertices(float[] data) {
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferSubData(GL_ARRAY_BUFFER, 0, data);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    // ---------------- STRIP DATA ----------------

    private float[] bottomLoop(AABB b) {
        Vector3f min = b.min;
        Vector3f max = b.max;

        return new float[]{
            min.x, min.y, min.z,
            max.x, min.y, min.z,
            max.x, min.y, max.z,
            min.x, min.y, max.z
        };
    }

    private float[] topLoop(AABB b) {
        Vector3f min = b.min;
        Vector3f max = b.max;

        return new float[]{
            min.x, max.y, min.z,
            max.x, max.y, min.z,
            max.x, max.y, max.z,
            min.x, max.y, max.z
        };
    }

    private float[] verticalLines(AABB b) {
        Vector3f min = b.min;
        Vector3f max = b.max;

        return new float[]{
            min.x, min.y, min.z,   min.x, max.y, min.z,
            max.x, min.y, min.z,   max.x, max.y, min.z,
            max.x, min.y, max.z,   max.x, max.y, max.z,
            min.x, min.y, max.z,   min.x, max.y, max.z
        };
    }


    // ===================== CLEANUP =====================

    public void cleanUp() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
        shader.cleanUp();
    }

    private Matrix4f createProjectionMatrix(int width, int height) {
        float aspectRatio = (float) width / (float) height;
        return new Matrix4f()
                .perspective(
                        (float) Math.toRadians(FOV),
                        aspectRatio,
                        NEAR_PLANE,
                        FAR_PLANE);
    }
}
