package com.tseenola.postools.security.utils;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * Created by lijun on 2020/7/30.
 */
public class Constant {
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

    //=========================================分割线======================================================
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
    //=========================================分割线======================================================

    /**
     * MAC加密类型
     */
    //
    public static final int MAC_TYPE_UNION_ECB = 0;
    //
    public static final int MAC_TYPE_9606 = 1;
    //
    public static final int MAC_TYPE_X99 = 2;
    //
    public static final int MAC_TYPE_X919 = 3;
    @IntDef({MAC_TYPE_UNION_ECB, MAC_TYPE_9606,MAC_TYPE_X99,MAC_TYPE_X919})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MacType {
    }

    //=========================================分割线======================================================
    /**
     * 磁道类型
     */
    //2磁道
    public static final int TRACK2 = 2;
    //3磁道
    public static final int TRACK3 = 3;
    @IntDef({TRACK2,TRACK3})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TrackType{}

}
