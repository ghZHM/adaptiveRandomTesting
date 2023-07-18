package com.company.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @Author Haoming Zeng
 * @Date 2023/7/12 15:56
 * @classDescribe A class used to calculate the metrics of executed set
 */
public class MetricsCalculator {
    public MetricsCalculator(){}

    public HashMap<String,Double> getMetrics(HashSet executedSet)
    {
        double divergence = 0;
        double diversity = 0;
        double dispersion = Double.MIN_VALUE;
        List []flatSet = new LinkedList[executedSet.size()];
        Iterator iterator = executedSet.iterator();
        int pos = 0;
        while (iterator.hasNext())
        {
            flatSet[pos] = (List) iterator.next();
            pos++;
        }
        for(int i=0;i<flatSet.length;i++)
        {
            double nearestNeighbor = Double.MAX_VALUE;
            for(int j=0;j<flatSet.length;j++)
            {
                if(i==j)
                {
                    continue;
                }
                double distanceTemp = Math.sqrt(Math.pow((double)flatSet[i].get(0)-(double)flatSet[j].get(0),2)+Math.pow((double)flatSet[i].get(1)-(double)flatSet[j].get(1),2));
                divergence+=distanceTemp;
                if(nearestNeighbor>distanceTemp)
                {
                    nearestNeighbor=distanceTemp;
                }
            }
            diversity+=nearestNeighbor;
            if(dispersion<nearestNeighbor)
            {
                dispersion=nearestNeighbor;
            }
        }
        HashMap<String,Double> ret = new HashMap<>();
        diversity = diversity/flatSet.length;
        if(flatSet.length>1)
        {
            divergence = divergence/(flatSet.length*(flatSet.length-1)/2);
        }
        ret.put("divergence",divergence);
        ret.put("diversity",diversity/flatSet.length);
        ret.put("dispersion",dispersion);
        return ret;
    }

    public void metricsOutput(List<HashMap> metricsList, List timeList, List FmeasureList,String filename) throws IOException {
        String metricOutput = "Run, F-measure, Diversity, Divergence, Dispersion, generationTime \n";
        for(int i=0;i<metricsList.size();i++) // get every class from the HashMap
        {
            BufferedWriter fw = new BufferedWriter(new FileWriter(filename));
            metricOutput+=i+","+ FmeasureList.get(i)+","+metricsList.get(i).get("diversity")+","+metricsList.get(i).get("divergence")+","+metricsList.get(i).get("dispersion")+","+timeList.get(i)+"\n";
            fw.write(metricOutput);
            fw.flush();
            fw.close();
        }
    }
}
