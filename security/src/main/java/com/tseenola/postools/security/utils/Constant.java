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
}
