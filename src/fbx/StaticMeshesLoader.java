package fbx;


import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class StaticMeshesLoader {
    public static Mesh[] load(String resourcePath, String texturesDir) throws Exception {
        return load(resourcePath, texturesDir, Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenSmoothNormals);
    }

    public static Mesh[] load(String resourcePath, String texturesDir, int flags) throws Exception {
        AIScene aiScene = Assimp.aiImportFile(resourcePath, flags);
        if (aiScene == null) throw new Exception(Assimp.aiGetErrorString());

        List<Material> materials = new ArrayList<>();
        int numMaterials = aiScene.mNumMaterials();
        try (MemoryStack stack = MemoryStack.stackPush()) {
            PointerBuffer aiMaterials = aiScene.mMaterials();
            for (int i = 0; i < numMaterials; i++) {
                AIMaterial aiMaterial = AIMaterial.create(aiMaterials.get(i));
                Material material = new Material();
                materials.add(material);
            }
        }

        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes();
        Mesh[] meshes = new Mesh[numMeshes];
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            meshes[i] = processMesh(aiMesh, materials);
        }

        Assimp.aiReleaseImport(aiScene);
        return meshes;
    }

    private static Mesh processMesh(AIMesh aiMesh, List<Material> materials) {
        List<Float> vertices = new ArrayList<>(); processVertices(aiMesh, vertices);
        List<Float> textures = new ArrayList<>(); processTextCoords(aiMesh, textures);
        List<Float> normals = new ArrayList<>(); processNormals(aiMesh, normals);
        List<Integer> indices = new ArrayList<>(); processIndices(aiMesh, indices);

        float[] v = Utils.lFloatToArray(vertices);
        float[] t = Utils.lFloatToArray(textures);
        float[] n = Utils.lFloatToArray(normals);
        int[] i = Utils.lIntToArray(indices);

        Mesh mesh = new Mesh(v, t, n, i);
        mesh.setMaterial(aiMesh.mMaterialIndex() < materials.size() ? materials.get(aiMesh.mMaterialIndex()) : null);
        return mesh;
    }

    private static void processVertices(AIMesh aiMesh, List<Float> vertices) {
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            vertices.add(aiVertex.x()); vertices.add(aiVertex.y()); vertices.add(aiVertex.z());
        }
    }

    private static void processNormals(AIMesh aiMesh, List<Float> normals) {
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();
        while (aiNormals.remaining() > 0) {
            AIVector3D aiNormal = aiNormals.get();
            normals.add(aiNormal.x()); normals.add(aiNormal.y()); normals.add(aiNormal.z());
        }
    }

    private static void processTextCoords(AIMesh aiMesh, List<Float> textures) {
        AIVector3D.Buffer aiTextCoords = aiMesh.mTextureCoords(0);
        if (aiTextCoords == null) return;
        while (aiTextCoords.remaining() > 0) {
            AIVector3D aiTextCoord = aiTextCoords.get();
            textures.add(aiTextCoord.x()); textures.add(aiTextCoord.y());
        }
    }

    private static void processIndices(AIMesh aiMesh, List<Integer> indices) {
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        while (aiFaces.remaining() > 0) {
            AIFace aiFace = aiFaces.get();
            IntBuffer buffer = aiFace.mIndices();
            while (buffer.remaining() > 0) indices.add(buffer.get());
        }
    }
}
