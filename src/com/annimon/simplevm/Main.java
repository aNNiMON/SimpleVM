package com.annimon.simplevm;

import static com.annimon.simplevm.Instructions.*;
import com.annimon.simplevm.assembler.Assembler;
import com.annimon.simplevm.lib.Print;
import com.annimon.simplevm.lib.Concat;
import com.annimon.simplevm.lib.ReflectionInvocator;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author aNNiMON
 */
public class Main {
    
    // Main method
    private static final byte[] main = {
        // Dynamically add method calc, then invoke it
        LDC, 9, // STRING "calc" (arg1: name)
        ICONST_0, // INT 0 (arg2: numLocals)
        LDC, 7, // class
        LDC, 8, // method with signature
        LDC, 6, // "reflectCall" 
        INVOKE_NATIVE,
        
        LDC, 9,
        INVOKE
    };
    
    // Calc average of sum ranges
    private static final byte[] calc = {
        // print((String) average(sum(0,20), sum(-40,-20)))
        
        // sum(0, 20)
        ICONST_0, // INT 0
        BCONST, 20, // 20
        LDC, 0, // STRING "sum"
        INVOKE,
        
        // sum(-40, -20)
        LDC, 1, // INT -40
        BCONST, 20, // 20
        INEG,
        LDC, 0, // STRING "sum"
        INVOKE,
        
        // average(i1, i2)
        LDC, 2, // STRING "average" 
        INVOKE,
        I2S,
        
        // concat strings
        LDC, 5, // STRING "Average of..."
        LDC, 4, // STRING "concat"
        INVOKE_NATIVE,
            
        LDC, 3, // STRING "print"
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
        ICONST_0, // INT 0
        ISTORE, 2,
        
        // #7
        // if (i > to) return sum
        ILOAD, 1,
        ILOAD, 0,
        IFGT, 27,
        
        // sum += i
        ILOAD, 2,
        ILOAD, 1,
        IADD,
        ISTORE, 2,
        
        // i++
        ILOAD, 1,
        IINC,
        ISTORE, 1,
        
        GOTO, 7,
        
        // #27
        // return sum
        ILOAD, 2,
    };
    
    // Calculates average of two numbers
    private static final byte[] average = {
        // i1 + i2
        IADD,
        // (i1 + i2) / 2
        BCONST, 2,
        IDIV
    };
    
    
    private static Program program;
    
    public static void main(String[] args) throws IOException {
        if (args.length == 1) {
            execute(args[0]);
        } else if (args.length > 1) {
            switch (args[0]) {
                case "-a":
                case "-asm":
                    assembler(args[1]);
                    break;
                default:
                    execute(args[1]);
            }
        } else example();
    }
    
    public static void execute(String file) throws IOException {
        program = Program.read(new FileInputStream(file));
        addNativeMethods();
        program.execute();
    }
    
    public static void assembler(String file) throws IOException {
        program = Assembler.fromInputStream(new FileInputStream(file));
        addNativeMethods();
        program.execute();
    }
        
     public static void example() throws IOException {
        program = new Program(10);
        program.setConstant(0, Constant.string("sum"));
        program.setConstant(1, Constant.integer(-40));
        program.setConstant(2, Constant.string("average"));
        program.setConstant(3, Constant.string("print"));
        program.setConstant(4, Constant.string("concat"));
        program.setConstant(5, Constant.string("Average of sum(0..20) and sum(-40..-20) is "));
        program.setConstant(6, Constant.string("reflectCall"));
        program.setConstant(7, Constant.string("com.annimon.simplevm.Main"));
        program.setConstant(8, Constant.string("addMethod(SI)V"));
        program.setConstant(9, Constant.string("calc"));
        
        program.addMethod("main", main, 0);
        program.addMethod("sum", sum, 3);
        program.addMethod("average", average, 0);
        addNativeMethods();
        
        program.execute();
    }
     
    public static void addMethod(String name, int numLocals) {
        System.out.println(String.format("Adding method %s with %d locals", name, numLocals));
        if (program.getMethod(name) == null)
            program.addMethod(name, calc, numLocals);
        else System.out.println("Method already exists");
    }
     
     private static void addNativeMethods() {
         program.addNativeMethod("print", new Print());
         program.addNativeMethod("concat", new Concat());
         program.addNativeMethod("reflectCall", new ReflectionInvocator());
     }
}
