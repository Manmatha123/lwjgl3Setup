package weather;

import org.joml.Vector3f;
import entities.Camera;
import window.WindowManager;

public class RainEmitter {

    private static final float RADIUS = 40f;
    private static final float HEIGHT = 25f;

    private static float spawnAccumulator = 0f;

public static void emit(Camera camera, ParticleTexture rainTexture) {

    spawnAccumulator += WindowManager.getDeltaTime();
    if (spawnAccumulator < 0.016f) return;
    spawnAccumulator = 0f;

    float pulse = (float) Math.sin(System.currentTimeMillis() * 0.002);
    int stormBoost = pulse > 0.5f ? 30 : 0;

    // BIG streaks (visible drops)
    spawnLayer(camera, rainTexture,
            0.1f,     // scale
            -30f,      // fast fall
            20 + stormBoost / 2);

    // MEDIUM rain (main body)
    spawnLayer(camera, rainTexture,
            0.07f,
            -20f,
            40 + stormBoost);

    // FINE mist (density illusion)
    spawnLayer(camera, rainTexture,
            0.015f,
            -0.1f,
            80);   // high count but small
}



private static void spawnLayer(Camera camera,
                               ParticleTexture tex,
                               float scale,
                               float baseSpeed,
                               int count) {

    Vector3f cam = camera.getPosition();

    for (int i = 0; i < count; i++) {

        // Slight cylindrical distribution (looks better than square)
        float angle = (float) (Math.random() * Math.PI * 2.0);
        float radius = rand(0f, RADIUS);

        float x = cam.x + (float)Math.cos(angle) * radius;
        float z = cam.z + (float)Math.sin(angle) * radius;

        // Random height avoids visible layers
        float y = cam.y + HEIGHT + rand(-3f, 5f);

        float speed = baseSpeed - rand(0f, 15f);

        Vector3f velocity = new Vector3f(
                WeatherSystem.wind.x + rand(-1.5f, 1.5f),
                speed,
                WeatherSystem.wind.z + rand(-1.5f, 1.5f)
        );

        // Lifetime varies with drop size
        float life = scale < 0.05f
                ? 1.0f + rand(0f, 0.4f)   // mist
                : 1.6f + rand(0f, 0.5f);  // heavy drops

        float finalScale = scale + rand(0f, scale * 0.6f);

        new Particle(
                tex,
                new Vector3f(x, y, z),
                velocity,
                -9.8f,
                life,
                finalScale
        );
    }
}


    private static float rand(float min, float max) {
        return min + (float) Math.random() * (max - min);
    }
}
