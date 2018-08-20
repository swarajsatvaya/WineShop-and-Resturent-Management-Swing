/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Java Developer
 */
public class Db_connection {
    
    public static Connection getConnection(){  
        Connection con=null;  
        try
        {  
            String[] str = new String[3];
            String strLine;
            int i = 0;
            BufferedReader br = new BufferedReader(new InputStreamReader( new FileInputStream(  "CS.shop"  )));
            while((strLine = br.readLine()) != null && i<3)
            {
                str[i] = strLine;
                i++;
            }
            Class.forName("com.mysql.jdbc.Driver");  
            con = DriverManager.getConnection("jdbc:mysql://"+str[0]+":3306/wineshop",str[1],str[2]);
            //con = DriverManager.getConnection("jdbc:mysql://192.168.1.8:3306/wineshop","rootlan","");
        }
        catch(ClassNotFoundException | SQLException e)
        {
            //System.out.println(e);
            JOptionPane.showMessageDialog(null,"<html><h2>DATABASE ERROR.</html>");
        } 
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, "<html><h3>Unable to Find Database Cardinals</html>");
        }
        return con;
    }
}
