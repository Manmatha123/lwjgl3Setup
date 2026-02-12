package gameEngine;



import imgui.ImGui;
import imgui.ImDrawList;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;


import imgui.ImGui;
import imgui.ImDrawList;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

import imgui.ImGui;
import imgui.ImDrawList;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;

public class DroneHUD {

    // Telemetry values (update from simulation)
    public float altitude = 120.0f;
    public float speed = 32.5f;
    public float distance = 540.0f;
    public float battery = 86.0f;

    // -------------------------
    // MAIN RENDER
    // -------------------------
    public void render() {

        float screenW = ImGui.getIO().getDisplaySizeX();
        float screenH = ImGui.getIO().getDisplaySizeY();

        // Transparent fullscreen window
        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
        ImGui.setNextWindowSize(screenW, screenH);

        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0);
        ImGui.pushStyleColor(imgui.flag.ImGuiCol.WindowBg, 0, 0, 0, 0);

        ImGui.begin("HUD",
                new ImBoolean(true),
                ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoSavedSettings |
                ImGuiWindowFlags.NoBringToFrontOnFocus);

        ImDrawList draw = ImGui.getWindowDrawList();

        float cx = screenW / 2f;
        float cy = screenH / 2f;

        drawCameraFrame(draw, screenW, screenH);
        drawREC(draw, screenW);
        drawTopButtons(screenW);
        drawBottomLeft(draw, screenH);
        drawBottomRight(draw, screenW, screenH);
        drawCameraFocus(draw, cx, cy);

        ImGui.end();
        ImGui.popStyleVar(2);
        ImGui.popStyleColor();
    }

    // -------------------------
    // TOP BUTTONS
    // -------------------------
    private void drawTopButtons(float w) {

        ImGui.setCursorPos(w / 2f - 130, 20);
        ImGui.button("AUTO");
        ImGui.sameLine();
        ImGui.button("MANUAL");
    }

    // -------------------------
    // BOTTOM LEFT TELEMETRY
    // -------------------------
  private void drawBottomLeft(ImDrawList draw, float h) {

    int white = 0xFFFFFFFF;

    // Altitude (arrow up)
    draw.addText(45, h - 115, white,
            "\uf062  ALT  : " + altitude + " m");

    // Speed (tachometer)
    draw.addText(45, h - 85, white,
            "\uf3fd  SPD  : " + speed + " m/s");

    // Distance (map marker)
    draw.addText(45, h - 55, white,
            "\uf3c5  DIST : " + distance + " m");
}


    // -------------------------
    // BOTTOM RIGHT TELEMETRY
    // -------------------------
 private void drawBottomRight(ImDrawList draw, float w, float h) {
    int white = 0xFFFFFFFF;
    draw.addText(w - 150, h - 115, white,
            "\uf240  BAT : " + battery + "%");
    draw.addText(w - 150, h - 85, white,
            "\uf124  GPS : OK");
    draw.addText(w - 150, h - 55, white,
            "\uf013  MODE : STABLE");
}


    // -------------------------
    // CAMERA FOCUS (CENTER)
    // -------------------------
    private void drawCameraFocus(ImDrawList draw, float cx, float cy) {

        int white = 0xFFFFFFFF;
        float t = 3f;

        float size = 45f;
        float gap  = 14f;

        // Top Left
        draw.addLine(cx - size, cy - size, cx - gap, cy - size, white, t);
        draw.addLine(cx - size, cy - size, cx - size, cy - gap, white, t);

        // Top Right
        draw.addLine(cx + size, cy - size, cx + gap, cy - size, white, t);
        draw.addLine(cx + size, cy - size, cx + size, cy - gap, white, t);

        // Bottom Left
        draw.addLine(cx - size, cy + size, cx - gap, cy + size, white, t);
        draw.addLine(cx - size, cy + size, cx - size, cy + gap, white, t);

        // Bottom Right
        draw.addLine(cx + size, cy + size, cx + gap, cy + size, white, t);
        draw.addLine(cx + size, cy + size, cx + size, cy + gap, white, t);

        // Center Circle
        draw.addCircle(cx, cy, 9, white, 32, 3);
    }

    // -------------------------
    // REC INDICATOR
    // -------------------------
    boolean isRecordRed=true;
    private long lastBlinkTime = 0;
private static final long BLINK_INTERVAL_MS = 500; // 0.5 second

    private void drawREC(ImDrawList draw, float w) {

        int white = 0xFFFFFFFF;
        int red   = 0xFF0000FF;

        updateRecBlink();
        float x =45;
        float y = 45;

        draw.addCircleFilled(x, y + 6, 6, isRecordRed?red:white, 24);
        draw.addText(x + 15, y, white, "REC");
    }

    private void updateRecBlink() {
    long now = System.currentTimeMillis();

    if (now - lastBlinkTime >= BLINK_INTERVAL_MS) {
        isRecordRed = !isRecordRed;
        lastBlinkTime = now;
    }
}


    private void Thread(Object object, int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'Thread'");
    }

    // -------------------------
    // CAMERA FRAME CORNERS
    // -------------------------
    private void drawCameraFrame(ImDrawList draw, float w, float h) {

        int white = 0xFFFFFFFF;
        float t = 4f;
        float m = 25f;
        float c = 70f;

        // Top Left
        draw.addLine(m, m, m + c, m, white, t);
        draw.addLine(m, m, m, m + c, white, t);

        // Top Right
        draw.addLine(w - m, m, w - m - c, m, white, t);
        draw.addLine(w - m, m, w - m, m + c, white, t);

        // Bottom Left
        draw.addLine(m, h - m, m + c, h - m, white, t);
        draw.addLine(m, h - m, m, h - m - c, white, t);

        // Bottom Right
        draw.addLine(w - m, h - m, w - m - c, h - m, white, t);
        draw.addLine(w - m, h - m, w - m, h - m - c, white, t);
    }
}











// ImGuiIO io = ImGui.getIO();

// ImFontConfig config = new ImFontConfig();
// config.setMergeMode(false);

// io.getFonts().addFontFromFileTTF(
//         "res/fonts/Roboto-Regular.ttf",
//         18
// );

// // Enable merge mode for icons
// config.setMergeMode(true);

// short[] ranges = new short[]{
//         (short) 0xf000, (short) 0xf3ff, 0
// };

// io.getFonts().addFontFromFileTTF(
//         "res/fonts/fa-solid-900.ttf",
//         18,
//         config,
//         ranges
// );

// config.destroy();
