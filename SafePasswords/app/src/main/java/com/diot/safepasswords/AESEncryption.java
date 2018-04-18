package com.diot.safepasswords;

/**
 * Created by Nishant on 12-04-2018.
 */

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * This example program shows how AES encryption and decryption can be done in Java.
 * Please note that secret key and encrypted text is unreadable binary and hence
 * in the following program we display it in hexadecimal format of the underlying bytes.
 * @author Nishant
 */
public class AESEncryption {
    SecretKey secKey=null;
    String keydata;
    byte[] cipherText;
    private static final String    HEXES    = "0123456789ABCDEF";

    public AESEncryption(String key) {
        keydata = key;
        setSecKey(keydata);
    }

    /**
     * 1. Generate a plain text for encryption
     * 2. Get a secret key (printed in hexadecimal form). In actual use this must
     * by encrypted and kept safe. The same key is required for decryption.
     * 3.
     */

    public String doEncryptionAES(String plainText) throws Exception {

//        String decryptedText = decryptText(cipherText, secKey);

        //secKey = getSecretEncryptionKey();
        cipherText = encryptText(plainText, secKey);
        //System.out.println("Original Text:" + plainText);
        //System.out.println("AES Key (Hex Form):"+bytesToHex(secKey.getEncoded()));
        //System.out.println("Encrypted Text (Hex Form):"+bytesToHex(cipherText));
        return bytesToHex(cipherText);
//        System.out.println("Descrypted Text:"+decryptedText);

    }
    public SecretKey getSecKey(){
        return secKey;
    }

    private void setSecKey(String keydata){
        byte[] key = new byte[0];
        try {
            key = (keydata).getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit

        secKey = new SecretKeySpec(key, "AES");
    }


    /**
     * gets the AES encryption key. In your actual programs, this should be safely
     * stored.
     * @return
     * @throws Exception
     */
    public static SecretKey getSecretEncryptionKey() throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        return secKey;
    }

    /**
     * Encrypts plainText in AES using the secret key
     * @param plainText
     * @param secKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptText(String plainText,SecretKey secKey) throws Exception{
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }

    /**
     * Decrypts encrypted byte array using the key used for encryption.
     * @param stringCipherText
     * @return
     * @throws Exception
     */
    public String decryptText(String stringCipherText) throws Exception {

        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        byte[] byteCipherText = hexStringToByteArray(stringCipherText);
        //Log.e("Test",bytesToHex(byteCipherText));
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
//        Log.e("byteplainText",new String(bytePlainText));
        return new String(bytePlainText);
    }

    /**
     * Convert a binary byte array into readable hex form
     * @param hash
     * @return
     */
    private static String  bytesToHex(byte[] hash) {
        final StringBuilder hex = new StringBuilder(2 * hash.length);
        for (final byte b : hash) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
