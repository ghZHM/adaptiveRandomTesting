package com.company;


import com.company.partition.StaticPartition;
import com.company.searchBased.HillClimbing;
import com.company.selectFromCandidate.TestCaseSelection;
import com.company.selectFromCandidate.candidateSetGenerator;
import com.company.utils.MetricsCalculator;
import com.company.utils.myRandom;

import java.io.IOException;
import java.util.*;

public class Main {

    static float failureRate = (float) 0.01;
    static double Xmin = 0;
    static double Xmax = Integer.MAX_VALUE;
    static double Ymin = 0;
    static double Ymax = Integer.MAX_VALUE;
    public static void main(String[] args) throws IOException {
        myRandom myRandomInstance = new myRandom();

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
        //partitioning strategy
        List<Integer> partitionFmeasureList = new LinkedList<>();
        List<Long> partitionGenerateTime = new LinkedList<>();
        List<HashMap> partitionMetricsList = new LinkedList<>();
        //search based
        List<Integer> searchBasedFmeasureList = new LinkedList<>();
        List<Long> searchBasedGenerateTime = new LinkedList<>();
        List<HashMap> searchBasedMetricsList = new LinkedList<>();

        // multiple run
        int runCount=0;
        while(runCount<500)
        {
            System.out.println("Run No."+runCount);
            //baseline
            long baselineTime = System.currentTimeMillis();
            BaselineRandomTest baseline = new BaselineRandomTest();
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
            baselineGenerateTime.add(System.currentTimeMillis()-baselineTime);
            baselineMetricsList.add(calculator.getMetrics(baselineSet));
            baselineFmeasureList.add(baselineSet.size());

            //Select from Candidate
            long curTime = System.currentTimeMillis();
            candidateSetGenerator cSetGenerator = new candidateSetGenerator();
            TestCaseSelection testCaseSelector = new TestCaseSelection();
            HashSet<List> executedSet= new HashSet<>();
            while (true)
            {
                double tcX = myRandomInstance.random(Xmin,Xmax);
                double tcY = myRandomInstance.random(Ymin,Ymax);
                if (updateExecutedSet(centerX, centerY, edge, executedSet, tcX, tcY)) break;
            }
            System.out.println("select from candidate.");
            while(true)
            {
                HashSet<List> candidate = cSetGenerator.nonUniformDistribution(Xmin,Xmax,Ymin,Ymax);
                List<Double> testCase = testCaseSelector.avgDistanceBased(candidate,executedSet);
                double tcX = testCase.get(0);
                double tcY = testCase.get(1);
                if (!updateExecutedSet(centerX, centerY, edge, executedSet, tcX, tcY)) break;
            }
            // calculate and record metrics
            selectFromCandidateGenerateTime.add(System.currentTimeMillis()-curTime);
            selectFromCandidateFmeasureList.add(executedSet.size());
            HashMap metrics=calculator.getMetrics(executedSet);
            selectFromCandidateMetricsList.add(metrics);

            // partitioning strategy
            System.out.println("Partitioning based strategy");
            long partitionStartTime = System.currentTimeMillis();
            StaticPartition myPartition = new StaticPartition((int)Math.sqrt(1/failureRate));
            HashSet<Double> executedSetPartition =  myPartition.generator(Xmin,Xmax,Ymin,Ymax,centerX,centerY,edge);
            partitionGenerateTime.add(System.currentTimeMillis()-partitionStartTime);
            HashMap partitionMetric = calculator.getMetrics(executedSetPartition);
            partitionMetricsList.add(partitionMetric);
            partitionFmeasureList.add(executedSetPartition.size());

            // Search based
            System.out.println("Hill Climbing");
            long hcStartTime = System.currentTimeMillis();
            HillClimbing hillClimbing = new HillClimbing(Xmin,Xmax,Ymin,Ymax);
            HashSet<List> executedSetSearch = hillClimbing.runHillClimbing(Xmin,Xmax,Ymin,Ymax,centerX,centerY,edge);
            searchBasedGenerateTime.add(System.currentTimeMillis()-hcStartTime);
            searchBasedFmeasureList.add(executedSetSearch.size());
            searchBasedMetricsList.add(calculator.getMetrics(executedSetSearch));

            runCount++;
        }

        //write metrics into a csv file.
        calculator.metricsOutput(baselineMetricsList,baselineGenerateTime,baselineFmeasureList,"baselineMetrics.csv");
        calculator.metricsOutput(selectFromCandidateMetricsList,selectFromCandidateGenerateTime,selectFromCandidateFmeasureList,"selectMetrics.csv");
        calculator.metricsOutput(partitionMetricsList,partitionGenerateTime,partitionFmeasureList,"partitionMetrics.csv");
        calculator.metricsOutput(searchBasedMetricsList,searchBasedGenerateTime,searchBasedFmeasureList,"searchBasedMetrics.csv");


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
