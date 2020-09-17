package top.mymytan.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 九宫格VIEW
 * create by tan on 2020/9/16 19:24
 */
public class TaNineGridView extends ViewGroup {

    private float mDividerWidth; //分割线宽度
    private List<String> mData = new ArrayList<>();
    private ItemViewEngine mItemViewEngine;
    private int mMaxItemSize = 9; //默认9个


    public TaNineGridView(Context context) {
        this(context, null);
    }

    public TaNineGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaNineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TaNineGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TaNineGridView);
        mDividerWidth = typedArray.getDimension(R.styleable.TaNineGridView_taDividerWidth, 0.0f);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childSize = getChildCount();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (childSize <= 0) {
            setMeasuredDimension(0, 0);
            return;
        }

        //判断数量
        if (childSize == 1) {
            //一张图
            int width = (widthSize - getPaddingLeft() - getPaddingRight()) / 2;
            int height = width;
            View childView = getChildAt(0);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            heightSize = childView.getMeasuredHeight();
            widthSize = childView.getMeasuredWidth() + getPaddingLeft() + getPaddingRight();
        } else if (childSize == 2) {
            //两张图
            int width = (widthSize - getPaddingLeft() - getPaddingRight() - (int) mDividerWidth) / 2;
            int height = width;
            for (int i = 0; i < childSize; i++) {
                View childView = getChildAt(i);
                childView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY));
            }
            heightSize = height;
        } else {
            //大于2张图
            int width = (widthSize - getPaddingLeft() - getPaddingRight() - (int) mDividerWidth * 2) / 3;
            int height = width;
            for (int i = 0; i < childSize; i++) {
                View childView = getChildAt(i);
                childView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY));
                heightSize = height * ((i / 3) + 1) + (i / 3) * (int) mDividerWidth;
            }
        }
        setMeasuredDimension(widthSize, heightSize + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childSize = getChildCount();
        if (childSize <= 0) {
            return;
        }

        int left, top, right, bottom;
        for (int i = 0; i < childSize; i++) {
            View childView = getChildAt(i);
            left = (i % 3) * childView.getMeasuredWidth() + getPaddingLeft() + (i % 3) * (int) mDividerWidth;
            top = (i / 3) * childView.getMeasuredHeight() + getPaddingTop() + (i / 3) * (int) mDividerWidth;
            right = left + childView.getMeasuredWidth();
            bottom = top + childView.getMeasuredHeight();
            childView.layout(left, top, right, bottom);
        }
    }

    /**
     * 更新View
     */
    private void notifyViewUpdate() {
        removeAllViews();
        if (mItemViewEngine == null) {
            return;
        }
        for (int i = 0; i < mData.size(); i++) {
            //最多9个
            if (i < mMaxItemSize) {
                addView(mItemViewEngine.createView());
            }
        }
    }

    //===================================== public =============================================================
    public void setData(@NonNull List<String> data) {
        if (mItemViewEngine == null) {
            throw new IllegalArgumentException("please setItemViewEngine before setData");
        }

        mData.clear();
        mData.addAll(data);
        notifyViewUpdate();
    }

    /**
     * 设置最大的显示Item数量
     *
     * @param maxItemSize maxItemSize
     */
    public void setShowMaxItemSize(int maxItemSize) {
        this.mMaxItemSize = maxItemSize;
    }

    public void setItemViewEngine(@NonNull ItemViewEngine itemViewEngine) {
        this.mItemViewEngine = itemViewEngine;
    }

    public static abstract class ItemViewEngine {
        public abstract View createView();
    }
}
