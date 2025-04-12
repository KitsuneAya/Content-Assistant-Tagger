package ayaya.ui;

import ayaya.Log;
import ayaya.controllers.UIController;
import ayaya.options.Preferences;
import ayaya.util.GlobalKeyListener;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import javax.swing.*;
import java.awt.*;


public final class MainWindow extends JFrame { private MainWindow() {}

    public static final MainWindow INSTANCE = new MainWindow();

    static {

        Log.fine("Initializing MainWindow singleton...");

        INSTANCE.setTitle("CAT - Content Assistant Tagger");
        INSTANCE.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        INSTANCE.setSizeAndLocation();
        INSTANCE.setLayout();
        INSTANCE.initContentPane();
        INSTANCE.initGlobalKeyListener();

    }

    private static void setSizeAndLocation() {

        double aspectRatio = 9f/16f;
        int windowWidth = 1280;
        int windowHeight = (int) (windowWidth * aspectRatio);
        Dimension windowDimension = new Dimension(windowWidth, windowHeight);
        INSTANCE.setSize(windowDimension);
        INSTANCE.setMinimumSize(windowDimension);
        INSTANCE.setLocationByPlatform(true);

        INSTANCE.setJMenuBar(MenuBar.INSTANCE);

    }

    private static void setLayout() {

        // This ensures TagLayerUIs will always add to the right of the previous TagLayerUI
        LayoutManager layout = new GridLayout(1, 0);
        INSTANCE.getContentPane().setLayout(layout);

    }

    private static void initContentPane() {

        int i = Preferences.initNumOfTagLayers;
        while (i-- > 0) UIController.addTagLayerUI();

    }

    private static void initGlobalKeyListener() {

        try { GlobalScreen.registerNativeHook(); }
        catch (NativeHookException ex) {
            Log.severe("There was a problem registering the native hook.");
            Log.severe(ex.getMessage());
            System.exit(1);
        } GlobalScreen.addNativeKeyListener(GlobalKeyListener.INSTANCE);

    }



}