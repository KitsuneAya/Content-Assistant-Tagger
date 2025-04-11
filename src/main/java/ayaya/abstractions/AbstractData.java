package ayaya.abstractions;

import ayaya.TagGroupType;
import ayaya.data.TagData;
import ayaya.data.TagGroupData;

import java.util.ArrayList;

public abstract class AbstractData {


    //\\//\\//\\//\\//\\//\\
////  Data Fields          \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    protected String name;


    //\\//\\//\\//\\//\\//\\
////  Name Methods         \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }


    //\\//\\//\\//\\//\\//\\
////  Sub-data Methods     \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    public abstract TagGroupData addNewSubData(TagGroupType type);
    public abstract TagData addNewSubData(String name);

    public abstract <T extends AbstractData> ArrayList<T> getSubData();

    public void deleteSubDatum(AbstractData subData) {
        this.getSubData().remove(subData);
    }

}
