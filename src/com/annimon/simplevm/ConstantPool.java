package com.annimon.simplevm;

/**
 *
 * @author aNNiMON
 */
public class ConstantPool {
    
    private final Constant[] constants;

    public ConstantPool(int size) {
        constants = new Constant[size];
    }
    
    public ConstantPool(Constant[] constants) {
        this.constants = constants;
    }
    
    public int getSize() {
        return constants.length;
    }
    
    public Constant get(int addr) {
        return constants[addr];
    }
    
    public void set(int addr, Constant value) {
        constants[addr] = value;
    }
}
