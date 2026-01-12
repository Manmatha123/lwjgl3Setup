package gameEngine;

import java.util.List;

import org.joml.Vector3f;

import entities.Camera;
import entities.ControlObject_Test;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TextureModel;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.objLoader;
import textures.ModelTexture;
import window.WindowManager;

public class GameLoop {

    private WindowManager window;
    private Loader loader;
    private Camera camera;
    private MasterRenderer renderer;
    private ControlObject_Test treeEntity;
      Light light;

    public void start() {
        window = new WindowManager(2080, 1080, "LWJGL 3 Engine");
        window.create();

        init();
        loop();
        cleanup();
    }

    private void init() {
        // 1️⃣ Initialize loader
        loader = new Loader();
light = new Light(new Vector3f(0,0,0), new Vector3f(0,0,0), new Vector3f(1.0f, 0.99f, 0.88f), 1.0f, 0.2f, 100000f, 0.0f, 0.0f);
        // 2️⃣ Load a single OBJ model
        List<RawModel> model =objLoader.loadObjModel(
                "E:\\lwjgl\\LWJGL3\\project1\\res\\ball.obj", loader);

        ModelTexture texture = new ModelTexture(loader.loadTexture("res/ball.png"));
        TextureModel texturedModel = new TextureModel(model.get(0), texture, "tree");

        treeEntity = new ControlObject_Test(
                texturedModel,
                new Vector3f(0, 0, 0), // Position
                0, 0, 0, // Rotation
                new Vector3f(6,6,6) 
        );
        camera = new Camera(treeEntity);
        renderer = new MasterRenderer(loader,2080, 1080);
    }

    private void loop() {

        while (!window.shouldClose()) {

        window.clear();

        float delta = window.getDeltaTime();

        // Update camera or entity if needed
        camera.move();

        // Render the single entity
        renderer.processEntity(treeEntity);  // Add entity to render queue
        // renderer.renderRaw();                   // Render all processed entities
        renderer.render(light,camera);                   // Render all processed entities
        // renderer.clear();                    // Clear queue for next frame

        // Swap buffers
        window.update();
        }
    }

    private void cleanup() {
        // renderer.cleanUp();
        // loader.cleanUp();
        window.destroy();
    }
}
