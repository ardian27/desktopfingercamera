
import Backpropagation.Data;
import Backpropagation.bpnn;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author angsakumisan
 */
public class Test2 {
    
    public static void main(String[] args) {
        
        Data data = new Data();
        bpnn bp = new bpnn(0.1);
        
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
        
        double y = bp.feedForward(normal, 0, bh, bias, bo);
        
        System.out.println("nilai forward  = "+y);
        
        
        
        double [][] compareUji = data.getCompareUji();
        
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
        System.out.println(nearest);
        data.getUser(nearest);
        
       
    }
}
