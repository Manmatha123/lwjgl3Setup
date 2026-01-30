package stripLine.dashPath;



import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.*;

import entities.Camera;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class DashedPathRenderer {

    private int vao;
    private int vbo;
    private int vertexCount;
    private DashedShader shader;

    public DashedPathRenderer(List<Vector3f> pathPoints) {
        shader = new DashedShader();

        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        FloatBuffer buffer = org.lwjgl.BufferUtils.createFloatBuffer(pathPoints.size() * 3);
        for (Vector3f v : pathPoints) {
            buffer.put(v.x).put(v.y).put(v.z);
        }
        buffer.flip();

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glBindVertexArray(0);

        vertexCount = pathPoints.size();
    }

    public void render(Matrix4f projection, Camera camera) {
        shader.start();

        shader.loadProjection(projection);
        shader.loadViewMatrix(camera);;

        shader.loadColor(new Vector3f(1, 0, 0));
        shader.loadDash(0.3f, 0.3f);
glLineWidth(5.0f);
        glBindVertexArray(vao);
        glDrawArrays(GL_LINE_STRIP, 0, vertexCount);
        glBindVertexArray(0);

        shader.stop();
    }

    public void cleanUp() {
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
        shader.cleanUp();
    }
}
