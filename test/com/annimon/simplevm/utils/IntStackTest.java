package com.annimon.simplevm.utils;

import java.util.EmptyStackException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aNNiMON
 */
public class IntStackTest {
    
    @Test
    public void testPushPop() {
        IntStack instance = new IntStack();
        instance.push(9);
        instance.push(90);
        
        assertEquals(90, instance.pop());
        assertEquals(9, instance.pop());
        
        instance.push(200);
        assertEquals(200, instance.pop());
    }
    
    @Test(expected = RuntimeException.class)
    public void testPushOverflow() {
        IntStack instance = new IntStack(2);
        instance.push(9);
        instance.push(90);
        instance.push(200);
    }

    @Test(expected = EmptyStackException.class)
    public void testPopOnEmptyStack() {
        IntStack instance = new IntStack();
        instance.pop();
    }
    
}
