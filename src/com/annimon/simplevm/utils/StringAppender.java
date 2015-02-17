package com.annimon.simplevm.utils;

/**
 *
 * @author aNNiMON
 */
public final class StringAppender implements CharSequence {
    
    private static final String NEW_LINE = System.lineSeparator();
    
    private final StringBuilder stringBuilder;

    public StringAppender() {
        stringBuilder = new StringBuilder();
    }
    
    public StringAppender newline() {
        add(NEW_LINE);
        return this;
    }
    
    public StringAppender line(String str) {
        return add(str).newline();
    }
    
    public StringAppender lines(String... lines) {
        for (String line : lines) {
            add(line).newline();
        }
        return this;
    }
    
    public StringAppender word(String str) {
        return add('"').add(str).add('"');
    }
    
    public StringAppender repeat(String str, int times) {
        for (int i = 0; i < times; i++) {
            add(str);
        }
        return this;
    }
    
    public StringAppender add(Object obj) {
        stringBuilder.append(obj);
        return this;
    }

    public StringAppender add(String str) {
        stringBuilder.append(str);
        return this;
    }

    public StringAppender add(char c) {
        stringBuilder.append(c);
        return this;
    }

    @Override
    public int length() {
        return stringBuilder.length();
    }

    @Override
    public char charAt(int index) {
        return stringBuilder.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return stringBuilder.subSequence(start, end);
    }
    
    @Override
    public String toString() {
        return stringBuilder.toString();
    }
}
