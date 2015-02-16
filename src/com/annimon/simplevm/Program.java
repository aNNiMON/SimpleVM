package com.annimon.simplevm;

import com.annimon.simplevm.lib.NativeMethod;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Program structure
 * @author aNNiMON
 */
public class Program {
    
    private static final int SIGNATURE = 0xBAD1DEA;
    
    public static Program read(InputStream is) throws IOException {
        final DataInputStream dis = new DataInputStream(is);
        
        final int signature = dis.readInt();
        if (signature != SIGNATURE)
            throw new RuntimeException("Bad file signature");
        
        // Read constant pool
        final int constantPoolSize = dis.readInt();
        final ConstantPool pool = new ConstantPool(constantPoolSize);
        for (int i = 0; i < constantPoolSize; i++) {
            final int constType = dis.readByte();
            switch (constType) {
                case Constant.INT:
                    pool.set(i, Constant.integer(dis.readInt()));
                    break;
                case Constant.STRING:
                    pool.set(i, Constant.string(dis.readUTF()));
                    break;
            }
        }
        
        final int numFields = dis.readInt();
        final Program program = new Program(pool, numFields);
        
        // Read methods
        final int methodsCount = dis.readInt();
        for (int i = 0; i < methodsCount; i++) {
            final String name = dis.readUTF();
            
            final int bytecodeLength = dis.readInt();
            byte[] bytecode = new byte[bytecodeLength];
            dis.readFully(bytecode, 0, bytecodeLength);
            
            final int numLocals = dis.readInt();
            program.addMethod(name, bytecode, numLocals);
        }
        
        return program;
    }
    
    public static void write(Program program, OutputStream os) throws IOException {
        final DataOutputStream dos = new DataOutputStream(os);
        
        dos.writeInt(SIGNATURE);
        
        // Write constant pool
        final int constantPoolSize = program.constantPool.getSize();
        dos.writeInt(constantPoolSize);
        for (int i = 0; i < constantPoolSize; i++) {
            final Constant constant = program.getConstant(i);
            dos.writeByte(constant.getType());
            switch (constant.getType()) {
                case Constant.INT:
                    dos.writeInt(((Constant.ConstantInt)constant).value);
                    break;
                case Constant.STRING:
                    dos.writeUTF(((Constant.ConstantString)constant).value);
                    break;
            }
        }
        
        dos.writeInt(program.getNumFields());
        
        // Write methods
        dos.writeInt(program.methods.size());
        for (Method method : program.methods.values()) {
            dos.writeUTF(method.getName());
            
            final int bytecodeLength = method.getInstructionsBytecode().length;
            dos.writeInt(bytecodeLength);
            dos.write(method.getInstructionsBytecode(), 0, bytecodeLength);
            
            dos.writeInt(method.getNumLocals());
        }
    }
    
    private final Map<String, Method> methods;
    private final Map<String, NativeMethod> nativeMethods;
    private final ConstantPool constantPool;
    private final int numFields;

    public Program() {
        this(5, 0);
    }
    
    public Program(int constantPoolSize) {
        this(constantPoolSize, 0);
    }
    
    public Program(int constantPoolSize, int numFields) {
        this(new ConstantPool(constantPoolSize), numFields);
    }
    
    public Program(ConstantPool constantPool, int numFields) {
        this.methods = new HashMap<>();
        this.nativeMethods = new HashMap<>();
        this.constantPool = constantPool;
        this.numFields = numFields;
    }
    
    public int getNumFields() {
        return numFields;
    }
    
    public void addMethod(Method method) {
        methods.put(method.getName(), method);
    }
    
    public void addMethod(String name, byte[] instructions, int numLocals) {
        addMethod(new Method(name, instructions, numLocals));
    }
    
    public Method getMethod(String name) {
        return methods.get(name);
    }
    
    public void addNativeMethod(String name, NativeMethod method) {
        nativeMethods.put(name, method);
    }
    
    public NativeMethod getNativeMethod(String name) {
        return nativeMethods.get(name);
    }

    public Constant getConstant(int addr) {
        return constantPool.get(addr);
    }

    public void setConstant(int addr, Constant value) {
        constantPool.set(addr, value);
    }
    
    public void execute() {
        new VirtualMachine(this).execute();
    }
}
