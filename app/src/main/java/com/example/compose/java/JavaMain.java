package com.example.compose.java;

import java.util.Locale;

/**
 * @author : YingYing Zhang
 * @e-mail : 540108843@qq.com
 * @time : 2023-12-06
 * @desc :
 */
class JavaMain {
    public static final int HUNDRED_THOUSAND = 100000;
    // 1百万
    public static final int MILLION = 1000000;
    // 1亿
    public static final int HUNDRED_MILLION = 100000000;
    public static final int THOUSAND = 1000;
    public static final int TEN_THOUSAND = 10000;
    private static final String UNIT_AHUNDRED_MILLION = "亿";
    private static final String UNIT_TEN_THOUSAND = "万";

    public static void main(String[] args) {
        String a1 = format(10489, "0");
        String a4 = format(10490, "0");
        String a5 = format(19500, "0");
        String a6 = format(19501, "0");
        String a2 = format(30000000, "0");
        String a3 = format(99999999, "0");

        System.out.println("a1 = " + a1);
        System.out.println("a4 = " + a4);
        System.out.println("a5 = " + a5);
        System.out.println("a6 = " + a6);
        System.out.println("a2 = " + a2);
        System.out.println("a3 = " + a3);


        float result = 19500 / (float)10000;
        System.out.println("result = " + result);
    }

    public static String format(long num, String defValue) {
        if (num >= HUNDRED_MILLION) {
            float n = num / (float) HUNDRED_MILLION;
            float remainder = n % 1;
            if (remainder >= 0.95 || remainder <= 0.049) {
                return StringFormatter.format(Locale.getDefault(), "%.0f" + UNIT_AHUNDRED_MILLION, n);
            } else {
                return StringFormatter.format(Locale.getDefault(), "%.1f" + UNIT_AHUNDRED_MILLION, n);
            }
        } else if (num >= 99999500) {
            // 防止出现10000万
            return "1" + UNIT_AHUNDRED_MILLION;
        } else if (num >= TEN_THOUSAND) {
            float n = num / (float) TEN_THOUSAND;
            float remainder = n % 1;
            // 14499 -> 1.4万 14999 -> 1.5万 19499 -> 1.9万 19599 -> 2万
            if (remainder >= 0.95 || remainder <= 0.049) {
                return StringFormatter.format(Locale.getDefault(), "%.0f" + UNIT_TEN_THOUSAND, n);
            } else {
                return StringFormatter.format(Locale.getDefault(), "%.1f" + UNIT_TEN_THOUSAND, n);
            }
        } else if (num > 0) {
            return String.valueOf(num);
        } else {
            return defValue;
        }
    }
}