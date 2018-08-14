package io.ocnet.android.devicefingerprint.entity;


import java.io.Serializable;

public class DeviceInfo implements Serializable {

    public static class Basic implements Serializable {
        private String IMEI;
        private String AD_ID;
        private String ANDROID_ID;
        private String Version;

        public Basic() {}

        public Basic(String imei, String ad_id, String android_id, String version) {
            this.IMEI = imei;
            this.AD_ID = ad_id;
            this.ANDROID_ID = android_id;
            this.Version = version;
        }

        public String getIMEI() {
            return IMEI;
        }

        public void setIMEI(String IMEI) {
            this.IMEI = IMEI;
        }

        public String getAD_ID() {
            return AD_ID;
        }

        public void setAD_ID(String AD_ID) {
            this.AD_ID = AD_ID;
        }

        public String getANDROID_ID() {
            return ANDROID_ID;
        }

        public void setANDROID_ID(String ANDROID_ID) {
            this.ANDROID_ID = ANDROID_ID;
        }

        public String getVersion() {
            return Version;
        }

        public void setVersion(String version) {
            this.Version = version;
        }

        @Override
        public String toString() {
            return "{ IMEI: " + IMEI
                    + ", AD_ID: " + AD_ID
                    + ", ANDROID_ID: " + ANDROID_ID
                    + ", Version: " + Version
                    + " }";
        }
    }

    public static class Wifi implements Serializable {
        private String SSID;
        private String BASSID;
        private String MacAddress;

        public Wifi() {}

        public Wifi(String ssid, String bassid, String macaddress) {
            this.SSID = ssid;
            this.BASSID = bassid;
            this.MacAddress = macaddress;
        }

        public String getSSID() {
            return SSID;
        }

        public void setSSID(String SSID) {
            this.SSID = SSID;
        }

        public String getBASSID() {
            return BASSID;
        }

        public void setBASSID(String BASSID) {
            this.BASSID = BASSID;
        }

        public String getMacAddress() {
            return MacAddress;
        }

        public void setMacAddress(String macAddress) {
            this.MacAddress = macAddress;
        }

        @Override
        public String toString() {
            return "{ SSID: " + SSID
                    + ", BASSID: " + BASSID
                    + ", MacAddress: " + MacAddress
                    + " }";
        }
    }

    public static class Network implements Serializable {
        private String UserAgent;
        private String SimOperatorName;
        private String NetworkType;
        private String PhoneType;
        private String ActiveNetworkInfo;
        private String telno; // 手机号码

        public Network() {}

        public Network(String useragent, String simoperatorname, String networktype,
                       String phonetype, String activenetworkinfo, String telno) {
            this.UserAgent = useragent;
            this.SimOperatorName = simoperatorname;
            this.NetworkType = networktype;
            this.PhoneType = phonetype;
            this.ActiveNetworkInfo = activenetworkinfo;
        }

        public String getUserAgent() {
            return UserAgent;
        }

        public void setUserAgent(String userAgent) {
            this.UserAgent = userAgent;
        }

        public String getSimOperatorName() {
            return SimOperatorName;
        }

        public void setSimOperatorName(String simOperatorName) {
            this.SimOperatorName = simOperatorName;
        }

        public String getNetworkType() {
            return NetworkType;
        }

        public void setNetworkType(String networkType) {
            this.NetworkType = networkType;
        }

        public String getPhoneType() {
            return PhoneType;
        }

        public void setPhoneType(String phoneType) {
            this.PhoneType = phoneType;
        }

        public String getActiveNetworkInfo() {
            return ActiveNetworkInfo;
        }

        public void setActiveNetworkInfo(String activeNetworkInfo) {
            this.ActiveNetworkInfo = activeNetworkInfo;
        }

        public String getTelno() {
            return telno;
        }

        public void setTelno(String telno) {
            this.telno = telno;
        }

        @Override
        public String toString() {
            return "{ UserAgent: " + UserAgent
                    + ", SimOperatorName: " + SimOperatorName
                    + ", NetworkType: " + NetworkType
                    + ", PhoneType: " + PhoneType
                    + ", ActiveNetworkInfo: " + ActiveNetworkInfo
                    + ", telno: " + telno
                    + " }";
        }
    }

    public static class Hardware implements Serializable {
        private String Processorf;
        private long ROM;
        private String clockSkew;

        public Hardware() {}

        public Hardware(String processor, long rom, String clockSkew) {
            this.Processorf = processor;
            this.ROM = rom;
            this.clockSkew = clockSkew;
        }

        public String getProcessorf() {
            return Processorf;
        }

        public void setProcessorf(String processorf) {
            this.Processorf = processorf;
        }

        public long getROM() {
            return ROM;
        }

        public void setROM(long ROM) {
            this.ROM = ROM;
        }

        public String getClockSkew() {
            return clockSkew;
        }

        public Hardware setClockSkew(String clockSkew) {
            this.clockSkew = clockSkew;
            return this;
        }

        @Override
        public String toString() {
            return "{ Processorf: " + Processorf
                    + ", ROM: " + ROM
                    + ", clockSkew: " + clockSkew
                    + " }";
        }
    }

    public static class Malicious implements Serializable {
        private boolean ROOT;
        private boolean Emulator;
        private boolean HOOK;
        private boolean MalFrame;

        public Malicious() {}

        public Malicious(boolean root, boolean emulator, boolean hook, boolean malframe) {
            this.ROOT = root;
            this.Emulator = emulator;
            this.HOOK = hook;
            this.MalFrame = malframe;
        }

        public boolean isROOT() {
            return ROOT;
        }

        public void setROOT(boolean ROOT) {
            this.ROOT = ROOT;
        }

        public boolean isEmulator() {
            return Emulator;
        }

        public void setEmulator(boolean emulator) {
            this.Emulator = emulator;
        }

        public boolean isHOOK() {
            return HOOK;
        }

        public void setHOOK(boolean HOOK) {
            this.HOOK = HOOK;
        }

        public boolean isMalFrame() {
            return MalFrame;
        }

        public void setMalFrame(boolean malFrame) {
            this.MalFrame = malFrame;
        }

        @Override
        public String toString() {
            return "{ ROOT: " + ROOT
                    + ", Emulator: " + Emulator
                    + ", HOOK: " + HOOK
                    + ", MalFrame: " + MalFrame
                    + " }";
        }
    }

    /*
       {
            "TOKEN":"",
            "FHASH": "",
            "device", "mobile", // 常量
            "os", "android",
            "Basic":{
                   "IMEI":"",
                   "AD_ID":"",
                   "ANDROID_ID":"",
                   "Version":"",
            },
            "Wifi":{
                   "SSID":"",
                   "BSSID":"",
                   "MacAddress":"",
            },
            "Network":{
                   "UserAgent":"",
                   "SimOperatorName":"中国移动",
                   "NetworkType":"HSPA, CDMA, etc.",
                   "PhoneType":"NONE, GSM, SIP, etc.",
                   "ActiveNetworkInfo":"WIFI, MOBILE, WIMAX, etc.",
                   "PhoneNumber": "",
                   "telno", 12345678901
            },
            "HW":{
                   "Processorf":"AArch64 Processor rev 2 (aarch64)",
                   "ROM":"",
            },
            "Mal":{
                   "ROOT":"",
                   "Emulator":"",
                   "HOOK":"",
                   "MalFrame":"",
            },
        }
     */
    private String TOKEN; // 客户端随机生成的uuid
    private String FHASH;
    private String device = "mobile"; // 常量，无须修改
    private String os = "android"; // 常量，无须修改
    private Basic Basic;
    private Wifi Wifi;
    private Network Network;
    private Hardware HW;
    private Malicious Mal;

    public DeviceInfo() {}

    public DeviceInfo(String token, String fhash, Basic basic, Wifi wifi,
                      Network network, Hardware hw, Malicious malicious) {
        this.TOKEN = token;
        this.FHASH = fhash;
        this.Basic = basic;
        this.Wifi = wifi;
        this.Network = network;
        this.HW = hw;
        this.Mal = malicious;
    }

    public String getDevice() {
        return device;
    }

    public DeviceInfo setDevice(String device) {
        this.device = device;
        return this;
    }

    public String getOs() {
        return os;
    }

    public DeviceInfo setOs(String os) {
        this.os = os;
        return this;
    }

    public Network getNetwork() {
        return Network;
    }

    public void setNetwork(Network network) {
        this.Network = network;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public String getFHASH() {
        return FHASH;
    }

    public void setFHASH(String FHASH) {
        this.FHASH = FHASH;
    }

    public Basic getBasic() {
        return Basic;
    }

    public void setBasic(Basic basic) {
        this.Basic = basic;
    }

    public Wifi getWifi() {
        return Wifi;
    }

    public void setWifi(Wifi wifi) {
        this.Wifi = wifi;
    }

    public Hardware getHW() {
        return HW;
    }

    public void setHW(Hardware HW) {
        this.HW = HW;
    }

    public Malicious getMal() {
        return Mal;
    }

    public void setMal(Malicious mal) {
        this.Mal = mal;
    }

    @Override
    public String toString() {
        return "{ TOKEN: " + TOKEN
                + ", FHASH: " + FHASH
                + ", device: " + device
                + ", os: " + os
                + ", Basic: " + Basic
                + ", Wifi: " + Wifi
                + ", Network: " + Network
                + ", HW: " + HW
                + ", Mal: " + Mal
                + " }";
    }
}
