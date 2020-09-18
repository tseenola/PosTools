package com.tseenola.postools.commonui.model;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: ZhengJuE
 * @CreateDate: 2020/8/31 19:49
 * @Description:
 * @UpdateUser: ZhengJuE
 * @UpdateDate: 2020/8/31 19:49
 * @UpdateRemark:
 * @Version: 1.0
 */
public class LogHelp {
    public static  void upLog(final Context context, File[]fs, String sn, String serverUrl, final UploadListener listener){
        UploadLogHelper.uploadLog(context, serverUrl,
                fs, 10240, 3, "", sn,
                new SimpleDateFormat("yyyyMMdd").format(new Date()),
                new UploadLogModule.Callback() {

                    @Override
                    public void onStartUploading(UploadLogModule arg0) {
                        listener.onStartUploading(arg0);
                    }

                    @Override
                    public void onProgressChanged(UploadLogModule arg0, Progress arg1) {
                        listener.onProgressChanged(arg0, arg1);
                    }

                    @Override
                    public void onHandleException(UploadLogModule arg0, Exception arg1) {
                        listener.onHandleException(arg0, arg1);
                        Toast.makeText(context, "上传失败:"+arg1, Toast.LENGTH_SHORT).show();;
                    }

                    @Override
                    public void onFinishUploading(UploadLogModule arg0) {
                        listener.onFinishUploading(arg0);
                        Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();;
                    }
                });
    }
}
