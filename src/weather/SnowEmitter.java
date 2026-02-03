package weather;

import org.joml.Vector3f;
import entities.Camera;
import window.WindowManager;

public class SnowEmitter {

    private static final float RADIUS = 45f;
    private static final float HEIGHT = 22f;

    private static float spawnAccumulator = 0f;

    public static void emit(Camera camera, ParticleTexture snowTexture) {

        spawnAccumulator += WindowManager.getDeltaTime();
        if (spawnAccumulator < 0.03f) return; // slower spawn than rain
        spawnAccumulator = 0f;

        // Big visible flakes
        spawnLayer(camera, snowTexture,
                0.12f,
                -2.5f,
                20);

        // Medium snow body
        spawnLayer(camera, snowTexture,
                0.07f,
                -1.8f,
                450);

        // Fine distant snow (density illusion)
        spawnLayer(camera, snowTexture,
                0.03f,
                -1.2f,
                700);
    }

    private static void spawnLayer(Camera camera,
                                   ParticleTexture tex,
                                   float scale,
                                   float baseSpeed,
                                   int count) {

        Vector3f cam = camera.getPosition();

        for (int i = 0; i < count; i++) {

            float angle = (float) (Math.random() * Math.PI * 2);
            float radius = rand(0f, RADIUS);

            float x = cam.x + (float) Math.cos(angle) * radius;
            float z = cam.z + (float) Math.sin(angle) * radius;
            float y = cam.y + HEIGHT + rand(-2f, 4f);

            Vector3f velocity = new Vector3f(
                    rand(-0.8f, 0.8f),        // sideways drift
                    baseSpeed + rand(-0.4f, 0.4f),
                    rand(-0.8f, 0.8f)
            );

            float life = 4.5f + rand(0f, 2.5f);

            new Particle(
                    tex,
                    new Vector3f(x, y, z),
                    velocity,
                    -0.6f,     // VERY low gravity
                    life,
                    scale + rand(0f, scale * 0.5f)
            );
        }
    }

    private static float rand(float min, float max) {
        return min + (float) Math.random() * (max - min);
    }
}
