package FBX3;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix4f;

public class AnimatedRenderer {

    private ShaderProgram shader;

    public AnimatedRenderer(ShaderProgram shader) {
        this.shader = shader;
    }

    public void render(AnimatedModel model, Matrix4f MVP) {

        shader.bind();
        shader.setUniform("MVP", MVP); // set main transformation

        // Send bone transforms to shader
        for (int i = 0; i < model.boneCount; i++) {
            shader.setUniform("bones[" + i + "]", model.bones[i].finalTransform);
        }

        // Render all meshes
        for (AnimatedMesh mesh : model.meshes) {
            glBindVertexArray(mesh.vao);
            glDrawArrays(GL_TRIANGLES, 0, mesh.vertexCount);
        }

        glBindVertexArray(0);
        shader.unbind();
    }
}
