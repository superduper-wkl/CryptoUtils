package com.wkl.cryptoutils;

import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by wkl on 2017/11/12.
 * 兼容至Android4.0
 */

public class CryptoUtils {

    public static final String DEFAULT_SECRETKEY_NAME = "default_secretkey_name";

    public static final String STORE_FILE_NAME = "crypto";

    private KeyStore keyStore;

    private CryptoUtils(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public synchronized static CryptoUtils getInstance(Context context) {
        KeyStore keyStore;
        File file = new File(context.getFilesDir(), STORE_FILE_NAME);

        try {

            keyStore = getKeyStore(file);

            initKey(keyStore, file);

            CryptoUtils crypto = new CryptoUtils(keyStore);

            return crypto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    private static byte[] initIV(Context context) {
//        byte[] ivData = SharedPreferencesUtils.getIVData(context);
//        if (ivData == null || ivData.length == 0) {
//            byte[] bytes = generateSeed();
//            ivData = new byte[16];
//            System.arraycopy(bytes, 0, ivData, 0, 16);
//            SharedPreferencesUtils.saveIVData(context, ivData);
//        }
//        return ivData;
//    }

    private static void initKey(KeyStore keyStore, File file) throws Exception {
        if (!keyStore.containsAlias(DEFAULT_SECRETKEY_NAME)) { // 秘钥不存在，则生成秘钥
//            Log.e("TAG", "生成了密钥");
            KeyGenerator keyGenerator = generateKeyGenerator();
            SecretKey secretKey = keyGenerator.generateKey();

            storeKey(keyStore, file, secretKey);
        }
    }

    private static void storeKey(KeyStore keyStore, File file, SecretKey secretKey) throws Exception {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            keyStore.setKeyEntry(DEFAULT_SECRETKEY_NAME, secretKey, null, null);
        } else {
            keyStore.setKeyEntry(DEFAULT_SECRETKEY_NAME, secretKey, null, null);
//            KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
//            KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(null);
//            keyStore.setEntry(DEFAULT_SECRETKEY_NAME, secretKeyEntry,
//                    keyStorePP);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                keyStore.store(fos, null);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }

    private static KeyStore getKeyStore(File file) throws Exception {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            return keyStore;
        } else if (android.os.Build.VERSION.SDK_INT >= 14) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            if (!file.exists()) {
                boolean isSuccess = file.createNewFile();

                if (!isSuccess) {
                    throw new SecurityException("创建内部存储文件失败");
                }

                keyStore.load(null, null);
            } else if (file.length() <= 0) {
                keyStore.load(null, null);
            } else {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    keyStore.load(fis, null);
                    fis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            return keyStore;
        } else {
            throw new RuntimeException("兼容至Android4.0以上");
        }
    }

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
//            if (android.os.Build.VERSION.SDK_INT >= 24) {
//                secureRandom = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
//            } else
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                secureRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            } else {
                secureRandom = SecureRandom.getInstance("SHA1PRNG");
            }
            secureRandom.setSeed(generateSeed());
            keyGenerator.init(128, secureRandom);
        }

        return keyGenerator;
    }

    private static byte[] generateSeed() {
        try {
            ByteArrayOutputStream seedBuffer = new ByteArrayOutputStream();
            DataOutputStream seedBufferOut =
                    new DataOutputStream(seedBuffer);
            seedBufferOut.writeLong(System.currentTimeMillis());
            seedBufferOut.writeLong(System.nanoTime());
            seedBufferOut.writeInt(android.os.Process.myPid());
            seedBufferOut.writeInt(android.os.Process.myUid());
            seedBufferOut.write(Build.BOARD.getBytes());
            return seedBuffer.toByteArray();
        } catch (IOException e) {
            throw new SecurityException("Failed to generate seed", e);
        }
    }

    /**
     * AES加密
     *
     * @param content
     * @return
     */
    public EncryptData aesEncrypt(String alias, String content) {
        try {
            SecretKey secretKey = getSecretKey(keyStore);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(StringUtils.string2Bytes(content));
            byte[] iv = cipher.getIV();
            String encryptString = Base64.encodeToString(bytes, Base64.DEFAULT);
            return new EncryptData(alias, encryptString, iv);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param encryptData
     * @return
     */
    public String aesDecrypt(EncryptData encryptData) {
        try {
            SecretKey secretKey = getSecretKey(keyStore);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(encryptData.getIv()));
            byte[] bytes = cipher.doFinal(Base64.decode(encryptData.getEncryptString()
                    , Base64.DEFAULT));
            return StringUtils.bytes2String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static SecretKey getSecretKey(KeyStore keyStore) {
        //            KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry)
//                    keyStore.getEntry(DEFAULT_SECRETKEY_NAME, null);
//            SecretKey secretKey = entry.getSecretKey();
        try {
            return (SecretKey) keyStore.getKey(DEFAULT_SECRETKEY_NAME, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
