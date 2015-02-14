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
        LDC, 0,
        LDC, 1,
        IADD,
        // (i1 + i2) * i3
        LDC, 2,
        IMUL,
        INVOKE_PRINT
    };
    
    public static void main(String[] args) {
        final Data constantPool = new Data(3);
        constantPool.set(0, 10);
        constantPool.set(1, 20);
        constantPool.set(2, -5);
        new VirtualMachine(sample, constantPool, new Data(0)).execute();
    }
}
