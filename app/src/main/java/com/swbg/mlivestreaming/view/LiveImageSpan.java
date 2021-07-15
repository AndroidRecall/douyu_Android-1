package com.swbg.mlivestreaming.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.swbg.mlivestreaming.R;
import com.swbg.mlivestreaming.utils.ScreenUtils;


public class LiveImageSpan extends ImageSpan {
    public String level;
    private Paint mTextPaint;
    private int mMaxHeight;

    public LiveImageSpan(@NonNull Context context, @NonNull Bitmap bitmap) {
        super(context, bitmap);
    }

    public LiveImageSpan(@NonNull Context context, @NonNull Bitmap bitmap, int verticalAlignment) {
        super(context, bitmap, verticalAlignment);
    }

    public LiveImageSpan(@NonNull Drawable drawable) {
        super(drawable);
    }

    public LiveImageSpan(@NonNull Drawable drawable, @NonNull String source) {
        super(drawable, source);
    }

    public LiveImageSpan(@NonNull Drawable drawable, @NonNull String source, int verticalAlignment) {
        super(drawable, source, verticalAlignment);
    }

    public LiveImageSpan(@NonNull Context context, int resourceId, String level) {
        super(context, resourceId);
        this.level = level;
        initPaint(context);
    }
    public LiveImageSpan(@NonNull Context context, @NonNull Bitmap bitmap, String level) {
        super(context, bitmap);
        this.level = level;
        initPaint(context);
    }
    public LiveImageSpan(@NonNull Context context, @NonNull Bitmap bitmap, int verticalAlignment, String level) {
        super(context, bitmap,verticalAlignment);
        this.level = level;
        initPaint(context);
    }
    public LiveImageSpan(@NonNull Context context, @NonNull Bitmap bitmap, String level, int maxHeight) {
        super(context, bitmap);
        this.level = level;
        mMaxHeight = maxHeight;
        initPaint(context);
    }
    private void initPaint(Context context) {
        mTextPaint = new Paint();
        mTextPaint.setTextSize(ScreenUtils.dp2px(11));
        mTextPaint.setColor(context.getResources().getColor(R.color.white));
    }

    public LiveImageSpan setTextCenter() {
        mTextPaint.setTextAlign(Paint.Align.CENTER);//Paint设置水平居中
        return this;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        try {
            Drawable d = getDrawable();
            Rect rect = d.getBounds();
            if (fm != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;
                drHeight = drHeight > mMaxHeight ? mMaxHeight : drHeight;
                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;

                fm.ascent = -bottom;
                fm.top = -bottom;
                fm.bottom = top;
                fm.descent = top;
            }
            return rect.right;
        } catch (Exception e) {
            return 20;
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
//        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        Drawable drawable = getDrawable();
        int halfWidth = (int) (drawable.getIntrinsicWidth() / 2f);

        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        int transY = (y + fm.descent + y + fm.ascent) / 2 - drawable.getBounds().bottom / 2;
        canvas.save();
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
        canvas.drawText(level, x + halfWidth, y, mTextPaint);
    }

}
