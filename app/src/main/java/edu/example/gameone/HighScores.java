package edu.example.gameone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class HighScores extends AppCompatActivity {


    TextView[] Names = new TextView[5];
    TextView[] Scores = new TextView[5];
    TextView[] Dates = new TextView[5];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);


        Intent myIntent = getIntent();
        Long newHiScoreId  = myIntent.getLongExtra("NewHiScoreId", -1);

        Log.i("gameone0: ", "NewHiScoreId: " + newHiScoreId);


        DatabaseHelper dh = new DatabaseHelper( this );

        Names[0] = findViewById( R.id.tvName1);
        Names[1] = findViewById( R.id.tvName2);
        Names[2] = findViewById( R.id.tvName3);
        Names[3] = findViewById( R.id.tvName4);
        Names[4] = findViewById( R.id.tvName5);

        Dates[0] = findViewById( R.id.tvDate1);
        Dates[1] = findViewById( R.id.tvDate2);
        Dates[2] = findViewById( R.id.tvDate3);
        Dates[3] = findViewById( R.id.tvDate4);
        Dates[4] = findViewById( R.id.tvDate5);

        Scores[0] = findViewById( R.id.tvScore1);
        Scores[1] = findViewById( R.id.tvScore2);
        Scores[2] = findViewById( R.id.tvScore3);
        Scores[3] = findViewById( R.id.tvScore4);
        Scores[4] = findViewById( R.id.tvScore5);


        List<HiScore> top5HiScores =  dh.getTop5HiScores();
        //List<HiScore> top5HiScores =  dh.getAllHiScores();

        int count = 0;
        for (HiScore hiScore : top5HiScores) {

            Dates[ count ].setText( hiScore.getDateOfScore() );
            String date = hiScore.getDateOfScore();
            Log.i("gameone0: ", "score_date: " + date);

            //Dates[ count ].setText( "13dec2020"  );

            if( hiScore.getScoreId() == newHiScoreId )
            {
                Names[ count ].setText( "*" + hiScore.getNameOfPlayer() + "*" );

                Dates[ count ].setTextColor( getColor( R.color.Gold  ) );
                Names[ count ].setTextColor( getColor( R.color.Gold  ) );
                Scores[ count ].setTextColor( getColor( R.color.Gold  ) );

            }
            else
            {
                Names[ count ].setText( hiScore.getNameOfPlayer() );
            }



            Scores[ count ].setText( "" + hiScore.getScore() );

            count++;
            String log =
                    "Id: " + hiScore.getScoreId() +
                            ", Date: " + hiScore.getDateOfScore() +
                            " , Player: " + hiScore.getNameOfPlayer() +
                            " , Score: " + hiScore.getScore();

            // Writing HiScore to log
            Log.i("gameone0: ", "score: " + log);
        }
        Log.i("divider", "====================");


        //dh.emptyHiScores();

        //HiScore hiScore = top5HiScores.get(top5HiScores.size() - 1);
        // hiScore contains the 5th highest score
        //Log.i("gameone0", "Lowest High Score: " +  String.valueOf(hiScore.getScore()) );

/*
        HiScore hiScore = dh.getLowestHighscore();
        if( hiScore == null )
        {
            Log.i("gameone0", "Lowest High Score2: " + -1  );
        }
        else
        {
            Log.i("gameone0", "Lowest High Score2: " + hiScore.getScore() );
        }*/
    }


    public void doNewGame(View view)
    {
        Log.i("gameone0", "doNewGame: " + "" );

        //Intent intent = new Intent();
        //intent.putExtra("value", value0);
        //setResult(RESULT_OK, intent);

        Intent myIntent = new Intent(view.getContext(), MainActivity.class );

        startActivity( myIntent );     // start the new page
        finish();
    }
}