package com.keven.joyrun.myplugin.widget.water;

import java.util.Random;

/**
 * Created by keven on 16/10/9.
 */

public class MathHelper {
    public static Random rand = new Random();
    public static float randomRange(float min, float max) {


        int randomNum = rand.nextInt(((int) max - (int) min) + 1) + (int) min;

        return randomNum;
    }
}
