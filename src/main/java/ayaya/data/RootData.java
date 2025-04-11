package ayaya.data;

import ayaya.TagGroupType;
import ayaya.abstractions.AbstractData;

import java.util.ArrayList;

/// This is what is loaded and saved for Tagging Schemes the user creates.
public class RootData extends AbstractData {


    //\\//\\//\\//\\//\\//\\
//|/  Data Fields          \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    private final ArrayList<TagGroupData> subData = new ArrayList<>();


    //\\//\\//\\//\\//\\//\\
//|/  Constructors         \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    private RootData() {} // JSON Constructor

    public RootData(String packageName) {
        super.name = packageName;
    }



    //\\//\\//\\//\\//\\//\\
//|/  Implemented Methods  \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    @Override public TagGroupData addNewSubData(TagGroupType type) {

        var tagGroupData = new TagGroupData(type);
        this.subData.add(tagGroupData);
        return tagGroupData;
    }

    @Override public ArrayList<TagGroupData> getSubData() {
        return this.subData;
    }



    //\\//\\//\\//\\//\\//\\
//|/  Deprecated Methods   \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    @Deprecated
    @Override public TagData addNewSubData(String name) {
        throw new UnsupportedOperationException("Use addNewSubData(TagGroupType)");
    }

}