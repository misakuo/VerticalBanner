package com.moxun.vbanner;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by moxun on 16/3/29.
 */
public class VerticalBannerView extends RecyclerView {

    private VBAdapter adapter;
    private Runnable action;
    private int delay = 3000;
    private SpecifiedLinearLayoutManager layoutManager;
    private int visibleCount = 1;
    private int itemHeight;
    private int mode = MODE_SCROLL_OUT;
    private boolean isStarted = false;
    private OnScrollListener scrollListener;

    public static final int MODE_SCROLL_OUT = 1;
    public static final int MODE_FADE_OUT = 2;

    public VerticalBannerView(Context context) {
        super(context);
        init();
    }

    public VerticalBannerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalBannerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        layoutManager = new SpecifiedLinearLayoutManager(getContext());
        setLayoutManager(layoutManager);
        setOverScrollMode(OVER_SCROLL_NEVER);
        scrollListener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_IDLE && mode == MODE_SCROLL_OUT) {
                    layoutManager.lock();
                    adapter.next();
                    scrollToPosition(0);
                }
            }
        };
        initAction();
    }

    private void initAction() {
        if (mode == MODE_FADE_OUT) {
            action = new Runnable() {
                @Override
                public void run() {
                    if (adapter != null && isStarted) {
                        layoutManager.lock();
                        removeOnScrollListener(scrollListener);
                        adapter.next();
                        postDelayed(action, delay);
                    }
                }
            };
        } else {
            action = new Runnable() {
                @Override
                public void run() {
                    if (adapter != null && isStarted) {
                        layoutManager.unlock();
                        removeOnScrollListener(scrollListener);
                        addOnScrollListener(scrollListener);
                        smoothScrollBy(0, itemHeight);
                        postDelayed(action, delay);
                    }
                }
            };
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof VBAdapter) {
            super.setAdapter(adapter);
            this.adapter = (VBAdapter) adapter;
        } else {
            throw new IllegalArgumentException(adapter.getClass().getSimpleName() + " is not an instance of class VBAdapter<D>.");
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof SpecifiedLinearLayoutManager) {
            super.setLayoutManager(layout);
        } else {
            throw new IllegalArgumentException("Only SpecifiedLinearLayoutManager will be accepted here and you needn't call this method actually.");
        }
    }

    public void setVisibleItemCount(int count) {
        visibleCount = count;
        invalidate();
    }

    public void setAutoScrollDelay(int delay) {
        this.delay = delay;
    }

    public void setSwitchMode(int mode) {
        this.mode = mode;
        init();
    }

    public void start() {
        isStarted = true;
        removeCallbacks(action);
        postDelayed(action, delay);
    }

    public void stop() {
        isStarted = false;
        removeCallbacks(action);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isStarted) {
            removeCallbacks(action);
            postDelayed(action, delay);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(action);
    }

    private class SpecifiedLinearLayoutManager extends LinearLayoutManager {

        private boolean locked;

        public SpecifiedLinearLayoutManager(Context context) {
            super(context);
        }

        public SpecifiedLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public SpecifiedLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public boolean canScrollVertically() {
            return !locked;
        }

        protected void lock() {
            locked = true;
        }

        protected void unlock() {
            locked = false;
        }

        @Override
        public void onMeasure(Recycler recycler, State state, int widthSpec, int heightSpec) {
            if (getChildCount() > 0) {
                final int widthSize = MeasureSpec.getSize(widthSpec);

                View child1 = recycler.getViewForPosition(0);
                child1.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

                int h = getPaddingTop() + getPaddingBottom() + child1.getMeasuredHeight() * visibleCount;
                itemHeight = child1.getMeasuredHeight();
                setMeasuredDimension(widthSize, h);
            } else {
                super.onMeasure(recycler, state, widthSpec, heightSpec);
            }
        }
    }
}
