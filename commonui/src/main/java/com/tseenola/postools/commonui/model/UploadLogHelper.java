package com.tseenola.postools.commonui.model;

import android.content.Context;

import java.io.File;

/**
 * @CreateDate: 2020/8/31 19:52
 * @Description:
 * @UpdateDate: 2020/8/31 19:52
 * @UpdateRemark:
 * @Version: 1.0
 */
public class UploadLogHelper {
    public UploadLogHelper() {
    }

    public static void uploadLog(Context ctx, String url, File[] uploadFiles, int uploadBufferLength, int retryCount, String commandCode, String dvcId, String uploadTime, UploadLogModule.Callback callback) {
        UploadLogModule module = new UploadLogModule(ctx, url, uploadFiles, uploadBufferLength, retryCount, commandCode, dvcId, uploadTime);
        module.setCallback(callback);
        module.execute(new Void[0]);
    }
}
