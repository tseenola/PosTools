package com.tseenola.postools.security.pos.mac;


import com.tseenola.postools.security.des.DesImpl;
import com.tseenola.postools.security.intface.ISecurity;
import com.tseenola.postools.security.utils.ConvertUtils;

import java.util.Arrays;

/**
 * Created by lijun on 2017/4/12.
 * 描述：MacECB算法，银联商务标准算法
 * a) 将欲发送给POS中心的消息中，从消息类型（MTI）到63域之间的部分构成MAC ELEMEMENT BLOCK （MAB）。
 * b) 对MAB，按每8个字节做异或（不管信息中的字符格式），如果最后不满8个字节，则添加“0X00”。
 * c) 将异或运算后的最后8个字节（RESULT BLOCK）转换成16 个HEXDECIMAL：
 * d) 取前8 个字节用MAK加密：
 * e) 将加密后的结果与后8 个字节异或：
 * f) 用异或的结果TEMP BLOCK 再进行一次单倍长密钥算法运算。
 * g) 将运算后的结果（ENC BLOCK2）转换成16 个HEXDECIMAL：
 * h) 取前8个字节作为MAC值。
 */
public class Mac_UnionEcb implements IMacCaculator{

    @Override
    public byte[] getMacByHard(byte[] pNeedCallMacDatas, ISecurity pSecurity) throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] getMacBySoft(byte[] pNeedCallMacDatas, byte[] pKeys, ISecurity pSecurity) throws Exception {
        byte[] buf = new byte[17];
        byte[] tmpbuf = new byte[17];
        byte [] macOut = new byte[8];
        int i = 0;
        int l = 0;
        int k = 0 ;

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
        for (i = 0; i < l; i++)
            for (k = 0; k < 8; k++)
                buf[k] ^= inbuf[i * 8 + k];

        ConvertUtils.BcdToAsc(tmpbuf, buf, 16);
        tmpbuf[16] = 0;

        System.arraycopy(tmpbuf, 0, buf, 0, 8);
        macbuf = DesImpl.getInstance().encryDataSoft(buf,pKeys);
        //Macbuf = DesUtils.encrypt(pKeys, buf, DesImpl.getInstance());

        Arrays.fill(buf, (byte) 0x00);
        System.arraycopy(macbuf, 0, buf, 0, 8);

        for (k = 0; k < 8; k++)
            buf[k] ^= tmpbuf[8 + k];

        Arrays.fill(macbuf, (byte) 0x00);

        macbuf = DesImpl.getInstance().encryDataSoft(buf,pKeys);
        //Macbuf = DesUtils.encrypt(pKeys, buf, DesImpl.getInstance());

        Arrays.fill(buf, (byte) 0x00);
        System.arraycopy(macbuf, 0, buf, 0, 8);

        Arrays.fill(tmpbuf, (byte) 0x00);
        ConvertUtils.BcdToAsc(tmpbuf, buf, 16);
        System.arraycopy(tmpbuf, 0, macOut, 0, 8);
        return macOut;
    }
}