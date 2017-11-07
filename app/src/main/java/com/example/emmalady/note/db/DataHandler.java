package com.example.emmalady.note.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Liz Nguyen on 01/11/2017.
 */

public class DataHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Notes2.db";
    public static final String TABLE_NAME = "Note";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_DATE_TIME = "datetime";
    public static final String COLUMN_ALARM = "alarm";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_COLOR = "color";

    public static final String CREATE_DB = "Create table " + TABLE_NAME + " ( "
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_CONTENT + " TEXT, "
            + COLUMN_DATE_TIME + " DATETIME, "
            + COLUMN_ALARM + " DATETIME, "
            + COLUMN_PHOTO + " TEXT, "
            + COLUMN_COLOR + " TEXT)";

    public DataHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //db.execSQL(CREATE_DB);
        onCreate(db);
    }
}
