package com.swbg.mlivestreaming.ui.video.comment.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.swbg.mlivestreaming.R;

/**
 * @author KCrason
 * @date 2018/4/28
 */
public class TextClickSpan extends ClickableSpan {

    protected Context mContext;

    protected String mUserName;
    protected boolean mPressed;

    public TextClickSpan(Context context, String userName) {
        this.mContext = context;
        this.mUserName = userName;
    }

    public void setPressed(boolean isPressed) {
        this.mPressed = isPressed;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.bgColor = mPressed ? ContextCompat.getColor(mContext, R.color.base_B5B5B5) : Color.TRANSPARENT;
        ds.setColor(ContextCompat.getColor(mContext, R.color.text_all_btn_color));
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
//        Toast.makeText(mContext, "You Click " + mUserName, Toast.LENGTH_SHORT).show();
    }
}
