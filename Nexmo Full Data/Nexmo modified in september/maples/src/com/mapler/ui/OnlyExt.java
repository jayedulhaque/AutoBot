package com.mapler.ui;

import java.io.File;

/**
 *
 * @author Mithun
 */
public class OnlyExt extends javax.swing.filechooser.FileFilter {

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return false;
        }
        String name = file.getName().toLowerCase();
        return (name.endsWith(".xls"));
    }

    @Override
    public String getDescription() {
        return "Excel ( *.xls)";
    }
}
