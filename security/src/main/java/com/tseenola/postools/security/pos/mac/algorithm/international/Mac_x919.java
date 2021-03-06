package com.tseenola.postools.security.pos.mac.algorithm.international;


import android.util.Pair;

import com.tseenola.postools.security.des.DesImpl;
import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.algorithm.IMacCaculator;
import com.tseenola.postools.security.utils.Constant;

/**
 * Created by lijun on 2017/4/12.
 * 描述：
 *              算法描述
 * ANSI X9.19 MAC算法
 *
 *    (1)      ANSI X9.19MAC算法只使用双倍长密钥。
 *    (2)      MAC数据先按8字节分组，表示为D0～Dn，如果Dn不足8字节时，尾部以字节00补齐。
 *    (3)      用MAC密钥左半部加密D0，加密结果与D1异或作为下一次的输入。
 *    (4)      将上一步的加密结果与下一分组异或，然后用MAC密钥左半部加密。
 *    (5)      直至所有分组结束。
 *    (6)      用MAC密钥右半部解密(5)的结果。
 *    (7)      用MAC密钥左半部加密(6)的结果。
 *    (8)      取(7)的结果的左半部作为MAC。
 */
public class Mac_x919<T> implements IMacCaculator<T> {

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
            ISecurity finalDes = new DesImpl();
            Pair<Boolean, EncryResult> macX99 = null;
            byte[] keyLeft = new byte[8];
            byte[] keyRight = new byte[8];
            if (pSecurityBy == Constant.SOFT) {
                System.arraycopy(pEncDecKey, 0, keyLeft, 0, 8);
                System.arraycopy(pEncDecKey, 8, keyRight, 0, 8);
            }else if (pSecurityBy == Constant.HARD) {
                return Pair.create(false,new EncryResult("无效的参数【加密方式】"));
            }else {
                return Pair.create(false,new EncryResult("x99不支持硬加密，请先使用 Mac_x99算法和左8字节对数据进行加密，然后使用右8字节密钥对上一步加密结果做DES加密"));
            }
            macX99 = new Mac_x99().getMac(pSecurityBy,keyLeft, pSecurityHardParam,pNeedCallMacDatas, finalDes);
            if (!macX99.first) {
                return macX99;
            }

            byte[] result99 = macX99.second.getEncryDecryResult();
            Pair<Boolean, EncryResult> lEncryResultPair = null;
            if (pSecurityBy == Constant.SOFT) {
                lEncryResultPair = finalDes.decryDataSoft(result99,keyRight);
                if (!lEncryResultPair.first) {
                    return lEncryResultPair;
                }
                byte[] resultTemp = lEncryResultPair.second.getEncryDecryResult();
                return pSecurity.encryDataSoft(resultTemp,keyLeft);
            }else if (pSecurityBy == Constant.HARD) {
                lEncryResultPair = pSecurity.decryDataHard(result99,pSecurityHardParam);
                if (!lEncryResultPair.first) {
                    return lEncryResultPair;
                }
                byte[] resultTemp = lEncryResultPair.second.getEncryDecryResult();
                return pSecurity.encryDataHard(resultTemp,pSecurityHardParam);
            }else {
                return Pair.create(false,new EncryResult("无效的参数【加密方式】"));
            }
        }catch (Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }
}
