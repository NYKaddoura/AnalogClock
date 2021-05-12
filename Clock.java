package com.example.nkaddouralab7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Clock extends View {
    public interface ClockListener{
        public void theTime(Date theDate);
    }

    private static final float dimension = 320;
    private static final float raduis = 140;
    private static final float minuteHandLength = 90;
    private static final float minuteHandTail = 5;
    private static final float hourHandLength = 80;
    private static final float hourHandTail = 8;
    private Date time;
    private ClockListener theTime;
    Paint paint = new Paint();
    private Bitmap bitmap;

    private static float[] hourHand = new float[] {0,0, -hourHandTail, -hourHandTail, 0, -hourHandLength , hourHandTail, -hourHandTail};
    private static float[] minuteHand = new float[] {0, 0,- minuteHandTail, -minuteHandTail, 0, -minuteHandLength, minuteHandTail, -minuteHandTail};

    public Clock(Context context) {
        super(context);
    }
    public Clock(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }
    public void setListener(ClockListener time){
        theTime = time;
    }
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        int color = Color.argb(255,0,0,0);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);

        Paint paint1 = new Paint();
        paint1.setColor(color);
        paint1.setStyle(Paint.Style.STROKE);

        canvas.drawRGB(255, 255, 255);

        Path secondsLine = new Path();
        secondsLine.moveTo(0, 0);
        secondsLine.lineTo(0, -135);
        secondsLine.lineTo(3, -135);
        secondsLine.lineTo(3, 0);
        secondsLine.close();

        Path hourLine = new Path();
        hourLine.moveTo(hourHand[0], hourHand[0]);
        hourLine.lineTo(hourHand[2], hourHand[3]);
        hourLine.lineTo(hourHand[4], hourHand[5]);
        hourLine.lineTo(hourHand[6], hourHand[7]);
        hourLine.close();

        Path minuteLine = new Path();
        minuteLine.moveTo(minuteHand[0], minuteHand[0]);
        minuteLine.lineTo(minuteHand[2], minuteHand[3]);
        minuteLine.lineTo(minuteHand[4], minuteHand[5]);
        minuteLine.lineTo(minuteHand[6], minuteHand[7]);
        minuteLine.close();

        float h = (float) getHeight() / dimension;
        float w = (float) getWidth() / dimension;

        if (bitmap != null){
            Rect rect = new Rect(0, 0, (int)getHeight(), (int)getWidth());
            canvas.drawBitmap(bitmap, null, rect, null);

            canvas.scale(w, h);
            canvas.translate(dimension / 2, dimension / 2);

            Calendar calendar = new GregorianCalendar();
            time = calendar.getTime();
            theTime.theTime(time);

            canvas.rotate(-15);

            float seconds = calendar.get(Calendar.SECOND);
            float minutes = calendar.get(Calendar.MINUTE);
            float hour = calendar.get(Calendar.HOUR_OF_DAY);

            canvas.save();
            canvas.rotate(seconds * 6);
            canvas.drawPath(secondsLine, paint);
            canvas.restore();

            canvas.save();
            canvas.rotate(minutes * 6);
            canvas.drawPath(minuteLine, paint);
            canvas.restore();

            canvas.save();
            canvas.rotate(hour * 30);
            canvas.drawPath(hourLine, paint);
            canvas.restore();
        } else {
            canvas.scale(w, h);
            canvas.translate(dimension / 2, dimension / 2);

            paint.setColor(color);

            Calendar calendar = new GregorianCalendar();
            time = calendar.getTime();
            theTime.theTime(time);

            drawCircles(canvas, paint1);
            canvas.rotate(-15);

            float seconds = calendar.get(Calendar.SECOND);
            float minutes = calendar.get(Calendar.MINUTE);
            float hour = calendar.get(Calendar.HOUR_OF_DAY);

            canvas.save();
            canvas.rotate(seconds * 6);
            canvas.drawPath(secondsLine, paint);
            canvas.restore();

            canvas.save();
            canvas.rotate(minutes * 6);
            canvas.drawPath(minuteLine, paint);
            canvas.restore();

            canvas.save();
            canvas.rotate(hour * 30);
            canvas.drawPath(hourLine, paint);
            canvas.restore();
        }
    }
    @Override
    public void onMeasure(int w, int h) {
        super.onMeasure(w, h);
        int width, height;

        if (h < w){
            height = MeasureSpec.getSize(h);
            w = h;
        } else {
            width = MeasureSpec.getSize(w);
            h = w;
        }

        setMeasuredDimension(w, h);
    }
    public void drawCircles(Canvas canvas, Paint paint){
        canvas.drawCircle(0, 0, raduis, paint);
        canvas.rotate(15);

        for (int i = 0; i < 60; i++){
            if (i == 0 || i  % 5 == 0){
                bigTics(canvas, paint);
            } else {
                smallTics(canvas, paint);
            }
            canvas.rotate(6);
        }
    }
    public void bigTics(Canvas canvas, Paint paint){
        canvas.drawLine(raduis/2, raduis/2, (float)(raduis * .7), (float)(raduis * .7), paint);
    }
    public void smallTics(Canvas canvas, Paint paint){
        canvas.drawLine((float)(raduis * .7), (float)(raduis * .7), (float) (raduis * .6), (float) (raduis * .6), paint);
    }
    public void changeFace(String face){
        System.out.println(face);
        if (face.equalsIgnoreCase("roman")){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.roman);
        } else if (face.equalsIgnoreCase("standard")){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.standard);
        } else if (face.equalsIgnoreCase("android")) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android);
        } else if (face.equalsIgnoreCase("blank")){
            bitmap = null;
        }
    }
}

