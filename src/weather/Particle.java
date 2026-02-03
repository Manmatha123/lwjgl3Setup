package weather;



import org.joml.Vector3f;

import entities.Camera;
import window.WindowManager;

public class Particle {

    private Vector3f position;
    private Vector3f velocity;
    private float gravity;
    private float lifeLength;
    private float scale;

    private float elapsedTime = 0;
    private float distance;

    private ParticleTexture texture;

    public Particle(ParticleTexture texture,
                    Vector3f position,
                    Vector3f velocity,
                    float gravity,
                    float lifeLength,
                    float scale) {

        this.texture = texture;
        this.position = position;
        this.velocity = velocity;
        this.gravity = gravity;
        this.lifeLength = lifeLength;
        this.scale = scale;

        ParticleMaster.addParticle(this);
    }

    protected boolean update(Camera camera) {

        float dt =WindowManager.getDeltaTime();

        velocity.y += gravity * dt;

        if(!WeatherSystem.isRainActive){
velocity.x += Math.sin(elapsedTime * 2.0f) * 0.02f;
velocity.z += Math.cos(elapsedTime * 2.0f) * 0.02f;

        }

        position.add(new Vector3f(velocity).mul(dt));

        distance = new Vector3f(camera.getPosition())
                .sub(position)
                .lengthSquared();

        elapsedTime += dt;
        return elapsedTime < lifeLength;
    }

    public float getDistance() { return distance; }
    public Vector3f getPosition() { return position; }
    public float getScale() { return scale; }
    public ParticleTexture getTexture() { return texture; }
}
