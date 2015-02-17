package com.annimon.simplevm.assembler;

/**
 *
 * @author aNNiMON
 */
public final class Opcode {

    private final byte opcode;
    private final int numOfArgs;
    private final String name;

    public Opcode(byte opcode) {
        this(opcode, 0);
    }
    
    public Opcode(byte opcode, int numOfArgs) {
        this(opcode, numOfArgs, "");
    }

    public Opcode(byte opcode, String name) {
        this(opcode, 0, name);
    }

    public Opcode(byte opcode, int numOfArgs, String name) {
        this.opcode = opcode;
        this.numOfArgs = numOfArgs;
        this.name = name;
    }
    
    public byte getOpcode() {
        return opcode;
    }

    public int getNumOfArgs() {
        return numOfArgs;
    }

    public String getName() {
        return name;
    }
}
