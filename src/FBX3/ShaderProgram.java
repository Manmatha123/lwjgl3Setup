package FBX3;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;

public class ShaderProgram {

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderProgram(String vertexCode, String fragmentCode) throws Exception {
        programId = glCreateProgram();
        if (programId == 0) throw new Exception("Could not create Shader");

        vertexShaderId = createShader(vertexCode, GL_VERTEX_SHADER);
        fragmentShaderId = createShader(fragmentCode, GL_FRAGMENT_SHADER);

        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0)
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId));

        glValidateProgram(programId);
    }

    private int createShader(String code, int type) throws Exception {
        int shaderId = glCreateShader(type);
        if (shaderId == 0) throw new Exception("Error creating shader. Type: " + type);

        glShaderSource(shaderId, code);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0)
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId));

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    public void bind() { glUseProgram(programId); }
    public void unbind() { glUseProgram(0); }

    public void cleanup() {
        unbind();
        if (vertexShaderId != 0) glDetachShader(programId, vertexShaderId);
        if (fragmentShaderId != 0) glDetachShader(programId, fragmentShaderId);
        glDeleteProgram(programId);
    }

    public void setUniform(String name, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            int location = glGetUniformLocation(programId, name);
            glUniformMatrix4fv(location, false, fb);
        }
    }
}
