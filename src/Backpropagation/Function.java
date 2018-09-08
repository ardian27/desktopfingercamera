/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backpropagation;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static jdk.nashorn.internal.objects.NativeMath.round;
  /**
 *
 * @author zorokunti
 */
public class Function {
    
    
    public void addPengguna(String dataPengguna){
        
        try {
            String sql = "INSERT INTO tbl_pengguna VALUES ("+dataPengguna+")";
            java.sql.Connection conn=(Connection)Connect.configDB();
            java.sql.PreparedStatement pst=conn.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    
    public Double normalisasi(Double input, Double min, Double max) {
        return (input - min) / (max - min);
    }
 
   
}
