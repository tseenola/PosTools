package com.tseenola.postools.security.pos.mac;


import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.MacResult;
import com.tseenola.postools.security.utils.Constant;

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
    public Pair<Boolean, MacResult> getMac(byte[] pNeedCallMacDatas, byte[] pKeys, ISecurity pSecurity, @Constant.SecurityType int pSecurityType){
        try{
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
            if (pSecurityType == Constant.SOFT) {
                macbuf = pSecurity.encryDataSoft(buf,pKeys);
            }else {
                macbuf = pSecurity.encryDataHard(buf);
            }
            System.arraycopy(macbuf, 0, macOut, 0, 8);
            return Pair.create(true,new MacResult(macOut));
        }catch (Exception pE){
            return Pair.create(false,new MacResult(pE.getMessage()));
        }
    }
}
