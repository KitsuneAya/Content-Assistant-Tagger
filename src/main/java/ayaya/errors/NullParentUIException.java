package ayaya.errors;

import ayaya.Log;

public class NullParentUIException extends NullPointerException {

    public NullParentUIException(Class<?> c) {
        super("Null UI Parent: " + c.getSimpleName());
        Log.severe("Null UI Parent: " + c.getSimpleName());
    }

}
