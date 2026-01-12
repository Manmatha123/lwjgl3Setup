package toolbox;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import entities.Camera;

import static org.lwjgl.glfw.GLFW.*;

public class MousePicker {

    private static final float RAY_RANGE = 600f;

    private final Camera camera;
    private final Matrix4f projectionMatrix;
    private final long windowId;

    private final Vector3f currentRay = new Vector3f();
    private final Matrix4f viewMatrix = new Matrix4f();

    public MousePicker(Camera camera, Matrix4f projectionMatrix, long windowId) {
        this.camera = camera;
        this.projectionMatrix = projectionMatrix;
        this.windowId = windowId;
    }

    public Vector3f getCurrentRay() {
        return new Vector3f(currentRay);
    }

    public void update(int windowWidth, int windowHeight) {
        Maths.createViewMatrix(camera).get(viewMatrix);
        calculateMouseRay(windowWidth, windowHeight);
    }

    /* ===========================
       RAY CALCULATION
       =========================== */

    private void calculateMouseRay(int width, int height) {

        double[] mouseX = new double[1];
        double[] mouseY = new double[1];
        glfwGetCursorPos(windowId, mouseX, mouseY);

        Vector2f ndc = getNormalizedDeviceCoords(
                (float) mouseX[0],
                (float) mouseY[0],
                width,
                height
        );

        Vector4f clipCoords = new Vector4f(ndc.x, -ndc.y, -1f, 1f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);

        currentRay.set(worldRay);
    }

    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = new Matrix4f(projectionMatrix).invert();
        Vector4f eyeCoords = invertedProjection.transform(clipCoords);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }

    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = new Matrix4f(viewMatrix).invert();
        Vector4f rayWorld = invertedView.transform(eyeCoords);

        return new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z).normalize();
    }

    private Vector2f getNormalizedDeviceCoords(float mouseX, float mouseY,
                                               int width, int height) {

        float x = (2f * mouseX) / width - 1f;
        float y = (2f * mouseY) / height - 1f;
        return new Vector2f(x, y);
    }

    /* ===========================
       AABB INTERSECTION (UNCHANGED)
       =========================== */

    // public boolean isRayIntersectingAABB(Vector3f rayOrigin,
    //                                      Vector3f rayDirection,
    //                                      AABB bbox) {
    //     float tMin = (bbox.min.x - rayOrigin.x) / rayDirection.x;
    //     float tMax = (bbox.max.x - rayOrigin.x) / rayDirection.x;
    //     if (tMin > tMax) { float tmp = tMin; tMin = tMax; tMax = tmp; }
    //     float tyMin = (bbox.min.y - rayOrigin.y) / rayDirection.y;
    //     float tyMax = (bbox.max.y - rayOrigin.y) / rayDirection.y;
    //     if (tyMin > tyMax) { float tmp = tyMin; tyMin = tyMax; tyMax = tmp; }
    //     if (tMin > tyMax || tyMin > tMax) return false;
    //     if (tyMin > tMin) tMin = tyMin;
    //     if (tyMax < tMax) tMax = tyMax;
    //     float tzMin = (bbox.min.z - rayOrigin.z) / rayDirection.z;
    //     float tzMax = (bbox.max.z - rayOrigin.z) / rayDirection.z;
    //     if (tzMin > tzMax) { float tmp = tzMin; tzMin = tzMax; tzMax = tmp; }
    //     if (tMin > tzMax || tzMin > tMax) return false;
    //     return true;
    // }


}
