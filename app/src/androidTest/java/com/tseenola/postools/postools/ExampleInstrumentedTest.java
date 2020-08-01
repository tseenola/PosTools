package com.tseenola.postools.postools;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.tseenola.postools.security.des.DesImpl;
import com.tseenola.postools.security.pos.mac.Mac_UnionEcb;
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
        String needCallMacData = "12345678901234567890111234567890";
        byte [] needCallMac = ConvertUtils.hexStringToByte(needCallMacData);
        String key = "1111111111111111";
        byte [] keys = ConvertUtils.hexStringToByte(key);
        try {
            byte macResult [] = new Mac_UnionEcb().getMac(needCallMac,keys,DesImpl.getInstance(),2);
            String result = ConvertUtils.bytesToHexString(macResult);
            System.out.println("result:"+result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
