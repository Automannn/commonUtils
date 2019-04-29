package com.automannn.commonUtils.security;

import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author automannn@163.com
 * @time 2019/4/28 17:53
 */
public class RSA {
    public static final String KEY_ALGORITHM = "RSA";

    public static final String SIGNATURE_ALGORITHM = "SHA1WithRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";

    private static final String PRIVATE_KEY = "RSAPrivateKey";

    private static final Integer KEY_SIZE = 1024;

    //生成非对称密钥
    public static Map<String, Object> initKey(){
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            // get public key
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // get private key
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Map<String, Object> keyMap = new HashMap<>(2);
            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);
            return keyMap;
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        }
    }

    //签名代理
    public static String sign(String signing, String privateKey, String charset){
        try {
            return sign(signing.getBytes(charset), privateKey);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException(e);
        }
    }

    //签名实际处理逻辑
    public static String sign(byte[] data, String privateKey){
        try {
            // decode private key with Base64
            byte[] keyBytes = base64Decode(privateKey);

            // construct PKCS8EncodedKeySpec obejct
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

            // get key factory of KEY_ALGORITHM
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            // generate PrivateKey object
            PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

            // sign data with private key and SIGNATURE_ALGORITHM algorithm
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(priKey);
            signature.update(data);

            // encode signed data with Base64
            return base64Encode(signature.sign());

        } catch (Exception e) {
            throw new SecurityException(e);
        }
    }

    //验证代理
    public static boolean verify(String signing, String signed, String publicKey, String charset) {
        try {
            return verify(signing.getBytes(charset), signed, publicKey);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException(e);
        }
    }

    //验证实际逻辑
    public static boolean verify(byte[] data, String signed, String publicKey) {
        try {
            // decode public key with Base64
            byte[] keyBytes = base64Decode(publicKey);

            // construct X509EncodedKeySpec object
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

            // KEY_ALGORITHM
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            // get key factory of KEY_ALGORITHM
            PublicKey pubKey = keyFactory.generatePublic(keySpec);

            // sign data with public key and SIGNATURE_ALGORITHM algorithm
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(pubKey);
            signature.update(data);

            // 验证签名是否正常
            return signature.verify(base64Decode(signed));
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    //私钥解码
    public static byte[] decryptByPrivateKey(byte[] data, String key) {
        try {
            // decode private key with Base64
            byte[] keyBytes = base64Decode(key);

            // get PKCS8EncodedKeySpec object
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            // decrypt data with private key
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    //公钥验签
    public static byte[] decryptByPublicKey(byte[] data, String key) {
        try {
            // decode public key with Base64
            byte[] keyBytes = base64Decode(key);

            // construct X509EncodedKeySpec object
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            // decrypt data with public key
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    //公钥数字签名
    public static byte[] encryptByPublicKey(byte[] data, String key){
        try {
            // decode public key with Base64
            byte[] keyBytes = base64Decode(key);

            // construct X509EncodedKeySpec data
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            // encrypt data with public key
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    //私钥加密
    public static byte[] encryptByPrivateKey(byte[] data, String key){
        try {
            // decode private key with Base64
            byte[] keyBytes = base64Decode(key);

            // construct PKCS8EncodedKeySpec data
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            // encrypt data with private key
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(data);
        } catch (Exception e){
            throw new SecurityException(e);
        }
    }

    //获取私钥
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return base64Encode(key.getEncoded());
    }

    //获取公钥
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return base64Encode(key.getEncoded());
    }

    //base64编码
    public static String base64Encode(byte[] key){
        return Base64.encode(key);
    }

    //base64解吗
    public static byte[] base64Decode(String key) {
        return Base64.decode(key);
    }


}
