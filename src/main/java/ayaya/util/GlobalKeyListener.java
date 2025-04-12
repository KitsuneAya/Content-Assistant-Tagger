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

        // TODO Make this function correctly
        // Keeps track of modifier keys without doing anything when the app is not focused.
        // if (!MainWindow.INSTANCE.hasFocus()) return;

        if (isCtrlPressed) {

            if (isShiftPressed) {
                if (keyCode == VC_C) DataController.copyTagsToClipboard();          // Ctrl + Shift + C
            }
            else if (isAltPressed) {
                if (keyCode == VC_S) DataController.saveDataAs();                   // Ctrl + Alt + S
            }
            else switch (keyCode) {
                case VC_S -> DataController.saveData();                             // Ctrl + S
                case VC_O -> DataController.loadData();                             // Ctrl + O
                case VC_T -> UIController.addTagLayerUI();                          // Ctrl + T
                case VC_W -> UIController.removeLastTagLayerUI();                   // Ctrl + W
            }

        }
        else if (isAltPressed) {

            UIController.displayDeleteButtons(true);

        }

        if (keyCode == VC_F1) {
            // TODO Open a help dialog
        }

    }

    @Override public void nativeKeyReleased(NativeKeyEvent e) {

        var keyCode = e.getKeyCode();
        this.modifierKeyControl(keyCode, false);

        // TODO Make this function correctly
        // Keeps track of modifier keys without doing anything when the app is not focused.
        // if (!MainWindow.INSTANCE.hasFocus()) return;

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
