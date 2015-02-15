package com.annimon.simplevm;

import static com.annimon.simplevm.Instructions.*;
import com.annimon.simplevm.lib.NativeMethod;
import com.annimon.simplevm.utils.Data;
import com.annimon.simplevm.utils.IntStack;

/**
 *
 * @author aNNiMON
 */
public class VirtualMachine {
    
    private final IntStack operandStack;
    private final Data fields;
    private final Program program;
    
    private byte[] instructionsBytecode;
    private int instructionPointer;
    
    public VirtualMachine(Program program) {
        operandStack = new IntStack();
        this.program = program;
        this.fields = new Data(program.getNumFields());
    }
    
    public void execute() {
        executeMethod("main");
    }
    
    public void executeMethod(String methodName) {
        try {
            executeMethod(program.getMethod(methodName));
        } catch (RuntimeException e) {
            System.out.printf("%s\nat %s, instruction #%d\n",
                    e.toString(), methodName, instructionPointer);
        }
    }
    
    public void executeMethod(Method method) {
        final Data local = new Data(method.getNumLocals());
        instructionPointer = 0;
        instructionsBytecode = method.getInstructionsBytecode();
        do {
            switch(readNextInstruction()) {
                case ILOAD: {
                    int memoryAddr = readNextInstruction();
                    operandStack.push(local.get(memoryAddr));
                } break;
                    
                case ISTORE: {
                    int memoryAddr = readNextInstruction();
                    local.set(memoryAddr, operandStack.pop());
                } break;
                    
                case LDC: {
                    int constAddr = readNextInstruction();
                    Constant constant = program.getConstant(constAddr);
                    if (constant.getType() == Constant.STRING) {
                        operandStack.pushString(((Constant.ConstantString)constant).value);
                    } else {
                        operandStack.push(((Constant.ConstantInt)constant).value);
                    }
                } break;
                    
                case ICONST_0:
                    operandStack.push(0);
                    break;
                    
                case ICONST_1:
                    operandStack.push(1);
                    break;
                    
                case BCONST: {
                    int value = readNextInstruction();
                    operandStack.push(value);
                } break;
                    
                case SCONST: {
                    int value = (readNextInstruction() << 8) | readNextInstruction();
                    operandStack.push(value);
                } break;
                    
                    
                case GETFIELD: {
                    int fieldAddr = readNextInstruction();
                    operandStack.push(fields.get(fieldAddr));
                } break;
                    
                case PUTFIELD: {
                    int fieldAddr = readNextInstruction();
                    fields.set(fieldAddr, operandStack.pop());
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
                    
                    
                case I2S: {
                    int arg = operandStack.pop();
                    operandStack.pushString(Integer.toString(arg));
                } break;
                    
                    
                case INVOKE: {
                    String methodName = operandStack.popString();
                    int oldAddr = instructionPointer;
                    executeMethod(methodName);
                    // Restore bytecode and pointer
                    instructionPointer = oldAddr;
                    instructionsBytecode = method.getInstructionsBytecode();
                } break;
                    
                case INVOKE_NATIVE: {
                    String methodName = operandStack.popString();
                    NativeMethod nm = program.getNativeMethod(methodName);
                    nm.setOperandStack(operandStack);
                    try {
                        nm.invoke();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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
