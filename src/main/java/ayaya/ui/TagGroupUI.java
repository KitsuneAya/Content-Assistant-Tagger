package ayaya.ui;

import ayaya.abstractions.AbstractDataUI;
import ayaya.controllers.UIController;
import ayaya.UIMode;
import ayaya.data.TagData;
import ayaya.data.TagGroupData;
import ayaya.TagGroupType;
import ayaya.options.Preferences;
import ayaya.util.UtilityFunctions;
import com.formdev.flatlaf.ui.FlatRoundBorder;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.StrokeBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


public class TagGroupUI extends AbstractDataUI {


    //\\//\\//\\//\\//\\//\\
//|/  Data References      \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    private final TagGroupData data;


    //\\//\\//\\//\\//\\//\\
//|/  UI References        \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    private final TagUI                 parentTag;
    private final TagLayerUI            parentUI;
    private final ArrayList<TagUI>      subUIs              = new ArrayList<>();


    //\\//\\//\\//\\//\\//\\
//|/  UI Components        \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    private final JPanel        headerContainer             = new JPanel        (new GridBagLayout());
    private final JPanel        headerSubcontainerWest      = new JPanel        (new GridBagLayout());
    private final JPanel        headerSubcontainerEast      = new JPanel        (new GridBagLayout());
    private final JPanel        tagContainer                = new JPanel        ();
    private final JPanel        footerContainer             = new JPanel        (new GridBagLayout());

    private final JLabel        groupTypeLabel              = new JLabel        ("NA", SwingConstants.RIGHT);
    private final JButton       deleteGroupButton           = new JButton       ("✖");
    private final JTextField    groupNameField              = new JTextField    (Preferences.defaultTagGroupName);
    private final JButton       groupTypeButton             = new JButton       ("☰");
    private final JButton       addTagButton                = new JButton       ("+ New Tag");



    //\\//\\//\\//\\//\\//\\
//|/  Constructors         \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    public TagGroupUI(TagGroupData tagGroupData, TagLayerUI parentTagLayerUI, TagUI parentTag) {

        super(parentTag, parentTagLayerUI, tagGroupData);
        this.parentUI = parentTagLayerUI;
        this.parentTag = parentTag;
        this.data = tagGroupData;

        // Set Layout
        this.setLayout(new GridBagLayout());

        // Add Components
        this.initHeader();
        this.initTagContainer();
        this.initFooter();

        // Finalization
        this.setBorder(new FlatRoundBorder());
        this.addDataSubTagUIs();

    }


    //\\//\\//\\//\\//\\//\\
//|/  Initializers         \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    private void initGroupTypeLabel() {

        this.groupTypeLabel.setFont(new Font("Monospaced", Font.PLAIN, 15));
        this.groupTypeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.groupTypeLabel.setVerticalAlignment(SwingConstants.CENTER);
        this.groupTypeLabel.setToolTipText("Group Type");
        this.groupTypeLabel.setBorder(new FlatRoundBorder());

        var gbc = new GridBagConstraints(); gbc.gridx = gbc.gridy = 0;
        this.headerSubcontainerWest.add(this.groupTypeLabel, gbc);

        this.setType(this.data.getType());

    }
    private void initGroupDeleteButton() {

        this.deleteGroupButton.setForeground(Color.RED);
        this.deleteGroupButton.setBackground(Color.DARK_GRAY.darker().darker());
        this.deleteGroupButton.setFont(this.deleteGroupButton.getFont().deriveFont(12.1f));
        this.deleteGroupButton.setToolTipText("Delete this group of tags and all their data");
        this.deleteGroupButton.addActionListener(_ -> this.delete());

        var gbc = new GridBagConstraints(); gbc.gridx = gbc.gridy = 0;
        this.headerSubcontainerWest.add(this.deleteGroupButton, gbc);

        this.deleteGroupButton.setVisible(false); // It's impossible to create a TagGroup while the delete buttons are shown

    }
    private void initWestHeaderContainer() {

        var gbc = new GridBagConstraints(); gbc.gridx = 0;
        gbc.insets = UtilityFunctions.createSquarePadding(Preferences.uiGapSize);

        var preferredSize = new Dimension(Preferences.uiSize, Preferences.uiSize);
        this.headerSubcontainerWest.setPreferredSize(preferredSize);

        this.headerContainer.add(this.headerSubcontainerWest, gbc);

    }

    private void initGroupNameField() {

        this.groupNameField.setBackground(null);
        this.groupNameField.setText(this.data.getName());
        this.groupNameField.setBorder(new EmptyBorder(0, Preferences.uiGapSize, 0, 0));

        this.groupNameField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                data.setName(groupNameField.getText());
            } // Updates the TagData as the user types
        });

        var gbc = new GridBagConstraints(); gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;

        this.headerContainer.add(this.groupNameField, gbc);

    }

    private void initGroupTypeButton() {

        // Properties
        JPopupMenu typeSelectionMenu = new JPopupMenu();
        groupTypeButton.setFont(new Font("Monospaced", Font.PLAIN, 11));
        groupTypeButton.setToolTipText("Group Type Options");
        groupTypeButton.addActionListener(_ -> {
            typeSelectionMenu.show(groupTypeButton, 0, groupTypeButton.getHeight());
        });

        // Popup Menu Choices
        for (TagGroupType type : TagGroupType.values()) {
            var typeSelection = new JMenuItem(type.toString());
            typeSelectionMenu.add(typeSelection);
            typeSelection.addActionListener(_ -> this.setType(type));
        }


        var gbc = new GridBagConstraints(); gbc.gridx = gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH; gbc.weightx = gbc.weighty = 1;
        this.headerSubcontainerEast.add(groupTypeButton, gbc);

    }
    private void initEastHeaderContainer() {

        var gbc = new GridBagConstraints(); gbc.gridx = 2;
        gbc.insets = UtilityFunctions.createSquarePadding(Preferences.uiGapSize);

        var preferredSize = new Dimension(Preferences.uiSize, Preferences.uiSize);
        this.headerSubcontainerWest.setPreferredSize(preferredSize);

        this.headerContainer.add(this.headerSubcontainerEast, gbc);

    }

    private void initHeader() {

        this.initGroupTypeLabel();      // Group Type Label
        this.initGroupDeleteButton();       // Group Delete Button
        this.initWestHeaderContainer(); // Left-side Header Components Container
        this.initGroupNameField();    // Optional Name for Tags Group
        this.initGroupTypeButton();      // Group Type Selection Button w/ Dropdown Menu
        this.initEastHeaderContainer(); // Right-side Header Components Container

        var gbc = new GridBagConstraints(); gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        this.add(this.headerContainer, gbc);

    }

    private void initTagContainer() {

        this.tagContainer.setLayout(new BoxLayout(this.tagContainer, BoxLayout.Y_AXIS));

        var gbc = new GridBagConstraints(); gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        this.add(this.tagContainer, gbc);

    }
    private void initAddButton() {

        this.addTagButton.addActionListener(_ -> this.addNewSubUI());

        int horizontalPadding = Preferences.uiSize + 2 * Preferences.uiGapSize;

        var gbc = new GridBagConstraints(); gbc.gridx = gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        gbc.insets = UtilityFunctions.createHorizontalPadding(horizontalPadding);

        this.footerContainer.add(this.addTagButton, gbc);

    }

    private void initFooter() {

        this.initAddButton();

        int verticalPadding = Preferences.uiGapSize;

        var gbc = new GridBagConstraints(); gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        gbc.insets = UtilityFunctions.createVerticalPadding(verticalPadding);

        this.add(this.footerContainer, gbc);

    }



    //\\//\\//\\//\\//\\//\\
//\/  UI Methods           \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    private boolean isMoveResponseAllowed = true; // Enables/disables sub-UIs change listeners

    /**
     * Removes, and then re-adds, all TagUIs in this TagGroupUI's content pane.
     * This method was created for updating the visual order of TagUIs.
     */
    @Override public void refreshUI() {

        this.isMoveResponseAllowed = false; // Prevents an infinite recursion loop
        this.tagContainer.removeAll();

        int position = 1;
        for (var tagUI : this.subUIs) {

            tagUI.setRow(position++);
            this.tagContainer.add(tagUI);
        }

        this.revalidate();
        this.isMoveResponseAllowed = true;

    }

    @Override public void showSubUIAddButton(boolean b) {
        this.addTagButton.setVisible(b);
    }

    @Override public void showDeleteButton(boolean b) {
        this.groupTypeLabel.setVisible(!b);
        this.deleteGroupButton.setVisible(b);
    }

    private void setType(TagGroupType type) {

        this.data.setType(type);
        String typeName = switch (type) {
            case ANY -> "≥0";
            case AT_LEAST_ONE -> "≥1";
            case ONE_AND_ONLY_ONE -> "=1";
            case OPTIONAL_ONE -> "≤1";
            case NON_TAG -> "!!";
        }; this.groupTypeLabel.setText(typeName);

        String toolTipText = switch (type) {
            case ANY -> "Select any amount of tags";
            case AT_LEAST_ONE -> "Select at least one tag";
            case ONE_AND_ONLY_ONE -> "Select one tag";
            case OPTIONAL_ONE -> "Select one tag or none";
            case NON_TAG -> "Select optional tag groups to show";
        }; this.groupTypeLabel.setToolTipText(toolTipText);

    }

    public TagGroupType getType() {

        return this.data.getType();
    }

    public void setVisibility() {

        var isEditMode = UIController.getUIMode() == UIMode.EDIT;
        this.showSubUIAddButton(isEditMode);

        // The first tag layer will always have all its tag groups visible
        if (UIController.isFirstTagLayerUI(this.parentUI)) return;

        var parentTagSelected = this.parentTag.isSelected();
        var parentTagVisible = this.parentTag.getParent().isVisible();

        var isVisible = parentTagVisible && parentTagSelected || isEditMode;
        this.setVisible(isVisible);

    }



    //\\//\\//\\//\\//\\//\\
//|/  Parent UI Methods    \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    @Override public TagLayerUI getParentUI() {
        return this.parentUI;
    }


    //\\//\\//\\//\\//\\//\\
//|/  Sub-UI Methods       \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    /**
     * Creates a new TagData object in this TagGroupUI's TagGroupData,
     * and then displays it as a TagUI within this TagGroupUI.
     */
    @Override public void addNewSubUI() {
        this.addNewSubTagUI();
    }

    /// Creates a new TagData and passes it on for diplaying.
    private void addNewSubTagUI() {

        var newTagData = this.data.addNewSubData(Preferences.defaultTagName);
        this.displaySubTagUI(newTagData, true);
    }

    /// Passes existing sub-TagData on for displaying.
    private void addDataSubTagUIs() {

        for (var subTagData : this.data.getSubData())
            this.displaySubTagUI(subTagData, false);

        this.revalidate();
    }

    /// Displays the given TagData as a TagUI within this TagGroupUI.
    private void displaySubTagUI(TagData tagData, boolean revalidate) {

        var tagUI = new TagUI(this, tagData);
        tagUI.refreshUI();

        this.tagContainer.add(tagUI);
        this.subUIs.add(tagUI);

        if (revalidate) this.revalidate();
    }

    @Override public void removeSubUI(AbstractDataUI tagUI) {

        if (!(tagUI instanceof TagUI)) throw new RuntimeException("TagGroupUI.removeSubUI() was not called with a TagUI");

        this.subUIs.remove(tagUI);
        this.tagContainer.remove(tagUI);

        this.revalidate();

    }

    @Override public ArrayList<TagUI> getSubUIs() {

        return this.subUIs;
    }



    //\\//\\//\\//\\//\\//\\
//|/  Checkbox Methods     \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    /**
     * After the user selects a tag, this ensures that only the correct number of tags in this group are selected.
     * Does not prevent the user from creating an illegal selection count when changing the group's type.
     * @param checkBox The checkbox that triggered this method to run.
     */
    void checkBoxClicked(JCheckBox checkBox) {

        switch (this.data.getType()) { case ANY, NON_TAG: return; } // No validation needed for these types

        final int[] bounds = this.getTagSelectionLimits();
        final int selectedTags = this.getSelectedTagCount();

        final int minimumSelection = bounds[0];
        final int maximumSelection = bounds[1];

        if (selectedTags >= minimumSelection &&
            selectedTags <= maximumSelection)
            return;

        if (maximumSelection == 1 && selectedTags >= 2) {
            for (var tag : this.subUIs) tag.setSelected(false);
            checkBox.setSelected(true);
        } else
        if (minimumSelection == 1 && selectedTags == 0) {
            checkBox.setSelected(true);
        }

        this.refreshSubUIs();

    }

    private int getSelectedTagCount() {

        int numSelected = 0;
        for (var tagUI : this.subUIs) {
            numSelected += (tagUI.isSelected() ? 1 : 0);
        } return numSelected;

    }

    /**
     * Number at index 0 is the lower limit, index 1 is the upper limit. The limits are both inclusive.
     * @return The minimum and maximum number of tags in this group that can be selected at any one time.
     */
    private int[] getTagSelectionLimits() {

        return switch (this.data.getType()) {
            case AT_LEAST_ONE -> new int[]{1, Integer.MAX_VALUE};
            case ONE_AND_ONLY_ONE -> new int[]{1, 1};
            case OPTIONAL_ONE -> new int[]{0, 1};
            default -> new int[]{0, Integer.MAX_VALUE};
        };

    }



    //\\//\\//\\//\\//\\//\\
//|/  Tag Copying Methods  \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    public ArrayList<String> getSelectedTagsToCopy() {

        final ArrayList<String> copyTags = new ArrayList<>();

        if (this.data.getType() != TagGroupType.NON_TAG) {
            for (TagUI tag : this.subUIs)
                if (tag.isSelected())
                    copyTags.add(tag.getName());
        }

        return copyTags;

    }

    public void checkForTagSelectionViolation() {

        final int selectedTags = this.getSelectedTagCount();

        final boolean hasViolation = switch (this.data.getType()) {

            case AT_LEAST_ONE -> selectedTags == 0;
            case ONE_AND_ONLY_ONE -> selectedTags != 1;
            case OPTIONAL_ONE -> selectedTags > 1;
            default -> false;

        }; this.highlightTagGroup(hasViolation);

    }

    private void highlightTagGroup(boolean b) {

        if (b) {
            this.groupTypeLabel.setForeground(Color.RED);
            this.groupTypeLabel.setFont(this.groupTypeLabel.getFont().deriveFont(Font.BOLD));
            this.setBorder(new StrokeBorder(new BasicStroke(2.5f), Color.RED.darker()));
        } else {
            this.groupTypeLabel.setFont(this.groupTypeLabel.getFont().deriveFont(Font.PLAIN));
            this.groupTypeLabel.setForeground(UIManager.getColor("Label.foreground"));
            this.setBorder(new FlatRoundBorder());
        }

    }



    //\\//\\//\\//\\//\\//\\
//|/  Tag Sorting Methods  \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    public void moveTag(TagUI tagUI, int newIndex) {

        int currentIndex = this.subUIs.indexOf(tagUI);
        this.moveTag(currentIndex, newIndex);

    }

    public void moveTag(int currentIndex, int newIndex) {

        if (!this.isMoveResponseAllowed) return;

        int tagsIndexSize = this.subUIs.size();

        // Input Checks
        if (currentIndex == newIndex) return;
        if (currentIndex < 0 || currentIndex >= tagsIndexSize) throw new IndexOutOfBoundsException();

        // Index Normalization
        if (newIndex >= 0) newIndex = Math.min(newIndex, tagsIndexSize - 1);
        else newIndex = (tagsIndexSize + (newIndex % tagsIndexSize)) % tagsIndexSize; // allows negative indexes to work as if this was python

        //if (newIndex > currentIndex) newIndex++;
        var tagToMove = this.subUIs.remove(currentIndex);
        this.subUIs.add(newIndex, tagToMove);

        this.refreshUI();

        this.data.moveTagData(currentIndex, newIndex);

    }


}
