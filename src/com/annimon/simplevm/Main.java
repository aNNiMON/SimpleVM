package com.annimon.simplevm;

import static com.annimon.simplevm.Instructions.*;

/**
 *
 * @author aNNiMON
 */
public class Main {
    
    // Main method
    private static final byte[] main = {
        // int v0 = average(10, 20)
        LDC, 0, // INT 10
        LDC, 1, // INT 20
        LDC, 5, // STRING "average" 
        INVOKE,
        ISTORE, 0,
        
        // print((String) average(-v0, -5))
        ILOAD, 0,
        INEG,
        LDC, 2, // INT -5
        LDC, 5, // STRING "average" 
        INVOKE,
        I2S,
        INVOKE_PRINT,
        
        // hello world
        LDC, 3,
        INVOKE_PRINT
    };
    
    // Calculates average of two numbers
    private static final byte[] average = {
        // i1 + i2
        IADD,
        // (i1 + i2) / 2
        LDC, 4, // INT 2
        IDIV
    };
    
    public static void main(String[] args) {
        Program program = new Program(6);
        program.setConstant(0, Constant.integer(10));
        program.setConstant(1, Constant.integer(20));
        program.setConstant(2, Constant.integer(-5));
        program.setConstant(3, Constant.string("Hello, world"));
        program.setConstant(4, Constant.integer(2));
        program.setConstant(5, Constant.string("average"));
        
        program.addMethod("main", main, 1);
        program.addMethod("average", average, 0);
        
        new VirtualMachine(program).execute();
    }
}
