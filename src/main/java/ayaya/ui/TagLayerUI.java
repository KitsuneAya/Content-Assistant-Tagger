package ayaya.ui;

import ayaya.abstractions.AbstractDataUI;
import ayaya.abstractions.AbstractUI;
import ayaya.controllers.UIController;
import ayaya.options.Preferences;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class TagLayerUI extends AbstractUI {


    //\\//\\//\\//\\//\\//\\
//|/  UI References        \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    private final ArrayList<TagGroupUI> subUIs;


    //\\//\\//\\//\\//\\//\\
//|/  UI Components        \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    private final JScrollPane   scrollPane          = new JScrollPane   ();
    private final JPanel        contentPane         = new JPanel        ();
    private final JPanel        tagGroupsPane       = new JPanel        ();
    private final JButton       addTagGroupButton   = new JButton       ("+ New Tag Group");



    //\\//\\//\\//\\//\\//\\
//|/  Constructors         \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    public TagLayerUI() {

        this.subUIs = new ArrayList<>();

        this.initScrollPane();
        this.initContentPanel();

        this.setLayout(new BorderLayout());
        this.add(this.scrollPane, BorderLayout.CENTER);

    }

    @Override
    public void refreshUI() {

        for (var tagGroupUI : this.subUIs)
            tagGroupUI.setVisibility();
    }


    //\\//\\//\\//\\//\\//\\
//|/  Initializers         \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    private void initScrollPane() {

        this.scrollPane.setVerticalScrollBarPolicy  (JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.tagGroupsPane.setLayout(new GridBagLayout());
        this.scrollPane.setViewportView(this.contentPane);
    }

    private void initContentPanel() {

        this.contentPane.setLayout(new GridBagLayout());

        var gbc = new GridBagConstraints(); gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1;
        this.contentPane.add(this.tagGroupsPane, gbc);

        gbc = new GridBagConstraints(); gbc.gridy = 1;
        gbc.fill = GridBagConstraints.VERTICAL; gbc.weighty = 1;
        this.contentPane.add(Box.createVerticalGlue(), gbc);
    }

    public void displayAddButton() {

        this.addTagGroupButton.setMargin(new Insets(0, 0, 0, 0));
        this.addTagGroupButton.addActionListener(_ -> UIController.addNewParentlessTagGroup(Preferences.defaultNewGrpType));

        var gbc = new GridBagConstraints(); gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        this.contentPane.add(this.addTagGroupButton, gbc);

    }



    //\\//\\//\\//\\//\\//\\
//|/  UI Methods           \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    public void displayTagGroup(TagGroupUI tagGrpUI) {

        int horizontalPadding = 4;
        int verticalPadding = 3;

        var gbc = new GridBagConstraints(); gbc.gridx = 0; // Stacks TagGroups vertically
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        gbc.insets = new Insets(verticalPadding, horizontalPadding, 0, horizontalPadding);

        this.subUIs.add(tagGrpUI);
        this.tagGroupsPane.add(tagGrpUI, gbc);

        this.revalidate();
    }

    @Override public void removeSubUI(AbstractDataUI tagGroupUI) {

        if (!(tagGroupUI instanceof TagGroupUI)) throw new RuntimeException("TagLayerUI.removeSubUI() was not called with a TagGroupUI");

        this.subUIs.remove(tagGroupUI);
        this.tagGroupsPane.remove(tagGroupUI);

        this.revalidate();
    }

    @Override public void showSubUIAddButton(boolean b) {

        this.addTagGroupButton.setVisible(b);
    }

    public void clear() {

        this.subUIs.clear();
        this.tagGroupsPane.removeAll();

        this.revalidate();
    }



    //\\//\\//\\//\\//\\//\\
//|/  Sub-UI Methods       \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    @SuppressWarnings("unchecked")
    @Override public ArrayList<TagGroupUI> getSubUIs() {
        return this.subUIs;
    }

}
