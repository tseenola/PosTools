package com.tseenola.postools.security.intface;

import android.util.Pair;

import com.tseenola.postools.security.model.EncryResult;

/**
 * Created by lenovo on 2020/7/30.
 * 描述：这个接口主要负责加密算法的调用
 * 例如 DES,3DES,SM4 and so on
 */
public interface ISecurity {
    /**
     * 软加密
     * 自己实现加密
     * @param pNeedEncryData :被加密数据
     * @param pKey            :密钥
     */
    Pair<Boolean, EncryResult> encryDataSoft(byte [] pNeedEncryData, byte [] pKey)throws Exception;
    /**
     * 软解密
     * 自己实现解密算法
     * @param pNeedEncryData :被加密数据
     * @param pKey            :密钥
     */
    Pair<Boolean, EncryResult> decryDataSoft(byte [] pNeedEncryData,byte [] pKey)throws Exception;

    /**
     * 硬加密
     * 调用底层安全芯片进行加密，可能是POS或者是密码键盘
     * @param pNeedEncryData 被加密的数据
     * @param obj             硬件加密需要传入的参数
     * @return
     * @throws Exception
     */
    Pair<Boolean, EncryResult> encryDataHard(byte [] pNeedEncryData, Object ... obj)throws Exception;

    /**
     * 硬解密
     * 调用底层安全芯片进行解密，可能是POS或者是密码键盘
     * @param pNeedDecryData   被解密数据
     * @param obj               硬件解密需要传入的参数
     * @return
     * @throws Exception
     */
    Pair<Boolean, EncryResult> decryDataHard(byte [] pNeedDecryData, Object ... obj)throws Exception;
}
