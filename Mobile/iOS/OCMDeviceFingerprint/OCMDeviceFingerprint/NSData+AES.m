
#import <CommonCrypto/CommonCryptor.h>
#import "NSData+AES.h"

@implementation NSData (AES)

- (NSData *)AESEncryptWithKey:(NSString*)key {
    NSData *mData = [key dataUsingEncoding:NSUTF8StringEncoding];
    CCCryptorStatus ccStatus = kCCSuccess;
    size_t bytesNeeded = 0;
    ccStatus = CCCrypt(kCCEncrypt,
                       kCCAlgorithmAES,
                       kCCOptionECBMode | kCCOptionPKCS7Padding,
                       [mData bytes],
                       [mData length],
                       nil,
                       [self bytes],
                       [self length],
                       NULL,
                       0,
                       &bytesNeeded);
    if(kCCBufferTooSmall != ccStatus){
        NSLog(@"Here it must return BUFFER TOO SMALL !!");
        return nil;
    }
    
    char* cypherBytes = malloc(bytesNeeded);
    size_t bufferLength = bytesNeeded;
    
    if(NULL == cypherBytes)
        NSLog(@"cypherBytes NULL");
    
    ccStatus = CCCrypt(kCCEncrypt,
                       kCCAlgorithmAES,
                       kCCOptionECBMode | kCCOptionPKCS7Padding,
                       [mData bytes],
                       [mData length],
                       nil,
                       [self bytes],
                       [self length],
                       cypherBytes,
                       bufferLength,
                       &bytesNeeded);
    
    if(kCCSuccess != ccStatus){
        NSLog(@"kCCSuccess NO!");
        return nil;
    }
    
    return [NSData dataWithBytes:cypherBytes length:bufferLength];
}

- (NSData *)AESDecryptWithKey:(NSString*)key {
    NSData *mData = [key dataUsingEncoding:NSUTF8StringEncoding];
    CCCryptorStatus ccStatus = kCCSuccess;
    size_t bytesNeeded = 0;
    ccStatus = CCCrypt(kCCDecrypt,
                       kCCAlgorithmAES,
                       kCCOptionECBMode | kCCOptionPKCS7Padding,
                       [mData bytes],
                       [mData length],
                       nil,
                       [self bytes],
                       [self length],
                       NULL,
                       0,
                       &bytesNeeded);
    if(kCCBufferTooSmall != ccStatus){
        NSLog(@"Here it must return BUFFER TOO SMALL !!");
        return nil;
    }
    
    char* cypherBytes = malloc(bytesNeeded);
    size_t bufferLength = bytesNeeded;
    
    if(NULL == cypherBytes)
        NSLog(@"cypherBytes NULL");
    
    ccStatus = CCCrypt(kCCDecrypt,
                       kCCAlgorithmAES,
                       kCCOptionECBMode | kCCOptionPKCS7Padding,
                       [mData bytes],
                       [mData length],
                       nil,
                       [self bytes],
                       [self length],
                       cypherBytes,
                       bufferLength,
                       &bytesNeeded);
    
    if(kCCSuccess != ccStatus){
        NSLog(@"kCCSuccess NO!");
        return nil;
    }
    
    return [NSData dataWithBytes:cypherBytes length:bufferLength];
}

@end
