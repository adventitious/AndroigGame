package edu.example.gameone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PlayActivity extends AppCompatActivity implements SensorEventListener {

    int Level;
    int Score;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    float NewTilt   = (float)2;
    float ResetTilt = (float)1;
    boolean PressedSouth = false;
    boolean PressedNorth = false;
    boolean PressedWest = false;
    boolean PressedEast = false;

    Button BtnBlue, BtnRed, BtnYellow, BtnGreen;

    int[] GameSequence;
    int ArrayIndex = 0;

    int TiltMode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent myIntent = getIntent();
        Level = myIntent.getIntExtra("Level", 1);
        Score = myIntent.getIntExtra("Score", 1);

        //textView = findViewById(R.id.textView);
        //ArrayList<String> numbersList = (ArrayList<String>) getIntent().getSerializableExtra("key");
        //textView.setText(String.valueOf(numbersList));

        GameSequence = (int[])getIntent().getSerializableExtra("GameSequence");

        Log.i("gameone0", "seq1" +GameSequence[0] );
        Log.i("gameone0", "seq0" +GameSequence[1] );

        Log.i("gameone0", "new Sensor" );
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // FlashedButton = findViewById( R.id.buttonGreen);
        BtnBlue = findViewById( R.id.buttonBlue);
        BtnRed = findViewById( R.id.buttonRed);
        BtnYellow = findViewById( R.id.buttonYellow);
        BtnGreen = findViewById( R.id.buttonGreen);
    }

    protected void onResume() {
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause()
    {
        super.onPause();
        mSensorManager.unregisterListener(this);    // turn off listener to save power
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        Log.i("gameone0", "accuracy changed" );
    }



    private void flashButton(Button button)
    {
        Button flashedButton = button;
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {

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

            } // end runnable
        };
        handler.postDelayed(r, 100);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // called byt the system every x ms
        float x, y, z;

        x = event.values[0];    // get x value from sensor
        y = event.values[1];
        z = event.values[2];

        if( TiltMode == 0 )
        {
            TiltMode = setTiltMode(  x, z );
        }

        int tilt;
        if( TiltMode == 1 )
        {
            tilt = checkTiltFlat( x, y );
        }
        else
        {
            tilt = checkTiltStanding( y, z );
        }

        if( tilt == GameStat.SOUTH )
        {
            Log.i("gameone0", "onSensorChanged: " + "south pressed" );
            flashButton( BtnYellow );
        }
        if( tilt == GameStat.NORTH )
        {
            Log.i("gameone0", "onSensorChanged: " + "north pressed" );
            flashButton( BtnRed );
        }
        if( tilt == GameStat.EAST )
        {
            Log.i("gameone0", "onSensorChanged: " + "east pressed" );
            flashButton( BtnGreen);
        }
        if( tilt == GameStat.WEST )
        {
            Log.i("gameone0", "onSensorChanged: " + "west pressed" );
            flashButton( BtnBlue );
        }



        //Log.i("gameone0", "onSensorChanged:  x:  " + x + "       y:  " + y + "        z:  " + z  );
    }

    void enterMove( int tilt )
    {
        if( tilt == GameSequence[ ArrayIndex ] )
        {
            Log.i("gameone0", "enterMove: " + "good " + GameSequence[ ArrayIndex ] );

            ArrayIndex++;
            Score++;

            if( ArrayIndex >= ( GameSequence.length - 1) ) // last element is empty
            {
                Log.i("gameone0", "enterMove: " + "win " );
                //ArrayIndex--;
                doGameWin();
            }
        }
        else
        {
            Log.i("gameone0", "enterMove: " + "failure" );
            doGameLose( );
        }
    }


    public int setTiltMode( float x, float z )
    {
        if( ( x > 4.8 ) && ( z < 4.8  ) )
        {
            return 2;
        }

        return 1;
    }


    public int checkTiltFlat( float x, float y )
    {
        //Log.i("gameone0", "checkTiltFlat: " + "  " );
        if( ( PressedSouth == false ) && ( x > NewTilt ) )
        {
            //Log.i("gameone0", "onSensorChanged: " + "south pressed" );
            PressedSouth = true;
            return GameStat.SOUTH;
        }
        else if( x < ResetTilt )
        {
            PressedSouth = false;
        }

        if( ( PressedNorth == false ) && ( x < -NewTilt ) )
        {
            //Log.i("gameone0", "onSensorChanged: " + "north pressed" );
            PressedNorth = true;
            return GameStat.NORTH;
        }
        else if( x > -ResetTilt )
        {
            PressedNorth = false;
        }


        if( ( PressedEast == false ) && ( y > NewTilt ) )
        {
            //Log.i("gameone0", "onSensorChanged: " + "east pressed" );
            PressedEast = true;
            return GameStat.EAST;
        }
        else if( y < ResetTilt )
        {
            PressedEast = false;
        }

        if( ( PressedWest == false ) && ( y < -NewTilt ) )
        {
            //Log.i("gameone0", "onSensorChanged: " + "west pressed" );
            PressedWest = true;
            return GameStat.WEST;
        }
        else if( y > -ResetTilt )
        {
            PressedWest = false;
        }
        return 0;
    }




    public int checkTiltStanding( float y, float z )
    {
        //Log.i("gameone0", "checkTiltStanding: " + "  " );
        if( ( PressedNorth == false ) && ( z > NewTilt ) )
        {
            //Log.i("gameone0", "onSensorChanged: " + "south pressed" );
            PressedNorth = true;
            return GameStat.NORTH;
        }
        else if( z < ResetTilt )
        {
            PressedNorth = false;
        }

        if( ( PressedSouth == false ) && ( z < -NewTilt ) )
        {
            //Log.i("gameone0", "onSensorChanged: " + "north pressed" );
            PressedSouth = true;
            return GameStat.SOUTH;
        }
        else if( z > -ResetTilt )
        {
            PressedSouth = false;
        }


        if( ( PressedEast == false ) && ( y > NewTilt ) )
        {
            //Log.i("gameone0", "onSensorChanged: " + "east pressed" );
            PressedEast = true;
            return GameStat.EAST;
        }
        else if( y < ResetTilt )
        {
            PressedEast = false;
        }

        if( ( PressedWest == false ) && ( y < -NewTilt ) )
        {
            //Log.i("gameone0", "onSensorChanged: " + "west pressed" );
            PressedWest = true;
            return GameStat.WEST;
        }
        else if( y > -ResetTilt )
        {
            PressedWest = false;
        }
        return 0;
    }


    public void doGameWin()
    {
        Log.i("gameone0", "doGameWin: " + "" );

        Intent myIntent = new Intent(this, GameOver.class );

        myIntent.putExtra("Level", Level );
        myIntent.putExtra("Score", Score );
        myIntent.putExtra("State", GameStat.SUCCESS );

        startActivity( myIntent );     // start the new page

        finish();
    }


    public void doWest(View view)
    {
        Log.i("gameone0", "doWest: " + "" );
        enterMove( 1 );
    }
    public void doNorth(View view)
    {
        Log.i("gameone0", "doNorth: " + "" );
        enterMove( 2 );
    }
    public void doSouth(View view)
    {
        Log.i("gameone0", "doSouth: " + "" );
        enterMove( 3 );
    }
    public void doEast(View view)
    {
        Log.i("gameone0", "doEast: " + "" );
        enterMove( 4 );
    }



    public void doGameLose( )
    {
        Log.i("gameone0", "doGameLose: " + "" );

        Intent myIntent = new Intent(this, GameOver.class );

        myIntent.putExtra("Level", Level );
        myIntent.putExtra("Score", Score );
        myIntent.putExtra("State", GameStat.FAILURE );

        startActivity( myIntent );     // start the new page
        finish();
    }
}