#import <AdSupport/ASIdentifierManager.h>
#import <CoreTelephony/CTCarrier.h>
#import <CoreTelephony/CTTelephonyNetworkInfo.h>
#import <SAMKeychain/SAMKeychain.h>
#import <sys/types.h>
#import <sys/sysctl.h>

#import "UIDevice+Fingerprint.h"
#import "TargetConditionals.h"

@implementation UIDevice (Fingerprint)

+ (NSString *)ocm_deviceModel {
    return [[UIDevice currentDevice] model];
}

+ (NSString *)ocm_platformLanguage {
    NSString *locale = [[NSLocale currentLocale] localeIdentifier];
    return locale;
}

+ (NSString *)ocm_platFormVersion {
    return [[UIDevice currentDevice] systemVersion];
}

+ (NSString *)ocm_bundleIdentifier {
    return [[NSBundle mainBundle] bundleIdentifier];
}

+ (NSString *)ocm_appVersion {
    return [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"];
}

+ (NSInteger)ocm_networkType {
    NSArray *subviews = [[[[UIApplication sharedApplication] valueForKey:@"statusBar"] valueForKey:@"foregroundView"]subviews];
    NSNumber *dataNetworkItemView = nil;
    
    for (id subview in subviews) {
        if([subview isKindOfClass:[NSClassFromString(@"UIStatusBarDataNetworkItemView") class]]) {
            dataNetworkItemView = subview;
            break;
        }
    }
    
    NSInteger type = [[dataNetworkItemView valueForKey:@"dataNetworkType"] integerValue];
    return type;
}

+ (NSString *)ocm_IDFAString {
    return [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
}

+ (NSString *)ocm_IDFVString {
    return [[[UIDevice currentDevice] identifierForVendor] UUIDString];
}

+ (NSInteger)ocm_isEmulator {
#if !(TARGET_OS_SIMULATOR)
    return 0;
#else
    return 1;
#endif
}

+ (NSInteger)ocm_isJailBreak {
    NSFileManager *fileManager = [NSFileManager defaultManager];
    if ([fileManager fileExistsAtPath:@"/Applications/Cydia.app"] ||
        [fileManager fileExistsAtPath:@"/Library/MobileSubstrate/MobileSubstrate.dylib"] ||
        [fileManager fileExistsAtPath:@"/bin/bash"] ||
        [fileManager fileExistsAtPath:@"/usr/sbin/sshd"] ||
        [fileManager fileExistsAtPath:@"/etc/apt"] ||
        [fileManager fileExistsAtPath:@"/private/var/lib/apt/"]) {
        return 1;
    }
    return 0;
}

+ (NSString *)ocm_platform {
    size_t size;
    sysctlbyname("hw.machine", NULL, &size, NULL, 0);
    char *machine = malloc(size);
    sysctlbyname("hw.machine", machine, &size, NULL, 0);
    NSString *platform = [NSString stringWithUTF8String:machine];
    free(machine);
    return platform;
}

+ (NSString *)ocm_carrierName {
    CTTelephonyNetworkInfo* info = [[CTTelephonyNetworkInfo alloc] init];
    CTCarrier* carrier = info.subscriberCellularProvider;
    NSString *carrierName = carrier.carrierName;
    // 应业务需求直接hardcode..
    if ([carrierName isEqualToString:@"中国联通"]) {
        carrierName = @"chinaunion";
    } else if ([carrierName isEqualToString:@"中国移动"]) {
        carrierName = @"cmcc";
    } else if ([carrierName isEqualToString:@"中国电信"]) {
        carrierName = @"chinanet";
    }
    return carrierName;
}

+ (NSString *)ocm_userAgent {
    UIWebView *webView = [[UIWebView alloc] initWithFrame:CGRectZero];
    return [webView stringByEvaluatingJavaScriptFromString:@"navigator.userAgent"];
}

+ (NSString *)ocm_deviceID {
    static NSString *key = @"ocm.deviceid";
    NSString *deviceId = [SAMKeychain passwordForService:key account:key];
    if (deviceId.length > 0) {
        return deviceId;
    }
    deviceId = [[NSUUID UUID] UUIDString];
    [SAMKeychain setPassword:deviceId forService:key account:key];
    return deviceId;
}


@end
