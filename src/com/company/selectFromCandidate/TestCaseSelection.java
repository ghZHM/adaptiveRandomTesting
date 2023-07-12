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
    public List avgDistanceBased(HashSet candidateSet, HashSet executedSet)
    {
        Iterator iterator = candidateSet.iterator();
        double maxDis = Double.MIN_VALUE;
        List ret = null;
        while (iterator.hasNext())
        {
            double avgDistance = 0;
            List<Double> candidateSetElement = (List<Double>) iterator.next();
            double x = candidateSetElement.get(0);
            double y = candidateSetElement.get(1);
            Iterator iterator4E = executedSet.iterator();
            while (iterator4E.hasNext())
            {
                List<Double> executedElement = (List<Double>) iterator4E.next();
                double distance = Math.sqrt(Math.pow(x-executedElement.get(0),2)+Math.pow(y-executedElement.get(1),2));
                avgDistance+=distance;
            }
            if(maxDis<avgDistance)
            {
                maxDis=avgDistance;
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

    public List gravityDistanceBased(HashSet candidateSet, HashSet executedSet)
    {
        Iterator iterator = candidateSet.iterator();
        double maxDis = Double.MIN_VALUE;
        List ret = null;
        List[] executedSetArray = (List[]) executedSet.toArray();
        double gravityX= 0;
        double gravityY = 0;
        if(executedSetArray.length==1)
        {
            gravityX= (double) executedSetArray[0].get(0);
            gravityY= (double) executedSetArray[0].get(1);
        }
        else if(executedSetArray.length==2)
        {
            gravityX= ((double) executedSetArray[0].get(0)+(double) executedSetArray[1].get(0))/2;
            gravityY= ((double) executedSetArray[0].get(1)+(double) executedSetArray[1].get(1))/2;
        }
        else
        {
            double area =0;
            for(int i=1;i<executedSetArray.length;i++)
            {
                
            }
        }
        while (iterator.hasNext())
        {
            double maxDis2E = Double.MIN_VALUE;
            List<Double> candidateSetElement = (List<Double>) iterator.next();
            double x = candidateSetElement.get(0);
            double y = candidateSetElement.get(1);
//            Iterator iterator4E = executedSet.iterator();
//            while (iterator4E.hasNext())
//            {
//                List<Double> executedElement = (List<Double>) iterator4E.next();
//                double distance = Math.sqrt(Math.pow(x-executedElement.get(0),2)+Math.pow(y-executedElement.get(1),2));
//                if(distance>maxDis2E)
//                {
//                    maxDis2E=distance;
//                }
//            }
//            if(maxDis<maxDis2E)
//            {
//                maxDis=maxDis2E;
//                ret=candidateSetElement;
//            }
        }
        return ret;
    }

}
