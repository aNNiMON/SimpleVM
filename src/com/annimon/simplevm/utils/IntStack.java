package com.annimon.simplevm.utils;

import java.util.EmptyStackException;

/**
 * Quick int stack
 * @author aNNiMON
 */
public class IntStack {
    
    private int[] stack;
    private int pointer;
    
    public IntStack() {
        this(100);
    }
    
    public IntStack(int size) {
        stack = new int[size];
        pointer = -1;
    }

    public void push(int value) {
        pointer++;
        if (pointer >= stack.length) throw new RuntimeException("Stack overflow");
        stack[pointer] = value;
    }

    public int pop() {
        if (pointer < 0) throw new EmptyStackException();
        return stack[pointer--];
    }
}
