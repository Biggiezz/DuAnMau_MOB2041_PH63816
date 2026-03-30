package com.example.DuAnMau_PH63816.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.ImageViewCompat;

import com.example.DuAnMau_PH63816.R;

public class CustomBottomButton extends LinearLayout {

    private final ImageView imgIcon;
    private final TextView txtLabel;
    private int iconResId;

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
        imgIcon = findViewById(R.id.imgIcon);
        txtLabel = findViewById(R.id.txtLabel);

        if (attrs == null) return;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomBottomButton);

        iconResId = a.getResourceId(R.styleable.CustomBottomButton_btnIcon, 0);
        if (iconResId != 0 && imgIcon != null) imgIcon.setImageResource(iconResId);

        a.recycle();
    }

    public void setLabel(CharSequence label) {
        if (txtLabel != null) {
            txtLabel.setText(label);
        }
    }

    public void setSelectedState(boolean selected) {
        int color = getContext().getColor(selected ? R.color.color_default : R.color.color_subtitle);
        if (imgIcon != null) {
            ImageViewCompat.setImageTintList(imgIcon, android.content.res.ColorStateList.valueOf(color));
        }
        if (txtLabel != null) {
            txtLabel.setTextColor(color);
        }
        setAlpha(selected ? 1f : 0.88f);
    }
}
