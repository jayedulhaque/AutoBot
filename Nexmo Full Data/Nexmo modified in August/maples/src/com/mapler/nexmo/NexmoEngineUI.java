package com.mapler.nexmo;

import com.mapler.service.INotifier;
import com.mapler.ui.OnlyExt;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author JAYED
 */
public class NexmoEngineUI extends javax.swing.JPanel implements INotifier {

    private String username;
    private String password;
    private NexmoModel nexmoModel;
    private static Logger log = Logger.getLogger(NexmoEngineUI.class);
    private INotifier iNotifier;
    private NexmoReadWorker nexmoReadWorker;
    private NexmoSendWorker nexmoSendWorker;
    private String userType;
    private String userId;

    public NexmoEngineUI(String username, String password, String userType, String userId) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.userId = userId;
        initComponents();
        initSettings();
    }

    public NexmoModel getsModel() {
        if (nexmoModel == null) {
            nexmoModel = new NexmoModel();
        }
        return nexmoModel;
    }

    public void setsModel(NexmoModel nexmoModel) {
        this.nexmoModel = nexmoModel;
    }

    public NexmoEngineUI() {
        initComponents();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        start = new javax.swing.JButton();
        end = new javax.swing.JButton();
        message = new javax.swing.JLabel();
        jBStop = new javax.swing.JButton();
        jCBRepeated = new javax.swing.JCheckBox();
        jTFRepeatTime = new javax.swing.JTextField();
        jTFWaitTime = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTFSendLimit = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButtonSend = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jTFMessageFile = new javax.swing.JTextField();
        jBFileSelector = new javax.swing.JButton();

        jButton1.setText("jButton1");

        start.setText("Read");
        start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startActionPerformed(evt);
            }
        });

        end.setText("Stop & Exit");
        end.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endActionPerformed(evt);
            }
        });

        jBStop.setText("Stop");
        jBStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBStopActionPerformed(evt);
            }
        });

        jCBRepeated.setText("Repeated");

        jLabel2.setText("Repeat Time");

        jLabel3.setText("Wait Time");

        jLabel7.setText("Limit to send");

        jButtonSend.setText("Send");
        jButtonSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSendActionPerformed(evt);
            }
        });

        jLabel12.setText("Messagefile");

        jBFileSelector.setText("Select file...");
        jBFileSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBFileSelectorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(message, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(start, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jCBRepeated)
                                    .addComponent(jButtonSend))
                                .addGap(140, 140, 140)
                                .addComponent(jBStop)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(end))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(160, 160, 160)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel12)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel7))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTFRepeatTime, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jTFMessageFile, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jBFileSelector))
                                            .addComponent(jTFWaitTime, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTFSendLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(0, 14, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(message, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 161, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFMessageFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBFileSelector)
                            .addComponent(jLabel12))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFRepeatTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTFWaitTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(18, 18, 18)
                        .addComponent(jTFSendLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7)))
                .addGap(47, 47, 47)
                .addComponent(jCBRepeated)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBStop)
                    .addComponent(end)
                    .addComponent(start)
                    .addComponent(jButtonSend))
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(37, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    public static void main(String[] args) {
        NexmoEngineUI nexmoengineui = new NexmoEngineUI();
        nexmoengineui.setVisible(true);
        //nexmoengineui.startActionPerformed(null);

    }
    private void startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startActionPerformed

        this.getsModel().setMessageFile(this.jTFMessageFile.getText());
        if (StringUtils.isNotBlank(this.jTFRepeatTime.getText())) {
            this.getsModel().setRepeatTime(Integer.parseInt(this.jTFRepeatTime.getText()));
        }
        if (StringUtils.isNotBlank(this.jTFWaitTime.getText())) {
            this.getsModel().setWaitTime(Integer.parseInt(this.jTFWaitTime.getText()));
        }

        this.getsModel().setRepeated(jCBRepeated.isSelected());

        this.getsModel().setStopEngine(false);
        this.getsModel().setUserid(userId);
        nexmoModel.setUsername(username);
        nexmoModel.setPassword(password);
        nexmoReadWorker = new NexmoReadWorker(this.getiNotifier(), this.getsModel());
        nexmoReadWorker.update();

    }//GEN-LAST:event_startActionPerformed

    private void endActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endActionPerformed
        try {
            Runtime.getRuntime().exec("taskkill /F /IM java.exe");

        } catch (Throwable ex) {
            log.debug("Error " + ex);
        }
    }//GEN-LAST:event_endActionPerformed

    private void jBStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBStopActionPerformed
        this.getsModel().setStopEngine(true);
    }//GEN-LAST:event_jBStopActionPerformed

    private void jButtonSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendActionPerformed

        this.getsModel().setMessageFile(this.jTFMessageFile.getText());

        if (StringUtils.isNotBlank(this.jTFWaitTime.getText())) {
            this.getsModel().setWaitTime(Integer.parseInt(this.jTFWaitTime.getText()));
        }
        if (StringUtils.isNotBlank(this.jTFRepeatTime.getText())) {
            this.getsModel().setRepeatTime(Integer.parseInt(this.jTFRepeatTime.getText()));
        }
        this.getsModel().setRepeated(jCBRepeated.isSelected());

        this.getsModel().setStopEngine(false);
        this.getsModel().setUserid(userId);
        nexmoModel.setUsername(username);
        nexmoModel.setPassword(password);
        nexmoSendWorker = new NexmoSendWorker(this.getiNotifier(), this.getsModel());
        nexmoSendWorker.update();
    }//GEN-LAST:event_jButtonSendActionPerformed

    private void jBFileSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBFileSelectorActionPerformed
        try {
            JFileChooser fc = new JFileChooser();
            fc.addChoosableFileFilter(new OnlyExt());

            int returnval = fc.showOpenDialog(null);
            if (returnval == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                jTFMessageFile.setText(file.getPath());
            }
        } catch (Throwable ex) {
            message.setText("Error " + ex.getMessage());
            log.debug("Error " + ex);
        }
    }//GEN-LAST:event_jBFileSelectorActionPerformed

    public void initSettings() {
        this.setsModel(null);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton end;
    private javax.swing.JButton jBFileSelector;
    private javax.swing.JButton jBStop;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonSend;
    private javax.swing.JCheckBox jCBRepeated;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTFMessageFile;
    private javax.swing.JTextField jTFRepeatTime;
    private javax.swing.JTextField jTFSendLimit;
    private javax.swing.JTextField jTFWaitTime;
    private javax.swing.JLabel message;
    private javax.swing.JButton start;
    // End of variables declaration//GEN-END:variables
 @Override
    public void notify(final String info) {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    message.setText(info);
                }
            });
        } catch (Throwable ex) {
            log.debug("notify:: Error causes " + ex.getMessage());
        }
    }

    public INotifier getiNotifier() {
        if (iNotifier == null) {
            iNotifier = this;
        }
        return iNotifier;
    }

    public void setiNotifier(INotifier iNotifier) {
        this.iNotifier = iNotifier;
    }

    @Override
    public void write(String info) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
