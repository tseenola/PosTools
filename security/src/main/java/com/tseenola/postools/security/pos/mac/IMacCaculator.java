package com.tseenola.postools.security.pos.mac;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.utils.Constant;

public interface IMacCaculator {
    /**
     * 计算Mac
     * @param pNeedCallMacDatas 待计算mac的数据
     * @param pKeys 密钥：如果是硬加密可以不穿
     * @param pSecurity 加密的类
     * @param pSecurityType
     * @return
     * @throws Exception
     */
    byte [] getMac(byte [] pNeedCallMacDatas, byte pKeys[], ISecurity pSecurity,@Constant.SecurityType int pSecurityType)throws Exception;
}