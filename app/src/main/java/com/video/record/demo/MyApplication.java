package com.video.record.demo;


import android.app.Application;
import android.os.Environment;

import com.yixia.camera.VCamera;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VCamera.setVideoCachePath( Environment.getExternalStorageDirectory().getAbsolutePath() + "/videoRecord/");
        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true);
        // 初始化拍摄SDK，必须
        VCamera.initialize(this);
    }
}
