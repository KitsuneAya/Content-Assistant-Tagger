package ayaya.options;

import ayaya.TagGroupType;
import ayaya.UIMode;

public class Preferences {

    public static UIMode startupMode = UIMode.EDIT;

    public static int uiSize = 24;
    public static int uiGapSize = uiSize / 6;

    public static int initNumOfTagLayers = 4;
    public static boolean keepEmptyExtraTagLayers = false;

    public static String defaultTagGroupName = "Tag Group";
    public static TagGroupType defaultNewGrpType = TagGroupType.ANY;

    public static String defaultTagName = "";

    public static String saveFileExtension = ".save";

}
