package FBX3;

import java.util.HashMap;
import java.util.Map;

public class Animation {
    public float duration;
    public float ticksPerSecond;
    public Node rootNode;
    public Map<String, Bone> bones = new HashMap<>();
}
