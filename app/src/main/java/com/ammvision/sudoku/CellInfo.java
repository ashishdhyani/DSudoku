package com.ammvision.sudoku;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ashiminty on 9/20/2014.
 */
public class CellInfo implements Parcelable {
    public int X;
    public int Y;
    public int CurrentValue = 0;
    public String Info;
    public boolean UserEntered = false;
    public boolean IsDisabled = false;
    public boolean IsError = false;
    public boolean IsSelected = false;
    public ColorStateList TextColor;

    public CellInfo() {}

    private CellInfo(Parcel in){
        Object[] data = null;
        data = in.readArray(CellInfo.class.getClassLoader());

        this.X = Integer.parseInt(data[0].toString());
        this.Y = Integer.parseInt(data[1].toString());
        this.CurrentValue = Integer.parseInt(data[2].toString());
        this.Info = (String)data[3];
        this.UserEntered = Boolean.parseBoolean(data[4].toString());
        this.IsDisabled = Boolean.parseBoolean(data[5].toString());
        this.IsError = Boolean.parseBoolean(data[6].toString());
        this.IsSelected = Boolean.parseBoolean(data[7].toString());
        this.TextColor = (ColorStateList)data[8];
    }

    public CellInfo Clone()
    {
        CellInfo info = new CellInfo();
        info.X = this.X;
        info.Y = this.Y;
        info.CurrentValue = this.CurrentValue;
        info.Info = this.Info;
        info.UserEntered = this.UserEntered;
        info.IsDisabled = this.IsDisabled;
        info.IsError = this.IsError;
        info.IsSelected = this.IsSelected;
        info.TextColor = this.TextColor;

        return info;
    }

    public static final Parcelable.Creator<CellInfo> CREATOR
            = new Parcelable.Creator<CellInfo>() {
        public CellInfo createFromParcel(Parcel in) {
            return new CellInfo(in);
        }

        public CellInfo[] newArray(int size) {
            return new CellInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeArray(new Object[]{ Integer.toString(X), Integer.toString(Y), Integer.toString(CurrentValue), "", Boolean.toString(UserEntered),
                Boolean.toString(IsDisabled), Boolean.toString(IsError), Boolean.toString(IsSelected), null});
    }
}
