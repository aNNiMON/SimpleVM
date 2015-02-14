package com.annimon.simplevm;

import static com.annimon.simplevm.Instructions.*;

/**
 *
 * @author aNNiMON
 */
public class Main {
    
    private static final byte[] sample = {
        // i1 + i2
        ILOAD,
        ILOAD,
        IADD,
        // (i1 + i2) * i3
        ILOAD,
        IMUL,
    };
    
    public static void main(String[] args) {
        new VirtualMachine(sample).execute();
    }
}
