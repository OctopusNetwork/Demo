package io.ocnet.android.devicefingerprint.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Arrays;

import io.ocnet.android.devicefingerprint.utils.SystemInfoHelper;
import io.ocnet.android.devicefingerprint.utils.SystemInfoUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;

@SuppressLint("unused")
public class PermissionActivity extends AppCompatActivity implements View.OnClickListener {

    Activity activity;

    Button btBtn;
    Button androidIdBtn;
    Button deviceIdBtn;
    Button operatorBtn;
    Button wifiMacBtn;
    Button ssidBtn;
    Button isRootBtn;
    Button userAgentBtn;
    Button netTypeBtn;
    Button osVersionBtn;
    Button adIdBtn;
    Button cpuBtn;
    Button romSizeBtn;
    Button flashBtn;

    RxPermissions rxPermissions;

    SystemInfoHelper systemInfoHelper;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisson);

        btBtn = (Button) findViewById(R.id.bluetooth_mac_btn);
        btBtn.setOnClickListener(this);
        androidIdBtn = (Button) findViewById(R.id.android_id_btn);
        androidIdBtn.setOnClickListener(this);
        deviceIdBtn = (Button) findViewById(R.id.device_id_btn);
        deviceIdBtn.setOnClickListener(this);
        operatorBtn = (Button) findViewById(R.id.operator_btn);
        operatorBtn.setOnClickListener(this);
        wifiMacBtn = (Button) findViewById(R.id.wifi_mac_btn);
        wifiMacBtn.setOnClickListener(this);
        ssidBtn = (Button) findViewById(R.id.ssid_bssid_btn);
        ssidBtn.setOnClickListener(this);
        isRootBtn = (Button) findViewById(R.id.is_root_btn);
        isRootBtn.setOnClickListener(this);
        userAgentBtn = (Button) findViewById(R.id.user_agent_btn);
        userAgentBtn.setOnClickListener(this);
        netTypeBtn = (Button) findViewById(R.id.network_type_btn);
        netTypeBtn.setOnClickListener(this);
        osVersionBtn = (Button) findViewById(R.id.os_version_btn);
        osVersionBtn.setOnClickListener(this);
        adIdBtn = (Button) findViewById(R.id.ad_id_btn);
        adIdBtn.setOnClickListener(this);
        cpuBtn = (Button) findViewById(R.id.cpu_btn);
        cpuBtn.setOnClickListener(this);
        romSizeBtn = (Button) findViewById(R.id.rom_size_btn);
        romSizeBtn.setOnClickListener(this);
        flashBtn = (Button) findViewById(R.id.flash_btn);
        flashBtn.setOnClickListener(this);

        activity = this;
        rxPermissions = new RxPermissions(this);
        systemInfoHelper = new SystemInfoHelper(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btBtn) {
            clickBtMacBtn(v);
        } else if (v == androidIdBtn) {
            clickAndroidIdBtn(v);
        } else if (v == deviceIdBtn) {
             clickDeviceIdBtn(v);
        } else if (v == operatorBtn) {
            clickOperatorBtn(v);
        } else if (v == wifiMacBtn) {
            clickWifiMacBtn(v);
        } else if (v == ssidBtn) {
            clickSsidAndBssidBtn(v);
        } else if (v == isRootBtn) {
            clickIsRootBtn(v);
        } else if (v == userAgentBtn) {
            clickUserAgentBtn(v);
        } else if (v == netTypeBtn) {
            clickNetTypeBtn(v);
        } else if (v == osVersionBtn) {
            clickOsVersionBtn(v);
        } else if (v == adIdBtn) {
            clickAdBtn(v);
        } else if (v == cpuBtn) {
            clickCpuBtn(v);
        } else if (v == romSizeBtn) {
            clickRomSizeBtn(v);
        } else if (v == flashBtn) {
            clickFlashBtn(v);
        }
    }

    void requestPermission(Action successAction, String... permissions) {
        rxPermissions.request(permissions)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        successAction.run();
                    } else {
                        Toast.makeText(activity, Arrays.toString(permissions) + "申请失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void clickBtMacBtn(View v) {
        requestPermission(
                () -> btBtn.setText(btBtn.getText().toString()
                        + ": " + BluetoothAdapter.getDefaultAdapter().getAddress()),
                Manifest.permission.BLUETOOTH);
    }

    void clickAndroidIdBtn(View v) {
        androidIdBtn.setText(androidIdBtn.getText() + ": "
                + systemInfoHelper.getAndroidId());
    }

    void clickDeviceIdBtn(View v) {
        rxPermissions.request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                        Toast.makeText(activity, Manifest.permission.READ_PHONE_STATE + "申请失败",
                                Toast.LENGTH_SHORT).show();
                    }
                    deviceIdBtn.setText(deviceIdBtn.getText() + ": " + systemInfoHelper.getImei());
                });
    }

    void clickOperatorBtn(View v) {
        operatorBtn.setText(operatorBtn.getText() + ": "
                + systemInfoHelper.getSimOperatorName());
    }

    void clickWifiMacBtn(View v) {
        wifiMacBtn.setText(wifiMacBtn.getText() + ": "
                + systemInfoHelper.getMacAddress());
    }

    void clickSsidAndBssidBtn(View view) {
        ssidBtn.setText(ssidBtn.getText() + ": "
                + systemInfoHelper.getWifiSsid() + "/"
                + systemInfoHelper.getWifiBssid());
    }

    /*
        "/sbin/" "/system/bin/" "/system/xbin/" "/data/local/xbin/" "/data/local/bin/" "/system/sd/xbin/" "/system/bin/failsafe/" "/data/local/" 路径存在 su 执行文件
        "/system/app/Superuser.apk"
        Landroid/os/Build;->TAGS:Ljava/lang/String; const-string v5, "test-keys"
        invoke-virtual {v0, v5}, Ljava/lang/String;->contains(Ljava/lang/CharSequence;)Z
     */

    void clickIsRootBtn(View view) {
        systemInfoHelper.isRoot()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> isRootBtn.setText(isRootBtn.getText() + ": " + aBoolean));
    }

    void clickUserAgentBtn(View v) {
        userAgentBtn.setText(userAgentBtn.getText() + ": " + systemInfoHelper.getUserAgent());
    }

    void clickNetTypeBtn(View v) {
        netTypeBtn.setText(netTypeBtn.getText() + ": "
                + systemInfoHelper.getMobileNetworkType());
    }

    void clickOsVersionBtn(View v) {
        osVersionBtn.setText(osVersionBtn.getText() + ": "
                + systemInfoHelper.getSystemVersion());
    }

    void clickAdBtn(View v) {
        adIdBtn.setEnabled(false);
        systemInfoHelper.getGooglePlayServiceAdId()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        aString -> {
                            adIdBtn.setEnabled(true);
                            adIdBtn.setText(adIdBtn.getText() + ": " + aString);
                        },
                        throwable -> {
                            adIdBtn.setEnabled(true);
                            Toast.makeText(activity, throwable.toString(),
                                    Toast.LENGTH_SHORT).show();
                        });
    }

    void clickCpuBtn(View v) {
        cpuBtn.setEnabled(false);
        systemInfoHelper.getCpuProcessor()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        aString -> {
                            cpuBtn.setEnabled(true);
                            cpuBtn.setText(cpuBtn.getText() + ": " + aString);
                        },
                        throwable -> cpuBtn.setEnabled(true)
                );
    }

    void clickRomSizeBtn(View v) {
        romSizeBtn.setText(romSizeBtn.getText() + ": "
                + SystemInfoUtils.formatSize(systemInfoHelper.getRomSize()));
    }

    void clickFlashBtn(View v) {
        flashBtn.setText(flashBtn.getText() + ": " + SystemInfoUtils.getFlash(this));
    }

}
