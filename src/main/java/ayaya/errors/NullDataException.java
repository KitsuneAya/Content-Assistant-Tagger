package ayaya.errors;

import ayaya.Log;

public class NullDataException extends NullPointerException {

    public NullDataException(Class<?> c) {
        super("Null Data: " + c.getSimpleName());
        Log.severe("Null Data: " + c.getSimpleName());
    }

}
