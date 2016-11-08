package com.somnus.androidx5webviewdemo;

import android.app.Application;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

/**
 * @date： 2016/11/8.
 * @FileName: com.somnus.androidx5webviewdemo.X5Appcation.java
 * @author: Somnus
 * @Description:
 */

public class X5Appcation extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initX5();
    }

    private void initX5() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                Log.e("apptbs", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub

            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d("apptbs", "onDownloadFinish");
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d("apptbs", "onInstallFinish");
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d("apptbs", "onDownloadProgress:" + i);
            }
        });

        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
