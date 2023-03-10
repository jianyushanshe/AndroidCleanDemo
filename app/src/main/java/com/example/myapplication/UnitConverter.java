package com.example.myapplication;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DecimalFormat;

public class UnitConverter {
    private static final double a(int paramInt1, int paramInt2) {
        double d;
        if (paramInt1 != 1024) {
            if (paramInt1 != 1048576) {
                if (paramInt1 != 1073741824) {
                    d = 1.0D;
                } else {
                    d = 1.073741824E9D;
                }
            } else {
                d = 1048576.0D;
            }
        } else {
            d = 1024.0D;
        }
        if (paramInt2 != 1024) {
            if (paramInt2 != 1048576) {
                if (paramInt2 == 1073741824)
                    d /= 1.073741824E9D;
            } else {
                d /= 1048576.0D;
            }
        } else {
            d /= 1024.0D;
        }
        return d;
    }

    public static long b(long paramLong, int paramInt1, int paramInt2) {
        if (paramLong <= 0L)
            return 0L;
        double d = a(paramInt1, paramInt2);
        return (long)(paramLong * d);
    }

    public static String c(long paramLong, int paramInt1, int paramInt2) {
        if (paramLong == 0L)
            return "0";
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        double d = a(paramInt1, paramInt2);
        return decimalFormat.format(paramLong * d);
    }

    @Retention(RetentionPolicy.SOURCE)
    public static @interface SizeType {}
}
