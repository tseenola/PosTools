package com.tseenola.postools.security.utils;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constant {
    public static final int SOFT = 0;
    public static final int HARD = 1;
    @IntDef({SOFT,HARD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SecurityType{}
}
