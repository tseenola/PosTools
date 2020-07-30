package com.tseenola.postools.security.intface;

/**
 * Created by lenovo on 2020/7/30.
 * 描述：这个接口主要负责加密算法的调用
 * 例如 DES,3DES,SM4 and so on
 */
public interface ISecurity {
    void encryData(byte [] pNeedEncryData);
}
