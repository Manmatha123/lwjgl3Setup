package renderEngine;

import models.RawModel;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

public class Loader {

    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();
    private final List<Integer> textures = new ArrayList<>();

    /* ================= VAO ================= */

    public RawModel loadToVAO(List<Vector3f> vertices,float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();
        // return new RawModel(vaoID, indices.length);
        return new RawModel(vaoID,indices.length, vertices);
    }

    public RawModel loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length / dimensions);
    }

    /* ================= TEXTURE ================= */

    public int loadTexture(String filePath) {
        int textureID;

        try (MemoryStack stack = stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            stbi_set_flip_vertically_on_load(true);
            ByteBuffer image = stbi_load(filePath, width, height, channels, 4);

            if (image == null) {
                throw new RuntimeException("Failed to load texture: " + filePath);
            }

            textureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureID);

            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    width.get(0),
                    height.get(0),
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    image
            );

            glGenerateMipmap(GL_TEXTURE_2D);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            stbi_image_free(image);
            textures.add(textureID);
        }

        return textureID;
    }

    /* ================= CLEANUP ================= */

    public void cleanUp() {
        for (int vao : vaos) glDeleteVertexArrays(vao);
        for (int vbo : vbos) glDeleteBuffers(vbo);
        for (int tex : textures) glDeleteTextures(tex);
    }

    /* ================= INTERNAL ================= */

    private int createVAO() {
        int vaoID = glGenVertexArrays();
        vaos.add(vaoID);
        glBindVertexArray(vaoID);
        return vaoID;
    }

    private void storeDataInAttributeList(int attribute, int size, float[] data) {
        int vboID = glGenBuffers();
        vbos.add(vboID);

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();

        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glVertexAttribPointer(attribute, size, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(attribute);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = glGenBuffers();
        vbos.add(vboID);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
    }

    private void unbindVAO() {
        glBindVertexArray(0);
    }
}
