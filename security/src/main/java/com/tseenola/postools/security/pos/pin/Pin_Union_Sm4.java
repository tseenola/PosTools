package com.tseenola.postools.security.pos.pin;

import android.util.Log;
import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.utils.Constant;
import com.tseenola.postools.security.utils.ConvertUtils;

/**
 * 银联标准PIN加密算法
 * 2017年7月17日 by lijun for：
 * 算法描述
 * 1 卡号从右边第二个字符向左取12位，前面补20个0，组成32位16进制字符。
 * 2 密码按照一个字节长度+密码+不够32个16进制字符就补位F
 * 3 卡号和密码做亦或运算
 * 4 对亦或后的结果进行SM4加密
 */
public class Pin_Union_Sm4<T> implements IPinCaculator<T>{

    private String TAG = this.getClass().getSimpleName();

    @Override
    public Pair<Boolean, EncryResult> getEncryedPin(
            @Constant.SecurityBy int pSecurityBy, byte[] pEncDecKey,
            T pSecurityHardParam, String pPan, String pExplainPin, ISecurity pSecurity) {
        try{
            if (pSecurityBy == Constant.SOFT){
                Log.d(TAG + " DEBUG2", "getEncryedPin: 输入数据 pSecurityBy: " + pSecurityBy+" pEncDecKey: "+
                        ConvertUtils.bytesToHexString(pEncDecKey)+ " pPan: "+ pPan + " pExplainPin: "+ pExplainPin);
            }else if(pSecurityBy == Constant.HARD){
                Log.d(TAG + " DEBUG2", "getEncryedPin: 输入数据 pSecurityBy: " + pSecurityBy + " pPan: "+ pPan + " pExplainPin: "+ pExplainPin);
            }else {
                return Pair.create(false,new EncryResult("无效的加密类型"));
            }

            //1 卡号从右边第二个字符向左取12位，前面补20个0，组成32位16进制字符。
            int inputedCardNoLen = pPan.length();
            String cardno12 = pPan.substring(inputedCardNoLen-13,inputedCardNoLen-1);
            String cardno32 = "00000000000000000000"+cardno12;//补20个0，直到构成32个字符
            byte cardno_b [] = ConvertUtils.hexStringToByte(cardno32);
            Log.d(TAG + " DEBUG2", "getEncryedPin: 卡号从右边第二个字符向左取12位，前面补20个0，组成32位16进制字符: " + cardno32);
            //2.密码按照一个字节长度+密码+不够32个16进制字符就补位F
            String pin32 = "06"+pExplainPin+"FFFFFFFFFFFFFFFFFFFFFFFF";//补24个F填充直到构成32个字符
            byte pint_b [] = ConvertUtils.hexStringToByte(pin32);
            Log.d(TAG + " DEBUG2", "getEncryedPin:密码按照一个字节长度+密码+不够32个16进制字符就补位F: " + pin32);
            //3.对处理好以后的卡号和密码进行亦或
            byte xorResult [] = new byte[16];
            for(int i = 0;i<16;i++){
                xorResult[i] = (byte) (cardno_b[i] ^ pint_b[i]);
            }
            Log.d(TAG + " DEBUG2", "getEncryedPin:3.对处理好以后的卡号和密码进行亦或得到: " + ConvertUtils.bytesToHexString(xorResult));
            Pair<Boolean, EncryResult> lEncryResultPair = null;
            if (pSecurityBy == Constant.SOFT) {
                return pSecurity.encryDataSoft(xorResult,pEncDecKey);
            }else if (pSecurityBy == Constant.HARD){
                return pSecurity.encryDataHard(xorResult,pSecurityHardParam);
            }else {
                return Pair.create(false,new EncryResult("无效的参数【加密方式】"));
            }
        }catch(Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }
}
