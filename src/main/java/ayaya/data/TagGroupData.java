package ayaya.data;

import ayaya.TagGroupType;
import ayaya.abstractions.AbstractData;

import java.util.ArrayList;


public class TagGroupData extends AbstractData {


    //\\//\\//\\//\\//\\//\\
//|/  Data Fields          \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    private TagGroupType type;
    private final ArrayList<TagData> subData = new ArrayList<>();


    //\\//\\//\\//\\//\\//\\
//|/  Constructors         \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    private TagGroupData() {} // JSON constructor
    public TagGroupData(TagGroupType type) { this.type = type; }


    //\\//\\//\\//\\//\\//\\
//|/  Implemented Methods  \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\

    @Override public TagData addNewSubData(String name) {

        TagData newSubTagData = new TagData(name);
        this.subData.add(newSubTagData);
        return newSubTagData;
    }
    @Override public ArrayList<TagData> getSubData() {

        return this.subData;
    }


    //\\//\\//\\//\\//\\//\\
//|/  Instance Methods     \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    public void setType(TagGroupType type) {

        this.type = type;
    }
    public TagGroupType getType() {

        return type;
    }

    public void moveTagData(int currentIndex, int newIndex) {

        int dataIndexSize = this.subData.size();

        // Input Checks
        if (currentIndex == newIndex) return;
        if (currentIndex < 0 || currentIndex >= dataIndexSize) throw new IndexOutOfBoundsException();

        // Index Normalization
        if (newIndex >= 0) newIndex = Math.min(newIndex, dataIndexSize - 1);
        else newIndex = (dataIndexSize + (newIndex % dataIndexSize)) % dataIndexSize; // allows negative indexes to work as if this was python

        var dataToMove = this.subData.remove(currentIndex);
        this.subData.add(newIndex, dataToMove);
    }

    public void moveTagData(TagData tagData, int newIndex) {

        int currentIndex = this.subData.indexOf(tagData);
        this.moveTagData(currentIndex, newIndex);
    }



    //\\//\\//\\//\\//\\//\\
//|/  Deprecated Methods   \\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\\//\


    @Override
    @Deprecated public TagGroupData addNewSubData(TagGroupType type) {
        throw new UnsupportedOperationException("Use addNewSubData(String name) instead");
    }
}
