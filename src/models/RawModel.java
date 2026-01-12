package models;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.List;

public class RawModel {

    private int VaoID;
    private int VboID;


    private int vertexCount;
    public List<Vector3f> vertex;
    public String filename;

    private FloatBuffer matrix44Buffer;

    public RawModel(int VaoID, int vertexCount) {
        this.VaoID = VaoID;
        this.vertexCount = vertexCount;
    }

    public RawModel(int VaoID, int vertexCount, List<Vector3f> vertex) {
        this(VaoID, vertexCount);
        this.vertex = vertex;
    }




    public RawModel(int VaoID, int vertexCount, List<Vector3f> vertex, String filename) {
        this(VaoID, vertexCount, vertex);
        this.filename = filename;
    
    }


    public int getVaoID() {
        return VaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void bindPositionOnly() {
        GL30.glBindVertexArray(VaoID);
        GL20.glEnableVertexAttribArray(0); // POSITION ONLY
    }

    public void unbindPositionOnly() {
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

}
