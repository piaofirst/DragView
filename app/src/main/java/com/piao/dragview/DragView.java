package com.piao.dragview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

public class DragView extends RelativeLayout {

    //上次触摸位置
    private View mDragView;
    private ViewDragHelper mDragHelper;
    private Context mContext;
    private OnClickListener mOnClickListener;
    private int dragViewContentLayout;
    // 记录最后的位置
    private float mLastX = -1;
    private float mLastY = -1;
    private float marginRight = 0;
    private float marginLeft = 0;
    private float marginTop = 0;
    private float marginBottom = 0;

    public DragView(@NonNull Context context) {
        this(context, null);
    }

    public DragView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext = context;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragView, 0, 0);
            dragViewContentLayout = typedArray.getResourceId(R.styleable.DragView_dragView_contentLayout, -1);
            typedArray.recycle();
        }
        mDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            // 决定了是否需要捕获这个 child，只有捕获了才能进行下面的拖拽行为
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                // 方法返回 true 时才会导致下面的回调方法被调用
                return child == mDragView;
            }

            // 修整 child 水平方向上的坐标，left 指 child 要移动到的坐标，dx 相对上次的偏移量
            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                if (left < 0) {
                    //限制左边界
                    left = 0;
                } else if (left > (getMeasuredWidth() - child.getMeasuredWidth())) {
                    //限制右边界
                    left = getMeasuredWidth() - child.getMeasuredWidth();
                }
                return left;
            }

            // 修整 child 垂直方向上的坐标，top 指 child 要移动到的坐标，dy 相对上次的偏移量
            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                if (top < 0) {
                    //限制上边界
                    top = 0;
                } else if (top > (getMeasuredHeight() - child.getMeasuredHeight())) {
                    //限制下边界
                    top = getMeasuredHeight() - child.getMeasuredHeight();
                }
                return top;
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            //位置变化时
            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                mLastX = changedView.getX();
                mLastY = changedView.getY();
            }

            // 手指释放时的回调
            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        restorePosition();
    }

    public void restorePosition() {
        if (mLastX == -1 && mLastY == -1) { // 初始位置 右下角
            mLastX = getWidth() - mDragView.getWidth() - marginRight;
            mLastY = getHeight() - mDragView.getHeight() - marginBottom;
        }
        //防止界面刷新后回到原位置
        mDragView.layout((int) mLastX, (int) mLastY,
                (int) mLastX + mDragView.getWidth(), (int) mLastY + mDragView.getHeight());
    }

    public void setMarginRight(float marginRight) {
        this.marginRight = marginRight;
    }

    public void setMarginLeft(float marginLeft) {
        this.marginLeft = marginLeft;
    }

    public void setMarginTop(float marginTop) {
        this.marginTop = marginTop;
    }

    public void setMarginBottom(float marginBottom) {
        this.marginBottom = marginBottom;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //是否应该拦截 children 的触摸事件，只有拦截了 ViewDragHelper 才能进行后续的动作
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //处理 ViewGroup 中传递过来的触摸事件序列
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (dragViewContentLayout != -1) {
            mDragView = LayoutInflater.from(mContext).inflate(dragViewContentLayout, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.addView(mDragView, params);
            mDragView.setOnClickListener(v -> mOnClickListener.onClick(v));
        }
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
}
