前言
--
基于VCamera二次开发，精简了流程，Java层完全开源，可以自定义UI交互
效果图
---
![这里写图片描述](http://ww3.sinaimg.cn/mw690/90bd89ffgw1f7lerya42kg208i0g04qr.gif)

使用说明
----

引入Library工程：

```
  compile project(':videorecord')
```

在Application中初始化，主要指定拍摄路径：

```
        VCamera.setVideoCachePath( Environment.getExternalStorageDirectory().getAbsolutePath() + "/videoRecord/");
        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true);
        // 初始化拍摄SDK，必须
        VCamera.initialize(this);

```

添加权限：

```
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

```

配置拍摄和预览的Activity：

```
        <activity android:name="com.yixia.camera.demo.ui.record.MediaRecorderActivity"/>
        <activity android:name="com.yixia.camera.demo.ui.record.VideoPlayerActivity"/>

```
调用拍摄小视频：

```
 Intent intent = new Intent(this, MediaRecorderActivity.class);
 startActivityForResult(intent, 7001);
```

拿到拍摄视频的路径：

```
 @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 7001:
                    String path = data.getStringExtra("mPath");
                    mTextView.setText("视频路径： " + path);
                    break;
            }
        }
    }
```
