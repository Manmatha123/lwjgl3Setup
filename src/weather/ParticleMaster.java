package weather;



import org.joml.Matrix4f;

import entities.Camera;
import renderEngine.Loader;

import java.util.*;

public class ParticleMaster {

    private static Map<ParticleTexture, List<Particle>> particles = new HashMap<>();
    private static ParticleRenderer renderer;

    public static void init(Loader loader, Matrix4f projectionMatrix) {
        renderer = new ParticleRenderer(loader, projectionMatrix);
    }

    public static void update(Camera camera) {

        Iterator<Map.Entry<ParticleTexture, List<Particle>>> mapIt =
                particles.entrySet().iterator();

        while (mapIt.hasNext()) {
            List<Particle> list = mapIt.next().getValue();

            Iterator<Particle> it = list.iterator();
            while (it.hasNext()) {
                if (!it.next().update(camera)) {
                    it.remove();
                }
            }

            list.sort((a, b) ->
                    Float.compare(b.getDistance(), a.getDistance()));

            if (list.isEmpty()) mapIt.remove();
        }
    }

    public static void render(Camera camera) {
        renderer.render(particles, camera);
    }

    public static void cleanUp() {
        renderer.cleanUp();
    }

    public static void addParticle(Particle particle) {
        particles
                .computeIfAbsent(particle.getTexture(), k -> new ArrayList<>())
                .add(particle);
    }
}
