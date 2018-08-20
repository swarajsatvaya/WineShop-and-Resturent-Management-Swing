/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package design;

import bean.Db_connection;
import bean.ObjDao;
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
public class MainStockEntry extends javax.swing.JPanel {

    /**
     * Creates new form MainStockEntry
     */
    //private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    //String registration_date= (df.format(new Date()));
   // String dt1= ((JTextField)jDateChooser1.getDateEditor().getUiComponent()).getText();
    public MainStockEntry() {
        initComponents();
        this.setFocusTraversalKeysEnabled(false);
        btn.add(b1);
        btn.add(b2);
        if(ObjDao.tpassno != null)
        {
            tpass.setText(ObjDao.tpassno);
            tpass.setEditable(false);
        }
        this.FillCat();
        
    }
    
    public void FillCat()
    {
        try 
        {
            Connection con = Db_connection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT distinct(cat) FROM ddesc");
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
            int type=0;
            if(!b1.isSelected() && !b2.isSelected())
            {
                JOptionPane.showMessageDialog(null, "<html><h3>Please Select On Shop Or Off Shop Option</html>");
                return;
            }
            if(b1.isSelected())
            {
                type=1;
            }
            else
            {
                type=0;
            }

            Connection con=Db_connection.getConnection();
            int instock = 0;
            PreparedStatement recv= con.prepareStatement("SELECT instock FROM mainstock where slno in(select max(slno) " +
                                                         "from mainstock where dcode = ? and shopt = ? group by dcode)");
            recv.setString(1, barcode.getText().trim());
            recv.setInt(2, type);
            ResultSet recvrs = recv.executeQuery();
            if(recvrs.next())
            {
                instock = recvrs.getInt(1);
            }
            else
                instock = 0;
            recvrs.close();
            recv.close();
            
            PreparedStatement ps= con.prepareStatement("insert into mainstock (dcode, date, ttype, qty, instock, pricepb, tpassno, shopt) values (?,?,?,?,?,?,?,?)");
            ps.setString(1, barcode.getText().trim());
            ps.setString(2, tdate);
            ps.setInt(3,1);
            ps.setInt(4, Integer.parseInt(tbot.getText().trim()));
            ps.setInt(5, instock + Integer.parseInt(tbot.getText().trim()));
            ps.setDouble(6, Double.parseDouble(tprice.getText().trim()) / Integer.parseInt(tbot.getText().trim()));
            ps.setString(7, tpass.getText().trim());
            ps.setInt(8, type);

            int m = ps.executeUpdate();
            if(m > 0)
            {
                tpass.setText(ObjDao.tpassno);
                tpass.requestFocus();
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
                tCas.setEditable(false);
                tCas.setText("00");
                tbot.setEditable(false);
                tbot.setText("00");
                tprice.setEditable(false);
                tprice.setText("0.00");
                bpcNo.setText("00");
                pbprice.setText("0.00");
                b1.setSelected(false);
                b1.setEnabled(false);
                b2.setSelected(false);
                b2.setEnabled(false);
                jButton1.setEnabled(false);
                //To Do
            }
            else
                JOptionPane.showMessageDialog(null, "<html>Data Can Not Be Inserted<br>Some Error Occoured<br>Please Try Again</html>");
            ps.close();
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
        jLabel3 = new javax.swing.JLabel();
        tpass = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        brname = new javax.swing.JComboBox<>();
        dt = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        cat = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        tCas = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        tbot = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tprice = new javax.swing.JTextField();
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
        pbprice = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Main Stock Entry");

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

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Transport Pass No");

        tpass.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        tpass.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tpassMouseClicked(evt);
            }
        });
        tpass.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tpassKeyPressed(evt);
            }
        });

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
        jLabel4.setText("Total Case");

        tCas.setEditable(false);
        tCas.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        tCas.setText("00");
        tCas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tCasKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setText("Total Price");

        tbot.setEditable(false);
        tbot.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        tbot.setText("00");
        tbot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tbotActionPerformed(evt);
            }
        });
        tbot.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbotKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbotKeyReleased(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel9.setText("Total Bottles");

        tprice.setEditable(false);
        tprice.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        tprice.setText("0.00");
        tprice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tpriceKeyPressed(evt);
            }
        });

        b2.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        b2.setText("Off Shop");
        b2.setEnabled(false);
        b2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                b2KeyPressed(evt);
            }
        });

        b1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        b1.setText("On Shop");
        b1.setEnabled(false);
        b1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                b1KeyPressed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jButton1.setText("Add");
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

        barcode.setEditable(false);
        barcode.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
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

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/cross-sign.png"))); // NOI18N

        pbprice.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        pbprice.setText("0.00");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cat, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(barcode))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(tpass))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(measureml, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(brname, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(80, 80, 80)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(b1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(b2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(dt, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(tCas)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel12)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(bpcNo, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(tprice, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel13)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(pbprice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addComponent(tbot, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(80, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {barcode, brname, cat, measureml, tpass});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dt, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tpass, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
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
                            .addComponent(brname, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(measureml, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bpcNo, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tCas, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tbot, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tprice, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pbprice, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(b2)
                            .addComponent(b1))))
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(149, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {b1, b2, barcode, brname, cat, dt, jLabel10, jLabel11, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jLabel9, measureml, tCas, tbot, tpass, tprice});

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
    
    private void tpassKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tpassKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB)
        {
            try 
            {
                Connection con = Db_connection.getConnection();
                PreparedStatement ps = con.prepareStatement("select date from dealer where tpassno = ?");
                ps.setString(1, tpass.getText().trim());
                ResultSet rs = ps.executeQuery();
                if(rs.next())
                {
                    dt.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("date")));
                    tpass.setEditable(false);
                    ObjDao.tpassno = tpass.getText().trim();
                    barcode.setEditable(true);
                    cat.setEnabled(true);
                    //brname.setEnabled(true);
                    //measureml.setEnabled(true);
                    //tCas.setEditable(true);
                    //tbot.setEditable(true);
                    //tprice.setEditable(true);
                    //b1.setEnabled(true);
                    //b2.setEnabled(true);
                    jButton1.setEnabled(true);
                    barcode.requestFocus();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "<html><h3>Transport Number Does Not Found</html>");
                    tpass.setText("");
                    ObjDao.tpassno = null;
                    jButton1.setEnabled(false);
                }
                rs.close();
                ps.close();
                con.close();
            }
            catch (SQLException | ParseException ex) 
            {
                Logger.getLogger(MainStockEntry.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_tpassKeyPressed

    private void tpassMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tpassMouseClicked
        tpass.setEditable(true);
        // TODO add your handling code here:
    }//GEN-LAST:event_tpassMouseClicked

    private void tbotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tbotActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbotActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        Submit();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        ObjDao.tpassno = null;
        tpass.setEditable(true);
        tpass.setText("");
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
        tCas.setEditable(false);
        tCas.setText("00");
        tbot.setEditable(false);
        tbot.setText("00");
        tprice.setEditable(false);
        tprice.setText("0.00");
        bpcNo.setText("00");
        pbprice.setText("0.00");
        b1.setEnabled(false);
        b2.setEnabled(false);
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
                PreparedStatement ps = con.prepareStatement("select * from ddesc where dcode = ?");
                ps.setString(1, barcode.getText().trim());
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
                    tCas.setEditable(true);
                    tbot.setEditable(true);
                    tprice.setEditable(true);
                    b1.setEnabled(true);
                    b2.setEnabled(true);
                    barcode.setEditable(false);
                    tCas.requestFocus();
                }
                else
                {
                    cat.setSelectedItem("-Select-");
                    brname.setEnabled(false);
                    brname.setSelectedItem("-Select-");
                    measureml.setEnabled(false);
                    measureml.setSelectedItem("-Select-");
                    tCas.setEditable(false);
                    tCas.setText("");
                    tbot.setEditable(false);
                    tbot.setText("");
                    tprice.setEditable(false);
                    tprice.setText("");
                    b1.setEnabled(false);
                    b2.setEnabled(false);
                    
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
        tCas.setEditable(false);
        tCas.setText("");
        tbot.setEditable(false);
        tbot.setText("");
        tprice.setEditable(false);
        tprice.setText("");
        barcode.requestFocus(true);
        
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
                    PreparedStatement ps = con.prepareStatement("SELECT distinct(bname) FROM ddesc where cat = ?");
                    ps.setString(1, item);
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
                    PreparedStatement ps = con.prepareStatement("SELECT distinct(mlpb) FROM ddesc where cat = ? and bname = ?");
                    ps.setString(1, item1);
                    ps.setString(2, item2);
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
                tCas.setEditable(false);
                tbot.setEditable(false);
                tprice.setEditable(false);
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
                    PreparedStatement ps = con.prepareStatement("SELECT dcode, bpc FROM ddesc where cat = ? and bname = ? and mlpb = ?");
                    ps.setString(1, item1);
                    ps.setString(2, item2);
                    ps.setInt(3, Integer.parseInt(item3));
                    ResultSet rs = ps.executeQuery();
                    if(rs.next())
                    {
                        barcode.setText(rs.getString("dcode"));
                        bpcNo.setText(String.valueOf(rs.getInt("bpc")));
                        tCas.setEditable(true);
                        tCas.requestFocus();
                        tbot.setEditable(true);
                        tprice.setEditable(true);
                        b1.setEnabled(true);
                        b2.setEnabled(true);
                    }
                    else
                    {
                        cat.setSelectedItem("-Select-");
                        brname.setEnabled(false);
                        brname.setSelectedItem("-Select-");
                        measureml.setEnabled(false);
                        measureml.setSelectedItem("-Select-");
                        tCas.setEditable(false);
                        tCas.setText("");
                        tbot.setEditable(false);
                        tbot.setText("");
                        tprice.setEditable(false);
                        tprice.setText("");
                        b1.setEnabled(false);
                        b2.setEnabled(false);

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
                tCas.setEditable(false);
                tbot.setEditable(false);
                tprice.setEditable(false);
                b1.setEnabled(false);
                b2.setEnabled(false);
            }
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_measuremlItemStateChanged

    private void tCasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tCasKeyPressed
        if(!tCas.getText().trim().equals("00") && (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB))
        {
            tbot.setText(String.valueOf(new DecimalFormat("##").format(Double.parseDouble(tCas.getText().trim())*Integer.parseInt(bpcNo.getText().trim()))));
            tbot.requestFocus();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tCasKeyPressed

    private void tbotKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbotKeyReleased
        if(!tbot.getText().trim().equals("00") && (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB))
        {
            tCas.setText(String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(tbot.getText().trim())/Integer.parseInt(bpcNo.getText().trim()))));
            tprice.requestFocus();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tbotKeyReleased

    private void tbotKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbotKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbotKeyPressed

    private void tpriceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tpriceKeyPressed
        if(!tprice.getText().trim().equals("0.00") && (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB))
        {
            pbprice.setText(String.valueOf(new DecimalFormat("##.##########").format(Double.parseDouble(tprice.getText().trim())/Integer.parseInt(tbot.getText().trim()))));
            b1.requestFocus();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tpriceKeyPressed

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
            jButton1.requestFocus();
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_b2KeyPressed

    private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
        if((evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_TAB))
        {
            Submit();
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1KeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton b1;
    private javax.swing.JRadioButton b2;
    private javax.swing.JTextField barcode;
    private javax.swing.JLabel bpcNo;
    private javax.swing.JComboBox<String> brname;
    private javax.swing.ButtonGroup btn;
    private javax.swing.JComboBox<String> cat;
    private com.toedter.calendar.JDateChooser dt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox<String> measureml;
    private javax.swing.JLabel pbprice;
    private javax.swing.JTextField tCas;
    private javax.swing.JTextField tbot;
    public javax.swing.JTextField tpass;
    private javax.swing.JTextField tprice;
    // End of variables declaration//GEN-END:variables
}
