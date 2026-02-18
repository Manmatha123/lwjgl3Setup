package gameEngine;

import java.awt.event.KeyEvent;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import FBX3.*;
import entities.*;
import models.RawModel;
import models.TextureModel;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.objLoader;
import stripLine.DebugAABBRenderer;
import stripLine.dashPath.DashedPathRenderer;
import textures.ModelTexture;
import toolbox.Maths;
import weather.*;

public class GameLoop {

    // ================= CORE =================

    private Loader loader;
    private Camera camera;
    private MasterRenderer renderer;

    public Animator animator;
    public FBXResult spiderModel;
    private AnimatedRenderer animatedRenderer;

    private DebugAABBRenderer debugAABBRenderer;
    private ParticleTexture rainTex;
    private ParticleTexture snowTex;
    private Light light;
    private DashedPathRenderer pathRenderer;

    private ModelTexture textureAero;
    private ControlObject_Test treeEntity;

    // ================= STATE =================

    private float zVal = 0f;
    private float rotationY = 0f;

    // ================= INIT (CALLED ONCE) =================

    private static boolean isInitialize=false;

    public void init() {

        loader = new Loader();

        light = new Light(
                new Vector3f(0,0,0),
                new Vector3f(0,0,0),
                new Vector3f(1,1,1),
                1f,0.2f,100000f,0,0
        );

        pathRenderer = new DashedPathRenderer(List.of(
                new Vector3f(0,0,0),
                new Vector3f(0,0,20),
                new Vector3f(5,0,20),
                new Vector3f(-200,0,0)
        ));

        List<RawModel> model =
                objLoader.loadObjModel(
                        "E:/lwjgl/LWJGL3/project1/res/ball.obj",
                        loader
                );

        spiderModel =
                AssimpLoader.load(
                        "E:/lwjgl/LWJGL3/project1/res/FuturisticCombatJet.fbx",
                        loader
                );

        animator = new Animator(spiderModel.animation);
        animatedRenderer = new AnimatedRenderer();

        ModelTexture texture =
                new ModelTexture(loader.loadTexture("res/ball.png"));

        textureAero =
                new ModelTexture(loader.loadTexture("res/AircraftC.jpg"));

        TextureModel texturedModel =
                new TextureModel(model.get(0), texture, "tree");

        treeEntity = new ControlObject_Test(
                texturedModel,
                new Vector3f(0,0,0),
                0,0,0,
                new Vector3f(6,6,6)
        );

        camera = new Camera(treeEntity);

        renderer = new MasterRenderer(loader,1280,720);

        ParticleMaster.init(loader, renderer.getProjectionMatrix());

        rainTex = new ParticleTexture(
                loader.loadTexture("res/rain.png"),1);

        snowTex = new ParticleTexture(
                loader.loadTexture("res/snow.png"),1);

        debugAABBRenderer = new DebugAABBRenderer();

        System.out.println("Game Initialized");
        isInitialize=true;
    }

    // ================= FRAME =================
float i=0;
    public void updateAndRender() {

        if(!isInitialize){
                init();
        }
        float delta =0.02f;

        AnimatedModel model = spiderModel.model;
        animator.update(delta, model);

        handleInput(delta);
        camera.move();

        rotationY+=1f;
        System.out.println("Rot: "+rotationY);

        Matrix4f staticModelMatrix =
    Maths.createTransformationMatrix(
        new Vector3f(0, 0, 20),  // world position
        0, rotationY, 0,
        1
    );



        animatedRenderer.render(
                model,
                camera,
                textureAero,
             staticModelMatrix
        );

        // pathRenderer.render(
        //         renderer.getProjectionMatrix(),
        //         camera
        // );

        if(WeatherSystem.isRainActive)
            RainEmitter.emit(camera, rainTex);
        else
            SnowEmitter.emit(camera, snowTex);

        ParticleMaster.update(camera);
        ParticleMaster.render(camera);
    }

    // ================= INPUT =================

    private void handleInput(float delta) {

        if(Input.isDown(KeyEvent.VK_UP))
            zVal += 20f * delta;

        if(Input.isDown(KeyEvent.VK_DOWN))
            zVal -= 20f * delta;

        // if(Input.isDown(KeyEvent.VK_LEFT)){
        //     rotationY +=10;
        //     System.out.println("Left clicked:"+rotationY);
        // }


        // if(Input.isDown(KeyEvent.VK_RIGHT)){
                
        // }
        //     rotationY -= 10;

        if(Input.isDown(KeyEvent.VK_R))
            WeatherSystem.isRainActive = true;

        if(Input.isDown(KeyEvent.VK_S))
            WeatherSystem.isRainActive = false;
    }
}
