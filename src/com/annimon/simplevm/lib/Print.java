package com.annimon.simplevm.lib;

/**
 *
 * @author aNNiMON
 */
public class Print extends NativeMethod {

    @Override
    public void invoke() {
        System.out.println(popString());
    }
}
