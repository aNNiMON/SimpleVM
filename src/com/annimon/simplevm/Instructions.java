package com.annimon.simplevm;

/**
 *
 * @author aNNiMON
 */
public class Instructions {
    
    public static final byte
            ILOAD = 10,
            ISTORE = 11,
            LDC = 12, // load constant
            
            IADD = 40, // n + m
            ISUB = 41, // n - m
            IMUL = 42, // n * m
            IDIV = 43, // n / m
            INEG = 44, // -n
            IINC = 45, // n++
            IDEC = 46, // n--
            
            IFEQ = 80, // ==
            IFNE = 81, // !=
            IFGE = 82, // >=
            IFGT = 83, // >
            IFLE = 84, // <=
            IFLT = 85, // <
            
            GOTO = 120,
            
            INVOKE = (byte) 200,
            INVOKE_PRINT = (byte) 201,
            
            NOP = 0;
            
}
