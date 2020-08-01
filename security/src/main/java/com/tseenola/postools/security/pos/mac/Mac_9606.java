package com.tseenola.postools.security.pos.mac;


import com.tseenola.postools.security.des.DesImpl;
import com.tseenola.postools.security.intface.ISecurity;

import java.util.Arrays;
/**
 * Created by lenovo on 2017/4/12.
 * 描述：Mac9606算法
 * 算法描述：
 *
 * a) 每8个字节做异或
 * b) 最后异或的结果做一次DES运算
 */
public class Mac_9606 implements IMacCaculator{
    @Override
    public byte[] getMacByHard(byte[] pNeedCallMacDatas, ISecurity pSecurity) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getMacBySoft(byte[] pNeedCallMacDatas, byte[] pKeys, ISecurity pSecurity) throws Exception {
        byte[] buf = new byte[17];
        byte[] tmpbuf = new byte[17];
        byte [] macOut = new byte[8];
        int i = 0;
        int l = 0;
        int k = 0 ;
        int iRet = 0;

        byte[] inbuf = new byte[pNeedCallMacDatas.length + 8];
        byte[] macbuf = new byte[9];

        Arrays.fill(buf, (byte) 0x00);

        System.arraycopy(pNeedCallMacDatas, 0, inbuf, 0, pNeedCallMacDatas.length);

        if (pNeedCallMacDatas.length % 8 != 0){
            l = pNeedCallMacDatas.length / 8 + 1;
        } else{
            l = pNeedCallMacDatas.length / 8;
        }

        buf = new byte[8];
        for (i = 0; i < l; i++)
            for (k = 0; k < 8; k++)
                buf[k] ^= inbuf[i * 8 + k];

        macbuf = DesImpl.getInstance().encryDataSoft(buf,pKeys);
        //Macbuf = DesUtils.encrypt(pKeys, buf, DesImpl.getInstance());

        System.arraycopy(macbuf, 0, macOut, 0, 8);

        return macOut;
    }
}
