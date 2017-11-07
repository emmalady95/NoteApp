package com.example.emmalady.note.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.emmalady.note.R;
import com.example.emmalady.note.adapter.ItemNoteAdapter;
import com.example.emmalady.note.db.DatabaseManager;
import com.example.emmalady.note.model.Notes;
import com.example.emmalady.note.utils.PhotoFormatUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_NOTE = "NOTE";
    public static final String KEY_POSITION = "POSITION";
    public static final String PATH_FOLDER = "/MyNote/";
    public static final String KEY_PHOTO_FOLDER = (Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES) + PATH_FOLDER);

    private ItemNoteAdapter mAdapter;
    private ActionBar mActionBar;
    //private AdapterView.OnItemClickListener itemNoteClickListener = new ItemNoteClickListener();

    private GridView mGridView;
    private TextView tvNoNotes;

    private List<Notes> notesList = new ArrayList<>();
    private int mPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvNoNotes = (TextView) findViewById(R.id.tvNoNotes);
        mGridView = (GridView) findViewById(R.id.gv_content);

        mAdapter = new ItemNoteAdapter(this, notesList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra(KEY_NOTE, (Notes) parent.getAdapter().getItem(position));
                i.putExtra(KEY_POSITION, position);
                startActivityForResult(i, 0);
            }
        });
        customActionBar();
        creatPictureFolder();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!(mPosition == 0 || getIntent() != null)){
            mPosition = getIntent().getIntExtra(KEY_POSITION, 0);
        }
        new LoadNote().execute(new Void[0]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        new LoadNote().execute(new Void[0]);
        super.onActivityResult(requestCode, resultCode, intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivityForResult(i, 0);
        }

        return super.onOptionsItemSelected(item);
    }
    //CREATE FOLDER TO SAVE PHOTO OF NOTE ON SMARTPHONE
    public void creatPictureFolder(){
        File file = new File(KEY_PHOTO_FOLDER);
        if(file.exists() == false){
            file.mkdirs();
        }
    }

    private void customActionBar() {
        this.mActionBar = getSupportActionBar();
        this.mActionBar.setDisplayOptions(16);
        this.mActionBar.setCustomView((int) R.layout.custom_actionbar);
        this.mActionBar.setDisplayShowHomeEnabled(true);
        this.mActionBar.setTitle(getString(R.string.app_name));
    }

    //ASYNC TASK LOAD NOTE
    public class LoadNote extends AsyncTask<Void, Void, List<Notes>> {
        public LoadNote(){

        }

        @Override
        protected List<Notes> doInBackground(Void... params) {
            return DatabaseManager.getInstance(MainActivity.this.getApplicationContext()).getAllData();
        }

        @Override
        protected void onPostExecute(List<Notes> note){
            super.onPostExecute(note);
            int size = note.size();

            if (size > 0){
                tvNoNotes.setVisibility(View.INVISIBLE);
            } else {
                tvNoNotes.setVisibility(View.VISIBLE);
            }
            notesList.clear();
            notesList.addAll(note);
            PhotoFormatUtils.tmpNote = note;

            if (mPosition == -1){
                Intent intent = new Intent(MainActivity.this.getApplicationContext(), DetailActivity.class);
                intent.putExtra(KEY_NOTE, (Notes) MainActivity.this.getIntent().getSerializableExtra(KEY_NOTE));
                intent.putExtra(KEY_POSITION, -1);
                mPosition = 0;
                startActivityForResult(intent, 0);
            }
        }
    }

}
