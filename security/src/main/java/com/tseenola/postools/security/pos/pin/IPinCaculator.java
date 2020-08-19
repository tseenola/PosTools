package com.tseenola.postools.security.pos.pin;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.utils.Constant;

/**
 * Created by lijun on 2020/8/17.
 * 描述：
 */
public interface IPinCaculator<T> {
    /**
     * 对pin进行加密
     * @param pSecurityType         加密或者解密
     * @param pEncDecKey            软加密传入的Key
     * @param pSecurityHardParam   硬加密需要传入的参数（主密钥，密钥索引，密钥类型，加密方式des,sm4等等.....）
     * @param pPan                  卡号
     * @param pExplainPin           密码明文
     * @param pSecurity
     * @return
     */
    Pair<Boolean, EncryResult> getEncryedPin(@Constant.SecurityType int pSecurityType, byte[] pEncDecKey,T pSecurityHardParam,String pPan, String pExplainPin, ISecurity pSecurity);
}
