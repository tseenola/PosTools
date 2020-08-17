package com.tseenola.postools.security.model;

public class EncryResult extends BaseResult{
    protected byte [] mac;
    public EncryResult(String pErrMsg){
        super(pErrMsg);
    }

    public EncryResult(byte[] pMac) {
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
