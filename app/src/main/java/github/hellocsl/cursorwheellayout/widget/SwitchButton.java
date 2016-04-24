package github.hellocsl.cursorwheellayout.widget;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.SoundEffectConstants;
import android.view.View;

import github.hellocsl.cursorwheellayout.R;


/**
 * <p/>
 * Created by chensuilun on 16-4-13.
 */
public class SwitchButton extends View {
    private static final String TAG = "SwitchButton";
    private static final int PROGRESS_MIN = 0;
    private static final int PROGRESS_MAX = 100;
    private static final int DEFAULT_BOARD_WIDTH = 4; // dp
    private static final int DEFAULT_BOARD_COLOR = 0xFF3c4952;
    private static final int DEFAULT_UNCHECK_REVEAL_COLOR = 0xFF3c4952;
    //DRAWABLE GRAVITY
    public static final int DRAWABLE_GRAVITY_LEFT = 0;
    public static final int DRAWABLE_GRAVITY_RIGHT = 1;
    public static final int DEFAULT_DRAWABLE_GRAVITY = -1;

    //    private static final int DEFAULT_UNCHECK_REVEAL_COLOR = 0X1D1F22;
    private static final int DEFAULT_CHECK_REVEAL_COLOR = 0xFFFFC52A;
    private static final int DEFAULT_DISABLE_COVER_COLOR = 0x4cffffff;
    private static final int DEFAULT_CHECK_DRAWABLE_ID = R.mipmap.ic_launcher;
    private static final int DEFAULT_UNCHECK_DRAWABLE_ID = R.mipmap.ic_launcher;

    private static final int DEFAULT_SIZE = 126; //dp

    private int mDrawableGravity = DEFAULT_DRAWABLE_GRAVITY;
    /**
     * 画边界
     */
    private Paint mBroadPaint;

    /**
     * 覆盖层
     */
    private Paint mCoverPaint;
    /**
     * 画扩散进度
     */
    private Paint mRevealBgPaint;
    /**
     * 边框颜色
     */
    private
    @ColorInt
    int mBoardColor = DEFAULT_BOARD_COLOR;


    /**
     * 边框厚度
     */
    private int mBoardWidth;
    /**
     * 选中的时候的颜色
     */
    private int mCheckRevealColor = DEFAULT_BOARD_COLOR;

    /**
     * 未选中时候的颜色
     */
    private int mUnCheckRevealColor = DEFAULT_UNCHECK_REVEAL_COLOR;

    /**
     * 用于动画 0-100 选中的时候;
     */
    private int mProgress;

    private boolean mChecked;

    private boolean mBroadcasting;

    private OnCheckedChangeListener mOnCheckedChangeListener;
    /**
     * 未选中的时候的资源ID
     */
    private Drawable mUncheckDrawable;
    /**
     * 未选中的时候的资源ID
     */
    private Drawable mCheckedDrawable;

    private ArgbEvaluator mArgbEvaluator;
    private float mFraction;
    private ObjectAnimator mCheckAnim;
    private boolean mAttachedToWindow;

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setClickable(true);
        mArgbEvaluator = new ArgbEvaluator();
        final float density = context.getResources().getDisplayMetrics().density;
        mBoardWidth = (int) (DEFAULT_BOARD_WIDTH * density + 0.5);
        //TODO 更多自定义属性的获取，这里hardcode先，以后有需要在提取出来
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
            int checkRes = ta.getResourceId(R.styleable.SwitchButton_checkSrc, DEFAULT_CHECK_DRAWABLE_ID);
            mCheckedDrawable = context.getResources().getDrawable(checkRes);
            int uncheckRes = ta.getResourceId(R.styleable.SwitchButton_uncheckSrc, DEFAULT_UNCHECK_DRAWABLE_ID);
            mUncheckDrawable = context.getResources().getDrawable(uncheckRes);
            mBoardWidth = ta.getDimensionPixelOffset(R.styleable.SwitchButton_boardWidth, mBoardWidth);
            mCheckRevealColor = ta.getColor(R.styleable.SwitchButton_checkRevealColor, DEFAULT_CHECK_REVEAL_COLOR);
            mUnCheckRevealColor = ta.getColor(R.styleable.SwitchButton_uncheckRevealColor, -1);
            ta.recycle();
        }
        //init paint
        mBroadPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBroadPaint.setStyle(Paint.Style.STROKE);
        mBroadPaint.setColor(mBoardColor);
        mBroadPaint.setStrokeWidth(mBoardWidth);
        mBroadPaint.setDither(true);

        mCoverPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCoverPaint.setStyle(Paint.Style.FILL);
        mCoverPaint.setColor(DEFAULT_DISABLE_COVER_COLOR);
        mCoverPaint.setDither(true);


        mRevealBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRevealBgPaint.setFilterBitmap(true);
        mRevealBgPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final float density = getContext().getResources().getDisplayMetrics().density;
        final int defaultSize = (int) (DEFAULT_SIZE * density + 0.5);
        int desiredWidth = getPaddingLeft() + getPaddingRight() + defaultSize;
        int desiredHeight = getPaddingBottom() + getPaddingTop() + defaultSize;
        int widthSpec = resolveSizeAndState(desiredWidth, widthMeasureSpec);
        int heightSpec = resolveSizeAndState(desiredHeight, heightMeasureSpec);
        super.onMeasure(widthSpec, heightSpec);
    }

    public static int resolveSizeAndState(int desireSize, int measureSpec) {
        int result = desireSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = desireSize;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < desireSize) {
                    result = specSize;
                } else {
                    result = desireSize;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttachedToWindow = false;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        final float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (DEFAULT_SIZE * density + 0.5);
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return getSuggestedMinimumWidth();
    }

    public void toggle() {
        setChecked(!mChecked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    private final void setProgress(int progress) {
        if (mProgress != progress) {
            this.mProgress = progress;
            mFraction = mProgress * 1.0f / PROGRESS_MAX;
//            if (BuildConfig.DEBUG) {
//                Log.d(TAG, "setProgress: mProgress:" + mProgress + ",mFraction:" + mFraction);
//            }
            invalidate();
        }
    }

    public final int getProgress() {
        return mProgress;
    }


    public static long slastTime;


    public boolean onDoubClick() {
        boolean flag = false;
        long time = System.currentTimeMillis() - slastTime;
        if (time < 500) {
            flag = true;
        }
        slastTime = System.currentTimeMillis();
        return flag;
    }

    @Override
    public boolean performClick() {
        if (onDoubClick()) {
            return false;
        }
        toggle();
        final boolean handled = super.performClick();
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK);
        }
        return handled;
    }

    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
            // Avoid infinite recursions if setChecked() is called from a listener
            if (mBroadcasting) {
                return;
            }
            if (mAttachedToWindow) {
                addAnim(checked);
            } else {
                cancelAnim();
                setProgress(checked ? PROGRESS_MAX : PROGRESS_MIN);
            }

            mBroadcasting = true;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
            mBroadcasting = false;
        }
    }

    private void cancelAnim() {
        if (mCheckAnim != null) {
            mCheckAnim.cancel();
        }
    }


    private void addAnim(boolean isChecked) {
        mCheckAnim = ObjectAnimator.ofInt(this, "progress", isChecked ? PROGRESS_MAX : PROGRESS_MIN);
        mCheckAnim.setDuration(300);
        mCheckAnim.start();
    }

    private int generateCurColor() {
        return (int) mArgbEvaluator.evaluate(mFraction, mUnCheckRevealColor, mCheckRevealColor);
    }


    public boolean isChecked() {
        return mChecked;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingBottom() - getPaddingTop();
        int radius = Math.min(width, height) / 2;
        final Drawable buttonDrawable = mChecked ? mCheckedDrawable : mUncheckDrawable;
        if (buttonDrawable != null) {
            //// FIXME: 16-4-20 臨時使用
            if (mDrawableGravity == DRAWABLE_GRAVITY_LEFT) {
                //偏右
                final int drawableHeight = buttonDrawable.getIntrinsicHeight();
                final int drawableWidth = buttonDrawable.getIntrinsicWidth();
                final int right = width / 2 - drawableWidth / 2;
                final int left = right - drawableWidth;
                final int top = (height - drawableHeight) / 2;
                final int bottom = top + drawableHeight;
                buttonDrawable.setBounds(left, top, right, bottom);
            } else if (mDrawableGravity == DRAWABLE_GRAVITY_RIGHT) {
                //偏左
                final int drawableHeight = buttonDrawable.getIntrinsicHeight();
                final int drawableWidth = buttonDrawable.getIntrinsicWidth();
                final int left = width / 2 + drawableWidth / 2;
                final int right = left + drawableWidth;
                final int top = (height - drawableHeight) / 2;
                final int bottom = top + drawableHeight;
                buttonDrawable.setBounds(left, top, right, bottom);
            } else {
                //居中
                final int drawableHeight = buttonDrawable.getIntrinsicHeight();
                final int drawableWidth = buttonDrawable.getIntrinsicWidth();
                final int left = (width - drawableWidth) / 2;
                final int right = left + drawableWidth;
                final int top = (height - drawableHeight) / 2;
                final int bottom = top + drawableHeight;
                buttonDrawable.setBounds(left, top, right, bottom);
            }
//            if (BuildConfig.DEBUG) {
//                Log.v(TAG, "onDraw:button drawable bound" + buttonDrawable.getBounds());
//            }
        }
        if (mUnCheckRevealColor != -1 && !mChecked) {
            mRevealBgPaint.setColor(mUnCheckRevealColor);
            canvas.drawCircle(width / 2, height / 2, radius - mBoardWidth, mRevealBgPaint);
        }
        //draw broad
        if (mBoardWidth > 0) {
            canvas.drawCircle(width / 2, height / 2, radius - mBoardWidth / 2, mBroadPaint);
        }

        mRevealBgPaint.setColor(generateCurColor());
        canvas.drawCircle(width / 2, height / 2, radius * mFraction, mRevealBgPaint);

        if (buttonDrawable != null) {
            buttonDrawable.draw(canvas);
        }
        if (!isEnabled()) {
            canvas.drawCircle(width / 2, height / 2, radius, mCoverPaint);
        }
        canvas.restore();
    }

    public void setDrawableGravity(int drawableGravity) {
        if (mDrawableGravity != drawableGravity) {
            mDrawableGravity = drawableGravity;
            invalidate();
        }
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(SwitchButton buttonView, boolean isChecked);
    }

    /**
     * @author chensuilun
     */
    static class SavedState extends BaseSavedState {
        boolean mChecked;

        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            mChecked = (Boolean) in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(mChecked);
        }

        @Override
        public String toString() {
            return "CompoundButton.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + mChecked + "}";
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);

        ss.mChecked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;

        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.mChecked);
        requestLayout();
    }

}
