package com.annimon.simplevm.gui;

import javax.swing.JFrame;

/**
 * Simple assembler GUI.
 * @author aNNiMON
 */
public class MainFrame extends JFrame {
    
    public MainFrame() {
        super("SimpleVM Assembler");
        setLocationByPlatform(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new EditorPanel());
        pack();
    }
}
