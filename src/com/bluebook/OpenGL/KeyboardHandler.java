package com.bluebook.OpenGL;

import org.lwjgl.glfw.GLFWKeyCallback;
import static org.lwjgl.glfw.GLFW.*;

public class KeyboardHandler extends GLFWKeyCallback{

    public static boolean[] keys = new boolean[65536];

    // The GLFWKeyCallback class is an abstract method that
    // can't be instantiated by itself and must instead be extended
    //
    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        // TODO Auto-generated method stub
        keys[key] = action != GLFW_RELEASE;
    }

    // boolean method that returns true if a given key
    // is pressed.
    public static boolean isKeyDown(int keycode) {
        return keys[keycode];
    }

}