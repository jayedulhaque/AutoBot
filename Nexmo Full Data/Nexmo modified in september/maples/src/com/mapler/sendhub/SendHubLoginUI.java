package com.mapler.sendhub;

import com.mapler.ui.*;
import com.mapler.utility.HttpHelper;
import com.mapler.utility.IConstant;
import com.mapler.utility.Util;
import java.awt.Color;
import java.net.URL;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

/**
 *
 * @author none
 */
public class SendHubLoginUI extends javax.swing.JDialog {

    private static Logger log = Logger.getLogger(SendHubLoginUI.class);

    /**
     * Creates new form LoginUI
     */
    public SendHubLoginUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        PropertyConfigurator.configure("./src/log4j.properties");
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        username = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        message = new javax.swing.JLabel();
        password = new javax.swing.JPasswordField();
        jBKill = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Username");

        jLabel2.setText("Password");

        jBKill.setText("Stop Process");
        jBKill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBKillActionPerformed(evt);
            }
        });

        jLabel3.setText("SendHub");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(70, Short.MAX_VALUE)
                .addComponent(message, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jBKill)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(username, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(password))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(162, 162, 162)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(message, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBKill)
                .addContainerGap(88, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            this.message.setText("");
            String name = this.username.getText();
            String pass = this.password.getText();
            if ((name == null || name.isEmpty()) && (pass == null || pass.isEmpty())) {
                this.message.setText("User name and password can not be empty");
                return;
            } else if (name == null || name.isEmpty()) {
                this.message.setText("User name can not be empty");
                return;
            } else if (pass == null || pass.isEmpty()) {
                this.message.setText("Password can not be empty");
                return;
            }
            String mac = Util.getSystemUUID();
            if (!isOKToLogin(name + ":" + mac)) {
                return;
            }
            String uri = "http://" + IConstant.HOSTNAME + "/index.php?r=userAccount/login&username="
                    + name + "&password=" + pass + "&mac=" + name + ":" + mac;
            String response = HttpHelper.post(new URL(uri), "");

            Document document = DocumentHelper.parseText(response);
            String errorCode = document.valueOf("Response/errorcode");
            if (errorCode.equalsIgnoreCase("001")) {
                String userid = document.valueOf("Response/userid");
                String usertype = document.valueOf("Response/usertype");
                String is_pva = document.valueOf("Response/is_pva");
                SendHubMainUI mainUI = new SendHubMainUI(name, pass, usertype, userid);
                mainUI.setVisible(true);
                mainUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainUI.setAlwaysOnTop(false);

                this.dispose();
            } else {
                String msg = document.valueOf("Response/message");
                if (msg.isEmpty()) {
                    msg = "Error on request. Please contact with system administrator.";
                }
                this.message.setText(msg);
                this.message.setBackground(Color.red);
            }
        } catch (Throwable ex) {
            this.message.setText("Error " + ex.getMessage());
            this.message.setText(ex.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jBKillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBKillActionPerformed
        try {
            Runtime.getRuntime().exec("taskkill /F /IM java.exe");
            Runtime.getRuntime().exec("taskkill /F /IM iRobot.exe");
            Runtime.getRuntime().exec("taskkill /F /IM javaw.exe");
        } catch (Throwable ex) {
            ex.printStackTrace();
            log.error("jBKillActionPerformed: " + ex.getMessage());
        }
    }//GEN-LAST:event_jBKillActionPerformed
    
    private boolean isOKToLogin(String mac) {
        try {
            if (!Util.isLoginByMac(mac)) {
                return true;
            }

            int p = Util.isAppsRunning();
            if (p == 0) {
                this.message.setText("Unable to execute iRobot. Please contact with system administrator.");
                this.jButton1.setVisible(false);
                this.jBKill.setVisible(false);
                return true;
            } else if (p == 1 || p == 2) {
                String uri = "http://" + IConstant.HOSTNAME + "/index.php?r=userAccount/logoutByMac&mac=" + mac;
                String response = HttpHelper.post(new URL(uri), "");

                Document document = DocumentHelper.parseText(response);
                String errorCode = document.valueOf("Response/errorcode");
                if (!errorCode.equalsIgnoreCase("001")) {
                    this.message.setText("Error on apps running. Please contact with system administrator.");
                    this.jButton1.setVisible(false);
                    this.jBKill.setVisible(false);
                } else {
                    return true;
                }

            } else if (p > 1) {
                this.message.setText("Too many java / iRobot process running. Kill first then start again.");
                this.jButton1.setVisible(false);
                this.jBKill.setVisible(true);
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
            log.error("doInit: " + ex.getMessage());
        }
        return false;
    }
    
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
        } catch (ClassNotFoundException ex) {
           
        } catch (InstantiationException ex) {
           
        } catch (IllegalAccessException ex) {
           
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
          
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SendHubLoginUI dialog = new SendHubLoginUI(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBKill;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel message;
    private javax.swing.JPasswordField password;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
