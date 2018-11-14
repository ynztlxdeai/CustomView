package com.luoxiang.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.luoxiang.customview.bean.Point;

import java.util.ArrayList;

/**
 * projectName: 	    MakeGradle
 * packageName:	        com.example.luoxiang.makegradle.view
 * className:	        RelationView
 * author:	            Luoxiang
 * time:	            13/11/2018	10:15 AM
 * desc:	            TODO
 *
 * svnVersion:	        $Rev
 * upDateAuthor:	    luoxiang
 * upDate:	            13/11/2018
 * upDateDesc:	        TODO
 */

public class RelationView
        extends ViewGroup
{

    public ArrayList<Point> mPoints;

    public RelationView(@NonNull Context context) {
        this(context, null);
    }

    public RelationView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RelationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        if (childCount > 0) {
            mPoints = new ArrayList<>(childCount);
        }
        int paddingTop    = getPaddingTop();
        int paddingLeft   = getPaddingLeft();
        for (int i = 0; i < childCount; i++) {
            View view           = getChildAt(i);
            int  measuredHeight = view.getMeasuredHeight();
            int  measuredWidth  = view.getMeasuredWidth();
            if (i == 0) {
                int t              = paddingTop;
                int l              = paddingLeft;
                int r              = l + measuredWidth;
                int b              = t + measuredHeight;
                view.layout(l, t, r, b);
                mPoints.add(new Point(view.getX() + measuredWidth / 2f, view.getBottom()));
                paddingTop = 2 * measuredHeight + paddingTop;
            } else {
                int t              = paddingTop;
                int l              = left;
                if (i == 1) {
                    l += paddingLeft;
                    left += paddingLeft;
                }
                int r = l + measuredWidth;
                int b = t + measuredHeight;
                view.layout(l, t, r, b);
                mPoints.add(new Point(view.getX() + measuredWidth / 2f, view.getY()));
                //因为下一个的left是改变的 因此要一直记录这个left值
                left += measuredWidth + measuredWidth / 3;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获得当前的大小
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int count = getChildCount();

        int childTotelHeight = 0;
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            if (i == 0) {
                childTotelHeight = view.getMeasuredHeight();
            }
        }
        //拿到控件的上下边距
        int height = getPaddingTop() + getPaddingBottom();
        Log.e(RelationView.class.getName() , "child heght = " + childTotelHeight );
        height += childTotelHeight * 3;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLines(canvas);
        super.onDraw(canvas);
    }

    private void drawLines(Canvas canvas) {
        if (mPoints.size() < 2) {
            return;
        }
        int   size  = mPoints.size();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(6);

        canvas.drawLine(mPoints.get(0).x,
                        mPoints.get(0).y,
                        mPoints.get(1).x,
                        mPoints.get(1).y,
                        paint);
        float middleY = mPoints.get(0).y + (mPoints.get(1).y - mPoints.get(0).y) / 2;
        canvas.drawLine(mPoints.get(0).x, middleY, mPoints.get(size - 1).x, middleY, paint);
        canvas.drawLine(mPoints.get(size - 1).x, middleY, mPoints.get(size - 1).x, mPoints.get(size - 1).y, paint);
        for (int i = 2; i < size - 1; i++) {
            canvas.drawLine(mPoints.get(i).x, middleY, mPoints.get(i).x, mPoints.get(i).y, paint);
        }
    }
}
