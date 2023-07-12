package com.company;


import com.company.selectFromCandidate.TestCaseSelection;
import com.company.selectFromCandidate.candidateSetGenerator;
import com.company.utils.MetricsCalculator;
import com.company.utils.myRandom;

import java.util.*;

public class Main {

    static float failureRate = (float) 0.002;
    static double Xmin = 0;
    static double Xmax = Integer.MAX_VALUE;
    static double Ymin = 0;
    static double Ymax = Integer.MAX_VALUE;
    public static void main(String[] args) {
        myRandom myRandomInstance = new myRandom();
        candidateSetGenerator cSetGenerator = new candidateSetGenerator();
        TestCaseSelection testCaseSelector = new TestCaseSelection();
        MetricsCalculator calculator = new MetricsCalculator();
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
        List<Integer> FmeasureList = new LinkedList<>();
        List<Long> GenerateTime = new LinkedList<>();
        // multiple run
        int runCount=0;
        while(runCount<30)
        {
            // Execute Set init
            long curTime = System.currentTimeMillis();
            HashSet<List> executedSet= new HashSet<>();
            while (true)
            {
                double tcX = myRandomInstance.random(Xmin,Xmax);
                double tcY = myRandomInstance.random(Ymin,Ymax);
                if (updateExecutedSet(centerX, centerY, edge, executedSet, tcX, tcY)) break;
            }
            // one run-generation in loop
            while(true)
            {
                HashSet<List> candidate = cSetGenerator.uniformDistribution(Xmin,Xmax,Ymin,Ymax);
                List<Double> testCase = testCaseSelector.maximumDistanceBased(candidate,executedSet);
                double tcX = testCase.get(0);
                double tcY = testCase.get(1);
                if (!updateExecutedSet(centerX, centerY, edge, executedSet, tcX, tcY)) break;
            }
            // calculate and record metrics
            GenerateTime.add(System.currentTimeMillis()-curTime);
            FmeasureList.add(executedSet.size());
            HashMap metrics=calculator.getMetrics(executedSet);
            System.out.println("No."+runCount+" , F-measure: "+executedSet.size());
            System.out.println("Diversity: "+metrics.get("diversity")+"\n divergence: "+metrics.get("divergence")+" \n dispersion:"+metrics.get("dispersion"));
            runCount++;
        }




    }

    private static boolean updateExecutedSet(double centerX, double centerY, double edge, HashSet<List> executedSet, double tcX, double tcY) {
        if(!((tcX>=centerX-edge/2&&tcX<=centerX+edge/2)&&(tcY>=centerY-edge/2&&tcY<=centerY+edge/2)))
        {
            List<Double> firstCase = new LinkedList<>();
            firstCase.add(tcX);
            firstCase.add(tcY);
            executedSet.add(firstCase);
            return true;
        }
        return false;
    }
}
