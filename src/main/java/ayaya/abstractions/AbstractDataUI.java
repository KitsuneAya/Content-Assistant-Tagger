package ayaya.abstractions;

import java.util.ArrayList;

@SuppressWarnings("all")
public abstract class AbstractDataUI extends AbstractUI {



    //\\//\\//\\//\\//\\//\\
////  Class Fields         \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    protected AbstractData data;                    // The data object displayed by this UI
    protected AbstractUI parentUI;           // Whatever object is displaying this UI
    protected AbstractDataUI parentDataUI;   // Whatever object is displaying the parent data of this UI


    //\\//\\//\\//\\//\\//\\
////  Constructors         \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    public AbstractDataUI(AbstractDataUI parentDataUI, AbstractUI parentTag, AbstractData data) {
        this.parentDataUI = parentDataUI;
        this.parentUI = parentTag;
        this.data = data;
    }


    //\\//\\//\\//\\//\\//\\                                             Accessors and mutators that only effect this UI
////  UI Methods           \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    public abstract void showDeleteButton(boolean b);


    //\\//\\//\\//\\//\\//\\                                    Access and mutators that can interact with the Parent UI
////  Parent UI Methods    \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    public abstract <T extends AbstractUI> T getParentUI();


    //\\//\\//\\//\\//\\//\\                                       Accessors and mutators that can interact with sub-UIs
////  Sub-UI Methods       \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    /**
     * Creates new sub-data in the data this UI is displaying, and then displays that new sub-data in its own UI.
     */
    public abstract void addNewSubUI();

    /**
     * Returns the UIs which have their data contained within this UI's.
     */
    public abstract <T extends AbstractDataUI> ArrayList<T> getSubUIs();



    //\\//\\//\\//\\//\\//\\                                                     Methods for deleting UIs and their data
////  Deletion Methods     \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    /**
     * Deletes this UI's data and removes it and its subUIs from display.
     */
    public void delete() {
        this.deleteUI();
        this.parentDataUI.deleteData(this.data);
    }

    /**
     * Called from the subUI to delete its data from this UI.
     */
    private void deleteData(AbstractData subTagData) {
        this.data.deleteSubDatum(subTagData);
    }

    /**
     * Called by the parent UI to remove this and its own sub-UIs from display.
     */
    private void deleteUI() {
        var subUIs = new ArrayList<AbstractDataUI>(this.getSubUIs());
        for (var subTagUI : subUIs) subTagUI.deleteUI();
        this.parentUI.removeSubUI(this);
    }

}