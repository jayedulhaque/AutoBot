
package com.mapler.nexmo;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;

/**
 *
 * @author JAYED
 */
public class NexmoMainUI extends javax.swing.JFrame {

    private String username;
    private String password;
    private String userType;
    private String userId;
    private NexmoEngineUI nexmoEngineUI;
    private NexmoAuthority nexmoAuthority;
    private JScrollPane jsp;
    private static Logger log = Logger.getLogger(NexmoMainUI.class);

    /**
     * Creates new form NexmoMainUI
     */
    public NexmoMainUI() {
        initComponents();
        init();
    }

    public NexmoMainUI(String username, String password, String userType, String userId) {
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
        nexmoEngineUI = new NexmoEngineUI(this.getUsername(), this.getPassword(), this.getUserType(), this.getUserId());
        nexmoAuthority = new NexmoAuthority(this.getUsername(), this.getPassword(), this.getUserType(), this.getUserId());
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
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jCheckBoxMenuItemForNumber = new javax.swing.JCheckBoxMenuItem();
        jMenuEngine = new javax.swing.JMenu();
        jMenuItemShow = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("Numbers");

        jCheckBoxMenuItemForNumber.setSelected(true);
        jCheckBoxMenuItemForNumber.setText("Numbers");
        jCheckBoxMenuItemForNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItemForNumberActionPerformed(evt);
            }
        });
        jMenu1.add(jCheckBoxMenuItemForNumber);

        jMenuBar1.add(jMenu1);

        jMenuEngine.setText("Engine");

        jMenuItemShow.setSelected(true);
        jMenuItemShow.setText("Nexmo");
        jMenuItemShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemShowActionPerformed(evt);
            }
        });
        jMenuEngine.add(jMenuItemShow);

        jMenuBar1.add(jMenuEngine);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItemShowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemShowActionPerformed
        this.clear();
        this.getContentPane().setLayout(new BorderLayout());
        this.add(nexmoEngineUI, BorderLayout.CENTER);
        nexmoEngineUI.initSettings();
        nexmoEngineUI.setVisible(true);
        this.pack();
    }//GEN-LAST:event_jMenuItemShowActionPerformed

    private void jCheckBoxMenuItemForNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItemForNumberActionPerformed
        this.clear();
        this.getContentPane().setLayout(new BorderLayout());
        this.add(nexmoAuthority, BorderLayout.CENTER);
        nexmoAuthority.initSettings();
        nexmoAuthority.setVisible(true);
        this.pack();
    }//GEN-LAST:event_jCheckBoxMenuItemForNumberActionPerformed
    private void clear() {
        if (jsp != null) {
            this.remove(jsp);
        }
        nexmoEngineUI.setVisible(false);
        nexmoAuthority.setVisible(false);
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
            java.util.logging.Logger.getLogger(NexmoMainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NexmoMainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NexmoMainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NexmoMainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NexmoMainUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItemForNumber;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuEngine;
    private javax.swing.JCheckBoxMenuItem jMenuItemShow;
    // End of variables declaration//GEN-END:variables
}
