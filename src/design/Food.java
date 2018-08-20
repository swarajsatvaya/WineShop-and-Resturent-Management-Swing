
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
public class Food extends javax.swing.JFrame {

    /**
     * Creates new form Food
     */
    int w,h;
    public Food() {
        ObjDao.fo=this;
       
        initComponents();
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension d = t.getScreenSize();
        w=(int)t.getScreenSize().getWidth();
        h=(int)t.getScreenSize().getHeight();
        this.setSize( w, h );
        this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

       Table(); 
       //  AddFood1();
       Add();
       btn.add(online);
       btn.add(cash);
    }

 int  hight2=10;
 ArrayList <AddFood> Af = new ArrayList <AddFood >();

 public void Add()
 {
   AddFood  ob1= new AddFood ();
        ob1.setSize(w-200,60);
        ob1.setLocation(0,hight2);
        hight2=hight2+60;
        jPanel2.repaint();
        jScrollPane1.repaint();

        jPanel2.add(ob1);
        ob1.setVisible(true);
        jPanel2.revalidate();
        jScrollPane1.revalidate();
        ob1.Ftype.requestFocus();
        if(!Af.isEmpty())
        {
         Af.get(Af.size() - 1).jLabel5.setVisible(false);
         Af.get(Af.size()-1).jLabel4.setVisible(false); 
        }
        //System.out.println("21 "+ +ob.getWidth() +" "+ob.getHeight());  
        Af.add(ob1);
 }
 
  public void RemoveFood()
    {
      if(Af.size()>1)
          {
            hight2=hight2-60;
             Af.get( Af.size()-1).setVisible(false);
             Af.remove( Af.size()-1);
            //System.out.println("1 "+count2.size());  
            
             Af.get( Af.size()-1).jLabel4.setVisible(true);  
             Af.get( Af.size()-1).jLabel5.setVisible(true); 
          }   
    }
 
 
  public void Table()
  {
   try{
       Connection con=Db_connection.getConnection();
       PreparedStatement ps=con.prepareStatement("Select tableno from constant where slno='"+1+"'" );
       ResultSet rs=ps.executeQuery();
       if(rs.next())
       {
           int i=rs.getInt("tableno");
           for(int m=1;m<=i;m++){
               tableno.addItem("Table No-"+m);
           }
    //           
       }
       rs.close();
       ps.close();
       con.close();
      }catch(Exception e)
      {
          e.printStackTrace();
      }
  }
 
 public void calculate()
 {  
    Double total = 0.0;
    for(AddFood each : Af)
    {
        if(!each.Ftype.getSelectedItem().equals("-Select-") && !each.fname1.getSelectedItem().equals("-Select-")
                 && !each.qty2.getText().trim().equals("0") && !each.price.getText().trim().equals("00.00") && !each.vat1.getText().trim().equals("00.00"))
        {
                        total = total + Double.parseDouble(each.price.getText().trim());            
        }
    }
    tBill.setText(String.valueOf(new DecimalFormat("##.##").format(total)));
 }
 
 int foodPtype = 1;
 long billno = 0;
 
 public void okClicked()
 {
    calculate();
    if(!cname.getText().trim().equals("") && !tBill.getText().trim().equals(""))
    {
       if(cash.isSelected())
        {
            foodPtype = 1;
            FoodTNo tForm = new FoodTNo();
            this.setEnabled(false);
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            tForm.setVisible(true);
            tForm.tno.setText("@InCash");
            tForm.callOkMouse();
            //inSert();
        }
        else if(online.isSelected())
        {
            foodPtype = 0;
            FoodTNo tForm = new FoodTNo();
            tForm.setVisible(true);
            this.setEnabled(false);
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            tForm.tno.requestFocus();
            //inSert();
        }
    }
 }
 
 public void inSert()
   {
       
       calculate();
       if(!cname.getText().trim().equals("") && !tBill.getText().trim().equals("") && !tableno.getSelectedItem().toString().trim().equals("-Select-"))
        {
           try 
           {
               Connection con = Db_connection.getConnection();
               PreparedStatement ps = con.prepareStatement("insert into bill (name,phno,ptype,tno,bfrom,offamt,onamt,foamt,tableno,date) values (?,?,?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
               ps.setString(1, cname.getText().trim());
               ps.setString(2, phno.getText().trim());
               ps.setInt(3, foodPtype);
               ps.setString(4, ObjDao.FoodTno);
               ps.setInt(5,3);
               ps.setInt(6, 0);
               ps.setInt(7, 0);
               ps.setString(8, tBill.getText().trim());
               String trname = tableno.getSelectedItem().toString().trim();
               ps.setInt(9, Integer.parseInt(trname.substring(9, trname.length())));
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
               con.close();
               
               /*for(AddFood each : Af)
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
               }*/
               
               
           } 
           catch (SQLException ex) 
           {
               JOptionPane.showMessageDialog(null, "<html><center>Some Error Occoured<br>Please Contact With DataBase Administrator</html>");
               Logger.getLogger(OffShop.class.getName()).log(Level.SEVERE, null, ex);
           }
            
        }
       new Food().setVisible(true);
       this.dispose();
   }
 
 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel48 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tableno = new javax.swing.JComboBox<>();
        jLabel46 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        cname = new javax.swing.JTextField();
        Quantity = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        phno = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tBill = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        Amount = new javax.swing.JLabel();
        Amount1 = new javax.swing.JLabel();
        online = new javax.swing.JRadioButton();
        cash = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        ok = new javax.swing.JButton();
        cal = new javax.swing.JButton();
        newBill = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Food");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jLabel48.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("<html><center>Discount<br>(%)");

        jLabel2.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel2.setText("Table No");

        tableno.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        tableno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Select-" }));

        jLabel46.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel46.setText("Food Name");

        jLabel3.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel3.setText("Customer Name");

        jLabel44.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel44.setText("Category");

        cname.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        Quantity.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        Quantity.setText("Quantity");

        jLabel4.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel4.setText("Ph No.");

        phno.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel5.setText("Total Bill");

        tBill.setEditable(false);
        tBill.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        tBill.setEnabled(false);

        jLabel49.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("<html><center>Total<br>Price(Rs)");

        jLabel50.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("<html><center>VAT");

        Amount.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        Amount.setText("Amount");

        Amount1.setFont(new java.awt.Font("Calibri", 1, 18)); // NOI18N
        Amount1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Amount1.setText("<html><center>Price<br>(RS)");

        online.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        online.setText("Online");

        cash.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        cash.setText("Cash");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75)
                                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(Quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(22, 22, 22)
                                .addComponent(Amount1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(Amount, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cname, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(phno, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(43, 43, 43)
                                        .addComponent(tableno, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tBill, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(70, 70, 70)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cash)
                                    .addComponent(online))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tableno, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBill, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cash, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phno, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(online, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Amount, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Amount1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2))
                    .addComponent(Quantity, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(1079, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ok, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(newBill, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addComponent(ok, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cal, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(newBill, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1865, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1210, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okActionPerformed
        okClicked();
        // TODO add your handling code here:
    }//GEN-LAST:event_okActionPerformed

    private void calActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calActionPerformed
        calculate();
        // TODO add your handling code here:
    }//GEN-LAST:event_calActionPerformed

    private void newBillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBillActionPerformed
        new Food().setVisible(true);
        this.dispose();
        // TODO add your handling code here:
    }//GEN-LAST:event_newBillActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Amount;
    private javax.swing.JLabel Amount1;
    private javax.swing.JLabel Quantity;
    private javax.swing.ButtonGroup btn;
    private javax.swing.JButton cal;
    private javax.swing.JRadioButton cash;
    private javax.swing.JTextField cname;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JButton newBill;
    private javax.swing.JButton ok;
    private javax.swing.JRadioButton online;
    private javax.swing.JTextField phno;
    private javax.swing.JTextField tBill;
    private javax.swing.JComboBox<String> tableno;
    // End of variables declaration//GEN-END:variables
}
