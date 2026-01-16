package fbx;


import org.joml.Matrix4f;

public class Bone {
    public final int id;
    public final String name;
    public final Matrix4f offsetMatrix;

    public Bone(int id, String name, Matrix4f offsetMatrix) {
        this.id = id;
        this.name = name;
        this.offsetMatrix = offsetMatrix;
    }
}
