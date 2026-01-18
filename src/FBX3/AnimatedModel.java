package FBX3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.RawModel;

public class AnimatedModel {
    public List<RawModel> rawModels = new ArrayList<>();
    public Map<String, Integer> boneMap = new HashMap<>();
    public BoneInfo[] bones = new BoneInfo[100];
    public int boneCount = 0;
}
