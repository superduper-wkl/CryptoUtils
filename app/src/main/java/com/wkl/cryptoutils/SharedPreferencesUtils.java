package com.wkl.cryptoutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by wkl on 2017/11/14.
 * wkl
 */

public class SharedPreferencesUtils {

    private static final String NAME = "crypto";
//    private static final String IV_KEY_NAME = "iv";

//    public static void saveIVData(Context context, byte[] iv) {
//        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = preferences.edit();
//        edit.putString(IV_KEY_NAME, Base64.encodeToString(iv, Base64.NO_WRAP));
//        edit.apply();
//    }
//
//    public static byte[] getIVData(Context context) {
//        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
//        String string = preferences.getString(IV_KEY_NAME, null);
//        if (TextUtils.isEmpty(string)) {
//            return null;
//        }
//        return Base64.decode(string, Base64.NO_WRAP);
//    }

    /**
     * 使用SharedPreference保存对象
     *
     * @param key        储存对象的key
     * @param saveObject 储存的对象
     */
    public static void save(Context context, String key, Object saveObject) {
        SharedPreferences sharedPreferences = context.getApplicationContext().
                getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String string = Object2String(saveObject);
        editor.putString(key, string);
        editor.apply();
    }

    /**
     * 获取SharedPreference保存的对象
     *
     * @param key     储存对象的key
     * @return object 返回根据key得到的对象
     */
    public static Object get(Context context, String key) {
        SharedPreferences sharedPreferences = context.getApplicationContext().
                getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String string = sharedPreferences.getString(key, null);
        if (string != null) {
            return String2Object(string);
        } else {
            return null;
        }
    }

    /**
     * writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
     * 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
     *
     * @param object 待加密的转换为String的对象
     * @return String   加密后的String
     */
    private static String Object2String(Object object) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            String string = new String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP));
            objectOutputStream.close();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用Base64解密String，返回Object对象
     *
     * @param objectString 待解密的String
     * @return object      解密后的object
     */
    private static Object String2Object(String objectString) {
        byte[] mobileBytes = Base64.decode(objectString.getBytes(), Base64.NO_WRAP);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
