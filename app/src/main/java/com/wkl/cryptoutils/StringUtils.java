package com.wkl.cryptoutils;

import java.io.UnsupportedEncodingException;

/**
 * Created by wkl on 2017/11/14.
 * wkl
 */

public class StringUtils {

    /**
     * string转byte数组，utf-8编码
     * @param string
     * @return
     */
    public static byte[] string2Bytes(String string) {
        if (string == null || string.isEmpty()) {
            return null;
        }
        try {
            return string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * byte数组转string，utf-8编码
     * @param bytes
     * @return
     */
    public static String bytes2String(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
