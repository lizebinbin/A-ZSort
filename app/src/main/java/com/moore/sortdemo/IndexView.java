package com.moore.sortdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by MooreLi on 2017/4/10.
 */

public class IndexView extends View {
    private String[] mIndexLetters = new String[]{"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Context mContext;
    int indexViewWidth;
    int indexViewHeight;
    int itemHeight;
    Paint letterPaint;
    Rect mRect;

    private int lastIndex = -1;
    private int currentIndex = -1;
    private AlertWindow mAlertWindow;
    private OnIndexChangedListener onIndexChangedListener;

    public IndexView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public IndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        initSize();
        this.setBackgroundColor(Color.GRAY);
        letterPaint = new Paint();
        letterPaint.setAntiAlias(true);
        letterPaint.setStyle(Paint.Style.STROKE);
        mRect = new Rect();
        mAlertWindow = new AlertWindow(mContext);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mIndexLetters.length; i++) {
            letterPaint.getTextBounds(mIndexLetters[i], 0, mIndexLetters[i].length(), mRect);
            int x = indexViewWidth / 2 - (mRect.width() / 2);
            int y = (itemHeight * i + itemHeight / 2) + (mRect.height() / 2);
            canvas.drawText(mIndexLetters[i], x, y, letterPaint);
        }
    }

    private void initSize() {
        itemHeight = dp2px(14);
        indexViewHeight = 27 * itemHeight;
        indexViewWidth = dp2px(15);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(indexViewWidth, indexViewHeight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            handleDown((int) event.getY());
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int moveY = (int) event.getY();
            handleMove(moveY);
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            handleUp();
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 手指按下，显示弹框
     *
     * @param downY
     */
    private void handleDown(int downY) {
        currentIndex = downY / itemHeight;
        if (mAlertWindow != null) {
            mAlertWindow.updateIndexLetter(mIndexLetters[currentIndex]);
            mAlertWindow.show();
        }
        lastIndex = currentIndex;
        if (onIndexChangedListener != null) {
            onIndexChangedListener.onIndexChanged(currentIndex, mIndexLetters[currentIndex]);
        }
    }

    /**
     * 手指移动，更新弹框
     *
     * @param moveY
     */
    private void handleMove(int moveY) {
        currentIndex = moveY / itemHeight;
        if (currentIndex != lastIndex) {
            if (mAlertWindow != null) {
                mAlertWindow.updateIndexLetter(mIndexLetters[currentIndex]);
            }
            lastIndex = currentIndex;
            if (onIndexChangedListener != null) {
                onIndexChangedListener.onIndexChanged(currentIndex, mIndexLetters[currentIndex]);
            }
        }
    }

    /**
     * 手指抬起，弹框消失
     */
    private void handleUp() {
        if (mAlertWindow != null && mAlertWindow.isShowing()) {
            mAlertWindow.dismiss();
        }
    }

    public void setOnIndexChangedListener(OnIndexChangedListener onIndexChangedListener) {
        this.onIndexChangedListener = onIndexChangedListener;
    }

    /**
     * 弹框window
     */
    private class AlertWindow extends PopupWindow {
        private LinearLayout contentView;
        private TextView mTvIndex;

        public AlertWindow(Context context) {
            super(context);
            initWindow();
        }

        private void initWindow() {
            this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
            this.setHeight(dp2px(40));
            this.setWidth(dp2px(40));

            contentView = new LinearLayout(mContext);
            contentView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            contentView.setBackgroundColor(Color.parseColor("#55CDC9C9"));
            contentView.setOrientation(LinearLayout.HORIZONTAL);
            mTvIndex = new TextView(mContext);
            mTvIndex.setTextSize(sp2px(14));
            mTvIndex.setTextColor(Color.parseColor("#696969"));
            mTvIndex.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            contentView.setGravity(Gravity.CENTER);
            contentView.addView(mTvIndex);

            this.setContentView(contentView);
        }

        public void updateIndexLetter(String letter) {
            mTvIndex.setText(letter);
            contentView.postInvalidate();
            postInvalidate();
        }

        public void show() {
            this.showAtLocation(getRootView(), Gravity.CENTER, 0, 0);
        }
    }

    /**
     * 选中回调接口
     */
    public interface OnIndexChangedListener {
        void onIndexChanged(int index, String indexLetter);
    }

    /********工具类*********/
    private int dp2px(int dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (scale * dp + 0.5f);
    }

    private int sp2px(int sp) {
        float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (scale * sp + 0.5f);
    }
}
