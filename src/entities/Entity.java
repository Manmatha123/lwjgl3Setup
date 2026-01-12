package entities;


import org.joml.Matrix4f;
import org.joml.Vector3f;
import models.TextureModel;
import toolbox.Maths;

public class Entity {
    public TextureModel model;
    public Vector3f position;
    public float rotX, rotY, rotZ;
    public Vector3f scale;
    private int textureIndex = 0;
    public int objectType = 0;
    public Vector3f offset;
    public Vector3f angleOffSet;

    private Vector3f size;

    public void setModel(TextureModel model) {
        this.model = model;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Vector3f getSize() {
        return size;
    }

    public void setSize(Vector3f size) {
        this.size = size;
    }

    public Entity(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = new Vector3f(scale);
 }

    public Entity(TextureModel model, Vector3f position, Vector3f offset, float rotX, float rotY, float rotZ,
            Vector3f scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = new Vector3f(scale);
        this.objectType = 1;
        this.offset = offset;
 }

    public Entity(TextureModel texturedModel, Vector3f position2, int rotX2, int rotY2, int rotZ2, float f) {
        //TODO Auto-generated constructor stub
    }

    public float getTextureXoffSet() {
        int column = textureIndex % model.getTexture().getNumberofrows();
        return (float) column / (float) model.getTexture().getNumberofrows();
    }

    public float getTextureYoffSet() {
        int row = textureIndex / model.getTexture().getNumberofrows();
        return (float) row / (float) model.getTexture().getNumberofrows();
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public TextureModel getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public Vector3f getScale() {
        return scale;
    }

    public float getScaleX() {
        return scale.x;
    }

    public float getScaleY() {
        return scale.y;
    }

    public float getScaleZ() {
        return scale.z;
    }

    public void setScaleX(float x) {
        this.scale.x = x;
    }

    public void setScaleY(float y) {
        this.scale.y = y;
    }

    public void setScaleZ(float z) {
        this.scale.z = z;
    }

    public void setOffset(Vector3f offset) {
        this.offset = new Vector3f(offset);
    }

    public void changeOffSet(double x, double y, double z) {
        offset.x = (float) (offset.x + x);
        offset.y = (float) (offset.y + y);
        offset.z = (float) (offset.z + z);
    }



    public Matrix4f createTransformMatrix() {
        Matrix4f transformationMatrix = new Matrix4f();
        transformationMatrix = Maths.createTransformationMatrix(this.getPosition(),
                this.getRotX(), this.getRotY(), this.getRotZ(), this.getScale());
        return transformationMatrix;
    }

}
