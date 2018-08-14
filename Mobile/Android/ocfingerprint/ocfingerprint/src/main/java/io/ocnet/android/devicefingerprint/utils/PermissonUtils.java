package io.ocnet.android.devicefingerprint.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;


class PermissonUtils {

    /**
     * 判断指定的权限是否已授权
     * @param permissions 检查的权限
     * @return true表示已授权，false表示未授权
     */
    static boolean isGranted(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
