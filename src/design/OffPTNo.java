/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package design;

import bean.ObjDao;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Arka
 */
public class OffPTNo extends javax.swing.JFrame {

    /**
     * Creates new form TNO
     */
    public OffPTNo() {
        initComponents();
        //ObjDao.tForm = this;
        //setUndecorated(true);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tno = new javax.swing.JTextField();
        ok = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Transection No.");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        tno.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        tno.setToolTipText("");
        tno.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tnoFocusGained(evt);
            }
        });
        tno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tnoKeyPressed(evt);
            }
        });

        ok.setFont(new java.awt.Font("Times New Roman", 3, 14)); // NOI18N
        ok.setText("OK");
        ok.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okMouseClicked(evt);
            }
        });
        ok.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                okKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tno, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ok)
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tno, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ok, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
       
    }//GEN-LAST:event_jLabel1MouseClicked

    private void tnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tnoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER )
            ok.requestFocus();
        
    }//GEN-LAST:event_tnoKeyPressed

    private void okMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okMouseClicked
        // TODO add your handling code here:
        if(!tno.getText().trim().equals(""))
        {
            ObjDao.offTno = tno.getText().trim();
            this.setVisible(false);
            ObjDao.of.inSert();
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Enter Transection No Please");
        }         
    }//GEN-LAST:event_okMouseClicked

    public void callOkMouse()
    {
        MouseEvent me = new MouseEvent(ok, 0, 0, 0, 100, 100, 1, false);
        for(MouseListener ml: ok.getMouseListeners())
        {
            ml.mousePressed(me);
        }
        okMouseClicked(me);
    }
    
    private void okKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_okKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == KeyEvent.VK_ENTER && !tno.getText().trim().equals("") )
        {
            ObjDao.offTno = tno.getText().trim();
            this.setVisible(false);
            ObjDao.of.inSert();
        }
         else
        {
            JOptionPane.showMessageDialog(this, "Enter Transection No Please");
        }
         
        
    }//GEN-LAST:event_okKeyPressed

    private void tnoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tnoFocusGained

    }//GEN-LAST:event_tnoFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    public javax.swing.JButton ok;
    public javax.swing.JTextField tno;
    // End of variables declaration//GEN-END:variables
}
