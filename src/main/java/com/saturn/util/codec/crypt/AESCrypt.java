package com.saturn.util.codec.crypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES算法的封装类
 */
public final class AESCrypt {

    private SecretKey secretKey;
    private Cipher cipher;

    public AESCrypt() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.cipher = Cipher.getInstance("AES");
    }

    /**
     * 生成AES密钥（密钥长度为128bit）
     * 
     * @param seed
     * @throws NoSuchAlgorithmException
     */
    public void genSecretKey(byte[] seed) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128, new SecureRandom(seed));
        setSecretKey(kg.generateKey());
    }

    /**
     * 获取AES密钥
     * 
     * @return
     */
    public SecretKey getSecretKey() {
        return secretKey;
    }

    /**
     * 获取AES密钥的byte数组
     * 
     * @return
     */
    public byte[] getSecretKeyBytes() {
        if (null != secretKey) {
            return secretKey.getEncoded();
        }
        return null;
    }

    /**
     * 设置AES密钥
     * 
     * @param secretKey
     */
    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * 通过byte数组设置AES密钥
     * 
     * @param secretKeyBytes
     */
    public void setSecretKey(byte[] secretKeyBytes) {
        setSecretKey(new SecretKeySpec(secretKeyBytes, "AES"));
    }

    /**
     * 加密
     * 
     * @param input
     * @return
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] encode(byte[] input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
        return cipher.doFinal(input);
    }

    /**
     * 解密
     * 
     * @param input
     * @return
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public byte[] decode(byte[] input) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
        return cipher.doFinal(input);
    }
}
