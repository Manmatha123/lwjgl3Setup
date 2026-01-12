package entities;

import org.joml.Vector3f;

import models.RawModel;
import models.TextureModel;
import textures.ModelTexture;

public class ControlModel extends Entity{

    public boolean selected = false ;

    public ControlModel(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale){
        super(model,position, rotX,rotY,rotZ,new Vector3f(scale.x(),scale.y(),scale.z()));
    }
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(float rotX, float rotY, float rotZ) {
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    public void setScale(Vector3f scale) {
        this.scale = new Vector3f(scale);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public Vector3f getScale() {
        return scale;
    }

    public RawModel getRawModel() {
        return model.getRawModel();
    }

    public ModelTexture getTexture() {
        return model.getTexture();
    }

//    public String getName() {
//        return name;
//    }

    public TextureModel getTextureModel() {
        return model;
    }

}
