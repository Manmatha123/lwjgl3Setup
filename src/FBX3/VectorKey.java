package FBX3;

import org.joml.Vector3f;

public class VectorKey {
    public float time;
    public Vector3f value;

    public VectorKey(float time, Vector3f value) {
        this.time = time;
        this.value = value;
    }
}