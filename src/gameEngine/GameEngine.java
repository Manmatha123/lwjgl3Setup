package gameEngine;

import javax.swing.*;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import window.WindowManager;

import java.awt.*;

public class GameEngine {
    public static void main(String[] args) throws Exception {
        WindowManager window = new WindowManager(1280, 720, "LWJGL3 Engine");

        window.create();

        GameLoop game = new GameLoop();
        ImGui.createContext();
        ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
        ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

        ImGuiIO io = ImGui.getIO();

ImFontConfig config = new ImFontConfig();
config.setMergeMode(false);

io.getFonts().addFontFromFileTTF(
        "res/fonts/Roboto-Regular.ttf",
        18
);

// Enable merge mode for icons
config.setMergeMode(true);

short[] ranges = new short[]{
        (short) 0xf000, (short) 0xf3ff, 0
};

io.getFonts().addFontFromFileTTF(
        "res/fonts/Font Awesome 7 Free-Solid-900.otf",
        18,
        config,
        ranges
);

config.destroy();

        imGuiGlfw.init(window.getWindowId(), true);
        imGuiGl3.init("#version 330 core");

        DroneHUD hud = new DroneHUD();

        game.init(); // initialize once

        while (!window.shouldClose()) {

            window.clear();

            game.updateAndRender(); // one frame

            imGuiGlfw.newFrame();
            ImGui.newFrame();

            hud.render();

            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            window.update(); // swap buffers + poll input
        }

        window.destroy();

    }
}