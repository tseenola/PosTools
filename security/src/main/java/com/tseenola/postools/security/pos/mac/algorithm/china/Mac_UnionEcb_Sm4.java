package com.tseenola.postools.security.pos.mac.algorithm.china;

import android.util.Log;
import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.algorithm.IMacCaculator;
import com.tseenola.postools.security.utils.Constant;
import com.tseenola.postools.security.utils.ConvertUtils;

/**
 * Created by lijun on 2017/4/12.
 * 描述：MacECB算法，银联商务标准算法
 * a) 将欲发送给POS中心的消息中，从消息类型（MTI）到63域之间的部分构成MAC ELEMEMENT BLOCK （MAB）。
 * b) 对MAB，按每 16 个字节做异或（不管信息中的字符格式），如果最后不满16个字节，则添加“0X00”。
 * c) 将异或运算后的最后16个字节（RESULT BLOCK）转换成32 个HEXDECIMAL：
 * d) 取前16 个字节用MAK加密：
 * e) 将加密后的结果与后16 个字节异或：
 * f) 用异或的结果TEMP BLOCK 再进行一次单倍长密钥算法运算。
 * g) 将运算后的结果（ENC BLOCK2）转换成32 个HEXDECIMAL：
 * h) 取前8个字节作为MAC值。
 */
public class Mac_UnionEcb_Sm4 <T> implements IMacCaculator<T> {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public Pair<Boolean, EncryResult> getMac(@Constant.SecurityBy int pSecurityBy, byte[] pEncDecKey, T pSecurityHardParam, byte[] pNeedCallMacDatas, ISecurity pSecurity) {
        try{
            if (pNeedCallMacDatas ==null) {
                return Pair.create(false,new EncryResult("报文不能为空"));
            }
            String needCallMacStr = ConvertUtils.bytesToHexString(pNeedCallMacDatas);
            Log.d(TAG + " DEBUG2", "getMac: 被计算mac数据" +needCallMacStr);
            //1.如果最后不满16个字节，则添加“0X00”。
            String mabStr = needCallMacStr;
            if(needCallMacStr.length() % 32 != 0) {
                int len = needCallMacStr.length() + (32 - needCallMacStr.length() % 32);
                mabStr = ConvertUtils.fillContentBy(ConvertUtils.Dir.right,"0",needCallMacStr,len);
                Log.d(TAG + " DEBUG2", "getMac: 1.如果最后不满16个字节，则添加“0X00”。" +needCallMacStr);
            }

            //2.按每 16 个字节做异或（不管信息中的字符格式）
            byte[] resultBlock = getResultBlock(ConvertUtils.hexStringToByte(mabStr));
            //3.将异或运算后的最后16个字节（RESULT BLOCK）转换成32 个HEXDECIMAL：
            String resultBlockStr = ConvertUtils.bytesToHexString(resultBlock);
            Log.d(TAG + " DEBUG2", "getMac: 3.将异或运算后的最后16个字节（RESULT BLOCK）转换成32 个HEXDECIMAL：" + resultBlockStr);
            //4.取前16 个字节用MAK加密：
            byte buf [] = resultBlockStr.substring(0,16).getBytes();
            Log.d(TAG + " DEBUG2", "getMac: 4.取前16 个字节用MAK加密：" +buf );
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
            byte[] encBlock1 = lEncryResultPair.second.getEncryDecryResult();
            //5.将加密后的结果与后16 个字节异或：
            ConvertUtils.doXor(encBlock1,resultBlockStr.substring(16,32).getBytes(),16);
            Log.d(TAG + " DEBUG2", "getMac: 5.将加密后的结果与后16 个字节异或：" + ConvertUtils.bytesToHexString(encBlock1));
            //6.用异或的结果TEMP BLOCK 再进行一次单倍长密钥算法运算。
            if (pSecurityBy == Constant.SOFT) {
                lEncryResultPair = pSecurity.encryDataSoft(encBlock1,pEncDecKey);
            }else if (pSecurityBy == Constant.HARD){
                lEncryResultPair = pSecurity.encryDataHard(encBlock1,pSecurityHardParam);
            }else {
                return Pair.create(false,new EncryResult("无效的参数【加密方式】"));
            }
            if (!lEncryResultPair.first) {
                return lEncryResultPair;
            }
            byte[] encBlock2 = lEncryResultPair.second.getEncryDecryResult();
            //7.将运算后的结果（ENC BLOCK2）转换成32 个HEXDECIMAL：
            String encBlock2Str = ConvertUtils.bytesToHexString(encBlock2);
            Log.d(TAG + " DEBUG2", "getMac: 7.将运算后的结果（ENC BLOCK2）转换成32 个HEXDECIMAL：" + encBlock2Str);
            //8.取前8个字节作为MAC值。
            byte mac [] = encBlock2Str.substring(0,8).getBytes();
            Log.d(TAG + " DEBUG2", "getMac: 8.取前8个字节作为MAC值。" + ConvertUtils.bytesToHexString(mac));
            return Pair.create(true,new EncryResult(mac));
        }catch (Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }

    //根据银联算法，将mab分组异或
    public byte[] getResultBlock(byte[] mabs) {
        int xorLen = 16;
        byte[] mab1 = new byte[xorLen];
        byte[] temp = new byte[xorLen];
        System.arraycopy(mabs, 0, mab1, 0, xorLen);

        for (int i = xorLen; i < mabs.length; i += xorLen) {
            System.arraycopy(mabs, i, temp, 0, xorLen);
            ConvertUtils.doXor(mab1, temp, xorLen);
        }

        return mab1;
    }
}