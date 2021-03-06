package com.tseenola.postools.security.utils;


import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * Created by lijun on 2016/11/20.
 * 描述：
 */
public class ConvertUtils {

    public static int PubBcdToByte(byte ch) {
        if (((ch & 0x0F) > 9) || ((ch >> 4) > 9))
            return 0;
        return (ch >> 4) * 10 + (ch & 0x0f);
    }

    /**
     * byte 转为16进制字符串
     *
     * @param bArray
     * @return
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 16进制字符串转换为byte 数组
     *
     * @param hex
     * @return
     */
    
    public static byte[] hexStringToByte( String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    /**
     * 二进制字符串转16进制字符串
     */
    public static String binaryStringToHexString(String binaryString) {
        if (binaryString == null || binaryString.length() % 4 != 0) {
            throw new IllegalArgumentException("┭┮﹏┭┮ 无效的位图");
        }
        String target = "";
        for (int i = 0; i < binaryString.length() / 4; i++) {
            String str = binaryString.substring(i * 4, i * 4 + 4);
            target += Integer.toString(Integer.parseInt(str, 2), 16);
        }
        return target.toUpperCase();
    }

    /**
     * 16进制字符串转为2进制字符串
     * @param hexString
     * @return
     */
    public static String hexStringToBinaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0)
            return null;
        String bString = "", tmp;
        for (int i = 0; i < hexString.length(); i++) {
            tmp = "0000"
                    + Integer.toBinaryString(Integer.parseInt(
                    hexString.substring(i, i + 1), 16));
            bString += tmp.substring(tmp.length() - 4);
        }
        return bString;
    }


    /**
     * 字符串转16进制字符串
     * @param
     * @return
     */
    /*public static String strToHexString(String pOrg) {
        String lTarget = "";
        for (int i = 0; i < pOrg.length(); i++) {
            int ch = (int) pOrg.charAt(i);
            String s4 = Integer.toHexString(ch);
            lTarget = lTarget + s4;
        }
        return lTarget.toUpperCase();
    }*/

    /**
     * 字符串转16进制字符串
     * @param
     * @return
     */
    public static String strToHexString(String pOrg) {
        try {
            if (TextUtils.isEmpty(pOrg)) {
                return "";
            }
            return bytesToHexString(pOrg.getBytes("gbk"));
        } catch (UnsupportedEncodingException pE) {
            pE.printStackTrace();
            return "";
        }
    }

    /**
     * 16进制字符串转为字符串
     * @return
     */
    public static String hexStringToStr(String pHexString){
        try {
            return new String(ConvertUtils.hexStringToByte(pHexString),"GBK");
        } catch (UnsupportedEncodingException pE) {
            pE.printStackTrace();
            return "";
        }
    }

    public static void BcdToAsc(byte[] sAscBuf, byte[] sBcdBuf, int iAscLen) {
        int i, j;

        j = 0;
        for (i = 0; i < iAscLen / 2; i++) {
            sAscBuf[j] = (byte) ((sBcdBuf[i] & 0xf0) >> 4);
            sAscBuf[j] = abcd_to_asc(sAscBuf[j]);
            j++;
            sAscBuf[j] = (byte) (sBcdBuf[i] & 0x0f);
            sAscBuf[j] = abcd_to_asc(sAscBuf[j]);
            j++;
        }
        if (iAscLen % 2 != 0) {
            sAscBuf[j] = (byte) ((sBcdBuf[i] & 0xf0) >> 4);
            sAscBuf[j] = abcd_to_asc(sAscBuf[j]);
        }
    }

    public static byte abcd_to_asc(byte ucBcd) {
        byte ucAsc;

        ucBcd &= 0x0f;
        if (ucBcd <= 9) {
            ucAsc = (byte) (ucBcd + (byte) '0');
        } else {
            ucAsc = (byte) (ucBcd + (byte) 'A' - (byte) 10);
        }
        return ucAsc;
    }


    public static void AscToBcd(byte[] sBcdBuf, byte[] sAscBuf, int iAscLen) {
        int i, j;

        j = 0;

        for (i = 0; i < (iAscLen + 1) / 2; i++) {
            sBcdBuf[i] = (byte) (aasc_to_bcd(sAscBuf[j++]) << 4);
            if (j >= iAscLen) {
                sBcdBuf[i] |= 0x00;
            } else {
                sBcdBuf[i] |= aasc_to_bcd(sAscBuf[j++]);
            }
        }
    }

    public static byte aasc_to_bcd(byte ucAsc) {
        byte ucBcd;

        if (ucAsc >= '0' && ucAsc <= '9')
            ucBcd = (byte) (ucAsc - '0');
        else if (ucAsc >= 'A' && ucAsc <= 'F')
            ucBcd = (byte) (ucAsc - 'A' + 10);
        else if (ucAsc >= 'a' && ucAsc <= 'f')
            ucBcd = (byte) (ucAsc - 'a' + 10);
        else if (ucAsc > 0x39 && ucAsc <= 0x3f)
            ucBcd = (byte) (ucAsc - '0');
        else ucBcd = 0x0f;

        return ucBcd;
    }

    /**
     * 对指定数据进行填充，直到达到需要的长度
     *
     * @param dir     在左或者右填充
     * @param fill    长度不足用什么填充
     * @param content 被填充的内容
     * @param mastLen 需要达到的长度
     * @return
     */

    public static String fillContentBy(Dir dir,  String fill,  String content, int mastLen){
        if(TextUtils.isEmpty(fill)){
            return "";
        }
        String x = fill;
        int contentLen = content.length();
        int needAddLen = mastLen - contentLen;
        if(needAddLen<=0){
            return content;
        }
        while(fill.length()<needAddLen){
            fill+=x;
        }
        if(dir== Dir.left){
            content = fill+content;
        }else{
            content = content+fill;
        }
        return content;
    }
    public enum Dir{
        left,right
    }

    /**
     * 异或
     * @param src1
     * @param src2
     * @param length
     */
    public static void doXor(byte[] src1, byte[] src2, int length) {
        int i;
        int data1 = 0;
        int data2 = 0;
        for (i = 0; i < length; i++) {
            data1 = 0x00ff & src1[i];
            data2 = 0x00ff & src2[i];
            data1 ^= data2;
            src1[i] = (byte) (data1 & 0x00ff);
        }
    }

    /**
     * 分组亦或
     * @param mabs 被分组亦或数据
     * @param pZuLen 多少字节一分组
     * @return
     */
    public static byte[] getResultBlock(byte[] mabs,int pZuLen) {
        int xorLen = pZuLen;
        byte[] mab1 = new byte[xorLen];
        byte[] temp = new byte[xorLen];
        System.arraycopy(mabs, 0, mab1, 0, xorLen);
        for (int i = xorLen; i < mabs.length; i += xorLen) {
            System.arraycopy(mabs, i, temp, 0, xorLen);
            Log.d(" DEBUG2", "doXor 亦或输入 mab1: "+ConvertUtils.bytesToHexString(mab1));
            Log.d(" DEBUG2", "doXor 亦或输入 temp: "+ConvertUtils.bytesToHexString(temp));
            ConvertUtils.doXor(mab1, temp, xorLen);
            Log.d(" DEBUG2", "doXor 亦或输出 mab1: "+ConvertUtils.bytesToHexString(mab1) );
            Log.d(" DEBUG2", "》》》》》》》》》》》》》》》》》" );
        }
        return mab1;
    }
}
