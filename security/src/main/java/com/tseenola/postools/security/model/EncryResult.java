package com.tseenola.postools.security.model;

/**
 * Created by lijun on 2020/7/30.
 */
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
