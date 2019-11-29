package com.example.cookinggame;

import android.media.MediaPlayer;

import java.util.Timer;
import java.util.TimerTask;

public class MediaPlayerHelper {
    private MediaPlayer _player;
    private float speed = 0.05f;
    private float volume = 0;

    public MediaPlayerHelper(MediaPlayer player)
    {
        this._player = player;
    }

    public void Play(boolean setLoop)
    {
        if(setLoop) {
            this._player.setLooping(true);
        }
        else
        {
            _player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    stopPlayer();
                }
            });
        }
        _player.start();

    }

    public void Stop()
    {
        FadeOut();
        //_player.stop();
    }
    public void Pause()
    {
        this._player.pause();
    }

    private void FadeOut(){

        // The duration of the fade
        final int FADE_DURATION = 2000;

        // The amount of time between volume changes. The smaller this is, the smoother the fade
        final int FADE_INTERVAL = 166;
        volume = 1;
        // Calculate the number of fade steps
        int numberOfSteps = FADE_DURATION / FADE_INTERVAL;

        // Calculate by how much the volume changes each step
        final float deltaVolume = volume / numberOfSteps;

        // Create a new Timer and Timer task to run the fading outside the main UI thread
        final Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                //Do a fade step
                fadeOutStep(deltaVolume);

                //Cancel and Purge the Timer if the desired volume has been reached
                if(volume <= 0){
                    timer.cancel();
                    timer.purge();
                    stopPlayer();
                }
            }
        };

        timer.schedule(timerTask,FADE_INTERVAL,FADE_INTERVAL);
    }

    private void fadeOutStep(float deltaVolume){
                       _player.setVolume(volume, volume);
        volume -= deltaVolume;
    }
    private void stopPlayer() {

        if (_player != null) {

            _player.release();
            _player = null;
        }
    }

}
