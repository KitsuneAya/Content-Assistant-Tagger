package ayaya.ui;

import ayaya.options.Preferences;

import javax.swing.*;

public class MenuItem extends JMenuItem {

    public MenuItem(String menuName, String keyboardShortcut) {

        this.setText(menuName, keyboardShortcut);
    }

    public void setText(String menuName, String keyboardShortcut) {

        var font = this.getFont();
        int fontSize = font.getSize();
        var fontMetrics = this.getFontMetrics(font);

        int textWidth = fontMetrics.stringWidth(menuName);
        int shortcutWidth = fontMetrics.stringWidth(keyboardShortcut);
        int spacingRequired = Preferences.menuBarItemWidth - textWidth - shortcutWidth;

        int spacerWidth = 0;
        float microFontSize = 0;
        while (spacerWidth < 1) {

            var microFont = font.deriveFont(microFontSize++);
            var microFontMetrics = this.getFontMetrics(microFont);
            spacerWidth = microFontMetrics.stringWidth("\u00A0");
        }

        int spacersRequired = spacingRequired / spacerWidth;
        String spacerText = "\u00A0".repeat(spacersRequired);


        super.setText("<html>"
                + "<span style='font-size:" + fontSize + ";'>" + menuName + "</span>"
                + "<span style='font-size:" + microFontSize + ";'>" + spacerText + "</span>"
                + "<span style='font-size:" + fontSize + ";color:#888888;'>" + keyboardShortcut + "</span>"
                + "</html>"
        );

    }



}
