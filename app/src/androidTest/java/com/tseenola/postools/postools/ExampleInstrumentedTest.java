package com.tseenola.postools.postools;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Pair;

import com.tseenola.postools.security.des.TripleDes_Key16;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.algorithm.china.Mac_UnionEcb_Sm4;
import com.tseenola.postools.security.pos.mac.algorithm.international.Mac_9606;
import com.tseenola.postools.security.pos.mac.algorithm.international.Mac_UnionEcb;
import com.tseenola.postools.security.sm.Sm4;
import com.tseenola.postools.security.utils.Constant;
import com.tseenola.postools.security.utils.ConvertUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
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
}
