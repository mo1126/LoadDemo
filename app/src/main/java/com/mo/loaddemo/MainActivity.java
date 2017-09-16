package com.mo.loaddemo;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private String url="http://125.39.134.47/r/a.gdown.baidu.com/data/wisegame/5d1417123bcf1bd9/wangyixinwen_727.apk?from=a1101";
    private ProgressDialog progressDialog;
    private Callback.Cancelable cancelable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         initDialog();
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "暂停", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //取消下载任务
                cancelable.cancel();
            }
        });
    }
    //检查版本号 更新
    public void checkVersion(View view) throws PackageManager.NameNotFoundException{
        //拿到版本号
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        int versionCode = packageInfo.versionCode;

        //进行网络请求
        Version version=new Version();
        version.setUrl(url);
        //比较版本号
        if(versionCode<version.getVersionCode()){
            File file=new File(Environment.getExternalStorageDirectory()+"/mo/versions.apk");
            downloadApk();
        }
    }

    //下载并安装
    private void downloadApk() {
        RequestParams requestParams=new RequestParams(url);
        requestParams.setAutoRename(true);//设置是否支持断点下载
        requestParams.setCancelFast(true);//设置是否立即取消
        //判断sdcard是否可用
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            //存在并可用
            requestParams.setSaveFilePath(Environment.getExternalStorageDirectory()+"/mo/versions.apk");
        }
       cancelable= x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                progressDialog.dismiss();

                install(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                progressDialog.show();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                    if(isDownloading){
                        progressDialog.setMax((int) total);
                        progressDialog.setProgress((int) current);
                        progressDialog.setTitle("下载进度");
                        System.out.println("current:" + (int) current * 100 / total);
                    }
            }
        });
    }

    //安装
    private void install(File file) {
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
