
#import <UIKit/UIKit.h>

@interface UIDevice (Fingerprint)

+ (NSString *)ocm_deviceModel;

+ (NSString *)ocm_platformLanguage;

+ (NSString *)ocm_platFormVersion;

+ (NSString *)ocm_bundleIdentifier;

+ (NSString *)ocm_appVersion;

+ (NSString *)ocm_IDFAString;

+ (NSString *)ocm_IDFVString;

+ (NSString *)ocm_platform;

+ (NSString *)ocm_carrierName;

+ (NSString *)ocm_userAgent;

+ (NSString *)ocm_deviceID;

+ (NSInteger)ocm_networkType;

+ (NSInteger)ocm_isEmulator;

+ (NSInteger)ocm_isJailBreak;

@end
