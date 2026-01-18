package gameEngine;

import java.sql.Time;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import FBX3.AnimatedModel;
import FBX3.AnimatedRenderer;
import FBX3.Animator;
import FBX3.AssimpLoader;
import FBX3.FBXResult;
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
    private ModelTexture textureAero;
    public Animator animator;
    public FBXResult spiderModel;

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
        light = new Light(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1.0f, 0.99f, 0.88f), 1.0f, 0.2f,
                100000f, 0.0f, 0.0f);
        // 2️⃣ Load a single OBJ model
        List<RawModel> model = objLoader.loadObjModel(
                "E:\\lwjgl\\LWJGL3\\project1\\res\\ball.obj", loader);

        spiderModel = AssimpLoader.load("E:\\lwjgl\\LWJGL3\\project1\\res\\FuturisticCombatJet.fbx", loader);
        animator = new Animator(spiderModel.animation);
        ModelTexture texture = new ModelTexture(loader.loadTexture("res/ball.png"));
        textureAero = new ModelTexture(loader.loadTexture("res/AircraftC.jpg"));
        TextureModel texturedModel = new TextureModel(model.get(0), texture, "tree");

        treeEntity = new ControlObject_Test(
                texturedModel,
                new Vector3f(0, 0, 0), // Position
                0, 0, 0, // Rotation
                new Vector3f(6, 6, 6));
        camera = new Camera(treeEntity);
        renderer = new MasterRenderer(loader, 2080, 1080);
    }

    float zVal = 0.0f;
    float rotationY = 0.0f;

    private void loop() {

        AnimatedModel model = spiderModel.model;
        AnimatedRenderer renderer = new AnimatedRenderer();

        while (!window.shouldClose()) {

            window.clear();

            float delta = window.getDeltaTime();

            animator.update(delta, model);

            if (GLFW.glfwGetKey(window.getWindowId(), GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
                zVal += 1f;
            }
            if (GLFW.glfwGetKey(window.getWindowId(), GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
                zVal -= 1f;
            }
            if (GLFW.glfwGetKey(window.getWindowId(), GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
                rotationY += 20 * delta;
            }

            if (GLFW.glfwGetKey(window.getWindowId(), GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
                rotationY -= 20 * delta;
            }

            // Update camera or entity if needed
            camera.move();

            renderer.render(
                    model,
                    camera,
                    textureAero,
                    new Matrix4f().identity().translate(0, 0, zVal)
                    .rotateY((float)Math.toRadians(rotationY))
                );

            // animator.update(delta, spiderModel.model);
            // renderer.render(spiderModel.model);

            // Render the single entity
            // renderer.processEntity(treeEntity); // Add entity to render queue
            // renderer.renderRaw(); // Render all processed entities
            // renderer.render(light,camera); // Render all processed entities
            // renderer.clear(); // Clear queue for next frame

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
