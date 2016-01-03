package net.venedi.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.venedi.data.DBConfig;
import net.venedi.data.repository.table.CourseTable;
import net.venedi.data.repository.table.ExamTable;
import net.venedi.model.EStatusType;
import net.venedi.model.Exam;
import net.venedi.util.DateUtil;

abstract class BaseRepository extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public BaseRepository(Context context){

        super(context, DBConfig.DB_NAME,null, DBConfig.DB_VERSION);

        // Android will look for the database defined by DB_NAME
        // And if not found will invoke your onCreate method
        this.db = this.getWritableDatabase();
    }

    protected SQLiteDatabase getDb() {
        return db;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Android has created the database identified by DB_NAME
        // The new database is passed to you vai the db arg
        // Now it is up to you to create the Schema.
        // This schema creates a very simple user table, in order
        // Store user login credentials
        String examSql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER, %s TEXT, %s TEXT, %s INTEGER, %s DATETIME, %s TEXT, %s DATETIME DEFAULT CURRENT_TIMESTAMP)",
                ExamTable.NAME,
                ExamTable.COL_ID,
                ExamTable.COL_COURSE_ID,
                ExamTable.COL_DESCRIPTION,
                ExamTable.COL_NOTE,
                ExamTable.COL_TAKEN,
                ExamTable.COL_EXAM_DATE,
                ExamTable.COL_EXAM_RESULT,
                ExamTable.COL_CREATE_DATETIME
        );

        String courseSql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s DATETIME DEFAULT CURRENT_TIMESTAMP)",
                CourseTable.NAME,
                CourseTable.COL_ID,
                CourseTable.COL_TITLE,
                CourseTable.COL_DESCRIPTION,
                CourseTable.COL_CREATE_DATETIME
        );

        db.beginTransaction();
        try{
            db.execSQL(examSql);
            db.execSQL(courseSql);

            //seed simple date
            seedCourseData(db);
            seedExamData(db);

            db.setTransactionSuccessful();

        }finally {
            db.endTransaction();
        }
    }

    private void seedCourseData(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();
        cv.put(CourseTable.COL_TITLE, "Android UI");
        cv.put(CourseTable.COL_DESCRIPTION, "some demo for testing purpose...");
        cv.put(CourseTable.COL_CREATE_DATETIME, DateUtil.getCurrentDate());
        db.insert(CourseTable.NAME, null, cv);

        cv.put(CourseTable.COL_TITLE, "iOS Persistense");
        cv.put(CourseTable.COL_DESCRIPTION, "some demo for testing purpose...");
//        cv.put(CourseTable.COL_CREATE_DATETIME, DateUtil.getCurrentDate());
        db.insert(CourseTable.NAME, null, cv);

        cv.put(CourseTable.COL_TITLE, "Windows Phone Concepts");
        cv.put(CourseTable.COL_DESCRIPTION, "some demo for testing purpose...");
//        cv.put(CourseTable.COL_CREATE_DATETIME, DateUtil.getCurrentDate());
        db.insert(CourseTable.NAME,null,cv);
    }

    private void seedExamData(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();
        cv.put(ExamTable.COL_DESCRIPTION, "I Part of android");
        cv.put(ExamTable.COL_COURSE_ID, 1);
        //cv.put(ExamTable.COL_CREATE_DATETIME,DateUtil.getCurrentDate());
        cv.put(ExamTable.COL_EXAM_DATE, "2015-12-12 16:15:00"); //yyyy-MM-dd HH:mm:ss
        cv.put(ExamTable.COL_TAKEN, EStatusType.SUCCESS.getTypeId());
        cv.put(ExamTable.COL_EXAM_RESULT, "6");
        db.insert(ExamTable.NAME, null, cv);

        cv.put(ExamTable.COL_DESCRIPTION, "III Part iOS - Advanced");
        cv.put(ExamTable.COL_COURSE_ID, 2);
        cv.put(ExamTable.COL_EXAM_DATE, ""); //yyyy-MM-dd HH:mm:ss
        cv.put(ExamTable.COL_TAKEN, EStatusType.PENDING.getTypeId());
        cv.put(ExamTable.COL_EXAM_RESULT, "");
        //cv.put(ExamTable.COL_CREATE_DATETIME,DateUtil.getCurrentDate());
        db.insert(ExamTable.NAME, null, cv);

        cv.put(ExamTable.COL_DESCRIPTION, "Design Patterns in Action");
        cv.put(ExamTable.COL_COURSE_ID, 3);
        cv.put(ExamTable.COL_EXAM_DATE, "2016-11-07 11:45:00"); //yyyy-MM-dd HH:mm:ss
        cv.put(ExamTable.COL_TAKEN, EStatusType.FAIL.getTypeId());
        cv.put(ExamTable.COL_EXAM_RESULT, "");
        //cv.put(ExamTable.COL_CREATE_DATETIME,DateUtil.getCurrentDate());
        db.insert(ExamTable.NAME,null,cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Later when you change the DB_VERSION
        // This code will be invoked to bring your database
        // Upto the correct specification

    }

}
