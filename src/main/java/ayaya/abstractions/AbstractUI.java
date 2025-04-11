package ayaya.abstractions;

import javax.swing.*;
import java.util.ArrayList;

@SuppressWarnings("all")
public abstract class AbstractUI extends JPanel {


    //\\//\\//\\//\\//\\//\\                                             Accessors and mutators that only effect this UI
////  UI Methods           \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    /// Ensures this UI displayed on screen is up to date with data in memory and mode selection.
    public abstract void refreshUI();

    /**
     * Make visible the button on this UI that will instantiate and display new sub-data.
     * @param b True for visible. False for hide.
     */
    public abstract void showSubUIAddButton(boolean b);



    //\\//\\//\\//\\//\\//\\                                       Accessors and mutators that can interact with sub-UIs
////  Sub-UI Methods       \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    /**
     * Allows methods outside of the class to iterate over sub-UI components.
     * @return The UI components displaying subdata of the data displayed by this.
     * @param <T> A {@link ayaya.ui.TagGroupUI} or a {@link ayaya.ui.TagUI}.
     */
    public abstract <T extends AbstractDataUI> ArrayList<T> getSubUIs();

    /// @return The number of sub-UI components this has access to regardless of their visiblity.
    public int getSubUISize() {
        return getSubUIs().size();
    }

    /// Ensures this UI and all of its sub-UIs displayed on screen are up to date with data in memory and mode selection.
    public void refreshSubUIs() {

        this.refreshUI();
        this.getSubUIs().forEach(AbstractDataUI::refreshSubUIs);

    }

    /// Removes the display of, and references to, a sub-UI component.
    public abstract void removeSubUI(AbstractDataUI subUI);


}
