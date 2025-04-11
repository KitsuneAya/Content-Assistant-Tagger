package ayaya.util;

import ayaya.options.Preferences;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class Filters {

    public static FileFilter getSaveFileFilter() {

        return new FileFilter() {

            @Override public boolean accept(File f) {
                return f.getName().endsWith(Preferences.saveFileExtension);
            }

            @Override public String getDescription() {
                return "Tagging Scheme Save Files (.save)";
            }

        };
    }
}
