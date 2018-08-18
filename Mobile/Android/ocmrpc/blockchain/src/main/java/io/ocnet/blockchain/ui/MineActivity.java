package io.ocnet.blockchain.ui;

import android.os.Bundle;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import io.ocnet.blockchain.R;
import io.ocnet.blockchain.Utils.Convert;
import io.ocnet.blockchain.Utils.StorageKey;
import io.ocnet.blockchain.model.BCRequest;
import io.ocnet.blockchain.model.RPCEntity;
import io.ocnet.blockchain.model.UserStorageUtils;

public class MineActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity);
        TextView addressView = (TextView) findViewById(R.id.tvAddress);
        final String address = UserStorageUtils.getObject(this, StorageKey.USER_ADDRESS);
        addressView.setText(address);

        BCRequest.getBalance(this, address, new BCRequest.Listener<RPCEntity>() {

            @Override
            public void onResponse(final RPCEntity response) {
                TextView tvBalance = (TextView) findViewById(R.id.tvBalance);
                String value = response.getResult().substring(2);
                BigInteger bd = new BigInteger(value,16);
                BigDecimal balance = new BigDecimal(bd.divide(new BigInteger("10000000000000")).toString());
                BigDecimal nbalance = balance.divide(new BigDecimal("100000"), 8, BigDecimal.ROUND_DOWN);
                System.out.println(nbalance);
                tvBalance.setText(response.getResult() + " Wei");
            }
        }, null);
    }

}
