package FBX3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.assimp.AIAnimation;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINodeAnim;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.AIVertexWeight;
import org.lwjgl.assimp.Assimp;

import models.RawModel;
import renderEngine.Loader;

public class AssimpLoader {

    public static FBXResult load(String path, Loader loader) {

        AIScene scene = Assimp.aiImportFile(
                path,
                Assimp.aiProcess_Triangulate |
                        Assimp.aiProcess_GenNormals |
                        Assimp.aiProcess_FlipUVs |
                        Assimp.aiProcess_LimitBoneWeights);

        if (scene == null)
            throw new RuntimeException("Failed to load FBX");

        AnimatedModel model = new AnimatedModel();

        for (int i = 0; i < scene.mNumMeshes(); i++) {
            AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(i));
            model.rawModels.add(processMesh(aiMesh, model, loader));
        }

        Animation animation = loadAnimation(scene, model);
        if (animation != null) {
            animation.rootNode = processNode(scene.mRootNode());
        }

        FBXResult result = new FBXResult();
        result.model = model;
        result.animation = animation;

        return result;
    }

    private static RawModel processMesh(AIMesh mesh, AnimatedModel model, Loader loader) {

        Vertex[] vertices = new Vertex[mesh.mNumVertices()];

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vertex();

            AIVector3D pos = mesh.mVertices().get(i);
            AIVector3D normals = mesh.mNormals().get(i);

            vertices[i].position = new Vector3f(pos.x(), pos.y(), pos.z());
            vertices[i].normal = new Vector3f(normals.x(), normals.y(), normals.z());

            if (mesh.mTextureCoords(0) != null) {
                AIVector3D uv = mesh.mTextureCoords(0).get(i);
                vertices[i].texCoord = new Vector2f(uv.x(), uv.y());
            } else {
                vertices[i].texCoord = new Vector2f(0, 0);
            }
        }

        extractBones(mesh, vertices, model);

        int vertexCount = mesh.mNumVertices();

        float[] positions = new float[vertexCount * 3];
        float[] normals = new float[vertexCount * 3];
        float[] texCoords = new float[vertexCount * 2];
        int[] boneIds = new int[vertexCount * 4];
        float[] weights = new float[vertexCount * 4];
        List<Vector3f> verticesList = new ArrayList<>();

        for (int i = 0; i < vertexCount; i++) {
            Vertex v = vertices[i];

            positions[i * 3] = v.position.x;
            positions[i * 3 + 1] = v.position.y;
            positions[i * 3 + 2] = v.position.z;

            verticesList.add(new Vector3f(v.position));

            normals[i * 3] = v.normal.x;
            normals[i * 3 + 1] = v.normal.y;
            normals[i * 3 + 2] = v.normal.z;

            texCoords[i * 2] = v.texCoord.x;
            texCoords[i * 2 + 1] = v.texCoord.y;

            for (int j = 0; j < 4; j++) {
                boneIds[i * 4 + j] = v.boneIds[j];
                weights[i * 4 + j] = v.weights[j];
            }
        }

        int[] indices = new int[mesh.mNumFaces() * 3];
        int index = 0;

        for (int i = 0; i < mesh.mNumFaces(); i++) {
            indices[index++] = mesh.mFaces().get(i).mIndices().get(0);
            indices[index++] = mesh.mFaces().get(i).mIndices().get(1);
            indices[index++] = mesh.mFaces().get(i).mIndices().get(2);
        }

        RawModel rawModel = loader.loadAnimatedVAO(verticesList, positions, texCoords, normals, indices, boneIds,
                weights);

        return rawModel;
    }

    private static Node processNode(org.lwjgl.assimp.AINode aiNode) {

        Node node = new Node();
        node.name = aiNode.mName().dataString();
        node.transform = toMatrix4f(aiNode.mTransformation());

        for (int i = 0; i < aiNode.mNumChildren(); i++) {
            node.children.add(
                    processNode(
                            org.lwjgl.assimp.AINode.create(aiNode.mChildren().get(i))));
        }
        return node;
    }

    private static Animation loadAnimation(AIScene scene, AnimatedModel model) {

        if (scene.mNumAnimations() == 0)
            return null;

        AIAnimation aiAnim = AIAnimation.create(scene.mAnimations().get(0));

        Animation animation = new Animation();

        animation.duration = (float) aiAnim.mDuration();
        animation.ticksPerSecond = aiAnim.mTicksPerSecond() != 0
                ? (float) aiAnim.mTicksPerSecond()
                : 25.0f;

        animation.bones = new HashMap<>();

        for (int i = 0; i < aiAnim.mNumChannels(); i++) {

            AINodeAnim channel = AINodeAnim.create(aiAnim.mChannels().get(i));

            String boneName = channel.mNodeName().dataString();

            Integer boneId = model.boneMap.get(boneName);
            if (boneId == null)
                continue;

            Bone bone = new Bone();
            bone.name = boneName;
            bone.id = boneId;

            bone.positions = extractPositions(channel);
            bone.rotations = extractRotations(channel);
            bone.scales = extractScales(channel);

            animation.bones.put(boneName, bone);
        }

        return animation;
    }

    private static List<VectorKey> extractPositions(AINodeAnim channel) {
        List<VectorKey> list = new ArrayList<>();
        for (int i = 0; i < channel.mNumPositionKeys(); i++) {
            var key = channel.mPositionKeys().get(i);
            list.add(new VectorKey(
                    (float) key.mTime(),
                    new Vector3f(key.mValue().x(), key.mValue().y(), key.mValue().z())));
        }
        return list;
    }

    private static List<QuatKey> extractRotations(AINodeAnim channel) {
        List<QuatKey> list = new ArrayList<>();
        for (int i = 0; i < channel.mNumRotationKeys(); i++) {
            var key = channel.mRotationKeys().get(i);
            list.add(new QuatKey(
                    (float) key.mTime(),
                    new Quaternionf(
                            key.mValue().x(),
                            key.mValue().y(),
                            key.mValue().z(),
                            key.mValue().w())));
        }
        return list;
    }

    private static List<VectorKey> extractScales(AINodeAnim channel) {
        List<VectorKey> list = new ArrayList<>();
        for (int i = 0; i < channel.mNumScalingKeys(); i++) {
            var key = channel.mScalingKeys().get(i);
            list.add(new VectorKey(
                    (float) key.mTime(),
                    new Vector3f(key.mValue().x(), key.mValue().y()-3f, key.mValue().z())));
        }
        return list;
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

        m.m00(ai.a1());
        m.m01(ai.b1());
        m.m02(ai.c1());
        m.m03(ai.d1());
        m.m10(ai.a2());
        m.m11(ai.b2());
        m.m12(ai.c2());
        m.m13(ai.d2());
        m.m20(ai.a3());
        m.m21(ai.b3());
        m.m22(ai.c3());
        m.m23(ai.d3());
        m.m30(ai.a4());
        m.m31(ai.b4());
        m.m32(ai.c4());
        m.m33(ai.d4());

        return m;
    }

}
