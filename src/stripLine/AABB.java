package stripLine;



import org.joml.Vector3f;

public class AABB {

    public Vector3f min;
    public Vector3f max;

    // 8 world-space corners (required by DebugAABBRenderer)
    public Vector3f[] worldCorners = new Vector3f[8];

    public AABB(Vector3f min, Vector3f max) {
        this.min = min;
        this.max = max;
        updateCorners();
    }

    public void updateCorners() {

        worldCorners[0] = new Vector3f(min.x, min.y, min.z);
        worldCorners[1] = new Vector3f(max.x, min.y, min.z);
        worldCorners[2] = new Vector3f(max.x, min.y, max.z);
        worldCorners[3] = new Vector3f(min.x, min.y, max.z);

        worldCorners[4] = new Vector3f(min.x, max.y, min.z);
        worldCorners[5] = new Vector3f(max.x, max.y, min.z);
        worldCorners[6] = new Vector3f(max.x, max.y, max.z);
        worldCorners[7] = new Vector3f(min.x, max.y, max.z);
    }
}
