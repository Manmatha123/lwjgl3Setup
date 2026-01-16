package fbx;

import org.joml.Matrix4f;
import java.util.HashMap;
import java.util.Map;

public class AnimatedFrame {
    public Map<Integer, Matrix4f> jointTransforms = new HashMap<>();

    public void setJointTransform(int boneId, Matrix4f transform) {
        jointTransforms.put(boneId, new Matrix4f(transform));
    }
}
