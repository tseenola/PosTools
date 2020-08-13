package com.tseenola.postools.security.pos.mac.algorithm;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.MacResult;
import com.tseenola.postools.security.pos.mac.model.MacParam;
import com.tseenola.postools.security.utils.Constant;

/**
 * Created by lijun on 2017/4/12.
 * 描述：X9.9MAC算法
 * 算法描述：
 * (1) ANSI X9.9MAC算法只使用单倍长密钥。
 * (2) MAC数据先按8字节分组，表示为D0～Dn，如果Dn不足8字节时，尾部以字节00补齐。
 * (3) 用MAC密钥加密D0，加密结果与D1异或作为下一次的输入。
 * (4) 将上一步的加密结果与下一分组异或，然后再用MAC密钥加密。
 * (5) 直至所有分组结束，取最后结果的左半部作为MAC。
 *
 */
public class Mac_x992 implements IMacCaculator2{
    @Override
    public Pair<Boolean, MacResult> getMac(MacParam param, byte[] pNeedCallMacDatas, ISecurity pSecurity) {
        try{
            final int dataLength = pNeedCallMacDatas.length;
            final int lastLength = dataLength % 8;
            final int lastBlockLength = lastLength == 0 ? 8 : lastLength;
            final int blockCount = dataLength / 8 + (lastLength > 0 ? 1 : 0);

            // 拆分数据（8字节块/Block）
            byte[][] dataBlock = new byte[blockCount][8];
            for (int i = 0; i < blockCount; i++) {
                int copyLength = i == blockCount - 1 ? lastBlockLength : 8;
                System.arraycopy(pNeedCallMacDatas, i * 8, dataBlock[i], 0, copyLength);
            }

            byte[] desXor = new byte[8];
            for (int i = 0; i < blockCount; i++) {
                byte[] tXor = xOr(desXor, dataBlock[i]);

                if (param.getmSecurityType() == Constant.SOFT) {
                    desXor = pSecurity.encryDataSoft(tXor,param.getSoftEncryKeys());
                }else if (param.getmSecurityType() == Constant.HARD){
                    desXor = pSecurity.encryDataHard(tXor,param.getHardEncryParam());
                }else {
                    return Pair.create(false,new MacResult("无效的参数【加密方式】"));
                }

            }
            return Pair.create(true,new MacResult(desXor));
        }catch (Exception pE){
            return Pair.create(false,new MacResult(pE.getMessage()));
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
        for (int i = 0; i < tXor.length; i++)
            tXor[i] = (byte) (b1[i] ^ b2[i]); // 异或(Xor)
        return tXor;
    }
}