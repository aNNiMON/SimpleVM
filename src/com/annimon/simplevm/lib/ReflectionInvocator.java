package com.annimon.simplevm.lib;

import java.lang.reflect.Method;

/**
 * Invoke methods by reflection.
 * @author aNNiMON
 */
public class ReflectionInvocator extends NativeMethod {

    @Override
    public void invoke() throws Exception {
        final String methodName = popString();
        final String className = popString();
        
        final int bracketStart = methodName.indexOf('(');
        final int bracketEnd = methodName.indexOf(')');
        final int argumentsCount = bracketEnd - bracketStart - 1;
        final char resultType = methodName.charAt(bracketEnd + 1);
        
        Object[] args = new Object[argumentsCount];
        Class<?>[] argTypes = new Class<?>[argumentsCount];
        if (argumentsCount > 0) {
            // Read argument values
            for (int i = argumentsCount - 1; i >= 0; i--) {
                final char type = methodName.charAt(bracketStart + i + 1);
                switch (type) {
                    case 'I':
                        argTypes[i] = int.class;
                        ((Object[]) args)[i] = pop();
                        break;
                    case 'S':
                        argTypes[i] = String.class;
                        ((Object[]) args)[i] = popString();
                        break;
                    case 'Z':
                        argTypes[i] = boolean.class;
                        ((Object[]) args)[i] = popBoolean();
                        break;
                }
            }
        }
        
        
        Method method = Class.forName(className).getMethod(methodName.substring(0, bracketStart), argTypes);
        Object result = method.invoke(null, args);
        switch (resultType) {
            case 'I':
                push((int) result);
                break;
            case 'S':
                pushString((String) result);
                break;
            case 'Z':
                pushBoolean((boolean) result);
                break;
        }
    }
}
