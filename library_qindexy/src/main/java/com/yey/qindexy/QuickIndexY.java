package com.yey.qindexy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


public class QuickIndexY extends View {

    private String[] mWords = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int mSelectIndex = -1;
    private int mWordHeight;
    private int mWordWidth;
    private float mTextSize;
    private int mTextColorSelect;
    private int mTextColorDefault;
    private int mBoxHeight;//每一个块的高度
    private int mBgColor;
    private float mRadius;
    private RectF mBgRectF;
    private Paint mBgPaint;

    public QuickIndexY(Context context) {
        this(context, null);

    }

    public QuickIndexY(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 1);
    }

    public QuickIndexY(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParame(context, attrs, defStyleAttr);
        initPaint();
    }

    @SuppressLint("ResourceAsColor")
    private void initParame(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickIndexY, defStyleAttr, 0);
        mTextColorDefault = typedArray.getColor(R.styleable.QuickIndexY_qiy_text_default_color, Color.YELLOW);
        mTextColorSelect = typedArray.getColor(R.styleable.QuickIndexY_qiy_text_select_color, Color.RED);
        mTextSize = typedArray.getDimension(R.styleable.QuickIndexY_qiy_text_size, 7);
        mBgColor = typedArray.getColor(R.styleable.QuickIndexY_qiy_bg_color, Color.BLUE);
        mRadius = typedArray.getDimension(R.styleable.QuickIndexY_qiy_bg_radius, 4);
        typedArray.recycle();
    }


    private void initPaint() {
        //索引画笔
        mPaint = new Paint();
        mPaint.setColor(mTextColorDefault);
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);//设置粗体
        mPaint.setTextSize(mTextSize);
        //背景画笔
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBgPaint.setColor(mBgColor);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mBgRectF = new RectF();
        mBgRectF.set(0, 0, mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        mBoxHeight = mHeight / mWords.length;
        for (int i = 0; i < mWords.length; i++) {
            if (i == mSelectIndex) {
                mPaint.setColor(mTextColorSelect);
            } else {
                mPaint.setColor(mTextColorDefault);
            }
            String mWord = mWords[i];
            measureWords(mWord);
            //绘制文字的左上角坐标
            int mWordLeft = mWidth / 2 - mWordWidth / 2;
            //当索引为0时候, 文字应该展现在第一个块中,依次类推,需要再索引+1基础上,文字Y轴坐标
            int mWordTop = mBoxHeight * i + mBoxHeight / 2 + mWordHeight / 2;
            canvas.drawText(mWord, mWordLeft, mWordTop, mPaint);
        }
    }

    /**
     * 绘制背景
     */
    boolean ifFirstDraw;

    private void drawBg(Canvas canvas) {
        canvas.drawRoundRect(mBgRectF, mRadius, mRadius, mBgPaint);
    }

    /**
     * 获取每个单词的宽高
     */
    private void measureWords(String mWord) {
        Rect rect = new Rect();
        mPaint.getTextBounds(mWord, 0, mWord.length(), rect);
        mWordWidth = rect.width();
        mWordHeight = rect.height();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float mY = event.getY();
                int index = (int) (mY / mBoxHeight);
                if (index != mSelectIndex) {
                    mSelectIndex = index;
                    invalidate();
                    if (onQySelectIndex != null) {
                        //防止越界
                        if (index < 0) {
                            index = 0;
                        }
                        if (index >= mWords.length) {
                            index = mWords.length - 1;
                        }
                        onQySelectIndex.index(index, mWords[index]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mSelectIndex = -1;
                invalidate();
                break;
        }

        return true;
    }

    public interface OnQySelectIndex {
        /**
         * @param index 被选中的索引
         * @param word  被选中的索引单词
         */
        void index(int index, String word);
    }

    private OnQySelectIndex onQySelectIndex;

    /**
     * 设置回调
     *
     * @param onQySelectIndex
     */
    public void setOnQySelectIndex(OnQySelectIndex onQySelectIndex) {
        this.onQySelectIndex = onQySelectIndex;
    }

    /**
     * 设置索引
     */
    public void setWords(String[] mWords) {
        this.mWords = mWords;
        invalidate();
    }

}
