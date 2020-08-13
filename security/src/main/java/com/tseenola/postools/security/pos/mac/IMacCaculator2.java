package com.tseenola.postools.security.pos.mac;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.MacResult;
import com.tseenola.postools.security.pos.mac.model.MacParam;
import com.tseenola.postools.security.utils.Constant;

public interface IMacCaculator2 {
    /**
     *
     * @param param
     * @param pNeedCallMacDatas 待计算mac的数据
     * @param pSecurity 加密的类
     * @return
     */
    Pair<Boolean, MacResult> getMac(MacParam param,byte[] pNeedCallMacDatas, ISecurity pSecurity);
}