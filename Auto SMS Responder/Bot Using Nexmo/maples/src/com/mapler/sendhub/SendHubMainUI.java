package com.mapler.sendhub;

import com.mapler.ui.*;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;

/**
 *
 * @author none
 */
public final class SendHubMainUI extends javax.swing.JFrame {

    private String username;
    private String password;
    private String userType;
    private String userId;
    private SendHubEngineUI sendHubEngineUI;
    private JScrollPane jsp;
    private static Logger log = Logger.getLogger(SendHubMainUI.class);

    /**
     * Creates new form MainUI
     */
    public SendHubMainUI() {
        initComponents();
        init();
    }

    public SendHubMainUI(String username, String password, String userType, String userId) {
        log.info("Init MainUI");
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.userId = userId;
        initComponents();
        init();
    }

    @SuppressWarnings("unchecked")
    public void init() {
        sendHubEngineUI = new SendHubEngineUI(this.getUsername(), this.getPassword(), this.getUserType(), this.getUserId());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuEngine = new javax.swing.JMenu();
        jMenuItemShow = new javax.swing.JMenuItem();

        jMenu4.setText("jMenu4");

        jMenu5.setText("jMenu5");

        jMenuItem6.setText("jMenuItem6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenuEngine.setText("Engine");
        jMenuEngine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showEngineHandler(evt);
            }
        });

        jMenuItemShow.setText("Sendhub");
        jMenuItemShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showEngineHandler(evt);
            }
        });
        jMenuEngine.add(jMenuItemShow);

        jMenuBar1.add(jMenuEngine);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 732, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 411, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void clear() {
        if (jsp != null) {
            this.remove(jsp);
        }
        sendHubEngineUI.setVisible(false);
    }
    private void showEngineHandler(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showEngineHandler
        this.clear();
        this.getContentPane().setLayout(new BorderLayout());
        this.add(sendHubEngineUI, BorderLayout.CENTER);
        sendHubEngineUI.initSettings();
        sendHubEngineUI.setVisible(true);
        this.pack();
    }//GEN-LAST:event_showEngineHandler

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Throwable ex) {
           ex.printStackTrace();
        } 
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SendHubMainUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuEngine;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItemShow;
    // End of variables declaration//GEN-END:variables
}
