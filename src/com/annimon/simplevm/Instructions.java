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
            ICONST_0 = 13, // 0
            ICONST_1 = 14, // 1
            BCONST = 15, // 1 byte const
            SCONST = 16, // 2 bytes const
            
            GETFIELD = 20,
            PUTFIELD = 21,
            
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
            
            I2S = (byte) 150, // int to string
            
            INVOKE = (byte) 200,
            INVOKE_NATIVE = (byte) 201,
            
            NOP = 0;
            
}
