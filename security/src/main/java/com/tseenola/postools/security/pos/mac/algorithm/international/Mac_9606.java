package com.tseenola.postools.security.pos.mac.algorithm.international;


import android.util.Log;
import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.algorithm.IMacCaculator;
import com.tseenola.postools.security.utils.Constant;
import com.tseenola.postools.security.utils.ConvertUtils;

/**
 * Created by lijun on 2017/4/12.
 * 描述：Mac9606算法
 * 算法描述：
 *
 * a) 每8个字节做异或
 * b) 最后异或的结果做一次DES运算
 */
public class Mac_9606<T> implements IMacCaculator<T> {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public Pair<Boolean, EncryResult> getMac(@Constant.SecurityBy int pSecurityBy, byte[] pEncDecKey, T pSecurityHardParam, byte[] pNeedCallMacDatas, ISecurity pSecurity) {
        try{
            if (pNeedCallMacDatas ==null) {
                return Pair.create(false,new EncryResult("报文不能为空"));
            }
            String needCallMacStr = ConvertUtils.bytesToHexString(pNeedCallMacDatas);
            Log.d(TAG + " DEBUG2", "getMac: 被计算mac数据: " + needCallMacStr);
            //1.如果最后不满8个字节，则添加“0X00”。
            String mabStr = needCallMacStr;
            if(needCallMacStr.length() % 16 != 0) {
                int len = needCallMacStr.length() + (16 - needCallMacStr.length() % 16);
                mabStr = ConvertUtils.fillContentBy(ConvertUtils.Dir.right,"0",needCallMacStr,len);
                Log.d(TAG + " DEBUG2", "getMac: 1.如果最后不满8个字节，则添加“0X00”。" +needCallMacStr);
            }

            //2.按每 8 个字节做异或（不管信息中的字符格式）
            byte[] resultBlock = ConvertUtils.getResultBlock(ConvertUtils.hexStringToByte(mabStr),8);
            //3.最后异或的结果做一次DES运算
            Log.d(TAG + " DEBUG2", "getMac: 3.最后异或的结果做一次DES运算" );
            Pair<Boolean, EncryResult> lEncryResultPair = null;
            if (pSecurityBy == Constant.SOFT) {
                lEncryResultPair = pSecurity.encryDataSoft(resultBlock,pEncDecKey);
            }else if (pSecurityBy == Constant.HARD){
                lEncryResultPair = pSecurity.encryDataHard(resultBlock,pSecurityHardParam);
            }else {
                return Pair.create(false,new EncryResult("无效的参数【加密方式】"));
            }
            if (!lEncryResultPair.first) {
                return lEncryResultPair;
            }
            byte[] encBlock1 = lEncryResultPair.second.getEncryDecryResult();
            Log.d(TAG + " DEBUG2", "getMac: mac运算结果: " +ConvertUtils.bytesToHexString(encBlock1));
            return Pair.create(true,new EncryResult(encBlock1));
        }catch (Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }

    /*@Override
    public Pair<Boolean, EncryResult> getMac(@Constant.SecurityBy int pSecurityBy, byte[] pEncDecKey, T pSecurityHardParam, byte[] pNeedCallMacDatas, ISecurity pSecurity) {
        try{
            byte[] buf = new byte[17];
            byte[] tmpbuf = new byte[17];
            byte [] macOut = new byte[8];
            int i = 0;
            int l = 0;
            int k = 0 ;
            int iRet = 0;
            byte[] inbuf = new byte[pNeedCallMacDatas.length + 8];
            byte[] macbuf = new byte[9];
            Arrays.fill(buf, (byte) 0x00);
            System.arraycopy(pNeedCallMacDatas, 0, inbuf, 0, pNeedCallMacDatas.length);
            if (pNeedCallMacDatas.length % 8 != 0){
                l = pNeedCallMacDatas.length / 8 + 1;
            } else{
                l = pNeedCallMacDatas.length / 8;
            }

            buf = new byte[8];
            for (i = 0; i < l; i++) {
                for (k = 0; k < 8; k++) {
                    buf[k] ^= inbuf[i * 8 + k];
                }
            }
            Pair<Boolean, EncryResult> lEncryResultPair = null;
            if (pSecurityBy == Constant.SOFT) {
                lEncryResultPair = pSecurity.encryDataSoft(buf,pEncDecKey);
            }else if (pSecurityBy == Constant.HARD){
                lEncryResultPair = pSecurity.encryDataHard(buf,pSecurityHardParam);
            }else {
                return Pair.create(false,new EncryResult("无效的参数【加密方式】"));
            }
            if (!lEncryResultPair.first) {
                return lEncryResultPair;
            }
            macbuf = lEncryResultPair.second.getEncryDecryResult();
            System.arraycopy(macbuf, 0, macOut, 0, 8);
            return Pair.create(true,new EncryResult(macOut));
        }catch (Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }*/
}