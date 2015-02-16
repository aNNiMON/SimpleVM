package com.annimon.simplevm.assembler;

/**
 *
 * @author aNNiMON
 */
public final class Opcode {

    private final byte opcode;
    private final int numOfArgs;

    public Opcode(byte opcode) {
        this(opcode, 0);
    }

    public Opcode(byte opcode, int numOfArgs) {
        this.opcode = opcode;
        this.numOfArgs = numOfArgs;
    }

    public byte getOpcode() {
        return opcode;
    }

    public int getNumOfArgs() {
        return numOfArgs;
    }
}
