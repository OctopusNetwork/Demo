package io.ocnet.android.devicefingerprint.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import io.ocnet.android.devicefingerprint.utils.UuidUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnPermission;
    Button btnApiTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("TGA", UuidUtils.nativeUuid(this));
        btnPermission = (Button) findViewById(R.id.button);
        btnApiTest = (Button) findViewById(R.id.button2);
        btnPermission.setOnClickListener(this);
        btnApiTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnPermission) {
            startActivity(new Intent(this, PermissionActivity.class));
        } else if (v == btnApiTest) {
            startActivity(new Intent(this, RequestActivity.class));
        }
    }
}
