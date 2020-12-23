package edu.example.gameone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GameOver extends AppCompatActivity {

    int Level;
    int Score;
    Button BtnNext, BtnNew, BtnHiScores, BtnAdd;
    TextView TvMessage;
    EditText EtName;
    long NewHiScoreId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);


        Log.i("gameone0", "GameOver onCreate: " + "" );

        TvMessage = findViewById( R.id.tvMessage);
        BtnNext   = findViewById( R.id.buttonContinue);
        BtnNew    = findViewById( R.id.buttonNewGame);
        BtnHiScores = findViewById( R.id.buttonHighScores);
        BtnAdd = findViewById( R.id.buttonAdd);
        EtName = findViewById( R.id.editTextName);

        Intent myIntent = getIntent();
        Level = myIntent.getIntExtra("Level", 1);
        Score = myIntent.getIntExtra("Score", 1);
        int state = myIntent.getIntExtra("State", 1);


        // you progressed a level
        if( state == GameStat.SUCCESS )
        {
            Log.i("gameone0", "GameOver success: " + "" );
            Log.i("gameone0", "score: " + Score );
            // you progressed a level
            // message, continue
            // new main


            String s1 = "Level " + Level + " complete";
            s1       += "\nscore: " + Score;

            TvMessage.setText(  s1 );
            Level++;
            BtnNext.setText("Level " + Level );

            BtnNext.setVisibility(View.VISIBLE);

            BtnHiScores.setVisibility(View.INVISIBLE);
            BtnNew.setVisibility(View.INVISIBLE);

            EtName.setVisibility(View.INVISIBLE);
            BtnAdd.setVisibility(View.INVISIBLE);
        }

        else if( state == GameStat.FAILURE )
        {
            Log.i("gameone0", "GameOver failure: " + "" );
            // you lost


            int hiScore = getLowestHighscore(); // check lowest hiScore

            if( Score > hiScore )
            {
                Log.i("gameone0", "GameOver hi score: " + "" );
                // you got a new high score
                // enter name

                String s1 = "Level " + Level + " not complete";
                s1       += "\nscore: " + Score;
                s1       += "\nnew Hi Score !";
                TvMessage.setText(  s1 );

                BtnNext.setVisibility(View.INVISIBLE);

                BtnHiScores.setVisibility(View.INVISIBLE);
                BtnNew.setVisibility(View.INVISIBLE);

                EtName.setVisibility(View.VISIBLE);
                BtnAdd.setVisibility(View.VISIBLE);
            }
            else
            {
                String s1 = "Level " + Level + " not complete";
                s1       += "\nscore: " + Score;
                TvMessage.setText(  s1 );

                BtnNext.setVisibility(View.INVISIBLE);

                BtnHiScores.setVisibility(View.VISIBLE);
                BtnNew.setVisibility(View.VISIBLE);

                EtName.setVisibility(View.INVISIBLE);
                BtnAdd.setVisibility(View.INVISIBLE);

            }

            // new highscores
        }




    }

    public int getLowestHighscore()
    {

        DatabaseHelper dh = new DatabaseHelper( this );

        HiScore hiScore = dh.getLowestHighscore();
        if( hiScore != null )
        {
            Log.i("gameone0", "Lowest High Score2: " + hiScore.getScore() );
            return hiScore.getScore();
        }
        return 0;
    }

    public void doAddName(View view)
    {
        Log.i("gameone0", "doAddName: " + "" );

        String name = EtName.getText().toString();


        DatabaseHelper dh = new DatabaseHelper( this );
        // delete scores
        //dh.emptyHiScores();
        if( name.equals("delete"))
        {
            dh.emptyHiScores();
            doHighScores(view);
            return;
        }



        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String strDate = df.format(new Date());

        HiScore hiScore = new HiScore( name, Score, strDate);

        NewHiScoreId = dh.addHiScore( hiScore );

        Log.i("gameone0", "doAddName2: " + Score );

        doHighScores(view);
    }

    public void doNewGame(View view)
    {
        Log.i("gameone0", "doNewGame: " + "" );

        Intent myIntent = new Intent(view.getContext(), MainActivity.class );

        startActivity( myIntent );     // start the new page

        finish();
    }

    public void doContinue(View view)
    {
        Log.i("gameone0", "doContinue: " + "" );

        Intent myIntent = new Intent(view.getContext(), MainActivity.class );

        myIntent.putExtra("Level", Level );
        myIntent.putExtra("Score", Score );

        startActivity( myIntent );     // start the new page

        finish();
    }



    public void doHighScores(View view)
    {
        Log.i("gameone0", "doHighScores: " + "" );

        Intent myIntent = new Intent(view.getContext(), HighScores.class );

        if( NewHiScoreId >= 0 )
        {
            myIntent.putExtra("NewHiScoreId", NewHiScoreId );
        }

        startActivity( myIntent );     // start the new page
        finish();
    }
}