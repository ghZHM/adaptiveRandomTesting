package com.company.utils;

import java.util.Random;

/**
 * @Author Haoming Zeng
 * @Date 2023/7/8 17:39
 * @classDescribe
 *  generate Random number with border
 */
 public class myRandom {
    public double random(double min,double max)
    {
        Random generater = new Random();
        return generater.nextDouble()*(max-min)+min;
    }
    public myRandom()
    {

    }
}
