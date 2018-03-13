package com.mapler.nexmo;

import com.mapler.service.INotifier;
import com.mapler.ui.OnlyExt;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author JAYED
 */
public class NexmoAuthority extends javax.swing.JPanel implements INotifier {

    private static Logger log = Logger.getLogger(NexmoEngineUI.class);
    private INotifier iNotifier = getiNotifier();
    private String userType;
    private String userId;
    private String username;
    private String password;
    private NexmoModel nexmoModel;
    private DBUtil dbutil;
    private List<Integer> listOfDeleteRow;

    public NexmoAuthority() {
        initComponents();
    }

    public NexmoAuthority(String username, String password, String userType, String userId) {

        this.username = username;
        this.password = password;
        this.userType = userType;
        this.userId = userId;
        initComponents();
        initSettings();
        showData();
        initUI();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTFAuthorityFilePath = new javax.swing.JTextField();
        jSelectFIleButton = new javax.swing.JButton();
        jImportButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        TblModel = new javax.swing.JTable();
        jRefrshButton = new javax.swing.JButton();
        message = new javax.swing.JLabel();
        jDeleteButton = new javax.swing.JButton();
        jCBIsHeader = new javax.swing.JCheckBox();

        setPreferredSize(new java.awt.Dimension(672, 570));

        jLabel1.setText("AuthorityFile");

        jTFAuthorityFilePath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTFAuthorityFilePathActionPerformed(evt);
            }
        });

        jSelectFIleButton.setText("Select file");
        jSelectFIleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSelectFIleButtonActionPerformed(evt);
            }
        });

        jImportButton.setText("Import");
        jImportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jImportButtonActionPerformed(evt);
            }
        });

        TblModel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Serial", "Api Key", "Api Secret", "Numbers", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(TblModel);

        jRefrshButton.setText("Refresh");
        jRefrshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRefrshButtonActionPerformed(evt);
            }
        });

        jDeleteButton.setText("Delete");
        jDeleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jDeleteButtonActionPerformed(evt);
            }
        });

        jCBIsHeader.setText("Is Header");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jDeleteButton)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(message)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jTFAuthorityFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(37, 37, 37)
                                    .addComponent(jSelectFIleButton)
                                    .addGap(18, 18, 18)
                                    .addComponent(jImportButton))
                                .addComponent(jCBIsHeader))
                            .addGap(18, 18, 18)
                            .addComponent(jRefrshButton))))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(message)
                .addGap(14, 14, 14)
                .addComponent(jCBIsHeader)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTFAuthorityFilePath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSelectFIleButton)
                    .addComponent(jImportButton)
                    .addComponent(jRefrshButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jDeleteButton)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(144, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTFAuthorityFilePathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTFAuthorityFilePathActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTFAuthorityFilePathActionPerformed

    private void jSelectFIleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSelectFIleButtonActionPerformed
        try {
            JFileChooser fc = new JFileChooser();
            fc.addChoosableFileFilter(new OnlyExt());

            int returnval = fc.showOpenDialog(null);
            if (returnval == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                jTFAuthorityFilePath.setText(file.getPath());
            }
        } catch (Throwable ex) {
            //message.setText("Error " + ex.getMessage());
            log.debug("Error " + ex);
        }
    }//GEN-LAST:event_jSelectFIleButtonActionPerformed

    private void jRefrshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRefrshButtonActionPerformed
        showData();
    }//GEN-LAST:event_jRefrshButtonActionPerformed
    private void showData() {
        DefaultTableModel model = (DefaultTableModel) TblModel.getModel();
        model.setRowCount(0);
        getDbutil();
        List<AuthInfo> keys = dbutil.getAuthorityInfoFromDb(userId);
        if (keys == null || keys.isEmpty()) {
            iNotifier.notify("Auth file is empty.");
            log.debug("Auth file is empty.");
            return;
        }
        boolean selected = false;
        for (AuthInfo key : keys) {
            model.addRow(new Object[]{key.getId(),
                key.getApiKey(),
                key.getApiSecret(),
                key.getNumber(),
                selected});
        }
    }
    private void jImportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jImportButtonActionPerformed
        getDbutil();
        if (jTFAuthorityFilePath.getText().isEmpty()) {
            return;
        }

        try {
            FileInputStream fs = new FileInputStream(jTFAuthorityFilePath.getText());
            HSSFWorkbook workbook = new HSSFWorkbook(fs);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            boolean isHeader = false;
            if (jCBIsHeader.isSelected()) {
                isHeader = true;
            }
            while (rowIterator.hasNext()) {
                if (isHeader) {
                    isHeader = false;
                    rowIterator.next();
                    continue;
                }
                Row row = rowIterator.next();
                String api_key = row.getCell(0).toString().trim();
                String api_secret = row.getCell(1).toString().trim();
                String number = row.getCell(2).toString().trim();
                dbutil.insertDataOfAuthorityFile(userId, number, api_key, api_secret);
            }
            iNotifier.notify("Saving information done..");
            log.debug("Saving information done..");
        } catch (IOException ex) {
            ex.printStackTrace();
            log.debug("Error in " + ex.getMessage());
        }
        showData();
    }//GEN-LAST:event_jImportButtonActionPerformed

    private void jDeleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jDeleteButtonActionPerformed
        getDbutil();
        try{
            dbutil.deleteAuthInfoFromDB(listOfDeleteRow);
            showData();;
        }catch(Exception ex){
            iNotifier.notify("Error in.."+ex.getMessage());
            log.debug("Error in.."+ex.getMessage());
        }
    }//GEN-LAST:event_jDeleteButtonActionPerformed
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

    public void initUI() {
        DefaultTableModel model = (DefaultTableModel) TblModel.getModel();
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    String id = TblModel.getValueAt(e.getFirstRow(), 0).toString();
                    getListOfDeleteRow();
                    listOfDeleteRow.add(Integer.parseInt(id));
                    //update(id, status);
                }
            }
        });
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

    public NexmoModel getsModel() {
        if (nexmoModel == null) {
            nexmoModel = new NexmoModel();
        }
        return nexmoModel;
    }

    public void setsModel(NexmoModel nexmoModel) {
        this.nexmoModel = nexmoModel;
    }

    public List<Integer> getListOfDeleteRow() {
        if (listOfDeleteRow == null) {
            listOfDeleteRow = new ArrayList<Integer>();
        }
        return listOfDeleteRow;
    }

    public void setListOfDeleteRow(List<Integer> listOfDeleteRow) {
        this.listOfDeleteRow = listOfDeleteRow;
    }

    public void initSettings() {
        this.setsModel(null);
    }

    public DBUtil getDbutil() {
        if (dbutil == null) {
            dbutil = new DBUtil();
        }
        return dbutil;
    }

    public void setDbutil(DBUtil dbutil) {
        this.dbutil = dbutil;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TblModel;
    private javax.swing.JCheckBox jCBIsHeader;
    private javax.swing.JButton jDeleteButton;
    private javax.swing.JButton jImportButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton jRefrshButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jSelectFIleButton;
    private javax.swing.JTextField jTFAuthorityFilePath;
    private javax.swing.JLabel message;
    // End of variables declaration//GEN-END:variables
}
