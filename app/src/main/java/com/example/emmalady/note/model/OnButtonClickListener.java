package com.example.emmalady.note.model;

import android.content.DialogInterface;
import android.view.View;

/**
 * Created by Liz Nguyen on 04/11/2017.
 */

public class OnButtonClickListener implements DialogInterface.OnClickListener {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
    private int mPosition;
    public OnButtonClickListener(int position){
        this.mPosition = position;
    }
    public interface setOnClick{
        public void onClick(View v);
    }
}
