package fbx;

import org.joml.Vector3f;

public class AnimatedEntity {

    private AnimatedModel model;   // model + skeleton
    private Animator animator;

    private Vector3f position;
    private float rotX, rotY, rotZ;
    private float scale;

    public AnimatedEntity(AnimatedModel model,
                          Vector3f position,
                          float rx, float ry, float rz,
                          float scale) {

        this.model = model;
        this.animator = new Animator(model.getRootJoint());

        this.position = position;
        this.rotX = rx;
        this.rotY = ry;
        this.rotZ = rz;
        this.scale = scale;
    }

    public void doAnimation(Animation animation) {
        animator.doAnimation(animation);
    }

    public Animator getAnimator() {
        return animator;
    }

    public AnimatedModel getModel() {
        return model;
    }

    public Matrix4f getTransformationMatrix() {
        return new Matrix4f()
                .translate(position)
                .rotateX((float) Math.toRadians(rotX))
                .rotateY((float) Math.toRadians(rotY))
                .rotateZ((float) Math.toRadians(rotZ))
                .scale(scale);
    }
}
