package com.tseenola.postools.postools;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.util.Pair;

import com.tseenola.postools.security.des.DesImpl;
import com.tseenola.postools.security.des.TripleDes_Key16;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.algorithm.china.Mac_UnionEcb_Sm4;
import com.tseenola.postools.security.pos.mac.algorithm.international.Mac_9606;
import com.tseenola.postools.security.pos.mac.algorithm.international.Mac_UnionEcb;
import com.tseenola.postools.security.pos.mac.algorithm.international.Mac_x919;
import com.tseenola.postools.security.sm.Sm4;
import com.tseenola.postools.security.utils.Constant;
import com.tseenola.postools.security.utils.ConvertUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void testMac919(){
        String sm4Key = "11111111111111111111111111111111";
        String needCallMac = "1234567890123456AFAFAFAFAFAFAFAF3456789012345678ABCDEFABCDEFABCD";
        Mac_x919<Object> lObjectMac_x919 = new Mac_x919<>();
        Pair<Boolean, EncryResult> lMac = lObjectMac_x919.getMac(Constant.SOFT, ConvertUtils.hexStringToByte(sm4Key),
                null, ConvertUtils.hexStringToByte(needCallMac), new DesImpl());
        Log.d( " DEBUG2", "....................................: "   );
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

    }

    @Test
    public void testMacUnion(){
        String sm4Key = "11111111111111111111111111111111";
        String needCallMac = "1234567890123456AFAFAFAFAFAFAFAF3456789012345678ABCDEFABCDEFABCD";
        Mac_UnionEcb lMac_unionEcb = new Mac_UnionEcb();
        Pair<Boolean, EncryResult> lMac = lMac_unionEcb.getMac(Constant.SOFT, ConvertUtils.hexStringToByte(sm4Key),
                null, ConvertUtils.hexStringToByte(needCallMac), new TripleDes_Key16());
    }

    @Test
    public void testMacUnion_Sm4(){
        String sm4Key = "11111111111111111111111111111111";
        String needCallMac = "1234567890123456AFAFAFAFAFAFAFAF3456789012345678ABCDEFABCDEFABCD";
        Mac_UnionEcb_Sm4 lMac_unionEcb = new Mac_UnionEcb_Sm4 ();
        Pair<Boolean, EncryResult> lMac = lMac_unionEcb.getMac(Constant.SOFT, ConvertUtils.hexStringToByte(sm4Key),
                null, ConvertUtils.hexStringToByte(needCallMac), new Sm4());
    }

    @Test
    public void testMac9606(){
        String sm4Key = "11111111111111111111111111111111";
        String needCallMac = "1234567890123456AFAFAFAFAFAFAFAF3456789012345678ABCDEFABCDEFABCD";
        Mac_9606 lMac_unionEcb = new Mac_9606();
        Pair<Boolean, EncryResult> lMac = lMac_unionEcb.getMac(Constant.SOFT, ConvertUtils.hexStringToByte(sm4Key),
                null, ConvertUtils.hexStringToByte(needCallMac), new TripleDes_Key16());
    }

    /**
     * 测试磁道加密sm4
     */
    @Test
    public   void testSm4Track2() throws Exception {
        String track2 = "123456789012345678901234567890";//小于32偶数
        encryDataSM4_2(track2,2);
        getTrack2EncryData(track2);
        Log.d( " DEBUG2", "....................................: "   );
        String track3 = "12345678901234567890123456789";//小于32奇数
        encryDataSM4_2(track3,2);
        getTrack2EncryData(track3);
        Log.d( " DEBUG2", "....................................: "   );
        String track4 = "12345678901234567890123456789011";//等于32
        encryDataSM4_2(track4,2);
        getTrack2EncryData(track4);
        Log.d( " DEBUG2", "....................................: "   );
        String track5 = "123456789012345678901234567890112";//大于32奇数
        encryDataSM4_2(track5,2);
        getTrack2EncryData(track5);
        Log.d( " DEBUG2", "....................................: "   );
        String track6 = "1234567890123456789012345678901123";//大于32偶数
        encryDataSM4_2(track6,2);
        getTrack2EncryData(track6);
        Log.d( " DEBUG2", "....................................: "   );
    }


    public   String getTrack2EncryData(String data) {
        String track2 = data;
        if (data.length()%2!=0) {
            track2 += "F";
        }
        track2 = ConvertUtils.fillContentBy(ConvertUtils.Dir.right,"F",track2,34);
        Log.d(" DEBUG2", "getTrack2EncryData 预处理结果: " + track2);
        String ss = track2.substring(track2.length()-((16+1)*2),track2.length()-( 1 *2));
        Log.d( " DEBUG2", "getTrack2EncryData 被加密数据: " + ss);
        return ss;
    }

    public   void encryDataSM4_2(String data,int pTrackKeyIndex) throws Exception {
        boolean lenthMore32 = false;
        byte[] byteData;
        byte[] inputData;
        byte[] outputDate;
        if (data.length() < 32) {
            //byteData = Funs.StrToHexByte(formatData32(data));
            byteData = ConvertUtils.hexStringToByte(formatData32(data));
        } else if (data.length() == 32) {
            //byteData = Funs.StrToHexByte(data + "FF");
            byteData = ConvertUtils.hexStringToByte(data + "FF");
        } else if (data.length() % 2 != 0) {
            //byteData = Funs.StrToHexByte(data + "F");
            byteData = ConvertUtils.hexStringToByte(data + "F");
            lenthMore32 = true;
        } else {
            //byteData = Funs.StrToHexByte(data);
            byteData = ConvertUtils.hexStringToByte(data);
        }
        Log.d(" DEBUG2", "encryDataSM4_2 预处理结果: " + ConvertUtils.bytesToHexString(byteData));
        inputData = new byte[16];
        Arrays.fill(inputData, (byte) 0xFF);
        System.arraycopy(byteData, byteData.length - 17, inputData,0, 16);
        Log.d( " DEBUG2", "encryDataSM4_2 被加密数据: " + ConvertUtils.bytesToHexString(inputData));
    }

    private   String formatData32(String card){
        StringBuffer sb = new StringBuffer();
        int len = card.length();
        for (int i = 0;i < 32-len;i++){
            sb.append("F");
        }
        sb.append("FF");
        String ssdfs = card+sb.toString();
        return card+sb.toString();
    }
}
