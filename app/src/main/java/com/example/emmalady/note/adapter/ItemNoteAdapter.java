package com.example.emmalady.note.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.emmalady.note.R;
import com.example.emmalady.note.activity.MainActivity;
import com.example.emmalady.note.model.Notes;
import com.example.emmalady.note.utils.DateFormatUtils;

import java.util.List;

/**
 * Created by Liz Nguyen on 03/11/2017.
 */

@SuppressLint({"layout_inflater"})
public class ItemNoteAdapter extends ArrayAdapter<Notes>{
    //private Drawable mIcAlarm = this.mContext.getResources().getDrawable(R.drawable.ic_alarm_gray_24dp);
    //private Drawable mIcAlarm = resize(this.mContext.getResources().getDrawable(R.drawable.ic_alarm_gray_24dp));
    private Context mContext;
    private List<Notes> mNotesList;
    private String enter = "\n";
    private String tab = "\t";

    //Resize Photos which show on gridview in MainActicity
    private Drawable resizeImage(Drawable image) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(Bitmap.createBitmap(
                Bitmap.createScaledBitmap(((BitmapDrawable) image).getBitmap(), dpToPx(24), dpToPx(24), true)));
        return bitmapDrawable;
    }

    public ItemNoteAdapter(Context context, List<Notes> note){
        super(context, R.layout.item_note_gridview, note);
        this.mContext = context;
        this.mNotesList = note;

    }

    /*https://stackoverflow.com/questions/2265661/how-to-use-arrayadaptermyclass*/

    //ViewHolder
    public static class ViewHolder{
        private LinearLayout llItemNote;
        private TextView tvNoteTitle;
        private TextView tvNoteContent;
        private TextView tvNoteDateTime;

        public ViewHolder(){

        }

        public ViewHolder(LinearLayout llItemNote, TextView tvNoteTitle, TextView tvNoteContent, TextView tvNoteDateTime){
            this.llItemNote = llItemNote;
            this.tvNoteTitle = tvNoteTitle;
            this.tvNoteContent = tvNoteContent;
            this.tvNoteDateTime = tvNoteDateTime;
        }
    }


    //getView
    public View getView(int position, View itemView, ViewGroup parent){
        ViewHolder viewItemNote;
        if (itemView == null){
            viewItemNote = new ViewHolder();

            //On create view holder
            itemView = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.item_note_gridview, parent, false);
            //On bind view holder
            viewItemNote.llItemNote = (LinearLayout) itemView.findViewById(R.id.llItemNote);
            viewItemNote.tvNoteTitle = (TextView) itemView.findViewById(R.id.tvNoteTitle);
            viewItemNote.tvNoteContent = (TextView) itemView.findViewById(R.id.tvNoteContent);
            viewItemNote.tvNoteDateTime = (TextView) itemView.findViewById(R.id.tvNoteDateTime);

            itemView.setTag(viewItemNote);
        } else{
            viewItemNote = (ViewHolder) itemView.getTag();
        }
        Notes note = (Notes) mNotesList.get(position);
        viewItemNote.tvNoteTitle.setText(note.getmTitle().replace(enter, tab));
        viewItemNote.tvNoteContent.setText(note.getmContent());
        viewItemNote.tvNoteDateTime.setText(DateFormatUtils.DATE_SHORT.format(note.getmDateTime()));
        viewItemNote.llItemNote.setBackgroundColor(Color.parseColor(note.getmColor()));
        if (note.getmAlarm() != null) {
            viewItemNote.tvNoteTitle.setCompoundDrawablesWithIntrinsicBounds(null, null,null , null);
        } else {
            viewItemNote.tvNoteTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        return itemView;
    }

    //Convert dp to Pixel
    public int dpToPx(int dp) {
        return Math.round(((float) dp) * (getContext().getResources().getDisplayMetrics().xdpi / 160.0f));
    }
}
