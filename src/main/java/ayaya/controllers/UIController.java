package ayaya.controllers;

import ayaya.TagGroupType;
import ayaya.UIMode;
import ayaya.options.Preferences;
import ayaya.ui.MainWindow;
import ayaya.ui.TagUI;
import ayaya.ui.TagGroupUI;
import ayaya.ui.TagLayerUI;

import java.util.ArrayList;
import java.util.HashMap;


public class UIController { private UIController() {}



    //\\//\\//\\//\\//\\//\\
//|/  Status Variables     \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    protected static UIMode currentUIMode = Preferences.startupMode;


    //\\//\\//\\//\\//\\//\\
//|/  UI References        \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    /**
     * The TagLayerUIs visible in the MainWindow singleton instance.
     * They are ordered by how deep within the data hierarchy their TagGroupUIs and TagUIs represent.
     */
    protected static final ArrayList<TagLayerUI> TAG_LAYERS = new ArrayList<>();

    /**
     * Keeps track of where a TagUI is within the TagLayerUI hierarchy.
     * This is more efficient than finding an index value from {@code TAG_LAYERS}.
     */
    protected static final HashMap<TagUI, Integer> TAG_MAP = new HashMap<>();


    //\\//\\//\\//\\//\\//\\
//|/  UI Registration      \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    /**
     * Makes a reference link between a TagUI and the TagLayerUI which contains it.
     * Use when instantiating a TagUI.
     */
    public static void registerUI(TagUI ui, TagLayerUI tagLayerUI) {
        int layerIndex = TAG_LAYERS.indexOf(tagLayerUI);
        TAG_MAP.put(ui, layerIndex);
    }

    /**
     * Removes the reference link of a TagUI to its containing TagLayerUI.
     * Use when deleting a TagUI.
     */
    public static void unregisterUI(TagUI ui) {
        TAG_MAP.remove(ui);
    } // TODO Use this actually


    //\\//\\//\\//\\//\\//\\
//|/  UI Status Methods    \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    public static UIMode getUIMode() {
        return currentUIMode;
    }
    public static void setUIMode(UIMode uiMode) {
        currentUIMode = uiMode;
        setVisibilities();
    }


    //\\//\\//\\//\\//\\//\\
//|/  TagLayerUI Methods   \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    /// Adds a new TagLayerUI after the current last TagLayerUI.
    public static void addTagLayerUI() {

        TagLayerUI newTagLayerUI = new TagLayerUI();
        TAG_LAYERS.add(newTagLayerUI);

        if (isFirstTagLayerUI(newTagLayerUI)) newTagLayerUI.displayAddButton();

        MainWindow.INSTANCE.getContentPane().add(newTagLayerUI);
        MainWindow.INSTANCE.getContentPane().revalidate();

    }

    /// Removes the last TagLayerUI from being displayed in the MainWindow, but does not delete its data.
    public static void removeLastTagLayerUI() {

        if (TAG_LAYERS.size() <= 1) return; // Prevents removing the first layer
        int lastCompIndex = MainWindow.INSTANCE.getContentPane().getComponentCount() - 1;
        if (lastCompIndex <= 0) return;

        var lastTagLayer = TAG_LAYERS.removeLast();
        MainWindow.INSTANCE.getContentPane().remove(lastTagLayer);
        MainWindow.INSTANCE.getContentPane().revalidate();

    }

    /// Returns true if the provided TagLayerUI is displaying the RootData.
    public static boolean isFirstTagLayerUI(TagLayerUI tagLayerUI) {
        return TAG_LAYERS.getFirst() == tagLayerUI;
    }

    /**
     * Returns the TagLayerUI immediatly following the one the TagUI is in.
     * If the TagUI is in the last TagLayerUI, returns null.
     */
    public static TagLayerUI getNextTagLayerUI(TagUI tagUI) {
        if (!nextTagLayerExists(tagUI)) return null;
        return TAG_LAYERS.get(TAG_MAP.get(tagUI) + 1);
    }

    /// Uses the map of registered TagUIs to determine if a TagLayerUI exists after the one the TagUI is in.
    public static boolean nextTagLayerExists(TagUI tagUI) {
        int nextLayerIndex = TAG_MAP.get(tagUI) + 1;
        return nextLayerIndex < TAG_LAYERS.size();
    }

    public static void removeEmptyExtraTagLayers() {

        if (Preferences.keepEmptyExtraTagLayers) return;

        int lastIndex = TAG_LAYERS.size() - 1;
        int lastAllowedEmptyIndex = Preferences.initNumOfTagLayers - 1;

        for (int i = lastIndex; i > lastAllowedEmptyIndex; i--) {
            var tagLayer = TAG_LAYERS.get(i);
            if (tagLayer.getSubUISize() == 0) removeLastTagLayerUI();
        }

    }


    //\\//\\//\\//\\//\\//\\
//|/  TagGroupUI Methods   \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    /// Instantiates new sub-data within the RootData and then creates a TagGroupUI in the first TagLayerUI to display it.
    public static void addNewParentlessTagGroup(TagGroupType type) {

        var firstTagLayerUI = TAG_LAYERS.getFirst();

        var tagGroupData = DataController.addNewParentlessTagGroupData(type);
        var tagGroupUI = new TagGroupUI(tagGroupData, firstTagLayerUI, null);

        firstTagLayerUI.displayTagGroup(tagGroupUI);

    }



    //\\//\\//\\//\\//\\//\\
//|/  UI Visibilities      \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    /**
     * Clears all TagGroupUI and TagUIs being displayed in the MainWindow,
     * and then instantiates new ones using the data currently in memory.
     */
    protected static void refreshDataDisplayed() {

        TAG_LAYERS.forEach(TagLayerUI::clear);
        TAG_MAP.clear();

        for (var tagGroupData : DataController.rootData.getSubData()) {

            var firstTagLayer = TAG_LAYERS.getFirst();
            var tagGroupUI = new TagGroupUI(tagGroupData, firstTagLayer, null);

            TAG_LAYERS.getFirst().displayTagGroup(tagGroupUI);
        }

        setVisibilities();

    }

    /**
     * Changes the visibility of TagGroupUIs and various components contained within them to
     * reflect the options available to the user given the current UIMode.
     */
    public static void setVisibilities() {

        final var isEditMode = currentUIMode == UIMode.EDIT;
        TAG_LAYERS.getFirst().showSubUIAddButton(isEditMode);

        var tagGroupUIs = new ArrayList<TagGroupUI>();
        var tagUIs = new ArrayList<TagUI>();

        TAG_LAYERS.forEach(tagLayer -> tagGroupUIs.addAll(tagLayer.getSubUIs()));
        tagGroupUIs.forEach(tagGroupUI -> tagUIs.addAll(tagGroupUI.getSubUIs()));

        tagGroupUIs.forEach(TagGroupUI::setVisibility);
        tagUIs.forEach(TagUI::refreshUI);

    }

    /// If in Edit mode, displays the delete buttons for all TagGroupUIs and TagUIs.
    public static void displayDeleteButtons(boolean b) {

        if (currentUIMode != UIMode.EDIT) return;

        for (TagLayerUI tagLayer : TAG_LAYERS)
            for (TagGroupUI tagGroup : tagLayer.getSubUIs()) {
                tagGroup.showDeleteButton(b);
                for (TagUI tag : tagGroup.getSubUIs()) tag.showDeleteButton(b);
            }

    }



}
