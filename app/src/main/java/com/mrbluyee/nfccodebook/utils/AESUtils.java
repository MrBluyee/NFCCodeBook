package com.mrbluyee.nfccodebook.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final String CipherMode = "AES/CFB/NoPadding";//使用CFB加密，需要设置IV

    public static byte[] encrypt(byte[] content,String keyString) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(keyString.getBytes());
            Cipher cipher = Cipher.getInstance(CipherMode);
            SecretKeySpec keyspec = new SecretKeySpec(sha.digest(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, new IvParameterSpec(
                    new byte[cipher.getBlockSize()]));
            byte[] encrypted = cipher.doFinal(content);
            return encrypted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] decrypt(byte[] content, String keyString){
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(keyString.getBytes());
            Cipher cipher = Cipher.getInstance(CipherMode);
            SecretKeySpec keyspec = new SecretKeySpec(sha.digest(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keyspec, new IvParameterSpec(
                    new byte[cipher.getBlockSize()]));
            byte[] original = cipher.doFinal(content);
            return original;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
