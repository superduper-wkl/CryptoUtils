package com.wkl.cryptoutils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText mEt1;
    private TextView mTv2;
    private TextView mTv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEt1 = findViewById(R.id.et_1);
        mTv2 = findViewById(R.id.tv_2);
        mTv3 = findViewById(R.id.tv_3);

//        Provider[] providers = Security.getProviders();
//        for (Provider provider : providers) {
//            Log.e("TAG", "provider名称" + provider);
//            Set<Provider.Service> services = provider.getServices();
//            for (Provider.Service service : services) {
//                Log.e("TAG", "类型" + service.getType());
//                Log.e("TAG", "算法" + service.getAlgorithm());
//                Log.e("TAG", "************************************************\n\n");
//            }
//        }
        
    }
    
    public void crypto(View view) {
        String str = mEt1.getText().toString();
        if (TextUtils.isEmpty(str)) {
            return;
        }

        CryptoUtils crypto = CryptoUtils.getInstance(this);
        String encryptStr = crypto.aesEncrypt(str);
        String decryptStr = crypto.aesDecrypt(encryptStr);

        mTv2.setText(encryptStr);
        mTv3.setText(decryptStr);
    }
}
