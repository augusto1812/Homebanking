package com.santander.homebanking.utils;

import java.util.Random;

public final class AccountUtils {


    public static String getNumeroCuenta() {
        String numeroCuenta;
        numeroCuenta="VIN";
        Integer number = getRandomNumber();
        String formatted = String.format("%0" + 4 + "d", number);
        numeroCuenta = numeroCuenta + number;
        return numeroCuenta;
    }

    public static int getRandomNumber() {
        Random random = new Random();
        return random.nextInt(9999999 - 1) + 1;

    }
}
