package com.tseenola.postools.security.utils;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constant {
    /**
     * 加密方式
     */
    //软加密
    public static final int SOFT = 0;
    //硬加密
    public static final int HARD = 1;

    @IntDef({SOFT,HARD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SecurityType{}

    /**
     * 密钥类型-硬件序列号密文数据
     */
    public static final int KEY_TYPE_TUSN = 0;
    //密钥类型-磁道密钥
    public static final int KEY_TYPE_TDK = 1;
    //密钥类型-PIN密钥
    public static final int KEY_TYPE_PIN = 2;
    //密钥类型-MAC密钥
    public static final int KEY_TYPE_MAC = 3;
    //密钥类型-主密钥
    public static final int KEY_TYPE_MASTER = 4;

    @IntDef({KEY_TYPE_TUSN, KEY_TYPE_TDK, KEY_TYPE_PIN, KEY_TYPE_MAC, KEY_TYPE_MASTER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface KeyType {
    }

    /**
     * 加密或者解密
     */
    // 加密
    public static final int DO_TYPE_ENCRYPT = 0;
    // 解密
    public static final int DO_TYPE_DECRYPT = 1;

    @IntDef({DO_TYPE_DECRYPT, DO_TYPE_ENCRYPT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DoType {
    }

    /**
     * 算法类型
     */
    // DES_ECB
    public final static byte ALG_DES_ECB = 0x01;
    // DES_CBC
    public final static byte ALG_DES_CBC = 0x02;
    // 国密 ECB
    public final static byte ALG_SM4_ECB = 0x03;
    // 国密 CBC
    public final static byte ALG_SM4_CBC = 0x04;

    @IntDef({ALG_DES_CBC, ALG_DES_ECB, ALG_SM4_CBC, ALG_SM4_ECB})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AlgType {
    }
}
