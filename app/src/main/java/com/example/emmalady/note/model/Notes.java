package com.example.emmalady.note.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Liz Nguyen on 01/11/2017.
 */

public class Notes implements Serializable {
    /*public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String CONTENT = "CONTENT";
    public static final String DATE_TIME = "DATETIME";
    public static final String ALARM = "ALARM";
    public static final String PHOTO = "PHOTO";
    public static final String COLOR = "COLOR";*/

    private int mId;
    private String mTitle;
    private String mContent;
    private Date mDateTime;
    private Date mAlarm;
    private String mPhotos;
    private String mColor;

    public Notes(){
        super();
    }

    /*public Notes(Parcel in) {
        this.mId = in.readInt();
        this.mTitle = in.readString();
        this.mContent = in.readString();

        long tmpDateTime = in.readLong();
        this.mDateTime = tmpDateTime == -1 ? null : new Date(tmpDateTime);
        long tmpAlarm= in.readLong();
        this.mAlarm = tmpAlarm == -1 ? null : new Date(tmpAlarm);

        String tmpPhotos = in.readString();
        this.mPhotos = (tmpPhotos == null) || ("".equals(tmpPhotos)) ? "" : tmpPhotos;

        String tmpColor = in.readString();
        this.mColor = tmpColor != null ? tmpColor : "0xFFFFFF";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mTitle);
        dest.writeString(this.mContent);
        dest.writeLong(mDateTime != null ? mDateTime.getTime() : -1);
        dest.writeLong(mAlarm != null ? mAlarm.getTime() : -1);
        dest.writeString(this.mPhotos);
        dest.writeString(this.mColor);
    }

    public static final Parcelable.Creator<Notes> CREATOR = new Parcelable.Creator<Notes>() {
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };*/

    public Notes(int id, String title, String content, Date dateTime, Date alarm, String photos, String color) {
        this.mId = id;
        this.mTitle = title;
        this.mContent = content;
        this.mDateTime = dateTime;
        this.mAlarm = alarm;

//        if (photos == null || "".equals(photos)) {
//            photos = "";
//        }
        this.mPhotos = (("".equals(photos)) || (photos == null)) ? photos = "" : photos;
        //this.mPhotos = photos;

//        if (color == null) {
//            color = "0xffffff";
//        }
        //this.mColor = color;
        this.mColor = (color == null ? "0xffffff" : color);
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public Date getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(Date mDateTime) {
        this.mDateTime = mDateTime;
    }

    public Date getmAlarm() {
        return mAlarm;
    }

    public void setmAlarm(Date mAlarm) {
        this.mAlarm = mAlarm;
    }

    public String getmPhotos() {
        return mPhotos;
    }

    public void setmPhotos(String mPicture) {
        this.mPhotos = mPicture;
    }

    public String getmColor() {
        return mColor;
    }

    public void setmColor(String mColor) {
        this.mColor = mColor;
    }
}
