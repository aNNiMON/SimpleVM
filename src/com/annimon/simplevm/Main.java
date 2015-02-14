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
        I2S,
        INVOKE_PRINT,
        
        // hello world
        LDC, 3,
        INVOKE_PRINT
    };
    
    public static void main(String[] args) {
        final ConstantPool constantPool = new ConstantPool(4);
        constantPool.set(0, new Constant.ConstantInt(10));
        constantPool.set(1, new Constant.ConstantInt(20));
        constantPool.set(2, new Constant.ConstantInt(-5));
        constantPool.set(3, new Constant.ConstantString("Hello, world"));
        new VirtualMachine(sample, constantPool, new Data(0)).execute();
    }
}
