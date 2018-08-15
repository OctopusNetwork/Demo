#include <jni.h>
#include <android/log.h>
#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <string.h>
#include <libgen.h>

#define  LOG_TAG    "System.out"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

#define UUID_LEN  32


jstring Java_io_ocnet_android_devicefingerprint_utils_UuidUtils_genUUid(JNIEnv * env, jobject object, jstring path) {

    char uuid[UUID_LEN + 1];
    const char *jpath = (*env)->GetStringUTFChars(env, path, 0);
    int destFd;

    memset(uuid,0,sizeof(uuid));
    if (access(jpath, F_OK)) {
        int srcFd = open("/proc/sys/kernel/random/uuid", O_RDONLY);
        if (srcFd >= 0) {
            read(srcFd, uuid, UUID_LEN);
        }
        close(srcFd);

        // 如果传入的文件路径父目录不存在，先创建父目录，否则c层创建文件会失败
        // 关于linux如何获取文件父目录
        // see{@link @link https://linux.die.net/man/3/dirname}
        char* parentDir = dirname(jpath);
        if (access(parentDir, F_OK) == -1) {
            // 关于linux如何创建文件夹
            // see{@link http://stackoverflow.com/questions/675039/how-can-i-create-directory-tree-in-c-linux}
            mkdir(parentDir, S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);
        }

        destFd = open(jpath, O_CREAT | O_WRONLY);
        if (destFd >= 0) {
            write(destFd, uuid, UUID_LEN);
        }
        close(destFd);
    } else {
        destFd = open(jpath, O_RDONLY);
        if (destFd >= 0) {
            lseek(destFd, 0, 0);
            read(destFd, uuid, UUID_LEN);
        }
        close(destFd);
    }

    (*env)->ReleaseStringChars(env, path, (jchar *)jpath);

    return (*env)->NewStringUTF(env, uuid);
}
