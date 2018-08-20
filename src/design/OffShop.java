/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package design;

import bean.Db_connection;
import bean.ObjDao;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Java Developer
 */
public class OffShop extends javax.swing.JFrame {

    /**
     * Creates new form OffShop
     */
    int p,q;
    
    public OffShop() {
        ObjDao.of=this; 
        initComponents();    
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        p=(int)t.getScreenSize().getWidth();
        q=(int)t.getScreenSize().getHeight();
        this.setSize( p, q );
        this.setExtendedState(this.MAXIMIZED_BOTH);
        cash.setSelected(true);
        Add();
    }

int  hight2=10;
ArrayList < AddItemOffShop> Aio = new ArrayList < AddItemOffShop >();

 public void Add()
 {
    AddItemOffShop ob1= new  AddItemOffShop();
    ob1.setSize(p-200,60);
    ob1.setLocation(0,hight2);
    hight2=hight2+60;
    jPanel3.repaint();
    jScrollPane1.repaint();

    jPanel3.add(ob1);
    ob1.setVisible(true);
    jPanel3.revalidate();
    jScrollPane1.revalidate();
    ob1.barcode.requestFocus();
    if(!Aio.isEmpty())
    {
        Aio.get(Aio.size() - 1).jLabel6.setVisible(false);
        Aio.get(Aio.size()-1).jLabel7.setVisible(false);
    }
    //System.out.println("21 "+ +ob.getWidth() +" "+ob.getHeight());  
    Aio.add(ob1);   
 }
 
 public void calculate()
 {
     Double total = 0.0;
     for(int i=0; i<Aio.size(); i++)
     {
         if(!Aio.get(i).barcode.getText().trim().equals("") && !Aio.get(i).brname.getText().trim().equals("")
                 && !Aio.get(i).qty.getText().trim().equals("") && !Aio.get(i).tprice.getText().trim().equals(""))
                        total = total + Double.parseDouble(Aio.get(i).tprice.getText().trim());
     }
     tBill.setText(String.valueOf(new DecimalFormat("##.##").format(total)));
 }
    
  public void RemoveOff()
    {
      if(Aio.size()>1)
          {
            hight2=hight2-60;
             Aio.get( Aio.size()-1).setVisible(false);
             Aio.remove( Aio.size()-1);
            //System.out.println("1 "+count2.size());  
            
             Aio.get( Aio.size()-1).jLabel6.setVisible(true);  
             Aio.get( Aio.size()-1).jLabel7.setVisible(true); 
          }   
    }
 
   public void okClicked()
   {
       calculate();
       if(!cname.getText().trim().equals("") && !tBill.getText().trim().equals("0"))
        {
           if(cash.isSelected())
            {
                offPtype = 1;
                OffPTNo tForm = new OffPTNo();
                this.setEnabled(false);
                this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                tForm.setVisible(true);
                tForm.tno.setText("@InCash");
                tForm.callOkMouse();
                //inSert();
            }
            else if(online.isSelected())
            {
                offPtype = 0;
                OffPTNo tForm = new OffPTNo();
                tForm.setVisible(true);
                this.setEnabled(false);
                this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                tForm.tno.requestFocus();
                //inSert();
            }
        }
   }
   
   int offPtype = 1;
   long billno = 0;
  
   public void inSert()
   {
       
       calculate();
       if(!cname.getText().trim().equals("") && !tBill.getText().trim().equals(""))
        {
           try 
           {
               Connection con = Db_connection.getConnection();
               PreparedStatement ps = con.prepareStatement("insert into bill (name,phno,ptype,tno,bfrom,offamt,onamt,foamt,tableno,date) values (?,?,?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
               ps.setString(1, cname.getText().trim());
               ps.setString(2, phno.getText().trim());
               ps.setInt(3, offPtype);
               ps.setString(4, ObjDao.offTno);
               ps.setInt(5,1);
               ps.setString(6, tBill.getText().trim());
               ps.setInt(7, 0);
               ps.setInt(8, 0);
               ps.setInt(9, 0);
               ps.setString(10, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
               ps.executeUpdate();
               ResultSet rs = ps.getGeneratedKeys();
               if(rs.next())
                    billno = rs.getLong(1);
               else
               {
                   JOptionPane.showMessageDialog(null, "<html>Bill No Can't Be Generated<br>Please Try Again</html>");
                   return;
               }
               rs.close();
               ps.close();
               
               for(AddItemOffShop each : Aio)
               {
                   if(!each.barcode.getText().trim().equals("") && !each.brname.getText().trim().equals("")
                            && !each.qty.getText().trim().equals("") && !each.tprice.getText().trim().equals("") && !each.rate.getText().trim().equals(""))
                   {
                        ps = con.prepareStatement("insert into offshopstock (date, dcode, qty, instock, pricepb, ttype, billno) values (?,?,?,?,?,?,?)");
                        ps.setString(1, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                        ps.setString(2, each.barcode.getText().trim());
                        ps.setInt(3, Integer.parseInt(each.qty.getText().trim()));
                        ps.setInt(4, (each.stock - Integer.parseInt(each.qty.getText().trim())));
                        ps.setDouble(5, Double.parseDouble(each.rate.getText().trim()));
                        ps.setInt(6, 0);
                        ps.setLong(7, billno);

                        int stat = ps.executeUpdate();
                        
                        if(stat == 0)
                            JOptionPane.showMessageDialog(null, "<html><center>Some Critical Error Occoured<br>Please Contact With DataBase Administrator</html>");
                   }
               }
               ps.close();
               con.close();
               
               
           } 
           catch (SQLException ex) 
           {
               JOptionPane.showMessageDialog(null, "<html><center>Some Error Occoured<br>Please Contact With DataBase Administrator</html>");
               Logger.getLogger(OffShop.class.getName()).log(Level.SEVERE, null, ex);
           }
            
        }
       new OffShop().setVisible(true);
       this.dispose();
   }
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ptype = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tBill = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cname = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        phno = new javax.swing.JTextField();
        cash = new javax.swing.JRadioButton();
        online = new javax.swing.JRadioButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        ok = new javax.swing.JButton();
        cal = new javax.swing.JButton();
        newBill = new javax.swing.JButton();

        ptype.add(cash);
        ptype.add(online);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Off Shop Billing System");

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("OFF SHOP SELL");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Barcode");

        jLabel3.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Quantity");

        jLabel4.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Price");

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Category");

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Brand Name");

        tBill.setEditable(false);
        tBill.setBackground(new java.awt.Color(255, 255, 255));
        tBill.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        tBill.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel7.setText("Ph No.");

        cname.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel8.setText("Customer Name");

        jLabel9.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel9.setText("Total Bill");

        phno.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        cash.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        cash.setText("Cash");

        online.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        online.setText("Online");

        jLabel12.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("ML/Bottle");

        jLabel13.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Rate");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cname, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(phno, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tBill, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cash)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(online))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 981, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cash, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(phno, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tBill, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(online, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jLabel12, jLabel13, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6});

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        ok.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        ok.setText("OK");
        ok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okActionPerformed(evt);
            }
        });

        cal.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        cal.setText("Calculate");
        cal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calActionPerformed(evt);
            }
        });

        newBill.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        newBill.setText("New Bill");
        newBill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBillActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(937, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ok, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newBill, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(ok, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cal, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(newBill, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1807, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void calActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calActionPerformed
        calculate();
        // TODO add your handling code here:
    }//GEN-LAST:event_calActionPerformed

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        okClicked();
        // TODO add your handling code here:
    }//GEN-LAST:event_okActionPerformed

    private void newBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBillActionPerformed
        new OffShop().setVisible(true);
        this.dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_newBillActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cal;
    private javax.swing.JRadioButton cash;
    private javax.swing.JTextField cname;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton newBill;
    private javax.swing.JButton ok;
    private javax.swing.JRadioButton online;
    private javax.swing.JTextField phno;
    private javax.swing.ButtonGroup ptype;
    private javax.swing.JTextField tBill;
    // End of variables declaration//GEN-END:variables
}
