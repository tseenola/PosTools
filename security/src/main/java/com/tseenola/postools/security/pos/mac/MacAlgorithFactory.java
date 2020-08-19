package com.tseenola.postools.security.pos.mac;

import com.tseenola.postools.security.pos.mac.algorithm.IMacCaculator2;
import com.tseenola.postools.security.pos.mac.algorithm.Mac_96062;
import com.tseenola.postools.security.pos.mac.algorithm.Mac_UnionEcb2;
import com.tseenola.postools.security.pos.mac.algorithm.Mac_x992;
import com.tseenola.postools.security.utils.Constant;

/**
 * Created by lijun on 2020/7/30.
 * 获取mac算法的工厂方法
 */
public class MacAlgorithFactory {
    public static IMacCaculator2 getMacCaculator(@Constant.MacType int pMacType){
        IMacCaculator2 iMacCaculator2 = null;
        switch (pMacType){
            case Constant.MAC_TYPE_UNION_ECB:
                iMacCaculator2 = new Mac_UnionEcb2();
                break;
            case Constant.MAC_TYPE_9606:
                iMacCaculator2 = new Mac_96062();
                break;
            case Constant.MAC_TYPE_X99:
                iMacCaculator2 = new Mac_x992();
                break;
            case Constant.MAC_TYPE_X919:
                iMacCaculator2 = new Mac_x992();
                break;
            default:
                break;
        }
        return iMacCaculator2;
    }
}
