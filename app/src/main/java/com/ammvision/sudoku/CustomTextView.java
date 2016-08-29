package com.ammvision.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ashiminty on 9/16/2014.
 */
public class CustomTextView extends TextView
{
    public int X = -1;
    public int Y = -1;
    public boolean IsDisabled = false;
    public int CurrentValue = 0;
    public boolean UserEntered = false;
    public boolean IsError = false;
    public boolean IsSelected = false;
    public boolean adjusted = false;

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);
        _shrinkToFit();
    }

    protected void _shrinkToFit() {

        int height = this.getHeight();
        int lines = this.getLineCount();
        Rect r = new Rect();
        int y1 = this.getLineBounds(0, r);
        int y2 = this.getLineBounds(lines-1, r);

        if(!adjusted) {
            float size = this.getTextSize();

            this.setTextSize(size + 2.0f);
            adjusted = true;
        }

        //if (y2 > height && size >= 8.0f) {
        //    this.setTextSize(size - 2.0f);
        //    _shrinkToFit();
        //}

    }
}
