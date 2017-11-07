package com.example.emmalady.note.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.emmalady.note.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Liz Nguyen on 06/11/2017.
 */

public class ItemPhotoPicassoAdapter extends ArrayAdapter<String>{
    private List<String> mPhotoList;
    private Context mContext;

    public ItemPhotoPicassoAdapter(Context context, List<String> photoList){
        super(context, R.layout.item_note_gridview, photoList);
        this.mContext = context;
        this.mPhotoList = photoList;
    }
    public static class ViewHolder{
        private ImageView imgView;
        private ImageButton ibRemove;

        public ViewHolder(){

        }
    }
    @Override
    public View getView(final int position, View itemView, ViewGroup parent) {
        ViewHolder viewItemPicture;
        if(itemView == null){
            viewItemPicture = new ViewHolder();

            itemView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.item_photo_gridview, parent, false);
            viewItemPicture.imgView = (ImageView) itemView.findViewById(R.id.ivShowPhotos);
            viewItemPicture.ibRemove = (ImageButton) itemView.findViewById(R.id.ibRemovePhotos);
            viewItemPicture.imgView.setLayoutParams(new RelativeLayout.LayoutParams(270, 270));
            itemView.setTag(viewItemPicture);
            Picasso
                    .with(mContext)
                    .load(mPhotoList.get(position))
                    .resize(270, 270)
                    .into(viewItemPicture.imgView);
        } else {
            viewItemPicture = (ViewHolder) itemView.getTag();
        }
        viewItemPicture.ibRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Confirm Delete";
                String msg = "Are you sure you want to delete this?";
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle(title);
                dialog.setMessage(msg);
                CharSequence string = mContext.getString(R.string.bt_ok);
                final int i = position;
                dialog.setPositiveButton(string, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mPhotoList.remove(i);
                        notifyDataSetChanged();
                    }
                });

                dialog.setNegativeButton(mContext.getString(R.string.bt_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        return itemView;
    }
}
