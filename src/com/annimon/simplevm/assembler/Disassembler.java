package com.annimon.simplevm.assembler;

import com.annimon.simplevm.Constant;
import com.annimon.simplevm.Instructions;
import com.annimon.simplevm.Method;
import com.annimon.simplevm.Program;
import com.annimon.simplevm.utils.StringAppender;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Disassembles svm file or program.
 * @author aNNiMON
 */
public final class Disassembler {
    
    public static Disassembler fromInputStream(InputStream is) throws IOException {
        return Disassembler.fromProgram(Program.read(is));
    }
    
    public static Disassembler fromProgram(Program program) {
        return new Disassembler(program);
    }
    
    private final Program program;
    private final Map<Byte, Opcode> opcodes;
    private final Options options;
    
    public Disassembler(Program program) {
        this.program = program;
        opcodes = Opcodes.getOpcodesByteKey();
        options = new Options();
    }
    
    public Disassembler showLineNumbers(boolean state) {
        options.showLineNumbers = state;
        return this;
    }
    
    public Disassembler showBytes(boolean state) {
        options.showBytes = state;
        return this;
    }

    public Disassembler showLdcRawAddress(boolean state) {
        options.showLdcRawAddress = state;
        return this;
    }

    public Disassembler showDebugInfo(boolean state) {
        options.showDebugInfo = state;
        return this;
    }

    public Disassembler showConstantPool(boolean state) {
        options.showConstantPool = state;
        return this;
    }
    
    public String process() {
        final StringAppender result = new StringAppender();
        if (options.showDebugInfo) {
            result.add("; fields ").add(program.getNumFields()).newline();
            if (options.showConstantPool) {
                result.line("; constants");
                int constantCounter = 0;
                for (Constant constant : program.constants()) {
                    result.add(";  ").add(constantCounter++).add(": ").add(constant).newline();
                }
            }
            result.newline();
        }
        
        for (Method method : program.methods()) {
            result.line(processMethod(method));
        }
        return result.toString();
    }
    
    private String processMethod(Method method) {
        final StringAppender code = new StringAppender();
        code.add(".method ").word(method.getName()).newline();
        final byte[] bytecode = method.getInstructionsBytecode();
        if (options.showDebugInfo) {
            code.add("; length ").add(bytecode.length).newline();
            code.add("; locals ").add(method.getNumLocals()).newline();
        }
        for (int i = 0; i < bytecode.length; i++) {
            if (!opcodes.containsKey(bytecode[i])) continue;
            
            final Opcode opcode = opcodes.get(bytecode[i]);
            
            if (options.showLineNumbers || options.showBytes) {
                if (options.showLineNumbers) {
                    code.add("; L").add(i).add(':');
                } else code.add(";");
                
                if (options.showBytes) {
                    for (int j = i; j <= i + opcode.getNumOfArgs(); j++) {
                        code.add(' ').add(String.format("%02X", bytecode[j]));
                    }
                }
                code.newline();
            }
            
            code.add(opcode.getName());
            if (opcode.getOpcode() == Instructions.LDC) {
                Constant constant = program.getConstant(bytecode[++i]);
                code.add(' ');
                if (constant.getType() == Constant.STRING) {
                    code.word(constant.valueAsString());
                } else {
                    code.add(constant.valueAsString());
                }
                if (options.showLdcRawAddress) {
                    code.add(" ; #").add(bytecode[i]);
                }
            } else {
                for (int j = opcode.getNumOfArgs(); j > 0; j--) {
                    code.add(' ').add(Integer.toString(bytecode[++i]));
                }
            }
            code.newline();
        }
        return code.toString();
    }
    
    private static class Options {
        boolean showLineNumbers;
        boolean showLdcRawAddress;
        boolean showDebugInfo;
        boolean showConstantPool;
        boolean showBytes;
    }
}
