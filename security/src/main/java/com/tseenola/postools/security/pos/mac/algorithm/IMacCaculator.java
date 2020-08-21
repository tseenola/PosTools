package com.tseenola.postools.security.pos.mac.algorithm;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.utils.Constant;
/**
 * Created by lijun on 2020/7/30.
 */
public interface IMacCaculator<T> {
    /**
     *
     * @param pSecurityBy      加密或者解密
     * @param pEncDecKey         软加密传入的Key
     * @param pSecurityHardParam  如果是软加密，这个参数不用传 硬加密需要传入的参数（主密钥，密钥索引，密钥类型，加密方式des,sm4等等.....）
     * @param pNeedCallMacDatas  待计算mac的数据
     * @param pSecurity
     * @return
     */
    Pair<Boolean, EncryResult> getMac(@Constant.SecurityBy int pSecurityBy, byte[] pEncDecKey, T pSecurityHardParam, byte[] pNeedCallMacDatas, ISecurity pSecurity);
}