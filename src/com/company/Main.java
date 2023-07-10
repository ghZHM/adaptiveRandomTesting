package com.company;


import com.company.selectFromCandidate.candidateSetGenerator;
import com.company.utils.myRandom;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Main {

    static float failureRate = (float) 0.002;
    static double Xmin = 0;
    static double Xmax = Integer.MAX_VALUE;
    static double Ymin = 0;
    static double Ymax = Integer.MAX_VALUE;
    public static void main(String[] args) {
        myRandom myRandomInstance = new myRandom();
        candidateSetGenerator cSetGenerator = new candidateSetGenerator();
        // Generate failure area - block
        double centerX = 0;
        double centerY = 0;
        double edge = 0;
        while (true)
        {
            centerX = myRandomInstance.random(Xmin,Xmax);
            centerY = myRandomInstance.random(Ymin,Ymax);
            double area = (Xmax-Xmin)*(Ymax-Ymin)*failureRate;
            edge = Math.sqrt(area);
            if((centerX+edge/2<=Xmax)&&(centerY+edge/2<=Ymax)&&(centerX-edge/2>=Xmin)&&(centerY-edge/2>=Ymin))
                break;
        }

        // Execute Set init
        HashSet<List> executedSet= new HashSet<>();
        while (true)
        {
            double tcX = myRandomInstance.random(Xmin,Xmax);
            double tcY = myRandomInstance.random(Ymin,Ymax);
            if(!((tcX>=centerX-edge/2&&tcX<=centerX+edge/2)&&(tcY>=centerY-edge/2&&tcY<=centerY+edge/2)))
            {
                List<Double> firstCase = new LinkedList<>();
                firstCase.add(tcX);
                firstCase.add(tcY);
                executedSet.add(firstCase);
                break;
            }
        }
        HashSet<List> candidate = cSetGenerator.uniformDistribution(Xmin,Xmax,Ymin,Ymax);
        Iterator iterator = candidate.iterator();
        while (iterator.hasNext())
        {
            System.out.println(iterator.next().toString());
        }




    }
}
