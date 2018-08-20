/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package design;

import bean.Db_connection;
import static design.WineDistribution.formatDate;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Arka
 */
public class TransportPassReport extends javax.swing.JPanel {

    /**
     * Creates new form TransportPassReport
     */
    public TransportPassReport() {

        initComponents();
        bsTable.getTableHeader().setPreferredSize(new Dimension(jScrollPane3.getWidth(), 40));
        LiqureDescription.getTableHeader().setPreferredSize(new Dimension(jScrollPane2.getWidth(), 40));
        btn.add(Bdate);
        btn.add(today);
        dt1.setVisible(false);
        to.setVisible(false);
        dt2.setVisible(false);
        submit.setVisible(false);
        jLabel1.setVisible(false);
        jTextField1.setVisible(false);
    }

    public void TPassData() {
        try {
            Connection con = Db_connection.getConnection();
            PreparedStatement ps = con.prepareStatement("select tpassno as \"Transport Pass No\", date as \"Date\", reqdate as \"Requisition No & Date\",\n"
                    + "tslno as \"Serial No Of Transport Pass\", consign_n as \"Name of The Consignee\",\n"
                    + "consign_a as \"Address of the Consignee\", name_conr as \"Name of the Consignor\", add_conr as \"Address of The Consignor\",\n"
                    + "make_vehicle as \"Make of the Vehical as specified in the Registration Certificate as Declared by the applicant\",\n"
                    + "model_vehicle as \"Model of the Vehical as specified in the Registration Certificate as Declared by the applicant\",\n"
                    + "reg_vehicle as \"Motor Vehical Registration No. of the vehicle as specified in the registration certificate as declared by the applicant\",\n"
                    + "invoiceno as \"Invoice No. of the consignment to be Transported as declared by the consignor\",\n"
                    + "tamt as \"Total Transaction Amount\" from dealer NATURAL JOIN constant where tpassno = ? ");
            ps.setString(1, jTextField1.getText().trim());
            ResultSet rs = ps.executeQuery();
            bsTable.setModel(DbUtils.resultSetToTableModel(rs));
            rs.close();
            ps.close();
            PreparedStatement ps1 = con.prepareStatement("select a.dcode as \"Bar Code\", a.date as \"Date\", kofl as \"Kind Of Forign Liqure\", cat as \"Category\", bname as \"Brand Name\", strng as \"Serngth\", mlpb as \"Measure In ML\",\n"
                    + "qty/bpc as \"Quantity By Case\", qty as \"Quantity By Bottle\", pricepb*qty as \"Price\", shopt as \"Shop Option\", a.tpassno as \"Transport Pass No\"\n"
                    + "from mainstock as a inner join ddesc as b on a.dcode = b.dcode where ttype = 1 and a.tpassno = ?");
            ps1.setString(1, jTextField1.getText().trim());
            ResultSet rs1 = ps1.executeQuery();
            LiqureDescription.setModel(DbUtils.resultSetToTableModel(rs1));
            rs1.close();
            ps1.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String formatDate(String givenDate, String givenFormat, String changeableFormat) throws ParseException {

        Date initDate = new SimpleDateFormat(givenFormat).parse(givenDate);
        SimpleDateFormat formatter = new SimpleDateFormat(changeableFormat);
        String changedDate = formatter.format(initDate);
        //SimpleDateFormat format = new SimpleDateFormat(changeableFormat);
        //Date date=format.parse(changedDate);
        return changedDate;
    }

    public void BetweenDateData() {
        try {

            Connection con = Db_connection.getConnection();
            PreparedStatement ps = con.prepareStatement("select tpassno as \"Transport Pass No\", date as \"Date\", reqdate as \"Requisition No & Date\",\n"
                    + "tslno as \"Serial No Of Transport Pass\", consign_n as \"Name of The Consignee\",\n"
                    + "consign_a as \"Address of the Consignee\", name_conr as \"Name of the Consignor\", add_conr as \"Address of The Consignor\",\n"
                    + "make_vehicle as \"Make of the Vehical as specified in the Registration Certificate as Declared by the applicant\",\n"
                    + "model_vehicle as \"Model of the Vehical as specified in the Registration Certificate as Declared by the applicant\",\n"
                    + "reg_vehicle as \"Motor Vehical Registration No. of the vehicle as specified in the registration certificate as declared by the applicant\",\n"
                    + "invoiceno as \"Invoice No. of the consignment to be Transported as declared by the consignor\",\n"
                    + "tamt as \"Total Transaction Amount\" from dealer NATURAL JOIN constant where tpassno in \n"
                    + "(select tpassno from dealer where date between ? and ?)");
            ps.setString(1, formatDate(((JTextField) dt1.getDateEditor().getUiComponent()).getText(), "dd-MM-yyyy", "yyyy-MM-dd"));
            ps.setString(2, formatDate(((JTextField) dt2.getDateEditor().getUiComponent()).getText(), "dd-MM-yyyy", "yyyy-MM-dd"));
            ResultSet rs = ps.executeQuery();
            bsTable.setModel(DbUtils.resultSetToTableModel(rs));
            rs.close();
            ps.close();
            PreparedStatement ps1 = con.prepareStatement("select a.dcode as \"Bar Code\", a.date as \"Date\", kofl as \"Kind Of Forign Liqure\", cat as \"Category\", bname as \"Brand Name\", strng as \"Serngth\", mlpb as \"Measure In ML\",\n"
                    + "qty/bpc as \"Quantity By Case\", qty as \"Quantity By Bottle\", pricepb*qty as \"Price\", shopt as \"Shop Option\", a.tpassno as \"Transport Pass No\"\n"
                    + "from mainstock as a inner join ddesc as b on a.dcode = b.dcode where a.tpassno in (select tpassno from mainstock where ttype = 1 and date between ? and ?)");// change the quary
            ps1.setString(1, formatDate(((JTextField) dt1.getDateEditor().getUiComponent()).getText(), "dd-MM-yyyy", "yyyy-MM-dd"));
            ps1.setString(2, formatDate(((JTextField) dt2.getDateEditor().getUiComponent()).getText(), "dd-MM-yyyy", "yyyy-MM-dd"));
            ResultSet rs1 = ps1.executeQuery();

            LiqureDescription.setModel(DbUtils.resultSetToTableModel(rs1));
            rs1.close();
            ps1.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "<html><h3>Please Select Correct Dates</html>");
        }
    }

    DefaultTableModel model = null;
    DefaultTableModel model1 = null;

    public void table_clear() {
        model = (DefaultTableModel) bsTable.getModel();
        while (model.getRowCount() > 0) {
            for (int i = 0; i < model.getRowCount(); i++) {
                model.removeRow(i);
            }
        }

    }

    public void table_clear1() {
        model1 = (DefaultTableModel) LiqureDescription.getModel();
        while (model1.getRowCount() > 0) {
            for (int i = 0; i < model1.getRowCount(); i++) {
                model1.removeRow(i);
            }
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn = new javax.swing.ButtonGroup();
        jPanel10 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        today = new javax.swing.JRadioButton();
        Bdate = new javax.swing.JRadioButton();
        dt1 = new com.toedter.calendar.JDateChooser();
        dt2 = new com.toedter.calendar.JDateChooser();
        jSeparator1 = new javax.swing.JSeparator();
        Generate = new javax.swing.JButton();
        submit = new javax.swing.JButton();
        to = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        bsTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        LiqureDescription = new javax.swing.JTable();

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));

        jLabel21.setBackground(new java.awt.Color(0, 0, 0));
        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText(" Transport Pass Report");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
        );

        today.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        today.setText("Today");
        today.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                todayMouseClicked(evt);
            }
        });

        Bdate.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        Bdate.setText("Between Date");
        Bdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BdateMouseClicked(evt);
            }
        });

        dt1.setDateFormatString("dd-MM-yyyy");

        dt2.setDateFormatString("dd-MM-yyyy");

        Generate.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        Generate.setText("Generate Report");
        Generate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                GenerateMouseClicked(evt);
            }
        });
        Generate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenerateActionPerformed(evt);
            }
        });

        submit.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        submit.setText("Submit");
        submit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                submitMouseClicked(evt);
            }
        });
        submit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitActionPerformed(evt);
            }
        });

        to.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        to.setText("To");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel1.setText("Transport Pass No.");

        jTextField1.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        bsTable.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        bsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9"
            }
        ));
        jScrollPane3.setViewportView(bsTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1124, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("<html><h4>Basic Particulars<h4></html>", jPanel1);

        LiqureDescription.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8", "Title 9", "Title 10"
            }
        ));
        jScrollPane2.setViewportView(LiqureDescription);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1124, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("<html><h4>Liqure Description</html>", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Generate, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(today, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Bdate)
                .addGap(18, 18, 18)
                .addComponent(dt1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(to, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dt2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(submit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dt1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(today)
                        .addComponent(Bdate))
                    .addComponent(dt2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(submit)
                    .addComponent(to, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Generate, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void todayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_todayMouseClicked
        // TODO add your handling code here:
        dt1.setVisible(false);
        to.setVisible(false);
        dt2.setVisible(false);
        submit.setVisible(false);
        jLabel1.setVisible(true);
        jTextField1.setVisible(true);
        table_clear();
        table_clear1();

    }//GEN-LAST:event_todayMouseClicked

    private void BdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BdateMouseClicked
        // TODO add your handling code here:

        dt1.setVisible(true);
        to.setVisible(true);
        dt2.setVisible(true);
        submit.setVisible(true);
        jLabel1.setVisible(false);
        jTextField1.setVisible(false);
        table_clear();
        table_clear1();

    }//GEN-LAST:event_BdateMouseClicked

    private void submitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitMouseClicked
        if (!((JTextField) dt1.getDateEditor().getUiComponent()).getText().equals("") && !((JTextField) dt1.getDateEditor().getUiComponent()).getText().equals("")) {
            table_clear();
            table_clear1();
            BetweenDateData();
        } else {
            JOptionPane.showMessageDialog(null, "Choose Date Please");
        }
    }//GEN-LAST:event_submitMouseClicked

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (bsTable.getRowCount() == 0 && LiqureDescription.getRowCount() == 0) {
                TPassData();
            } else {
                table_clear();  // for clear the j table row
                TPassData();// for gating to day data from data base
            }
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyPressed

    private void submitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_submitActionPerformed

    private void GenerateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GenerateMouseClicked
        // TODO add your handling code here:
        printComponenet();
    }//GEN-LAST:event_GenerateMouseClicked

    private void GenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenerateActionPerformed
        printComponenet();
        // TODO add your handling code here:
    }//GEN-LAST:event_GenerateActionPerformed

    public void printComponenet() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName(" Sells Report");

        pj.setPrintable((Graphics pg, PageFormat pf, int pageNum)
                -> {
            if (pageNum > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2 = (Graphics2D) pg;
            g2.translate(pf.getImageableX(), pf.getImageableY());
            g2.scale(0.6658, 0.6666);
            //g2.rotate(1);
            bsTable.paint(g2);
            return Printable.PAGE_EXISTS;
        }
        );
        if (pj.printDialog() == false) {
            return;
        }

        try {
            pj.print();
        } catch (PrinterException ex) {
            // handle exception
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton Bdate;
    private javax.swing.JButton Generate;
    private javax.swing.JTable LiqureDescription;
    private javax.swing.JTable bsTable;
    private javax.swing.ButtonGroup btn;
    private com.toedter.calendar.JDateChooser dt1;
    private com.toedter.calendar.JDateChooser dt2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton submit;
    private javax.swing.JLabel to;
    public javax.swing.JRadioButton today;
    // End of variables declaration//GEN-END:variables
}
