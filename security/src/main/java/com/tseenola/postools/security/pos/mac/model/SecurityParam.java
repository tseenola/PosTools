package com.tseenola.postools.security.pos.mac.model;

import com.tseenola.postools.security.utils.Constant;

/**
 * Created by lijun on 2020/7/30.
 * 计算Mac/pin/track需要传入的参数
 */
public class SecurityParam {
    protected @Constant.SecurityType int mSecurityType;
    //软加密的key
    protected byte [] softEncryKeys;
    //硬加密的参数（mac索引，密钥类型，算法类型DES,SM4等）
    protected Object [] hardEncryParam;

    /**
     * 软加密用这个构造函数
     * @param softEncryKeys
     */
    public SecurityParam(byte[] softEncryKeys) {
        mSecurityType = Constant.SOFT;
        this.softEncryKeys = softEncryKeys;
    }

    /**
     * 硬加密用这个构造函数
     * @param hardEncryParam
     */
    public SecurityParam(Object[] hardEncryParam) {
        mSecurityType = Constant.HARD;
        this.hardEncryParam = hardEncryParam;
    }

    public int getmSecurityType() {
        return mSecurityType;
    }

    public byte[] getSoftEncryKeys() {
        return softEncryKeys;
    }

    public Object[] getHardEncryParam() {
        return hardEncryParam;
    }
}
