package com.company;


import com.company.selectFromCandidate.TestCaseSelection;
import com.company.selectFromCandidate.candidateSetGenerator;
import com.company.utils.MetricsCalculator;
import com.company.utils.myRandom;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    static float failureRate = (float) 0.002;
    static double Xmin = 0;
    static double Xmax = Integer.MAX_VALUE;
    static double Ymin = 0;
    static double Ymax = Integer.MAX_VALUE;
    public static void main(String[] args) throws IOException {
        myRandom myRandomInstance = new myRandom();
        candidateSetGenerator cSetGenerator = new candidateSetGenerator();
        TestCaseSelection testCaseSelector = new TestCaseSelection();
        MetricsCalculator calculator = new MetricsCalculator();
        BaselineRandomTest baseline = new BaselineRandomTest();
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
        System.out.println("Failure area-block generated.");

        // metric storage

        //baseline
        List<Integer> baselineFmeasureList = new LinkedList<>();
        List<Long> baselineGenerateTime = new LinkedList<>();
        List<HashMap> baselineMetricsList = new LinkedList<>();
        //selectFromCandidate
        List<Integer> selectFromCandidateFmeasureList = new LinkedList<>();
        List<Long> selectFromCandidateGenerateTime = new LinkedList<>();
        List<HashMap> selectFromCandidateMetricsList = new LinkedList<>();

        // multiple run
        int runCount=0;
        while(runCount<30)
        {
            //baseline
            long baselineTime = System.currentTimeMillis();
            HashSet<List> baselineSet = new HashSet<>();
            while (true)
            {
                double tcX = myRandomInstance.random(Xmin,Xmax);
                double tcY = myRandomInstance.random(Ymin,Ymax);
                if (updateExecutedSet(centerX, centerY, edge, baselineSet, tcX, tcY)) break;
            }
            System.out.println("Start to build baseline.");
            while (true)
            {
                List<Double> temp= baseline.generator(Xmin,Xmax,Ymin,Ymax);
                double tempX = temp.get(0);
                double tempY = temp.get(1);
                if (!updateExecutedSet(centerX, centerY, edge, baselineSet, tempX, tempY)) break;
            }
            System.out.println("Recording metrics of run No."+runCount);
            baselineGenerateTime.add(System.currentTimeMillis()-baselineTime);
            baselineMetricsList.add(calculator.getMetrics(baselineSet));
            baselineFmeasureList.add(baselineSet.size());

            //Select from Candidate
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
            System.out.println("generating test cases via select from candidate.");
            while(true)
            {
                HashSet<List> candidate = cSetGenerator.uniformDistribution(Xmin,Xmax,Ymin,Ymax);
                List<Double> testCase = testCaseSelector.maximumDistanceBased(candidate,executedSet);
                double tcX = testCase.get(0);
                double tcY = testCase.get(1);
                if (!updateExecutedSet(centerX, centerY, edge, executedSet, tcX, tcY)) break;
            }
            // calculate and record metrics
            selectFromCandidateGenerateTime.add(System.currentTimeMillis()-curTime);
            selectFromCandidateFmeasureList.add(executedSet.size());
            System.out.println("Start to calculate metrics.");
            HashMap metrics=calculator.getMetrics(executedSet);
            System.out.println("No."+runCount+" , F-measure: "+executedSet.size());
            System.out.println("Diversity: "+metrics.get("diversity")+"\n divergence: "+metrics.get("divergence")+" \n dispersion:"+metrics.get("dispersion"));
            selectFromCandidateMetricsList.add(metrics);
            runCount++;
        }

        //write metrics into a csv file.
        calculator.metricsOutput(baselineMetricsList,baselineGenerateTime,baselineFmeasureList,"baselineMetrics.csv");
        calculator.metricsOutput(selectFromCandidateMetricsList,selectFromCandidateGenerateTime,selectFromCandidateFmeasureList,"metricsOfSelect.csv");


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
