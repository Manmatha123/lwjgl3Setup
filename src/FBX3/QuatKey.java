package FBX3;


import org.joml.Quaternionf;

public class QuatKey {
    public float time;
    public Quaternionf value;

    public QuatKey(float time, Quaternionf value) {
        this.time = time;
        this.value = value;
    }
}