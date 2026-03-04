package com.adson.staymanager.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<CPF, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;

        String cpf = value.replaceAll("\\D", "");
        
        if (cpf.length() != 11) return false;

        // todos iguais?
        char first = cpf.charAt(0);
        boolean allEqual = true;
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != first) { allEqual = false; break; }
        }
        if (allEqual) return false;

        try {
            int d1 = calculateDigit(cpf.substring(0, 9), 10);
            int d2 = calculateDigit(cpf.substring(0, 10), 11);

            return d1 == Character.getNumericValue(cpf.charAt(9))
                && d2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }

    private int calculateDigit(String digits, int factor) {
        int sum = 0;
        int weight = factor;

        for (int i = 0; i < digits.length(); i++) {
            int digit = Character.getNumericValue(digits.charAt(i));
            sum += digit * weight;
            weight--;
        }

        int result = 11 - (sum % 11);
        return (result >= 10) ? 0 : result;
    }
}
