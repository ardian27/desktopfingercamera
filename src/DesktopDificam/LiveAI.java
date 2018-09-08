/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DesktopDificam;

import Backpropagation.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;

/**
 *
 * @author angsakumisan
 */
public class LiveAI extends javax.swing.JFrame {

    /**
     * Creates new form LiveAI
     */
    
    Integer jumlahdata;
    Data dt = new Data();
    
    public LiveAI() {
        initComponents();
        
        jRadio5050.setActionCommand("50");
        jRadio7030.setActionCommand("70");
        jRadio9010.setActionCommand("90");
        
       
        
        buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(jRadio5050);
        buttonGroup1.add(jRadio7030);
        buttonGroup1.add(jRadio9010);
        
       //
       //dt.setJumlahData(0);
        jRadio5050.setSelected(true);
        String RadioData = buttonGroup1.getSelection().getActionCommand();
        jumlahdata = Integer.parseInt(RadioData);
        dt.setJumlahData(jumlahdata);
        

        
    }
    public void checkHost(){
       String ipSub="192.168.43";
        int timeout=1500;
        for (int i=1;i<255;i++){
       String hostIP=ipSub + "." + i;
           try {
               if (InetAddress.getByName(hostIP).isReachable(timeout)){
                   System.out.println(hostIP);
               }   } catch (UnknownHostException ex) {
              
           } catch (IOException ex) {
              
           }
        }
     
    }
    
     public void  trainingBPNN(){
        
         DecimalFormat df1 = new DecimalFormat("#.#");
           
        double alfa = Double.parseDouble(et_lr.getText());
        int max_iterasi = Integer.parseInt(et_epoh.getText());
        double maxError = Double.parseDouble(et_toleransi_error.getText());
        
        bpnn bp = new bpnn(alfa);
        Data dt = new Data();
        
        double [][] dataLatih = dt.getDataLatih();
         //System.out.println("Jumlah data latih yg diget dari Live ai ="+dataLatih.length);
        
        double bobotBaru [][]=bp.backpro(dataLatih, max_iterasi,maxError);
        
        double insertBobot [][]=new double[6][6];
         for (int i = 0; i < bobotBaru.length; i++) {
             for (int j = 0; j < bobotBaru[0].length; j++) {
                 if (Double.valueOf(df1.format(bobotBaru[i][j])) >= 1.0000000){
                     insertBobot[i][j]=bobotBaru[i][j]-0.1;
                 }else{
                     insertBobot[i][j]=bobotBaru[i][j];
                 }
             }
         }
        
        try {
            
                    String truncate = "TRUNCATE TABLE bobot";
                    String que = "insert into bobot (v0,v1,v2,v3,v4,w)"+" values (?,?,?,?,?,?)";
                    java.sql.Connection conn=(Connection)Connect.configDB();
                    
                    PreparedStatement tc = conn.prepareStatement(truncate);
                    tc.execute();
                    
                    PreparedStatement pst = conn.prepareStatement(que);
                    for (int l = 0; l < bobotBaru.length; l++) {
                        for (int k = 0; k < 1; k++) {
                        pst.setDouble(1, Double.valueOf(df1.format(insertBobot[l][0])));
                        pst.setDouble(2, Double.valueOf(df1.format(insertBobot[l][1])));
                        pst.setDouble(3, Double.valueOf(df1.format(insertBobot[l][2])));
                        pst.setDouble(4, Double.valueOf(df1.format(insertBobot[l][3])));
                        pst.setDouble(5, Double.valueOf(df1.format(insertBobot[l][4])));
                        pst.setDouble(6, Double.valueOf(df1.format(insertBobot[l][5])));
                        pst.executeUpdate();
                        }
                        
            }       
                } catch (Exception e) {
                }
        
        dt.setAllUji(alfa);
       
         double [][] bh = dt.getBobotHiddenBPNN(insertBobot);
         double [] bias = dt.getBiasBPNN(insertBobot);
         double [] bo = dt.getBobotOutputBPNN(insertBobot);
         
        System.out.println("MATRIK  TERBARU ");
        
        for(int i=0;i<insertBobot.length;i++){
            for(int j=0;j<insertBobot[0].length;j++){
               // System.out.println("i="+i+"j="+j);
                System.out.print(insertBobot[i][j]+" ");
                
            }
            System.out.println(""); 
        }
        
 /*      
        System.out.println("\n\n Bobot Hidden");
        for (int i = 0; i < bh.length; i++) {
             for (int j = 0; j < bh[0].length; j++) {
                 System.out.print(""+bh[i][j]+" ");
             }
             System.out.println(""); 
         }
        
        System.out.println("\n\n Bobot Bias");
        for (int i = 0; i < bias.length; i++) {
            System.out.print(""+bias[i]+" ");
         }
        
         System.out.println("\n\n Bobot Output");
        for (int i = 0; i < bo.length; i++) {
            System.out.print(""+bo[i]+" ");
         }
        */
         
     
        
       
        
       
    }
     
      public double pengujian(){
        double alfa = Double.parseDouble(et_lr.getText());
        bpnn bp = new bpnn(alfa);
        Data dt = new Data();
        
        
      
        double x1,x2,x3,x4,x5;
        
        x1 = Double.parseDouble(et_mean.getText());
        x2 = Double.parseDouble(et_variance.getText());
        x3 = Double.parseDouble(et_skewness.getText());
        x4 = Double.parseDouble(et_kurtosis.getText());
        x5 = Double.parseDouble(et_entrophy.getText());
        
        double [][] xx = new double[1][5];
        double [][] bobothidden = dt.getBobotHidden();
        double [] bobotbias = dt.getBias();
        double [] bobotoutput = dt.getBobotOutput();
        
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j<1; j++) {
                xx[0][0]=x1;
                xx[0][1]=x2;
                xx[0][2]=x3;
                xx[0][3]=x4;
                xx[0][4]=x5;
            }
        }
        
        double y = bp.feedForward(xx, 0, bobothidden, bobotbias, bobotoutput);
        System.out.println(y);
        
        double [][] compareUji = dt.getCompareUji();
        
        double start = y;
        
        
        List<Double> nilaiCompare = new ArrayList<>();
       
          for (int i = 0; i < compareUji.length; i++) {
              for (int j = 0; j < compareUji[0].length-1; j++) {
                  nilaiCompare.add(compareUji[i][j]);
              }
          }

        Collections.sort(nilaiCompare);

        double nearest = 0;

        for (double i : nilaiCompare)
        {
            if (i <= start) {
                nearest = i;
                
            }
        }
        //System.out.println(nearest);
        dt.getUser(nearest);
                  
        return y;
    }
      
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField3 = new javax.swing.JTextField();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt_area_host = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        btn_test = new javax.swing.JButton();
        et_mean = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        et_variance = new javax.swing.JTextField();
        et_skewness = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        et_kurtosis = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        et_entrophy = new javax.swing.JTextField();
        btn_identifikasi = new javax.swing.JButton();
        btn_testfungsi = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btn_auto_training = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        et_lr = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        et_epoh = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        et_toleransi_error = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jRadio5050 = new javax.swing.JRadioButton();
        jRadio7030 = new javax.swing.JRadioButton();
        jRadio9010 = new javax.swing.JRadioButton();

        jTextField3.setText("jTextField3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server - Live Training Artificial Inteligent");
        setAlwaysOnTop(true);
        setResizable(false);

        jPanel2.setBackground(java.awt.Color.lightGray);

        jPanel5.setBackground(java.awt.Color.darkGray);

        txt_area_host.setColumns(20);
        txt_area_host.setRows(5);
        jScrollPane1.setViewportView(txt_area_host);

        jLabel4.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("All Host Connected");

        btn_test.setText("Test");
        btn_test.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_testActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_test))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(btn_test))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        et_mean.setText("0.99443");

        jLabel5.setText("Mean");

        jLabel13.setText("Variance");

        et_variance.setText("0.97108");

        et_skewness.setText("0.86554");
        et_skewness.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                et_skewnessActionPerformed(evt);
            }
        });

        jLabel14.setText("Skewness");

        jLabel15.setText("Kurtosis");

        et_kurtosis.setText("0.81181");

        jLabel16.setText("Entrophy");

        et_entrophy.setText("0.98731");

        btn_identifikasi.setText("Identifikasi");
        btn_identifikasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_identifikasiActionPerformed(evt);
            }
        });

        btn_testfungsi.setText("Dev Menu");
        btn_testfungsi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_testfungsiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btn_identifikasi)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel16)
                                                .addComponent(jLabel15))
                                            .addGap(27, 27, 27))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(et_skewness)
                                        .addComponent(et_kurtosis)
                                        .addComponent(et_entrophy)
                                        .addComponent(et_variance)
                                        .addComponent(et_mean, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addComponent(btn_testfungsi))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(btn_testfungsi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(et_mean, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(et_variance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(et_skewness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(et_kurtosis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(8, 8, 8)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(et_entrophy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(29, 29, 29)
                .addComponent(btn_identifikasi)
                .addGap(60, 60, 60))
        );

        jPanel3.setBackground(java.awt.Color.gray);

        btn_auto_training.setText("Training BPNN");
        btn_auto_training.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_auto_trainingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_auto_training, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_auto_training, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(java.awt.Color.gray);

        jLabel1.setBackground(java.awt.Color.white);
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Learning Rate");

        et_lr.setText("0.1");
        et_lr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                et_lrActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Max Epoh");

        et_epoh.setText("10000");
        et_epoh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                et_epohActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Toleransi Error");

        et_toleransi_error.setText("0.00001");
        et_toleransi_error.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                et_toleransi_errorActionPerformed(evt);
            }
        });

        jLabel6.setBackground(java.awt.Color.white);
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Pembagian Data");

        jRadio5050.setForeground(new java.awt.Color(255, 255, 255));
        jRadio5050.setText("Data 50:50");
        jRadio5050.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadio5050ActionPerformed(evt);
            }
        });

        jRadio7030.setText("Data 70:30");
        jRadio7030.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadio7030ActionPerformed(evt);
            }
        });

        jRadio9010.setText("Data 90:10");
        jRadio9010.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jRadio9010ComponentAdded(evt);
            }
        });
        jRadio9010.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadio9010ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadio9010, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jRadio7030, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jRadio5050, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))
                                .addGap(25, 25, 25))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(et_epoh)
                    .addComponent(et_lr)
                    .addComponent(et_toleransi_error, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(et_lr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(et_epoh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(et_toleransi_error, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadio5050)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadio7030)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadio9010)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void et_lrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_et_lrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_et_lrActionPerformed

    private void et_epohActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_et_epohActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_et_epohActionPerformed

    private void et_toleransi_errorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_et_toleransi_errorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_et_toleransi_errorActionPerformed

    private void btn_testActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_testActionPerformed
        // TODO add your handling code here:
        txt_area_host.setText("Connecting...\n");
        checkHost();
    }//GEN-LAST:event_btn_testActionPerformed

    private void et_skewnessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_et_skewnessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_et_skewnessActionPerformed

    private void btn_identifikasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_identifikasiActionPerformed
        // TODO add your handling code here:
        pengujian();
        //System.out.println("mean field "+et_mean.getText());
    }//GEN-LAST:event_btn_identifikasiActionPerformed

    private void btn_testfungsiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_testfungsiActionPerformed

        MainBPNN bp = new MainBPNN();
        bp.setVisible(true);
    }//GEN-LAST:event_btn_testfungsiActionPerformed

    private void btn_auto_trainingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_auto_trainingActionPerformed
        // TODO add your handling code here:
        trainingBPNN();
    }//GEN-LAST:event_btn_auto_trainingActionPerformed

    private void jRadio9010ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadio9010ActionPerformed
        // TODO add your handling code here:
       
        
    }//GEN-LAST:event_jRadio9010ActionPerformed

    private void jRadio9010ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jRadio9010ComponentAdded
        // TODO add your handling code here:
        
        
        String RadioData = buttonGroup1.getSelection().getActionCommand();
        jumlahdata = Integer.parseInt(RadioData);
        dt.setJumlahData(jumlahdata);
        
    }//GEN-LAST:event_jRadio9010ComponentAdded

    private void jRadio7030ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadio7030ActionPerformed
        // TODO add your handling code here:
        
        
        String RadioData = buttonGroup1.getSelection().getActionCommand();
        jumlahdata = Integer.parseInt(RadioData);
        dt.setJumlahData(jumlahdata);
        
    }//GEN-LAST:event_jRadio7030ActionPerformed

    private void jRadio5050ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadio5050ActionPerformed
        // TODO add your handling code here:
        
        
        String RadioData = buttonGroup1.getSelection().getActionCommand();
        jumlahdata = Integer.parseInt(RadioData);
        dt.setJumlahData(jumlahdata);
        
        //System.out.println("setdata"+RadioData);
        
        
    }//GEN-LAST:event_jRadio5050ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LiveAI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LiveAI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LiveAI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LiveAI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LiveAI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_auto_training;
    private javax.swing.JButton btn_identifikasi;
    private javax.swing.JButton btn_test;
    private javax.swing.JButton btn_testfungsi;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JTextField et_entrophy;
    private javax.swing.JTextField et_epoh;
    private javax.swing.JTextField et_kurtosis;
    private javax.swing.JTextField et_lr;
    private javax.swing.JTextField et_mean;
    private javax.swing.JTextField et_skewness;
    private javax.swing.JTextField et_toleransi_error;
    private javax.swing.JTextField et_variance;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRadio5050;
    private javax.swing.JRadioButton jRadio7030;
    private javax.swing.JRadioButton jRadio9010;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextArea txt_area_host;
    // End of variables declaration//GEN-END:variables
}
