package com.hal.utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * Class that extract usefull math method as sum and sub
 * 
 */
public final class MathUtils {

    private MathUtils() {
    }
    
    public static Double sum(double price1, double price2) {
        BigDecimal price1Bd = new BigDecimal("" + price1);
        BigDecimal price2Bd = new BigDecimal("" + price2);
        BigDecimal sum = price1Bd.add(price2Bd);
        return new Double(sum.toString());
    }

    public static Double sub(double price1, double price2) {
        BigDecimal price1Bd = new BigDecimal("" + price1);
        BigDecimal price2Bd = new BigDecimal("" + price2);
        BigDecimal sum = price1Bd.subtract(price2Bd);
        return new Double(sum.toString());
    }
    
/*    public static BigDecimal round(BigDecimal toRound, CurrencyDTO currency) {
        BigDecimal unitMinimum = new BigDecimal("" + currency.getMinNonCash());
        BigDecimal remainder = toRound.remainder(unitMinimum);
        if (remainder.compareTo(unitMinimum.divide(new BigDecimal("2"))) < 0) {
            return toRound.subtract(remainder).setScale(currency.getDecimalNumber(), BigDecimal.ROUND_HALF_UP);
        } else {
            return toRound.subtract(remainder).add(unitMinimum).setScale(currency.getDecimalNumber(), BigDecimal.ROUND_HALF_UP);
        }
    }*/

    public static double sum(List<Double> values) {
        double result = 0;
        for (Double value : values) {
            if (value != null) {
                result = sum(result, value);
            }
        }
        return result;
    }
    
    public static boolean isBetween(double number, double min, double max) {
        return number >= min && number <= max;
    }
    
}
