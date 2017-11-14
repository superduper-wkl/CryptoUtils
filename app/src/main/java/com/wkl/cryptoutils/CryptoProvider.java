package com.wkl.cryptoutils;

import java.security.Provider;

/**
 * Created by wkl on 2017/11/14.
 * wkl
 */

public class CryptoProvider extends Provider {
    public CryptoProvider() {
        super("CryptoUtils", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
        put("SecureRandom.SHA1PRNG",
                "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
        put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
    }
}
