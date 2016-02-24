package com.saturn.util.codec.crypt;

import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 * 将byte数组 - 16进制数字 进行编码/解码 （统一使用小写字母）
 * 相比Hex类加入了特殊的乱序编码方式，以增强安全性
 * </pre>
 */
public class SecretHex {

    /**
     * 编码所使用的字符
     */
    public static final char[] DIGITS_NORMAL = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    public static void main(String[] args) {
        System.out.println(Integer.toHexString(0xFF));
    }

    private char[] digitsSecret;
    private char[] digits_table_nor2sec;
    private char[] digits_table_sec2nor;

    public SecretHex(char[] digitsSecret) {
        if (digitsSecret.length != DIGITS_NORMAL.length) {
            throw new IllegalArgumentException("digitsSecret's len is not " + DIGITS_NORMAL.length);
        }

        this.digitsSecret = digitsSecret;

        Set<Character> checkSet = new HashSet<Character>(DIGITS_NORMAL.length);
        for (int i = 0; i < digitsSecret.length; i++) {
            Character c = digitsSecret[i];
            if (c < '0' || c > '9' && c < 'a' || c > 'f') {
                throw new IllegalArgumentException("digitsSecret's char[" + i + "] is not hex:" + c);
            }

            if (checkSet.contains(c)) {
                throw new IllegalArgumentException("digitsSecret's char[" + i + "] is duplicate:" + c);
            }
            checkSet.add(c);
        }

        int table_len = 'f' + 1;
        digits_table_nor2sec = new char[table_len];
        digits_table_sec2nor = new char[table_len];

        // nor 找到对应 sec
        for (int i = 0; i < DIGITS_NORMAL.length; i++) {
            char c = digitsSecret[i];
            char index = DIGITS_NORMAL[i];
            digits_table_nor2sec[index] = c;
        }
        // sec 找到对应 nor
        for (int i = 0; i < digitsSecret.length; i++) {
            char c = DIGITS_NORMAL[i];
            char index = digitsSecret[i];
            digits_table_sec2nor[index] = c;
        }
    }

    public char[] getDigitsSecret() {
        return digitsSecret;
    }

    public char nor2sec(char nor) {
        return digits_table_nor2sec[nor];
    }

    public String nor2sec(String nor) {
        char[] sec = new char[nor.length()];
        for (int i = 0; i < sec.length; i++) {
            sec[i] = digits_table_nor2sec[nor.charAt(i)];
        }
        return new String(sec);
    }

    public char sec2nor(char sec) {
        return digits_table_sec2nor[sec];
    }

    public String sec2nor(String sec) {
        char[] nor = new char[sec.length()];
        for (int i = 0; i < nor.length; i++) {
            nor[i] = digits_table_sec2nor[sec.charAt(i)];
        }
        return new String(nor);
    }

}
