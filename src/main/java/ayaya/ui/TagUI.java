package ayaya.ui;

import ayaya.abstractions.AbstractDataUI;
import ayaya.controllers.UIController;
import ayaya.UIMode;
import ayaya.data.TagData;
import ayaya.data.TagGroupData;
import ayaya.exceptions.NullDataException;
import ayaya.exceptions.NullParentUIException;
import ayaya.options.Preferences;
import ayaya.util.UtilityFunctions;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class TagUI extends AbstractDataUI {


    //\\//\\//\\//\\//\\//\\
//|/  Data References      \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    private final TagData data;


    //\\//\\//\\//\\//\\//\\
//|/  UI References        \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    private final TagGroupUI parentUI;
    private final ArrayList<TagGroupUI> subUIs = new ArrayList<>();


    //\\//\\//\\//\\//\\//\\
//|/  UI Components        \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    private final JPanel        leftComponentsContainer     = new JPanel        (new GridBagLayout());
    private final JPanel        rightComponentsContainer    = new JPanel        (new GridBagLayout());

    private final JCheckBox     checkBox                    = new JCheckBox     ();
    private final JSpinner      indexSpinner                = new JSpinner      ();
    private final JTextField    textField                   = new JTextField    ();
    private final JButton       addButton                   = new JButton       ("▶");
    private final JButton       deleteButton                = new JButton       ("✖");



    //\\//\\//\\//\\//\\//\\
//|/  Constructors         \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    public TagUI(TagGroupUI parentTagGroupUI, TagData tagData) {

        super(parentTagGroupUI, parentTagGroupUI, tagData);
        this.parentUI = parentTagGroupUI;
        this.data = tagData;

        if (this.parentUI == null) throw new NullParentUIException(TagUI.class);
        if (this.data == null) throw new NullDataException(TagUI.class);

        // Set Layout
        this.setLayout(new GridBagLayout());

        // Add Components
        this.addLeftComponents();
        this.addTextField();
        this.addRightComponents();

        var tagLayerUI = parentTagGroupUI.getParentUI();
        UIController.registerUI(this, tagLayerUI);

        this.addDataSubTagGroupUIs();
        this.revalidate();

    }



    //\\//\\//\\//\\//\\//\\
//|/  Initializers         \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    private void initCheckBoxAlignment() {
        this.checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        this.checkBox.setVerticalAlignment(SwingConstants.CENTER);
    }
    private void initCheckBoxToolTip() {
        var text = "Check to enable copying";
        this.checkBox.setToolTipText(text);
    }
    private void initCheckBoxActionListener() {
        this.checkBox.addActionListener(_ -> this.parentUI.checkBoxClicked(this.checkBox));
    }
    private void initCheckBoxPreferredSize() {
        int width = Preferences.uiSize;
        int height = Preferences.uiSize;
        this.checkBox.setPreferredSize(new Dimension(width, height));
    }

    private void initCheckBox(GridBagConstraints gbc) {

        this.initCheckBoxAlignment();
        this.initCheckBoxToolTip();
        this.initCheckBoxActionListener();
        this.initCheckBoxPreferredSize();

        gbc.weightx = gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        this.leftComponentsContainer.add(this.checkBox, gbc);

    }

    private void initIndexSpinnerModel() {
        var indexPosition = this.parentUI.getSubUISize() + 1;
        var integerModel = new SpinnerNumberModel(indexPosition, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        this.indexSpinner.setModel(integerModel);
    }
    private void initIndexSpinnerUI() {
        this.indexSpinner.setUI(new BasicSpinnerUI() {
            @Override protected Component createNextButton() {return null;}
            @Override protected Component createPreviousButton() {return null;}
        });
    }
    private void initIndexSpinnerChangeListener() {
        this.indexSpinner.addChangeListener(_ -> {
            int newIndex = (int) this.indexSpinner.getValue();
            if (newIndex > 0) newIndex--;
            ((TagGroupUI) this.parentUI).moveTag(this, newIndex);
        });
    }
    private void initIndexSpinnerPreferredSize() {
        this.indexSpinner.setPreferredSize(new Dimension(Preferences.uiSize*2, Preferences.uiSize));
    }

    private void initIndexSpinner(GridBagConstraints gbc) {

        this.initIndexSpinnerModel();
        this.initIndexSpinnerUI();
        this.initIndexSpinnerChangeListener();
        this.initIndexSpinnerPreferredSize();

        gbc.weightx = gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        this.leftComponentsContainer.add(this.indexSpinner, gbc);

    }

    private void addLeftComponents() {

        var gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;

        this.initIndexSpinner(gbc);
        this.initCheckBox(gbc);

        gbc.insets = UtilityFunctions.createHorizontalPadding(Preferences.uiGapSize);
        gbc.weightx = gbc.weighty = 0; gbc.fill = GridBagConstraints.NONE;

        this.add(this.leftComponentsContainer, gbc);

    }

    private void addTextField() {

        if (this.data != null) this.textField.setText(this.data.getName());

        this.textField.setToolTipText("Tag Name");

        // Updates the TagData as the user types
        this.textField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                if (data == null) throw new NullDataException(TagUI.class);
                data.setName(textField.getText());
            }
        });

        // Converts user's input to lowercase after they are done typing
        this.textField.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                textField.setText(textField.getText().toLowerCase());
            }
        });

        var gbc = new GridBagConstraints(); gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1;
        this.add(this.textField, gbc);
    }

    private void initAddButton(GridBagConstraints gbc) {

        this.addButton.setToolTipText("Add Group of Sub-tags");
        this.addButton.addActionListener(_ -> addNewSubUI());

        this.addButton.setHorizontalAlignment(SwingConstants.CENTER);
        this.addButton.setVerticalAlignment(SwingConstants.CENTER);

        this.rightComponentsContainer.add(this.addButton, gbc);
    }

    private void initDeleteButton(GridBagConstraints gbc) {

        this.deleteButton.setForeground(Color.RED);
        this.deleteButton.setBackground(Color.DARK_GRAY.darker().darker());
        this.deleteButton.setToolTipText("Delete this tag and all its sub-tags");
        this.deleteButton.addActionListener(_ -> this.delete());

        this.deleteButton.setVisible(false);
        this.rightComponentsContainer.add(this.deleteButton, gbc);

    }

    private void addRightComponents() {

        var gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;

        this.initAddButton(gbc);
        this.initDeleteButton(gbc);

        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = UtilityFunctions.createHorizontalPadding(Preferences.uiGapSize);

        this.add(this.rightComponentsContainer, gbc);
    }



    //\\//\\//\\//\\//\\//\\
//|/  UI Methods           \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    @Override public void refreshUI() {

        var currentUIMode = UIController.getUIMode();

        var isEditMode = currentUIMode == UIMode.EDIT;
        var isSortMode = currentUIMode == UIMode.SORT;

        try {
            this.checkBox.setVisible(!isSortMode);
            this.indexSpinner.setVisible(isSortMode);
            this.textField.setFocusable(isEditMode);
            this.showSubUIAddButton(isEditMode);
        } catch (Exception _) {}

        this.revalidate();

    }

    @SuppressWarnings("unchecked")
    @Override public TagGroupUI getParentUI() {
        return this.parentUI;
    }

    @Override public void showSubUIAddButton(boolean b) {
        this.addButton.setVisible(b);
    }

    @Override public void showDeleteButton(boolean b) {
        this.addButton.setVisible(!b);
        this.deleteButton.setVisible(b);
    }

    public boolean isSelected() {
        return this.checkBox.isSelected();
    }

    public void setSelected(boolean b) {
        this.checkBox.setSelected(b);
    }

    public void setRow(int position) {
        this.indexSpinner.setValue(position);
    }



    //\\//\\//\\//\\//\\//\\
//|/  Sub-UI Methods       \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    /**
     * Creates a new TagGroupData object in this TagUI's TagData,
     * and then displays it as a TagGroupUI in the next TagLayerUI.
     */
    @Override public void addNewSubUI() {
        this.addNewSubTagGroupUI();
    }

    /**
     * Creates a new TagGroupData and passes it on for diplaying.
     */
    private void addNewSubTagGroupUI() {

        var newTagGroupData = this.data.addNewSubData(Preferences.defaultNewGrpType);
        this.displaySubTagGroupUI(newTagGroupData);
    }

    /**
     * Passes existing sub-TagGroupData on for displaying.
     */
    private void addDataSubTagGroupUIs() {

        for (var subTagGroupData : this.data.getSubData())
            this.displaySubTagGroupUI(subTagGroupData);
    }

    /**
     * Displays the given TagGroupData as a TagGroupUI in the next TagLayerUI.
     */
    private void displaySubTagGroupUI(TagGroupData tagGroupData) {

        var nextTagLayer = this.getNextTagLayerUI();
        if (nextTagLayer == null) return; // Returns if cannot add a new TagLayerUI to the MainWindow

        var tagGroupUI = new TagGroupUI(tagGroupData, nextTagLayer, this);
        nextTagLayer.displayTagGroup(tagGroupUI);
        this.subUIs.add(tagGroupUI);

    }

    @SuppressWarnings("unchecked")
    @Override public ArrayList<TagGroupUI> getSubUIs() {
        return this.subUIs;
    }



    //\\//\\//\\//\\//\\//\\
//|/  Sub-UI Methods       \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    /**
     * Attempts to get the next TagLayerUI after the one this TagUI is in.
     * If there are no TagLayerUIs after this one, an attempt will be made to create one.
     * If that fails, null will be returned.
     */
    private TagLayerUI getNextTagLayerUI() {

        var nextTagLayer = UIController.getNextTagLayerUI(this);

        if (nextTagLayer == null) {
            UIController.addTagLayerUI();
            nextTagLayer = UIController.getNextTagLayerUI(this);
        }

        return nextTagLayer;
    }



    //\\//\\//\\//\\//\\//\\
//|/  Deprecated Methods   \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    @Override
    @Deprecated public void removeSubUI(AbstractDataUI tagGrpUI) {
        throw new UnsupportedOperationException("Remove sub-tags groups via their parent tag layer");
    }

    // TODO Figure out how to remove references to TagGroupUIs in TagUI since the TagGroupUI's parent is a TagLayer


}