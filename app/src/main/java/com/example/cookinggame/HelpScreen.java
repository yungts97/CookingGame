package com.example.cookinggame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpScreen extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private ImageButton imageButton;
    private TextView textView;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen);
        imageButton = findViewById(R.id.btnClose);
        imageView = findViewById(R.id.imageHelp);
        textView = findViewById(R.id.txtNext);
        imageButton.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnClose)
        {
            finish();
        }
        else if(view.getId() == R.id.txtNext)
        {
            ++count;
            if(count == 1)
            {
                imageView.setImageResource(R.drawable.help2);
            }
            else if(count == 2) {
                imageView.setImageResource(R.drawable.help3);
                textView.setOnClickListener(null);
                textView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
