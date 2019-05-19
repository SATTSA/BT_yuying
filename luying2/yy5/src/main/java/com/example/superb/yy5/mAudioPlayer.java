package com.example.superb.yy5;


import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class mAudioPlayer {
    private  Context context;
    private PipedInputStream instream;
    private boolean isPlaying ;
    private AudioTrack audioplayer;
    private    AudioManager mAudioManager;

    private byte[] buffer;
    public mAudioPlayer(Context context) {
        isPlaying = false;
        instream = null;
        //初始化播音类

       this.context=context;
//        mAudioManager = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
//        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//        mAudioManager.setMode(AudioManager.MODE_RINGTONE);
     //   mAudioManager.setBluetoothA2dpOn(false);
    //  mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, true);
        //让声音路由到蓝牙A2DP。此方法虽已弃用，但就它比较直接、好用。
       // mAudioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_BLUETOOTH_A2DP, AudioManager.ROUTE_BLUETOOTH);

        mAudioManager.setBluetoothA2dpOn(true);
        int bufsize = AudioTrack.getMinBufferSize(11025, AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioplayer = new AudioTrack(AudioManager.STREAM_MUSIC, 11025, AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, bufsize,AudioTrack.MODE_STREAM);
    }
    //设置管道流，用于接受音频数据
    public void setOutputStream(PipedOutputStream out) throws IOException{
        instream = new PipedInputStream(out);

    }
    public void startPlayAudio(){ //调用之前先调用setOutputStream 函数
        isPlaying = true;
        audioplayer.play();//开始接受数据流播放
        buffer = new byte[1024];
     //   mAudioManager.stopBluetoothSco();
        while (instream!=null&&isPlaying){
            try {
                while (instream.available()>0){
                    int size = instream.read(buffer);
                    audioplayer.write(buffer, 0
                            , size);//不断播放数据
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void stopPlay(){//停止播放
        isPlaying = false ;
        mAudioManager.setStreamSolo(AudioManager.STREAM_MUSIC, false);
        mAudioManager.setMode(AudioManager.MODE_NORMAL);
mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        try {
            instream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioplayer.stop();
    }

}

