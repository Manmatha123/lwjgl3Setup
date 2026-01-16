package fbx;

import org.joml.Vector3f;
import java.util.ArrayList;
import java.util.List;

public class BoneTransform {
    public List<Keyframe> positionKeys = new ArrayList<>();
    public List<Keyframe> rotationKeys = new ArrayList<>();
    public List<Keyframe> scaleKeys = new ArrayList<>();

    public static class Keyframe {
        public float time;
        public Vector3f value;

        public Keyframe(float time, Vector3f value) {
            this.time = time;
            this.value = new Vector3f(value);
        }
    }
}
