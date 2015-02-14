package com.annimon.simplevm;

/**
 * Method structure
 * @author aNNiMON
 */
public class Method {
    
    private final String name;
    private final byte[] instructionsBytecode;
    private final int numLocals;

    public Method(String name, byte[] instructionsBytecode, int numLocals) {
        this.name = name;
        this.instructionsBytecode = instructionsBytecode;
        this.numLocals = numLocals;
    }

    public String getName() {
        return name;
    }

    public byte[] getInstructionsBytecode() {
        return instructionsBytecode;
    }

    public int getNumLocals() {
        return numLocals;
    }
}
