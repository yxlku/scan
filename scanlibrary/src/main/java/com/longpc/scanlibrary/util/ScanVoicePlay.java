package com.longpc.scanlibrary.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanVoicePlay {

    public static final String FILE_PATH = "sound/%s";

    private ExecutorService mExecutorService;
    private Context mContext;

    private ScanVoicePlay(Context context) {
        this.mContext = context;
        this.mExecutorService = Executors.newCachedThreadPool();
    }

    private volatile static ScanVoicePlay mVoicePlay = null;

    /**
     * 单例模式启动播放器
     *
     * @return
     */
    public static ScanVoicePlay with(Context context) {
        if (mVoicePlay == null) {
            synchronized (ScanVoicePlay.class) {
                if (mVoicePlay == null) {
                    mVoicePlay = new ScanVoicePlay(context);
                }
            }
        }
        return mVoicePlay;
    }

    /**
     * 接收自定义
     *
     * @param voicePlay
     */
    public void play(String voicePlay) {
        executeStart(voicePlay);
    }

    /**
     * 开启线程
     *
     * @param voicePlay
     */
    private void executeStart(final String voicePlay) {
        if (voicePlay == null || voicePlay.isEmpty()) {
            return;
        }

        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                start(voicePlay);
            }
        });
    }

    /**
     * 开始播报
     *
     * @param voicePlay
     */
    private void start(final String voicePlay) {
        synchronized (ScanVoicePlay.this) {

            MediaPlayer mMediaPlayer = new MediaPlayer();
            final CountDownLatch mCountDownLatch = new CountDownLatch(1);
            AssetFileDescriptor assetFileDescription = null;

            try {
                assetFileDescription = ScanFileUtil.getAssetFileDescription(mContext,
                        String.format(FILE_PATH, voicePlay));
                mMediaPlayer.setDataSource(
                        assetFileDescription.getFileDescriptor(),
                        assetFileDescription.getStartOffset(),
                        assetFileDescription.getLength());
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mCountDownLatch.countDown();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                mCountDownLatch.countDown();
            } finally {
                if (assetFileDescription != null) {
                    try {
                        assetFileDescription.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                mCountDownLatch.await();
                notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
