package com.company;

import com.company.utils.myRandom;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author Haoming Zeng
 * @Date 2023/7/13 19:02
 * @classDescribe Baseline random testing method
 */
public class BaselineRandomTest {
    public BaselineRandomTest()
    {

    }

    public List generator(double Xmin,double Xmax,double Ymin,double Ymax)
    {
        myRandom myRandomInstance = new myRandom();
        List<Double> temp = new LinkedList<>();
        temp.add(myRandomInstance.random(Xmin,Xmax));
        temp.add(myRandomInstance.random(Ymin,Ymax));
        return temp;
    }
}
