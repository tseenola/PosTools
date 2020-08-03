package com.tseenola.postools.security.model;

public class MacResult extends BaseResult{
    protected byte [] mac;
    public MacResult(String pErrMsg){
        super(pErrMsg);
    }

    public MacResult(byte[] pMac) {
        super("");
        this.mac = pMac;
    }

    public byte[] getMac() {
        return mac;
    }

    public void setMac(byte[] mac) {
        this.mac = mac;
    }
}
