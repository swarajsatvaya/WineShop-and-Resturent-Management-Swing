/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package design;

import bean.Db_connection;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
public class SellsReport extends javax.swing.JPanel {

    /**
     * Creates new form SelsReport
     */
    public SellsReport() {
        initComponents();
         btn.add(Bdate);
        btn.add(today);
        dt1.setVisible(false);
        to.setVisible(false);
        dt2.setVisible(false);
        submit.setVisible(false);
    }

       
    public static String formatDate (String givenDate, String givenFormat, String changeableFormat) throws ParseException 
    {

        Date initDate = new SimpleDateFormat(givenFormat).parse(givenDate);
        SimpleDateFormat formatter = new SimpleDateFormat(changeableFormat);
        String changedDate = formatter.format(initDate);
        return changedDate;
    }
    
    
     public void TodayData()
    {
       try{
           if(!type.getSelectedItem().equals("-Select-"))
            {
            Connection con=Db_connection.getConnection();
                         //--food---
            if(type.getSelectedItem().equals("Food"))
            {
            PreparedStatement ps=con.prepareStatement("SELECT date as \"Date\", billno as \"Bill No\", name as \"Customer Name\",\n" +
                                                        "phno as \"Customer Ph No\", tableno as \"Table No\", tno as \"Transaction No\", offamt+onamt+foamt as \"Amount\"\n" +
                                                        "FROM bill WHERE bfrom = 3 and date = ?");// change the quary
            ps.setString(1, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            ResultSet rs=ps.executeQuery();
            sellsreportTab.setModel(DbUtils.resultSetToTableModel(rs));    
            rs.close();
            ps.close();
            }      
                     //--on shop--//
            if(type.getSelectedItem().equals("On Shop"))
            {
            PreparedStatement ps1=con.prepareStatement("SELECT date as \"Date\", billno as \"Bill No\", name as \"Customer Name\",\n" +
                                                        "phno as \"Customer Ph No\", tableno as \"Table No\", tno as \"Transaction No\", offamt+onamt+foamt as \"Amount\"\n" +
                                                        "FROM bill WHERE bfrom = 2 and date = ?");// change the quary
            ps1.setString(1, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            ResultSet rs1=ps1.executeQuery();
           
            sellsreportTab.setModel(DbUtils.resultSetToTableModel(rs1));  
            rs1.close();
            ps1.close();
            }
                     //-Of Shop--//
            if(type.getSelectedItem().equals("Off Shop"))
            {
            PreparedStatement ps2=con.prepareStatement("SELECT date as \"Date\", billno as \"Bill No\", name as \"Customer Name\",\n" +
                                                        "phno as \"Customer Ph No\", tableno as \"Table No\", tno as \"Transaction No\", offamt+onamt+foamt as \"Amount\"\n" +
                                                        "FROM bill WHERE bfrom = 1 and date = ?");// change the quary
            ps2.setString(1, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            ResultSet rs2=ps2.executeQuery();
            sellsreportTab.setModel(DbUtils.resultSetToTableModel(rs2));  
            rs2.close();
            ps2.close();
            } 
            con.close();
           }
           else
            {
            JOptionPane.showMessageDialog(null, "Select shop please");
            }
          }
          catch(Exception e)
          {
           e.printStackTrace();
           }
       }

     
     
   public void BetweenDateData()
      {
      try
         {
          if(!type.getSelectedItem().equals("-Select-"))
          {
           Connection con=Db_connection.getConnection();
           
           if(type.getSelectedItem().equals("Food"))
           {
           PreparedStatement ps=con.prepareStatement("SELECT date as \"Date\", billno as \"Bill No\", name as \"Customer Name\",\n" +
                                                    "phno as \"Customer Ph No\", tableno as \"Table No\", tno as \"Transaction No\", offamt+onamt+foamt as \"Amount\"\n" +
                                                    "FROM bill WHERE bfrom = 3 and date between ? and ?");// change the quary
           ps.setString(1, formatDate(((JTextField)dt1.getDateEditor().getUiComponent()).getText(),"dd-MM-yyyy","yyyy-MM-dd"));
           ps.setString(2, formatDate(((JTextField)dt2.getDateEditor().getUiComponent()).getText(), "dd-MM-yyyy" ,"yyyy-MM-dd"));
           ResultSet rs=ps.executeQuery();
           sellsreportTab.setModel(DbUtils.resultSetToTableModel(rs));    
           rs.close();
           ps.close();
           }
           
           if(type.getSelectedItem().equals("On Shop"))
           {
           PreparedStatement ps1=con.prepareStatement("SELECT date as \"Date\", billno as \"Bill No\", name as \"Customer Name\",\n" +
                                                        "phno as \"Customer Ph No\", tableno as \"Table No\", tno as \"Transaction No\", offamt+onamt+foamt as \"Amount\"\n" +
                                                        "FROM bill WHERE bfrom = 2 and date between ? and ?");// change the quary
           ps1.setString(1, formatDate(((JTextField)dt1.getDateEditor().getUiComponent()).getText(),"dd-MM-yyyy","yyyy-MM-dd"));
           ps1.setString(2, formatDate(((JTextField)dt2.getDateEditor().getUiComponent()).getText(), "dd-MM-yyyy" ,"yyyy-MM-dd"));
           ResultSet rs1=ps1.executeQuery();
           sellsreportTab.setModel(DbUtils.resultSetToTableModel(rs1));  
           rs1.close();
           ps1.close();
           }
           
           if(type.getSelectedItem().equals("Off Shop"))
           {
           PreparedStatement ps2=con.prepareStatement("SELECT date as \"Date\", billno as \"Bill No\", name as \"Customer Name\",\n" +
                                                        "phno as \"Customer Ph No\", tableno as \"Table No\", tno as \"Transaction No\", offamt+onamt+foamt as \"Amount\"\n" +
                                                        "FROM bill WHERE bfrom = 1 and date between ? and ?");// change the quary
           ps2.setString(1, formatDate(((JTextField)dt1.getDateEditor().getUiComponent()).getText(),"dd-MM-yyyy","yyyy-MM-dd"));
           ps2.setString(2, formatDate(((JTextField)dt2.getDateEditor().getUiComponent()).getText(), "dd-MM-yyyy" ,"yyyy-MM-dd"));
           ResultSet rs2=ps2.executeQuery();
           sellsreportTab.setModel(DbUtils.resultSetToTableModel(rs2));  
           rs2.close();
           ps2.close();
           } 
           con.close();
         }
         else
          {
          JOptionPane.showMessageDialog(null, "Select shop please");
          }
          }
          catch(Exception e)
          {
            e.printStackTrace();
          }
      }
     
      DefaultTableModel model=null;
       
     public void table_clear()  
     { 
         model = (DefaultTableModel) sellsreportTab.getModel();
         while(model.getRowCount() > 0 )
         {
         for(int i = 0 ; i < model.getRowCount();i++)
          {
          model.removeRow(i);
          }
        }
     }  
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn = new javax.swing.ButtonGroup();
        type = new javax.swing.JComboBox<>();
        jPanel10 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        today = new javax.swing.JRadioButton();
        Bdate = new javax.swing.JRadioButton();
        dt1 = new com.toedter.calendar.JDateChooser();
        to = new javax.swing.JLabel();
        dt2 = new com.toedter.calendar.JDateChooser();
        submit = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        sellsreportTab = new javax.swing.JTable();
        print = new javax.swing.JButton();

        type.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Select-", "Food", "On Shop", "Off Shop" }));

        jPanel10.setBackground(new java.awt.Color(0, 0, 0));

        jLabel21.setBackground(new java.awt.Color(0, 0, 0));
        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Sells Report");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
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

        to.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        to.setText("To");

        dt2.setDateFormatString("dd-MM-yyyy");

        submit.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        submit.setText("Submit");
        submit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                submitMouseClicked(evt);
            }
        });

        sellsreportTab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(sellsreportTab);

        print.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        print.setText("Generate Report");
        print.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                printMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(type, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(today, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Bdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dt1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(to, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dt2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(submit)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(352, 352, 352)
                .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dt1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(today)
                        .addComponent(Bdate)
                        .addComponent(type, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dt2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(submit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(to, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void todayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_todayMouseClicked
        // TODO add your handling code here:
     
        dt1.setVisible(false);
        to.setVisible(false);
        dt2.setVisible(false);
        submit.setVisible(false);
        table_clear(); 
        TodayData();

    }//GEN-LAST:event_todayMouseClicked

    private void BdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BdateMouseClicked
        // TODO add your handling code here:

        dt1.setVisible(true);
        to.setVisible(true);
        dt2.setVisible(true);
        submit.setVisible(true);
        table_clear();
       
    }//GEN-LAST:event_BdateMouseClicked

    private void submitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_submitMouseClicked
        
           
           if(!((JTextField)dt1.getDateEditor().getUiComponent()).getText().equals("")&&!((JTextField)dt1.getDateEditor().getUiComponent()).getText().equals(""))
            {
            table_clear();
            BetweenDateData();
            }
           else{
               JOptionPane.showMessageDialog(null,"Choose Date Please");
               } 
        
    
    }//GEN-LAST:event_submitMouseClicked

    private void printMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_printMouseClicked
        // TODO add your handling code here:
         printComponenet();
    }//GEN-LAST:event_printMouseClicked

   public void printComponenet()
    {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName(" Sells Report");

        pj.setPrintable ((Graphics pg, PageFormat pf, int pageNum) -> 
        {
        if (pageNum > 0)
        {
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D g2 = (Graphics2D) pg;
        g2.translate(pf.getImageableX(), pf.getImageableY());
        g2.scale(0.6658, 0.6666);
        //g2.rotate(1);
        sellsreportTab.paint(g2);
        return Printable.PAGE_EXISTS;
        }
        );
        if (pj.printDialog() == false)
            return;

        try 
        {
            pj.print();
        } 
        catch (PrinterException ex) 
        {
            // handle exception
        } 
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton Bdate;
    private javax.swing.ButtonGroup btn;
    private com.toedter.calendar.JDateChooser dt1;
    private com.toedter.calendar.JDateChooser dt2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton print;
    private javax.swing.JTable sellsreportTab;
    private javax.swing.JButton submit;
    private javax.swing.JLabel to;
    private javax.swing.JRadioButton today;
    public javax.swing.JComboBox<String> type;
    // End of variables declaration//GEN-END:variables
}
