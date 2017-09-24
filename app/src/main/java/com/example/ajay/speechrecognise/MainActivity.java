package com.example.ajay.speechrecognise;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.RemoteConference;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText resultView;
    ImageView voiceButton;
    TextView scoresView;
    Handler handler;
    boolean goodToGo=true;
    Button stop,start;
    Runnable r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultView=(EditText) findViewById(R.id.resutlView);
        voiceButton=(ImageView)findViewById(R.id.voiceIcon);
        scoresView=(TextView)findViewById(R.id.confidenceScores);
        stop=(Button) findViewById(R.id.stop_rec);
        start=(Button) findViewById(R.id.continue_rec);

        handler=new Handler();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodToGo=true;
                voiceButton.performClick();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goodToGo=false;
                handler.removeCallbacks(r);
            }
        });


        voiceButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent speechIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak something...");
                speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);

                try{
                    startActivityForResult(speechIntent,143);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(MainActivity.this,"Sorry!!! your device is not supported..",Toast.LENGTH_LONG);
                }
            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {
            ArrayList<String> resultStrings=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);


            resultView.setText(resultView.getText()+" "+resultStrings.get(0));

            if(data.hasExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES)) {
                StringBuilder builder1=new StringBuilder();
                float[] confidences = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);

                for(float f:confidences)
                {
                    builder1.append(f);
                }

//                scoresView.setText(builder1.toString());
            }
            if(goodToGo)
            {
                listenAgain();
            }
        }
        else
        {
            listenAgain();
        }

    }
    private void listenAgain()
    {
        r=new Runnable() {
            @Override
            public void run() {
                        voiceButton.performClick();
            }
        };

        handler.postDelayed(r,1000);
    }
}
