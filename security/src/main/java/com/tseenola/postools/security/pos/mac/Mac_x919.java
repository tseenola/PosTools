package com.tseenola.postools.security.pos.mac;


import android.util.Pair;

import com.tseenola.postools.security.des.DesImpl;
import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.MacResult;
import com.tseenola.postools.security.utils.Constant;

/**
 * Created by lenovo on 2017/4/12.
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
public class Mac_x919 implements IMacCaculator{
    @Override
    public Pair<Boolean, MacResult> getMac(byte[] pNeedCallMacDatas, byte[] pKeys, ISecurity pSecurity, @Constant.SecurityType int pSecurityType){
        try{
            byte[] keyLeft = new byte[8];
            byte[] keyRight = new byte[8];
            System.arraycopy(pKeys, 0, keyLeft, 0, 8);
            System.arraycopy(pKeys, 8, keyRight, 0, 8);
            Pair<Boolean, MacResult> macX99 = new Mac_x99().getMac(pNeedCallMacDatas, keyLeft, pSecurity, pSecurityType);
            if (!macX99.first) {
                return macX99;
            }

            byte[] result99 = macX99.second.getMac();
            if (pSecurityType == Constant.SOFT) {
                byte[] resultTemp = pSecurity.decryDataSoft(result99,keyRight);
                return Pair.create(true,new MacResult(pSecurity.encryDataSoft(resultTemp,keyLeft)));
            }else {
                byte[] resultTemp = pSecurity.decryDataHard(result99);
                return Pair.create(true,new MacResult(pSecurity.encryDataHard(resultTemp)));
            }
        }catch (Exception pE){
            return Pair.create(false,new MacResult(pE.getMessage()));
        }
    }
}
