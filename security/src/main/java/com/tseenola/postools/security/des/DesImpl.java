package com.tseenola.postools.security.des;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
/**
 * Created by lijun on 2020/8/17.
 * 描述：
 */
public class DesImpl<T> implements ISecurity<T> {
    /**
     * des软加密
     * @param pNeedEncryData :被加密数据
     * @param pKey            :密钥
     * @return
     * @throws Exception
     */
    @Override
    public Pair<Boolean, EncryResult> encryDataSoft(byte[] pNeedEncryData, byte[] pKey) throws Exception {
        try{
            checkParams(pNeedEncryData ,pKey);
            SecretKeySpec secretKey = new SecretKeySpec(pKey, "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte result [] = cipher.doFinal(pNeedEncryData);
            return Pair.create(true,new EncryResult(result));
        }catch(Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }

    @Override
    public Pair<Boolean, EncryResult> decryDataSoft(byte[] pNeedEncryData, byte[] pKey) throws Exception {
        try{
            checkParams(pNeedEncryData ,pKey);
            SecretKeySpec secretKey = new SecretKeySpec(pKey, "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte result [] = cipher.doFinal(pNeedEncryData);
            return Pair.create(true,new EncryResult(result));
        }catch(Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }

    }

    @Override
    public Pair<Boolean, EncryResult> encryDataHard(byte[] pNeedEncryData, T hardEncryParam) throws Exception {
        return Pair.create(false,new EncryResult("硬件加/解密需要你自己继承此类并覆写此函数"));
    }

    @Override
    public Pair<Boolean, EncryResult> decryDataHard(byte[] pNeedDecryData, T hardDecryParam) throws Exception {
        return Pair.create(false,new EncryResult("硬件加/解密需要你自己继承此类并覆写此函数"));
    }


    /**
     * 检查传入的DES参数是否正确
     * DES加密：密钥长度为8字节，被加密数据为8的倍数
     * @param pData
     * @param pKey
     */
    protected void checkParams(byte[] pData ,byte[] pKey){
        if (pData==null) {
            throw new IllegalArgumentException("DES运算加解密数据不能为空");
        }
        if (pKey == null) {
            throw new IllegalArgumentException("DES运算密钥不能为空");
        }
        if (pData.length%8 != 0){
            throw new IllegalArgumentException("被加密数据长度必须为8的倍数");
        }
        if (pKey.length!=8){
            throw new IllegalArgumentException("密钥长度应该为8字节");
        }
    }
}
