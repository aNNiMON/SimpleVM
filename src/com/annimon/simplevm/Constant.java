package com.annimon.simplevm;

/**
 *
 * @author aNNiMON
 */
public class Constant {
    
    public static int
            INT = 1,
            STRING = 2;
    
    private final int type;
    
    public Constant(int type) {
        this.type = type;
    }
    
    public int getType() {
        return type;
    }
    
    public static class ConstantInt extends Constant {
        
        public ConstantInt() {
            super(INT);
        }
        
        public ConstantInt(int value) {
            super(INT);
            this.value = value;
        }
        
        public int value;
    }
    
    public static class ConstantString extends Constant {
        
        public ConstantString() {
            super(STRING);
        }
        
        public ConstantString(String value) {
            super(STRING);
            this.value = value;
        }
        
        public String value;
    }
}
