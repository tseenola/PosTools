package com.tseenola.postools.commonui.model;

/**
 * @Author: ZhengJuE
 * @CreateDate: 2020/8/31 19:53
 * @Description:
 * @UpdateUser: ZhengJuE
 * @UpdateDate: 2020/8/31 19:53
 * @UpdateRemark:
 * @Version: 1.0
 */
public class UploadLogResult {
    protected int status;
    protected Exception exception;
    public static final int STATUS_EXCEPTION = -1;
    public static final int STATUS_FAILURE = 0;
    public static final int STATUS_SUCCESS = 1;

    public UploadLogResult(int status, Exception exception) {
        this.status = status;
        this.exception = exception;
    }

    public int getStatus() {
        return this.status;
    }

    public Exception getException() {
        return this.exception;
    }
}
