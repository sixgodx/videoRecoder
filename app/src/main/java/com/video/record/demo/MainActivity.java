package com.video.record.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yixia.camera.demo.ui.record.MediaRecorderActivity;
import com.yixia.camera.demo.ui.record.VideoPlayerActivity;

public class MainActivity extends AppCompatActivity implements MediaRecorderActivity.OnFinishRecordingListener {
    private ImageView mPreview;
    private ImageView mPlay;
    private TextView mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPreview = (ImageView) findViewById(R.id.preview);
        mPlay = (ImageView) findViewById(R.id.play);
        mPath = (TextView) findViewById(R.id.path);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goRecordVideo();
            }
        });
    }

    private void goRecordVideo() {
        MediaRecorderActivity.startActivity(this, this);
    }


    @Override
    public void onRecordFinished(final String path) {
        if (!TextUtils.isEmpty(path)) {
            mPlay.setVisibility(View.VISIBLE);
            mPreview.setImageBitmap(getVideoThumbnail(path));
            mPath.setText("路径： "+path);
            mPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //预览
                    Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
                    intent.putExtra("path", path);
                    intent.putExtra("preview", true);//仅预览
                    MainActivity.this.startActivity(intent);
                }
            });
        }
    }

    /**
     * 获取缩略图
     * @param filePath
     * @return
     */
    private Bitmap getVideoThumbnail(final String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime(2000);//
        } catch (Exception e) {
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }


}
