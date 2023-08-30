package com.company;


import com.company.partition.StaticPartition;
import com.company.searchBased.HillClimbing;
import com.company.selectFromCandidate.TestCaseSelection;
import com.company.selectFromCandidate.candidateSetGenerator;
import com.company.utils.MetricsCalculator;
import com.company.utils.myRandom;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
//        double centerX = 0;
//        double centerY = 0;
//        double edge = 0;
//        while (true)
//        {
//            centerX = myRandomInstance.random(Xmin,Xmax);
//            centerY = myRandomInstance.random(Ymin,Ymax);
//            double area = (Xmax-Xmin)*(Ymax-Ymin)*failureRate;
//            edge = Math.sqrt(area);
//            if((centerX+edge/2<=Xmax)&&(centerY+edge/2<=Ymax)&&(centerX-edge/2>=Xmin)&&(centerY-edge/2>=Ymin))
//                break;
//        }
//        System.out.println("Failure area-block generated.");

        //generate failure area - strip

        HashMap strip = stripGenerator();

        System.out.println("Failure area-strip generated.");
        System.out.println(strip.get("k-value")+",b1:"+strip.get("b1")+",b2:"+strip.get("b2"));

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
        while(runCount<800)
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
//                if (updateExecutedSet(centerX, centerY, edge, baselineSet, tcX, tcY)) break;
                if (checkStrip(baselineSet,tcX,tcY,strip)) break;
            }
            System.out.println("Start to build baseline.");
            while (true)
            {
                List<Double> temp= baseline.generator(Xmin,Xmax,Ymin,Ymax);
                double tempX = temp.get(0);
                double tempY = temp.get(1);
//                if (!updateExecutedSet(centerX, centerY, edge, baselineSet, tempX, tempY)) break;
                if (!checkStrip(baselineSet,tempX,tempY,strip)) break;
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
//                if (updateExecutedSet(centerX, centerY, edge, executedSet, tcX, tcY)) break;
                if (checkStrip(executedSet,tcX,tcY,strip)) break;
            }
            System.out.println("select from candidate.");
            while(true)
            {
                HashSet<List> candidate = cSetGenerator.nonUniformDistribution(Xmin,Xmax,Ymin,Ymax);
                List<Double> testCase = testCaseSelector.avgDistanceBased(candidate,executedSet);
                double tcX = testCase.get(0);
                double tcY = testCase.get(1);
//                if (!updateExecutedSet(centerX, centerY, edge, executedSet, tcX, tcY)) break;
                if (!checkStrip(executedSet,tcX,tcY,strip)) break;
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
//            HashSet<Double> executedSetPartition =  myPartition.generator(Xmin,Xmax,Ymin,Ymax,centerX,centerY,edge);
            HashSet<Double> executedSetPartition =  myPartition.generator(Xmin,Xmax,Ymin,Ymax,strip);
            partitionGenerateTime.add(System.currentTimeMillis()-partitionStartTime);
            HashMap partitionMetric = calculator.getMetrics(executedSetPartition);
            partitionMetricsList.add(partitionMetric);
            partitionFmeasureList.add(executedSetPartition.size());

            // Search based
            System.out.println("Hill Climbing");
            long hcStartTime = System.currentTimeMillis();
            HillClimbing hillClimbing = new HillClimbing(Xmin,Xmax,Ymin,Ymax);
//            HashSet<List> executedSetSearch = hillClimbing.runHillClimbing(Xmin,Xmax,Ymin,Ymax,centerX,centerY,edge);
            HashSet<List> executedSetSearch = hillClimbing.runHillClimbing(Xmin,Xmax,Ymin,Ymax,strip);
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
        List<Double> firstCase = new LinkedList<>();
        firstCase.add(tcX);
        firstCase.add(tcY);
        executedSet.add(firstCase);
        if(!((tcX>=centerX-edge/2&&tcX<=centerX+edge/2)&&(tcY>=centerY-edge/2&&tcY<=centerY+edge/2)))
        {
            return true;
        }
        return false;
    }

    private static HashMap<String,Double> stripGenerator()
    {
        HashMap<String,Double> ret = new HashMap<>();
        // store the generated region, k-value and b for 2 line

        // randomly pick the adjacent border
        Random random = new Random();
        int edgeGroup = random.nextInt(4);
        // 0 - x & y axis, 1 - x axis & x=Integer.MAX_VALUE, 2 - x=Integer.MAX_VALUE & y=Integer.MAX_VALUE, 3 - y axis & y=Integer.MAX_VALUE
        System.out.println(edgeGroup);
        double area = failureRate*(Xmax-Xmin)*(Ymax-Ymin);

        myRandom myRandom = new myRandom();
        if(edgeGroup==0)
        {
            double x1,b1;
            while (true)
            {
                x1 = myRandom.random(Xmin,Xmax);
                b1 = myRandom.random(Ymin,Ymax);
                if(x1!=Xmax&&x1!=0&&b1!=Ymax&&b1!=0)
                {
                    break;
                }
            }

            double k = -b1/x1;
            ret.put("k-value",k);
            ret.put("b1",b1);
            double b2=Math.sqrt(Math.abs(Math.pow(b1,2)-(2*b1*area/x1)));
            ret.put("b2",b2);
        }
        else if(edgeGroup==1)
        {
            while (true)
            {
                double x,y;
                do {
                    x = myRandom.random(Xmin, Xmax);
                    y = myRandom.random(Ymin, Ymax);
                } while (x == Xmax || x == 0 || y == Ymax || y == 0);
                double k = y/(Xmax-x);
                ret.put("k-value",k);
                ret.put("b1",-1*x*k);
                double a=1/k;
                double b = 2*Xmax;
                double c = k*Math.pow(Xmax,2)+2*area-y*(Xmax-x);
                double b2 = (-b+Math.sqrt(Math.pow(b,2)-4*a*c))/(2*a);
                if((b2+k*Xmax>0&&b2+k*Xmax<Ymax))
                {
                    ret.put("b2",b2);
                    break;
                }
                else
                {
                    b2= (-b-Math.sqrt(Math.pow(b,2)-4*a*c))/(2*a);
                }
                if(b2+k*Xmax>0&&b2+k*Xmax<Ymax)
                {
                    ret.put("b2",b2);
                    break;
                }
            }

        }
        else if (edgeGroup==2)
        {
            while (true)
            {
                double x2,y2;
                do {
                    x2 = myRandom.random(Xmin, Xmax);
                    y2 = myRandom.random(Ymin, Ymax);
                } while (x2 == Xmax || x2 == 0 || y2 == Ymax || y2 == 0);
                double k = (Ymax-y2)/(x2-Xmax);
                ret.put("k-value",k);
                double b1 = Ymax-k*x2;
                ret.put("b1",b1);
//                double b2 = Math.sqrt(k*Math.abs(2*area-(Xmax-x2)*(Ymax-y2)))+Ymax-k*Xmax;
//                ret.put("b2",b2);
                double a = 1/k;
                double b = 2*(k*Xmax-Ymax)/k;
                double c= Math.pow(k*Xmax-Ymax,2)/k+(Xmax-x2)*(Ymax-y2)+2*area;
                double b2 = (-b+Math.sqrt(Math.pow(b,2)-4*a*c))/(2*a);
                if((b2+k*Xmax>0&&b2+k*Xmax<Ymax))
                {
                    ret.put("b2",b2);
                    break;
                }
                else
                {
                    b2= (-b-Math.sqrt(Math.pow(b,2)-4*a*c))/(2*a);
                }
                if(b2+k*Xmax>0&&b2+k*Xmax<Ymax)
                {
                    ret.put("b2",b2);
                    break;
                }
            }

        }
        else
        {
            while (true)
            {
                double x3,y3;
                do {
                    x3 = myRandom.random(Xmin, Xmax);
                    y3 = myRandom.random(Ymin, Ymax);
                } while (x3 == Xmax || x3 == 0 || y3 == Ymax || y3 == 0);
                ret.put("k-value",(Ymax-y3)/x3);
                double k =(Ymax-y3)/x3;
                ret.put("b1",y3);
                double a = 1/k;
                double b = (-2*Ymax)/k;
                double c= Math.pow(Ymax,2)/k+2*area-x3*(Ymax-y3);
                double b2 = (-b+Math.sqrt(Math.pow(b,2)-4*a*c))/(2*a);
                if((b2>0&&b2<Ymax))
                {
                    ret.put("b2",b2);
                    break;
                }
                else
                {
                    b2= (-b-Math.sqrt(Math.pow(b,2)-4*a*c))/(2*a);
                }
                if(b2>0&&b2<Ymax)
                {
                    ret.put("b2",b2);
                    break;
                }
            }

        }
        return ret;
    }

    private static boolean checkStrip(HashSet<List> executedSet, double tcX, double tcY, HashMap failureRegion)
    {
        List<Double> firstCase = new LinkedList<>();
        firstCase.add(tcX);
        firstCase.add(tcY);
        executedSet.add(firstCase);
        double upper = Math.max((double) failureRegion.get("b2"), (double) failureRegion.get("b1"));
        double lower = Math.min((double) failureRegion.get("b2"), (double) failureRegion.get("b1"));

        double k = (double)failureRegion.get("k-value");
        upper+= k*tcX;
        lower+= k*tcX;
        if(tcY>=lower&&tcY<=upper)
        {
            return true;
        }
        return false;
    }

}
