package com.example.emmalady.note.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.emmalady.note.R;
import com.example.emmalady.note.adapter.ItemPhotoPicassoAdapter;
import com.example.emmalady.note.model.Notes;
import com.example.emmalady.note.model.AlarmReceiver;
import com.example.emmalady.note.utils.DateFormatUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Liz Nguyen on 06/11/2017.
 */

public class BaseActivity extends ActionBarActivity {

    public static final String PATH_FOLDER = "/MyNote/";
    public static final String KEY_PHOTO_FOLDER = (Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES) + PATH_FOLDER);

    public static final String KEY_NOTE = "NOTE";
    public static final String KEY_COLOR = "COLOR";
    public static final String KEY_PHOTO = "PHOTO";
    public static final String PHOTO_PATH = "IMG_";
    public static final String PHOTO_DOT_JPG = ".jpg";
    public static final String ACTION_CAPTURE = "android.media.action.IMAGE_CAPTURE";
    public static final String TAKE_PHOTO_OUTPUT = "TakePhoto";
    public static final String ACTION_PICK = "android.intent.action.PICK";
    public static final String ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT";
    public static final String GET_ALL_IMAGE = "image/*";
    public static final String ACTION_VIEW = "android.intent.action.VIEW";
    public static final String PATH_DATA = "_data";

    public AdapterView.OnItemSelectedListener onItemDateSelected = new OnItemDateSeleted();
    public AdapterView.OnItemSelectedListener onItemTimeSelected = new OnItemTimeSelected();
    public ItemPhotoPicassoAdapter itemPhotoPicassoAdapter;

    public AlarmManager mAlarmManager;
    public ScrollView mScrollView;
    public GridView mGridViewPhoto;
    public LinearLayout mBackendAlarm;
    public LinearLayout mFrontendLayout;
    private AlertDialog mInsertPhotoDialog;
    private AlertDialog mChooseColorDialog;

    public TextView tvGetDateTime;
    public EditText etTitle;
    public EditText etContent;
    public Spinner spDateAlarm;
    public Spinner spTimeAlarm;

    public Date mAlarm;
    public String mColor;

    public ArrayAdapter<String> mDateAlarmAdapapter;
    public ArrayAdapter<String> mTimeAlarmAdapter;
    public List<String> mDateAlarmList = new ArrayList<>();
    public List<String> mTimeAlarmList = new ArrayList<>();
    public ArrayList<String> mPhotoList;
    private Notes mNotes;

    public String mPathPhoto;
    public static int mSpinnerDatePos = 0;
    public static int mSpinnerTimePos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mColor = savedInstanceState.getString(KEY_COLOR);
            this.mPhotoList = savedInstanceState.getStringArrayList(KEY_PHOTO);
        }
        this.mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

//        String [] date = {"Today", "Tomorrow", "Next", "Other..."};
//        String [] time = {"09:00", "13:00", "17:00", "20:00", "Other..."};
        this.mDateAlarmList = Arrays.asList(getResources().getStringArray(R.array.alarm_date_array));
        this.mTimeAlarmList = Arrays.asList(getResources().getStringArray(R.array.alarm_time_array));
        this.mDateAlarmList.set(2, new StringBuilder(String.valueOf((String) this.mDateAlarmList.get(2)))
                .append(" ").append(DateFormatUtils.DAY_OF_WEEK.format(new Date())).toString());
        if (this.mPhotoList == null) {
            this.mPhotoList = new ArrayList();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_COLOR, this.mColor);
        outState.putStringArrayList(KEY_PHOTO, this.mPhotoList);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (resultCode != -1) {
            return;
        }

        //Catch Insert Photo: RequestCode = 1 -> Takes Photo, Request Code = 2 -> Choose Photo In Gallery
        if (requestCode == 1) {
            if (mPathPhoto != null) {
                updatePhotoList(mPathPhoto);
                mPathPhoto = null;
            }
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {

            try {
                Cursor cursor = getContentResolver().query(intent.getData(), new String[0], null, null, null);
                cursor.moveToFirst();
                String picturePath = cursor.getString(cursor.getColumnIndex(PATH_DATA));
                cursor.close();
                String absoluteName = picturePath.split("/")[picturePath.split("/").length - 1];
                tmpCopy(new File(picturePath), new File(PATH_FOLDER + absoluteName));
                updatePhotoList(absoluteName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_camera:
                showChoosePhotoDialog();
                break;
            case R.id.action_module:
                showChooseColorDialog();
                break;
            case R.id.action_done:
                finish();
                break;
        }
        saveNote();
        //finish();
        return true;
        //return super.onOptionsItemSelected(item);
    }

    public void saveNote() {

    }

    private void showChoosePhotoDialog() {
        this.mInsertPhotoDialog = new AlertDialog.Builder(this).create();
        this.mInsertPhotoDialog.setView(((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.dialog_insert_photo, null));
        this.mInsertPhotoDialog.setTitle(getString(R.string.action_choose_photo));
        this.mInsertPhotoDialog.show();
    }

    private void showChooseColorDialog() {
        this.mChooseColorDialog = new AlertDialog.Builder(this).create();
        this.mChooseColorDialog.setView(((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.dialog_choose_color, null));
        this.mChooseColorDialog.setTitle(getString(R.string.action_choose_color));
        this.mChooseColorDialog.show();
    }
    //ON CLICK -------------------------------------------------------------------------
    //This
    public void disabledAlarm(View v){
        mFrontendLayout.setVisibility(View.VISIBLE);
        mBackendAlarm.setVisibility(View.INVISIBLE);
        mAlarm = null;
        spDateAlarm.setOnItemSelectedListener(null);
        spTimeAlarm.setOnItemSelectedListener(null);
    }
    public void enabledAlarm(View v){
        this.mFrontendLayout.setVisibility(View.INVISIBLE);
        this.mBackendAlarm.setVisibility(View.VISIBLE);
        if (mAlarm == null) {
            this.mAlarm = new Date();

            //Default The First element In Time Array is 09:00
            this.mAlarm.setHours(9);
            this.mAlarm.setMinutes(0);
            this.mAlarm.setSeconds(0);
        }
        spDateAlarm.setOnItemSelectedListener(this.onItemDateSelected);
        spTimeAlarm.setOnItemSelectedListener(this.onItemTimeSelected);
    }

    //Dialog Insert Photo
    public void takePhotos(View v){
        mInsertPhotoDialog.dismiss();
        try {
            File image = File.createTempFile(new StringBuilder(PHOTO_PATH)
                    .append(DateFormatUtils.DATE_PICTURE.format(new Date()))
                    .append("_").toString(), PHOTO_DOT_JPG, new File(KEY_PHOTO_FOLDER));
            mPathPhoto = image.getAbsolutePath();
            Intent takePhotoIntent = new Intent(ACTION_CAPTURE);
            takePhotoIntent.putExtra(TAKE_PHOTO_OUTPUT, Uri.fromFile(image));

            startActivityForResult(takePhotoIntent, 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void choosePhotos(View v){
        mInsertPhotoDialog.dismiss();
        Intent pickPhotoIntent = new Intent(ACTION_PICK);
        pickPhotoIntent.setType(GET_ALL_IMAGE);
        pickPhotoIntent.setAction(ACTION_GET_CONTENT);
        startActivityForResult(pickPhotoIntent, 2);
    }
    //Dialog Choose Color
    public void getColor(View v){
        mChooseColorDialog.dismiss();
        mColor = ((Button) v).getText().toString();
        mScrollView.setBackgroundColor(Color.parseColor(this.mColor));
    }
    //-------------------------------------------------------------------------------------

    public void convertPhoto(String photo) {
        mPhotoList.clear();
        if (mPhotoList.size() == 0 && photo != null && !"".equals(photo)) {
            String[] pics = photo.split(",");
            for (Object add : pics) {
                mPhotoList.add((String) add);
            }
        }
    }
    //--------------------------------------------------------------------------


    //FILE AND PHOTO -----------------------------------------------------------
    public void tmpCopy(File src, File dst) throws IOException {
        InputStream inputStream = new FileInputStream(src);
        OutputStream outputStream = new FileOutputStream(dst);
        byte[] buffer = new byte[1024];
        while (true) {

            int line = inputStream.read(buffer);
            if (line <= 0) {
                inputStream.close();
                outputStream.close();
                return;
            }
            outputStream.write(buffer, 0, line);
        }
    }

    public void updatePhotoList(String pathPhoto) {
        mPhotoList.add(pathPhoto);
        itemPhotoPicassoAdapter.notifyDataSetChanged();
    }
    //--------------------------------------------------------------------------

    //ALARM ---------------------------------------------------------------------
    public void addAlarm(Notes note){
        Long time = Long.valueOf(note.getmAlarm().getTime());
        if (time.longValue() > System.currentTimeMillis()) {
            Intent intentAlarm = new Intent(getApplicationContext(), AlarmReceiver.class);
            intentAlarm.putExtra(KEY_NOTE, note);
            this.mAlarmManager.set(1, time.longValue(),
                    PendingIntent.getBroadcast(this, (int) note.getmId(), intentAlarm, 0));
        }
    }

    public void removeAlarm(Notes note) {
        Intent intent= new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra(KEY_NOTE, note);
        this.mAlarmManager.cancel(PendingIntent.getBroadcast(this, (int) note.getmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
        this.mAlarmManager.cancel(PendingIntent.getBroadcast(this, (int) note.getmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }
    //-------------------------------------------------------------------------------------------

    //IMPLEMENTS LISTENER AND SELECTED --------------------------------------------------------------

    public class OnItemDateSeleted implements AdapterView.OnItemSelectedListener{
        public OnItemDateSeleted(){

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Calendar calendar;
            String other = "Other...";
            switch (position) {
                case 0:
                    mAlarm.setDate(new Date().getDate());
                    mDateAlarmList.set(mDateAlarmList.size() - 1, other);
                    mDateAlarmAdapapter.notifyDataSetChanged();
                    mSpinnerDatePos = position;
                    return;
                case 1:
                    calendar = Calendar.getInstance();
                    if (calendar.get(calendar.DATE) + 1 <= calendar.getActualMaximum(calendar.DATE)) {
                        mAlarm.setDate(calendar.get(calendar.DATE) + 1);
                    } else {
                        mAlarm.setDate((calendar.get(calendar.DATE) + 1) - calendar.get(calendar.DATE));
                        mAlarm.setMonth(calendar.get(calendar.MONTH) + 2);
                    }
                    mDateAlarmList.set(mDateAlarmList.size() - 1, other);
                    mDateAlarmAdapapter.notifyDataSetChanged();
                    mSpinnerDatePos = position;
                    return;
                case 2:
                    calendar = Calendar.getInstance();
                    if (calendar.get(calendar.DATE) + 7 <= calendar.getActualMaximum(calendar.DATE)) {
                        mAlarm.setDate(calendar.get(calendar.DATE) + 7);
                    } else {
                        mAlarm.setDate((calendar.get(calendar.DATE) + 7) - calendar.get(calendar.DATE));
                        mAlarm.setMonth(calendar.get(calendar.MONTH) + 2);
                    }
                    mDateAlarmList.set(mDateAlarmList.size() - 1, other);
                    mDateAlarmAdapapter.notifyDataSetChanged();
                    mSpinnerDatePos = position;
                    return;
                case 3:
                    if (position != mSpinnerDatePos) {
                        showDateDialog();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    public void showDateDialog(){
        final Date date = mAlarm;
        String chooseDate = "Choose Date";
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mAlarm.setYear(year - 1900);
                mAlarm.setMonth(month);
                mAlarm.setDate(dayOfMonth);
                mDateAlarmList.set(mDateAlarmList.size() - 1, DateFormatUtils.DATE.format(mAlarm));
                mDateAlarmAdapapter.notifyDataSetChanged();
            }
        }, calendar.get(calendar.YEAR), calendar.get(calendar.MONTH), calendar.get(calendar.DATE));
        datePickerDialog.setTitle(chooseDate);
        datePickerDialog.setCancelable(true);
        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                mAlarm = date;
                spDateAlarm.setSelection(mSpinnerDatePos);
            }
        });
        datePickerDialog.show();
    }

    public class OnItemTimeSelected implements AdapterView.OnItemSelectedListener{
        private String other = "Other...";
        public OnItemTimeSelected(){

        }
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                case 1:
                case 2:
                case 3:
                    try {
                        Date date = DateFormatUtils.DATE_HOUR.parse(spTimeAlarm.getItemAtPosition(position).toString());
                        mAlarm.setSeconds(0);
                        mAlarm.setMinutes(date.getMinutes());
                        mAlarm.setHours(date.getHours());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    mTimeAlarmList.set(mTimeAlarmList.size() - 1, other);
                    mTimeAlarmAdapter.notifyDataSetChanged();
                    mSpinnerTimePos = position;
                    return;
                case 4:
                    if (position != mSpinnerTimePos) {
                        showTimeDialog();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    public void showTimeDialog() {
        final Date cacheDate = mAlarm;
        String chooseTime = "Choose Time";
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mAlarm.setHours(hourOfDay);
                mAlarm.setMinutes(minute);
                mAlarm.setSeconds(0);
                mTimeAlarmList.set(mTimeAlarmList.size() - 1, DateFormatUtils.HOUR.format(mAlarm));
                mTimeAlarmAdapter.notifyDataSetChanged();
            }
        }, calendar.get(calendar.HOUR_OF_DAY) + 1, 0, true);
        timePickerDialog.setTitle(chooseTime);
        timePickerDialog.setCancelable(true);
        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                mAlarm = cacheDate;
                mTimeAlarmList.set(mTimeAlarmList.size() - 1, "Other...");
                spTimeAlarm.setSelection(mSpinnerTimePos);
            }
        });
        timePickerDialog.show();
    }

}
