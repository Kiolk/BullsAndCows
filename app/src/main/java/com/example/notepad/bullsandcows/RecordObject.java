package com.example.notepad.bullsandcows;

public class RecordObject {

    private String numberOfCodedDigits;
    private String timeOfWin;
    private String nikName;
    private String numberOfMoves;
    private String needTimeForWin;

    public RecordObject(String pNumberOfCodedDigits, String pTimeOfWin, String pNikName, String pNumberOfMoves, String pNeedTimeForWin) {
        numberOfCodedDigits = pNumberOfCodedDigits;
        timeOfWin = pTimeOfWin;
        nikName = pNikName;
        numberOfMoves = pNumberOfMoves;
        needTimeForWin = pNeedTimeForWin;
    }

    public String getNumberOfCodedDigits() {
        return numberOfCodedDigits;
    }

    public void setNumberOfCodedDigits(String pNumberOfCodedDigits) {
        numberOfCodedDigits = pNumberOfCodedDigits;
    }

    public String getTimeOfWin() {
        return timeOfWin;
    }

    public void setTimeOfWin(String pTimeOfWin) {
        timeOfWin = pTimeOfWin;
    }

    public String getNikName() {
        return nikName;
    }

    public void setNikName(String pNikName) {
        nikName = pNikName;
    }

    public String getNumberOfMoves() {
        return numberOfMoves;
    }

    public void setNumberOfMoves(String pNumberOfMoves) {
        numberOfMoves = pNumberOfMoves;
    }

    public String getNeedTimeForWin() {
        return needTimeForWin;
    }

    public void setNeedTimeForWin(String pNeedTimeForWin) {
        needTimeForWin = pNeedTimeForWin;
    }
}
