package com.tseenola.postools.security.pos.mac.algorithm;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.model.SecurityParam;

public interface IMacCaculator2 {
    /**
     *
     * @param param
     * @param pNeedCallMacDatas 待计算mac的数据
     * @param pSecurity 加密的类
     * @return
     */
    Pair<Boolean, EncryResult> getMac(SecurityParam param, byte[] pNeedCallMacDatas, ISecurity pSecurity);
}