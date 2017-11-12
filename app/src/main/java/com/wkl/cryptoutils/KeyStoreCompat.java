package com.wkl.cryptoutils;

import java.security.KeyStore;
import java.security.KeyStoreException;

/**
 * Created by wkl on 2017/11/12.
 * 兼容至Android4.0
 */

public class KeyStoreCompat {

    public static final String DEFAULT_KEY_NAME = "default_key_name";

    public static KeyStore getInstance() {
        KeyStore keyStore;
        try {
            if (android.os.Build.VERSION.SDK_INT >= 18) {
                keyStore = KeyStore.getInstance("AndroidKeyStore");
            } else if (android.os.Build.VERSION.SDK_INT >= 14) {
                keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            } else {
                throw new RuntimeException("兼容至Android4.0以上");
            }
            return keyStore;
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return null;
        }
    }

}
