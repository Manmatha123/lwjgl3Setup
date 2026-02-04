package gameEngine;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.awt.AWTGLCanvas;
import org.lwjgl.opengl.awt.GLData;

public class GameCanvas extends AWTGLCanvas {

    private GameLoop game;

    public GameCanvas() throws Exception {
        super(createData());
        game = new GameLoop();
    }

    private static GLData createData() {
        GLData d = new GLData();
        d.majorVersion = 3;
        d.minorVersion = 3;
        d.profile = GLData.Profile.CORE;
        d.doubleBuffer = true;
        return d;
    }

    @Override
    public void initGL() {
        GL.createCapabilities();
        GL11.glClearColor(0.2f, 0.3f, 0.8f, 1.0f); // Cornflower Blue
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        game.init();
    }

    @Override
    public void paintGL() {
        // 1. Clear buffers
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // 2. Run game logic and draw calls
        if (game != null) {
            game.updateAndRender();
        }

        // 3. Swap the back buffer to the front
        swapBuffers();
    }
}