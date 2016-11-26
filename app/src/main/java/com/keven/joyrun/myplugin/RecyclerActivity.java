package com.keven.joyrun.myplugin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.keven.joyrun.myplugin.provider.ScreenshotContentObserver;
import com.keven.joyrun.myplugin.services.BackgroundAudioService;
import com.keven.joyrun.myplugin.services.WorkingService;

import java.util.ArrayList;

/**
 * Created by keven on 16/10/17.
 */

public class RecyclerActivity extends AppCompatActivity {

    private static final int STATE_PAUSED = 0;
    private static final int STATE_PLAYING = 1;

    RecyclerView mRecyclerView;

    private int mCurrentState;
    private MediaBrowserCompat mMediaBrowserCompat;
    private MediaControllerCompat mMediaControllerCompat;
    private Button mPlayPauseToggleButton;


    private MediaControllerCompat.Callback mMediaControllerCompatCallback = new MediaControllerCompat.Callback() {

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            if( state == null ) {
                return;
            }

            switch( state.getState() ) {
                case PlaybackStateCompat.STATE_PLAYING: {
                    mCurrentState = STATE_PLAYING;
                    break;
                }
                case PlaybackStateCompat.STATE_PAUSED: {
                    mCurrentState = STATE_PAUSED;
                    break;
                }
            }
        }

        @Override
        public void onExtrasChanged(Bundle extras) {
            super.onExtrasChanged(extras);
        }
    };


    private MediaBrowserCompat.ConnectionCallback mMediaBrowserCompatConnectionCallback = new MediaBrowserCompat.ConnectionCallback() {

        @Override
        public void onConnected() {
            super.onConnected();
            try {
                mMediaControllerCompat = new MediaControllerCompat(RecyclerActivity.this, mMediaBrowserCompat.getSessionToken());
                mMediaControllerCompat.registerCallback(mMediaControllerCompatCallback);
                setSupportMediaController(mMediaControllerCompat);
                getSupportMediaController().getTransportControls().playFromMediaId(String.valueOf(R.raw.m_finish_relax_muscle), null);

            } catch( RemoteException e ) {

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("keven1119","RecyclerActivity  onCreate");
//        Window win = getWindow();
//        win.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
//        win.setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.activity_recycler);
//        startService(new Intent(this, WorkingService.class));
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_itemdecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        String num = "";
        int temp = 0;
        final ArrayList<String> numberDatas = new ArrayList<>();
        for (int i = 0; i<260; i++){
            numberDatas.add(num);
            if(i%10 == 0){
                temp = temp + 1;
            }
            num = temp + "";
        }

        RecyclerView.Adapter<RecyclerView.ViewHolder> viewHolderAdapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(new TextView(RecyclerActivity.this)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((TextView)(holder.itemView)).setText(numberDatas.get(position));
            }

            @Override
            public int getItemCount() {
                return numberDatas.size();
            }
        };

        TitleItemDecoration titleItemDecoration = new TitleItemDecoration(this, numberDatas);
        mRecyclerView.addItemDecoration(titleItemDecoration);
        mRecyclerView.setAdapter(viewHolderAdapter);


        mPlayPauseToggleButton = (Button) findViewById(R.id.button);

        mMediaBrowserCompat = new MediaBrowserCompat(this, new ComponentName(this, BackgroundAudioService.class),
                mMediaBrowserCompatConnectionCallback, getIntent().getExtras());

        mMediaBrowserCompat.connect();

        mPlayPauseToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( mCurrentState == STATE_PAUSED ) {
                    getSupportMediaController().getTransportControls().play();
                    mCurrentState = STATE_PLAYING;
                } else {
                    PlaybackStateCompat playbackState = getSupportMediaController().getPlaybackState();
                    int state = playbackState.getState();
                    if( state == PlaybackStateCompat.STATE_PLAYING ) {
                        getSupportMediaController().getTransportControls().pause();
                    }

                    mCurrentState = STATE_PAUSED;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("keven1119","RecyclerActivity  onResume");
        ScreenshotContentObserver.startObserve();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScreenshotContentObserver.stopObserve();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenshotContentObserver.stopObserve();

        super.onDestroy();
        if( getSupportMediaController().getPlaybackState().getState() == PlaybackStateCompat.STATE_PLAYING ) {
            getSupportMediaController().getTransportControls().pause();
        }

        mMediaBrowserCompat.disconnect();
    }
}
