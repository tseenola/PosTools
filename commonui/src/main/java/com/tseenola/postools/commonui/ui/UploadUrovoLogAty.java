package com.tseenola.postools.commonui.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tseenola.postools.commonui.R;
import com.tseenola.postools.commonui.model.LogHelp;
import com.tseenola.postools.commonui.model.Progress;
import com.tseenola.postools.commonui.model.UploadListener;
import com.tseenola.postools.commonui.model.UploadLogModule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.ProgressDialog.STYLE_SPINNER;

/**
 * Created by lenovo on 2020/9/18.
 * 描述：
 */
public class UploadUrovoLogAty extends Activity implements AdapterView.OnItemClickListener,UploadListener {
    public static final String EXTRA_LOG_PATH = "logPath";//日志路径
    public static final String EXTRA_SN_AND_PACKAGE = "snAndPackage";//sn号和包名
    public static final String EXTRA_UPLOAD_URL = "uploadUrl";//上传服务器地址
    protected ListView mLvLogList;
    protected List<String> mLogFileList = new ArrayList<>();
    private String mLogPath;
    protected String mSnAndPackage;
    protected String mUploadUrl;
    protected String mReadCardLogPath;
    protected ProgressDialog mWaitDialog;
    protected AlertDialog.Builder mChoiceTowDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_log);
        mLvLogList = (ListView) findViewById(R.id.lv_LogList);
        setTitle("日志上传");
        //获取交易日志
        mLogPath = getIntent().getStringExtra(EXTRA_LOG_PATH);
        mSnAndPackage = getIntent().getStringExtra(EXTRA_SN_AND_PACKAGE);
        mUploadUrl = getIntent().getStringExtra(EXTRA_UPLOAD_URL);
        if (TextUtils.isEmpty(mLogPath)) {
            Toast.makeText(this, "缺少日志上传参数【logPath】", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (TextUtils.isEmpty(mSnAndPackage)) {
            Toast.makeText(this, "缺少日志上传参数【snAndPackage】", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (TextUtils.isEmpty(mUploadUrl)) {
            Toast.makeText(this, "缺少日志上传参数【uploadUrl】", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        File lFile = new File(mLogPath);//交易日志
        if (lFile!=null && lFile.exists()){
            String fileList [] = lFile.list();
            if (fileList!=null && fileList.length>0){
                for (int lI = fileList.length - 1; lI >= 0; lI--) {
                    mLogFileList.add(fileList[lI]);
                }
            }
        }
        //获取读卡日志
        File readCardLogFile = new File(Environment.getExternalStorageDirectory(), "UROPE");
        if (readCardLogFile!=null && readCardLogFile.exists()){
            mReadCardLogPath = readCardLogFile.getAbsolutePath();
            String fileList [] = readCardLogFile.list();
            if (fileList!=null && fileList.length>0){
                for (int lI = fileList.length - 1; lI >= 0; lI--) {
                    mLogFileList.add(fileList[lI]);
                }
            }
        }
        mLvLogList.setAdapter(new MyAdapter());
        mLvLogList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> pAdapterView, View pView, final int pI, long pL) {
        mChoiceTowDialog = new AlertDialog.Builder(this);
        mChoiceTowDialog.setTitle("提示");
        mChoiceTowDialog.setMessage("确定上传\n"+mLogFileList.get(pI)+"?");
        mChoiceTowDialog.setCancelable(false);    //设置是否可以通过点击对话框外区域或者返回按键关闭对话框
        mChoiceTowDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                File lFile = null;
                if (mLogFileList.get(pI).contains("ICCMD") || mLogFileList.get(pI).contains("Trace")) {
                    lFile = new File(mReadCardLogPath+File.separator+mLogFileList.get(pI));
                }else {
                    lFile = new File(mLogPath+File.separator+mLogFileList.get(pI));
                }

                if (lFile!=null && lFile.exists()){
                    //上传Urovo 服务器
                    List<File> fileList = new ArrayList<>();
                    fileList.add(lFile);
                    uploadFiles(fileList);
                }else {
                    Toast.makeText(UploadUrovoLogAty.this, "上传失败，文件不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mChoiceTowDialog.show();
    }

    /**
     * 上传文件
     * @param uploadFiles
     */
    private void uploadFiles(List<File> uploadFiles) {
        File[] fs = new File[uploadFiles.size()];
        for (int i = 0; i < uploadFiles.size(); i++) {
            fs[i] = uploadFiles.get(i);
        }
        LogHelp.upLog(UploadUrovoLogAty.this, fs, mSnAndPackage,
                mUploadUrl, this);
    }

    @Override
    public void onStartUploading(UploadLogModule uploadLogModule) {
        mWaitDialog = new ProgressDialog(this);
        mWaitDialog.setTitle("等待 ProgressDialog");
        mWaitDialog.setMessage("请稍等");
        mWaitDialog.setProgressStyle(STYLE_SPINNER);
        mWaitDialog.setCancelable(true);
        mWaitDialog.show();
    }

    @Override
    public void onProgressChanged(UploadLogModule uploadLogModule, Progress progress) {
    }

    @Override
    public void onHandleException(UploadLogModule uploadLogModule, Exception exception) {
        try {
            mWaitDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "上传失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishUploading(UploadLogModule uploadLogModule) {
        try {
            mWaitDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mLogFileList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(UploadUrovoLogAty.this).inflate(R.layout.item_log_list,null);
            TextView lLogNameTv = view.findViewById(R.id.tv_LogName);
            lLogNameTv.setText(mLogFileList.get(position));
            return view;
        }
    }
}
