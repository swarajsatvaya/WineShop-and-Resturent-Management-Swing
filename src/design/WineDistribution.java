/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package design;

import bean.Db_connection;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Arka
 */
public class WineDistribution extends javax.swing.JPanel {

    /**
     * Creates new form MainStockEntry
     */
    //private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    //String registration_date= (df.format(new Date()));
   // String dt1= ((JTextField)jDateChooser1.getDateEditor().getUiComponent()).getText();
    public WineDistribution() {
        initComponents();
        dt.setDate(new Date());
        //this.setFocusTraversalKeysEnabled(false);
        btn.add(b1);
        btn.add(b2);
               
    }
    
    public void FillCat()
    {
        try 
        {
            Connection con = Db_connection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT distinct(cat) FROM ddesc a inner join mainstock b on a.dcode = b.dcode and a.dcode " +
                                                        "in (select dcode from mainstock where slno in (select max(slno) from mainstock " +
                                                        "where instock>0 and shopt = ? group by dcode))");
            ps.setInt(1, type);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                cat.addItem(rs.getString(1).trim());
            }
            rs.close();
            ps.close();
            con.close();
        } 
        catch (SQLException ex) 
        {
            Logger.getLogger(AddItemOnShop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clear()
    {
        barcode.setText("");
        barcode.setEditable(true);
        bpcNo.setText("00");
        //barcode.requestFocus();
        cat.setEnabled(true);
        cat.setSelectedItem("-Select-");
        brname.removeAllItems();
        brname.addItem("-Select-");
        brname.setEnabled(false);
        measureml.removeAllItems();
        measureml.addItem("-Select-");
        noCase.setEditable(false);
        noCase.setText("");
        noBot.setEditable(false);
        noBot.setText("");
        barcode.requestFocus(true);
        pbprice.setText("00.00");
        caseStock.setText("0.00");
        botStock.setText("00");
        mlStock.setText("00");
        jButton1.setEnabled(false);
    }
    public static String formatDate (String givenDate, String givenFormat, String changeableFormat) throws ParseException 
    {

        Date initDate = new SimpleDateFormat(givenFormat).parse(givenDate);
        SimpleDateFormat formatter = new SimpleDateFormat(changeableFormat);
        String changedDate = formatter.format(initDate);

        return changedDate;
    }
    
    public void Submit()
    {
      
      try
        { 
            String tdate= formatDate(((JTextField)dt.getDateEditor().getUiComponent()).getText(),"dd-MM-yyyy","yyyy-MM-dd");
            
            Connection con=Db_connection.getConnection();
            /*int mainstock = 0;
            PreparedStatement recvm= con.prepareStatement("SELECT instock FROM mainstock where dcode = ? and shopt = ? and slno in(select max(slno) " +
                                                        "from mainstock group by dcode)");
            recvm.setString(1, barcode.getText().trim());
            recvm.setInt(2, type);
            ResultSet recvmrs = recvm.executeQuery();
            if(recvmrs.next())
            {
                mainstock = recvmrs.getInt(1);
            }
            else
                mainstock = 0;
            recvmrs.close();
            recvm.close();*/
            if(type == 1)
            {
                Long onstock = (long)0;
                Double price30ml = 0.0;
                PreparedStatement recvon= con.prepareStatement("SELECT mlinstock, price30ml FROM onshopstock where dcode = ? "
                                                             + "and slno in(select max(slno) from onshopstock group by dcode)");
                recvon.setString(1, barcode.getText().trim());
                ResultSet recvonrs = recvon.executeQuery();
                if(recvonrs.next())
                {
                    onstock = recvonrs.getLong("mlinstock");
                    price30ml = recvonrs.getDouble("price30ml");
                }
                recvonrs.close();
                recvon.close();
                
                PreparedStatement onPs = con.prepareStatement("insert into onshopstock(date, dcode, qtyml, mlinstock, price30ml, ttype, billno)values(?,?,?,?,?,?,?)");
                onPs.setString(1, tdate);
                onPs.setString(2, barcode.getText().trim());
                onPs.setLong(3, Integer.parseInt(noBot.getText()) * Integer.parseInt(measureml.getSelectedItem().toString().trim()));
                onPs.setLong(4, onstock + Integer.parseInt(noBot.getText()) * Integer.parseInt(measureml.getSelectedItem().toString().trim()));
                onPs.setDouble(5, price30ml);
                onPs.setInt(6, 1);
                onPs.setInt(7, -1);
                int onRs = onPs.executeUpdate();
                onPs.close();
                
                PreparedStatement mPs= con.prepareStatement(" insert into mainstock(dcode,date,ttype,qty,instock,pricepb,tpassno,shopt)values(?,?,?,?,?,?,?,?)");
                mPs.setString(1, barcode.getText().trim());
                mPs.setString(2, tdate);
                mPs.setInt(3, 0);
                mPs.setInt(4, Integer.parseInt(noBot.getText().trim()));
                mPs.setInt(5, Integer.parseInt(botStock.getText().trim()) - Integer.parseInt(noBot.getText().trim()));
                mPs.setDouble(6, Double.parseDouble(pbprice.getText().trim()));
                mPs.setString(7, "-1@");
                mPs.setInt(8, type);
                int mRs = mPs.executeUpdate();
                mPs.close();
                
                if(onRs == 1 && mRs == 1)
                {
                    JOptionPane.showMessageDialog(null, "<html><h3>Item Succssfully Send To On Shop Stock</html>");
                    clear();
                }
                else if(onRs == 1 && mRs == 0)
                {
                    mPs = con.prepareStatement("DELETE FROM onshopstock WHERE slno in (select max(slno) from onshopstock)");
                    int err = mPs.executeUpdate();
                    if(err != 1)
                        JOptionPane.showMessageDialog(null, "<html><h3>A Critical Data Error Occoured<br>Contact Database Administrator</html>");
                }
                else
                    JOptionPane.showMessageDialog(null, "<html><h3><center>Unable To Complete Last Transfer<br>Please Try Again</html>");
            }
            
            if(type == 0)
            {
                int offstock = 0;
                //Double pricepb = 0.0;
                PreparedStatement recvoff= con.prepareStatement("SELECT instock, pricepb FROM offshopstock where dcode = ? "
                                                             + "and slno in(select max(slno) from offshopstock group by dcode)");
                recvoff.setString(1, barcode.getText().trim());
                ResultSet recvoffrs = recvoff.executeQuery();
                if(recvoffrs.next())
                {
                    offstock = recvoffrs.getInt("instock");
                    //pricepb = recvoffrs.getDouble("pricepb");
                }
                recvoffrs.close();
                recvoff.close();
                
                PreparedStatement offPs = con.prepareStatement("insert into offshopstock(date, dcode, qty, instock, pricepb, ttype, billno)values(?,?,?,?,?,?,?)");
                offPs.setString(1, tdate);
                offPs.setString(2, barcode.getText().trim());
                offPs.setInt(3, Integer.parseInt(noBot.getText()));
                offPs.setLong(4, offstock + Integer.parseInt(noBot.getText()));
                offPs.setDouble(5, Double.parseDouble(pbprice.getText().trim()));
                offPs.setInt(6, 1);
                offPs.setInt(7, -1);
                int onRs = offPs.executeUpdate();
                offPs.close();
                
                PreparedStatement mPs= con.prepareStatement(" insert into mainstock(dcode,date,ttype,qty,instock,pricepb,tpassno,shopt)values(?,?,?,?,?,?,?,?)");
                mPs.setString(1, barcode.getText().trim());
                mPs.setString(2, tdate);
                mPs.setInt(3, 0);
                mPs.setInt(4, Integer.parseInt(noBot.getText().trim()));
                mPs.setInt(5, Integer.parseInt(botStock.getText().trim()) - Integer.parseInt(noBot.getText().trim()));
                mPs.setDouble(6, Double.parseDouble(pbprice.getText().trim()));
                mPs.setString(7, "-1@");
                mPs.setInt(8, type);
                int mRs = mPs.executeUpdate();
                mPs.close();
                
                if(onRs == 1 && mRs == 1)
                {
                    JOptionPane.showMessageDialog(null, "<html><h3>Item Succssfully Send To Off Shop Stock</html>");
                    clear();
                }
                else if(onRs == 1 && mRs == 0)
                {
                    mPs = con.prepareStatement("DELETE FROM offshopstock WHERE slno in (select max(slno) from offshopstock)");
                    int err = mPs.executeUpdate();
                    if(err != 1)
                        JOptionPane.showMessageDialog(null, "<html><h3>A Critical Data Error Occoured<br>Contact Database Administrator</html>");
                }
                else
                    JOptionPane.showMessageDialog(null, "<html><h3><center>Unable To Complete Last Transfer<br>Please Try Again</html>");
            
            }
            
            con.close();
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "<html>Data Can Not Be Inserted<br>Some Error Occoured<br>Please Try Again</html>");
            e.printStackTrace();
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        brname = new javax.swing.JComboBox<>();
        dt = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        cat = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        noCase = new javax.swing.JTextField();
        noBot = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        b2 = new javax.swing.JRadioButton();
        b1 = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        measureml = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        barcode = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        bpcNo = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        pbprice = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        botStock = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        caseStock = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        mlStock = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Main Stock To Off Shop / On Shop Distribution");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setText("Date");

        brname.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        brname.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Select-" }));
        brname.setEnabled(false);
        brname.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                brnameItemStateChanged(evt);
            }
        });
        brname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                brnameKeyPressed(evt);
            }
        });

        dt.setDateFormatString("dd-MM-yyyy");
        dt.setEnabled(false);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText("Category");

        cat.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        cat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Select-" }));
        cat.setEnabled(false);
        cat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                catItemStateChanged(evt);
            }
        });
        cat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                catMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                catMouseReleased(evt);
            }
        });
        cat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                catActionPerformed(evt);
            }
        });
        cat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                catKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setText("Brand Name");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setText("No Of Case");

        noCase.setEditable(false);
        noCase.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        noCase.setText("00");
        noCase.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                noCaseKeyPressed(evt);
            }
        });

        noBot.setEditable(false);
        noBot.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        noBot.setText("00");
        noBot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noBotActionPerformed(evt);
            }
        });
        noBot.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                noBotKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                noBotKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel9.setText("No Of Bottles");

        b2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        b2.setText("Off Shop");
        b2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                b2ItemStateChanged(evt);
            }
        });
        b2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                b2MouseClicked(evt);
            }
        });
        b2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                b2KeyPressed(evt);
            }
        });

        b1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        b1.setText("On Shop");
        b1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                b1ItemStateChanged(evt);
            }
        });
        b1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                b1MouseClicked(evt);
            }
        });
        b1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                b1KeyPressed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jButton1.setText("Submit");
        jButton1.setEnabled(false);
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel10.setText("Measure (ml)");

        measureml.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        measureml.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Select-" }));
        measureml.setEnabled(false);
        measureml.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                measuremlItemStateChanged(evt);
            }
        });
        measureml.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                measuremlKeyPressed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel11.setText("Bar Code");

        barcode.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        barcode.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                barcodeMouseClicked(evt);
            }
        });
        barcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barcodeActionPerformed(evt);
            }
        });
        barcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                barcodeKeyPressed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jButton2.setText("Clear");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        bpcNo.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        bpcNo.setText("00");

        jLabel12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cross-sign.png"))); // NOI18N

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel13.setText("Price / Bottles");

        pbprice.setEditable(false);
        pbprice.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        pbprice.setText("00.00");
        pbprice.setEnabled(false);
        pbprice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pbpriceActionPerformed(evt);
            }
        });
        pbprice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pbpriceKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pbpriceKeyReleased(evt);
            }
        });

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cross-sign.png"))); // NOI18N

        botStock.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        botStock.setText("00");

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/package.png"))); // NOI18N

        caseStock.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        caseStock.setText("0.00");

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/glass.png"))); // NOI18N

        mlStock.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        mlStock.setText("00");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(barcode))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(35, 35, 35)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(measureml, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(brname, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cat, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(161, 161, 161)
                        .addComponent(b1)
                        .addGap(73, 73, 73)
                        .addComponent(b2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 249, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dt, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 158, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(caseStock, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(botStock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(mlStock, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(noCase)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel12)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(bpcNo, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(noBot)
                                            .addComponent(pbprice, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(21, 21, 21))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {barcode, brname, cat, measureml});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(b1)
                            .addComponent(b2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(barcode, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(brname, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bpcNo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(noCase, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(noBot, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pbprice, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(measureml, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(caseStock, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botStock, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mlStock, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(120, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {b1, b2, barcode, brname, cat, dt, jLabel10, jLabel11, jLabel13, jLabel4, jLabel5, jLabel6, jLabel7, jLabel9, measureml, noBot, noCase, pbprice});

    }// </editor-fold>//GEN-END:initComponents

    private void brnameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_brnameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_brnameKeyPressed

    private void catKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_catKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_catKeyPressed

    private void measuremlKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_measuremlKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_measuremlKeyPressed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        //Submit();
    }//GEN-LAST:event_jButton1MouseClicked
    
    private void noBotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noBotActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noBotActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Submit();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dt.setEnabled(false);
        dt.setCalendar(null);
        barcode.setEditable(false);
        barcode.setText("");
        cat.setEnabled(false);
        cat.setSelectedItem("-Select-");
        brname.setEnabled(false);
        brname.removeAllItems();
        brname.addItem("-Select-");
        measureml.setEnabled(false);
        noCase.setEditable(false);
        noCase.setText("00");
        noBot.setEditable(false);
        noBot.setText("00");
        bpcNo.setText("00");
        pbprice.setText("00.00");
        caseStock.setText("0.00");
        botStock.setText("00");
        mlStock.setText("00");
        jButton1.setEnabled(false);

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed
    //int bpc = 0;
    private void barcodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barcodeKeyPressed
        if(!barcode.getText().trim().equals("") && barcode.isEditable() && (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB))
        {
            try 
            {
                Connection con = Db_connection.getConnection();
                PreparedStatement ps = con.prepareStatement("SELECT * FROM ddesc a inner join mainstock b on a.dcode = b.dcode and " +
                                                            "b.slno in (select max(slno) from mainstock where dcode = ? and instock>0 " +
                                                            "and shopt = ? group by dcode)");
                ps.setString(1, barcode.getText().trim());
                ps.setInt(2, type);
                ResultSet rs = ps.executeQuery();
                if(rs.next())
                {
                    cat.setSelectedItem(rs.getString("cat"));
                    cat.setEnabled(false);
                    //brname.removeAllItems();
                    //brname.addItem(rs.getString("bname"));
                    brname.setSelectedItem(rs.getString("bname"));
                    brname.setEnabled(false);
                    //measureml.removeAllItems();
                    //measureml.addItem(rs.getString("mlpb"));
                    measureml.setSelectedItem(rs.getString("mlpb"));
                    measureml.setEnabled(false);
                    bpcNo.setText(String.valueOf(rs.getInt("bpc")));
                    pbprice.setText(String.valueOf(rs.getDouble("pricepb")));
                    caseStock.setText(String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(rs.getInt("instock")))/rs.getInt("bpc"))));
                    botStock.setText(String.valueOf(rs.getInt("instock")));
                    mlStock.setText(String.valueOf(rs.getInt("instock")*rs.getInt("mlpb")));
                    noCase.setEditable(true);
                    noBot.setEditable(true);
                    barcode.setEditable(false);
                    noCase.requestFocus();
                }
                else
                {
                    cat.setSelectedItem("-Select-");
                    brname.setEnabled(false);
                    brname.setSelectedItem("-Select-");
                    measureml.setEnabled(false);
                    measureml.setSelectedItem("-Select-");
                    noCase.setEditable(false);
                    noCase.setText("");
                    noBot.setEditable(false);
                    noBot.setText("");
                    pbprice.setText("00.00");
                    caseStock.setText("0.00");
                    botStock.setText("00");
                    mlStock.setText("00");
                    
                    JOptionPane.showMessageDialog(null, "<html><h3>Bar Code Not Found<br>Please Select By Category</html>");
                    bpcNo.setText("00");
                    cat.requestFocus();
                    barcode.setText("");
                }
                rs.close();
                ps.close();
                con.close();
            }
            catch (SQLException ex) 
            {
                Logger.getLogger(MainStockEntry.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_barcodeKeyPressed

    private void catMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_catMouseClicked
        clear();
        
        // TODO add your handling code here:
    }//GEN-LAST:event_catMouseClicked

    private void catActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_catActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_catActionPerformed

    private void catItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_catItemStateChanged
        if (evt.getStateChange() == evt.SELECTED) 
        {
            String item = evt.getItem().toString().trim();
            if(!item.equals("-Select-"))
            {
                brname.removeAllItems();
                brname.addItem("-Select-");
                try 
                {
                    Connection con = Db_connection.getConnection();
                    PreparedStatement ps = con.prepareStatement("SELECT distinct(bname) FROM ddesc a inner join mainstock b on a.dcode = b.dcode and a.dcode " +
                                                                "in (select dcode from mainstock where slno in(select max(slno) from mainstock where instock>0 " + 
                                                                "and shopt = ? group by dcode)) and cat = ?");
                    ps.setInt(1, type);
                    ps.setString(2, item);
                    ResultSet rs = ps.executeQuery();
                    while(rs.next())
                    {
                        brname.addItem(rs.getString(1).trim());
                    }
                    brname.setEnabled(true);
                    brname.requestFocus();
                    measureml.setSelectedItem("-Select-");
                    rs.close();
                    ps.close();
                    con.close();
                } 
                catch (SQLException ex) 
                {
                    Logger.getLogger(AddItemOnShop.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
       }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_catItemStateChanged

    private void brnameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_brnameItemStateChanged
    if (evt.getStateChange() == evt.SELECTED) 
        {
            String item1 = cat.getSelectedItem().toString().trim();
            String item2 = evt.getItem().toString().trim();
            
            if(!item2.equals("-Select-"))
            {
                measureml.removeAllItems();
                measureml.addItem("-Select-");
                try 
                {
                    Connection con = Db_connection.getConnection();
                    PreparedStatement ps = con.prepareStatement("SELECT distinct(mlpb) FROM ddesc a inner join mainstock b on a.dcode = b.dcode and a.dcode " +
                                                                "in (select dcode from mainstock where slno in(select max(slno) from mainstock where instock>0 " + 
                                                                " and shopt = ? group by dcode)) and cat = ? and bname = ?");
                    ps.setInt(1, type);
                    ps.setString(2, item1);
                    ps.setString(3, item2);
                    ResultSet rs = ps.executeQuery();
                    while(rs.next())
                    {
                        measureml.addItem(rs.getString(1).trim());
                    }
                    measureml.setEnabled(true);
                    measureml.requestFocus();
                    rs.close();
                    ps.close();
                    con.close();
                } 
                catch (SQLException ex) 
                {
                    Logger.getLogger(AddItemOnShop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                measureml.setSelectedItem("-Select-");
                measureml.setEnabled(false);
                noCase.setEditable(false);
                noBot.setEditable(false);
            }
        }  
    // TODO add your handling code here:
    }//GEN-LAST:event_brnameItemStateChanged

    private void measuremlItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_measuremlItemStateChanged
        if (evt.getStateChange() == evt.SELECTED) 
        {
            String item1 = cat.getSelectedItem().toString().trim();
            String item2 = brname.getSelectedItem().toString().trim();
            String item3 = evt.getItem().toString().trim();
            
            if(!item3.equals("-Select-"))
            {
                try 
                {
                    Connection con = Db_connection.getConnection();
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM ddesc a inner join mainstock b on a.dcode = b.dcode and b.slno " +
                                                                "in (select max(slno) from mainstock where instock>0 and shopt = ? group by dcode) " +
                                                                "and cat = ? and bname = ? and mlpb = ?");
                    ps.setInt(1, type);
                    ps.setString(2, item1);
                    ps.setString(3, item2);
                    ps.setInt(4, Integer.parseInt(item3));
                    ResultSet rs = ps.executeQuery();
                    if(rs.next())
                    {
                        barcode.setText(rs.getString("dcode"));
                        bpcNo.setText(String.valueOf(rs.getInt("bpc")));
                        pbprice.setText(String.valueOf(rs.getDouble("pricepb")));
                        caseStock.setText(String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(String.valueOf(rs.getInt("instock")))/rs.getInt("bpc"))));
                        botStock.setText(String.valueOf(rs.getInt("instock")));
                        mlStock.setText(String.valueOf(rs.getInt("instock")*rs.getInt("mlpb")));
                        noCase.setEditable(true);
                        noCase.requestFocus();
                        noBot.setEditable(true);
                    }
                    else
                    {
                        cat.setSelectedItem("-Select-");
                        brname.setEnabled(false);
                        brname.setSelectedItem("-Select-");
                        measureml.setEnabled(false);
                        measureml.setSelectedItem("-Select-");
                        caseStock.setText("0.00");
                        botStock.setText("00");
                        mlStock.setText("00");
                        noCase.setEditable(false);
                        noCase.setText("");
                        noBot.setEditable(false);
                        noBot.setText("");
                        pbprice.setText("00.00");

                        

                        JOptionPane.showMessageDialog(null, "<html><h3>Item Not Found<br>Try Again</html>");
                        bpcNo.setText("00");
                        barcode.requestFocus();
                        barcode.setText("");
                    }
                    rs.close();
                    ps.close();
                    con.close();
                } 
                catch (SQLException ex) 
                {
                    Logger.getLogger(AddItemOnShop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                noCase.setEditable(false);
                noBot.setEditable(false);
            }
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_measuremlItemStateChanged

    private void noCaseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_noCaseKeyPressed
        if(!noCase.getText().trim().equals("") && (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB))
        {
            noBot.setText(String.valueOf(new DecimalFormat("##").format(Double.parseDouble(noCase.getText().trim())*Integer.parseInt(bpcNo.getText().trim()))));
            noBot.requestFocus();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_noCaseKeyPressed

    private void noBotKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_noBotKeyReleased
        if(!noBot.getText().trim().equals("") && (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB))
        {
            noCase.setText(String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(noBot.getText().trim())/Integer.parseInt(bpcNo.getText().trim()))));
            if(Integer.parseInt(noBot.getText()) <= Integer.parseInt(botStock.getText()))
            {
                jButton1.setEnabled(true);
                jButton1.requestFocus();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "<html><h3>No of Bottle Not In Stock</html>");
                noCase.setText("00");
                noBot.setText("");
                noBot.requestFocus();
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_noBotKeyReleased

    private void noBotKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_noBotKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_noBotKeyPressed

    private void b1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_b1KeyPressed
        if((evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB))
        {
            b2.requestFocus();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_b1KeyPressed

    private void b2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_b2KeyPressed
        if((evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB))
        {
            barcode.requestFocus();
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_b2KeyPressed
    
    int type;
    private void b1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_b1ItemStateChanged
        if(b1.isSelected())
        {
            cat.removeAllItems();
            cat.addItem("-Select-");
            type = 1;
            FillCat();
            dt.setEnabled(true);
            barcode.setEditable(true);
            cat.setEnabled(true);
            dt.setDate(new Date());
            //brname.setEnabled(true);
            //measureml.setEnabled(true);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_b1ItemStateChanged

    private void b2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_b2ItemStateChanged
        if(b2.isSelected())
        {
            cat.removeAllItems();
            cat.addItem("-Select-");
            type = 0;
            FillCat();
            dt.setEnabled(true);
            barcode.setEditable(true);
            cat.setEnabled(true);
            dt.setDate(new Date());
            //brname.setEnabled(true);
            //measureml.setEnabled(true);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_b2ItemStateChanged

    private void pbpriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pbpriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pbpriceActionPerformed

    private void pbpriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pbpriceKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_pbpriceKeyPressed

    private void pbpriceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pbpriceKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_pbpriceKeyReleased

    private void catMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_catMouseReleased

        // TODO add your handling code here:
    }//GEN-LAST:event_catMouseReleased

    private void barcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_barcodeActionPerformed

    private void barcodeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_barcodeMouseClicked
        //clear();
        // TODO add your handling code here:
    }//GEN-LAST:event_barcodeMouseClicked

    private void b1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b1MouseClicked
        clear();
        // TODO add your handling code here:
    }//GEN-LAST:event_b1MouseClicked

    private void b2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b2MouseClicked
        clear();
        // TODO add your handling code here:
    }//GEN-LAST:event_b2MouseClicked

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
        if((evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB))
        {
            Submit();
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1KeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JRadioButton b1;
    private javax.swing.JRadioButton b2;
    private javax.swing.JTextField barcode;
    private javax.swing.JLabel botStock;
    private javax.swing.JLabel bpcNo;
    private javax.swing.JComboBox<String> brname;
    private javax.swing.ButtonGroup btn;
    private javax.swing.JLabel caseStock;
    private javax.swing.JComboBox<String> cat;
    private com.toedter.calendar.JDateChooser dt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox<String> measureml;
    private javax.swing.JLabel mlStock;
    private javax.swing.JTextField noBot;
    private javax.swing.JTextField noCase;
    private javax.swing.JTextField pbprice;
    // End of variables declaration//GEN-END:variables
}
