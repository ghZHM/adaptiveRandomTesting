package com.company;


import com.company.utils.myRandom;

public class Main {

    static float failureRate = (float) 0.002;
    static int Xmin = 0;
    static int Xmax = Integer.MAX_VALUE;
    static int Ymin = Integer.MIN_VALUE;
    static int Ymax = Integer.MAX_VALUE;
    public static void main(String[] args) {
	    // Generate failure area - block
        while (true)
        {
            float centerX = myRandom.random(Xmin,Xmax);
            float centerY = myRandom.random(Ymin,Ymax);
            float area = (Xmax-Xmin)*(Ymax-Ymin)*failureRate;
            float edge = (float) Math.sqrt(area);
            if((centerX+edge/2<=Xmax)&&(centerY+edge/2<=Ymax)&&(centerX-edge/2>=Xmin)&&(centerY-edge/2>=Ymin))
                break;
        }






    }
}
