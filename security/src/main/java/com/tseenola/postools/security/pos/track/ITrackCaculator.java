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
     * @param pSecurityBy         加密方式：1、软加密：传入密钥和数据，直接加密，
     *                            或者2、硬加密：传入参数给底层芯片，用芯片加密 {@link Constant.SecurityBy}
     * @param pEncDecKey            软加密传入的Key
     * @param pSecurityHardParam    硬加密参数，如果是软加密(pSecurityBy值为 SOFT )时 这个参数传 null 硬加密需要传入的参数（主密钥，密钥索引，密钥类型，加密方式des,sm4等等.....）
     * @param pExplainTrack         磁道明文
     * @param pTrackType            磁道类型 2、3 磁道，不同磁道加密逻辑不同
     * @param pSecurity         加密算法，SM4,3DES 等等
     *                          {@link com.tseenola.postools.security.sm.Sm4}
     *                          {@link com.tseenola.postools.security.des.3DES}
     * @return
     */
    Pair<Boolean, EncryResult> getEncryedTrack(@Constant.SecurityBy int pSecurityBy, byte[] pEncDecKey, T pSecurityHardParam, String pExplainTrack, @Constant.TrackType int pTrackType, ISecurity pSecurity);
}
