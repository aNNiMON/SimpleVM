package com.annimon.simplevm;

import static com.annimon.simplevm.Instructions.*;
import com.annimon.simplevm.utils.Data;

/**
 *
 * @author aNNiMON
 */
public class Main {
    
    private static final byte[] sample = {
        // i1 + i2
        ILOAD, 0,
        ILOAD, 1,
        IADD,
        // (i1 + i2) * i3
        ILOAD, 2,
        IMUL,
    };
    
    public static void main(String[] args) {
        final Data memory = new Data(3);
        memory.set(0, 10);
        memory.set(1, 20);
        memory.set(2, -5);
        new VirtualMachine(sample, new Data(0), memory).execute();
    }
}
