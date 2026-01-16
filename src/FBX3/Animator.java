package FBX3;

import org.joml.Matrix4f;

public class Animator {

    private Animation animation;
    private float time;

    public Animator(Animation animation) {
        this.animation = animation;
    }

    public void update(float dt, AnimatedModel model) {
        time += animation.ticksPerSecond * dt;
        time %= animation.duration;
        calculate(animation.rootNode, new Matrix4f(), model);
    }

    private void calculate(Node node, Matrix4f parent, AnimatedModel model) {

        Matrix4f nodeTransform = node.transform;

        Bone bone = animation.bones.get(node.name);
        if (bone != null) {
            nodeTransform = bone.interpolate(time);
            BoneInfo info = model.bones[bone.id];
            info.finalTransform.set(parent)
                .mul(nodeTransform)
                .mul(info.offset);
        }

        Matrix4f global = new Matrix4f(parent).mul(nodeTransform);

        for (Node child : node.children)
            calculate(child, global, model);
    }
}
