package com.ammvision.sudoku;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by ashiminty on 9/14/2014.
 */
public class DefaultRandomizer implements IRandomizer
{
    Random rnd = null;
    Random rndBtwn = null;

    public DefaultRandomizer()
    {
        rnd = new Random();
        rndBtwn = new Random();
    }

    public int GetInt(int max)
    {
        int randomNum = rnd.nextInt(max);

        return randomNum;
    }

    public int GetInt(int min, int max)
    {
        int randomNum = min + rndBtwn.nextInt(max);

        return randomNum;
    }
}

