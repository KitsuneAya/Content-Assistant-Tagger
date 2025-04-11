package ayaya.data;

import ayaya.TagGroupType;
import ayaya.abstractions.AbstractData;

import java.util.ArrayList;


public class TagData extends AbstractData {


    //\\//\\//\\//\\//\\//\\
//|/  Data Fields          \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    private final ArrayList<TagGroupData> subData = new ArrayList<>();


    //\\//\\//\\//\\//\\//\\
//|/  Constructors         \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    private TagData() {}; // JSON constructor
    public TagData(String name) {
        this.name = name;
    }


    //\\//\\//\\//\\//\\//\\
//|/  Implemented Methods  \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    @Override public void setName(String name) {
        super.name = name.toLowerCase();
    }

    @Override public TagGroupData addNewSubData(TagGroupType type) {

        var subTagGroupData = new TagGroupData(type);
        this.subData.add(subTagGroupData);
        return subTagGroupData;

    }

    @Override public ArrayList<TagGroupData> getSubData() {
        return this.subData;
    }



    //\\//\\//\\//\\//\\//\\
//|/  Deprecated Methods   \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    @Override
    @Deprecated public TagData addNewSubData(String name) {
        throw new UnsupportedOperationException("Use addNewSubData(TagGroupType type) instead");
    }

}