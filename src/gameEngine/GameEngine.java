package gameEngine;

import javax.swing.*;
import java.awt.*;

public class GameEngine {
   public static void main(String[] args) throws Exception {
    JFrame frame = new JFrame("LWJGL AWT Engine");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1280, 720);

    // 1. The 3D Canvas goes in the standard Content Pane
    GameCanvas canvas = new GameCanvas();
    frame.getContentPane().add(canvas);

    // 2. Create the HUD and set it as the Glass Pane
    JPanel hud = createHUD();
    frame.setGlassPane(hud);
    hud.setVisible(true); // GlassPanes are hidden by default!

    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    // 3. Game Loop
    new Thread(() -> {
// Define a flag or check displayable status
while (frame.isDisplayable()) { 
    // Only render if the window isn't minimized to save GPU
    if (frame.getExtendedState() != JFrame.ICONIFIED) {
        canvas.render();
    }
    
    try { 
        Thread.sleep(16); // 16ms is roughly 60 FPS
    } catch (Exception e) {
        break; 
    }
}
    }).start();
}

private static JPanel createHUD() {
    // We use a custom JPanel that is transparent
    JPanel hud = new JPanel(new BorderLayout()) {
        @Override
        protected void paintComponent(Graphics g) {
            // Do not paint a background (keeps it transparent)
        }
    };
    hud.setOpaque(false);

    // --- Add your buttons here exactly as before ---
    JPanel topRow = new JPanel(new BorderLayout());
    topRow.setOpaque(false);
    
    JButton retry = new JButton("Retry");
    JButton exit = new JButton("Exit");
    JPanel buttons = new JPanel();
    buttons.setOpaque(false);
    buttons.add(retry);
    buttons.add(exit);
    
    topRow.add(buttons, BorderLayout.CENTER);
    hud.add(topRow, BorderLayout.NORTH);

    return hud;
}


}