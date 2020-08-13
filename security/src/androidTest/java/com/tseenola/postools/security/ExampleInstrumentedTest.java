package com.tseenola.postools.security;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Pair;

import com.tseenola.postools.security.des.DesImpl;
import com.tseenola.postools.security.pos.mac.Mac_UnionEcb2;
import com.tseenola.postools.security.pos.mac.model.MacParam;
import com.tseenola.postools.security.pos.pin.Pin_Union;
import com.tseenola.postools.security.utils.ConvertUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

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
        MacParam lMacParam = new MacParam(ConvertUtils.hexStringToByte("12345678901234567890123456789011"));
        String needCallMac = "12345678901234567890123456789011";
        new Mac_UnionEcb2().getMac(lMacParam,ConvertUtils.hexStringToByte(needCallMac),DesImpl.getInstance());

    }
}
