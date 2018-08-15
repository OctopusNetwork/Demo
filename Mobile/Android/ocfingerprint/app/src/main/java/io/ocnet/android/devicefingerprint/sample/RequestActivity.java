package io.ocnet.android.devicefingerprint.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.ocnet.android.devicefingerprint.utils.SystemInfoHelper;

public class RequestActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPermission;
    Button btnTest;
    TextView tvResult;

    SystemInfoHelper systemInfoHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        btnPermission = (Button) findViewById(R.id.btnPermission);
        btnPermission.setOnClickListener(this);
        btnTest = (Button) findViewById(R.id.btnTest);
        btnTest.setOnClickListener(this);
        tvResult = (TextView) findViewById(R.id.tvResult);

        systemInfoHelper = new SystemInfoHelper(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnTest) {
            clickTest(v);
        } else if (v == btnPermission) {
            clickPermission(v);
        }
    }

    void clickPermission(View v) {
        btnPermission.setEnabled(false);
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE)
                .subscribe(
                        aBoolean -> {
                            btnPermission.setEnabled(true);
                            Toast.makeText(RequestActivity.this, aBoolean ? "授权成功" : "已授权",
                                    Toast.LENGTH_SHORT).show();
                        },
                        throwable -> {
                            btnPermission.setEnabled(true);
                            Toast.makeText(RequestActivity.this, throwable.toString(),
                                    Toast.LENGTH_SHORT).show();
                        });
    }

    void clickTest(View v) {
        btnTest.setEnabled(false);
        tvResult.setText("");

        systemInfoHelper.setOcnetGroup("ocnet");
//        systemInfoHelper.upload()
//                .subscribe(
//                        entity -> {
//                            btnTest.setEnabled(true);
//                            Toast.makeText(RequestActivity.this, "请求成功！",
//                                    Toast.LENGTH_SHORT).show();
//
//                            String dataStream = new StringBuilder()
//                                    .append("\n<---\n")
//                                    .append(entity)
//                                    .toString();
//                            tvResult.setText(dataStream);
//                        },
//                        throwable -> {
//                            btnTest.setEnabled(true);
//                            Toast.makeText(RequestActivity.this, "请求失败: " + throwable.getMessage(),
//                                    Toast.LENGTH_LONG).show();
//                        }
//                );
    }
}
