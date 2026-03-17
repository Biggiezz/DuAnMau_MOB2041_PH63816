package com.example.DuAnMau_PH63816.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextUtils;

import androidx.cardview.widget.CardView;
import androidx.annotation.Nullable;

import com.example.DuAnMau_PH63816.R;
import com.squareup.picasso.Picasso;

public class CustomCardView extends CardView {
    private final ImageView imgIcon;
    private final ImageView imgEdit;
    private final TextView txtTitle;
    private final TextView txtSubTitle;
    private final TextView txtSubTitle2;

    public CustomCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.card_custom, this, true);

        imgIcon = findViewById(R.id.imgIcon);
        imgEdit = findViewById(R.id.imgEdit);
        txtTitle = findViewById(R.id.txtTitle);
        txtSubTitle = findViewById(R.id.txtSubtitle);
        txtSubTitle2 = findViewById(R.id.txtSubtitle2);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomCardView);

        int titleStyle = array.getResourceId(R.styleable.CustomCardView_cardTitleStyle, 0);
        if (titleStyle != 0) {
            txtTitle.setTextAppearance(titleStyle);
            txtTitle.setTypeface(android.graphics.Typeface.DEFAULT); // reset hoàn toàn về normal
        } else {
            txtTitle.setTypeface(android.graphics.Typeface.DEFAULT_BOLD); // mặc định bold
        }
        int icon = array.getResourceId(R.styleable.CustomCardView_cardIcon, 0);
        String title = array.getString(R.styleable.CustomCardView_cardTitle);
        String subtitle = array.getString(R.styleable.CustomCardView_cardSubtitle);
        String subtitle2 = array.getString(R.styleable.CustomCardView_cardSubtitle2);
        int icon2 = array.getResourceId(R.styleable.CustomCardView_cardIcon2, 0);

        if (icon != 0) imgIcon.setImageResource(icon);
        if (icon2 != 0) {
            imgEdit.setImageResource(icon2);
            imgEdit.setVisibility(VISIBLE);
        }

        txtTitle.setText(title);
        txtSubTitle.setText(subtitle);
        if (!TextUtils.isEmpty(subtitle2)) {
            txtSubTitle2.setVisibility(VISIBLE);
            txtSubTitle2.setText(subtitle2);
        } else {
            txtSubTitle2.setVisibility(GONE);
        }

        array.recycle();
    }

    public void setOnEditClickListener(OnClickListener listener) {
        imgEdit.setOnClickListener(listener);
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void setSubtitle(String subtitle) {
        txtSubTitle.setText(subtitle);
    }

    public void setIcon(int resId) {
        if (resId != 0) {
            imgIcon.setImageResource(resId);
        }
    }

    public void setIconUrl(@Nullable String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            Picasso.get()
                    .load(url)
                    .placeholder(placeholderRes)
                    .error(placeholderRes)
                    .fit()
                    .centerCrop()
                    .into(imgIcon);
        }
    }

    public void setSubtitle2(String subtitle2) {
        if (!TextUtils.isEmpty(subtitle2)) {
            txtSubTitle2.setVisibility(VISIBLE);
            txtSubTitle2.setText(subtitle2);
        } else {
            txtSubTitle2.setVisibility(GONE);
        }
    }
}
