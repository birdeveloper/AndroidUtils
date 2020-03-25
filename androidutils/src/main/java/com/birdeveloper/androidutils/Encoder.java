package com.birdeveloper.androidutils;



public class Encoder {

    private static final String key = "Zx" + Math.log(2) / 3;

    public static String obfuscate(String s) {
        char[] result = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            result[i] = (char) (s.charAt(i) + key.charAt(i % key.length()));
        }

        return new String(result);
    }

    public static String unObfuscate(String s) {
        char[] result = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            result[i] = (char) (s.charAt(i) - key.charAt(i % key.length()));
        }
        String resultStr = new String(result);
        try {
            resultStr = new String(Base64.decode(resultStr), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultStr;
    }
}
