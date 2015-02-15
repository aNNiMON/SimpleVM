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
        if (pointer >= stack.length) {
            final int[] arr = new int[(int)(stack.length * 1.5)];
            System.arraycopy(stack, 0, arr, 0, stack.length);
            stack = arr;
        }
        stack[pointer] = value;
    }

    public int pop() {
        if (pointer < 0) throw new EmptyStackException();
        return stack[pointer--];
    }
    
    public void pushBoolean(boolean value) {
        push(value ? 1 : 0);
    }
    
    public boolean popBoolean() {
        return (pop() != 0);
    }
    
    public void pushString(String value) {
        final int length = value.length();
        for (int i = length - 1; i >= 0; i--) {
            push(value.charAt(i));
        }
        push(length);
    }
    
    public String popString() {
        final int length = pop();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char)pop());
        }
        return sb.toString();
    }
}
