package com.annimon.simplevm.utils;

/**
 * Handle exceptions.
 * @author aNNiMON
 */
public final class ExceptionHandler {
    
    private static final boolean DEBUG_MODE = false;
    
    public static void handle(Throwable throwable) {
        if (DEBUG_MODE) {
            throwable.printStackTrace();
        }
    }
    
    public static void handle(Throwable throwable, String comment) {
        if (DEBUG_MODE) {
            System.out.println(comment);
            handle(throwable);
        }
    }
}
