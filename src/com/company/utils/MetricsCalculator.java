package com.company.utils;

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
        System.out.println("Copy complete, start to traverse");
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
        ret.put("divergence",divergence);
        ret.put("diversity",diversity);
        ret.put("dispersion",dispersion);
        return ret;
    }
}
