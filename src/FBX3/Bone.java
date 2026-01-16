package FBX3;

import java.util.List;

import org.joml.Matrix4f;

public class Bone {
    public String name;
    public int id;

    public List<VectorKey> positions;
    public List<QuatKey> rotations;

    public Matrix4f interpolate(float time) {
        // minimal: return first key for now
        Matrix4f m = new Matrix4f();
        m.translate(positions.get(0).value);
        m.rotate(rotations.get(0).value);
        return m;
    }
}
