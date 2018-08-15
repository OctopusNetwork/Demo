package io.ocnet.android.devicefingerprint.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SystemInfoUtils {

    // 识别系统是否root的场景，需要检查下面目录是否存在
    private static final String[] ROOT_CHECK_FILES = new String[] {
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su",
            "/system/app/Superuser.apk"
    };

    /**
     * 格式化文件大小
     */
    public static String formatSize(long size) {
        String suffix = null;
        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }
        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }
        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    /**
     * 获取外部存储大小，如果手机不支持sd卡扩展，外部存储和内部存储都共用一个存储芯片，这个方法就是代表手机存储的大小
     */
    public static long getExternalStorageSize() {
        if (isExternalStorageAvailable()) {
            StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long blockSize = statFs.getBlockSize();
            long totalBlocks = statFs.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return 0L;
        }
    }

    /**
     * 获取内部存储大小
     */
    public static long getInternalStorageSize() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long blockSize = statFs.getBlockSize();
        long blockCount = statFs.getBlockCount();
        return blockCount * blockSize;
    }

    public static boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 获取cpu硬件信息
     */
    public static Observable<String> getCpuHardware() {
        return Observable.just("/proc/cpuinfo")
                .subscribeOn(Schedulers.io())
                .flatMap(aString -> {
                    FileReader fr = null;
                    BufferedReader br = null;
                    try {
                        fr = new FileReader(aString);
                        br = new BufferedReader(fr);
                        StringBuilder sb = new StringBuilder();
                        String oneLine;
                        while ((oneLine = br.readLine()) != null) {
                            sb.append(oneLine).append(";");
                        }
                        int index = sb.indexOf("Hardware");
                        return Observable.just(sb.substring(sb.indexOf(":", index), sb.indexOf(";", index)));
                    } catch (IOException ioe) {
                        return Observable.error(ioe);
                    } finally {
                        close(br, fr);
                    }
                });
    }

    /**
     * 获取cpu时钟频率
     */
    // see{@link https://stackoverflow.com/questions/3021054/how-to-read-cpu-frequency-on-android-device}
    public static Observable<String> getCpuFrequency() {
        return Observable.just("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq")
                .subscribeOn(Schedulers.io())
                .flatMap(aString -> {
                    FileReader fr = null;
                    BufferedReader br = null;
                    try {
                        fr = new FileReader(aString);
                        br = new BufferedReader(fr);
                        String oneline = br.readLine();
                        return Observable.just(oneline);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        return Observable.error(ioe);
                    } finally {
                        close(fr, br);
                    }
                });
    }

    /**
     * 判断当前系统是否root
     */
    public static Observable<Boolean> isRoot() {
        // see{@link http://stackoverflow.com/questions/1101380/determine-if-running-on-a-rooted-device}
        return Observable.just(ROOT_CHECK_FILES)
                .subscribeOn(Schedulers.io())
                .flatMap(files -> {
                    // 遍历系统一些目录，判断是否存在root的相关执行文件
                    File dirFile;
                    boolean isRoot = false;
                    for (String dir : files) {
                        dirFile = new File(dir);
                        isRoot |= dirFile.exists();
                    }
                    return Observable.just(isRoot);
                })
                .flatMap(aBoolean -> {
                    // 另外的一个方式判断是否root
                    String buildTags = android.os.Build.TAGS;
                    boolean isRoot = buildTags != null && buildTags.contains("test-keys");
                    return Observable.just(aBoolean | isRoot);
                });
    }

    /**
     * 获取flash
     */
    // 需要NDK去处理
    public static String getFlash(Context context) {
        return UuidUtils.nativeUuid(context);
    }

    private static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ////////////////////////////////////////////////////

    /**
     * cpu single core info
     */
    public static class CpuCoreInfo {
        /*
            processor	: 0
            model name	: ARMv7 Processor rev 1 (v7l)
            BogoMIPS	: 38.00
            Features	: swp half thumb fastmult vfp edsp neon vfpv3 tls vfpv4 idiva idivt
            CPU implementer	: 0x51
            CPU architecture: 7
            CPU variant	: 0x3
            CPU part	: 0x06f
            CPU revision	: 1
         */
        private String processor;
        private String model_name;
        private String BogoMIPS;
        private String Features;
        private String CPU_implementer;
        private String CPU_architecture;
        private String CPU_variant;
        private String CPU_part;
        private String CPU_revision;

        public CpuCoreInfo(
                String processor, String model_name, String bogoMIPS,
                String features, String CPU_implementer, String CPU_architecture,
                String CPU_variant, String CPU_part, String CPU_revision) {
            this.processor = processor;
            this.model_name = model_name;
            BogoMIPS = bogoMIPS;
            Features = features;
            this.CPU_implementer = CPU_implementer;
            this.CPU_architecture = CPU_architecture;
            this.CPU_variant = CPU_variant;
            this.CPU_part = CPU_part;
            this.CPU_revision = CPU_revision;
        }

        public String getProcessor() {
            return processor;
        }

        public String getModel_name() {
            return model_name;
        }

        public String getBogoMIPS() {
            return BogoMIPS;
        }

        public String getFeatures() {
            return Features;
        }

        public String getCPU_implementer() {
            return CPU_implementer;
        }

        public String getCPU_architecture() {
            return CPU_architecture;
        }

        public String getCPU_variant() {
            return CPU_variant;
        }

        public String getCPU_part() {
            return CPU_part;
        }

        public String getCPU_revision() {
            return CPU_revision;
        }
    }

    /**
     * cpu total info
     */
    public static class CpuInfo {
        /*
            Hardware	: Qualcomm APQ 8084 (Flattened Device Tree)
            Revision	: 83a0
            Serial		: 7fda6a0011000000
            Device		: shamu
            Radio		: 7
            MSM Hardware	: APQ8084 ES1.1
         */
        private List<CpuCoreInfo> coreInfos;
        private String Hardware;
        private String Revision;
        private String Serial;
        private String Device;
        private String Radio;
        private String MSM_Hardware;

        public CpuInfo(
                List<CpuCoreInfo> coreInfos, String hardware, String revision,
                String serial, String device, String radio, String MSM_Hardware) {
            this.coreInfos = coreInfos;
            Hardware = hardware;
            Revision = revision;
            Serial = serial;
            Device = device;
            Radio = radio;
            this.MSM_Hardware = MSM_Hardware;
        }

        public List<CpuCoreInfo> getCoreInfos() {
            return coreInfos;
        }

        public String getHardware() {
            return Hardware;
        }

        public String getRevision() {
            return Revision;
        }

        public String getSerial() {
            return Serial;
        }

        public String getDevice() {
            return Device;
        }

        public String getRadio() {
            return Radio;
        }

        public String getMSM_Hardware() {
            return MSM_Hardware;
        }
    }
}
