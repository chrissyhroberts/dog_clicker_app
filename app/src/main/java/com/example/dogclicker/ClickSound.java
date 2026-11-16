package com.example.dogclicker;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/** A short pre-trimmed click preloaded into a static AudioTrack. */
final class ClickSound {
    private static final Object LOCK = new Object();
    private static final AudioTrack[] tracks = new AudioTrack[3];

    private ClickSound() { }

    static void play(Context context) {
        playCount(context, 1);
    }

    static void playCount(Context context, int count) {
        int safeCount = Math.max(1, Math.min(3, count));
        synchronized (LOCK) {
            int index = safeCount - 1;
            if (tracks[index] == null) {
                int resourceId = index == 0 ? R.raw.click
                        : index == 1 ? R.raw.click_2 : R.raw.click_3;
                tracks[index] = buildTrack(context.getApplicationContext(), resourceId);
            }
            AudioTrack track = tracks[index];
            if (track.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                track.stop();
                track.reloadStaticData();
            }
            track.play();
        }
    }

    private static AudioTrack buildTrack(Context context, int resourceId) {
        byte[] pcm = readPcm(context, resourceId);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        AudioFormat format = new AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(44100)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .build();
        AudioTrack result = new AudioTrack.Builder()
                .setAudioAttributes(attributes)
                .setAudioFormat(format)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .setBufferSizeInBytes(pcm.length)
                .build();
        result.write(pcm, 0, pcm.length);
        result.setVolume(1.0f);
        return result;
    }

    private static byte[] readPcm(Context context, int resourceId) {
        try (InputStream input = context.getResources().openRawResource(resourceId);
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int count;
            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }
            return output.toByteArray();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load click sound", exception);
        }
    }
}
