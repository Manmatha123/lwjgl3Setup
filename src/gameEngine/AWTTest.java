// package gameEngine;

// import javax.swing.*;
// import java.awt.*;
// import org.lwjgl.opengl.GL;
// import org.lwjgl.opengl.GL11;
// import org.lwjgl.opengl.awt.AWTGLCanvas;
// import org.lwjgl.opengl.awt.GLData;

// public class AWTTest extends AWTGLCanvas {

//     public AWTTest() throws Exception {
//         super(create());
//     }

//     static GLData create() {
//         GLData d = new GLData();
//         d.majorVersion = 3;
//         d.minorVersion = 3;
//         return d;
//     }

//     @Override
//     public void initGL() {
//         System.out.println("INIT OK");
//         GL.createCapabilities();
//         GL11.glClearColor(1,0,0,1);
//     }

//     @Override
//     public void paintGL() {
//         System.out.println("PAINT OK");
//         GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
//         swapBuffers();
//         repaint();
//     }

//     public static void main(String[] args) throws Exception {
//         JFrame f = new JFrame("TEST");
//         f.setLayout(new BorderLayout());
//         f.add(new AWTTest(), BorderLayout.CENTER);
//         f.setSize(800,600);
//         f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         f.setVisible(true);
//     }
// }
