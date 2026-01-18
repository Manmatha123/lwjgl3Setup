package FBX3;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class Bone {

    public String name;
    public int id;

    public List<VectorKey> positions;
    public List<QuatKey> rotations;
    public List<VectorKey> scales;

    public Matrix4f getLocalTransform(float time) {
        Vector3f position = interpolatePosition(time);
        Quaternionf rotation = interpolateRotation(time);
        Vector3f scale = interpolateScale(time);

        return new Matrix4f()
                .translate(position)
                .rotate(rotation)
                .scale(scale);
    }


    private Vector3f interpolatePosition(float time) {
    if (positions.size() == 1)
        return new Vector3f(positions.get(0).value);

    int index = getPositionIndex(time);
    int nextIndex = index + 1;

    float t1 = positions.get(index).time;
    float t2 = positions.get(nextIndex).time;
    float factor = (time - t1) / (t2 - t1);

    return new Vector3f(positions.get(index).value)
            .lerp(positions.get(nextIndex).value, factor);
}

private Quaternionf interpolateRotation(float time) {
    if (rotations.size() == 1)
        return new Quaternionf(rotations.get(0).value);

    int index = getRotationIndex(time);
    int nextIndex = index + 1;

    float t1 = rotations.get(index).time;
    float t2 = rotations.get(nextIndex).time;
    float factor = (time - t1) / (t2 - t1);

    return new Quaternionf(rotations.get(index).value)
            .slerp(rotations.get(nextIndex).value, factor);
}

private Vector3f interpolateScale(float time) {
    if (scales.size() == 1)
        return new Vector3f(scales.get(0).value);

    int index = getScaleIndex(time);
    int nextIndex = index + 1;

    float t1 = scales.get(index).time;
    float t2 = scales.get(nextIndex).time;
    float factor = (time - t1) / (t2 - t1);

    return new Vector3f(scales.get(index).value)
            .lerp(scales.get(nextIndex).value, factor);
}

private int getPositionIndex(float time) {
    for (int i = 0; i < positions.size() - 1; i++) {
        if (time < positions.get(i + 1).time)
            return i;
    }
    return positions.size() - 2;
}

private int getRotationIndex(float time) {
    for (int i = 0; i < rotations.size() - 1; i++) {
        if (time < rotations.get(i + 1).time)
            return i;
    }
    return rotations.size() - 2;
}

private int getScaleIndex(float time) {
    for (int i = 0; i < scales.size() - 1; i++) {
        if (time < scales.get(i + 1).time)
            return i;
    }
    return scales.size() - 2;
}

}