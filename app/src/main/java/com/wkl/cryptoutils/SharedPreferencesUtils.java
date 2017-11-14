package com.wkl.cryptoutils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by wkl on 2017/11/14.
 * wkl
 */

public class SharedPreferencesUtils {

    private static final String NAME = "crypto";
    private static final String IV_KEY_NAME = "iv";

    public static void saveIVData(Context context, byte[] iv) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(IV_KEY_NAME, Base64.encodeToString(iv, Base64.DEFAULT));
        edit.apply();
    }

    public static byte[] getIVData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String string = preferences.getString(IV_KEY_NAME, null);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return Base64.decode(string, Base64.DEFAULT);
    }
}
