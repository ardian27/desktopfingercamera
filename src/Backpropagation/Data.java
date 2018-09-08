package Backpropagation;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.omg.CORBA.DynAny;
/**
 *
 * @author angsakumisan
 */
public class Data {
    
    private Statement stat ;
    PreparedStatement prep;
    private Connection konek;
    private String url = "jdbc:mysql://localhost/dificam";
    private ResultSet res;
    
    int jumlahData=50;
    
     double data [][] = new double[jumlahData][6];
     double data_asli [][] = new double[jumlahData][6];
     int target [] = new int[jumlahData];
     double [] dataa,m,v,s,k,e,tr;
    
    
    ArrayList<Double> mm= new ArrayList<Double>();
    ArrayList<Double> vv= new ArrayList<Double>();
    ArrayList<Double> ss= new ArrayList<Double>();
    ArrayList<Double> kk= new ArrayList<Double>();
    ArrayList<Double> ee= new ArrayList<Double>();
    ArrayList<Integer> trr= new ArrayList<Integer>();
    
    
    ArrayList<Double> x1= new ArrayList<Double>();
    ArrayList<Double> x2= new ArrayList<Double>();
    ArrayList<Double> x3= new ArrayList<Double>();
    ArrayList<Double> x4= new ArrayList<Double>();
    ArrayList<Double> x5= new ArrayList<Double>();
    ArrayList<Integer> xt= new ArrayList<Integer>();
  
  
    public Data(){
        koneksi();
       
        //data = new int[60];
        
        int i = 0;
        try{
            res = stat.executeQuery("SELECT * FROM tbl_pelatihan");
            while((res.next())){
                mm.add(res.getDouble("mean"));
                vv.add(res.getDouble("variance"));
                ss.add(res.getDouble("skewness"));
                kk.add(res.getDouble("kurtosis"));
                ee.add(res.getDouble("entrophy"));
                trr.add(res.getInt("id_pengguna"));
                
              //  System.out.println("m"+mm.get(i)+"v"+vv.get(i)+"s"+ss.get(i)+"k"+kk.get(i)+"e"+ee.get(i)+"t"+trr.get(i));
                i++;
            }
            
            stat.close();
            Function fc = new Function();
            
                double minM = Collections.min(mm);
                double maxM = Collections.max(mm);
                double minV = Collections.min(vv);
                double maxV = Collections.max(vv);
                double minS = Collections.min(ss);
                double maxS = Collections.max(ss);
                double minK = Collections.min(kk);
                double maxK = Collections.max(kk);
                double minE = Collections.min(ee);
                double maxE = Collections.max(ee);
                double minT = Collections.min(trr);
                double maxT = Collections.max(trr);
                
             setMinMax(minM, maxM, minV, maxV, minS, maxS, minK, maxK, minE, maxE);
                 
                
            DecimalFormat df = new DecimalFormat("#.#####");
            for(int a = 0; a<mm.size(); a++){
                
                double ex1 = fc.normalisasi(mm.get(a),minM,maxM);
                double ex2 = fc.normalisasi(vv.get(a),minV,maxV);
                double ex3 = fc.normalisasi(ss.get(a),minS,maxS);
                double ex4 = fc.normalisasi(kk.get(a),minK,maxK);
                double ex5 = fc.normalisasi(ee.get(a),minE,maxE);
                
                x1.add(Double.valueOf(df.format(ex1)));
                
                x2.add(Double.valueOf(df.format(ex2)));
                
                x3.add(Double.valueOf(df.format(ex3)));
                
                x4.add(Double.valueOf(df.format(ex4)));
                
                x5.add(Double.valueOf(df.format(ex5)));
                
                xt.add(trr.get(a));   
                
            }
            
            //data latih
            for (int j = 0; j < jumlahData; j++) {
                data[j][0]=x1.get(j);
                data[j][1]=x2.get(j);
                data[j][2]=x3.get(j);
                data[j][3]=x4.get(j);
                data[j][4]=x5.get(j);
                data[j][5]=xt.get(j);
                //System.out.println("nilai mean ke "+j+"="+data[j][0]);
            }
            
            System.out.println("Jumlah Data Di Database = "+mm.size());
            
            // data asli
            for (int j = 0; j < jumlahData; j++) {
                data_asli[j][0]=(Double.valueOf(df.format(mm.get(j))));
                data_asli[j][1]=(Double.valueOf(df.format(vv.get(j))));
                data_asli[j][2]=(Double.valueOf(df.format(ss.get(j))));
                data_asli[j][3]=(Double.valueOf(df.format(kk.get(j))));
                data_asli[j][4]=(Double.valueOf(df.format(ee.get(j))));
                data_asli[j][5]=(Double.valueOf(df.format(trr.get(j))));
                target[j]=(Integer.valueOf(df.format(trr.get(j))));
            }
            
            /*
                System.out.println("m"+data[0][0]);
                System.out.println("v"+data[0][1]);
                System.out.println("s"+data[0][2]);
                System.out.println("k"+data[0][3]);
                System.out.println("e"+data[0][4]);
                System.out.println("t"+data[0][5]);
            */
            
        }
        catch(Exception e){
        }
    }
    
    
    public void setJumlahData(int jumlahdata){
        jumlahData=jumlahdata;
        System.out.println("Data Berhasil di set");
    }
    
    public int getJumlahData(){
        return jumlahData;
    }
    
    
    public double[][] getDataLatih(){
        System.out.println("Jumlah Data Latih yg diambil = "+data.length);
        return data;
    }
    
    
    
    public double[][] getDataAsli(){
        
        return data_asli;
    }
    
    public int [] target(){
    return target;
    }
    
    public double [][] getBobotFromDB(){
        koneksi();
        double [][] tampung = new double[6][6];
        int i = 0;
        try{
            
            res = stat.executeQuery("SELECT * FROM bobot");
            while((res.next())){
                tampung [i][0] = res.getDouble("v0");
                tampung [i][1] = res.getDouble("v1");
                tampung [i][2] = res.getDouble("v2");
                tampung [i][3] = res.getDouble("v3");
                tampung [i][4] = res.getDouble("v4");
                tampung [i][5] = res.getDouble("w");
                i++;
            }
           stat.close();
           
        }catch(Exception e){
        }
        
        
        //System.out.println("bobot get v01"+tampung[0][2]);
        return tampung;
    }
    
    public double [][] getBobotHidden(){
        double bobotDB[][] = getBobotFromDB();
        //double bobotDB[][] = bobotArray;
        
        double[][] bobotHidden = new double[bobotDB.length - 1][bobotDB[0].length - 1];
        
        for (int ii = 0; ii < bobotHidden.length; ii++) {
            for (int j = 0; j < bobotHidden[0].length; j++) {
                bobotHidden[ii][j] = bobotDB[ii][j];
                //System.out.println(""+bobotHidden[ii][j]);
            }
        }
        
        return bobotHidden;
    }
    public double [][] getBobotHiddenBPNN(double [][] bobotArray){
        //double bobotDB[][] = getBobotFromDB();
        double bobotDB[][] = bobotArray;
        
        double[][] bobotHidden = new double[bobotDB.length - 1][bobotDB[0].length - 1];
        
        for (int ii = 0; ii < bobotHidden.length; ii++) {
            for (int j = 0; j < bobotHidden[0].length; j++) {
                bobotHidden[ii][j] = bobotDB[ii][j];
                //System.out.println(""+bobotHidden[ii][j]);
            }
        }
        
        return bobotHidden;
    }
    
    public double [] getBobotOutput(){
        double bobotDB[][] = getBobotFromDB();
        //double bobotDB[][] = bobotArray;
        double[] bobotOutput = new double[bobotDB.length - 1];
         
        for (int m= 0; m < bobotOutput.length; m++) {
            bobotOutput[m] = bobotDB[m][5];
        }
        
        return bobotOutput;
    }    
    
    public double [] getBobotOutputBPNN(double [][] bobotArray){
        //double bobotDB[][] = getBobotFromDB();
        double bobotDB[][] = bobotArray;
        double[] bobotOutput = new double[bobotDB.length - 1];
         
        for (int m= 0; m < bobotOutput.length; m++) {
            bobotOutput[m] = bobotDB[m][5];
        }
        
        return bobotOutput;
    }
    
    public double [] getBias (){
         double bobotDB[][] = getBobotFromDB();
         //double bobotDB[][] = bobotArray;
         double[] bias = bobotDB[bobotDB.length - 1];

         return bias;
    }
    public double [] getBiasBPNN (double [][] bobotArray){
         //double bobotDB[][] = getBobotFromDB();
         double bobotDB[][] = bobotArray;
         double[] bias = bobotDB[bobotDB.length - 1];

         return bias;
    }
    
    public double [][] getCompareUji(){
        koneksi();
        double [][] dataaa = new double[jumlahData][jumlahData];
        
        String dataUji = "SELECT * FROM temp_y";
        try {
            res = stat.executeQuery(dataUji);
            int i=0;
            while((res.next())){
                 dataaa [i][0] = res.getDouble("y");
                 dataaa [i][1] = res.getDouble("t");
                 i++;
            }
        
            
        } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return dataaa;
    }
    
    public void getUser(double nilai_y){
        koneksi();
        
        String user = "select b.nama from temp_y as a , tbl_pengguna as b where a.y = "+nilai_y+" and a.t=b.id_pengguna";
        String userr [] = new String[1];
        
        try {
            res = stat.executeQuery(user);
            while((res.next())){
                 
                userr[0]= res.getString("b.nama");
                
            }
            
            
           System.out.println("nilai user = "+userr[0]+"");
        } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void  setMinMax (double minm ,double maxm,double minv,double maxv,double mins,double maxs,double mink,double maxk,double mine,double maxe){
        koneksi();
        String minmax = "Insert into min_max values (null,"+minm+","+maxm+","+minv+","+maxv+","+mins+","+maxs+","+mink+","+maxk+","+mine+","+maxe+")";
    
        String tc = "truncate table min_max";
        try {
            PreparedStatement preparedStatement = konek.prepareStatement(minmax);
            PreparedStatement preparedStatement1 = konek.prepareStatement(tc);
            preparedStatement1.execute();
            preparedStatement.execute();
            
            preparedStatement.close();
            
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    public void setAllUji(double alfaa){
        double [][]data = getDataLatih();
        
        bpnn bp = new bpnn(alfaa);
        
        double [][] xx = new double[data.length][data[0].length-1];
        double [][] bobothidden = getBobotHidden();
        double [] bobotbias = getBias();
        double [] bobotoutput = getBobotOutput();
        double [][] target = getDataLatih();
        
        double nilaiUjiAll [] = new double[jumlahData];
        
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j<data[0].length-1; j++) {   
              xx[i][j]=data[i][j];
            }
            double singleData[][] = new double[1][5];
            for (int j = 0; j < singleData[0].length; j++) {
                singleData[0][j]=xx[i][j];
            }
            nilaiUjiAll[i]=bp.feedForward(singleData, 0, bobothidden, bobotbias, bobotoutput);
            //System.out.println("Nilai uji data ke = "+i+"="+nilaiUjiAll[i]);
        }
        
        koneksi();
        String truncate = "TRUNCATE TABLE temp_y";
        String que = "insert into temp_y (id_y,y,t)"+" values (?,?,?)";
        
        
        
        try {
            PreparedStatement del = konek.prepareStatement(truncate);
            del.execute();
            
            PreparedStatement ins = konek.prepareStatement(que);
                    for (int l = 0; l < nilaiUjiAll.length; l++) {
                        
                        ins.setInt(1, l+1);
                        ins.setDouble(2, nilaiUjiAll[l]);
                        ins.setDouble(3, target[l][5]);
                        ins.executeUpdate();
                    }
            
                    } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
                    
        
    }
    
    
    public void koneksi(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            konek = DriverManager.getConnection(url,"root","");
            stat = konek.createStatement();
        }
        catch(Exception e){
        }
    }
    
//    public double[][] aturData (int[] data){
//        double[][] dataBaru = new double[data.length/7][7];
//        for(int i=0;i<dataBaru.length;i++){
//            for(int j=0;j<dataBaru[i].length;j++){
//                dataBaru[i][j] = (double)data[j%dataBaru[i].length+i*dataBaru[i].length];
//            }
//        }
//        return dataBaru;
//    }
//    
    public static void main(String[] args) {
        Data data = new Data();
     
//        
    }
    
}

