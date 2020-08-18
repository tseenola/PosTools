package com.tseenola.postools.security.pos.pin;

import android.util.Pair;

import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.model.SecurityParam;
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
public class Pin_Union implements IPinCaculator{

    @Override
    public Pair<Boolean, EncryResult> getEncryedPin(SecurityParam param, String pPan, String pExplainPin, ISecurity pSecurity) {
        try{
            //1 卡号从右边第二个字符向左取12位，前面补4个0，组成16位16进制字符。
            String inputtedCardNo = pPan;
            if(inputtedCardNo==null || inputtedCardNo.length()<13){
                return Pair.create(false,new EncryResult("卡号长度不够:"+pPan));
            }
            int inputedCardNoLen = inputtedCardNo.length();
            String cardno12 = inputtedCardNo.substring(inputedCardNoLen-13,inputedCardNoLen-1);

            String cardno16 = "0000"+cardno12;
            byte cardno_b [] = ConvertUtils.hexStringToByte(cardno16);

            //2 密码按照一个字节长度+密码+不够16个16进制字符就补位F
            String inputtedPin = pExplainPin;
            if(inputtedPin == null || inputtedPin.length()!=6){
                return Pair.create(false,new EncryResult("密码长度错误:"+inputtedPin));
            }
            String pin16 = "06"+inputtedPin+"FFFFFFFF";
            byte pint_b [] = ConvertUtils.hexStringToByte(pin16);
            //3 对处理好以后的卡号和密码进行亦或
            byte xorResult [] = new byte[8];
            for(int i = 0;i<8;i++){
                xorResult[i] = (byte) (cardno_b[i] ^ pint_b[i]);
            }
            //4 对亦或后的结果进行3DES加密
            byte pinEncResult_b [] = new byte[8];
            if (param.getmSecurityType() == Constant.SOFT) {
                return pSecurity.encryDataSoft(xorResult,param.getSoftEncryKeys());
            }else if (param.getmSecurityType() == Constant.HARD){
                return pSecurity.encryDataHard(xorResult,param.getHardEncryParam());
            }else {
                return Pair.create(false,new EncryResult("无效的参数【加密方式】"));
            }
        }catch(Exception pE){
            pE.printStackTrace();
            return Pair.create(false,new EncryResult(pE.getMessage()));
        }
    }
}
