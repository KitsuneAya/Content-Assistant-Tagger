package ayaya;

import ayaya.ui.MainWindow;
import com.formdev.flatlaf.intellijthemes.FlatSpacegrayIJTheme;

import javax.swing.*;
import java.io.File;

public class App {
    
    public static final File DEFAULT_SAVE_FOLDER = new File(System.getProperty("user.dir") + "/saves");

    public static void main(String[] args) throws Exception {

        Log.fine("Setting GUI look and feel...");
        UIManager.setLookAndFeel(new FlatSpacegrayIJTheme());

        var mainWindow = MainWindow.INSTANCE;
        mainWindow.setVisible(true);

    }

}