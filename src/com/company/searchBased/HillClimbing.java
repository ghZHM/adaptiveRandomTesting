package com.company.searchBased;

import com.company.utils.myRandom;

import java.util.*;

/**
 * @Author Haoming Zeng
 * @Date 2023/7/17 20:31
 * @classDescribe implement search based ART
 * proposed by Schneckenburger C, Schweiggert F.
 * Investigating the dimensionality problem of Adaptive Random Testing incorporating a local search technique[C]//
 * 2008 IEEE International Conference on Software Testing Verification and Validation Workshop. IEEE, 2008: 241-250.
 */
public class HillClimbing {
    double XWidth;
    double YWidth;
    double shakeEdge;
    static int testSetSize = 30;
    static int nMax = 250;
    public HillClimbing(double Xmin, double Xmax,double Ymin,double Ymax)
    {
        this.XWidth=Xmax-Xmin;
        this.YWidth=Ymax-Ymin;
        this.shakeEdge=Math.sqrt(XWidth*YWidth)*0.01/2;
    }

    public HashSet runHillClimbing(double Xmin,double Xmax,double Ymin,double Ymax,double failureX, double failureY, double edge)
    {
        HashSet<List> executedSet = new HashSet<>();
        while (true)
        {
            HashSet<List> underTest = testCaseGenerator(Xmin,Xmax,Ymin,Ymax);
            Iterator iterator = underTest.iterator();
            int flag=0;
            while (iterator.hasNext())
            {
                List<Double> temp = (List<Double>) iterator.next();
                if (checkFailure(temp.get(0),temp.get(1),failureX,failureY,edge))
                {
                    executedSet.add(temp);
                }
                else
                {
                    executedSet.add(temp);
                    flag=1;
                    break;
                }
            }
            if (flag==1)
            {
                break;
            }
        }
        return executedSet;
    }

    public HashSet runHillClimbing(double Xmin,double Xmax,double Ymin,double Ymax,HashMap<String,Double> strip)
    {
        HashSet<List> executedSet = new HashSet<>();
        while (true)
        {
            HashSet<List> underTest = testCaseGenerator(Xmin,Xmax,Ymin,Ymax);
            Iterator iterator = underTest.iterator();
            int flag=0;
            while (iterator.hasNext())
            {
                List<Double> temp = (List<Double>) iterator.next();
//                if (checkFailure(temp.get(0),temp.get(1),failureX,failureY,edge))
                if (checkStrip(temp.get(0),temp.get(1),strip))
                {
                    executedSet.add(temp);
                }
                else
                {
                    executedSet.add(temp);
                    flag=1;
                    break;
                }
            }
            if (flag==1)
            {
                break;
            }
        }
        return executedSet;
    }

    private HashSet testCaseGenerator(double Xmin, double Xmax, double Ymin, double Ymax)
    {
        HashSet<List> initialSet = new HashSet<>();
        myRandom random = new myRandom();
        double alpha = 0.01;
        // generate initial test set S
        while (initialSet.size()<testSetSize)
        {
            List<Double> temp = new LinkedList<>();
            temp.add((alpha*random.random(Xmin,Xmax)+(1-alpha)*random.random(Xmin,Xmax)));
            temp.add((alpha*random.random(Ymin,Ymax)+(1-alpha)*random.random(Ymin,Ymax)));
            initialSet.add(temp);
        }
        HashSet<List> setT = new HashSet<>();
        while (true)
        {
            if(initialSet.isEmpty())
            {
                // copy T to S and set T empty
                Iterator iterator = setT.iterator();
                while (iterator.hasNext())
                {
                    initialSet.add((List) iterator.next());
                }
                setT.clear();
                continue;
            }
            List<List> temp = new ArrayList(initialSet);
            int index = new Random().nextInt(temp.size());
            List<Double> pickedT = temp.get(index);
            initialSet.remove(pickedT);
            // compute f*
            double fStar = Double.MAX_VALUE;
            Iterator iterator = initialSet.iterator();
            fStar = getfStar(pickedT, fStar, iterator);
            iterator = setT.iterator();
            fStar = getfStar(pickedT, fStar, iterator);
            int n=0;
            //shake t
            while (true)
            {
                double shakedXT = random.random(pickedT.get(0)-shakeEdge,pickedT.get(0)+shakeEdge);
                double shakedYT = random.random(pickedT.get(1)-shakeEdge,pickedT.get(1)+shakeEdge);
                n++;
                if(n>=nMax)
                {
                    iterator = setT.iterator();
                    while (iterator.hasNext())
                    {
                        initialSet.add((List) iterator.next());
                    }
                    break;
                }
                double minDistance = Double.MAX_VALUE;
                minDistance = getMinDistance(initialSet, shakedXT, shakedYT, minDistance);
                minDistance = getMinDistance(setT, shakedXT, shakedYT, minDistance);
                if(minDistance>fStar)
                {
                    List<Double> toAdd = new LinkedList<>();
                    toAdd.add(shakedXT);
                    toAdd.add(shakedYT);
                    setT.add(toAdd);
                    break;
                }
            }
            if(n>=nMax)
            {
                break;
            }
        }
        return initialSet;
    }

    private double getfStar(List<Double> pickedT, double fStar, Iterator iterator) {
        while (iterator.hasNext())
        {
            List<Double> element = (List<Double>) iterator.next();
            double distance = continuousDistance(pickedT.get(0),pickedT.get(1),element.get(0),element.get(1));
            if(distance<fStar)
            {
                fStar=distance;
            }
        }
        return fStar;
    }

    private double getMinDistance(HashSet<List> setT, double shakedXT, double shakedYT, double minDistance) {
        Iterator iterator;
        iterator = setT.iterator();
        while (iterator.hasNext())
        {
            List<Double> sElement = (List<Double>) iterator.next();

            double distanceTStar = continuousDistance(shakedXT,shakedYT,sElement.get(0),sElement.get(1));
            if(distanceTStar<minDistance)
            {
                minDistance = distanceTStar;
            }
        }
        return minDistance;
    }

    private double continuousDistance(double aX,double aY, double bX, double bY)
    {
        return Math.sqrt(Math.pow(Math.min(Math.abs(aX-bX),XWidth-Math.abs(aX-bX)),2)+Math.pow(Math.min(Math.abs(aY-bY),YWidth-Math.abs(aY-bY)),2));
    }

    private boolean checkFailure(double tcX, double tcY, double centerX, double centerY, double edge)
    {
        if(!((tcX>=centerX-edge/2&&tcX<=centerX+edge/2)&&(tcY>=centerY-edge/2&&tcY<=centerY+edge/2)))
        {
            return true;
        }
        return false;
    }

    private static boolean checkStrip( double tcX, double tcY, HashMap failureRegion)
    {
        double upper = Math.max((double) failureRegion.get("b2"), (double) failureRegion.get("b1"));
        double lower = Math.min((double) failureRegion.get("b2"), (double) failureRegion.get("b1"));

        double k = (double)failureRegion.get("k-value");
        upper+= k*tcX;
        lower+= k*tcX;
        if(!(tcY>=lower&&tcY<=upper))
        {
            return true;
        }
        return false;
    }
}
