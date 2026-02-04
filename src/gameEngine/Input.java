package gameEngine;


import java.util.HashSet;
import java.util.Set;

public class Input {

    private static Set<Integer> keys = new HashSet<>();

    public static void press(int key){
        keys.add(key);
    }

    public static void release(int key){
        keys.remove(key);
    }

    public static boolean isDown(int key){
        return keys.contains(key);
    }
}
