package com.automannn.commonUtils.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author automannn@163.com
 * @time 2019/4/28 17:41
 */

/*MD5加密*/
public class MD5 {

    private static MessageDigest md5Digest = null;


    //盐值，用于混淆
    private static final String salt = "qgk8(Y1*Rp";

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException("MD5 not supported", e);
        }
    }

    public static String generate(String source, boolean withSalt) {
        if (source == null) {
            return null;
        }
        if (withSalt) {
            source = source + salt;
        }
        return digest(source);
    }

    private static String digest(String source) {
        if (source == null) {
            return null;
        }
        try {
            MessageDigest md5 = (MessageDigest) md5Digest.clone();
            md5.update(source.getBytes("UTF-8"));
            //核心
            byte[] bs =  md5.digest();
            return byte2hex(bs);
        } catch (CloneNotSupportedException e) {
            throw new SecurityException("clone of MD5 not supported", e);
        } catch (UnsupportedEncodingException e) {
            throw new SecurityException("encoding utf-8 not supported", e);
        }
    }

    //将md5 字节转换为 hex码
    private static String byte2hex(byte[] bs) {
        String hs = "";
        String stmp;
        for (byte b : bs){
            stmp = (Integer.toHexString(b & 0XFF));
            if (stmp.length() == 1) {
                //两个hex为一组
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }
}
