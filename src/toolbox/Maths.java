package toolbox;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import entities.Camera;
import entities.Entity;

public class Maths {

    /* ===============================
       BASIC TRANSFORMATION MATRICES
       =============================== */

    public static Matrix4f createTransformationMatrix(
            Vector3f translation,
            float rx, float ry, float rz,
            Vector3f scale) {

        return new Matrix4f()
                .identity()
                .translate(translation)
                .rotateY((float) Math.toRadians(ry))
                .rotateX((float) Math.toRadians(rx))
                .rotateZ((float) Math.toRadians(rz))
                .scale(scale);
    }

    public static Matrix4f createTransformationMatrix(
            Vector2f translation,
            Vector2f scale) {

        return new Matrix4f()
                .identity()
                .translate(translation.x, translation.y, 0f)
                .scale(scale.x, scale.y, 1f);
    }

    public static Matrix4f createTransformationMatrix(Entity entity) {
        return new Matrix4f()
                .identity()
                .translate(entity.getPosition())
                .rotateX((float) Math.toRadians(entity.getRotX()))
                .rotateY((float) Math.toRadians(entity.getRotY()))
                .rotateZ((float) Math.toRadians(entity.getRotZ()))
                .scale(entity.getScale());
    }

    public static Matrix4f createTransformationMatrix(
            Vector3f translation,
            Vector3f offset,
            float rx, float ry, float rz,
            Vector3f scale) {

        Vector3f finalOffset = calculateP2(offset, 0);

        return new Matrix4f()
                .identity()
                .translate(
                        translation.x + finalOffset.x,
                        translation.y + finalOffset.y,
                        translation.z + finalOffset.z
                )
                .rotateY((float) Math.toRadians(ry))
                .rotateX((float) Math.toRadians(rx))
                .rotateZ((float) Math.toRadians(rz))
                .translate(
                        -finalOffset.x,
                        -finalOffset.y,
                        -finalOffset.z
                )
                .scale(scale);
    }

    public static Matrix4f createTransformationMatrix(
            Vector3f translation,
            Vector3f offset,
            Vector3f angleOffset,
            float rx, float ry, float rz,
            Vector3f scale) {

        return new Matrix4f()
                .identity()
                .translate(offset).translate(translation)
                .rotateY((float) Math.toRadians(ry + angleOffset.y))
                .rotateZ((float) Math.toRadians(rz + angleOffset.z))
                .rotateX((float) Math.toRadians(rx))
                .rotateZ((float) Math.toRadians(-angleOffset.z))
                .rotateY((float) Math.toRadians(-angleOffset.y))
                .translate(offset.negate(new Vector3f()))
                .scale(scale);
    }

    /* ===============================
       ORTHOGRAPHIC MATRIX
       =============================== */

    public static Matrix4f createOrthoMatrix(
            float left, float right,
            float bottom, float top,
            float near, float far) {

        return new Matrix4f().ortho(left, right, bottom, top, near, far);
    }

    /* ===============================
       VECTOR TRANSFORM
       =============================== */

    public static Vector3f transform(Matrix4f matrix, Vector3f vector) {
        Vector4f result = new Vector4f(vector, 1.0f);
        matrix.transform(result);
        return new Vector3f(result.x, result.y, result.z);
    }

    /* ===============================
       VIEW MATRICES
       =============================== */

    public static Matrix4f createViewMatrix(Camera camera) {
        return new Matrix4f()
                .identity()
                .rotateX((float) Math.toRadians(camera.getPitch()))
                .rotateY((float) Math.toRadians(camera.getYaw()))
                .rotateZ((float) Math.toRadians(camera.getRoll()))
                .translate(
                        -camera.getPosition().x,
                        -camera.getPosition().y,
                        -camera.getPosition().z
                );
    }

    public static Matrix4f createSunViewMatrix(Camera camera) {
        return new Matrix4f()
                .identity()
                .rotateX((float) Math.toRadians(camera.getPitch()))
                .rotateY((float) Math.toRadians(camera.getYaw()))
                .rotateZ((float) Math.toRadians(camera.getRoll()));
    }

    public static Matrix4f createLightViewMatrix(Vector3f lightDirection) {

        Vector3f dir = new Vector3f(lightDirection).normalize();
        Vector3f lightPos = new Vector3f(dir).mul(-1000f);

        return new Matrix4f()
                .lookAt(
                        lightPos,
                        new Vector3f(0, 0, 0),
                        new Vector3f(0, 1, 0)
                );
    }

    public static Matrix4f createViewProjectionMatrix(
            Matrix4f view,
            Matrix4f projection) {

        return new Matrix4f(projection).mul(view);
    }

    /* ===============================
       HELPER FUNCTIONS
       =============================== */

    public static Vector3f calculateP2(Vector3f p1, double theta) {

        double r = Math.sqrt(p1.x * p1.x + p1.z * p1.z);
        double alpha = Math.atan2(p1.z, p1.x) + theta;

        float x2 = (float) (r * Math.cos(alpha));
        float z2 = (float) (r * Math.sin(alpha));

        return new Vector3f(x2, p1.y, z2);
    }
}
