/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package design;

import bean.Db_connection;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Arka
 */
public class NewDrinksCategory extends javax.swing.JPanel {

    /**
     * Creates new form NewDrinksCategory
     */
    public NewDrinksCategory() {
        initComponents();
        Getdata() ;
    }

    
     public void Getdata()   //for retriving value of user details
     {
         try
	 {
             Connection con=Db_connection.getConnection();
             PreparedStatement ps;               
             ps= con.prepareStatement("select * from ddesc");
             ResultSet rs = ps.executeQuery();
             int i=0;
              DefaultTableModel model = null;
	     model = (DefaultTableModel) DrinksCategory.getModel();
             while(rs.next())
             {
                 model.insertRow(i , new Object[]{rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(1)});
                    i++;
	     }
             rs.close();
	     ps.close();
	     
             ps = con.prepareStatement("select distinct(kofl) from ddesc");
             rs = ps.executeQuery();
             kofl.removeAllItems();
             kofl.addItem("-Select-");
             while (rs.next())
             {
                 kofl.addItem(rs.getString("kofl"));
             }
             rs.close();
             ps.close();
             
             ps = con.prepareStatement("select distinct(cat) from ddesc");
             rs = ps.executeQuery();
             cat.removeAllItems();
             cat.addItem("-Select-");
             while (rs.next())
             {
                 cat.addItem(rs.getString("cat"));
             }
             rs.close();
             ps.close();
             
             ps = con.prepareStatement("select distinct(bname) from ddesc");
             rs = ps.executeQuery();
             brname.removeAllItems();
             brname.addItem("-Select-");
             while (rs.next())
             {
                 brname.addItem(rs.getString("bname"));
             }
             rs.close();
             ps.close();
             
             ps = con.prepareStatement("select distinct(strng) from ddesc");
             rs = ps.executeQuery();
             str.removeAllItems();
             str.addItem("-Select-");
             while (rs.next())
             {
                 str.addItem(rs.getString("strng"));
             }
             rs.close();
             ps.close();
         }
         catch(Exception e)
         {
             e.printStackTrace();
         }
    
     }
    
    
     
     public void onSubmit()
     {
        if(!kofl.getSelectedItem().toString().trim().equals("-Select-") && !cat.getSelectedItem().toString().trim().equals("-Select-") && !brname.getSelectedItem().toString().trim().equals("-Select-") && !str.getSelectedItem().toString().trim().equals("-Select-") && !barcode.getText().trim().equals("") && !ml.getText().trim().equals("") && !bottle.getText().trim().equals(""))
	{
	    try 
	    {
		Connection con = Db_connection.getConnection();
		PreparedStatement ps = con.prepareStatement("insert into ddesc values(?,?,?,?,?,?,?)");
		ps.setString(1, barcode.getText().trim());
		ps.setString(2, kofl.getSelectedItem().toString().trim());
		ps.setString(3, cat.getSelectedItem().toString().trim());
		ps.setString(4, brname.getSelectedItem().toString().trim());
		ps.setString(5, str.getSelectedItem().toString().trim());
                ps.setInt(6, Integer.parseInt(ml.getText().trim()));
		ps.setInt(7,Integer.parseInt( bottle.getText().trim()));
		
		int r = ps.executeUpdate();
		if(r == 0)
		{
		    JOptionPane.showMessageDialog(null, "<html><center><h3>New Entry Can Not Be Inserted<br>Barcode Must Be Unique<br>Please Click Update For Fodification</html>");
		}
		else
		{
		    DefaultTableModel model = null;
		    model = (DefaultTableModel) DrinksCategory.getModel();
		    //System.out.println(model.getRowCount());
		    //model.createNewRow();
		    model.insertRow(model.getRowCount(), new Object[]{kofl.getSelectedItem().toString().trim(), cat.getSelectedItem().toString().trim(), brname.getSelectedItem().toString().trim(), str.getSelectedItem().toString().trim(),ml.getText().trim(),bottle.getText().trim(),barcode.getText().trim()});
		    DrinksCategory.getSelectionModel().clearSelection();
		    row = -1;
		    kofl.setSelectedItem("-Select-");
		    cat.setSelectedItem("-Select-");
		    brname.setSelectedItem("-Select-");
		    str.setSelectedItem("-Select-");
		    ml.setText("");
		    bottle.setText("");
		    barcode.setText("");
                    barcode.setEditable(true);
                     kofl.setEnabled(true);
                    cat.setEnabled(true);
                    brname.setEnabled(true);
                    
		   /* Password.setEditable(true);
		    jComboBox7.setSelectedItem(1);
		    jComboBox7.setEnabled(true);*/
		}
		ps.close();
		con.close();
	    } 
	    catch (SQLException | HeadlessException ex) 
	    {
		JOptionPane.showMessageDialog(null, "<html><center><h3>Barcode Can not be inserted<br>Bar Code must be unique<br>Please click update for modification</html>");
	    } 
	}
	else
        {
	    JOptionPane.showMessageDialog(null, "<html><h3>Fields can not be empty</html>");
        }
        Getdata();
     }
    
     
     public void onUpdate()
     {
         if (row == -1)
	{
	    JOptionPane.showMessageDialog(null, "<html><center><h3>Select a existing Row for upadate</html>");
	    return;
	}
         
        if(!kofl.getSelectedItem().toString().trim().equals("-Select-") && !cat.getSelectedItem().toString().trim().equals("-Select-") && !brname.getSelectedItem().toString().trim().equals("-Select-") && !str.getSelectedItem().toString().trim().equals("-Select-") && !barcode.getText().trim().equals("") && !ml.getText().trim().equals("") && !bottle.getText().trim().equals(""))
        {
            try 
	    {
		Connection con = Db_connection.getConnection();
		PreparedStatement ps = con.prepareStatement("update ddesc SET kofl=?, cat=? , bname=? ,strng=?, mlpb=?,bpc=? where dcode ='"+barcode.getText().trim()+"'"); 
                ps.setString(1, kofl.getSelectedItem().toString().trim());
		ps.setString(2, cat.getSelectedItem().toString().trim());
		ps.setString(3, brname.getSelectedItem().toString().trim());
		ps.setString(4, str.getSelectedItem().toString().trim());
                ps.setInt(5, Integer.parseInt(ml.getText().trim()));
		ps.setInt(6, Integer.parseInt(bottle.getText().trim()));
                int r=ps.executeUpdate();
                if(r == 0)
		{
		    JOptionPane.showMessageDialog(null, "<html><center><h3> Does not exist</html>");
		}
		else
		{
		    DefaultTableModel model = null;
		    model = (DefaultTableModel) DrinksCategory.getModel();
		    model.removeRow(row);
		    model.insertRow(row , new Object[]{kofl.getSelectedItem().toString().trim(), cat.getSelectedItem().toString().trim(), brname.getSelectedItem().toString().trim(), str.getSelectedItem().toString().trim(),ml.getText().trim(),bottle.getText().trim(),barcode.getText().trim()});
		    DrinksCategory.getSelectionModel().clearSelection();
		    row = -1;
                    kofl.setSelectedItem("-Select-");
		    cat.setSelectedItem("-Select-");
		    brname.setSelectedItem("-Select-");
		    str.setSelectedItem("-Select-");
		    ml.setText("");
		    bottle.setText("");
		    barcode.setText("");
                    
            }
                ps.close();
                con.close();
                        
                        
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
         {
             JOptionPane.showMessageDialog(null, "Fill Up The Empty Field");
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        barcode = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        ml = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        bottle = new javax.swing.JTextField();
        add = new javax.swing.JButton();
        update = new javax.swing.JButton();
        clear = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        DrinksCategory = new javax.swing.JTable();
        kofl = new javax.swing.JComboBox<>();
        cat = new javax.swing.JComboBox<>();
        brname = new javax.swing.JComboBox<>();
        str = new javax.swing.JComboBox<>();

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("New Drinks Category ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setText("Barcode");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setText("Category");

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setText("Brand Name");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setText("Strength");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel16.setText("<html><center>King Of Forigen<br> Liqure</html>");

        barcode.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel7.setText("<html><center>Bottles Per<br>Case</html>");

        ml.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel8.setText("<html><center>Measure In<br>ML</html>");

        bottle.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N

        add.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        add.setText("Submit");
        add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addMouseClicked(evt);
            }
        });
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        update.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        update.setText("Update");
        update.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                updateMouseClicked(evt);
            }
        });
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });

        clear.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        clear.setText("Clear");
        clear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearMouseClicked(evt);
            }
        });

        DrinksCategory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "King Of Forigen liqure", "Category", "Brand Name", "Strength", "measure in (ML)", "Bottole In Case", "Barcode"
            }
        ));
        DrinksCategory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DrinksCategoryMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(DrinksCategory);

        kofl.setEditable(true);
        kofl.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        kofl.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Select-" }));
        kofl.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                koflKeyPressed(evt);
            }
        });

        cat.setEditable(true);
        cat.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        cat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Select-" }));
        cat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                catKeyPressed(evt);
            }
        });

        brname.setEditable(true);
        brname.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        brname.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Select-" }));
        brname.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                brnameKeyPressed(evt);
            }
        });

        str.setEditable(true);
        str.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        str.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-Select-" }));
        str.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                strKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                        .addGap(231, 231, 231))
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(kofl, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cat, 0, 193, Short.MAX_VALUE)
                            .addComponent(brname, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(str, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(add, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(barcode)
                    .addComponent(ml)
                    .addComponent(bottle)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(update, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clear, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jScrollPane1)
                .addGap(11, 11, 11))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {brname, cat, kofl});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(kofl, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2)
                    .addComponent(barcode, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cat, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ml, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(brname, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(bottle, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(str, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(add, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(update)
                        .addComponent(clear)))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    int row=-1;
    private void addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseClicked
        //onSubmit();
        // TODO add your handling code here                              

    }//GEN-LAST:event_addMouseClicked

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        onSubmit();
        // TODO add your handling code here:
    }//GEN-LAST:event_addActionPerformed

    private void clearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearMouseClicked
        // TODO add your handling code here:
                    kofl.setSelectedItem("-Select-");
		    cat.setSelectedItem("-Select-");
		    brname.setSelectedItem("-Select-");
		    str.setSelectedItem("-Select-");
		    ml.setText("");
		    bottle.setText("");
		    barcode.setText("");
                    barcode.setEditable(true);
                   kofl.setEnabled(true);
                    cat.setEnabled(true);
                    brname.setEnabled(true);
    }//GEN-LAST:event_clearMouseClicked

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        // TODO add your handling code here:
        onUpdate();
    }//GEN-LAST:event_updateActionPerformed
//int row=-1;
    private void DrinksCategoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DrinksCategoryMouseClicked
        // TODO add your handling code here:
       row=DrinksCategory.getSelectedRow();
	if(row != -1)
	{
	    kofl.setSelectedItem(DrinksCategory.getModel().getValueAt(row,0).toString());
	    cat.setSelectedItem(DrinksCategory.getModel().getValueAt(row,1).toString());
	    brname.setSelectedItem(DrinksCategory.getModel().getValueAt(row,2).toString());
            str.setSelectedItem(DrinksCategory.getModel().getValueAt(row,3).toString());
           ml.setText(DrinksCategory.getModel().getValueAt(row,4).toString());
           bottle.setText(DrinksCategory.getModel().getValueAt(row,5).toString());
           barcode.setText(DrinksCategory.getModel().getValueAt(row,6).toString());
           barcode.setEditable(false);
            kofl.setEnabled(false);
            cat.setEnabled(false);
            brname.setEnabled(false);
                    
        }
    }//GEN-LAST:event_DrinksCategoryMouseClicked

    private void koflKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_koflKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_koflKeyPressed

    private void catKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_catKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_catKeyPressed

    private void brnameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_brnameKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_brnameKeyPressed

    private void updateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_updateMouseClicked
        //onUpdate();
        // TODO add your handling code here:
    }//GEN-LAST:event_updateMouseClicked

    private void strKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_strKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_strKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable DrinksCategory;
    private javax.swing.JButton add;
    private javax.swing.JTextField barcode;
    private javax.swing.JTextField bottle;
    private javax.swing.JComboBox<String> brname;
    private javax.swing.JComboBox<String> cat;
    private javax.swing.JButton clear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JComboBox<String> kofl;
    private javax.swing.JTextField ml;
    private javax.swing.JComboBox<String> str;
    private javax.swing.JButton update;
    // End of variables declaration//GEN-END:variables
}
