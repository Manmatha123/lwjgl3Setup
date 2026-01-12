package entities;

import org.joml.Vector3f;

public class Light {
    private Vector3f position;
    private Vector3f colour;

    private Vector3f direction;

    public Vector3f getDirection() {
        return direction;
    }

    private float attenConstant;
    private float ambient;
    private float range;
    private float attenLinear;
    private float attenExponent;

    public float getAttenConstant() {
        return attenConstant;
    }

    public float getAmbient() {
        return ambient;
    }

    public float getRange() {
        return range;
    }

    public float getAttenLinear() {
        return attenLinear;
    }

    public float getAttenExponent() {
        return attenExponent;
    }

    public Light(Vector3f position, Vector3f direction, Vector3f colour, float attenConstant, float ambient, float range, float attenLinear, float attenExponent){
        this.position = position;
        this.colour = colour;
        this.direction=direction;
        this.attenConstant=attenConstant;
        this.ambient=ambient;
        this.range=range;
        this.attenLinear=attenLinear;
        this.attenExponent=attenExponent;
    }


    public void setAttenuation(Vector3f attenuation){
        if (attenuation != null) {
            this.attenConstant = attenuation.x;
            this.attenLinear = attenuation.y;
            this.attenExponent = attenuation.z;
        }
    }
    public float getAttenuation(){
        return this.attenConstant;
    }
    public Vector3f getPosition(){
        return position;
    }
    public void setPosition(Vector3f position){
        this.position = position;
    }
    public Vector3f getColour(){
        return colour;
    }
    public void setColour(Vector3f colour){
        this.colour = colour;
    }

public void setDirection(Vector3f direction) {
    if (direction != null) {
        this.direction = new Vector3f(direction).normalize();
    }
}

}
