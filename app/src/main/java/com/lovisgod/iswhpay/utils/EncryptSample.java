package com.lovisgod.iswhpay.utils;

public class EncryptSample {

    private static final String KEY = "AB1C11111111111AAAAAAADDDDD11111";
    private static final int PIN_LENGTH = 6;

    public static void main(String[] args) throws Exception {

        TripleDES td = new TripleDES(KEY, PIN_LENGTH);

        String pan = "4111111111111111";
        String pin = "123456";

        String encrypted = td.encrypt(pan, pin);
        System.out.println("encrypted: " + encrypted);

        String decrypted = td.decrypt(pan, encrypted, KEY);
        System.out.println("decrypted    : " + decrypted);
    }
}

