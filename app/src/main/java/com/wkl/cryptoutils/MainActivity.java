package com.wkl.cryptoutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.security.Provider;
import java.security.Security;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Provider[] providers = Security.getProviders();
        for (Provider provider : providers) {
            Set<Provider.Service> services = provider.getServices();
            for (Provider.Service service : services) {
                service.getType();
                service.getAlgorithm();
            }
        }
    }
}
