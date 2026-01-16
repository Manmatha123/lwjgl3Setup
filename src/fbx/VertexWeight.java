package fbx;

public class VertexWeight {
    public final int boneId;
    public final int vertexId;
    public final float weight;

    public VertexWeight(int boneId, int vertexId, float weight) {
        this.boneId = boneId;
        this.vertexId = vertexId;
        this.weight = weight;
    }
}