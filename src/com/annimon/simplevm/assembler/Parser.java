package com.annimon.simplevm.assembler;

import com.annimon.simplevm.Constant;
import com.annimon.simplevm.ConstantPool;
import static com.annimon.simplevm.Instructions.*;
import com.annimon.simplevm.Method;
import com.annimon.simplevm.Program;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author aNNiMON
 */
public class Parser {
    
    private static final String KEYWORD_METHOD = ".method";
    private static final String OPCODE_LDC = "ldc";
    
    public static Program parse(List<Token> tokens) {
        return new Parser(tokens).process().getProgram();
    }

    private Program program;
    private final List<Token> tokens;
    private final List<Constant> constantPool;
    private String[] constantPoolValues;
    private final List<Method> methods;
    private final HashMap<String, Opcode> opcodes;
    private int maxFieldAddress;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        constantPool = new ArrayList<>();
        methods = new ArrayList<>();
        maxFieldAddress = -1;
        
        opcodes = new HashMap<>();
        opcodes.put("iload", new Opcode(ILOAD, 1));
        opcodes.put("istore", new Opcode(ISTORE, 1));
        opcodes.put(OPCODE_LDC, new Opcode(LDC, 1));
        opcodes.put("iconst_0", new Opcode(ICONST_0));
        opcodes.put("iconst_1", new Opcode(ICONST_1));
        opcodes.put("bconst", new Opcode(BCONST, 1));
        opcodes.put("sconst", new Opcode(SCONST, 2));

        opcodes.put("getfield", new Opcode(GETFIELD, 1));
        opcodes.put("putfield", new Opcode(PUTFIELD, 1));

        opcodes.put("iadd", new Opcode(IADD));
        opcodes.put("isub", new Opcode(ISUB));
        opcodes.put("imul", new Opcode(IMUL));
        opcodes.put("idiv", new Opcode(IDIV));
        opcodes.put("ineg", new Opcode(INEG));
        opcodes.put("iinc", new Opcode(IINC));
        opcodes.put("idec", new Opcode(IDEC));

        opcodes.put("ifeq", new Opcode(IFEQ, 1));
        opcodes.put("ifne", new Opcode(IFNE, 1));
        opcodes.put("ifge", new Opcode(IFGE, 1));
        opcodes.put("ifgt", new Opcode(IFGT, 1));
        opcodes.put("ifle", new Opcode(IFLE, 1));
        opcodes.put("iflt", new Opcode(IFLT, 1));

        opcodes.put("goto", new Opcode(GOTO, 1));

        opcodes.put("i2s", new Opcode(I2S));

        opcodes.put("invoke", new Opcode(INVOKE));
        opcodes.put("invoke_native", new Opcode(INVOKE_NATIVE));

        opcodes.put("nop", new Opcode(NOP));
    }
    
    public Program getProgram() {
        return program;
    }
    
    private void createProgram() {
        ConstantPool pool = new ConstantPool(constantPool.toArray(new Constant[0]));
        final int maxFields = 1+ maxFieldAddress;
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

            if (opcodeName.equals(OPCODE_LDC)) {
                final Token next = iterator.next();
                bytecode.write( findConstantIndexInPool(next.getText()) );
            } else {
                for (int i = opcode.getNumOfArgs(); i > 0; i--) {
                    final Token next = iterator.next();
                    bytecode.write( Integer.parseInt(next.getText()) );
                }
            }
        }
        
        int numLocals = 1 + calculateMaxFieldAndLocal(bytecode.toByteArray());
        methods.add( new Method(methodName, bytecode.toByteArray(), numLocals) );
        try {
            bytecode.close();
        } catch (IOException ex) { }
    }
    
    private int calculateMaxFieldAndLocal(byte[] bytecode) {
        int maxLocalAddress = -1;
        int maxGlobalAddress = -1;
        final int last = bytecode.length - 1;
        for (int i = 0; i < last; i++) {
            switch (bytecode[i]) {
                case ILOAD:
                case ISTORE:
                    if (bytecode[i+1] > maxLocalAddress) {
                        maxLocalAddress = bytecode[i+1];
                    }
                    break;
                    
                case GETFIELD:
                case PUTFIELD:
                    if (bytecode[i+1] > maxGlobalAddress) {
                        maxGlobalAddress = bytecode[i+1];
                    }
                    break;
            }
        }
        maxFieldAddress = Math.max(maxFieldAddress, maxGlobalAddress);
        return maxLocalAddress;
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
