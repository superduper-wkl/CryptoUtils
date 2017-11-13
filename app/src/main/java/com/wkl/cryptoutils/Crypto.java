package com.wkl.cryptoutils;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.KeyStore;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;

/**
 * Created by wkl on 2017/11/12.
 * 兼容至Android4.0
 */

public class Crypto {

    public static final String DEFAULT_SECRETKEY_NAME = "default_secretkey_name";

    private KeyStore keyStore;

    private Crypto(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    public static Crypto getInstance() {
        KeyStore keyStore;
        try {

            if (android.os.Build.VERSION.SDK_INT >= 18) {
                keyStore = KeyStore.getInstance("AndroidKeyStore");
            } else if (android.os.Build.VERSION.SDK_INT >= 14) {
                keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            } else {
                throw new RuntimeException("兼容至Android4.0以上");
            }
            keyStore.load(null);

            if (!keyStore.containsAlias(DEFAULT_SECRETKEY_NAME)) { // 秘钥不存在，则生成秘钥


            }

            return new Crypto(keyStore);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static Crypto getInstance() {
//
//        if (DEFAULT_CRYPTO == null) {
//          synchronized (Crypto.class) {
//              if (DEFAULT_CRYPTO == null) {
//                  KeyStore keyStore;
//                  try {
//                      if (android.os.Build.VERSION.SDK_INT >= 18) {
//                          keyStore = KeyStore.getInstance("AndroidKeyStore");
//                      } else if (android.os.Build.VERSION.SDK_INT >= 14) {
//                          keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//                      } else {
//                          throw new RuntimeException("兼容至Android4.0以上");
//                      }
//                      keyStore.load(null);
//                      return new Crypto(keyStore);
//                  } catch (Exception e) {
//                      e.printStackTrace();
//                      return null;
//                  }
//              }
//          }
//        }
//
//        return DEFAULT_CRYPTO;
//    }

//    public String aesEncrypt(String content) {
//        try {
//            SecretKey secretKey = (SecretKey) keyStore.getKey(DEFAULT_SECRETKEY_NAME, null);
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private static KeyGenerator generateKeyGenerator() throws Exception {
        KeyGenerator keyGenerator;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(DEFAULT_SECRETKEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(false)
                    // Require that the user has unlocked in the last 30 seconds
//                            .setUserAuthenticationValidityDurationSeconds(AUTHENTICATION_DURATION_SECONDS)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
        } else {
            keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom secureRandom;
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                secureRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            } else {
                secureRandom = SecureRandom.getInstance("SHA1PRNG");
            }
            secureRandom.setSeed(System.currentTimeMillis());
            keyGenerator.init(128);
        }

        return keyGenerator;
    }
}
