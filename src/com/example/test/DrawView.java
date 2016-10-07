package com.example.test;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class DrawView extends View implements OnTouchListener {
    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();
    static boolean first = true;
    Path path=new Path();
    public DrawView(Context context) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
    }

 
   @Override
    public void onDraw(Canvas canvas) {
    	
    	first = true;
        for (Point point : points) {
        	if(first){
                first = false;
                path.moveTo(point.x, point.y);
            }
            else{
                path.lineTo(point.x, point.y);
                
                
            }
        }
        canvas.drawPath(path, paint);
        
    }

    public boolean onTouch(View view, MotionEvent event) {
    	
        Point point = new Point();
        point.x = event.getX();
        point.y = event.getY();
        Log.d("check",""+point.x);
        points.add(point);
        
        invalidate();
        if (event.getAction()==MotionEvent.ACTION_DOWN) 
    	{
        	invalidate();
    	}
    	if (event.getAction()==MotionEvent.ACTION_UP) 
    	{
    		invalidate();
    	}
        return true;
    }
}

class Point {
    float x, y;
}   
