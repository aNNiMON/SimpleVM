package com.annimon.simplevm;

import static com.annimon.simplevm.Instructions.*;

/**
 *
 * @author aNNiMON
 */
public class Main {
    
    // Main method
    private static final byte[] main = {
        // print((String) average(sum(0,20), sum(-40,-20)))
        
        // sum(0, 20)
        LDC, 0, // INT 0
        LDC, 1, // INT 20
        LDC, 2, // STRING "sum"
        INVOKE,
        
        // sum(-40, -20)
        LDC, 3, // INT -40
        LDC, 1, // INT 20
        INEG,
        LDC, 2, // STRING "sum"
        INVOKE,
        
        // average(i1, i2)
        LDC, 5, // STRING "average" 
        INVOKE,
        I2S,
        INVOKE_PRINT,
    };
    
    // Calculates sum of range from two arguments
    private static final byte[] sum = {
        // sum = 0
        // for(int i = from; i <= to; i++)
        //     sum += i
        
        ISTORE, 0, // arg2 - to
        ISTORE, 1, // i - from
        
        // sum = 0
        LDC, 0, // INT 0
        ISTORE, 2,
        
        // #8
        // if (i > to) return sum
        ILOAD, 1,
        ILOAD, 0,
        IFGT, 28,
        
        // sum += i
        ILOAD, 2,
        ILOAD, 1,
        IADD,
        ISTORE, 2,
        
        // i++
        ILOAD, 1,
        IINC,
        ISTORE, 1,
        
        GOTO, 8,
        
        // #28
        // return sum
        ILOAD, 2,
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
        program.setConstant(0, Constant.integer(0));
        program.setConstant(1, Constant.integer(20));
        program.setConstant(2, Constant.string("sum"));
        program.setConstant(3, Constant.integer(-40));
        program.setConstant(4, Constant.integer(2));
        program.setConstant(5, Constant.string("average"));
        
        program.addMethod("main", main, 0);
        program.addMethod("sum", sum, 3);
        program.addMethod("average", average, 0);
        
        new VirtualMachine(program).execute();
    }
}
