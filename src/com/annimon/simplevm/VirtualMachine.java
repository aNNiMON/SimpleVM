package com.annimon.simplevm;

import static com.annimon.simplevm.Instructions.*;
import com.annimon.simplevm.utils.Data;
import com.annimon.simplevm.utils.IntStack;

/**
 *
 * @author aNNiMON
 */
public class VirtualMachine {
    
    private final IntStack operandStack;
    private final ConstantPool constantPool;
    private final Data memory;
    
    private final byte[] instructionsBytecode;
    private int instructionPointer;
    
    public VirtualMachine(byte[] instructions) {
        this(instructions, new ConstantPool(0), new Data(20));
    }
    
    public VirtualMachine(byte[] instructions, ConstantPool constantPool, Data memory) {
        operandStack = new IntStack();
        this.constantPool = constantPool;
        this.memory = memory;
        
        this.instructionsBytecode = instructions;
        instructionPointer = 0;
    }
    
    public void execute() {
        do {
            switch(readNextInstruction()) {
                case ILOAD: {
                    int memoryAddr = readNextInstruction();
                    operandStack.push(memory.get(memoryAddr));
                } break;
                    
                case ISTORE: {
                    int memoryAddr = readNextInstruction();
                    memory.set(memoryAddr, operandStack.pop());
                } break;
                    
                case LDC: {
                    int constAddr = readNextInstruction();
                    Constant constant = constantPool.get(constAddr);
                    if (constant.getType() == Constant.STRING) {
                        operandStack.pushString(((Constant.ConstantString)constant).value);
                    } else {
                        operandStack.push(((Constant.ConstantInt)constant).value);
                    }
                } break;

                    
                case IADD:
                    operandStack.push(operandStack.pop() + operandStack.pop());
                    break;
                    
                case IMUL:
                    operandStack.push(operandStack.pop() * operandStack.pop());
                    break;
                    
                case ISUB: {
                    int arg2 = operandStack.pop();
                    int arg1 = operandStack.pop();
                    operandStack.push(arg1 - arg2);
                } break;
                    
                case IDIV: {
                    int arg2 = operandStack.pop();
                    int arg1 = operandStack.pop();
                    operandStack.push(arg1 / arg2);
                } break;
                    
                case INEG:
                    operandStack.push(-operandStack.pop());
                    break;
                    
                case IINC: {
                    int arg = operandStack.pop() + 1;
                    operandStack.push(arg);
                } break;
                    
                case IDEC: {
                    int arg = operandStack.pop() - 1;
                    operandStack.push(arg);
                } break;

                case IFEQ: {
                    int arg2 = operandStack.pop();
                    int arg1 = operandStack.pop();
                    if (arg1 == arg2) instructionPointer = readNextInstruction();
                } break;
                    
                case IFNE: {
                    int arg2 = operandStack.pop();
                    int arg1 = operandStack.pop();
                    if (arg1 != arg2) instructionPointer = readNextInstruction();
                } break;
                    
                case IFGE: {
                    int arg2 = operandStack.pop();
                    int arg1 = operandStack.pop();
                    if (arg1 >= arg2) instructionPointer = readNextInstruction();
                } break;
                    
                case IFGT: {
                    int arg2 = operandStack.pop();
                    int arg1 = operandStack.pop();
                    if (arg1 > arg2) instructionPointer = readNextInstruction();
                } break;
                    
                case IFLE: {
                    int arg2 = operandStack.pop();
                    int arg1 = operandStack.pop();
                    if (arg1 <= arg2) instructionPointer = readNextInstruction();
                } break;
                    
                case IFLT: {
                    int arg2 = operandStack.pop();
                    int arg1 = operandStack.pop();
                    if (arg1 < arg2) instructionPointer = readNextInstruction();
                } break;
                    
                case GOTO:
                    instructionPointer = readNextInstruction();
                    break;
                    
                case INVOKE_PRINT: {
                    System.out.println(operandStack.popString());
                } break;
                    
                case NOP:
                    // do nothing
                    break;
            }
        } while (hasMoreInstructions());
    }
    
    private byte readNextInstruction() {
        return instructionsBytecode[instructionPointer++];
    }
    
    private boolean hasMoreInstructions() {
        return instructionPointer < instructionsBytecode.length;
    }
}
