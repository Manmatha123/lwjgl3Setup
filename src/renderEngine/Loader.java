package renderEngine;

import models.RawModel;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
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


  public RawModel loadAnimatedVAO(
    List<Vector3f>vertices,
        float[] positions,
        float[] texCoords,
        float[] normals,
        int[] indices,
        int[] boneIds,
        float[] weights
) {
    int vaoID = GL30.glGenVertexArrays();
    GL30.glBindVertexArray(vaoID);

    // ---------- Indices (EBO) ----------
    int ebo = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);

    IntBuffer indexBuffer = BufferUtils.createIntBuffer(indices.length);
    indexBuffer.put(indices).flip();

    GL15.glBufferData(
            GL15.GL_ELEMENT_ARRAY_BUFFER,
            indexBuffer,
            GL15.GL_STATIC_DRAW
    );

    // ---------- Vertex Attributes ----------
    storeFloatAttribute(0, 3, positions); // position
    storeFloatAttribute(1, 2, texCoords); // texCoords
    storeFloatAttribute(2, 3, normals);   // normals


    storeIntAttribute(3, 4, boneIds);     // ivec4 bone IDs
    storeFloatAttribute(4, 4, weights);   // vec4 weights

    // ---------- Unbind ----------
    GL30.glBindVertexArray(0);

    return new RawModel(vaoID, indices.length,vertices);
}


     private void storeFloatAttribute(int location, int size, float[] data) {
        int vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();

        GL15.glBufferData(
                GL15.GL_ARRAY_BUFFER,
                buffer,
                GL15.GL_STATIC_DRAW
        );

        GL20.glVertexAttribPointer(
                location,
                size,
                GL11.GL_FLOAT,
                false,
                0,
                0
        );
        GL20.glEnableVertexAttribArray(location);
    }

    // IMPORTANT: integer attribute
    private void storeIntAttribute(int location, int size, int[] data) {
        int vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data).flip();

        GL15.glBufferData(
                GL15.GL_ARRAY_BUFFER,
                buffer,
                GL15.GL_STATIC_DRAW
        );

        GL30.glVertexAttribIPointer(
                location,
                size,
                GL11.GL_INT,
                0,
                0
        );
        GL20.glEnableVertexAttribArray(location);
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
