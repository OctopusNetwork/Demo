package io.ocnet.blockchain.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.ocnet.blockchain.R;
import io.ocnet.blockchain.Utils.StorageKey;
import io.ocnet.blockchain.model.BCRequest;
import io.ocnet.blockchain.model.RPCEntity;
import io.ocnet.blockchain.model.UserStorageUtils;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        findViewById(R.id.btRegister).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.evPassword);
                String password = editText.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please input transaction password !", Toast.LENGTH_LONG).show();
                } else {
                    BCRequest.newAccount(RegisterActivity.this, password, new BCRequest.Listener<RPCEntity>() {
                        @Override
                        public void onResponse(RPCEntity response) {
                            UserStorageUtils.putObject(RegisterActivity.this, StorageKey.USER_ADDRESS, response.getResult());
                            Intent intent = new Intent(RegisterActivity.this, ProxyZonesActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, null);
                }
            }
        });
    }
}
