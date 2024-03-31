package com.github.beltraliny.testeuex.utils;

public class CpfUtils {

    private CpfUtils() { }

    public static boolean isValid(String cpf) {
        if (cpf == null) return false;
        if (cpf.isEmpty()) return false;

        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");

        if (cpf.equals("00000000000") || cpf.equals("11111111111") ||
                cpf.equals("22222222222") || cpf.equals("33333333333") ||
                cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") ||
                cpf.equals("88888888888") || cpf.equals("99999999999") ||
                (cpf.length() != 11)) return(false);

        try{
            Long.parseLong(cpf);
        } catch(NumberFormatException numberFormatException){
            return false;
        }

        int d1, d2;
        int digitOne, digitTwo, rest;
        int digitCpf;
        String digitResult;

        d1 = d2 = 0;

        for (int i = 1; i < cpf.length() - 1; i++) {
            digitCpf = Integer.parseInt(cpf.substring(i - 1, i));
            d1 = d1 + (11 - i) * digitCpf;
            d2 = d2 + (12 - i) * digitCpf;
        };

        rest = (d1 % 11);

        if (rest < 2) {
            digitOne = 0;
        } else {
            digitOne = 11 - rest;
        }

        d2 += 2 * digitOne;

        rest = (d2 % 11);

        if (rest < 2) {
            digitTwo = 0;
        } else {
            digitTwo = 11 - rest;
        }
        String digitToBeChecked = cpf.substring(cpf.length() - 2, cpf.length());

        digitResult = String.valueOf(digitOne) + String.valueOf(digitTwo);

        return digitToBeChecked.equals(digitResult);
    }
}
