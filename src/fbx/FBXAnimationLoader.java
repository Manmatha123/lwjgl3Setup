package fbx;

package your.package;

import org.lwjgl.assimp.*;
import org.joml.Matrix4f;
import java.util.*;

public class FBXAnimationLoader extends StaticMeshesLoader {
    public static AnimGameItem loadAnimGameItem(String resourcePath, String texturesDir) throws Exception {
        AIScene aiScene = Assimp.aiImportFile(resourcePath, 
            Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenSmoothNormals | 
            Assimp.aiProcess_LimitBoneWeights);
        if (aiScene == null) throw new Exception(Assimp.aiGetErrorString());

        List<Material> materials = loadMaterials(aiScene, texturesDir);
        List<Bone> boneList = new ArrayList<>();
        Mesh[] meshes = loadMeshesWithBones(aiScene, materials, boneList);
        
        Node rootNode = buildNodeHierarchy(aiScene.mRootNode());
        Map<String, Animation> animations = loadAnimations(aiScene, boneList);

        Assimp.aiReleaseImport(aiScene);
        return new AnimGameItem(meshes, rootNode, animations, boneList);
    }

    private static Mesh[] loadMeshesWithBones(AIScene aiScene, List<Material> materials, 
                                            List<Bone> boneList) {
        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes();
        Mesh[] meshes = new Mesh[numMeshes];
        
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            List<Integer> boneIds = new ArrayList<>();
            List<Float> weights = new ArrayList<>();
            meshes[i] = processAnimMesh(aiMesh, materials, boneList, boneIds, weights);
        }
        return meshes;
    }

    private static Mesh processAnimMesh(AIMesh aiMesh, List<Material> materials, 
                                      List<Bone> boneList, List<Integer> boneIds, List<Float> weights) {
        List<Float> vertices = new ArrayList<>(); processVertices(aiMesh, vertices);
        List<Float> textures = new ArrayList<>(); processTextCoords(aiMesh, textures);
        List<Float> normals = new ArrayList<>(); processNormals(aiMesh, normals);
        List<Integer> indices = new ArrayList<>(); processIndices(aiMesh, indices);

        processBones(aiMesh, boneList, boneIds, weights, vertices.size() / 3);

        float[] v = Utils.lFloatToArray(vertices);
        float[] t = Utils.lFloatToArray(textures);
        float[] n = Utils.lFloatToArray(normals);
        int[] i = Utils.lIntToArray(indices);
        int[] bIds = Utils.lIntToArray(boneIds);
        float[] w = Utils.lFloatToArray(weights);

        Mesh mesh = new Mesh(v, t, n, i, bIds, w);
        mesh.setMaterial(aiMesh.mMaterialIndex() < materials.size() ? materials.get(aiMesh.mMaterialIndex()) : null);
        return mesh;
    }

    private static void processBones(AIMesh aiMesh, List<Bone> boneList, List<Integer> boneIds, 
                                   List<Float> weights, int numVertices) {
        Map<Integer, List<VertexWeight>> weightSet = new HashMap<>();
        
        int numBones = aiMesh.mNumBones();
        PointerBuffer aiBones = aiMesh.mBones();
        for (int i = 0; i < numBones; i++) {
            AIBone aiBone = AIBone.create(aiBones.get(i));
            int id = boneList.size();
            boneList.add(new Bone(id, aiBone.mName().dataString(), toJomlMatrix(aiBone.mOffsetMatrix())));
            
            AIVertexWeight.Buffer aiWeights = aiBone.mWeights();
            for (int j = 0; j < aiBone.mNumWeights(); j++) {
                AIVertexWeight vw = aiWeights.get(j);
                weightSet.computeIfAbsent(vw.mVertexId(), k -> new ArrayList<>())
                        .add(new VertexWeight(id, vw.mVertexId(), vw.mWeight()));
            }
        }

        // Fill per-vertex bone data (4 weights max per vertex)
        for (int i = 0; i < numVertices; i++) {
            List<VertexWeight> vws = weightSet.getOrDefault(i, new ArrayList<>());
            for (int j = 0; j < Mesh.MAX_WEIGHTS; j++) {
                if (j < vws.size()) {
                    boneIds.add(vws.get(j).boneId);
                    weights.add(vws.get(j).weight);
                } else {
                    boneIds.add(0);
                    weights.add(0.0f);
                }
            }
        }
    }

    private static Matrix4f toJomlMatrix(AIMatrix4x4 aiMatrix) {
        return new Matrix4f(aiMatrix.a1(), aiMatrix.b1(), aiMatrix.c1(), aiMatrix.d1(),
                           aiMatrix.a2(), aiMatrix.b2(), aiMatrix.c2(), aiMatrix.d2(),
                           aiMatrix.a3(), aiMatrix.b3(), aiMatrix.c3(), aiMatrix.d3(),
                           aiMatrix.a4(), aiMatrix.b4(), aiMatrix.c4(), aiMatrix.d4());
    }

    private static Node buildNodeHierarchy(AINode aiNode) {
        Node node = new Node();
        node.name = aiNode.mName().dataString();
        node.transformation = toJomlMatrix(aiNode.mTransformation());
        node.meshIndex = aiNode.mNumMeshes() > 0 ? aiNode.mMeshes().get(0) : -1;

        int numChildren = aiNode.mNumChildren();
        PointerBuffer aiChildren = aiNode.mChildren();
        for (int i = 0; i < numChildren; i++) {
            node.children.add(buildNodeHierarchy(AINode.create(aiChildren.get(i))));
        }
        return node;
    }

    // Animation processing methods would go here (simplified for brevity)
}
