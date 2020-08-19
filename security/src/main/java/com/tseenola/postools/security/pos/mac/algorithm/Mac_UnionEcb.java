package com.tseenola.postools.security.pos.mac.algorithm;


import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.utils.Constant;
import com.tseenola.postools.security.utils.ConvertUtils;

import java.util.Arrays;

/**
 * Created by lijun on 2017/4/12.
 * 描述：MacECB算法，银联商务标准算法
 * a) 将欲发送给POS中心的消息中，从消息类型（MTI）到63域之间的部分构成MAC ELEMEMENT BLOCK （MAB）。
 * b) 对MAB，按每8个字节做异或（不管信息中的字符格式），如果最后不满8个字节，则添加“0X00”。
 * c) 将异或运算后的最后8个字节（RESULT BLOCK）转换成16 个HEXDECIMAL：
 * d) 取前8 个字节用MAK加密：
 * e) 将加密后的结果与后8 个字节异或：
 * f) 用异或的结果TEMP BLOCK 再进行一次单倍长密钥算法运算。
 * g) 将运算后的结果（ENC BLOCK2）转换成16 个HEXDECIMAL：
 * h) 取前8个字节作为MAC值。
 */
public class Mac_UnionEcb<T> implements IMacCaculator<T> {
    @Override
    public Pair<Boolean, EncryResult> getMac(int pSecurityBy, byte[] pEncDecKey, T pSecurityHardParam, byte[] pNeedCallMacDatas, ISecurity pSecurity) {
        try{
            byte[] buf = new byte[17];
            byte[] tmpbuf = new byte[17];
            byte [] macOut = new byte[8];
            int i = 0;
            int l = 0;
            int k = 0 ;

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

            ConvertUtils.BcdToAsc(tmpbuf, buf, 16);
            tmpbuf[16] = 0;

            System.arraycopy(tmpbuf, 0, buf, 0, 8);
            Pair<Boolean, EncryResult> lEncryResultPair = null;
            if (pSecurityBy == Constant.SOFT) {
                lEncryResultPair = pSecurity.encryDataSoft(buf,pEncDecKey);
            }else if (pSecurityBy == Constant.HARD){
                lEncryResultPair = pSecurity.encryDataHard(buf,pSecurityHardParam);
            }else {
                return Pair.create(false,new EncryResult("无效的参数【加密方式】"));
            }
            if (!lEncryResultPair.first) {
                return lEncryResultPair;
            }
            macbuf = lEncryResultPair.second.getEncryDecryResult();

            Arrays.fill(buf, (byte) 0x00);
            System.arraycopy(macbuf, 0, buf, 0, 8);

            for (k = 0; k < 8; k++) {
                buf[k] ^= tmpbuf[8 + k];
            }

            Arrays.fill(macbuf, (byte) 0x00);

            if (pSecurityBy == Constant.SOFT){
                lEncryResultPair = pSecurity.encryDataSoft(buf,pEncDecKey);
            }else if (pSecurityBy == Constant.HARD){
                lEncryResultPair = pSecurity.encryDataHard(buf,pSecurityHardParam);
            } else {
                return Pair.create(false,new EncryResult("无效的参数【加密方式】"));
            }
            if (!lEncryResultPair.first) {
                return lEncryResultPair;
            }
            macbuf = lEncryResultPair.second.getEncryDecryResult();

            Arrays.fill(buf, (byte) 0x00);
            System.arraycopy(macbuf, 0, buf, 0, 8);

            Arrays.fill(tmpbuf, (byte) 0x00);
            ConvertUtils.BcdToAsc(tmpbuf, buf, 16);
            System.arraycopy(tmpbuf, 0, macOut, 0, 8);
            return Pair.create(true,new EncryResult(macOut));
        }catch (Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }
}
