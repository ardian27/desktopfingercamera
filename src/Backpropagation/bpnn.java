package Backpropagation;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.util.Random;
//import javax.swing.JOptionPane;

/**
 *
 * @author angsakumisan
 */
public class bpnn {
    double alfa;
    double[][] bobotHidden;
    double[] bobotOutput;
    double[] bias;
    Random rand = new Random();
    
    DecimalFormat df = new DecimalFormat("#.######");
    int panjangData=5;

    
//    FUNGSI INISIALIASI BOBOT DAN BIAS RANDOM
    public bpnn( double alfa){
        bobotHidden = new double[panjangData][panjangData];
        for(int i=0;i<bobotHidden.length;i++){ 
            for(int j=0;j<bobotHidden[0].length;j++){
                bobotHidden[i][j] = round(rand.nextFloat()*1, 1);
               //  System.out.print("bobot ["+i+"]["+j+"]= "+bobotHidden[i][j]);
            }
        }
        bobotOutput = new double[panjangData];
        for(int i=0;i<bobotOutput.length;i++){
            bobotOutput[i] = round(rand.nextFloat()*1, 1);
            //System.out.println(" bobot ["+i+"] ="+bobotOutput[i]);
        }
        bias = new double[panjangData+1];
        for(int i=0;i<bias.length;i++){
            bias[i] = round(rand.nextFloat()*1, 1);
        }
        this.panjangData = panjangData;
        this.alfa = alfa;
    }
    
    public static float round(float d, int decimalPlace) {
    BigDecimal bd = new BigDecimal(Float.toString(d));
    bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
    return bd.floatValue();
    }
    
    //FUNGSI AMBIL JUMLAH DATA
    public int getJumlahData(double[][] data){
        return data.length;
    }
    
    //HITUNG Z_in
    public double[] z_in(double[][] normal, int a, double[][] bobotHidden, double[] bias){
        double[] z_in = new double[bobotHidden[0].length];
        for(int i=0;i<z_in.length;i++){
            for(int j=0;j<bobotHidden[0].length;j++){
                z_in[i] = z_in[i] + (normal[a][j]*bobotHidden[i][j]);
            }
            z_in[i] = z_in[i]+bias[i];
        }
        return z_in;
    }
    //HITUNG Z
    public double[] z(double[] z_in){
        double[] z = new double[z_in.length];
        for(int i=0;i<z.length;i++){
            z[i]=1/(1+Math.exp(-(z_in[i])));
        }
        return z;
    }
    //HITUNG Y_in
    public double y_in(double[] z, double[] bobotOutput, double[] bias){
        double y_in=0;
        for(int i=0;i<z.length;i++){
            y_in = y_in + (z[i]*bobotOutput[i]);
        }
        y_in = y_in+bias[bias.length-1];
        return y_in;
    }
    //HITUNG Y
    public double y(double y_in){
       // double y=1/(1+Math.exp(-(y_in)));
        double y=1/(1+Math.exp(-(y_in)));
        return y;
    }
    //HITUNG error
    public double error(double y, double[][] normal, int a){
        double error = normal[a][normal[0].length-1]-y;
        return error;
    }
    //HITUNG FAKTOR KOREKSI ERROR OUTPUT
    public double koreksiOutput(double error, double y){
        double koreksiOutput = error*(y*(1-y));
        return koreksiOutput;
    }
    //HITUNG PERUBAHAN BOBOT OUTPUT UNIT
    public double[] deltaBobotOutput(double koreksiOutput, double[] z){
        double[] deltaBobotOutput = new double[z.length];
        for(int i=0;i<deltaBobotOutput.length;i++){
            deltaBobotOutput[i] = alfa*koreksiOutput*z[i];
        }
        return deltaBobotOutput;
    }
    //HITUNG PERUBAHAN BIAS OUTPUT UNIT
    public double deltaBiasOutput(double koreksiOutput){
        double deltaBiasOutput = alfa*koreksiOutput;
        return deltaBiasOutput;
    }
    //HITUNG FAKTOR KOREKSI error_in Hidden Unit
    public double[] koreksiHidden_in(double koreksiOutput, double[] bobotOutput){
        double[] koreksiHidden_in = new double[bobotOutput.length];
        for(int i=0;i<koreksiHidden_in.length;i++){
            koreksiHidden_in[i]=koreksiOutput*bobotOutput[i];
        }
        return koreksiHidden_in;
    }
    //HITUNG FAKTOR KOREKSI error Hidden Unit
    public double[] koreksiHidden(double[] z, double[] koreksiHidden_in){
        double[] koreksiHidden = new double[z.length];
        for(int i=0;i<koreksiHidden.length;i++){
            koreksiHidden[i]=koreksiHidden_in[i]*z[i]*(1-z[i]);
        }
        return koreksiHidden;
    }
    //HITUNG PERUBAHAN BOBOT HIDDEN UNIT
    public double[][] deltaBobotHidden(double[] koreksiHidden, double[][] normal, int a, double[][] bobotHidden){
        double[][] deltaBobotHidden = new double[bobotHidden.length][bobotHidden[0].length];
        for(int i=0;i<deltaBobotHidden.length;i++){
            for(int j=0;j<deltaBobotHidden[0].length;j++){
                deltaBobotHidden[i][j]=alfa*koreksiHidden[i]*normal[a][j];
            }
        }
        return deltaBobotHidden;
    }
    //HITUNG PERUBAHAN BIAS HIDDEN UNIT
    public double[] deltaBiasHidden(double[] koreksiHidden){
        double[] deltaBiasHidden = new double[koreksiHidden.length];
        for(int i=0;i<deltaBiasHidden.length;i++){
            deltaBiasHidden[i]=alfa*koreksiHidden[i];
        }
        return deltaBiasHidden;
    }
    //UPDATE BOBOT HIDDEN UNIT BARU
    public double[][] bobotHiddenBaru(double[][] deltaBobotHidden, int a, double[][] bobotHidden){
        double[][] bobotHiddenBaru = new double[bobotHidden.length][bobotHidden[0].length];
        for(int i=0;i<bobotHiddenBaru.length;i++){
            for(int j=0;j<bobotHiddenBaru[0].length;j++){
                bobotHiddenBaru[i][j]=bobotHidden[i][j]+deltaBobotHidden[i][j];
            }
        }
        return bobotHiddenBaru;
    }
    //UPDATE BIAS HIDDEN UNIT BARU
    public double[] biasHiddenBaru(double[] deltaBiasHidden, double[] biasBaru){
        double[] biasHiddenBaru = new double[deltaBiasHidden.length];
        for(int i=0;i<biasHiddenBaru.length;i++){
            biasHiddenBaru[i]=biasBaru[i]+deltaBiasHidden[i];
        }
        return biasHiddenBaru;
    }
    //UPDATE BOBOT OUTPUT UNIT BARU
    public double[] bobotOutputBaru(double[] deltaBobotOutput, double[] bobotOutput){
        double[] bobotOutputBaru = new double[bobotOutput.length];
        for(int i=0;i<bobotOutputBaru.length;i++){
            bobotOutputBaru[i]=bobotOutput[i]+deltaBobotOutput[i];
        }
        return bobotOutputBaru;
    }
    //UPDATE BIAS OUTPUT UNIT BARU
    public double biasOutputBaru(double deltaBiasOutput, double[] biasBaru){
        double biasOutputBaru = biasBaru[panjangData]+deltaBiasOutput;
        return biasOutputBaru;
    }
    //TAMPUNG BIAS BARU KEDALAM 1 MATRIK
    public double[] biasBaru(double[] biasHiddenBaru, double biasOutputBaru){
        double[] biasBaru = new double[biasHiddenBaru.length+1];
        for(int i=0;i<biasHiddenBaru.length; i++) {
            biasBaru[i] = biasHiddenBaru[i];
        }
        biasBaru[biasBaru.length-1] = biasOutputBaru;
        return biasBaru;
    }
    //TAMPUNG BOBOT DAN BIAS BARU KEDALAM 1 MATRIK
    public double[][] bobotBaru(double[][] bobotHiddenUnit, double[] bobotOutputUnit, double[] biasBaru){
        double[][] bobotHiddenUnitTrans = new double[bobotHiddenUnit[0].length][bobotHiddenUnit.length];
        for(int i=0;i<bobotHiddenUnit.length;i++){
            for(int j=0;j<bobotHiddenUnit[0].length;j++){
                bobotHiddenUnitTrans[j][i] = bobotHiddenUnit[i][j];
            }
        }
        double[][] bobotTampung = new double[bobotHiddenUnitTrans.length][bobotHiddenUnitTrans[0].length+1];
        for(int i=0;i<bobotTampung.length;i++){
            for(int j=0;j<bobotHiddenUnitTrans[0].length;j++){
                bobotTampung[i][j] = bobotHiddenUnitTrans[i][j];
            }
        }
        for(int i=0;i<bobotTampung.length;i++){
            bobotTampung[i][bobotHiddenUnit[0].length] = bobotOutputUnit[i];
        }
        double[][] tampung = new double[bobotTampung.length+1][bobotHiddenUnitTrans[0].length+1];
        for(int i=0;i<tampung.length-1;i++){
            for(int j=0;j<tampung[0].length;j++){
                tampung[i][j] = bobotTampung[i][j];
            }
        }
        for(int i=0;i<tampung[0].length;i++){
            tampung[bobotHiddenUnit.length][i] = biasBaru[i];
        }
        return tampung;
    
    
    }
    
    
    
    //DE_NORMALISASI
    public double deNormalisasi(double y, double[][] data){
        //MENCARI NILAI MAXIMAL DATA
        double max = 0;
        for(int i=0;i<data.length;i++){
            for(int j=0;j<data[i].length;j++){
                if(data[i][j]>max){
                    max = data[i][j];
                }
            }
        }
        //MENCARI NILAI MINIMAL DATA
        double min = 0;
        for(int i=0;i<data.length;i++){
            for(int j=0;j<data[0].length;j++){
                if(data[i][j]<min){
                    min = data[i][j];
                }
            }
        }
        double denor = (y*(max-min))+min;
        return denor;
    }
    //HITUNG MSE
    public double mse(double[] denor, double[][] data){
        double error = 0;
        double mse = 0;
        for(int i=0;i<denor.length;i++){
            error = error + (Math.pow(data[i][panjangData]-denor[i],2));
        }
        mse = error/denor.length;
        return mse;
    }
    //HITUNG NILAI KEAKURATAN
    public double akurat(double mse){
        double akurat = 1/(1+mse);
        return akurat;
    }
    
    //FUNGSI FEEDFORWARD
    public double feedForward(double[][] data, int barisData, double[][] bobotHidden, double[] bias, double[] bobotOutput){
        double[][] normal = data;
        double[] z_in = z_in(normal, barisData, bobotHidden, bias);
        double[] z = z(z_in);
        double y_in = y_in(z, bobotOutput, bias);
        double y = y(y_in);
        return y;
    }
    
    //FUNGSI BACKPROPAGATION
    public double[][] backpro(double[][] data, int jumlahIterasi, double maxError){
        double[][] normal = data;
        double[] z_in = z_in(normal, 0, bobotHidden, bias);
        double[] z = z(z_in);
        double y_in = y_in(z, bobotOutput, bias);
        double y = y(y_in);
        double error = error(y, normal, 0);
        double koreksiOutput = koreksiOutput(error, y);
        double[] deltaBobotOutput = deltaBobotOutput(koreksiOutput, z);
        double deltaBiasOutput = deltaBiasOutput(koreksiOutput);
        double[] koreksiHidden_in = koreksiHidden_in(koreksiOutput, bobotOutput);
        double[] koreksiHidden = koreksiHidden(z, koreksiHidden_in);
        double[][] deltaBobotHidden = deltaBobotHidden(koreksiHidden, normal, 0, bobotHidden);
        double[] deltaBiasHidden = deltaBiasHidden(koreksiHidden);
        double[][] bobotHiddenBaru = bobotHiddenBaru(deltaBobotHidden, 0, bobotHidden);
        double[] biasHiddenBaru = biasHiddenBaru(deltaBiasHidden, bias);
        double[] bobotOutputBaru = bobotOutputBaru(deltaBobotOutput, bobotOutput);
        double biasOutputBaru = biasOutputBaru(deltaBiasOutput, bias);
        double[] biasBaru = biasBaru(biasHiddenBaru, biasOutputBaru);
        for(int j=0;j<getJumlahData(normal);j++){
            z_in = z_in(normal, j, bobotHiddenBaru, biasBaru);
            z = z(z_in);
            y_in = y_in(z, bobotOutputBaru, biasBaru);
            y = y(y_in);
            error = error(y, normal, j);
            koreksiOutput = koreksiOutput(error, y);
            deltaBobotOutput = deltaBobotOutput(koreksiOutput, z);
            deltaBiasOutput = deltaBiasOutput(koreksiOutput);
            koreksiHidden_in = koreksiHidden_in(koreksiOutput, bobotOutputBaru);
            koreksiHidden = koreksiHidden(z, koreksiHidden_in);
            deltaBobotHidden = deltaBobotHidden(koreksiHidden, normal, j, bobotHiddenBaru);
            deltaBiasHidden = deltaBiasHidden(koreksiHidden);
            bobotHiddenBaru = bobotHiddenBaru(deltaBobotHidden, j, bobotHiddenBaru);
            biasHiddenBaru = biasHiddenBaru(deltaBiasHidden, biasHiddenBaru);
            bobotOutputBaru = bobotOutputBaru(deltaBobotOutput, bobotOutputBaru);
            biasOutputBaru = biasOutputBaru(deltaBiasOutput, biasBaru);
            biasBaru = biasBaru(biasHiddenBaru, biasOutputBaru);
        }
        
        int xx=1;
        double nilai_error =0;
        
             //System.out.println("jumlah error ="+koreksiOutput);
  
            while( xx < jumlahIterasi && maxError <= koreksiOutput){
            for (int j = 0; j < getJumlahData(normal)-1; j++) {
                
                //System.out.println("Jumlah data di class bpnn  "+j+"="+data.length);
                z_in = z_in(normal, j, bobotHiddenBaru, biasBaru);
                z = z(z_in);
                y_in = y_in(z, bobotOutputBaru, biasBaru);
                y = y(y_in);
                error = error(y, normal, j);
                koreksiOutput = koreksiOutput(error, y);
                deltaBobotOutput = deltaBobotOutput(koreksiOutput, z);
                deltaBiasOutput = deltaBiasOutput(koreksiOutput);
                koreksiHidden_in = koreksiHidden_in(koreksiOutput, bobotOutputBaru);
                koreksiHidden = koreksiHidden(z, koreksiHidden_in);
                deltaBobotHidden = deltaBobotHidden(koreksiHidden, normal, j, bobotHiddenBaru);
                deltaBiasHidden = deltaBiasHidden(koreksiHidden);
                bobotHiddenBaru = bobotHiddenBaru(deltaBobotHidden, j, bobotHiddenBaru);
                biasHiddenBaru = biasHiddenBaru(deltaBiasHidden, biasHiddenBaru);
                bobotOutputBaru = bobotOutputBaru(deltaBobotOutput, bobotOutputBaru);
                biasOutputBaru = biasOutputBaru(deltaBiasOutput, biasBaru);
                biasBaru = biasBaru(biasHiddenBaru, biasOutputBaru);
               // System.out.println("no"+xx);
               nilai_error = Double.valueOf(df.format(koreksiOutput));
                
            }
            xx++;
            System.out.println("Finish di iterasi "+xx);
            //System.out.println("Dengan Nilai Error "+df.format(koreksiOutput));
        }
            
         // System.out.println("nilai max error = "+maxError);
            
            
        
        double[][] bobotBaru = bobotBaru(bobotHiddenBaru, bobotOutputBaru, biasBaru);

        return bobotBaru;
    }
    
            
    public static void main(String[] args) {
        bpnn backpro = new bpnn(0.1);
        Data data = new Data();
//        double feedForward = backpro.feedForward(data.aturData(data.getData()));
        double [][] test = new double [1][5];
        test [0][0] = 0.9256;
        test [0][1] = 0.7126;
        test [0][2] = 1;
        test [0][3] = 0.7971;
        test [0][4] = 0.6009;
        //double[][] normal = data.getDataLatih();
        double [][] bh = data.getBobotHidden();
        double [] bo = data.getBobotOutput();
        double [] bias = data.getBias();
        double[][] normal = test;
        
//        double[][] dataTampung = data.getDataLatih(6,4);
//        
//        for(int i=0;i<dataTampung.length;i++){
//            for(int j=0;j<dataTampung[i].length;j++){
//                System.out.print(dataTampung[i][j]+" ");
//            }
//            System.out.println("");
//        }
        
// batas test 

        System.out.println("=========");
        System.out.println("ITERASI 1");
        System.out.println("=========");
        //TAMPILKAN DATA NORMALISASI
        System.out.println("HASIL NORMALISASI DATA:");
        for(int i=0;i<normal.length;i++){
            for(int j=0;j<normal[0].length;j++){
                System.out.print(normal[i][j] +" ");
            }
            System.out.println("");
        }
        //TAMPILKAN Z_in
        System.out.println("");
        System.out.println("NILAI Z_in DATA 1");
        
        double[] z_in = backpro.z_in(normal, 0, bh, bias);
        for(int i=0;i<z_in.length;i++){
            System.out.println(z_in[i]);
        }
        //TAMPILKAN Z
        System.out.println("");
        System.out.println("NILAI Z DATA 1");
        double[] z = backpro.z(z_in);
        for(int i=0;i<z.length;i++){
            System.out.println(z[i]);
        }
        //TAMPILKAN Y_in
        System.out.println("");
        System.out.println("NILAI Y_in DATA 1");
        double y_in = backpro.y_in(z, bo, bias);
        System.out.println(y_in);
        //TAMPILKAN Y
        System.out.println("");
        System.out.println("NILAI Y DATA 1");
        double y = backpro.y(y_in);
        System.out.println(y);
        //TAMPILKAN error
        System.out.println("");
        System.out.println("NILAI Error DATA 1");
        double error = backpro.error(y, normal, 0);
        System.out.println(error);
        //TAMPILKAN FAKTOR KOREKSI ERROR OUTPUT UNIT
        System.out.println("");
        System.out.println("NILAI Faktor Koreksi Error Output Unit DATA 1");
        double koreksiOutput = backpro.koreksiOutput(error, y);
        System.out.println(koreksiOutput);
        //TAMPILKAN PERUBAHAN BOBOT OUTPUT
        System.out.println("");
        System.out.println("PERUBAHAN BOBOT OUTPUT UNIT DATA 1");
        double[] deltaBobotOutput = backpro.deltaBobotOutput(koreksiOutput, z);
        for(int i=0;i<deltaBobotOutput.length;i++){
            System.out.println(deltaBobotOutput[i]);
        }
        //TAMPILKAN PERUBAHAN BIAS OUTPUT
        System.out.println("");
        System.out.println("PERUBAHAN BIAS OUTPUT UNIT DATA 1");
        double deltaBiasOutput = backpro.deltaBiasOutput(koreksiOutput);
        System.out.println(deltaBiasOutput);
        //TAMPILKAN FAKTOR KOREKSI error_in Hidden Unit
        System.out.println("");
        System.out.println("NILAI Faktor Koreksi_in Hidden Unit DATA 1");
        double[] koreksiHidden_in = backpro.koreksiHidden_in(koreksiOutput, bo);
        for(int i=0;i<koreksiHidden_in.length;i++){
            System.out.println(koreksiHidden_in[i]);
        }
        //TAMPILKAN FAKTOR KOREKSI HIDDEN UNIT
        System.out.println("");
        System.out.println("NILAI Faktor Koreksi Hidden Unit DATA 1");
        double[] koreksiHidden = backpro.koreksiHidden(z, koreksiHidden_in);
        for(int i=0;i<koreksiHidden.length;i++){
            System.out.println(koreksiHidden[i]);
        }
        //TAMPILKAN PERUBAHAN BOBOT HIDDEN UNIT
        System.out.println("");
        System.out.println("PERUBAHAN BOBOT HIDDEN UNIT DATA 1");
        double[][] deltaBobotHidden = backpro.deltaBobotHidden(koreksiHidden, normal, 0, bh);
        for(int i=0;i<deltaBobotHidden.length;i++){
            for(int j=0;j<deltaBobotHidden[0].length;j++){
                System.out.print(deltaBobotHidden[i][j] +" ");
            }
            System.out.println("");
        }
        //TAMPILKAN PERUBAHAN BIAS HIDDEN UNIT
        System.out.println("");
        System.out.println("PERUBAHAN BIAS HIDDEN UNIT DATA 1");
        double[] deltaBiasHidden = backpro.deltaBiasHidden(koreksiHidden);
        for(int i=0;i<deltaBiasHidden.length;i++){
            System.out.println(deltaBiasHidden[i]);
        }
        //UPDATE BOBOT HIDDEN UNIT BARU
        System.out.println("");
        System.out.println("BOBOT HIDDEN UNIT BARU DATA 1");
        double[][] bobotHiddenBaru = backpro.bobotHiddenBaru(deltaBobotHidden, 0, bh);
        for(int i=0;i<bobotHiddenBaru.length;i++){
            for(int j=0;j<bobotHiddenBaru[0].length;j++){
                System.out.print(bobotHiddenBaru[i][j] +" ");
            }
            System.out.println("");
        }
        //UPDATE BIAS HIDDEN UNIT BARU
        System.out.println("");
        System.out.println("BIAS HIDDEN UNIT BARU DATA 1");
        double[] biasHiddenBaru = backpro.biasHiddenBaru(deltaBiasHidden, bias);
        for(int i=0;i<biasHiddenBaru.length;i++){
            System.out.println(biasHiddenBaru[i]);
        }
        //UPDATE BOBOT OUTPUT UNIT BARU
        System.out.println("");
        System.out.println("BOBOT OUTPUT UNIT BARU DATA 1");
        double[] bobotOutputBaru = backpro.bobotOutputBaru(deltaBobotOutput, bo);
        for(int i=0;i<bobotOutputBaru.length;i++){
            System.out.println(bobotOutputBaru[i]);
        }
        //UPDATE BIAS OUTPUT UNIT BARU
        System.out.println("");
        System.out.println("BIAS OUTPUT UNIT BARU DATA 1");
        double biasOutputBaru = backpro.biasOutputBaru(deltaBiasOutput, bias);
        System.out.println(biasOutputBaru);
        
        //TAMPUNG NILAI MATRIK BIAS BARU
        double[] biasBaru = backpro.biasBaru(biasHiddenBaru, biasOutputBaru);
        
        //==============================================================================================================================================   
        
        //PERULANGAN DATA
        for(int j=1;j<backpro.getJumlahData(normal);j++){
            //TAMPILKAN Z_in
            System.out.println("");
            System.out.println("NILAI Z_in DATA "+(j+1));
            z_in = backpro.z_in(normal, j, bobotHiddenBaru, biasBaru);
            for (int i = 0; i < z_in.length; i++) {
                System.out.println(z_in[i]);
            }
            //TAMPILKAN Z
            System.out.println("");
            System.out.println("NILAI Z DATA "+(j+1));
            z = backpro.z(z_in);
            for (int i = 0; i < z.length; i++) {
                System.out.println(z[i]);
            }
            //TAMPILKAN Y_in
            System.out.println("");
            System.out.println("NILAI Y_in DATA "+(j+1));
            y_in = backpro.y_in(z, bobotOutputBaru, biasBaru);
            System.out.println(y_in);
            //TAMPILKAN Y
            System.out.println("");
            System.out.println("NILAI Y DATA "+(j+1));
            y = backpro.y(y_in);
            System.out.println(y);
            //TAMPILKAN error
            System.out.println("");
            System.out.println("NILAI Error DATA "+(j+1));
            error = backpro.error(y, normal, j);
            System.out.println(error);
            //TAMPILKAN FAKTOR KOREKSI ERROR OUTPUT UNIT
            System.out.println("");
            System.out.println("NILAI Faktor Koreksi Error Output Unit DATA "+(j+1));
            koreksiOutput = backpro.koreksiOutput(error, y);
            System.out.println(koreksiOutput);
            //TAMPILKAN PERUBAHAN BOBOT OUTPUT
            System.out.println("");
            System.out.println("PERUBAHAN BOBOT OUTPUT UNIT DATA "+(j+1));
            deltaBobotOutput = backpro.deltaBobotOutput(koreksiOutput, z);
            for (int i = 0; i < deltaBobotOutput.length; i++) {
                System.out.println(deltaBobotOutput[i]);
            }
            //TAMPILKAN PERUBAHAN BIAS OUTPUT
            System.out.println("");
            System.out.println("PERUBAHAN BIAS OUTPUT UNIT DATA "+(j+1));
            deltaBiasOutput = backpro.deltaBiasOutput(koreksiOutput);
            System.out.println(deltaBiasOutput);
            //TAMPILKAN FAKTOR KOREKSI error_in Hidden Unit
            System.out.println("");
            System.out.println("NILAI Faktor Koreksi_in Hidden Unit DATA "+(j+1));
            koreksiHidden_in = backpro.koreksiHidden_in(koreksiOutput, bobotOutputBaru);
            for (int i = 0; i < koreksiHidden_in.length; i++) {
                System.out.println(koreksiHidden_in[i]);
            }
            //TAMPILKAN FAKTOR KOREKSI HIDDEN UNIT
            System.out.println("");
            System.out.println("NILAI Faktor Koreksi Hidden Unit DATA "+(j+1));
            koreksiHidden = backpro.koreksiHidden(z, koreksiHidden_in);
            for (int i = 0; i < koreksiHidden.length; i++) {
                System.out.println(koreksiHidden[i]);
            }
            //TAMPILKAN PERUBAHAN BOBOT HIDDEN UNIT
            System.out.println("");
            System.out.println("PERUBAHAN BOBOT HIDDEN UNIT DATA "+(j+1));
            deltaBobotHidden = backpro.deltaBobotHidden(koreksiHidden, normal, j, bobotHiddenBaru);
            for (int i = 0; i < deltaBobotHidden.length; i++) {
                for (int k = 0; k < deltaBobotHidden[i].length; k++) {
                    System.out.print(deltaBobotHidden[i][k] + " ");
                }
                System.out.println("");
            }
            //TAMPILKAN PERUBAHAN BIAS HIDDEN UNIT
            System.out.println("");
            System.out.println("PERUBAHAN BIAS HIDDEN UNIT DATA "+(j+1));
            deltaBiasHidden = backpro.deltaBiasHidden(koreksiHidden);
            for (int i = 0; i < deltaBiasHidden.length; i++) {
                System.out.println(deltaBiasHidden[i]);
            }
            //UPDATE BOBOT HIDDEN UNIT BARU
            System.out.println("");
            System.out.println("BOBOT HIDDEN UNIT BARU DATA "+(j+1));
            bobotHiddenBaru = backpro.bobotHiddenBaru(deltaBobotHidden, j, bobotHiddenBaru);
            for (int i = 0; i < bobotHiddenBaru.length; i++) {
                for (int k = 0; k < bobotHiddenBaru[i].length; k++) {
                    System.out.print(bobotHiddenBaru[i][k] + " ");
                }
                System.out.println("");
            }
            //UPDATE BIAS HIDDEN UNIT BARU
            System.out.println("");
            System.out.println("BIAS HIDDEN UNIT BARU DATA "+(j+1));
            biasHiddenBaru = backpro.biasHiddenBaru(deltaBiasHidden, biasHiddenBaru);
            for (int i = 0; i < biasHiddenBaru.length; i++) {
                System.out.println(biasHiddenBaru[i]);
            }
            //UPDATE BOBOT OUTPUT UNIT BARU
            System.out.println("");
            System.out.println("BOBOT OUTPUT UNIT BARU DATA "+(j+1));
            bobotOutputBaru = backpro.bobotOutputBaru(deltaBobotOutput, bobotOutputBaru);
            for (int i = 0; i < bobotOutputBaru.length; i++) {
                System.out.println(bobotOutputBaru[i]);
            }
            //UPDATE BIAS OUTPUT UNIT BARU
            System.out.println("");
            System.out.println("BIAS OUTPUT UNIT BARU DATA "+(j+1));
            biasOutputBaru = backpro.biasOutputBaru(deltaBiasOutput, biasBaru);
            System.out.println(biasOutputBaru);
            //TAMPUNG NILAI MATRIK BIAS BARU
            biasBaru = backpro.biasBaru(biasHiddenBaru, biasOutputBaru);
        }
        
        //==============================================================================================================================================
        for(int l=1;l<1;l++){
            System.out.println("=========");
            System.out.println("ITERASI "+(l+1));
            System.out.println("=========");
            
            //PERULANGAN DATA
            for (int j = 0; j < backpro.getJumlahData(normal); j++) {
                //TAMPILKAN Z_in
                System.out.println("");
                System.out.println("NILAI Z_in DATA " + (j + 1));
                z_in = backpro.z_in(normal, j, bobotHiddenBaru, biasBaru);
                for (int i = 0; i < z_in.length; i++) {
                    System.out.println(z_in[i]);
                }
                //TAMPILKAN Z
                System.out.println("");
                System.out.println("NILAI Z DATA " + (j + 1));
                z = backpro.z(z_in);
                for (int i = 0; i < z.length; i++) {
                    System.out.println(z[i]);
                }
                //TAMPILKAN Y_in
                System.out.println("");
                System.out.println("NILAI Y_in DATA " + (j + 1));
                y_in = backpro.y_in(z, bobotOutputBaru, biasBaru);
                System.out.println(y_in);
                //TAMPILKAN Y
                System.out.println("");
                System.out.println("NILAI Y DATA " + (j + 1));
                y = backpro.y(y_in);
                System.out.println(y);
                //TAMPILKAN error
                System.out.println("");
                System.out.println("NILAI Error DATA " + (j + 1));
                error = backpro.error(y, normal, j);
                System.out.println(error);
                //TAMPILKAN FAKTOR KOREKSI ERROR OUTPUT UNIT
                System.out.println("");
                System.out.println("NILAI Faktor Koreksi Error Output Unit DATA " + (j + 1));
                koreksiOutput = backpro.koreksiOutput(error, y);
                System.out.println(koreksiOutput);
                //TAMPILKAN PERUBAHAN BOBOT OUTPUT
                System.out.println("");
                System.out.println("PERUBAHAN BOBOT OUTPUT UNIT DATA " + (j + 1));
                deltaBobotOutput = backpro.deltaBobotOutput(koreksiOutput, z);
                for (int i = 0; i < deltaBobotOutput.length; i++) {
                    System.out.println(deltaBobotOutput[i]);
                }
                //TAMPILKAN PERUBAHAN BIAS OUTPUT
                System.out.println("");
                System.out.println("PERUBAHAN BIAS OUTPUT UNIT DATA " + (j + 1));
                deltaBiasOutput = backpro.deltaBiasOutput(koreksiOutput);
                System.out.println(deltaBiasOutput);
                //TAMPILKAN FAKTOR KOREKSI error_in Hidden Unit
                System.out.println("");
                System.out.println("NILAI Faktor Koreksi_in Hidden Unit DATA " + (j + 1));
                koreksiHidden_in = backpro.koreksiHidden_in(koreksiOutput, bobotOutputBaru);
                for (int i = 0; i < koreksiHidden_in.length; i++) {
                    System.out.println(koreksiHidden_in[i]);
                }
                //TAMPILKAN FAKTOR KOREKSI HIDDEN UNIT
                System.out.println("");
                System.out.println("NILAI Faktor Koreksi Hidden Unit DATA " + (j + 1));
                koreksiHidden = backpro.koreksiHidden(z, koreksiHidden_in);
                for (int i = 0; i < koreksiHidden.length; i++) {
                    System.out.println(koreksiHidden[i]);
                }
                //TAMPILKAN PERUBAHAN BOBOT HIDDEN UNIT
                System.out.println("");
                System.out.println("PERUBAHAN BOBOT HIDDEN UNIT DATA " + (j + 1));
                deltaBobotHidden = backpro.deltaBobotHidden(koreksiHidden, normal, j, bobotHiddenBaru);
                for (int i = 0; i < deltaBobotHidden.length; i++) {
                    for (int k = 0; k < deltaBobotHidden[i].length; k++) {
                        System.out.print(deltaBobotHidden[i][k] + " ");
                    }
                    System.out.println("");
                }
                //TAMPILKAN PERUBAHAN BIAS HIDDEN UNIT
                System.out.println("");
                System.out.println("PERUBAHAN BIAS HIDDEN UNIT DATA " + (j + 1));
                deltaBiasHidden = backpro.deltaBiasHidden(koreksiHidden);
                for (int i = 0; i < deltaBiasHidden.length; i++) {
                    System.out.println(deltaBiasHidden[i]);
                }
                //UPDATE BOBOT HIDDEN UNIT BARU
                System.out.println("");
                System.out.println("BOBOT HIDDEN UNIT BARU DATA " + (j + 1));
                bobotHiddenBaru = backpro.bobotHiddenBaru(deltaBobotHidden, j, bobotHiddenBaru);
                for (int i = 0; i < bobotHiddenBaru.length; i++) {
                    for (int k = 0; k < bobotHiddenBaru[i].length; k++) {
                        System.out.print(bobotHiddenBaru[i][k] + " ");
                    }
                    System.out.println("");
                }
                //UPDATE BIAS HIDDEN UNIT BARU
                System.out.println("");
                System.out.println("BIAS HIDDEN UNIT BARU DATA " + (j + 1));
                biasHiddenBaru = backpro.biasHiddenBaru(deltaBiasHidden, biasHiddenBaru);
                for (int i = 0; i < biasHiddenBaru.length; i++) {
                    System.out.println(biasHiddenBaru[i]);
                }
                //UPDATE BOBOT OUTPUT UNIT BARU
                System.out.println("");
                System.out.println("BOBOT OUTPUT UNIT BARU DATA " + (j + 1));
                bobotOutputBaru = backpro.bobotOutputBaru(deltaBobotOutput, bobotOutputBaru);
                for (int i = 0; i < bobotOutputBaru.length; i++) {
                    System.out.println(bobotOutputBaru[i]);
                }
                //UPDATE BIAS OUTPUT UNIT BARU
                System.out.println("");
                System.out.println("BIAS OUTPUT UNIT BARU DATA " + (j + 1));
                biasOutputBaru = backpro.biasOutputBaru(deltaBiasOutput, biasBaru);
                System.out.println(biasOutputBaru);
                //TAMPUNG NILAI MATRIK BIAS BARU
                biasBaru = backpro.biasBaru(biasHiddenBaru, biasOutputBaru);
                
               
                
            }
        }
        System.out.println("");
        System.out.println("MATRIK BOBOT DAN BIAS TERBARU ");
        double[][] bobotBaru = backpro.bobotBaru(bobotHiddenBaru, bobotOutputBaru, biasBaru);
        for(int i=0;i<bobotBaru.length;i++){
            for(int j=0;j<bobotBaru[0].length;j++){
                System.out.print(bobotBaru[i][j]+" ");
                
            }
            System.out.println(""); 
        }
        
         try {
                    String que = "insert into bobot_baru (v0,v1,v2,v3,v4,v5,w)"+" values (?,?,?,?,?,?,?)";
                    java.sql.Connection conn=(Connection)Connect.configDB();
                    PreparedStatement pst = conn.prepareStatement(que);
                    for (int l = 0; l < bobotBaru.length; l++) {
                        for (int k = 0; k < bobotBaru[0].length; k++) {
                        pst.setDouble(1, bobotBaru[l][0]);
                        pst.setDouble(2, bobotBaru[l][1]);
                        pst.setDouble(3, bobotBaru[l][2]);
                        pst.setDouble(4, bobotBaru[l][3]);
                        pst.setDouble(5, bobotBaru[l][4]);
                        pst.setDouble(6, bobotBaru[l][5]);
                        pst.setDouble(7, bobotBaru[l][6]);
                        pst.executeUpdate();
                        }
                
            }
                    
                    
                } catch (Exception e) {
                }
    }

}