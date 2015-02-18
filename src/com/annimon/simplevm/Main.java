package com.annimon.simplevm;

import com.annimon.simplevm.assembler.Assembler;
import com.annimon.simplevm.assembler.Disassembler;
import com.annimon.simplevm.gui.MainFrame;
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
                case "-d":
                    disassembler(args);
                    break;
                default:
                    execute(args[1]);
            }
        } else {
            // Execute assembler GUI.
            new MainFrame().setVisible(true);
        }
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
    
    public static void disassembler(String[] args) throws IOException {
        // args: -d sample.svm -nbl
        String file = "";
        String options = "";
        for (int i = 1; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                options = args[i];
            } else {
                file = args[i];
            }
        }
        
        if (file.isEmpty()) return;
        System.out.println(Disassembler.fromInputStream(new FileInputStream(file))
                 .showBytes(options.contains("b"))
                 .showConstantPool(options.contains("p"))
                 .showDebugInfo(options.contains("d"))
                 .showLineNumbers(options.contains("n"))
                 .showLdcRawAddress(options.contains("l"))
                 .process()
         );
    }
     
    public static void addMethod(String name, int numLocals) {
        System.out.println(String.format("Adding method %s with %d locals", name, numLocals));
    }
     
     private static void addNativeMethods() {
         program.addNativeMethod("print", new Print());
         program.addNativeMethod("concat", new Concat());
         program.addNativeMethod("reflectCall", new ReflectionInvocator());
     }
}
