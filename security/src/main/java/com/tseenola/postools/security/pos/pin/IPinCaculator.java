package com.tseenola.postools.security.pos.pin;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.model.SecurityParam;

/**
 * Created by lenovo on 2020/8/17.
 * 描述：
 */
public interface IPinCaculator {
    /**
     * 对pin进行加密
     * @param param
     * @param pPan 卡号
     * @param pExplainPin 密码明文
     * @param pSecurity
     * @return
     */
    Pair<Boolean, EncryResult> getEncryedPin(SecurityParam param, String pPan, String pExplainPin, ISecurity pSecurity);
}
