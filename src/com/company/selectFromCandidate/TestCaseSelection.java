package com.company.selectFromCandidate;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * @Author Haoming Zeng
 * @Date 2023/7/10 12:35
 * @classDescribe
 * @ perform selection from candidate-set
 */
public class TestCaseSelection {
    public TestCaseSelection(){

    }
    public List minimumDistanceBased(HashSet candidateSet, HashSet executedSet)
    {
        Iterator iterator = candidateSet.iterator();
        double minDis = Double.MAX_VALUE;
        List ret = null;
        while (iterator.hasNext())
        {
            double minDis2E = Double.MAX_VALUE;
            List<Double> candidateSetElement = (List<Double>) iterator.next();
            double x = candidateSetElement.get(0);
            double y = candidateSetElement.get(1);
            Iterator iterator4E = executedSet.iterator();
            while (iterator4E.hasNext())
            {
                List<Double> executedElement = (List<Double>) iterator4E.next();
                double distance = Math.sqrt(Math.pow(x-executedElement.get(0),2)+Math.pow(y-executedElement.get(1),2));
                if(distance<minDis2E)
                {
                    minDis2E=distance;
                }
            }
            if(minDis>minDis2E)
            {
                minDis=minDis2E;
                ret=candidateSetElement;
            }
        }
        return ret;
    }

    public List maximumDistanceBased(HashSet candidateSet, HashSet executedSet)
    {
        Iterator iterator = candidateSet.iterator();
        double maxDis = Double.MIN_VALUE;
        List ret = null;
        while (iterator.hasNext())
        {
            double maxDis2E = Double.MIN_VALUE;
            List<Double> candidateSetElement = (List<Double>) iterator.next();
            double x = candidateSetElement.get(0);
            double y = candidateSetElement.get(1);
            Iterator iterator4E = executedSet.iterator();
            while (iterator4E.hasNext())
            {
                List<Double> executedElement = (List<Double>) iterator4E.next();
                double distance = Math.sqrt(Math.pow(x-executedElement.get(0),2)+Math.pow(y-executedElement.get(1),2));
                if(distance>maxDis2E)
                {
                    maxDis2E=distance;
                }
            }
            if(maxDis<maxDis2E)
            {
                maxDis=maxDis2E;
                ret=candidateSetElement;
            }
        }
        return ret;
    }

}
