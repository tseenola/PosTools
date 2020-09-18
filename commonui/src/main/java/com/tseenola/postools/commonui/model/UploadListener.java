package com.tseenola.postools.commonui.model;

/**
 * @Author: ZhengJuE
 * @CreateDate: 2020/8/31 20:26
 * @Description:
 * @UpdateUser: ZhengJuE
 * @UpdateDate: 2020/8/31 20:26
 * @UpdateRemark:
 * @Version: 1.0
 */
public interface UploadListener {
    void onStartUploading(UploadLogModule uploadLogModule);
    void onProgressChanged(UploadLogModule uploadLogModule, Progress progress);
    void onHandleException(UploadLogModule uploadLogModule, Exception exception);
    void onFinishUploading(UploadLogModule uploadLogModule);
}
