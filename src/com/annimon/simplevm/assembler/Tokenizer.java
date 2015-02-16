package com.annimon.simplevm.assembler;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author aNNiMON
 */
public class Tokenizer {
    
    public static List<Token> tokenize(String input) {
        return new Tokenizer().process(input).getTokens();
    }
    
    private final List<Token> tokens;
    private final StringBuilder buffer;
    
    private TokenizeState state;
    private int pos;
    
    private enum TokenizeState {
        DEFAULT, NUMBER, WORD, TEXT, KEYWORD, COMMENT
    }
    
    public Tokenizer() {
        tokens = new LinkedList<>();
        buffer = new StringBuilder();
        state = TokenizeState.DEFAULT;
    }
    
    public List<Token> getTokens() {
        return tokens;
    }
    
    public Tokenizer process(String input) {
        for (pos = 0; pos < input.length(); pos++) {
            tokenize(input.charAt(pos));
        }
        tokenize('\0');// EOF
        return this;
    }

    private void tokenize(final char ch) {
        switch (state) {
            case DEFAULT: tokenizeDefault(ch); break;
            case WORD: tokenizeWord(ch); break;
            case NUMBER: tokenizeNumber(ch); break;
            case TEXT: tokenizeText(ch); break;
            case KEYWORD: tokenizeKeyword(ch); break;
            case COMMENT: tokenizeComment(ch); break;
        }
    }

    private void tokenizeDefault(final char ch) {
        if (Character.isLetter(ch)) {
            // Word (invoke, ldc, iadd etc)
            buffer.append(ch);
            state = TokenizeState.WORD;
        } else if (Character.isDigit(ch) || (ch == '-')) {
            // Number
            buffer.append(ch);
            state = TokenizeState.NUMBER;
        } else if (ch == '"') {
            // String "text"
            state = TokenizeState.TEXT;
        } else if (ch == '.') {
            // Keyword (.method)
            buffer.append(ch);
            state = TokenizeState.KEYWORD;
        } else if (ch == ';') {
            state = TokenizeState.COMMENT;
        }
    }
    
    private void tokenizeWord(final char ch) {
        if (Character.isLetterOrDigit(ch) || (ch == '_')) {
            // ldc, invoke_native, iconst_0
            buffer.append(ch);
        } else {
            addToken(TokenType.WORD);
        }
    }
    
    private void tokenizeNumber(final char ch) {
        if (Character.isDigit(ch)) {
            buffer.append(ch);
        } else {
            addToken(TokenType.NUMBER);
        }
    }

    private void tokenizeText(final char ch) {
        // Allow to add multiline text
        if (ch == '"') {
            final int len = buffer.length();
            // If previous char isn't escape symbol
            if ( (len > 0) && (buffer.charAt(len - 1) != '\\') ) {
                addToken(TokenType.TEXT, false);
                return;
            }
        }
        buffer.append(ch);
    }
    
    private void tokenizeKeyword(final char ch) {
        if (Character.isLetter(ch)) {
            // dot already in buffer, proceed only letters
            buffer.append(ch);
        } else {
            addToken(TokenType.KEYWORD);
        }
    }
    
    private void tokenizeComment(final char ch) {
        if (ch == '\n' || ch == '\r') {
            state = TokenizeState.DEFAULT;
        }
    }
    
    private void addToken(TokenType type) {
        addToken(type, true);
    }
    
    private void addToken(TokenType type, boolean reprocessLastChar) {
        tokens.add(new Token(buffer.toString(), type));
        buffer.setLength(0);
        if (reprocessLastChar) pos--;
        state = TokenizeState.DEFAULT;
    }
}
