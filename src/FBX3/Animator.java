package FBX3;


import org.joml.Matrix4f;

public class Animator {

    private float currentTime = 0f;
    private Animation animation;

    public Animator(Animation animation) {
        this.animation = animation;
    }

    public void update(float deltaTime, AnimatedModel model) {
        float ticks = deltaTime * animation.ticksPerSecond;
        currentTime = (currentTime + ticks) % animation.duration;

        Matrix4f identity = new Matrix4f();
        calculateBoneTransform(
                animation.rootNode,
                identity,
                model
        );
    }

    private void calculateBoneTransform(
            Node node,
            Matrix4f parentTransform,
            AnimatedModel model) {

        Matrix4f nodeTransform = new Matrix4f(node.transform);

        Bone bone = animation.bones.get(node.name);
        if (bone != null) {
            nodeTransform = bone.getLocalTransform(currentTime);
        }

        Matrix4f globalTransform =
                new Matrix4f(parentTransform).mul(nodeTransform);

        Integer boneId = model.boneMap.get(node.name);
        if (boneId != null) {
            model.bones[boneId].finalTransform
                    .set(globalTransform)
                    .mul(model.bones[boneId].offset);
        }

        for (Node child : node.children) {
            calculateBoneTransform(child, globalTransform, model);
        }
    }
}
