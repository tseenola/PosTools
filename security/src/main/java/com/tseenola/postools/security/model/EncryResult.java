package com.tseenola.postools.security.model;

public class EncryResult extends BaseResult{
    protected byte [] encryDecryResult;
    public EncryResult(String pErrMsg){
        super(pErrMsg);
    }

    public EncryResult(byte[] pEncryDecryResult) {
        super("");
        this.encryDecryResult = pEncryDecryResult;
    }

    public byte[] getEncryDecryResult() {
        return encryDecryResult;
    }

    public void setEncryDecryResult(byte[] pEncryDecryResult) {
        this.encryDecryResult = pEncryDecryResult;
    }
}
