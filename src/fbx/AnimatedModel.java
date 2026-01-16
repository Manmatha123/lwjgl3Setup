package fbx;

import org.joml.Matrix4f;
import java.util.*;

public class AnimatedModel {

    public List<AnimatedMesh> meshes = new ArrayList<>();
    public Map<String, Bone> bones = new HashMap<>();
    public Matrix4f globalInverseTransform;
    public int boneCount;
}






