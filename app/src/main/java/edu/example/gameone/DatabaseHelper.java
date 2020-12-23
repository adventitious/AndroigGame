package edu.example.gameone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper  extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myGame";
    private static final String TABLE_HI_SCORES = "hi_scores";

    private static final String KEY_SCORE_ID = "score_id";
    private static final String KEY_NAME_OF_PLAYER = "NameOfPlayer";
    private static final String KEY_SCORE = "score";
    private static final String KEY_DATE_OF_GAME = "date_of_game";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HI_SCORES_TABLE = "CREATE TABLE " + TABLE_HI_SCORES + "("
                + KEY_SCORE_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME_OF_PLAYER + " TEXT NOT NULL,"
                + KEY_DATE_OF_GAME + " TEXT NOT NULL,"
                + KEY_SCORE + " INTEGER NOT NULL"
                + ")";

        db.execSQL(CREATE_HI_SCORES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HI_SCORES);

        // Create tables again
        onCreate(db);
    }

    // code to add the new HiScore
    long addHiScore(HiScore hiScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME_OF_PLAYER, hiScore.getNameOfPlayer());


        //Log.i("gameone0: ", "put date: " + hiScore.getDateOfScore());
        values.put(KEY_DATE_OF_GAME, hiScore.getDateOfScore());
        //Log.i("gameone0: ", "put score: " + hiScore.getScore());
        values.put(KEY_SCORE, hiScore.getScore());

        // Inserting Row
        long newRowId = db.insert(TABLE_HI_SCORES, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

        return  newRowId;
    }

    // code to get the single contact
    HiScore getHiScore(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HI_SCORES, new String[] {
                KEY_SCORE_ID,
                KEY_NAME_OF_PLAYER,
                KEY_DATE_OF_GAME,
                KEY_SCORE }, KEY_SCORE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        HiScore hiScore = new HiScore(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                Integer.parseInt( cursor.getString(2) ),
                cursor.getString(3));
        // return contact
        return hiScore;
    }



    public HiScore getLowestHighscore()
    {


        List<HiScore> hiScoreList = getTop5HiScores();

        Log.i("gameone0", "Lowest High Score size: " + hiScoreList.size() );

        if( hiScoreList.size() >= 1 && hiScoreList.size() >= 5 )
        {
            return hiScoreList.get(hiScoreList.size() - 1);
        }

        Log.i("gameone0", "Lowest High Score send null: " + hiScoreList.size() );
        return null;
    }

    // code to get all contacts in a list view
    public List<HiScore> getTop5HiScores() {
        List<HiScore> hiScoreList = new ArrayList<HiScore>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HI_SCORES +
                " ORDER BY SCORE DESC " +
                " LIMIT 5";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HiScore hiScore = new HiScore();

                hiScore.setScoreId( Integer.parseInt(cursor.getString(0)));
                hiScore.setNameOfPlayer(cursor.getString(1));

                Log.i("gameone0: ", "set score: " +  cursor.getString(2)      );
                hiScore.setScore(cursor.getInt(3));

                Log.i("gameone0: ", "set date: " +  cursor.getString(3)      );
                hiScore.setDateOfScore(cursor.getString(2));
                // Adding hiScore to list
                hiScoreList.add(hiScore);
            } while (cursor.moveToNext());
        }

        // return hiScore list
        return hiScoreList;
    }

    // code to update the single hiScore
    public int updateHiScore(HiScore hiScore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME_OF_PLAYER, hiScore.getNameOfPlayer());
        values.put(KEY_SCORE, hiScore.getScore());

        // updating row
        return db.update(TABLE_HI_SCORES, values, KEY_SCORE_ID + " = ?",
                new String[] { String.valueOf(hiScore.getScoreId()) });
    }

    // Deleting single hiScore
    public void deleteHiScore(HiScore hiScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HI_SCORES, KEY_SCORE_ID + " = ?",
                new String[] { String.valueOf(hiScore.getScoreId() ) });
        db.close();
    }

    // code to get the single contact
    HiScore getHiScoreWhereNameLike( String name )
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_HI_SCORES, new String[] { KEY_SCORE_ID,
                        KEY_NAME_OF_PLAYER, KEY_SCORE, KEY_DATE_OF_GAME }, KEY_NAME_OF_PLAYER + "=?",
                new String[] { String.valueOf( name ) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        HiScore hiScore = new HiScore(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                Integer.parseInt( cursor.getString(2) ),
                cursor.getString(3));

        // return contact
        return hiScore;
    }
    // Getting hiScores Count
    public int getHiScoresCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_HI_SCORES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
    public void emptyHiScores() {
        // Drop older table if existed
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HI_SCORES);

        // Create tables again
        onCreate(db);
    }

    // code to get all contacts in a list view
    public List<HiScore> getAllHiScores() {
        List<HiScore> hiScoreList = new ArrayList<HiScore>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_HI_SCORES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                HiScore hiScore = new HiScore();
                hiScore.setScoreId( Integer.parseInt(cursor.getString(0)));
                hiScore.setNameOfPlayer(cursor.getString(1));
                hiScore.setScore(cursor.getInt(2));
                hiScore.setDateOfScore(cursor.getString(3));
                // Adding hiScore to list
                hiScoreList.add(hiScore);
            } while (cursor.moveToNext());
        }

        // return hiScore list
        return hiScoreList;
    }
}