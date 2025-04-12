package ayaya.exceptions;

import ayaya.Log;

public class NullDataParentUIException extends NullPointerException{

    public NullDataParentUIException(Class<?> c) {
        super("Null UI Data Parent: " + c.getSimpleName());
        Log.severe("Null UI Data Parent: " + c.getSimpleName());
    }

}
