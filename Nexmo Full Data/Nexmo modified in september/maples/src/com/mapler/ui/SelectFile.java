/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mapler.ui;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class SelectFile extends JFrame {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setLayout(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Select File for Linking");
        frame.setSize(400, 100);
        Container container = frame.getContentPane();
        container.setLayout(new GridBagLayout());

        final JTextField text = new JTextField(20);

        JButton b = new JButton("Select File");
        text.setBounds(20, 20, 120, 20);
        b.setBounds(150, 20, 80, 20);

        // b.setText("<html><font color='blue'><u>Select File</u></font></html>");
        b.setHorizontalAlignment(SwingConstants.LEFT);
        //b.setBorderPainted(false);
        //b.setOpaque(false);
        // b.setBackground(Color.lightGray);
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.addChoosableFileFilter(new OnlyExt());

                int returnval = fc.showOpenDialog(null);
                if (returnval == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    text.setText(file.getPath());
                }
            }
        });
        container.add(text);
        container.add(b);
        frame.setVisible(true);
    }
}