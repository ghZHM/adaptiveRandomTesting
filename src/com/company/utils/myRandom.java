package com.company.utils;

import java.util.Random;

/**
 * @Author Haoming Zeng
 * @Date 2023/7/8 17:39
 * @classDescribe
 *  generate Random number with border
 */
 public class myRandom {
    static public int random(int min,int max)
    {
        Random generater = new Random();
        return generater.nextInt(max-min+1)+min;
    }
    public myRandom()
    {

    }
}
