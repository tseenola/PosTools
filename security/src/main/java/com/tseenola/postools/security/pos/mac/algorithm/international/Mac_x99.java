package com.tseenola.postools.security.pos.mac.algorithm.international;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.algorithm.IMacCaculator;
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
public class Mac_x99<T> implements IMacCaculator<T> {

    private String TAG = this.getClass().getSimpleName();
    /**
     *
     * @param pSecurityBy      加密方式：1、软加密：传入密钥和数据，直接加密，
     *                         或者2、硬加密：传入参数给底层芯片，用芯片加密 {@link Constant.SecurityBy}
     * @param pEncDecKey         软加密传入的Key
     * @param pSecurityHardParam  硬加密参数，如果是软加密(pSecurityBy值为 SOFT )时 这个参数传 null 硬加密需要传入的参数（主密钥，密钥索引，密钥类型，加密方式des,sm4等等.....）
     * @param pNeedCallMacDatas  待计算mac的数据
     * @param pSecurity         加密算法，SM4,3DES 等等
     *                          {@link com.tseenola.postools.security.sm.Sm4}
     *                          {@link com.tseenola.postools.security.des.3DES}
     * @return
     */
    @Override
    public Pair<Boolean, EncryResult> getMac(@Constant.SecurityBy int pSecurityBy, byte[] pEncDecKey, T pSecurityHardParam, byte[] pNeedCallMacDatas, ISecurity pSecurity) {
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
