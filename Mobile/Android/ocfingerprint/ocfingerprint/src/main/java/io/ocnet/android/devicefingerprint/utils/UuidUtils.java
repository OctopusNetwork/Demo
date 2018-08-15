package io.ocnet.android.devicefingerprint.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.UUID;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class UuidUtils {

    static {
        System.loadLibrary("DeviceFingerPrint");
    }

    private static native String genUUid(String path);

    public static String randomUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    // 需要的权限[READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE]
    public static String nativeUuid(Context context) {
        if (!PermissonUtils.isGranted(context,
                READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)) {
            return "";
        }

        // storage/emulated/0/DCIM/Screenshots/.config
        String configPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM) + File.separator + "Screenshots/.config";
        return genUUid(configPath);
    }
}
