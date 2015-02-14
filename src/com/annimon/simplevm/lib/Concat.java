package com.annimon.simplevm.lib;

/**
 *
 * @author aNNiMON
 */
public class Concat extends NativeMethod {

    @Override
    public void invoke() {
        pushString( popString() + popString() );
    }
}
