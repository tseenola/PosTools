package com.tseenola.postools.security.des;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.proxy.SeManagerProxy;

/**
 * 3DES
 * 密钥长度 24 字节
 */
public class TripleDes_Key24 implements ISecurity {
    @Override
    public byte[] encryDataSoft(byte[] pNeedEncryData, byte[] pKey) throws Exception {
        checkParams24(pNeedEncryData ,pKey);

        byte k1[] = new byte [8];
        byte k2[] = new byte [8];
        byte k3[] = new byte [8];
        System.arraycopy(pKey, 0, k1, 0, 8);
        System.arraycopy(pKey, 8, k2, 0, 8);
        System.arraycopy(pKey, 16,k3, 0, 8);

        DesImpl lDes = new DesImpl();
        byte encypt1 [] = lDes.encryDataSoft(pNeedEncryData,k1);
        byte decypt2 [] = lDes.decryDataSoft(encypt1,k2);
        byte encypt3 [] = lDes.encryDataSoft(decypt2,k3);

        return encypt3;
    }

    @Override
    public byte[] decryDataSoft(byte[] pNeedEncryData, byte[] pKey) throws Exception {
        checkParams24(pNeedEncryData,pKey);

        byte k1[] = new byte [8];
        byte k2[] = new byte [8];
        byte k3[] = new byte [8];
        System.arraycopy(pKey, 0, k1, 0, 8);
        System.arraycopy(pKey, 8, k2, 0, 8);
        System.arraycopy(pKey, 16,k3, 0, 8);

        DesImpl lDes = new DesImpl();
        byte decypt3 [] = lDes.decryDataSoft(pNeedEncryData,k3);
        byte encypt2 [] = lDes.encryDataSoft(decypt3,k2);
        byte decypt1 [] = lDes.decryDataSoft(encypt2,k1);

        return decypt1;
    }

    @Override
    public byte[] encryDataHard(byte[] pNeedEncryData, Object... obj) throws Exception {
        SeManagerProxy.getInstance().doEncry(
                (int)obj[0],
                (int)obj[1],
                (int)obj[2],
                (byte[])obj[3],
                (int)obj[4],
                (int)obj[5],
                pNeedEncryData,
                pNeedEncryData.length,
                (byte[])obj[6],
                (byte[])obj[7]);
        return new byte[0];
    }

    @Override
    public byte[] decryDataHard(byte[] pNeedDecryData, Object... obj) throws Exception {
        SeManagerProxy.getInstance().doEncry(
                (int)obj[0],
                (int)obj[1],
                (int)obj[2],
                (byte[])obj[3],
                (int)obj[4],
                (int)obj[5],
                pNeedDecryData,
                pNeedDecryData.length,
                (byte[])obj[6],
                (byte[])obj[7]);
        return new byte[0];
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
