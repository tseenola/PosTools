package com.tseenola.postools.security.des;

import com.tseenola.postools.security.intface.ISecurity;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DesImpl implements ISecurity {
    private DesImpl(){}
    private static DesImpl ins;
    /**
     * 软加密用这个做初始化
     * @param pKey
     * @return
     */
    public static DesImpl getInsSoft(byte[] pKey){
        if (ins==null) {
            ins = new DesImpl();
        }
        return ins;
    }

    /**
     * 硬加密用这个初始化
     * @return
     */

    public static DesImpl getInsHard(int pMacKeyIndex){
        if (ins==null) {
            ins = new DesImpl();
        }
        return ins;
    }
    /**
     * des软加密
     * @param pNeedEncryData :被加密数据
     * @param pKey            :密钥
     * @return
     * @throws Exception
     */
    @Override
    public byte[] encryDataSoft(byte[] pNeedEncryData, byte[] pKey) throws Exception {
        checkParams(pNeedEncryData ,pKey);
        SecretKeySpec secretKey = new SecretKeySpec(pKey, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(pNeedEncryData);
    }

    @Override
    public byte[] decryDataSoft(byte[] pNeedEncryData, byte[] pKey) throws Exception {
        checkParams(pNeedEncryData ,pKey);
        SecretKeySpec secretKey = new SecretKeySpec(pKey, "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(pNeedEncryData);
    }

    @Override
    public byte[] encryDataHard(byte[] pNeedEncryData, Object... obj) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] decryDataHard(byte[] pNeedDecryData, Object... obj) throws Exception {
        return new byte[0];
    }


    /**
     * 检查传入的DES参数是否正确
     * DES加密：密钥长度为8字节，被加密数据为8的倍数
     * @param pData
     * @param pKey
     */
    protected void checkParams(byte[] pData ,byte[] pKey){
        if (pKey == null || pData == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (pKey.length!=8){
            throw new IllegalArgumentException("密钥长度必须为8的倍数");
        }

        if (pData.length%8 != 0){
            throw new IllegalArgumentException("被加密数据长度必须为8的倍数");
        }
    }
}
