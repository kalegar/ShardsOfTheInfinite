package com.kalegar.soti.util;

public class Constants {

    //Don't allow to be instantiated
    private Constants() {

    }

    //Physics Constants
    public static final float PPM = 32f;
    public static final float PHYSICS_TIME_STEP = 1/60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
}
