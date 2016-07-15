package com.sahaware.resysbni.component;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by PCnya-AINI on 22/04/2016.
 */
public class StoTextView extends TextView {
    public StoTextView(Context context) {
        super(context);
    }

    public StoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public StoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StoTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("SourceSansPro-Regular.ttf", context);
        setTypeface(customFont);
    }
}
