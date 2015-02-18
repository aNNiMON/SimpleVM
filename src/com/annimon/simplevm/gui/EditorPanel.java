package com.annimon.simplevm.gui;

import com.annimon.simplevm.Program;
import com.annimon.simplevm.assembler.Assembler;
import com.annimon.simplevm.lib.Concat;
import com.annimon.simplevm.lib.GUI;
import com.annimon.simplevm.lib.Print;
import com.annimon.simplevm.lib.ReflectionInvocator;
import com.annimon.simplevm.utils.ExceptionHandler;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

/**
 *
 * @author aNNiMON
 */
public class EditorPanel extends JPanel {
    
    private final JToolBar toolBar;
    private final JEditorPane editor;
    
    private String projectName;
    private Program program;

    public EditorPanel() {
        toolBar = new JToolBar();
        editor = new JEditorPane();
        initUI();
        projectName = "NewProject";
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        JButton save = new JButton("Save");
        save.setFocusable(false);
        save.addActionListener(saveListener);
        toolBar.add(save);
        
        toolBar.addSeparator();
        
        JButton build = new JButton("Build");
        build.setFocusable(false);
        build.addActionListener(buildListener);
        toolBar.add(build);
        
        JButton run = new JButton("Run");
        run.setFocusable(false);
        run.addActionListener(runListener);
        toolBar.add(run);
        
        toolBar.setRollover(true);
        add(toolBar, BorderLayout.NORTH);
        
        editor.setFont(new Font("Lucida Console", Font.PLAIN, 14));
        editor.setPreferredSize(new Dimension(350, 300));
        add(new JScrollPane(editor), BorderLayout.CENTER);
    }
    
    private void compile(boolean saveSvmFile) {
        program = Assembler.fromString(editor.getText());
        program.addNativeMethod("print", new Print());
        program.addNativeMethod("concat", new Concat());
        program.addNativeMethod("gui", new GUI());
        program.addNativeMethod("reflectCall", new ReflectionInvocator());
        if (saveSvmFile) {
            try (OutputStream fos = new FileOutputStream(projectName + ".svm")) {
                Program.write(program, fos);
            } catch (IOException ex) {
                ExceptionHandler.handle(ex);
            }
        } else {
            program.execute();
        }
    }
    
    private final ActionListener buildListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            compile(true);
        }
    };
    
    private final ActionListener runListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            compile(false);
        }
    };
    
    private final ActionListener saveListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File("."));
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            if (chooser.showSaveDialog(EditorPanel.this) == JFileChooser.APPROVE_OPTION) {
                final File selected = chooser.getSelectedFile();
                String filename = selected.getName();
                final int dot = filename.lastIndexOf('.');
                if (0 < dot && dot < filename.length() - 1) {
                    projectName = filename.substring(dot+1);
                } else {
                    projectName = filename;
                    filename = filename + ".avm";
                }
                
                try (OutputStream os = new FileOutputStream(filename);
                     OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8")) {
                    writer.write(editor.getText());
                } catch (IOException ex) {
                    ExceptionHandler.handle(ex);
                }
            }
        }
    };
}
