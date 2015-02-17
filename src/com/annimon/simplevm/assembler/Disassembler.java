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
 *
 * @author aNNiMON
 */
public final class Disassembler {
    
    public static String fromInputStream(InputStream is) throws IOException {
        return Disassembler.fromProgram(Program.read(is));
    }
    
    public static String fromProgram(Program program) {
        return new Disassembler(program).process();
    }
    
    private final Program program;
    private final Map<Byte, Opcode> opcodes;

    public Disassembler(Program program) {
        this.program = program;
        opcodes = Opcodes.getOpcodesByteKey();
    }

    public String process() {
        final StringAppender result = new StringAppender();
        for (Method method : program.methods()) {
            result.line(processMethod(method));
        }
        return result.toString();
    }
    
    private String processMethod(Method method) {
        final StringAppender code = new StringAppender();
        code.add(".method ").word(method.getName()).newline();
        final byte[] bytecode = method.getInstructionsBytecode();
        for (int i = 0; i < bytecode.length; i++) {
            if (!opcodes.containsKey(bytecode[i])) continue;
            
            final Opcode opcode = opcodes.get(bytecode[i]);
            code.add(opcode.getName());
            if (opcode.getOpcode() == Instructions.LDC) {
                Constant constant = program.getConstant(bytecode[++i]);
                code.add(' ');
                if (constant.getType() == Constant.STRING) {
                    code.word(constant.valueAsString());
                } else {
                    code.add(constant.valueAsString());
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
    
}
