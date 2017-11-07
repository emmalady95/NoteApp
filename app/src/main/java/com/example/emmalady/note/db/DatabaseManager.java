package com.example.emmalady.note.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.emmalady.note.model.Notes;
import com.example.emmalady.note.utils.DateFormatUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liz Nguyen on 03/11/2017.
 */

public class DatabaseManager {
    public static final String DATABASE_NAME = "Notes.db";
    public static final String TABLE_NAME = "Note";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_DATE_TIME = "datetime";
    public static final String COLUMN_ALARM = "alarm";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_COLOR = "color";

    public static DatabaseManager instance;
    public static int mIdLastNote = 0;

    private SQLiteDatabase db;
    public List<Notes> notesList = new ArrayList<>();

    public DatabaseManager(Context context){
        db = new DataHandler(context).getWritableDatabase();
    }

    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    public boolean insertData(Notes note){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, Integer.valueOf(note.getmId()));
        contentValues.put(COLUMN_TITLE, note.getmTitle());
        contentValues.put(COLUMN_CONTENT, note.getmContent());

        contentValues.put(COLUMN_DATE_TIME, DateFormatUtils.DATE_LONG.format(note.getmDateTime()));
        if (note.getmAlarm() != null) {
            contentValues.put(COLUMN_ALARM, DateFormatUtils.DATE_LONG.format(note.getmAlarm()));
        } else {
            contentValues.putNull(COLUMN_ALARM);
        }
        contentValues.put(COLUMN_PHOTO, note.getmPhotos());

        //Add color in db but replace color with # and 0x
        contentValues.put(COLUMN_COLOR, note.getmColor().replace("#", "0x"));
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateData(Notes note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, Integer.valueOf(note.getmId()));
        contentValues.put(COLUMN_TITLE, note.getmTitle());
        contentValues.put(COLUMN_CONTENT, note.getmContent());

        contentValues.put(COLUMN_DATE_TIME, DateFormatUtils.DATE_LONG.format(note.getmDateTime()));
        if (note.getmAlarm() != null) {
            contentValues.put(COLUMN_ALARM, DateFormatUtils.DATE_LONG.format(note.getmAlarm()));
        } else {
            contentValues.putNull(DataHandler.COLUMN_ALARM);
        }

        contentValues.put(COLUMN_PHOTO, note.getmPhotos());
        contentValues.put(COLUMN_COLOR, note.getmColor());
        db.update(TABLE_NAME, contentValues, "id =" + note.getmId(), null);
        return true;
    }

    public void deteleData(int id) {
        db.delete(DataHandler.TABLE_NAME, "id =" + id, null);
    }

    public List<Notes> getAllData() {
        List<Notes> notes = new ArrayList<>();
        String query = "SELECT * FROM " + DataHandler.TABLE_NAME;
        Cursor res = db.rawQuery(query, null);
        res.moveToFirst();

        if(res != null) {
            while (!res.isAfterLast()) {
                Notes newNotes = parseData(res);
                if (newNotes != null) {
                    notes.add(newNotes);
                    if (newNotes.getmId() > mIdLastNote) {
                        mIdLastNote = newNotes.getmId();
                    }
                }
                res.moveToNext();
            }
        }
        return notes;
    }

    public Notes parseData(Cursor res){
        int id = res.getInt(res.getColumnIndex(DataHandler.COLUMN_ID));
        String title = res.getString(res.getColumnIndex(DataHandler.COLUMN_TITLE));
        String content = res.getString(res.getColumnIndex(DataHandler.COLUMN_CONTENT));

        String dateTime = res.getString(res.getColumnIndex(DataHandler.COLUMN_DATE_TIME));
        String alarm = res.getString(res.getColumnIndex(DataHandler.COLUMN_ALARM));
        String photo = res.getString(res.getColumnIndex(DataHandler.COLUMN_PHOTO));
        String color = res.getString(res.getColumnIndex(DataHandler.COLUMN_COLOR)).replace("0x", "#");

        try {
            Notes newNotes = new Notes(id, title, content, DateFormatUtils.DATE_LONG.parse(dateTime),
                    alarm != null ? DateFormatUtils.DATE_LONG.parse(alarm) : null,color, photo );
            return newNotes;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
