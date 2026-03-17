package com.example.DuAnMau_PH63816.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.DuAnMau_PH63816.R;


public class CustomMenuRow extends LinearLayout {

    public CustomMenuRow(Context context) {
        this(context, null);
    }

    public CustomMenuRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomMenuRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(android.view.Gravity.CENTER_VERTICAL);
        int p = (int) (12 * context.getResources().getDisplayMetrics().density);
        setPadding(p, p, p, p);

        LayoutInflater.from(context).inflate(R.layout.item_menu_row, this, true);

        if (attrs == null) return;

        ImageView imgIcon = findViewById(R.id.imgMenuIcon);
        FrameLayout frameIcon = findViewById(R.id.frameIcon);
        TextView tvTitle = findViewById(R.id.tvMenuTitle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomMenuRow);

        String title = a.getString(R.styleable.CustomMenuRow_menuTitle);
        if (title != null && tvTitle != null) tvTitle.setText(title);

        int icon = a.getResourceId(R.styleable.CustomMenuRow_menuIcon, 0);
        if (icon != 0 && imgIcon != null) imgIcon.setImageResource(icon);

        if (frameIcon != null) {
            GradientDrawable bg = new GradientDrawable();
            bg.setCornerRadius(20f);
            frameIcon.setBackground(bg);
        }

        a.recycle();
    }
}


