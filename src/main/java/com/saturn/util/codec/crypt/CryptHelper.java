package com.saturn.util.codec.crypt;

import java.util.Random;

public class CryptHelper {

    /**
     * 随机生成指定长度的16进制byte字符串
     * 
     * @param len byte长度
     */
    public static String randomByteString(int len) {
        int length = len << 1;
        char[] arr = new char[length];
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = SecretHex.DIGITS_NORMAL[rand.nextInt(16)];
        }
        return new String(arr);
    }

}
