package com.swbg.mlivestreaming.view;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swbg.mlivestreaming.R;


/**
 * Created by VULCAN on 2017/12/6 0006.
 */

public class WarningView extends FrameLayout {
    private ImageView iconImg;
    private TextView hintTxt, retryTxt;

    public WarningView(@NonNull Context context) {
        super(context);
        init();
    }

    public WarningView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WarningView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setVisibility(View.GONE);
        View.inflate(getContext(), R.layout.layout_warning, this);
        iconImg = findViewById(R.id.img_warning_icon);
        hintTxt = findViewById(R.id.txt_warning_hint);
        retryTxt = findViewById(R.id.txt_warning_retry);
    }

    public void addOnRetryListener(OnClickListener listener) {
        if (listener != null) {
            retryTxt.setOnClickListener(listener);
        }
    }

    public void showNoNetWorkWarning() {
        retryTxt.setVisibility(View.VISIBLE);
        hintTxt.setText(R.string.warning_no_network);
//        Glide.with(getContext()).load(R.drawable.nonetwork).asBitmap().into(iconImg);
        iconImg.setBackgroundResource(R.mipmap.icon_no_network);
        setVisibility(View.VISIBLE);
    }
    public void showOtherWarning(int icon, String hint) {
        retryTxt.setVisibility(View.GONE);
        hintTxt.setText(hint);
//        Glide.with(getContext()).load(gicon).into(iconImg);
        iconImg.setBackgroundResource(icon);
        setVisibility(View.VISIBLE);
    }
    public void showOtherWarning(int icon, int hint) {
        retryTxt.setVisibility(View.GONE);
        hintTxt.setText(hint);
//        Glide.with(getContext()).load(gicon).into(iconImg);
        iconImg.setBackgroundResource(icon);
        setVisibility(View.VISIBLE);
    }

    public void hideWarning() {
        setVisibility(View.GONE);
    }

    public void setHintColor(int color) {
        hintTxt.setTextColor(getResources().getColor(color));
    }
}
