package com.tseenola.postools.security.pos.track;

import android.text.TextUtils;
import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.utils.Constant;
import com.tseenola.postools.security.utils.ConvertUtils;

/**
 * Created by lijun on 2020/8/17.
 *  描述：银联标准加密
 *  2017年7月17日 by lijun for：
 *  算法描述
 *  1  对于二磁道或三磁道缺失的情况，终端应上送16字节全F
 *  2  二磁道数据（35域）从结束标志“？”向左第2个字节开始，再向左取16个字节作为参与加密的二磁道中发卡方信息，记为TDB2。
 *  3  类似二磁道数据源构造方法，三磁道数据（36域，如果存在）磁道信息块构造方法如下：
 *         三磁道数据（36域）从结束标志“？”向左第2个字节开始，再向左取16个字节作为参与加密的三磁道中发卡方信息（若不足右补足F），记为TDB3。
 *  4  采用双倍长密钥TDK分别对TDB2，TDB3进行加密，将密文输出后按照对应位置替换原先的明文数据。
 */
public class Track_Union_Sm4 implements ITrackCaculator{

    private String TAG = this.getClass().getSimpleName();
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
    @Override
    public Pair<Boolean, EncryResult> getEncryedTrack(@Constant.SecurityBy int pSecurityBy, byte[] pEncDecKey, Object pSecurityHardParam, String pExplainTrack, int pTrackType, ISecurity pSecurity) {
        try{
            //1  对于二磁道或三磁道缺失的情况，终端应上送8字节全F
            String src = pExplainTrack;
            if (TextUtils.isEmpty(src)) {
                return Pair.create(true,new EncryResult(ConvertUtils.hexStringToByte("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF")));
            }
            src = src.replace('=','D');
            src = ConvertUtils.fillContentBy(ConvertUtils.Dir.right,"F",src,34);
            //2,3
            String encryTemp = src.substring(src.length()-((16+1)*2),src.length()-( 1 *2));
            //4  采用双倍长密钥TDK分别对TDB2，TDB3进行加密，将密文输出后按照对应位置替换原先的明文数据。
            byte[] encryOut = new byte[16];
            Pair<Boolean, EncryResult> lEncryResultPair = null;
            if (pSecurityBy == Constant.SOFT) {
                lEncryResultPair = pSecurity.encryDataSoft(ConvertUtils.hexStringToByte(encryTemp),pEncDecKey);
            }else if (pSecurityBy == Constant.HARD){
                lEncryResultPair = pSecurity.encryDataHard(ConvertUtils.hexStringToByte(encryTemp),pSecurityHardParam);
            }else {
                return Pair.create(false,new EncryResult("无效的参数【加密方式】"));
            }
            if (!lEncryResultPair.first) {
                return lEncryResultPair;
            }
            encryOut = lEncryResultPair.second.getEncryDecryResult();
            String trackEnryData = src.replaceFirst(encryTemp,ConvertUtils.bytesToHexString(encryOut));
            return Pair.create(true,new EncryResult(ConvertUtils.hexStringToByte(trackEnryData)));
        }catch(Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }
}
