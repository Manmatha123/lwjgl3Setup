package fbx;


import org.joml.Matrix4f;
import java.util.ArrayList;
import java.util.List;

public class Node {
    public String name;
    public Matrix4f transformation;
    public List<Node> children;
    public int meshIndex = -1;

    public Node() {
        children = new ArrayList<>();
        transformation = new Matrix4f();
    }
}
