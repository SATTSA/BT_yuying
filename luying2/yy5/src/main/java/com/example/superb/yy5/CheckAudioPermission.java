package com.example.superb.yy5;


import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

/**
 * Function:判断录音权限,兼容android6.0以下以及以上系统
 *
 * @author xuzhuyun
 * @date 2018/5/10
 */

public class CheckAudioPermission {
    /**
     * 音频获取源
     */
    public static int audioSource = MediaRecorder.AudioSource.MIC;
    /**
     * 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
     */
    public static int sampleRateInHz = 44100;

    /**
     * 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
     */
    public static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    /**
     * 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
     */
    public static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    /**
     * 缓冲区字节大小
     */
    public static int bufferSizeInBytes = 0;

    public static AudioRecord audioRecord;


    /**
     * 判断是是否有录音权限.
     */
    public static boolean isHasPermission(final Context context) {
        bufferSizeInBytes = 0;
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, audioFormat);
        if (audioRecord == null) {
            audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);

        }
        //开始录制音频
        try {
            // 防止某些手机崩溃，例如联想
            audioRecord.startRecording();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        /**
         * 根据开始录音判断是否有录音权限s
         */
        if (audioRecord.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING) {
            return false;
        }
        audioRecord.stop();
        //释放资源
        audioRecord.release();
        audioRecord = null;

        return true;
    }

}

