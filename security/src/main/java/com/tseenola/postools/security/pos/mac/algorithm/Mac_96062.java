package com.tseenola.postools.security.pos.mac.algorithm;


import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.model.SecurityParam;
import com.tseenola.postools.security.utils.Constant;

import java.util.Arrays;

/**
 * Created by lijun on 2017/4/12.
 * 描述：Mac9606算法
 * 算法描述：
 *
 * a) 每8个字节做异或
 * b) 最后异或的结果做一次DES运算
 */
public class Mac_96062 implements IMacCaculator2{
    @Override
    public Pair<Boolean, EncryResult> getMac(SecurityParam param, byte[] pNeedCallMacDatas, ISecurity pSecurity) {
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
            for (i = 0; i < l; i++) {
                for (k = 0; k < 8; k++) {
                    buf[k] ^= inbuf[i * 8 + k];
                }
            }
            if (param.getmSecurityType() == Constant.SOFT) {
                macbuf = pSecurity.encryDataSoft(buf,param.getSoftEncryKeys());
            }else if (param.getmSecurityType() == Constant.HARD){
                macbuf = pSecurity.encryDataHard(buf,param.getHardEncryParam());
            }else {
                return Pair.create(false,new EncryResult("无效的参数【加密方式】"));
            }

            System.arraycopy(macbuf, 0, macOut, 0, 8);
            return Pair.create(true,new EncryResult(macOut));
        }catch (Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }
}
