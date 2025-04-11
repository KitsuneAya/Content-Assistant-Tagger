package ayaya.util;

import ayaya.controllers.DataController;
import ayaya.controllers.UIController;
import ayaya.ui.MainWindow;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import static com.github.kwhat.jnativehook.keyboard.NativeKeyEvent.*;


public final class GlobalKeyListener implements NativeKeyListener { private GlobalKeyListener() {}

    public static GlobalKeyListener INSTANCE = new GlobalKeyListener();

    private boolean isAltPressed = false;
    private boolean isCtrlPressed = false;
    private boolean isShiftPressed = false;


    @Override public void nativeKeyPressed(NativeKeyEvent e) {

        var keyCode = e.getKeyCode();
        this.modifierKeyControl(keyCode, true);

        // Keeps track of modifier keys without doing anything when the app is not focused.
        if (!MainWindow.INSTANCE.hasFocus()) return;

        if (isCtrlPressed) {

            if (isShiftPressed && keyCode == VC_C) DataController.copyTagsToClipboard();    // Ctrl + Shift + c
            else if (isAltPressed && keyCode == VC_S) DataController.saveDataAs();          // Ctrl + Alt + s
            else if (keyCode == VC_S) DataController.saveData();                            // Ctrl + s
            else if (keyCode == VC_O) DataController.loadData();                            // Ctrl + o

        } else if (isAltPressed) {

            UIController.displayDeleteButtons(true);
        }

        if (keyCode == VC_F1) {
            // TODO Open a help dialog
        }

    }

    @Override public void nativeKeyReleased(NativeKeyEvent e) {

        var keyCode = e.getKeyCode();
        this.modifierKeyControl(keyCode, false);

        // Keeps track of modifier keys without doing anything when the app is not focused.
        if (!MainWindow.INSTANCE.hasFocus()) return;

        if (keyCode == VC_ALT) {
            UIController.displayDeleteButtons(false);
        }

    }

    @Override public void nativeKeyTyped(NativeKeyEvent e) {
        //System.out.println("Key Typed: " + e.getKeyChar());
    }

    private void modifierKeyControl(int keyCode, boolean isPressed) {

        switch (keyCode) {
            case VC_ALT -> isAltPressed = isPressed;
            case VC_CONTROL -> isCtrlPressed = isPressed;
            case VC_SHIFT -> isShiftPressed = isPressed;
        }

    }


}
