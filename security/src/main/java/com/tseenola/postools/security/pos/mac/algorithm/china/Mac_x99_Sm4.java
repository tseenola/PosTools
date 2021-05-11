package com.tseenola.postools.security.pos.mac.algorithm.china;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.algorithm.IMacCaculator;
import com.tseenola.postools.security.utils.Constant;

/**
 * Created by lijun on 2017/4/12.
 * 描述：X9.9MAC(sm4)算法
 * 算法描述：
 * (1) ANSI X9.9MAC算法只使用单倍长密钥。
 * (2) MAC数据先按16字节分组，表示为D0～Dn，如果Dn不足16字节时，尾部以字节00补齐。
 * (3) 用MAC密钥加密D0，加密结果与D1异或作为下一次的输入。
 * (4) 将上一步的加密结果与下一分组异或，然后再用MAC密钥加密。
 * (5) 直至所有分组结束，取最后结果的左半部作为MAC。
 *
 */
public class Mac_x99_Sm4 <T> implements IMacCaculator<T> {
    private String TAG = this.getClass().getSimpleName();
    @Override
    public Pair<Boolean, EncryResult> getMac(int pSecurityBy, byte[] pEncDecKey,
                                             T pSecurityHardParam, byte[] pNeedCallMacDatas,
                                             ISecurity pSecurity) {
        try{
            final int dataLength = pNeedCallMacDatas.length;
            final int lastLength = dataLength % 16;
            final int lastBlockLength = lastLength == 0 ? 16 : lastLength;
            final int blockCount = dataLength / 16 + (lastLength > 0 ? 1 : 0);

            // 拆分数据（16字节块/Block）
            byte[][] dataBlock = new byte[blockCount][16];
            for (int i = 0; i < blockCount; i++) {
                int copyLength = i == blockCount - 1 ? lastBlockLength : 16;
                System.arraycopy(pNeedCallMacDatas, i * 16, dataBlock[i], 0, copyLength);
            }

            byte[] desXor = new byte[16];
            Pair<Boolean, EncryResult> lEncryResultPair = null;
            for (int i = 0; i < blockCount; i++) {
                byte[] tXor = xOr(desXor, dataBlock[i]);
                if (pSecurityBy == Constant.SOFT) {
                    lEncryResultPair = pSecurity.encryDataSoft(tXor,pEncDecKey);
                }else if (pSecurityBy == Constant.HARD){
                    lEncryResultPair = pSecurity.encryDataHard(tXor,pSecurityHardParam);
                }else {
                    return Pair.create(false,new EncryResult("无效的参数【加密方式】"));
                }
                if (!lEncryResultPair.first) {
                    return lEncryResultPair;
                }
                desXor = lEncryResultPair.second.getEncryDecryResult();
            }
            return Pair.create(true,new EncryResult(desXor));
        }catch (Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }

    /**
     * 将b1和b2做异或，然后返回
     * @param b1
     * @param b2
     * @return 异或结果
     */
    private byte[] xOr(byte[] b1, byte[] b2) {
        byte[] tXor = new byte[Math.min(b1.length, b2.length)];
        for (int i = 0; i < tXor.length; i++) {
            tXor[i] = (byte) (b1[i] ^ b2[i]); // 异或(Xor)
        }
        return tXor;
    }
}