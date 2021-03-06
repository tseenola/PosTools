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
 * 1 卡号从右边第二个字符向左取12位，前面补4个0，组成16位16进制字符。
 * 2 密码按照一个字节长度+密码+不够16个16进制字符就补位F
 * 3 卡号和密码做亦或运算
 * 4 对亦或后的结果进行3DES加密
 */
public class Pin_Union<T> implements IPinCaculator<T>{

    private String TAG = this.getClass().getSimpleName();
    /**
     * 对pin进行加密
     * @param pSecurityBy         加密方式：1、软加密：传入密钥和数据，直接加密，
     *                            或者2、硬加密：传入参数给底层芯片，用芯片加密 {@link Constant.SecurityBy}
     * @param pEncDecKey            软加密传入的Key
     * @param pSecurityHardParam   硬加密参数，如果是软加密(pSecurityBy值为 SOFT )时 这个参数传 null 硬加密需要传入的参数（主密钥，密钥索引，密钥类型，加密方式des,sm4等等.....）
     * @param pPan                  卡号
     * @param pExplainPin           密码明文
     * @param pSecurity         加密算法，SM4,3DES 等等
     *                          {@link com.tseenola.postools.security.sm.Sm4}
     *                          {@link com.tseenola.postools.security.des.3DES}
     * @return
     */
    @Override
    public Pair<Boolean, EncryResult> getEncryedPin(@Constant.SecurityBy int pSecurityBy, byte[] pEncDecKey, T pSecurityHardParam, String pPan, String pExplainPin, ISecurity pSecurity) {
        try{
            if (pSecurityBy == Constant.SOFT){
                Log.d(TAG + " DEBUG2", "getEncryedPin: 输入数据 pSecurityBy: " + pSecurityBy+" pEncDecKey: "+
                        ConvertUtils.bytesToHexString(pEncDecKey)+ " pPan: "+ pPan + " pExplainPin: "+ pExplainPin);
            }else if(pSecurityBy == Constant.HARD){
                Log.d(TAG + " DEBUG2", "getEncryedPin: 输入数据 pSecurityBy: " + pSecurityBy + " pPan: "+ pPan + " pExplainPin: "+ pExplainPin);
            }else {
                return Pair.create(false,new EncryResult("无效的加密类型"));
            }
            //1 卡号从右边第二个字符向左取12位，前面补4个0，组成16位16进制字符。
            String inputtedCardNo = pPan;
            if(inputtedCardNo==null || inputtedCardNo.length()<13){
                return Pair.create(false,new EncryResult("卡号长度不够:"+pPan));
            }

            int inputedCardNoLen = inputtedCardNo.length();
            String cardno12 = inputtedCardNo.substring(inputedCardNoLen-13,inputedCardNoLen-1);
            String cardno16 = "0000"+cardno12;
            byte cardno_b [] = ConvertUtils.hexStringToByte(cardno16);
            Log.d(TAG + " DEBUG2", "getEncryedPin: 卡号从右边第二个字符向左取12位，前面补4个0，组成16位16进制字符: " + cardno16);
            //2 密码按照一个字节长度+密码+不够16个16进制字符就补位F
            String inputtedPin = pExplainPin;
            if(inputtedPin == null || inputtedPin.length()!=6){
                return Pair.create(false,new EncryResult("密码长度错误:"+inputtedPin));
            }
            String pin16 = "06"+inputtedPin+"FFFFFFFF";
            byte pint_b [] = ConvertUtils.hexStringToByte(pin16);
            Log.d(TAG + " DEBUG2", "getEncryedPin:密码按照一个字节长度+密码+不够16个16进制字符就补位F: " + pin16);
            //3 对处理好以后的卡号和密码进行亦或
            byte xorResult [] = new byte[8];
            for(int i = 0;i<8;i++){
                xorResult[i] = (byte) (cardno_b[i] ^ pint_b[i]);
            }
            Log.d(TAG + " DEBUG2", "getEncryedPin:3.对处理好以后的卡号和密码进行亦或得到: " + ConvertUtils.bytesToHexString(xorResult));
            //4 对亦或后的结果进行3DES加密
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
