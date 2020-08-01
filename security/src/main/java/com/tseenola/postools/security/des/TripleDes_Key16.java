package com.tseenola.postools.security.des;

import com.tseenola.postools.security.intface.ISecurity;

/**
 * 3DES
 * 密钥长度16字节
 */
public class TripleDes_Key16 implements ISecurity {
    @Override
    public byte[] encryDataSoft(byte[] pNeedEncryData, byte[] pKey) throws Exception {
        checkParams16(pNeedEncryData,pKey);
        byte k1[] = new byte [8];
        byte k2[] = new byte [8];
        System.arraycopy(pKey, 0, k1, 0, 8);
        System.arraycopy(pKey, 8, k2, 0, 8);
        byte k3 [] = k1;
        DesImpl lDes = DesImpl.getInstance();
        byte encypt1 [] = lDes.encryDataSoft(pNeedEncryData,k1);
        byte decypt2 [] = lDes.decryDataSoft(encypt1,k2);
        byte encypt3 [] = lDes.encryDataSoft(decypt2,k3);
        return encypt3;
    }

    @Override
    public byte[] decryDataSoft(byte[] pNeedEncryData, byte[] pKey) throws Exception {
        checkParams16(pNeedEncryData,pKey);

        byte k1[] = new byte [8];
        byte k2[] = new byte [8];
        System.arraycopy(pKey, 0, k1, 0, 8);
        System.arraycopy(pKey, 8, k2, 0, 8);
        byte k3 [] = k1;
        DesImpl lDes = DesImpl.getInstance();
        byte decypt3 [] = lDes.decryDataSoft(pNeedEncryData,k3);
        byte encypt2 [] = lDes.encryDataSoft(decypt3,k2);
        byte decypt1 [] = lDes.decryDataSoft(encypt2,k1);

        return decypt1;
    }

    @Override
    public byte[] encryDataHard(byte[] pNeedEncryData) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] decryDataHard(byte[] pNeedEncryData) throws Exception {
        return new byte[0];
    }

    /**
     * 检查传入的3DES（密钥长度16个字节）参数是否正确
     * @param pData
     * @param pKey
     */
    protected void checkParams16(byte[] pData , byte[] pKey){
        if (pKey == null || pData == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if(pKey.length != 16){
            throw new IllegalArgumentException("checkParams16 3Des密钥长度应该是16字节");
        }

        if(pData.length %8 != 0){
            throw new IllegalArgumentException("被加密数据的长度应该为8的倍数");
        }
    }
}
