package com.tseenola.postools.security.pos.mac.algorithm;


import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.MacResult;
import com.tseenola.postools.security.pos.mac.model.MacParam;
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
public class Mac_x9192 implements IMacCaculator2{
    @Override
    public Pair<Boolean, MacResult> getMac(MacParam param, byte[] pNeedCallMacDatas, ISecurity pSecurity) {
        try{
            Pair<Boolean, MacResult> macX99 = null;
            byte[] keyLeft = new byte[8];
            byte[] keyRight = new byte[8];
            if (param.getmSecurityType() == Constant.SOFT) {
                System.arraycopy(param.getSoftEncryKeys(), 0, keyLeft, 0, 8);
                System.arraycopy(param.getSoftEncryKeys(), 8, keyRight, 0, 8);
            }else if (param.getmSecurityType() == Constant.HARD) {

            }else {
                return Pair.create(false,new MacResult("无效的参数【加密方式】"));
            }
            macX99 = new Mac_x992().getMac(param, pNeedCallMacDatas, pSecurity);
            if (!macX99.first) {
                return macX99;
            }

            byte[] result99 = macX99.second.getMac();
            if (param.getmSecurityType() == Constant.SOFT) {
                byte[] resultTemp = pSecurity.decryDataSoft(result99,keyRight);
                return Pair.create(true,new MacResult(pSecurity.encryDataSoft(resultTemp,keyLeft)));
            }else if (param.getmSecurityType() == Constant.HARD) {
                byte[] resultTemp = pSecurity.decryDataHard(result99);
                return Pair.create(true,new MacResult(pSecurity.encryDataHard(resultTemp)));
            }else {
                return Pair.create(false,new MacResult("无效的参数【加密方式】"));
            }
        }catch (Exception pE){
            return Pair.create(false,new MacResult(pE.getMessage()));
        }
    }
}
