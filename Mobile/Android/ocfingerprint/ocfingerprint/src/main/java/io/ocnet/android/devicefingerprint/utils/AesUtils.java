package io.ocnet.android.devicefingerprint.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

class AesUtils {

    /**
     * aes加密数据，加密后的字节数组，采用base64编码转成字符串返回
     *
     * @param data 待加密数据
     * @param secret aes密钥
     *
     * @return 加密后生成的字节数组使用base64编码成的字符串
     */
    static String encrypt(String data, String secret) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] original = Base64.encode(cipher.doFinal(data.getBytes()), Base64.NO_WRAP);
        return new String(original);
    }

    /**
     * aes解密，传入的字符串必须是加密的字节数组使用base64编码成的字符串
     *
     * @param data 待解密的字节数组使用base64编码成的字符串
     * @param secret aes密钥
     *
     * @return 解密后的字符串
     */
    static String decrypt(String data, String secret) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] original = cipher
                .doFinal(Base64.decode(data, Base64.NO_WRAP));
        return new String(original).trim();
    }

}
