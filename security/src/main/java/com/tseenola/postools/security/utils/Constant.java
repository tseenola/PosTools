package com.tseenola.postools.security.utils;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constant {
    public static final String SOFT = "SOFT";
    public static final String HARD = "HARD";
    @StringDef({SOFT,HARD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SecurityType{}
}
