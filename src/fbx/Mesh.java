package fbx;


public class Mesh {
    public static final int MAX_WEIGHTS = 4;
    private float[] vertices;
    private float[] textures;
    private float[] normals;
    private int[] indices;
    private int[] boneIds;
    private float[] weights;
    private Material material;

    public Mesh(float[] vertices, float[] textures, float[] normals, int[] indices) {
        this(vertices, textures, normals, indices, null, null);
    }

    public Mesh(float[] vertices, float[] textures, float[] normals, int[] indices, 
                int[] boneIds, float[] weights) {
        this.vertices = vertices;
        this.textures = textures;
        this.normals = normals;
        this.indices = indices;
        this.boneIds = boneIds != null ? boneIds : new int[vertices.length / 3 * MAX_WEIGHTS];
        this.weights = weights != null ? weights : new float[vertices.length / 3 * MAX_WEIGHTS];
    }

    // Getters + OpenGL setup methods (vbo, vao, etc.)
    public float[] getVertices() { return vertices; }
    public int[] getBoneIds() { return boneIds; }
    public float[] getWeights() { return weights; }
    public void setMaterial(Material material) { this.material = material; }
}
