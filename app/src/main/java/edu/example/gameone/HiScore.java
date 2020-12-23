package edu.example.gameone;

public class HiScore {

    /*
- scoreID - primary key - int - autoincrementm- unique
- Name (TEXT)
- score (INT)
- Date (long - DATE format)*/


    int ScoreId;            // primary key
    String NameOfPlayer;    // example:  02 Dec 2020
    int Score;              // Joe
    String DateOfScore;     // score, should be even

    public HiScore(int scoreId, String nameOfPlayer, int score, String dateOfScore) {
        ScoreId = scoreId;
        NameOfPlayer = nameOfPlayer;
        Score = score;
        DateOfScore = dateOfScore;
    }

    public HiScore( String nameOfPlayer, int score, String dateOfScore) {
        NameOfPlayer = nameOfPlayer;
        Score = score;
        DateOfScore = dateOfScore;
    }

    public HiScore( )
    {
    }

    public int getScoreId() {
        return ScoreId;
    }

    public String getNameOfPlayer() {
        return NameOfPlayer;
    }

    public int getScore() {
        return Score;
    }

    public String getDateOfScore() {
        return DateOfScore;
    }

    public void setScoreId(int scoreId) {
        ScoreId = scoreId;
    }

    public void setNameOfPlayer(String nameOfPlayer) {
        NameOfPlayer = nameOfPlayer;
    }

    public void setScore(int score) {
        Score = score;
    }

    public void setDateOfScore(String dateOfScore) {
        DateOfScore = dateOfScore;
    }



}
