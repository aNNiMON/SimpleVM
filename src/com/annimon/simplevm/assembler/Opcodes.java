package com.annimon.simplevm.assembler;

import static com.annimon.simplevm.Instructions.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aNNiMON
 */
public final class Opcodes {
    
    public static final String OPCODE_LDC = "ldc";
    public static final String OPCODE_ILOAD = "iload";
    public static final String OPCODE_ISTORE = "istore";
    public static final String OPCODE_GETFIELD = "getfield";
    public static final String OPCODE_PUTFIELD = "putfield";
    
    public static Map<String, Opcode> getOpcodes() {
        Map<String, Opcode> opcodes = new HashMap<>();
        opcodes.put(OPCODE_ILOAD, new Opcode(ILOAD, 1));
        opcodes.put(OPCODE_ISTORE, new Opcode(ISTORE, 1));
        opcodes.put(OPCODE_LDC, new Opcode(LDC, 1));
        opcodes.put("iconst_0", new Opcode(ICONST_0));
        opcodes.put("iconst_1", new Opcode(ICONST_1));
        opcodes.put("bconst", new Opcode(BCONST, 1));
        opcodes.put("sconst", new Opcode(SCONST, 2));

        opcodes.put(OPCODE_GETFIELD, new Opcode(GETFIELD, 1));
        opcodes.put(OPCODE_PUTFIELD, new Opcode(PUTFIELD, 1));

        opcodes.put("iadd", new Opcode(IADD));
        opcodes.put("isub", new Opcode(ISUB));
        opcodes.put("imul", new Opcode(IMUL));
        opcodes.put("idiv", new Opcode(IDIV));
        opcodes.put("ineg", new Opcode(INEG));
        opcodes.put("iinc", new Opcode(IINC));
        opcodes.put("idec", new Opcode(IDEC));

        opcodes.put("ifeq", new Opcode(IFEQ, 1));
        opcodes.put("ifne", new Opcode(IFNE, 1));
        opcodes.put("ifge", new Opcode(IFGE, 1));
        opcodes.put("ifgt", new Opcode(IFGT, 1));
        opcodes.put("ifle", new Opcode(IFLE, 1));
        opcodes.put("iflt", new Opcode(IFLT, 1));

        opcodes.put("goto", new Opcode(GOTO, 1));

        opcodes.put("i2s", new Opcode(I2S));

        opcodes.put("invoke", new Opcode(INVOKE));
        opcodes.put("invoke_native", new Opcode(INVOKE_NATIVE));

        opcodes.put("nop", new Opcode(NOP));
        
        return opcodes;
    }
    
    public static Map<Byte, Opcode> getOpcodesByteKey() {
        final Map<Byte, Opcode> result = new HashMap<>();
        final Map<String, Opcode> opcodes = getOpcodes();
        for (Map.Entry<String, Opcode> entry : opcodes.entrySet()) {
            final Opcode opcode = entry.getValue();
            final String name = entry.getKey();
            final byte key = opcode.getOpcode();
            result.put(key, new Opcode(key, opcode.getNumOfArgs(), name));
        }
        return result;
    }
}
