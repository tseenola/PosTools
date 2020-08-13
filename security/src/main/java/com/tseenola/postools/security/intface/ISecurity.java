package com.tseenola.postools.security.intface;

import com.tseenola.postools.security.utils.Constant;

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
    byte []  encryDataSoft(byte [] pNeedEncryData,byte [] pKey)throws Exception;
    /**
     * 软解密
     * 自己实现解密算法
     * @param pNeedEncryData :被加密数据
     * @param pKey            :密钥
     */
    byte []  decryDataSoft(byte [] pNeedEncryData,byte [] pKey)throws Exception;

    /**
     * 硬加密
     * 调用底层安全芯片进行加密，可能是POS或者是密码键盘
     * @param pNeedEncryData
     * @throws Exception
     */
    byte []  encryDataHard(byte [] pNeedEncryData, Object ... obj)throws Exception;

    /**
     * 硬解密
     * 调用底层安全芯片进行解密，可能是POS或者是密码键盘
     * @param pNeedDecryData 被解密数据
     * @throws Exception
     */
    byte []  decryDataHard(byte [] pNeedDecryData, Object ... obj)throws Exception;
}
