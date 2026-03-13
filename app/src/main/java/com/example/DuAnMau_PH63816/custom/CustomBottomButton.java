package com.example.DuAnMau_PH63816.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.DuAnMau_PH63816.R;

public class CustomBottomButton extends LinearLayout {

    public CustomBottomButton(Context context) {
        this(context, null);
    }

    public CustomBottomButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBottomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(LinearLayout.VERTICAL);
        setGravity(android.view.Gravity.CENTER);
        int p = (int) (8 * context.getResources().getDisplayMetrics().density);
        setPadding(p, p, p, p);

        LayoutInflater.from(context).inflate(R.layout.bottom_button_custom, this, true);

        if (attrs == null) return;

        ImageView imgIcon = findViewById(R.id.imgIcon);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomButton);

        int icon = a.getResourceId(R.styleable.CustomBottomButton_btnIcon, 0);
        if (icon != 0 && imgIcon != null) imgIcon.setImageResource(icon);

        a.recycle();
    }
}
