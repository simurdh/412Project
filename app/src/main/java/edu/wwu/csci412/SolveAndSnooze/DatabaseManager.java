package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarmDB";
    private static final int DATABASE_VERSION = 1;

    private static final String ALARM_TABLE_NAME= "alarms";
    private static final String ID = "id";
    private static final String HOUR = "hour";
    private static final String MINUTES = "minutes";
    private static final String AM_PM = "am_pm";
    private static final String DAYS = "days";
    private static final String CHALLENGES = "challenges";
    private static final String ACTIVE = "active";


    public DatabaseManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        String sqlCreate = "create table " + ALARM_TABLE_NAME + "(" + ID;
        sqlCreate += " integer primary key autoincrement, " + HOUR;
        sqlCreate += " integer, " + MINUTES + " integer, ";
        sqlCreate += AM_PM + " text, " + DAYS + " text, ";
        sqlCreate += CHALLENGES + " text, " + ACTIVE + "text)";
        db.execSQL(sqlCreate);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists " + ALARM_TABLE_NAME);
        onCreate(db);
    }

    public void insert(AlarmData newAlarm){
        SQLiteDatabase db  = this.getReadableDatabase();
        String sqlInsert = "insert into " + ALARM_TABLE_NAME;
        sqlInsert += " values(null, " + newAlarm.getHour() + ", ";
        sqlInsert += newAlarm.getMinutes() + ", '" + newAlarm.getAM_PM();
        sqlInsert += "', '" + newAlarm.getDays() + "', '";
        sqlInsert += newAlarm.getChallenges() + "', ";
        sqlInsert += "'" + newAlarm.getActive() + "')";

        db.execSQL(sqlInsert);
        db.close();
    }

    public void deleteByID(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlDelete = "delete from " + ALARM_TABLE_NAME;
        sqlDelete += " where " + ID  + " = " + id;

        db.execSQL(sqlDelete);
        db.close();
    }

    public void updateById(int id, int hour, int minutes, String amPm, String days,
                           String challenges, String active){
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlUpdate = "update " + ALARM_TABLE_NAME;
        sqlUpdate += " set " + HOUR + " = " + hour +", ";
        sqlUpdate += MINUTES + " = " + minutes +", ";
        sqlUpdate += AM_PM + " = '" + amPm +"', ";
        sqlUpdate += DAYS + " = '" + days +"', ";
        sqlUpdate += CHALLENGES + " = '" + challenges +"', ";
        sqlUpdate += ACTIVE + " = '" + active +"' ";
        sqlUpdate += "where " + ID + " = " + id;

        db.execSQL(sqlUpdate);
        db.close();
    }

    public AlarmData selectById(int id){
        String sqlQuery = "select * from " + ALARM_TABLE_NAME;
        sqlQuery += " where " + ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        //this is where we instantiate AlarmData, but constructor needs to be fixed

        db.close();

        return null;
    }

    public AlarmData selectALl(int id){
        String sqlQuery = "select * from " + ALARM_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        //this is where we instantiate AlarmDatas, but constructor needs to be fixed

        db.close();

        return null;
    }
}
