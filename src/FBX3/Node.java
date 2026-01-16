package FBX3;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;

public class Node {
    public String name;
    public Matrix4f transform;
    public List<Node> children = new ArrayList<>();
}
