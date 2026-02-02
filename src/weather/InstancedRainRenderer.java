// package weather;

// import java.nio.FloatBuffer;

// import org.joml.Matrix4f;
// import org.joml.Vector3f;
// import org.lwjgl.BufferUtils;
// import org.lwjgl.opengl.GL11;
// import org.lwjgl.opengl.GL15;
// import org.lwjgl.opengl.GL20;
// import org.lwjgl.opengl.GL30;
// import org.lwjgl.opengl.GL33;

// import renderEngine.Loader;

// public class InstancedRainRenderer {

//     private static final int MAX_DROPS = 50000;
//     private static final int FLOATS_PER_INSTANCE = 7;

//     private int vao;
//     private int quadVbo;
//     private int instanceVbo;
//     private RainInstancedShader shader;

//     private FloatBuffer instanceBuffer =
//             BufferUtils.createFloatBuffer(MAX_DROPS * FLOATS_PER_INSTANCE);

//     private int instanceCount = 0;

//     public InstancedRainRenderer(Loader loader, Matrix4f projection) {

//         // Quad
//         float[] quad = {
//                 -0.5f,  0.5f,
//                 -0.5f, -0.5f,
//                  0.5f,  0.5f,
//                  0.5f, -0.5f
//         };

//         vao = loader.createVAO();
//         quadVbo = loader.storeDataInAttributeList(0, 2, quad);

//         // Instance buffer
//         instanceVbo = GL15.glGenBuffers();
//         GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, instanceVbo);
//         GL15.glBufferData(
//                 GL15.GL_ARRAY_BUFFER,
//                 MAX_DROPS * FLOATS_PER_INSTANCE * Float.BYTES,
//                 GL15.GL_STREAM_DRAW
//         );

//         int stride = FLOATS_PER_INSTANCE * Float.BYTES;

//         // pos (3)
//         GL20.glVertexAttribPointer(1, 3, GL11.GL_FLOAT, false, stride, 0);
//         GL33.glVertexAttribDivisor(1, 1);

//         // scale (1)
//         GL20.glVertexAttribPointer(2, 1, GL11.GL_FLOAT, false, stride, 3 * 4);
//         GL33.glVertexAttribDivisor(2, 1);

//         // velocity (3)
//         GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, stride, 4 * 4);
//         GL33.glVertexAttribDivisor(3, 1);

//         GL30.glBindVertexArray(0);

//         shader = new RainInstancedShader();
//         shader.start();
//         shader.loadProjectionMatrix(projection);
//         shader.stop();
//     }


//     public void addDrop(Vector3f pos, float scale, Vector3f vel) {
//     instanceBuffer.put(pos.x).put(pos.y).put(pos.z);
//     instanceBuffer.put(scale);
//     instanceBuffer.put(vel.x).put(vel.y).put(vel.z);
//     instanceCount++;
// }


// }
