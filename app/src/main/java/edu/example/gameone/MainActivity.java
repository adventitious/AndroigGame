package edu.example.gameone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static int MY_REQUEST_CODE = 1;

    Button BtnBlue, BtnRed, BtnYellow, BtnGreen, BtnPlay;

    private final int BLUE      = 1;
    private final int RED       = 2;
    private final int YELLOW    = 3;
    private final int GREEN     = 4;

    int sequenceCount = 4, n = 0;

    int[] GameSequence;
    int ArrayIndex = 0;

    View myView;

    long MillisInFuture = 10000;
    long Interval = 500;

    int Level = 1;
    int Score = 0;
    TextView TvLevel, TvScore;

    CountDownTimer ct = new CountDownTimer(MillisInFuture,  Interval) {

        public void onTick(long millisUntilFinished)
        {
            Log.i("gameone0", "onTick?: " + GameSequence[ ArrayIndex ]  );

            oneButton( GameSequence[ ArrayIndex ]);
            ArrayIndex++;

            if(ArrayIndex == GameSequence.length )
            {
                Log.i("gameone0", "onTick?: end early"   );
                // waitAWhile(800);
                cancel();
                onFinish();
            }
        }

        public void onFinish()
        {
            Log.i("gameone0", "finish!: "  );

            Intent myIntent = new Intent(myView.getContext(), PlayActivity.class );
            myIntent.putExtra("Level", Level);
            myIntent.putExtra("Score", Score);

            myIntent.putExtra("GameSequence", GameSequence);

            startActivity( myIntent );     // start the new page

            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //DatabaseHelper dh = new DatabaseHelper(this);
        // dh.addHiScore( new HiScore( 1,"jim", 16, "21 dec 2020"));
        //List<HiScore> hs = dh.getAllHiScores();
        //Log.i("gameone0", "score added: " + hs.get(0).getNameOfPlayer() );

        Log.i("gameone0", "score added: !!!" + "" );

        BtnPlay = findViewById( R.id.buttonPlay);
        BtnBlue = findViewById( R.id.buttonBlue);
        BtnRed = findViewById( R.id.buttonRed);
        BtnYellow = findViewById( R.id.buttonYellow);
        BtnGreen = findViewById( R.id.buttonGreen);

        TvLevel = findViewById( R.id.tvLevel);
        TvScore = findViewById( R.id.tvScore);

        Intent myIntent = getIntent();

        Log.i("gameone0", "score2 ?: !!!" + Score );

        Level = myIntent.getIntExtra("Level", 1);
        Score = myIntent.getIntExtra("Score", 0);

        int numberOfColors = 3 + ( Level * 2 );       // last one is empty
        //int numberOfColors = 4;// 2 + ( Level * 2 );

        GameSequence = new int[ numberOfColors ];

        Log.i("gameone0", "score1 ?: !!!" + Score );

        TvLevel.setText( "Level: " + Level);
        if( Score > 0 )
        {
            TvScore.setText( "Score: " + Score);
        }
        else
        {
            TvScore.setVisibility(View.INVISIBLE);
        }

        if( Level <= 1 )
        {
            TvLevel.setVisibility(View.INVISIBLE);
        }
    }


    public void doPlay(View view)
    {
        Log.i("gameone0", "doPlay: " + "" );


        MillisInFuture = Interval * GameSequence.length;

        for (int i = 0; i < GameSequence.length - 1; i++)
        {
            int x1 = i % 4;
            x1 = x1 + 1 ;
            Log.i("gameone0", "doPlay: x1 " + x1 );
            GameSequence[i] =  getRandom(4);
        }

        GameSequence[ GameSequence.length - 1 ] = 0; // last element empty, so there is a pause

        myView = view;
        ct.start();
        //doTest( );
        Log.i("gameone0", "doPlay2: " + "" );
        TvLevel.setVisibility( View.INVISIBLE );
        TvScore.setVisibility( View.INVISIBLE );
        BtnPlay.setVisibility( View.INVISIBLE );
    }




    private void oneButton( int buttonFlash ) {
        switch (buttonFlash) {
            case 1:
                flashButton(BtnBlue);
                break;
            case 2:
                flashButton(BtnRed);
                break;
            case 3:
                flashButton(BtnYellow);
                break;
            case 4:
                flashButton(BtnGreen);
                break;
            case 0:
                flashButton(null);
                break;
            default:
                break;
        }   // end switch
    }

    //
    // return a number between 1 and maxValue
    private int getRandom(int maxValue)
    {
        return ((int) ((Math.random() * maxValue) + 1));
    }

    private void flashButton(Button flashedButton)
    {
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {

                if( flashedButton != null )
                {

                    flashedButton.setPressed(true);
                    flashedButton.invalidate();
                    flashedButton.performClick();
                    Handler handler1 = new Handler();
                    Runnable r1 = new Runnable() {
                        public void run() {
                            flashedButton.setPressed(false);
                            flashedButton.invalidate();
                        }
                    };

                    handler1.postDelayed(r1, 100);

                }
            } // end runnable
        };
        handler.postDelayed(r, 100);
    }




}