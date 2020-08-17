package com.tseenola.postools.security.pos.track;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.model.SecurityParam;
import com.tseenola.postools.security.utils.Constant;

/**
 * Created by lenovo on 2020/8/17.
 * 描述：
 */
public interface ITrackCaculator {
    /**
     * 对二磁道进行加密
     * @param param
     * @param pPan 卡号
     * @param pExplainPin 密码明文
     * @param pSecurity
     * @return
     */
    /**
     * 对 磁道进行加密
     * @param param
     * @param pExplainTrack 磁道明文
     * @param pTrackType 磁道类型 2、3 磁道
     * @param pSecurity
     * @return
     */
    Pair<Boolean, EncryResult> getEncryedTrack(SecurityParam param, String pExplainTrack, @Constant.TrackType int pTrackType, ISecurity pSecurity);
}
