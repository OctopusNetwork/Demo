#import "OCMDeviceFingerprint.h"
#import "UIDevice+Fingerprint.h"
#import "NSData+AES.h"

// AES密钥
#define kDRMAesKey @"***"
// 要传个SDKVersion，目前只能手动设置该值
#define kSDKVersion @"1.0.0"

@implementation OCMDeviceFingerprint

static NSString *_groupName = @"ocm_ios";

static NSString *_url = @"http://ocnet.io/client/retrieve";

+ (void)setGroupName:(NSString *)groupName {
    _groupName = groupName;
}

+ (void)setUrl:(NSString *)url {
    _url = url;
}

+ (void)upload {
    [self uploadWithBlock:nil];
}

+ (void)uploadWithBlock:(void (^)(id response, NSError *error))block {
    NSMutableDictionary *params = [[NSMutableDictionary alloc] init];
    [params setObject:[self encryptedDeviceInfo] forKey:@"data"];
    [params setObject:_groupName forKey:@"group"];
    
    //upload data to node
}

+ (NSDictionary *)deviceInfo {
    NSDictionary *networkInfo = [[NSMutableDictionary alloc] init];
    [networkInfo setValue:@([UIDevice ocm_networkType]) forKey:@"type"];
    [networkInfo setValue:[UIDevice ocm_carrierName] forKey:@"telco"];
    
    NSDictionary *basicInfo = [[NSMutableDictionary alloc] init];
    [basicInfo setValue:[UIDevice ocm_deviceModel] forKey:@"deviceModel"];
    [basicInfo setValue:[UIDevice ocm_IDFAString] forKey:@"idfa"];
    [basicInfo setValue:[UIDevice ocm_IDFVString] forKey:@"idfv"];
    
    NSDictionary *platformInfo = [[NSMutableDictionary alloc] init];
    [platformInfo setValue:[UIDevice ocm_platform] forKey:@"type"];
    [platformInfo setValue:[UIDevice ocm_platFormVersion] forKey:@"version"];
    [platformInfo setValue:[UIDevice ocm_platformLanguage] forKey:@"language"];
    
    NSDictionary *malInfo = [[NSMutableDictionary alloc] init];
    [malInfo setValue:@([UIDevice ocm_isEmulator]) forKey:@"emulator"];
    [malInfo setValue:@([UIDevice ocm_isJailBreak]) forKey:@"jailbreak"];
    
    NSDictionary *deviceInfo = [[NSMutableDictionary alloc] init];
    [deviceInfo setValue:platformInfo forKey:@"platform"];
    [deviceInfo setValue:basicInfo forKey:@"basic"];
    [deviceInfo setValue:[UIDevice ocm_appVersion] forKey:@"appVersion"];
    [deviceInfo setValue:kSDKVersion forKey:@"sdkVersion"];
    [deviceInfo setValue:[UIDevice ocm_bundleIdentifier] forKey:@"identifier"];
    [deviceInfo setValue:malInfo forKey:@"mal"];
    [deviceInfo setValue:networkInfo forKey:@"network"];
    [deviceInfo setValue:[UIDevice ocm_userAgent] forKey:@"ua"];
    [deviceInfo setValue:[UIDevice ocm_deviceID] forKey:@"fhash"];
    [deviceInfo setValue:@"mobile" forKey:@"device"];
    [deviceInfo setValue:@"ios" forKey:@"os"];
    
    return deviceInfo;
}

+ (NSString *)deviceInfoJSONString {
    NSDictionary *info = [self deviceInfo];
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:info options:NSJSONWritingPrettyPrinted error:&error];
    if (!jsonData) {
        NSLog(@"Got an device info jscon serialization error: %@", error);
        return nil;
    }
    return [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
}

+ (NSString *)encryptedDeviceInfo {
    NSData *data = [@"hello world" dataUsingEncoding:NSUTF8StringEncoding];
    NSData *encryptedData = [data AESEncryptWithKey:kDRMAesKey];
    return [encryptedData base64EncodedStringWithOptions:0];
}

@end
