package ayaya.controllers;

import ayaya.App;
import ayaya.Log;
import ayaya.TagGroupType;
import ayaya.data.RootData;
import ayaya.data.TagGroupData;
import ayaya.options.Preferences;
import ayaya.ui.MainWindow;
import ayaya.ui.TagGroupUI;
import ayaya.ui.TagLayerUI;
import ayaya.util.Filters;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public final class DataController { private DataController() {}


    //\\//\\//\\//\\//\\//\\
//|/  Data References      \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    /// Tagging Scheme Data Root
    static RootData rootData = new RootData("");
    static String taggingSchemaFilePath;


    //\\//\\//\\//\\//\\//\\
//|/  Loading Methods      \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    /// Clears currently loaded data and instantiates a new RootData object in its place.
    public static void newData() {

        rootData = new RootData("");
        taggingSchemaFilePath = null;
        UIController.refreshDataDisplayed();
    }

    /// Opens a JFileChooser dialog and prompts the user to pick a serialized save file.
    public static void loadData() {

        // FileChooser Setup
        JFileChooser chooser = new JFileChooser();

        chooser.setCurrentDirectory         (App.DEFAULT_SAVE_FOLDER);
        chooser.setApproveButtonText        ("Load");
        chooser.setFileFilter               (Filters.getSaveFileFilter());
        chooser.removeChoosableFileFilter   (chooser.getAcceptAllFileFilter());

        // Prompt the user to choose a save name and location
        int returnVal = chooser.showOpenDialog(MainWindow.INSTANCE);
        if (returnVal != JFileChooser.APPROVE_OPTION) return;

        // Get the chosen file path and pass to saveData(String filePath)
        String filePath = chooser.getSelectedFile().getAbsolutePath();

        try (Scanner sc = new Scanner(new File(filePath))) {
            String json = sc.nextLine();
            var objectMapper = new ObjectMapper();
            rootData = objectMapper.readValue(json, RootData.class);
        } catch (Exception _) {
            Log.warning("Tagging Scheme could not be loaded.");
        }

        UIController.refreshDataDisplayed();

    }



    //\\//\\//\\//\\//\\//\\
//|/  Saving Methods       \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    /// Opens a JFileChooser dialog and prompts the user to pick a file name and location to save the current tagging scheme data.
    public static void saveDataAs() {

        // Create the save directory if needed
        App.DEFAULT_SAVE_FOLDER.mkdir();

        // FileChooser Setup
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(App.DEFAULT_SAVE_FOLDER);
        chooser.setApproveButtonText("Save");
        chooser.setFileFilter(Filters.getSaveFileFilter());
        chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());

        // Prompt the user to choose a save name and location
        int returnVal = chooser.showOpenDialog(MainWindow.INSTANCE);
        if (returnVal != JFileChooser.APPROVE_OPTION) return;

        // Get the chosen file path and pass to saveData(String filePath)
        String filePath = chooser.getSelectedFile().getAbsolutePath();
        saveData(filePath);

    }

    /// Saves over the last loaded save file with the current tagging scheme data.
    public static void saveData() {
        if (taggingSchemaFilePath == null) saveDataAs();
        else saveData(taggingSchemaFilePath);
    }

    /// Saves the current tagging scheme data to the provided path.
    public static void saveData(String filePath) {

        // Append the save extension in case it's missing
        if (!filePath.endsWith(Preferences.saveFileExtension)) filePath += Preferences.saveFileExtension;

        // Serialize and save the tagging scheme
        try (FileWriter fw = new FileWriter(filePath)) {

            var objectMapper = new ObjectMapper();
            var json = objectMapper.writeValueAsString(rootData);
            fw.write(json);
            taggingSchemaFilePath = filePath; // Only assigned if write succeeds

        } catch (Exception e) {

            Log.warning("Saving data failed.");
            Log.info(e.toString());
        }

    }



    //\\//\\//\\//\\//\\//\\
//|/  Naming Methods       \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    // TODO Implement Tagging Scheme Names
    public static void setTaggingSchemeName(String name) {
        rootData.setName(name);
    }
    public static String getTaggingSchemeName() {
        return rootData.getName();
    }


    //\\//\\//\\//\\//\\//\\
//|/  RootData Methods     \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    /// Creates and returns a new TagGroupData object in the RootData of the tagging scheme.
    public static TagGroupData addNewRootTagGroupData(TagGroupType type) {
        return rootData.addNewSubData(type);
    }

    // This is sort of a hybrid of UIController and DataController,
    // but it ultimately copies data to the clipboard so it's put here.
    public static void copyTagsToClipboard() {

        try {
            StringBuilder tagsToCopy = new StringBuilder();

            // Highlights all tag selection problems
            for (TagLayerUI tagLayerUI : UIController.TAG_LAYERS)
                for (TagGroupUI tagGroupUI : tagLayerUI.getSubUIs())
                    if (tagGroupUI.isVisible()) // Ignore hidden selected tags
                        tagGroupUI.checkForTagSelectionViolation();

            for (TagLayerUI tagLayerUI : UIController.TAG_LAYERS)
                for (TagGroupUI tagGroupUI : tagLayerUI.getSubUIs())
                    if (tagGroupUI.isVisible()) // Ignore hidden selected tags
                        if (tagGroupUI.getType() != TagGroupType.NON_TAG) // Don't include non-tags
                            for (String selectedTag : tagGroupUI.getSelectedTagsToCopy())
                                tagsToCopy.append(selectedTag).append('\n');

            // Remove the last new line character
            if (tagsToCopy.toString().endsWith("\n")) tagsToCopy.deleteCharAt(tagsToCopy.length() - 1);

            StringSelection tags = new StringSelection(tagsToCopy.toString());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(tags, null);

        } catch (Exception _) {
            Toolkit.getDefaultToolkit().beep();
        }

    }

}
