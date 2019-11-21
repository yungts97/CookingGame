package com.example.cookinggame;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog dialog;
    public Button btnYes;
    public TextView textView1, textView2;

    public CustomDialogClass(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new Dialog(c);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        textView1 = dialog.findViewById(R.id.txtScore);
        textView2 = dialog.findViewById(R.id.txtHighScore);
        btnYes = (Button) dialog.findViewById(R.id.buttonOk);
        btnYes.setOnClickListener(this);
        dialog.show();

        DatabaseAdapter db = new DatabaseAdapter(c);
        db.open();
        Cursor c = db.getHighScore();
        if(c.getCount() == 0)
        {
            db.insertHighScore(StartGameScreen.score);
            textView1.setText("Your Score: " + StartGameScreen.score);
            textView2.setText("High Score: " + StartGameScreen.score);
        }
        else
        {
            if(StartGameScreen.score > c.getInt(1))
            {
                db.updateHighScore(StartGameScreen.score);
                c = db.getHighScore();
            }
            textView1.setText("Your Score: " + StartGameScreen.score);
            textView2.setText("High Score: " + c.getInt(1));
        }
        db.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonOk:
                MainActivity.GameBGMplayer.Pause();
                MainActivity.GameBGMplayer.Stop();
                MainActivity.BGMplayer = new MediaPlayerHelper(MediaPlayer.create(getContext(),R.raw.bgm));
                MainActivity.BGMplayer.Play(true);
                dialog.dismiss();
                c.finish();
                break;
            default:
                break;
        }
    }
}
