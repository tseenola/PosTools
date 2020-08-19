package com.tseenola.postools.security.model;

/**
 * Created by lijun on 2020/7/30.
 */
public class BaseResult {
    protected String errMsg;

    public BaseResult(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
