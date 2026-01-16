package FBX3;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVertexWeight;
import org.lwjgl.assimp.Assimp;

public class AssimpLoader {

    public static AnimatedModel load(String path) {

        AIScene scene = Assimp.aiImportFile(
                path,
                Assimp.aiProcess_Triangulate |
                Assimp.aiProcess_GenNormals |
                Assimp.aiProcess_FlipUVs |
                Assimp.aiProcess_LimitBoneWeights
        );

        if (scene == null)
            throw new RuntimeException("Failed to load FBX");

        AnimatedModel model = new AnimatedModel();

        for (int i = 0; i < scene.mNumMeshes(); i++) {
            AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(i));
            model.meshes.add(processMesh(aiMesh, model));
        }

        return model;
    }

    private static AnimatedMesh processMesh(AIMesh mesh, AnimatedModel model) {

        Vertex[] vertices = new Vertex[mesh.mNumVertices()];

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vertex();
            
            AIVector3D meshdata=mesh.mVertices().get(i);
            AIVector3D normals=mesh.mNormals().get(i);

            vertices[i].position = new Vector3f(meshdata.x(),meshdata.y(), meshdata.z());
            vertices[i].normal   = new Vector3f(normals.x(), normals.y(), normals.z());
        }

        extractBones(mesh, vertices, model);

        // ---- VAO creation omitted for clarity ----

        AnimatedMesh animatedMesh = new AnimatedMesh();
        animatedMesh.vertexCount = mesh.mNumVertices();
        return animatedMesh;
    }

private static void extractBones(AIMesh mesh, Vertex[] vertices, AnimatedModel model) {

    for (int i = 0; i < mesh.mNumBones(); i++) {

        AIBone aiBone = AIBone.create(mesh.mBones().get(i));
        String name = aiBone.mName().dataString();

        int boneId = model.boneMap.computeIfAbsent(name, n -> {
            BoneInfo info = new BoneInfo();
            info.offset = toMatrix4f(aiBone.mOffsetMatrix());
            model.bones[model.boneCount] = info;
            return model.boneCount++;
        });

        AIVertexWeight.Buffer weights = aiBone.mWeights();
        for (int w = 0; w < aiBone.mNumWeights(); w++) {
            AIVertexWeight weight = weights.get(w);
            vertices[weight.mVertexId()]
                    .addBoneData(boneId, weight.mWeight());
        }
    }
}


private static Matrix4f toMatrix4f(AIMatrix4x4 ai) {
    Matrix4f m = new Matrix4f();

    m.m00(ai.a1()); m.m01(ai.b1()); m.m02(ai.c1()); m.m03(ai.d1());
    m.m10(ai.a2()); m.m11(ai.b2()); m.m12(ai.c2()); m.m13(ai.d2());
    m.m20(ai.a3()); m.m21(ai.b3()); m.m22(ai.c3()); m.m23(ai.d3());
    m.m30(ai.a4()); m.m31(ai.b4()); m.m32(ai.c4()); m.m33(ai.d4());

    return m;
}

}
