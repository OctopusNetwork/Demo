#import <Foundation/Foundation.h>

@interface NSData (AES)

- (NSData *)AESEncryptWithKey:(NSString*)key;

- (NSData *)AESDecryptWithKey:(NSString*)key;

@end
