package com.zzl.monitoring.station.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zzl.monitoring.station.R;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * Create by zilu 2020/09/02
 */
public class WindDirectionView extends ImageView {

    private Bitmap needle;
    private Point center = new Point();
    private Paint needlePaint;
    private float degrees = 0;

    public WindDirectionView(Context context) {
        this(context, null, 0);
    }

    public WindDirectionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WindDirectionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        needle = BitmapFactory.decodeResource(getResources(), R.drawable.ic_wind_direction_needle);
        needlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        needlePaint.setStrokeWidth(3);
        needlePaint.setColor(getResources().getColor(R.color.azure));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        center.x = w / 2;
        center.y = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(degrees, center.x, center.y);
        canvas.drawBitmap(needle, center.x - (needle.getWidth() / 2f), center.y - needle.getHeight(), needlePaint);
//        canvas.drawLine(center.x, center.y, center.x, 80, needlePaint);
    }

    public void setDirection(float degrees) {
        this.degrees = degrees;
        invalidate();
    }

//    private void startAnimation(float degrees) {
//
//    }
}
