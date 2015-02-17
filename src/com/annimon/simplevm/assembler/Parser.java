package com.annimon.simplevm.assembler;

import com.annimon.simplevm.Constant;
import com.annimon.simplevm.ConstantPool;
import static com.annimon.simplevm.assembler.Opcodes.*;
import com.annimon.simplevm.Method;
import com.annimon.simplevm.Program;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author aNNiMON
 */
public class Parser {
    
    private static final String KEYWORD_METHOD = ".method";
    
    public static Program parse(List<Token> tokens) {
        return new Parser(tokens).process().getProgram();
    }

    private Program program;
    private final List<Token> tokens;
    private final List<Constant> constantPool;
    private String[] constantPoolValues;
    private final List<Method> methods;
    private final Map<String, Opcode> opcodes;
    private int maxFieldAddress;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        constantPool = new ArrayList<>();
        methods = new ArrayList<>();
        maxFieldAddress = -1;
        opcodes = Opcodes.getOpcodes();
    }
    
    public Program getProgram() {
        return program;
    }
    
    private void createProgram() {
        ConstantPool pool = new ConstantPool(constantPool.toArray(new Constant[0]));
        final int maxFields = 1 + maxFieldAddress;
        program = new Program(pool, maxFields);
        for (Method method : methods) {
            program.addMethod(method);
        }
    }
    
    public Parser process() {
        prepareConstantPool();
        // Needs to quickly replace constants by index in LDC instruction
        createSortedConstantPoolValues();
        processMethods();
        createProgram();
        return this;
    }

    private void prepareConstantPool() {
        final Set<Constant> pool = new HashSet<>();
        for (Iterator<Token> iterator = tokens.iterator(); iterator.hasNext();) {
            final Token token = iterator.next();
            if (!isInstruction(token, OPCODE_LDC)) continue;
            
            if (iterator.hasNext()) {
                final Token next = iterator.next();
                switch (next.getType()) {
                    case NUMBER:
                        pool.add(Constant.integer(Integer.parseInt(next.getText())));
                        break;
                    case TEXT:
                        pool.add(Constant.string(next.getText()));
                        break;
                }
            }
        }
        constantPool.addAll(pool);
    }
    
    private void createSortedConstantPoolValues() {
        Collections.sort(constantPool);
        constantPoolValues = new String[constantPool.size()];
        for (int i = 0; i < constantPoolValues.length; i++) {
            final Constant constant = constantPool.get(i);
            constantPoolValues[i] = constant.valueAsString();
        }
    }
    
    private void processMethods() {
        for (Iterator<Token> iterator = tokens.iterator(); iterator.hasNext();) {
            final Token token = iterator.next();
            if (isKeyword(token, KEYWORD_METHOD)) {
                processMethod(iterator);
            }
        }
    }

    private void processMethod(Iterator<Token> iterator) {
        // Read method name
        // TODO add checks
        final String methodName = iterator.next().getText();
        final ByteArrayOutputStream bytecode = new ByteArrayOutputStream();
        int maxLocalAddress = -1;
        while (iterator.hasNext()) {
            final Token token = iterator.next();
            if (isKeyword(token, KEYWORD_METHOD)) {
                // We meet another method
                processMethod(iterator);
                break;
            }
            
            if (token.getType() != TokenType.WORD) {
                // Unknown token
                continue;
            }
            
            // Parse instructions
            final String opcodeName = token.getText().toLowerCase();
            if (!opcodes.containsKey(opcodeName)) {
                // Unknown opcode
                continue;
            }
            
            final Opcode opcode = opcodes.get(opcodeName);
            bytecode.write(opcode.getOpcode());

            switch (opcodeName) {
                case OPCODE_LDC: {
                    final Token next = iterator.next();
                    bytecode.write( findConstantIndexInPool(next.getText()) );
                } break;
                    
                // Search operations with local variables to calc max locals
                case OPCODE_ILOAD:
                case OPCODE_ISTORE: {
                    final Token next = iterator.next();
                    final int value = Integer.parseInt(next.getText());
                    if (value > maxLocalAddress) {
                        maxLocalAddress = value;
                    }
                    bytecode.write(value);
                } break;
                
                // Search operations with fields to calc max fields
                case OPCODE_GETFIELD:
                case OPCODE_PUTFIELD: {
                    final Token next = iterator.next();
                    final int value = Integer.parseInt(next.getText());
                    if (value > maxFieldAddress) {
                        maxFieldAddress = value;
                    }
                    bytecode.write(value);
                } break;
                    
                default: {
                    for (int i = opcode.getNumOfArgs(); i > 0; i--) {
                        final Token next = iterator.next();
                        bytecode.write( Integer.parseInt(next.getText()) );
                    }
                }
                    
            }
        }
        
        final int numLocals = 1 + maxLocalAddress;
        methods.add( new Method(methodName, bytecode.toByteArray(), numLocals) );
        try {
            bytecode.close();
        } catch (IOException ex) { }
    }
    
    private int findConstantIndexInPool(String text) {
        return Arrays.binarySearch(constantPoolValues, text);
    }
    
    private boolean isInstruction(Token token, String name) {
        return ( (token.getType() == TokenType.WORD)
                && (name.equals(token.getText().toLowerCase())) );
    }
    
    private boolean isKeyword(Token token, String name) {
        return ( (token.getType() == TokenType.KEYWORD)
                && (name.equals(token.getText().toLowerCase())) );
    }
}
