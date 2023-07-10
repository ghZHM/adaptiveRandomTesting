package com.company.selectFromCandidate;

import com.company.utils.myRandom;
import sun.plugin.javascript.navig.Link;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author Haoming Zeng
 * @Date 2023/7/8 18:04
 * @classDescribe
 * @ Generate candidate set
 */
public class candidateSetGenerator {
    static int setSize = 10;
    public candidateSetGenerator()
    {

    }
    public HashSet<List> uniformDistribution(double Xmin,double Xmax,double Ymin,double Ymax)
    {
        HashSet<List> ret = new HashSet<>();
        myRandom myRandomInstance = new myRandom();
        while (ret.size()<setSize)
        {
            List<Double> temp = new LinkedList<>();
            temp.add(myRandomInstance.random(Xmin,Xmax));
            temp.add(myRandomInstance.random(Ymin,Ymax));
            ret.add(temp);
        }
        return ret;
    }

    public HashSet<List> nonUniformDistribution(double Xmin,double Xmax,double Ymin,double Ymax)
    {
        myRandom myRandomInstance = new myRandom();
        HashSet<List> ret = new HashSet<>();
        double alpha = 0.01;
        while (ret.size()<setSize)
        {
            List<Double> temp = new LinkedList<>();
            temp.add((alpha*myRandomInstance.random(Xmin,Xmax)+(1-alpha)*myRandomInstance.random(Xmin,Xmax)));
            temp.add((alpha*myRandomInstance.random(Ymin,Ymax)+(1-alpha)*myRandomInstance.random(Ymin,Ymax)));
            ret.add(temp);
        }
        return ret;
    }
}
