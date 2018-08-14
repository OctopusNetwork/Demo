package io.ocnet.android.devicefingerprint.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.IOException;

import io.ocnet.android.devicefingerprint.entity.DeviceInfo;
import io.ocnet.android.devicefingerprint.entity.RetrieveEntity;
import io.ocnet.fingerprint.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;

public class SystemInfoHelper {

    private Context context;
    private TelephonyManager telephonyMgr;
    private WifiManager wifiMgr;
    private ConnectivityManager connectivityMgr;
    private Request request;

    private String ocnetGroup;

    public SystemInfoHelper(Context context) {
        this.context = context;
    }

    /**
     * 设置app名称
     */
    public void setOcnetGroup(String app) {
        this.ocnetGroup = app;
    }

    /**
     * 上传device info到区块链，通过RPC方式访问
     */
//    public Observable<RetrieveEntity> upload() {
//        return getDeviceInfo()
//                .subscribeOn(Schedulers.io())
//                .flatMap(deviceInfo -> {
//                    try {
//                        String json = new ObjectMapper().writeValueAsString(deviceInfo);
//                        // 将device info的pojo转成json string，然后aes加密
//                        String aesKey = context.getString(R.string.drdevicefingerprint_key);
//                        String encryptString = AesUtils.encrypt(json, aesKey);
//
//                        Log.i("ocfingerprint", String.format("SystemInfoHelper.upload,\njson: %s,\nencrypt: %s,\ndecrypt: %s",
//                                json, encryptString,
//                                AesUtils.decrypt(encryptString, aesKey)
//                        ));
//                        return Observable.just(encryptString);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.e("ocfingerprint", "encrypt error: " + android.util.Log.getStackTraceString(e));
//                        return Observable.error(e);
//                    }
//                })
//                .flatMap(encryptionString ->
//                        ServiceCreator
//                                .create("devicefingerprint",
//                                        request.deviceFingerprint(Request.API_DEVICE_FINGERPRINT,
//                                                encryptionString, ocnetGroup))
//                                .toObservable()
//                                .flatMap(contentWrapper -> Observable.just(contentWrapper.getContent())))
//                .observeOn(AndroidSchedulers.mainThread());
//    }

    /**
     * 收集系统的一些参数信息，组织成{@link DeviceInfo}返回
     */
    public Observable<DeviceInfo> getDeviceInfo() {
        return Observable.just(new Object())
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(aVoid -> {
                    DeviceInfo deviceInfo = new DeviceInfo();
                    // token
                    deviceInfo.setTOKEN(UuidUtils.randomUuid());
                    // flash
                    deviceInfo.setFHASH(SystemInfoUtils.getFlash(context));
                    // basic
                    DeviceInfo.Basic basic = new DeviceInfo.Basic();
                    basic.setANDROID_ID(getAndroidId());
                    basic.setIMEI(getImei());
                    basic.setVersion(getSystemVersion());
                    // 这个是异步的，后面异步去获取
                    basic.setAD_ID(null);
                    deviceInfo.setBasic(basic);
                    // wifi
                    DeviceInfo.Wifi wifi = new DeviceInfo.Wifi();
                    wifi.setBASSID(getWifiBssid());
                    wifi.setSSID(getWifiSsid());
                    wifi.setMacAddress(getMacAddress());
                    deviceInfo.setWifi(wifi);
                    // network
                    DeviceInfo.Network network = new DeviceInfo.Network();
                    network.setActiveNetworkInfo(getActiveNetworkInfo());
                    network.setNetworkType(getMobileNetworkType());
                    network.setPhoneType(getPhoneType());
                    network.setSimOperatorName(getSimOperatorName());
                    network.setUserAgent(getUserAgent());
                    deviceInfo.setNetwork(network);
                    // hardware
                    DeviceInfo.Hardware hardware = new DeviceInfo.Hardware();
                    // 这个是异步的，后面异步去取
                    hardware.setProcessorf(null);
                    hardware.setROM(getRomSize());
                    // 异步，后面去取
                    hardware.setClockSkew(null);
                    deviceInfo.setHW(hardware);
                    // malicious
                    DeviceInfo.Malicious mal = new DeviceInfo.Malicious();
                    mal.setEmulator(isEmulator());
                    mal.setHOOK(isHook());
                    // 异步，后面异步去取
                    mal.setROOT(false);
                    // 暂时不做
                    mal.setMalFrame(false);
                    deviceInfo.setMal(mal);

                    return Observable.just(deviceInfo);
                })
                .flatMap(deviceInfo ->
                        // 获取系统是否root
                        isRoot()
                                .flatMap(isRoot -> {
                                    deviceInfo.getMal().setROOT(isRoot);
                                    return Observable.just(deviceInfo);
                                })
                )
                .flatMap(deviceInfo ->
                        // 获取google play service ad id
                        getGooglePlayServiceAdId()
                                .subscribeOn(Schedulers.io())
                                .onErrorResumeNext(throwable -> {
                                    return Observable.just("");
                                })
                                .flatMap(adId -> {
                                    deviceInfo.getBasic().setAD_ID(adId);
                                    return Observable.just(deviceInfo);
                                })
                )
                .flatMap(deviceInfo ->
                        // 获取cpu processor
                        getCpuProcessor()
                                .subscribeOn(Schedulers.io())
                                .flatMap(cpuProcessor -> {
                                    deviceInfo.getHW().setProcessorf(cpuProcessor);
                                    return Observable.just(deviceInfo);
                                })
                )
                .flatMap(deviceInfo ->
                        // 获取cpu时钟频率
                        getCpuFrequency()
                                .subscribeOn(Schedulers.io())
                                .flatMap(frequency -> {
                                    deviceInfo.getHW().setClockSkew(frequency);
                                    return Observable.just(deviceInfo);
                                })
                );
    }

    /**
     * 获取设备的imei，需要{@link android.Manifest.permission#READ_PHONE_STATE}权限，
     * 如果未获得权限，返回值未空
     */
    public String getImei() {
        String deviceId = "";
        if (!PermissonUtils.isGranted(context, Manifest.permission.READ_PHONE_STATE)) {
            return deviceId;
        }
        try {
            deviceId = getTelephonyMgr().getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceId;
    }

    // google play service ad id
    public Observable<String> getGooglePlayServiceAdId() {
        return Observable.just(null)
                .subscribeOn(Schedulers.io())
                .flatMap(aVoid -> {
                    String adId = null;
                    GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
                    if (ConnectionResult.SUCCESS == apiAvailability.isGooglePlayServicesAvailable(context)) {
                        try{
                            AdvertisingIdClient.Info info = new AdvertisingIdClient(context).getInfo();
                            if (!info.isLimitAdTrackingEnabled()) {
                                adId = new AdvertisingIdClient(context).getInfo().getId();
                            } else {
                                return Observable.error(new Error("google play service ad tracking is disable"));
                            }
                        } catch (IOException e) {
                            return Observable.error(e);
                        }
                    } else {
                        return Observable.error(new Error("google play service is not connected"));
                    }
                    return Observable.just(adId);
                });
    }

    public String getAndroidId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getSystemVersion() {
        String versionName = context.getPackageName();
        int versionCode = -1;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(versionName, 0);
            versionCode = info.versionCode;
        } catch (Exception e) {
        }
        return versionName + "/" + versionCode;
    }

    public String getWifiSsid() {
        if (!PermissonUtils.isGranted(context, Manifest.permission.ACCESS_WIFI_STATE)) {
            return "";
        }

        String ssid = getWifiManager().getConnectionInfo().getSSID();
        // 系统返回的ssid名称有可能返回包括分号的名称，这里处理一下
        if (!TextUtils.isEmpty(ssid) && ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        return ssid;
    }

    public String getWifiBssid() {
        if (!PermissonUtils.isGranted(context, Manifest.permission.ACCESS_WIFI_STATE)) {
            return "";
        }

        return getWifiManager().getConnectionInfo().getBSSID().trim();
    }

    /**
     * 获取wifi的mac地址
     */
    public String getMacAddress() {
        return getWifiManager().getConnectionInfo().getMacAddress();
    }

    /**
     * 获取webview的useragent
     */
    public String getUserAgent() {
        return System.getProperty("http.agent");
    }

    /**
     * 获取运营商名称
     */
    public String getSimOperatorName() {
        return getTelephonyMgr().getSimOperatorName();
    }

    /**
     * 获取移动网络类型，如3G，4G LTE
     */
    public String getMobileNetworkType() {
        int type = getTelephonyMgr().getNetworkType();
        String typeString = "unknow";
        if (type == TelephonyManager.NETWORK_TYPE_CDMA) {
            typeString = "cdma";
        } else if (type == TelephonyManager.NETWORK_TYPE_1xRTT) {
            typeString = "1xRTT";
        } else if (type == TelephonyManager.NETWORK_TYPE_EDGE) {
            typeString = "edge";
        } else if (type == TelephonyManager.NETWORK_TYPE_EHRPD) {
            typeString = "ehrpd";
        } else if (type == TelephonyManager.NETWORK_TYPE_EVDO_0) {
            typeString = "evdo_0";
        } else if (type == TelephonyManager.NETWORK_TYPE_EVDO_A) {
            typeString = "evdo_a";
        } else if (type == TelephonyManager.NETWORK_TYPE_EVDO_B) {
            typeString = "evdo_b";
        } else if (type == TelephonyManager.NETWORK_TYPE_GPRS) {
            typeString = "gprs";
        } else if (type == TelephonyManager.NETWORK_TYPE_HSDPA) {
            typeString = "hsdpa";
        } else if (type == TelephonyManager.NETWORK_TYPE_HSPA) {
            typeString = "hspa";
        } else if (type == TelephonyManager.NETWORK_TYPE_HSPAP) {
            typeString = "hspap";
        } else if (type == TelephonyManager.NETWORK_TYPE_GSM) {
            typeString = "gsm";
        } else if (type == TelephonyManager.NETWORK_TYPE_HSUPA) {
            typeString = "hsupa";
        } else if (type == TelephonyManager.NETWORK_TYPE_IDEN) {
            typeString = "iden";
        } else if (type == TelephonyManager.NETWORK_TYPE_IWLAN) {
            typeString = "iwlan";
        } else if (type == TelephonyManager.NETWORK_TYPE_LTE) {
            typeString = "lte";
        } else if (type == TelephonyManager.NETWORK_TYPE_TD_SCDMA) {
            typeString = "td_scdma";
        } else if (type == TelephonyManager.NETWORK_TYPE_UMTS) {
            typeString = "umts";
        }

        return typeString;
    }

    /**
     * 获取移动网络制式，如GSM，CDMA等
     */
    public String getPhoneType() {
        int type = getTelephonyMgr().getPhoneType();
        String typeString = "none";
        if (type == TelephonyManager.PHONE_TYPE_CDMA) {
            typeString = "cdma";
        } else if (type == TelephonyManager.PHONE_TYPE_GSM) {
            typeString = "gsm";
        } else if (type == TelephonyManager.PHONE_TYPE_SIP) {
            typeString = "sip";
        }
        return typeString;
    }

    /**
     * 获取当前网络类型，如mobile或者wifi
     */
    public String getActiveNetworkInfo() {
        if (!PermissonUtils.isGranted(context, Manifest.permission.ACCESS_WIFI_STATE)) {
            return "";
        }

        if (getConnectivityManager() != null
                && getConnectivityManager().getActiveNetworkInfo() != null
                && getConnectivityManager().getActiveNetworkInfo().isAvailable()) {
            return getConnectivityManager().getActiveNetworkInfo().getTypeName();
        } else {
            return "none";
        }
    }

    /**
     * 获取设备CPU的信息
     */
    public Observable<String> getCpuProcessor() {
        return SystemInfoUtils.getCpuHardware();
    }

    /**
     * 获取设备CPU时钟频率
     */
    public Observable<String> getCpuFrequency() {
        return SystemInfoUtils.getCpuFrequency();
    }

    /**
     * 获取设备ROM大小
     */
    public long getRomSize() {
        return SystemInfoUtils.getExternalStorageSize();
    }

    /**
     * 设备是否已root
     */
    public Observable<Boolean> isRoot() {
        return SystemInfoUtils.isRoot();
    }

    /**
     * 设备是否是模拟器
     */
    public boolean isEmulator() {
        // see{@link http://stackoverflow.com/questions/2799097/how-can-i-detect-when-an-android-application-is-running-in-the-emulator?page=1&tab=votes#tab-top}
        return Build.FINGERPRINT.contains("generic");
    }

    // TODO
    public boolean isHook() {
        return false;
    }

    private TelephonyManager getTelephonyMgr() {
        if (telephonyMgr != null) {
            return telephonyMgr;
        }
        telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyMgr;
    }

    private WifiManager getWifiManager() {
        if (wifiMgr != null) {
            return wifiMgr;
        }
        wifiMgr = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        return wifiMgr;
    }

    private ConnectivityManager getConnectivityManager() {
        if (connectivityMgr != null) {
            return connectivityMgr;
        }
        connectivityMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityMgr;
    }

}
