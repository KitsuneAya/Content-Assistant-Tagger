package ayaya.util;

import java.awt.*;

public class UtilityFunctions { private UtilityFunctions() {}

    public static Insets createPadding(int left, int right, int top, int bottom) {
        return new Insets(top, left, bottom, right);
    }

    public static Insets createHorizontalPadding(int horizontal) {
        return new Insets(0, horizontal, 0, horizontal);
    }

    public static Insets createHorizontalPadding(int left, int right) {
        return new Insets(0, left, 0, right);
    }

    public static Insets createVerticalPadding(int vertical) {
        return new Insets(vertical, 0, vertical, 0);
    }

    public static Insets createVerticalPadding(int top, int bottom) {
        return new Insets(top, 0, bottom, 0);
    }

    public static Insets createSquarePadding(int allSides) {
        return new Insets(allSides, allSides, allSides, allSides);
    }

    public static Insets createRectangularPadding(int horizontal, int vertical) {
        return new Insets(vertical, horizontal, vertical, horizontal);
    }

}
