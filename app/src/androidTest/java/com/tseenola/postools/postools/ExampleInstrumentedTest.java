package com.tseenola.postools.postools;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import android.util.Pair;

import com.tseenola.postools.security.des.DesImpl;
import com.tseenola.postools.security.model.EncryResult;
import com.tseenola.postools.security.pos.mac.algorithm.international.Mac_UnionEcb;
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
        String sm4Key = "11111111111111111111111111111111";
        String needCallMac = "12345678901234567890123456789012345678901234567890123456789012345678901234567890";
        Mac_UnionEcb lMac_unionEcb = new Mac_UnionEcb();
        Pair<Boolean, EncryResult> lMac = lMac_unionEcb.getMac(Constant.SOFT, ConvertUtils.hexStringToByte(sm4Key),
                null, ConvertUtils.hexStringToByte(needCallMac), new DesImpl());
        if (lMac.first) {
            byte mac [] = lMac.second.getEncryDecryResult();
            Log.d( " DEBUG2", "useAppContext: mac " +ConvertUtils.bytesToHexString(mac) );;

        }else {
            Log.d( " DEBUG2", "useAppContext:err "   );
        }
    }
}
