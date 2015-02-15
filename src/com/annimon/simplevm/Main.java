package com.annimon.simplevm;

import static com.annimon.simplevm.Instructions.*;
import com.annimon.simplevm.lib.Print;
import com.annimon.simplevm.lib.Concat;
import com.annimon.simplevm.lib.ReflectionInvocator;
import java.io.IOException;

/**
 *
 * @author aNNiMON
 */
public class Main {
    
    // Main method
    private static final byte[] main = {
        // Dynamically add method calc, then invoke it
        LDC, 12, // STRING "calc" (arg1: name)
        LDC, 0,  // INT 0 (arg2: numLocals)
        LDC, 10, // class
        LDC, 11, // method with signature
        LDC, 9, // "reflectCall" 
        INVOKE_NATIVE,
        
        LDC, 12,
        INVOKE
    };
    
    // Calc average of sum ranges
    private static final byte[] calc = {
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
        
        // concat strings
        LDC, 8, // STRING "Average of..."
        LDC, 7, // STRING "concat"
        INVOKE_NATIVE,
            
        LDC, 6, // STRING "print"
        INVOKE_NATIVE
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
    
    
    private static Program program;
    
    public static void addMethod(String name, int numLocals) {
        System.out.println(String.format("Adding method %s with %d locals", name, numLocals));
        program.addMethod(name, calc, numLocals);
    }
    
    public static void main(String[] args) throws IOException {
//        program = Program.read(new FileInputStream("sample.svm"));
        
        program = new Program(13);
        program.setConstant(0, Constant.integer(0));
        program.setConstant(1, Constant.integer(20));
        program.setConstant(2, Constant.string("sum"));
        program.setConstant(3, Constant.integer(-40));
        program.setConstant(4, Constant.integer(2));
        program.setConstant(5, Constant.string("average"));
        program.setConstant(6, Constant.string("print"));
        program.setConstant(7, Constant.string("concat"));
        program.setConstant(8, Constant.string("Average of sum(0..20) and sum(-40..-20) is "));
        program.setConstant(9, Constant.string("reflectCall"));
        program.setConstant(10, Constant.string("com.annimon.simplevm.Main"));
        program.setConstant(11, Constant.string("addMethod(SI)V"));
        program.setConstant(12, Constant.string("calc"));
        
        program.addMethod("main", main, 0);
        program.addMethod("sum", sum, 3);
        program.addMethod("average", average, 0);
        
        program.addNativeMethod("print", new Print());
        program.addNativeMethod("concat", new Concat());
        program.addNativeMethod("reflectCall", new ReflectionInvocator());
        
        new VirtualMachine(program).execute();
        
//        Program.write(program, new FileOutputStream("sample.svm"));
    }
}
