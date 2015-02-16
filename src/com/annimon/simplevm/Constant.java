package com.annimon.simplevm;

/**
 *
 * @author aNNiMON
 */
public class Constant {
    
    public static final int
            INT = 1,
            STRING = 2;
    
    public static Constant integer(int value) {
        return new ConstantInt(value);
    }
    
    public static Constant string(String value) {
        return new ConstantString(value);
    }
    
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

        @Override
        public int hashCode() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ConstantInt other = (ConstantInt) obj;
            return this.value == other.value;
        }
        
        @Override
        public String toString() {
            return "Int: " + value;
        }
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

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ConstantString other = (ConstantString) obj;
            return (value == other.value) ||
                   (value != null && value.equals(other.value));
        }
        
        
        @Override
        public String toString() {
            return "String: " + value;
        }
    }
}
