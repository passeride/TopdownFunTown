/*
package com.bluebook.OpenGL;

import com.bluebook.graphics.Sprite;
import com.bluebook.graphics.SpriteLoader;
import com.sun.prism.PixelFormat;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.nio.*;

import static javafx.scene.image.PixelFormat.getByteRgbInstance;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class HelloWorld {

    private static final int WIDTH = 800, HEIGHT = 600;

    public int xOfsett  = 0;
    // The window handle
    private long window;
    // This prevents our window from crashing later on.
    private GLFWKeyCallback keyCallback;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        keyCallback.free();

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(WIDTH, 600, "Hello World!", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // Sets our keycallback to equal our newly created Input class()
        glfwSetKeyCallback(window, keyCallback = new KeyboardHandler());


    }


    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        // Set the clear color
//        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);


        glMatrixMode(GL_PROJECTION);
        glOrtho(0, WIDTH, HEIGHT, 0, -1, 1); //2D projection matrix
        glMatrixMode(GL_MODELVIEW);

        glClearColor(0, 1, 0, 0); //Green clear color

        File f = new File("./assets/sprite/friendlies/character_0" + ".png");
        File f1 = new File("./assets/sprite/friendlies/senik.png");

        System.out.println(f.getAbsolutePath());
        Image img = null;
        try {
            img = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }


        BufferedImage test = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        BufferedImage test2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);

        try {
            test = ImageIO.read(f);
            test2 = ImageIO.read(f1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int textureID = loadTexture(test);
        int textureID2 = loadTexture(test2);

        glEnable(GL_TEXTURE_2D); //Enable texturing

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( !glfwWindowShouldClose(window) ) {

            if (KeyboardHandler.isKeyDown(GLFW_KEY_SPACE)) {
                xOfsett += 10;
            }

            glClear(GL_COLOR_BUFFER_BIT);

            //Enable blending so the green background can be seen through the texture
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

            glPushMatrix();
            glTranslatef(xOfsett, 0, 0);
            glBindTexture(GL_TEXTURE_2D, textureID);
            glBegin(GL_QUADS);
            {
                glTexCoord2f(0, 0);
                glVertex2f(0, 0);

                glTexCoord2f(1, 0);
                glVertex2f(WIDTH, 0);

                glTexCoord2f(1, 1);
                glVertex2f(WIDTH, HEIGHT);

                glTexCoord2f(0, 1);
                glVertex2f(0, HEIGHT);
            }
            glEnd();
            glPopMatrix();

            glPushMatrix();
            glTranslatef(100, 50, 0);
            glBindTexture(GL_TEXTURE_2D, textureID2);
            glBegin(GL_QUADS);
            {
                glTexCoord2f(0, 0);
                glVertex2f(0, 0);

                glTexCoord2f(1, 0);
                glVertex2f(128, 0);

                glTexCoord2f(1, 1);
                glVertex2f(128, 128);

                glTexCoord2f(0, 1);
                glVertex2f(0, 128);
            }
            glEnd();
            glPopMatrix();

            glfwSwapBuffers(window);

            glfwPollEvents();

        }
    }

    private static final int BYTES_PER_PIXEL = 4;
    public static int loadTexture(BufferedImage image){

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); //4 for RGBA, 3 for RGB

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS

        // You now have a ByteBuffer filled with the color data of each pixel.
        // Now just create a texture ID and bind it. Then you can load it using
        // whatever OpenGL method you want, for example:

        int textureID = glGenTextures(); //Generate texture ID
        glBindTexture(GL_TEXTURE_2D, textureID); //Bind texture ID

        //Setup wrap mode
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        //Setup texture scaling filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        //Send texel data to OpenGL
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        //Return the texture ID so we can bind it later again
        return textureID;
    }


    public static void main(String[] args) {
        new HelloWorld().run();
    }

}*/
