package ayaya.ui;

import ayaya.controllers.DataController;
import ayaya.controllers.UIController;
import ayaya.UIMode;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("all")
public final class MenuBar extends JMenuBar {

    public static final MenuBar INSTANCE = new MenuBar();

    private MenuBar() {
        this.initFileMenu();
        this.initEditMenu();
        this.initHelpMenu();
    }

    private void initFileMenu() {

        JMenu fileMenu = new JMenu("File");
        fileMenu.setName("File Menu");
        fileMenu.setMnemonic('F');

        JMenuItem newFileMI = new JMenuItem("New");
        newFileMI.addActionListener(_ -> DataController.newData());
        fileMenu.add(newFileMI);

        JMenuItem openMI = new JMenuItem("Open");
        openMI.addActionListener(_ -> DataController.loadData());
        fileMenu.add(openMI);

        JMenuItem saveMI = new JMenuItem("Save");
        saveMI.addActionListener(_ -> DataController.saveData());
        fileMenu.add(saveMI);

        JMenuItem saveAsMI = new JMenuItem("Save as...");
        saveAsMI.addActionListener(_ -> DataController.saveDataAs());
        fileMenu.add(saveAsMI);

        fileMenu.addSeparator();

        JMenuItem exitMI = new JMenuItem("Exit");
        // TODO Confirm exit when data has not been saved
        exitMI.addActionListener(_ -> System.exit(0));
        fileMenu.add(exitMI);

        this.add(fileMenu);
    }

    private void initEditMenu() {

        JMenu editMenu = new JMenu("Edit");
        editMenu.setName("Edit Menu");
        editMenu.setMnemonic('E');

        MenuItem copyTagsMI = new MenuItem("Copy Tags", "Ctrl + Shift + C");
        copyTagsMI.addActionListener(_ -> DataController.copyTagsToClipboard());
        editMenu.add(copyTagsMI);

        editMenu.addSeparator();

        JMenuItem tagModeMI = new JMenuItem("Tagging Mode");
        editMenu.add(tagModeMI);

        JMenuItem editModeMI = new JMenuItem("Editing Mode ✔");
        editMenu.add(editModeMI);

        JMenuItem sortModeMI = new JMenuItem("Sorting Mode");
        editMenu.add(sortModeMI);

        // Had to be seperated from above so that tagModeMI's action listener could reference editModeMI
        tagModeMI.addActionListener(_ -> {
            UIController.setUIMode(UIMode.TAG);
            tagModeMI.setText("Tagging Mode ✔");
            editModeMI.setText("Editing Mode");
            sortModeMI.setText("Sorting Mode");
        });
        editModeMI.addActionListener(_ -> {
            UIController.setUIMode(UIMode.EDIT);
            tagModeMI.setText("Tagging Mode");
            editModeMI.setText("Editing Mode ✔");
            sortModeMI.setText("Sorting Mode");
        });
        sortModeMI.addActionListener(_ -> {
            UIController.setUIMode(UIMode.SORT);
            tagModeMI.setText("Tagging Mode");
            editModeMI.setText("Editing Mode");
            sortModeMI.setText("Sorting Mode ✔");
        });

        editMenu.addSeparator();

        JMenuItem addTagLayerMI = new JMenuItem("Add TagLayer");
        addTagLayerMI.addActionListener(_ -> UIController.addTagLayerUI());
        editMenu.add(addTagLayerMI);

        JMenuItem removeTagLayerMI = new JMenuItem("Remove TagLayer");
        removeTagLayerMI.addActionListener(_ -> UIController.removeLastTagLayerUI());
        editMenu.add(removeTagLayerMI);

        this.add(editMenu);

    }

    private void initHelpMenu() {

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setName("Help");

        JMenuItem helpMI = new JMenuItem("How to use");
        helpMenu.add(helpMI);

        JMenuItem aboutMI = new JMenuItem("About");
        aboutMI.addActionListener(_ -> {});
        helpMenu.add(aboutMI);

        helpMenu.addSeparator();

        JMenuItem bonusMI = new JMenuItem("Bonus");
        bonusMI.addActionListener(_ -> {

            ImageIcon originalIcon = new ImageIcon("src/data/6497510.png");
            Image scaledImage = originalIcon.getImage().getScaledInstance(600, 690, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JOptionPane.showMessageDialog(null, "", "BONUS MAHO", JOptionPane.INFORMATION_MESSAGE, scaledIcon);

        }); helpMenu.add(bonusMI);

        this.add(helpMenu);

    }


}
