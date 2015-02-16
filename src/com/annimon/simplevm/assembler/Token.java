package com.annimon.simplevm.assembler;

/**
 *
 * @author aNNiMON
 */
public class Token {
    
    private final String text;
    private final TokenType type;
    
    public Token(String text, TokenType type) {
        this.text = text;
        this.type = type;
    }
    
    public String getText() {
        return text;
    }
    
    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return text + " " + type.name();
    }
}
