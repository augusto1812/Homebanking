package com.santander.homebanking.utils;

import java.util.Random;

public final class CardUtils {

    private CardUtils() {}

    public static int getCvv() {
        Random random = new Random();
        int cvv = random.nextInt((999 - 100) + 100);
        return cvv;
    }

    public static String getCardNumber() {
        Random random = new Random();
        String number = "";
        for (int i = 0; i<=3; i++){
            number += (random.nextInt(9999 - 1000) + 1000) +((i==3) ? "" : "-");
        }
        return number;
    }
}
