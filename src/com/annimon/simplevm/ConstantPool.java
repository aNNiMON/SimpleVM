package com.annimon.simplevm;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author aNNiMON
 */
public class ConstantPool implements Iterable<Constant> {
    
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

    @Override
    public Iterator<Constant> iterator() {
        return new Iterator<Constant>() {
            
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < constants.length;
            }

            @Override
            public Constant next() {
                if (hasNext()) return constants[index++];
                throw new NoSuchElementException();
            }
        };
    }
}
