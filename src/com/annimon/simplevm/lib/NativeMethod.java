package com.annimon.simplevm.lib;

import com.annimon.simplevm.utils.IntStack;

/**
 * Native method
 * @author aNNiMON
 */
public abstract class NativeMethod {
    
    private IntStack operandStack;
    
    public final void setOperandStack(IntStack operandStack) {
        this.operandStack = operandStack;
    }
    
    public abstract void invoke();

    
    protected final void push(int value) {
        operandStack.push(value);
    }

    protected final int pop() {
        return operandStack.pop();
    }

    protected final void pushBoolean(boolean value) {
        operandStack.pushBoolean(value);
    }

    protected final boolean popBoolean() {
        return operandStack.popBoolean();
    }

    protected final void pushString(String value) {
        operandStack.pushString(value);
    }

    protected final String popString() {
        return operandStack.popString();
    }
}
