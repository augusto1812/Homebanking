package com.santander.homebanking.utils;

public final class MathUtils {
    public static double round(double value) {

        return  Math.round(value * 100.0) / 100.0;
    }
}
