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
        EncryptData encryptData = crypto.aesEncrypt("测试", str);
        String decryptStr = crypto.aesDecrypt(encryptData);

        // 保存加密后的信息
//        SharedPreferencesUtils.save(this, encryptData.getAlias(), encryptData);
        // 获取加密后的信息
//        SharedPreferencesUtils.get(this, encryptData.getAlias());

        mTv2.setText(encryptData.getEncryptString());
        mTv3.setText(decryptStr);
    }
}
