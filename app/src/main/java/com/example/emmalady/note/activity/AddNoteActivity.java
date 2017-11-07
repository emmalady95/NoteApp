package com.example.emmalady.note.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.File;
import java.util.Date;

public class AddNoteActivity extends BaseActivity {
    public ItemPhotoPicassoAdapter mItemPhotoAdapter;
    public ActionBar mActionBar;
    public ImageButton ibBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewByIDControl();
        mDateAlarmAdapapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mDateAlarmList);
        //int i = mDateAlarmAdapapter.getCount();

        mTimeAlarmAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mTimeAlarmList);
        //int j = mTimeAlarmAdapter.getCount();
//        //this.mTitleET.addTextChangedListener(this.textChangedListenter);
        mDateAlarmAdapapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeAlarmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spDateAlarm.setAdapter(mDateAlarmAdapapter);
        spTimeAlarm.setAdapter(mTimeAlarmAdapter);

        mItemPhotoAdapter = new ItemPhotoPicassoAdapter(AddNoteActivity.this, mPhotoList);
        itemPhotoPicassoAdapter = new ItemPhotoPicassoAdapter(AddNoteActivity.this, mPhotoList);
        mGridViewPhoto.setAdapter(itemPhotoPicassoAdapter);
        mGridViewPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File((String) mPhotoList.get(position))), GET_ALL_IMAGE);
                startActivity(intent);
            }
        });

        mColor = mColor != null ? mColor : "#FFFFFF";
        mScrollView.setBackgroundColor(Color.parseColor(this.mColor));
        tvGetDateTime.setText(DateFormatUtils.DATE_SHORT.format(new Date()));
        customActionBar();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
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
                AddNoteActivity.this.finish();
            }
        });
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
    }
    public void saveNote(){
        String title = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (!title.equals("")) {
            Notes note = new Notes(DatabaseManager.mIdLastNote + 1, title, content, new Date(),
                    this.mAlarm != null ? this.mAlarm : null, this.mColor, TextUtils.join(",", this.mPhotoList));
            if (this.mAlarm != null) {
                addAlarm(note);
            }
            DatabaseManager.getInstance(getApplicationContext()).insertData(note);
        }
    }

}
