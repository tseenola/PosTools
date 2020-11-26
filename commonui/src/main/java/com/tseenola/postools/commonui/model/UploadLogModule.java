package com.tseenola.postools.commonui.model;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @CreateDate: 2020/8/31 19:53
 * @Description:
 * @UpdateDate: 2020/8/31 19:53
 * @UpdateRemark:
 * @Version: 1.0
 */
public class UploadLogModule extends AsyncTask<Void, Long, UploadLogResult> {
    protected Context ctx;
    protected String url;
    protected File[] uploadFiles;
    protected File zipFile;
    protected int uploadBufferLength;
    protected int retryCount;
    protected String commandCode;
    protected String dvcId;
    protected String uploadTime;
    protected Callback callback;

    public UploadLogModule(Context ctx, String url, File[] uploadFiles, int uploadBufferLength, int retryCount, String commandCode, String dvcId, String uploadTime) {
        this.ctx = ctx;
        this.url = url;
        this.uploadFiles = uploadFiles;
        this.zipFile = new File(ctx.getCacheDir(), "Log_" + dvcId+"_"+uploadTime + ".zip");
        this.uploadBufferLength = uploadBufferLength;
        this.retryCount = retryCount;
        this.commandCode = commandCode;
        this.dvcId = dvcId;
        this.uploadTime = uploadTime;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    protected int read(byte[] buffer, File file, long start) throws Exception {
        FileInputStream input = null;
        int readLen;
        try {
            input = new FileInputStream(file);
            input.skip(start);
            readLen = input.read(buffer);
        } catch (Exception var11) {
            throw var11;
        } finally {
            if (input != null) {
                input.close();
            }

        }

        return readLen;
    }

    protected String doPost(String url, byte[] params, int start, int len, Map<String, String> headers) throws Exception {
        byte[] strBytes = HttpTool.doPost(url, headers, params, start, len, 30000, 30000, false);
        return new String(strBytes, "UTF-8");
    }

    protected void zipFiles(File[] srcfile, File zipfile) throws Exception {
        ZipOutputStream output = new ZipOutputStream(new FileOutputStream(zipfile));

        for(int i = 0; i < srcfile.length; ++i) {
            FileInputStream input = new FileInputStream(srcfile[i]);
            output.putNextEntry(new ZipEntry(srcfile[i].getName()));
            byte[] buf = new byte[1024];

            int len;
            while((len = input.read(buf)) > 0) {
                output.write(buf, 0, len);
            }

            input.close();
            output.closeEntry();
        }

        output.close();
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (this.callback != null) {
            this.callback.onStartUploading(this);
        }

    }

    protected UploadLogResult doInBackground(Void... params) {
        UploadLogResult taskResult = null;

        try {
            this.zipFiles(this.uploadFiles, this.zipFile);
            byte[] buffer = new byte[this.uploadBufferLength];
            long start = 0L;
            int retry = 0;
            Map<String, String> headers = new HashMap();
            headers.put("fileID", UUID.randomUUID().toString());
            headers.put("fileName", this.zipFile.getName());
            headers.put("fileLen", String.valueOf(this.zipFile.length()));
            headers.put("commandCode", this.commandCode == null ? "" : this.commandCode);
            headers.put("dvcId", this.dvcId);
            headers.put("writeDate", this.uploadTime);

            int len;
            while((len = this.read(buffer, this.zipFile, start)) > 0) {
                try {
                    headers.put("startLen", String.valueOf(start));
                    String result = this.doPost(this.url, buffer, 0, len, headers);
                    String[] arr = result.split("\\|");
                    if ("failed".equals(arr[0])) {
                        throw new IllegalArgumentException("参数错误");
                    }

                    if ("ok".equals(arr[0])) {
                        String currLen = arr[1];
                        start = Long.valueOf(currLen);
                        this.publishProgress(new Long[]{start});
                        retry = 0;
                    } else if ("finish".equals(arr[0])) {
                        taskResult = new UploadLogResult(1, (Exception)null);
                        break;
                    }
                } catch (Exception var16) {
                    ++retry;
                    if (retry >= this.retryCount) {
                        throw var16;
                    }
                }
            }
        } catch (Exception var17) {
            taskResult = new UploadLogResult(-1, var17);
        } finally {
            if (this.zipFile != null && this.zipFile.exists() && this.zipFile.isFile()) {
                this.zipFile.delete();
            }

        }

        return taskResult;
    }

    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        if (this.callback != null) {
            this.callback.onProgressChanged(this, new Progress(this.zipFile.length(), values[0]));
        }

    }

    protected void onPostExecute(UploadLogResult result) {
        super.onPostExecute(result);
        if (result.getStatus() == 1) {
            if (this.callback != null) {
                this.callback.onFinishUploading(this);
            }
        } else if (result.getStatus() == -1 && this.callback != null) {
            this.callback.onHandleException(this, result.getException());
        }

    }

    public interface Callback {
        void onStartUploading(UploadLogModule var1);

        void onProgressChanged(UploadLogModule var1, Progress var2);

        void onFinishUploading(UploadLogModule var1);

        void onHandleException(UploadLogModule var1, Exception var2);
    }
}
