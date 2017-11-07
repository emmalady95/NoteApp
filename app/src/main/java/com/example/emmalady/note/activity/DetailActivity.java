package com.example.emmalady.note.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.emmalady.note.R;
import com.example.emmalady.note.adapter.ItemPhotoPicassoAdapter;
import com.example.emmalady.note.db.DatabaseManager;
import com.example.emmalady.note.model.Notes;
import com.example.emmalady.note.utils.DateFormatUtils;
import com.example.emmalady.note.utils.PhotoFormatUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

public class DetailActivity extends BaseActivity {

    public static final String KEY_NOTE = "NOTE";
    public static final String KEY_POSITION = "POSITION";
    public static final String ACTION_SEND = "android.intent.action.SEND";
    public static final String EXTRA_TEXT = "android.intent.extra.TEXT";
    public static final String PATH_FOLDER = "/MyNote/";
    public static final String KEY_PHOTO_FOLDER = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + PATH_FOLDER);

    private ActionBar mActionBar;

    private ImageButton ibBack;
    private ImageButton ibNext;
    private ImageButton ibPrevious;

    private Notes mNote;
    private int mPosition = 0;
    public String enter = "\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            this.mNote = (Notes) getIntent().getExtras().getSerializable(KEY_NOTE);
            this.mPosition = getIntent().getExtras().getInt(KEY_POSITION);
        } else {
            this.mNote = (Notes) savedInstanceState.getSerializable(KEY_NOTE);
            this.mPosition = savedInstanceState.getInt(KEY_POSITION);
        }
        findViewByIDControl();

        this.mDateAlarmAdapapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, this.mDateAlarmList);
        this.mTimeAlarmAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, this.mTimeAlarmList);
        mDateAlarmAdapapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeAlarmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spDateAlarm.setAdapter(this.mDateAlarmAdapapter);
        this.spTimeAlarm.setAdapter(this.mTimeAlarmAdapter);
        convertPhoto(this.mNote.getmPhotos());
        this.itemPhotoPicassoAdapter = new ItemPhotoPicassoAdapter(this, this.mPhotoList);
        this.mGridViewPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File((String) mPhotoList.get(position))), GET_ALL_IMAGE);
                startActivity(intent);
            }
        });
        this.mGridViewPhoto.setAdapter(this.itemPhotoPicassoAdapter);
        updateNote(this.mNote);
        setEnableButton();
        customActionBar();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveNote();
        outState.putSerializable(KEY_NOTE, this.mNote);
        outState.putInt(KEY_POSITION, this.mPosition);
    }
    public void findViewByIDControl(){
        mScrollView = (ScrollView) findViewById(R.id.sv_content);
        mGridViewPhoto = (GridView) findViewById(R.id.gv_add_photos);
        mBackendAlarm = (LinearLayout) findViewById(R.id.ll_backend_alarm);
        mFrontendLayout = (LinearLayout) findViewById(R.id.ll_frontend_alarm);
        tvGetDateTime = (TextView) findViewById(R.id.tvGetDateTimeNow);
        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etNote);
        spDateAlarm = (Spinner) findViewById(R.id.spDateAlarm);
        spTimeAlarm = (Spinner) findViewById(R.id.spTimeAlarm);

        ibNext = (ImageButton) findViewById(R.id.ibNext);
        ibPrevious = (ImageButton) findViewById(R.id.ibbPrevious);
    }
    private void customActionBar() {
        this.mActionBar = getSupportActionBar();
        this.mActionBar.setDisplayOptions(16);
        this.mActionBar.setCustomView((int) R.layout.custom_back_actionbar);
        this.mActionBar.setDisplayShowHomeEnabled(true);
        this.mActionBar.setTitle(getString(R.string.app_name));
        View view = getSupportActionBar().getCustomView();
        ibBack = (ImageButton) view.findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
                DetailActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.mPosition != -1) {
            getMenuInflater().inflate(R.menu.menu_add, menu);
            getMenuInflater().inflate(R.menu.menu_main, menu);
            menu.findItem(R.id.action_add).setShowAsAction(0);
        }
        return true;
    }
    //ON BUTTON NAVIGATOR CLICK
    public void previousNote(View v){
        List list = PhotoFormatUtils.tmpNote;
        int i = mPosition - 1;
        mPosition = i;
        mNote = (Notes) list.get(i);
        updateNote(mNote);
    }
    public void shareNote(View v) {
        //AddNoteActivity.saveNoteFix(etTitle, etContent);
        //EXAMPLE ON STACKOVERFLOW
        /*String shareBody = "Here is the share content body";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));*/

        String actionShare = "Share with...";
        Intent i = new Intent(ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(EXTRA_TEXT, new StringBuilder(String.valueOf(etTitle.getText().toString().trim()))
                .append(enter).append(etContent.getText().toString().trim()).toString());
        startActivity(Intent.createChooser(i, actionShare));
    }
    public void deleteNote(View v) {
        String title = "Confirm Delete";
        String msg = "Are you sure you want to delete this?";
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);
        dialog.setMessage(msg);
        dialog.setPositiveButton(getString(R.string.bt_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!(PhotoFormatUtils.tmpNote == null || DetailActivity.this.mPosition == -1)) {
                    PhotoFormatUtils.tmpNote.remove(DetailActivity.this.mPosition);
                }
                DatabaseManager.getInstance(DetailActivity.this.getApplicationContext()).deteleData(DetailActivity.this.mNote.getmId());
                DetailActivity.this.finish();
            }
        });
        dialog.setNegativeButton(getString(R.string.bt_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void nextNote(View v){
        List list = PhotoFormatUtils.tmpNote;
        int i = mPosition + 1;
        mPosition = i;
        this.mNote = (Notes) list.get(i);
        updateNote(mNote);
    }

    public void updateNote(Notes note) {
        etTitle.setText(note.getmTitle());
        etContent.setText(note.getmContent());
        tvGetDateTime.setText(DateFormatUtils.DATE.format(note.getmDateTime()));
        if(note.getmAlarm() != null){
            mAlarm = (Date) note.getmAlarm().clone();
        }else{
            mAlarm = null;
        }
        mColor = note.getmColor();
        mScrollView.setBackgroundColor(Color.parseColor(mColor));
        convertPhoto(note.getmPhotos());
        itemPhotoPicassoAdapter.notifyDataSetChanged();
        if (mAlarm != null) {
            mFrontendLayout.setVisibility(View.INVISIBLE);
            mBackendAlarm.setVisibility(View.VISIBLE);
            mSpinnerDatePos= mDateAlarmList.size() - 1;
            mSpinnerTimePos = mTimeAlarmList.size() - 1;

            mDateAlarmList.set(mDateAlarmList.size() - 1, DateFormatUtils.DATE.format(mAlarm));
            mDateAlarmAdapapter.notifyDataSetChanged();
            spDateAlarm.setSelection(mDateAlarmList.size() - 1);
            spDateAlarm.setOnItemSelectedListener(this.onItemDateSelected);

            mTimeAlarmList.set(mTimeAlarmList.size() - 1, DateFormatUtils.DATE_HOUR.format(mAlarm));
            mTimeAlarmAdapter.notifyDataSetChanged();
            spTimeAlarm.setSelection(mTimeAlarmList.size() - 1);
            spTimeAlarm.setOnItemSelectedListener(this.onItemTimeSelected);

        } else {
            mFrontendLayout.setVisibility(View.VISIBLE);
            mBackendAlarm.setVisibility(View.INVISIBLE);
            spDateAlarm.setOnItemSelectedListener(null);
            spTimeAlarm.setOnItemSelectedListener(null);
        }
        setEnableButton();
    }

    public void setEnableButton() {
        int size = PhotoFormatUtils.tmpNote.size() - 1;
        if (mPosition == 0) {
            ibPrevious.setEnabled(false);
            ibPrevious.setAlpha(0.25f);
        } else {
            ibPrevious.setEnabled(true);
            ibPrevious.setAlpha(1.0f);
        }
        if (mPosition == -1) {
            ibPrevious.setEnabled(false);
            ibNext.setAlpha(0.25f);
            ibNext.setEnabled(false);
            ibNext.setAlpha(0.25f);
            return;
        }
        if (mPosition == size) {
            ibNext.setEnabled(false);
            ibNext.setAlpha(0.25f);
            return;
        }
        ibNext.setEnabled(true);
        ibNext.setAlpha(1.0f);
    }
    public boolean alarmChanged() {
        if (mAlarm != null) {
            if (mAlarm.equals(mNote.getmAlarm())) {
                return false;
            }
            return true;
        } else if ((mNote.getmAlarm().equals(mAlarm)) || (mNote.getmAlarm() == null)) {
            return false;
        } else {
            return true;
        }
    }
    public void saveNote() {
        String title = this.etTitle.getText().toString().trim();
        String content = this.etContent.getText().toString().trim();
        String pictures = TextUtils.join(",", this.mPhotoList);

        if ((!title.equals("") && (!this.mNote.getmTitle().equals(title) || !this.mNote.getmContent().equals(content)))
                || !this.mColor.equals(this.mNote.getmColor()) || alarmChanged() || !this.mNote.getmPhotos().equals(pictures)) {
            this.mNote.setmTitle(title);
            this.mNote.setmContent(content);
            this.mNote.setmDateTime(new Date());
            this.mNote.setmAlarm(this.mAlarm);
            this.mNote.setmPhotos(pictures);
            this.mNote.setmColor(this.mColor);

            if (this.mAlarm != null) {
                addAlarm(this.mNote);
            } else {
                removeAlarm(this.mNote);
            }
            DatabaseManager.getInstance(getApplicationContext()).updateData(this.mNote);
            if (this.mPosition != -1) {
                PhotoFormatUtils.tmpNote.set(this.mPosition, this.mNote);
            }
        }
    }

}
