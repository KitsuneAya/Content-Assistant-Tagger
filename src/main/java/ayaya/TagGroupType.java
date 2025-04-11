package ayaya;

public enum TagGroupType {

    ANY,                // >=0 tags can be selected
    AT_LEAST_ONE,       // >0 tags must be selected
    ONE_AND_ONLY_ONE,   // ==1 tag must be selected
    OPTIONAL_ONE,       // <=1 tag can be selected
    NON_TAG,            // For tags that only function as containers for sub-TagGroups, >=0 can be selected

}
