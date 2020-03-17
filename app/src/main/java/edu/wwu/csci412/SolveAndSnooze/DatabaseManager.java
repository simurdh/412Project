/**
 * Database Manager class
 */
package edu.wwu.csci412.SolveAndSnooze;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "alarmDB";
    private static final int DATABASE_VERSION = 7;

    private static final String ALARM_TABLE_NAME= "alarms";
    private static final String ID = "id";
    private static final String HOUR = "hour";
    private static final String MINUTES = "minutes";
    private static final String AM_PM = "am_pm";
    private static final String DAYS = "days";
    private static final String CHALLENGES = "challenges";
    private static final String ACTIVE = "active";
    private static final String IN_RANGE = "inRange";
    private static final String HAS_GF = "hasGeofence";
    private static final String MEM_PUZZLE = "memoryPuzzle";
    private static final String MATH_PUZZLE = "mathPuzzle";
    private static final String TILT_PUZZLE = "tilePuzzle";
    private static final String CHALLENGES_COMPLETED = "challengesCompleted";


    public DatabaseManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        String sqlCreate = "create table " + ALARM_TABLE_NAME + "(" + ID;
        sqlCreate += " integer primary key autoincrement, " + HOUR;
        sqlCreate += " integer, " + MINUTES + " integer, ";
        sqlCreate += AM_PM + " text, " + DAYS + " text, ";
        sqlCreate += CHALLENGES + " integer, " + ACTIVE + " text, ";
        sqlCreate += IN_RANGE + " text, ";
        sqlCreate += HAS_GF + " text, ";
        sqlCreate += MEM_PUZZLE + " text, ";
        sqlCreate += MATH_PUZZLE + " text, ";
        sqlCreate += TILT_PUZZLE + " text, ";
        sqlCreate += CHALLENGES_COMPLETED + " integer)";

        System.out.println(sqlCreate);
        db.execSQL(sqlCreate);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists " + ALARM_TABLE_NAME);
        onCreate(db);
    }

    /**
     * insert an alarm into database
     * @param newAlarm alarm info to insert
     * @return the id of newly inserted alarm
     */
    public Integer insert(AlarmData newAlarm){
        SQLiteDatabase db  = this.getReadableDatabase();
        String sqlInsert = "insert into " + ALARM_TABLE_NAME;
        sqlInsert += " values(null, " + newAlarm.getHour() + ", ";
        sqlInsert += newAlarm.getMinutes() + ", '" + newAlarm.getAM_PM();
        sqlInsert += "', '" + newAlarm.getDays() + "', '";
        sqlInsert += newAlarm.getChallenges() + "', ";
        sqlInsert += "'" + newAlarm.getActive() + "', ";
        sqlInsert += "'" + newAlarm.isInRange() + "', ";
        sqlInsert += "'" + newAlarm.getHasGf() + "', ";
        sqlInsert += "'" + newAlarm.getMemEnabled() + "', ";
        sqlInsert += "'" + newAlarm.getMathEnabled() + "', ";
        sqlInsert += "'" + newAlarm.getTiltEnabled() + "', ";
        sqlInsert += newAlarm.getChallengesCompleted() + ")";

        db.execSQL(sqlInsert);

        // return the id of inserted alarm
        String sqlQuery = "select * from " + ALARM_TABLE_NAME;
        Cursor cursor = db.rawQuery(sqlQuery, null);

        Integer id = 0;
        while (cursor.moveToNext()) {
            id = Integer.parseInt(cursor.getString(0));
        }

        db.close();

        return id;
    }

    public void deleteByID(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlDelete = "delete from " + ALARM_TABLE_NAME;
        sqlDelete += " where " + ID  + " = " + id;

        db.execSQL(sqlDelete);
        db.close();
    }

    public void updateById(int id, int hour, int minutes, String amPm, String days,
                           int challenges, String active, String inRange, String hasGf, String memPuzzle,
                           String mathPuzzle, String tiltPuzzle, int challengesCompleted){
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlUpdate = "update " + ALARM_TABLE_NAME;
        sqlUpdate += " set " + HOUR + " = " + hour +", ";
        sqlUpdate += MINUTES + " = " + minutes +", ";
        sqlUpdate += AM_PM + " = '" + amPm +"', ";
        sqlUpdate += DAYS + " = '" + days +"', ";
        sqlUpdate += CHALLENGES + " = '" + challenges +"', ";
        sqlUpdate += ACTIVE + " = '" + active + "', ";
        sqlUpdate += IN_RANGE + " = '" + inRange + "', ";
        sqlUpdate += HAS_GF + " = '" + hasGf + "', ";
        sqlUpdate += MEM_PUZZLE + "= '" + memPuzzle + "', ";
        sqlUpdate += MATH_PUZZLE + "= '" + mathPuzzle +"', ";
        sqlUpdate += TILT_PUZZLE + "= '" + tiltPuzzle +"', ";
        sqlUpdate += CHALLENGES_COMPLETED + "= '" + challengesCompleted + "' ";
        sqlUpdate += "where " + ID + " = " + id;

        db.execSQL(sqlUpdate);
        db.close();
    }


    public void updateInRangeById(int id, String inRange){
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlUpdate = "update " + ALARM_TABLE_NAME;
        sqlUpdate += " set " + IN_RANGE + " = '" + inRange + "' ";
        sqlUpdate += "where " + ID + " = " + id;

        db.execSQL(sqlUpdate);
        db.close();
    }

    public void updateHasGfById(int id, String hasGf){
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlUpdate = "update " + ALARM_TABLE_NAME;
        sqlUpdate += " set " + HAS_GF + " = '" + hasGf + "' ";
        sqlUpdate += "where " + ID + " = " + id;

        db.execSQL(sqlUpdate);
        db.close();
    }

    public AlarmData selectById(int id){
        String sqlQuery = "select * from " + ALARM_TABLE_NAME;
        sqlQuery += " where " + ID + " = " + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);
        AlarmData alarm = null;

        if(cursor.moveToFirst()){
            alarm = new AlarmData(Integer.parseInt(cursor.getString(0)),
                    Integer.parseInt(cursor.getString(1)),
                    Integer.parseInt(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getString(4),
                    Integer.parseInt(cursor.getString(5)),
                    Boolean.parseBoolean(cursor.getString(6)),
                    Boolean.parseBoolean(cursor.getString(7)),
                    Boolean.parseBoolean(cursor.getString(8)),
                    Boolean.parseBoolean(cursor.getString(9)),
                    Boolean.parseBoolean(cursor.getString(10)),
                    Boolean.parseBoolean(cursor.getString(11)),
                    Integer.parseInt(cursor.getString(12)));
        }

        db.close();

        return alarm;
    }

    public ArrayList<AlarmData> selectAll(){
        String sqlQuery = "select * from " + ALARM_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sqlQuery, null);

        ArrayList<AlarmData> alarms = new ArrayList<>();

        while(cursor.moveToNext()){
            AlarmData alarm = new AlarmData(Integer.parseInt(cursor.getString(0)),
                    Integer.parseInt(cursor.getString(1)),
                    Integer.parseInt(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getString(4),
                    Integer.parseInt(cursor.getString(5)),
                    Boolean.parseBoolean(cursor.getString(6)),
                    Boolean.parseBoolean(cursor.getString(7)),
                    Boolean.parseBoolean(cursor.getString(8)),
                    Boolean.parseBoolean(cursor.getString(9)),
                    Boolean.parseBoolean(cursor.getString(10)),
                    Boolean.parseBoolean(cursor.getString(11)),
                    Integer.parseInt(cursor.getString(12)));

            alarms.add(alarm);
        }

        db.close();

        return alarms;
    }
}