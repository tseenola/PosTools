package com.tseenola.postools.security.pos.mac;

import com.tseenola.postools.security.pos.mac.algorithm.IMacCaculator;
import com.tseenola.postools.security.pos.mac.algorithm.Mac_9606;
import com.tseenola.postools.security.pos.mac.algorithm.Mac_UnionEcb;
import com.tseenola.postools.security.pos.mac.algorithm.Mac_x99;
import com.tseenola.postools.security.utils.Constant;

/**
 * Created by lijun on 2020/7/30.
 * 获取mac算法的工厂方法
 */
public class MacAlgorithFactory {
    public static IMacCaculator getMacCaculator(@Constant.MacType int pMacType){
        IMacCaculator iMacCaculator2 = null;
        switch (pMacType){
            case Constant.MAC_TYPE_UNION_ECB:
                iMacCaculator2 = new Mac_UnionEcb();
                break;
            case Constant.MAC_TYPE_9606:
                iMacCaculator2 = new Mac_9606();
                break;
            case Constant.MAC_TYPE_X99:
                iMacCaculator2 = new Mac_x99();
                break;
            case Constant.MAC_TYPE_X919:
                iMacCaculator2 = new Mac_x99();
                break;
            default:
                break;
        }
        return iMacCaculator2;
    }
}
