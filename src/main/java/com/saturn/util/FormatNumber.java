/** ******FormatNumber.java*****/
/**
 *Copyright
 *
 **/
package com.saturn.util;

import java.text.DecimalFormat;

/**
 * @describe: <pre>
 * </pre>
 * @date :2015年3月16日 下午3:09:58
 * @author : ericcoderr@gmail.com
 */
public class FormatNumber {

    private static final DecimalFormat DF = new DecimalFormat("######0.000");

    public static String rate(int divisor, int dividend) {
        double result = 0;
        if (dividend == 0) {
            result = 0;
        } else {
            result = ((double) divisor / dividend) * 100;
        }
        return DF.format(result);
    }

    public static String rateWithPercent(int divisor, int dividend) {
        double result = 0;
        if (dividend == 0) {
            result = 0;
        } else {
            result = ((double) divisor / dividend) * 100;
        }
        return DF.format(result) + "%";
    }

    public static String converDouble(int divisor, int dividend) {
        double result = 0;
        if (dividend == 0) {
            result = 0;
        } else {
            result = ((double) divisor / dividend);
        }
        return DF.format(result);
    }
}
