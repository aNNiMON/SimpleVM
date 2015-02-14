package com.annimon.simplevm.utils;

/**
 * Array wrapper for readability
 * @author aNNiMON
 */
public class Data {
    
    private final int[] data;

    public Data() {
        this(100);
    }
    
    public Data(int size) {
        data = new int[size];
    }
    
    public int get(int addr) {
        return data[addr];
    }
    
    public void set(int addr, int value) {
        data[addr] = value;
    }
}
