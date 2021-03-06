package com.example.zhangyang.ijktest;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.widget.media.AndroidMediaController;
import tv.danmaku.ijk.media.widget.media.IjkVideoView;

public class MainActivity extends AppCompatActivity {
    IjkVideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.fullscreen);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOrientation(getResources().getConfiguration().orientation);
            }
        });
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        videoView = (IjkVideoView) findViewById(R.id.ijkPlayer);
        AndroidMediaController controller = new AndroidMediaController(this, false);
        videoView.setMediaController(controller);
        if (BaseVideoService.getMediaPlayer() != null) {
            videoView.setMediaPlayer((IjkMediaPlayer) BaseVideoService.getMediaPlayer());
        } else {
            String url = TVChannel.tv;
            videoView.setVideoURI(Uri.parse(url));
        }
        videoView.start();
    }

    private void setOrientation(int orientation) {
        if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  videoView.pause();
    }

    @Override
    protected void onDestroy() {
        BaseVideoService.setMediaPlayer(videoView.getMediaPlayer());
        BaseVideoService.intentToStart(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.resume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            videoView.suspend();
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
