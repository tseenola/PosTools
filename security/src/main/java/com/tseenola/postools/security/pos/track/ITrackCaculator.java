package com.tseenola.postools.security.pos.track;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.utils.Constant;

/**
 * Created by lijun on 2020/8/17.
 * 描述：
 */
public interface ITrackCaculator<T> {
    /**
     * 对2、3 磁道进行加密
     * @param pSecurityBy         加密或者解密
     * @param pEncDecKey            软加密传入的Key
     * @param pSecurityHardParam    硬加密需要传入的参数（主密钥，密钥索引，密钥类型，加密方式des,sm4等等.....）
     * @param pExplainTrack         磁道明文
     * @param pTrackType            磁道类型 2、3 磁道，不同磁道加密逻辑不同
     * @param pSecurity
     * @return
     */
    Pair<Boolean, EncryResult> getEncryedTrack(@Constant.SecurityBy int pSecurityBy, byte[] pEncDecKey, T pSecurityHardParam, String pExplainTrack, @Constant.TrackType int pTrackType, ISecurity pSecurity);
}
