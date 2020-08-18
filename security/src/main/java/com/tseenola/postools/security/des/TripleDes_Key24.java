package com.tseenola.postools.security.des;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;

/**
 * 3DES
 * 密钥长度 24 字节
 */
public class TripleDes_Key24 implements ISecurity {


    @Override
    public Pair<Boolean, EncryResult> encryDataSoft(byte[] pNeedEncryData, byte[] pKey) throws Exception {
        try{
            checkParams24(pNeedEncryData ,pKey);

            byte k1[] = new byte [8];
            byte k2[] = new byte [8];
            byte k3[] = new byte [8];
            System.arraycopy(pKey, 0, k1, 0, 8);
            System.arraycopy(pKey, 8, k2, 0, 8);
            System.arraycopy(pKey, 16,k3, 0, 8);

            DesImpl lDes = new DesImpl();
            //1、使用 k1 加密
            Pair<Boolean, EncryResult> lEncryResultPair = lDes.encryDataSoft(pNeedEncryData,k1);
            if (!lEncryResultPair.first) {
                return lEncryResultPair;
            }
            byte encypt1 [] = lEncryResultPair.second.getEncryDecryResult();
            //2、使用 k2 对k1加密结果解密
            lEncryResultPair = lDes.decryDataSoft(encypt1,k2);
            if (!lEncryResultPair.first) {
                return lEncryResultPair;
            }
            byte decypt2 [] = lEncryResultPair.second.getEncryDecryResult();
            //3、使用 k3 对k2 解密结果进行加密
            lEncryResultPair = lDes.encryDataSoft(decypt2,k3);
            return lEncryResultPair;
        }catch(Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }

    @Override
    public Pair<Boolean, EncryResult> decryDataSoft(byte[] pNeedEncryData, byte[] pKey) throws Exception {
        try{
            checkParams24(pNeedEncryData,pKey);

            byte k1[] = new byte [8];
            byte k2[] = new byte [8];
            byte k3[] = new byte [8];
            System.arraycopy(pKey, 0, k1, 0, 8);
            System.arraycopy(pKey, 8, k2, 0, 8);
            System.arraycopy(pKey, 16,k3, 0, 8);

            DesImpl lDes = new DesImpl();
            //1、使用k3对数据进行解密
            Pair<Boolean, EncryResult> lEncryResultPair = lDes.decryDataSoft(pNeedEncryData,k3);
            if (!lEncryResultPair.first) {
                return lEncryResultPair;
            }
            byte decypt3 [] = lEncryResultPair.second.getEncryDecryResult();
            //2、使用k2对 k3 解密数据进行加密
            lEncryResultPair = lDes.encryDataSoft(decypt3,k2);
            if (!lEncryResultPair.first) {
                return lEncryResultPair;
            }
            byte encypt2 [] = lEncryResultPair.second.getEncryDecryResult();
            //3、使用k1 对 k2加密数据进行解密
            lEncryResultPair = lDes.decryDataSoft(encypt2,k1);
            return lEncryResultPair;
        }catch(Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }

    @Override
    public Pair<Boolean, EncryResult> encryDataHard(byte[] pNeedEncryData, Object... obj) throws Exception {
        return Pair.create(false,new EncryResult("硬件加/解密需要你自己继承此类并覆写此函数"));
    }

    @Override
    public Pair<Boolean, EncryResult> decryDataHard(byte[] pNeedDecryData, Object... obj) throws Exception {
        return Pair.create(false,new EncryResult("硬件加/解密需要你自己继承此类并覆写此函数"));
    }


    /**
     * 检查传入的3DES（密钥长度24个字节）参数是否正确
     * @param pData
     * @param pKey
     */
    protected void checkParams24(byte[] pData , byte[] pKey){
        if (pKey == null || pData == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if(pKey.length != 24){
            throw new IllegalArgumentException("checkParams24 3Des密钥长度应该是24字节");
        }

        if(pData.length %8 != 0){
            throw new IllegalArgumentException("被加密数据的长度应该为8的倍数");
        }
    }
}
