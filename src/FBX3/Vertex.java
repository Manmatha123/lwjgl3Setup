package FBX3;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Vertex {
    public Vector3f position;
    public Vector3f normal;
    public Vector2f texCoord;

    public int[] boneIds = new int[4];
    public float[] weights = new float[4];

    public void addBoneData(int id, float weight) {
        for (int i = 0; i < 4; i++) {
            if (weights[i] == 0.0f) {
                boneIds[i] = id;
                weights[i] = weight;
                return;
            }
        }
    }
}
