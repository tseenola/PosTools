package com.tseenola.postools.security.pos.mac;

import com.tseenola.postools.security.intface.ISecurity;

public interface IMacCaculator {
    /**
     * 计算MAC通过(硬加密）的方式
     * @param pNeedCallMacDatas 待计算mac的数据
     * @param pSecurity  加密的类
     * @return mac计算结果
     * @throws Exception
     */
    byte [] getMacByHard(byte[] pNeedCallMacDatas, ISecurity pSecurity) throws Exception;

    /**
     * 计算MAC通过（软加密）的方式
     * @param pNeedCallMacDatas 待计算mac的数据
     * @param pKeys 密钥
     * @param pSecurity 加密的类
     * @return mac计算结果
     * @throws Exception
     */
    byte [] getMacBySoft(byte[] pNeedCallMacDatas , byte pKeys[], ISecurity pSecurity) throws Exception;
}
