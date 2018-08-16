#import <Foundation/Foundation.h>

@interface OCMDeviceFingerprint : NSObject

// 设置要上传的group name
+ (void)setGroupName:(NSString *)groupName;

// 设置设备指纹服务器地址
+ (void)setUrl:(NSString *)url;

// 上传数据
+ (void)upload;

// 上传参数并拿到回调结果
+ (void)uploadWithBlock:(void (^)(id response, NSError *error))block;

// 直接获取指纹数据信息
+ (NSDictionary *)deviceInfo;

@end
