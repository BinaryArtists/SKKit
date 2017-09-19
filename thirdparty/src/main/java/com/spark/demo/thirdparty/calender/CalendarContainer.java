package com.spark.demo.thirdparty.calender;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.spark.demo.thirdparty.R;
import com.spark.demo.thirdparty.log.Logger;

public class CalendarContainer extends FrameLayout implements PtrCheckListener {

    private static final int DEFAULT_ROW_HEIGHT = 150;

    private final static String MONTH_TAG = "month";
    private final static String WEEK_TAG = "week";
    private final static String LIST_TAG = "list";
    private final static String LIST_PAGER_TAG = "list pager";

    /**
     * 停止滚动
     */
    public static final int SCROLL_STATE_IDLE = 0;
    /**
     * 手指在屏幕上正在滚动
     */
    public static final int SCROLL_STATE_TOUCH_SCROLL = 1;
    /**
     * 手指离开屏幕滚动
     */
    public static final int SCROLL_STATE_FLING = 2;

    private int mScrollState = SCROLL_STATE_IDLE;

    private VelocityTracker mVelocityTracker;

    private ViewPager mMonthPager;
    private ViewPager mWeekPager;
    private ViewPager mListPager;
    private ListView mListView;

    private int mRowHeight = DEFAULT_ROW_HEIGHT;
    private int mRowCount = CalendarController.WEEK_MAX_COUNT_OF_MONTH;

    private int mSelectedRow = 0;

    private float mLastMotionY;

    private boolean mIsOnSlidablyArea = false;

    private int mTouchSlop;

    /**
     * view是否展开
     */
    private boolean mExpanded = true;

    private CalendarType mCalendarType = CalendarType.Month;

    private OnCalendarTypeChangeListener mListener;

    public CalendarContainer(Context context) {
        this(context, null);
    }

    public CalendarContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledTouchSlop();

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CalendarContainer, defStyle, 0);
//         mRowCount = a.getInt(R.styleable.CalendarContainer_rowCount,
//                 DEFAULT_ROW_HEIGHT);
        mRowHeight = a.getDimensionPixelSize(R.styleable.CalendarContainer_rowHeight,
                DEFAULT_ROW_HEIGHT);

        a.recycle();
        init();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        removeView(mListView);
        removeView(mListPager);
        removeView(mMonthPager);
        removeView(mWeekPager);
        init();
    }

    private void init() {
        final Context context = getContext();

        View monthPager = findViewWithTag(MONTH_TAG);
        if (!(monthPager instanceof ViewPager)) {
            monthPager = new ViewPager(context);
            addView(monthPager);
        }
        mMonthPager = (ViewPager) monthPager;
        LayoutParams monthLp = new LayoutParams(LayoutParams.MATCH_PARENT, mRowCount
                * mRowHeight);
        monthLp.x = 0;
        monthLp.y = 0;
        mMonthPager.setLayoutParams(monthLp);
        mMonthPager.setVisibility(View.VISIBLE);

        View listView = findViewWithTag(LIST_TAG);
        if (!(listView instanceof ListView)) {
            listView = new ListView(context);
            addView(listView);
        }
        mListView = (ListView) listView;
        LayoutParams listLp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        listLp.x = 0;
        listLp.y = mRowCount * mRowHeight;
        mListView.setLayoutParams(listLp);
        mListView.setVisibility(View.VISIBLE);

        View listPager = findViewWithTag(LIST_PAGER_TAG);
        if (!(listPager instanceof ViewPager)) {
            listPager = new ViewPager(context);
            addView(listPager);
        }
        mListPager = (ViewPager) listPager;
        LayoutParams listPagerLp = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        listPagerLp.x = 0;
        listPagerLp.y = mRowCount * mRowHeight;
        mListPager.setLayoutParams(listPagerLp);
        mListPager.setVisibility(View.VISIBLE);

        View weekPager = findViewWithTag(WEEK_TAG);
        if (!(weekPager instanceof ViewPager)) {
            weekPager = new ViewPager(context);
            addView(weekPager);
        }
        mWeekPager = (ViewPager) weekPager;
        LayoutParams weekLp = new LayoutParams(LayoutParams.MATCH_PARENT, mRowHeight);
        weekLp.x = 0;
        weekLp.y = 0;
        mWeekPager.setLayoutParams(weekLp);
        mWeekPager.setVisibility(View.GONE);

        setCalendarType(CalendarType.Month);
    }

    private void acquireVelocityTrackerAndAddMovement(MotionEvent ev) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mScrollState == SCROLL_STATE_FLING) {
            return true;
        }
        if (getChildCount() <= 0) {
            mScrollState = SCROLL_STATE_IDLE;
            return super.onTouchEvent(ev);
        }
        acquireVelocityTrackerAndAddMovement(ev);

        final int action = ev.getAction();
        // final float x = ev.getX();
        final float y = ev.getY();
        Logger.i("onTouchEvent--y--"+y);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mScrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    final float deltaY = y - mLastMotionY;
                    if (Math.abs(deltaY) > 1.0f) {
                        move((int) deltaY);
                        mLastMotionY = y;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mScrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    flingAnimation();
                }
                releaseVelocityTracker();
                break;

            case MotionEvent.ACTION_CANCEL:
                cancel();
                releaseVelocityTracker();
                break;
        }

        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isListOnTop()) {
            return super.onInterceptTouchEvent(ev);
        }
        if (mScrollState == SCROLL_STATE_TOUCH_SCROLL
                || mScrollState == SCROLL_STATE_FLING) {
            return true;
        }
        if (getChildCount() <= 0) {
            return super.onInterceptTouchEvent(ev);
        }
        acquireVelocityTrackerAndAddMovement(ev);
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
        Logger.i("onInterceptTouchEvent--y--"+y);

        if (action != MotionEvent.ACTION_DOWN && !mIsOnSlidablyArea) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mIsOnSlidablyArea = isOnSlidablyArea(x, y);
                mScrollState = SCROLL_STATE_IDLE;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                determineScrollingStart(ev);
                break;
            case MotionEvent.ACTION_UP:
                releaseVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                cancel();
                releaseVelocityTracker();
                break;
        }
        return mScrollState == SCROLL_STATE_TOUCH_SCROLL
                || mScrollState == SCROLL_STATE_FLING;
    }

    private void determineScrollingStart(MotionEvent ev) {
        final float y = ev.getY();
        final float deltaY = y - mLastMotionY;
        final int yDiff = (int) Math.abs(deltaY);
        boolean xScrolled = yDiff >= mTouchSlop;
        if (xScrolled && ((deltaY < 0 && mExpanded) || (deltaY > 0 && !mExpanded))) {
            mScrollState = SCROLL_STATE_TOUCH_SCROLL;
        }
    }

    private void move(int distance) {
        LayoutParams listLp = getListLayoutParams();
        int listTop = mRowHeight;
        int listBottom = mRowHeight * mRowCount;
        int listTargetY = listLp.y + distance;
        listTargetY = Math.min(listBottom, Math.max(listTargetY, listTop));
        boolean needRequestLayout = false;
        if (listLp.y != listTargetY) {    // listview的滑动
            listLp.y = listTargetY;
            listLp.x = 0;
            needRequestLayout = true;
        }

        LayoutParams pagerLp = (LayoutParams) mMonthPager.getLayoutParams();
        int pagerTop = -mRowHeight * mSelectedRow;
        int pagerBottom = 0;
        int pagerTargetY = pagerLp.y + distance;
        pagerTargetY = Math.min(pagerBottom, Math.max(pagerTargetY, pagerTop));
        if (pagerLp.y != pagerTargetY) {  // month的滑动
            pagerLp.y = pagerTargetY;
            pagerLp.x = 0;
            needRequestLayout = true;
        }

        if (needRequestLayout) {
            requestLayout();
        }
    }

    private void end() {
        mExpanded = !mExpanded;
        mScrollState = SCROLL_STATE_IDLE;
    }

    private void cancel() {
        mScrollState = SCROLL_STATE_IDLE;
        LayoutParams listLp = getListLayoutParams();
        int listTargetY = mExpanded ? mRowHeight * mRowCount : mRowHeight;
        boolean needRequestLayout = false;
        if (listLp.y != listTargetY) {
            listLp.y = listTargetY;
            listLp.x = 0;
            needRequestLayout = true;
        }

        LayoutParams pagerLp = (LayoutParams) mMonthPager.getLayoutParams();
        int pagerTargetY = mExpanded ? 0 : -mRowHeight * mSelectedRow;
        if (pagerLp.y != pagerTargetY) {
            pagerLp.y = pagerTargetY;
            pagerLp.x = 0;
            needRequestLayout = true;
        }

        if (needRequestLayout) {
            requestLayout();
        }
    }

    private void flingAnimation() {
        mScrollState = SCROLL_STATE_FLING;
        final LayoutParams listLp = getListLayoutParams();
        int listTop = mRowHeight;
        int listBottom = mRowHeight * mRowCount;

        final LayoutParams pagerLp = (LayoutParams) mMonthPager.getLayoutParams();
        int pagerTop = -mRowHeight * mSelectedRow;
        int pagerBottom = 0;

        if (listLp.y == listTop) {
            mScrollState = SCROLL_STATE_IDLE;
            mExpanded = false;
        } else if (listLp.y == listBottom) {
            mScrollState = SCROLL_STATE_IDLE;
            mExpanded = true;
        } else {
            final boolean expanded = mExpanded;
            int listTargetY = expanded ? listTop : listBottom;
            ValueAnimator listAnimator = ValueAnimator.ofInt(listLp.y, listTargetY);
            listAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    int targetY = (Integer) (animator.getAnimatedValue());
                    listLp.y = targetY;
                    requestLayout();
                }
            });

            int pagerTargetY = expanded ? pagerTop : pagerBottom;
            ValueAnimator pagerAnimator = ValueAnimator.ofInt(pagerLp.y, pagerTargetY);
            pagerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    int targetY = (Integer) (animator.getAnimatedValue());
                    pagerLp.y = targetY;
                    requestLayout();
                }
            });

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(listAnimator, pagerAnimator);
            animatorSet.setDuration(300);
            animatorSet.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    end();
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    cancel();
                }
            });
            animatorSet.start();
        }
    }

    private boolean isListOnTop() {
        ListView listView = getListView();
        if (listView == null) {
            return true;
        }
        int count = listView.getCount();
        if (count <= 0) {
            return true;
        }
        if (listView != null) {
            int firstVisiblePosition = listView.getFirstVisiblePosition();
            View firstView = listView.getChildAt(0);
            int listPaddingTop = listView.getPaddingTop();
            return firstVisiblePosition == 0 && firstView != null
                    && firstView.getTop() == listPaddingTop;
        }
        return false;
    }

    /**
     * 计算可滑动区域，list在父控件中的可见区域
     */
    private boolean isOnSlidablyArea(float x, float y) {
        View view = getListOrPagerView();
        int left = Math.max(0, view.getLeft());
        int top = Math.max(0, view.getTop());
        int right = Math.min(getWidth(), view.getRight());
        int bottom = Math.min(getHeight(), view.getBottom());
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    public ViewPager getMonthPager() {
        return mMonthPager;
    }

    public ViewPager getWeekPager() {
        return mWeekPager;
    }

    public ViewPager getListPager() {
        return mListPager;
    }

    public ListView getListView() {
        PagerAdapter pagerAdapter = mListPager.getAdapter();
        if (pagerAdapter instanceof BasePagerAdapter) {
            return (ListView) ((BasePagerAdapter) pagerAdapter).getViewByPos(mListPager
                    .getCurrentItem());
        }
        return mListView;
    }

    View getListOrPagerView() {
        int listPagerChildCount = mListPager.getChildCount();
        PagerAdapter pagerAdapter = mListPager.getAdapter();
        if (pagerAdapter == null || listPagerChildCount <= 0) {
            return mListView;
        }
        return mListPager;
    }

    LayoutParams getListLayoutParams() {
        PagerAdapter pagerAdapter = mListPager.getAdapter();
        if (pagerAdapter != null) {
            return (LayoutParams) mListPager.getLayoutParams();
        }
        return (LayoutParams) mListView.getLayoutParams();
    }

    public void setOnCalendarTypeChangeListener(OnCalendarTypeChangeListener l) {
        mListener = l;
    }

    public CalendarType getCalendarType() {
        return mCalendarType;
    }

    public void setCalendarType(CalendarType type) {
        if (type != mCalendarType) {
            mCalendarType = type;
            if (mListener != null) {
                mListener.onCalendarTypeChange(mCalendarType);
            }
        }
    }

    public int getRowCount() {
        return mRowCount;
    }

    public int getSelectdRow() {
        return mSelectedRow;
    }

    public void setSelectedRow(int selectedRow) {
        if (selectedRow != mSelectedRow) {
            mSelectedRow = selectedRow;
            cancel();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        boolean measurePager = mListPager.getAdapter() != null;
        mListPager.setVisibility(measurePager ? View.VISIBLE : View.GONE);
        if (measurePager && mListPager.getVisibility() != View.GONE) {
            LayoutParams listLp = (LayoutParams) mListPager.getLayoutParams();
            listLp.width = widthSize - paddingLeft - paddingRight;
            listLp.height = heightSize - mRowHeight - paddingTop - paddingBottom;
            mListPager.measure(
                    MeasureSpec.makeMeasureSpec(listLp.width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(listLp.height, MeasureSpec.EXACTLY));

            mMonthPager.setVisibility(listLp.y == mRowHeight ? View.GONE : View.VISIBLE);
            mWeekPager.setVisibility(listLp.y == mRowHeight ? View.VISIBLE : View.GONE);
            setCalendarType(listLp.y == mRowHeight ? CalendarType.Week
                    : CalendarType.Month);
        }
        mListView.setVisibility(measurePager ? View.GONE : View.VISIBLE);
        if (!measurePager && mListView.getVisibility() != View.GONE) {
            LayoutParams listLp = (LayoutParams) mListView.getLayoutParams();
            listLp.width = widthSize - paddingLeft - paddingRight;
            listLp.height = heightSize - mRowHeight - paddingTop - paddingBottom;
            mListView.measure(
                    MeasureSpec.makeMeasureSpec(listLp.width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(listLp.height, MeasureSpec.EXACTLY));

            mMonthPager.setVisibility(listLp.y == mRowHeight ? View.GONE : View.VISIBLE);
            mWeekPager.setVisibility(listLp.y == mRowHeight ? View.VISIBLE : View.GONE);
            setCalendarType(listLp.y == mRowHeight ? CalendarType.Week
                    : CalendarType.Month);
        }

        if (mMonthPager.getVisibility() != View.GONE) {
            LayoutParams pagerLp = (LayoutParams) mMonthPager.getLayoutParams();
            pagerLp.width = widthSize - paddingLeft - paddingRight;
            pagerLp.height = mRowHeight * mRowCount;
            mMonthPager.measure(
                    MeasureSpec.makeMeasureSpec(pagerLp.width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(pagerLp.height, MeasureSpec.EXACTLY));
        }

        if (mWeekPager.getVisibility() != View.GONE) {
            LayoutParams pagerLp = (LayoutParams) mWeekPager.getLayoutParams();
            pagerLp.width = widthSize - paddingLeft - paddingRight;
            pagerLp.height = mRowHeight;
            mWeekPager.measure(
                    MeasureSpec.makeMeasureSpec(pagerLp.width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(pagerLp.height, MeasureSpec.EXACTLY));
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        boolean layoutPager = mListPager.getAdapter() != null;
        if (layoutPager && mListPager.getVisibility() != View.GONE) {
            LayoutParams listLp = (LayoutParams) mListPager.getLayoutParams();
            mListPager.layout(listLp.x + paddingLeft, listLp.y + paddingTop, listLp.x
                    + paddingLeft + listLp.width, listLp.y + paddingTop + listLp.height);

        }

        if (!layoutPager && mListView.getVisibility() != View.GONE) {
            LayoutParams listLp = (LayoutParams) mListView.getLayoutParams();
            mListView.layout(listLp.x + paddingLeft, listLp.y + paddingTop, listLp.x
                    + paddingLeft + listLp.width, listLp.y + paddingTop + listLp.height);
        }

        if (mMonthPager.getVisibility() != View.GONE) {
            LayoutParams pagerLp = (LayoutParams) mMonthPager.getLayoutParams();
            mMonthPager.layout(pagerLp.x + paddingLeft, pagerLp.y + paddingTop, pagerLp.x
                    + paddingLeft + pagerLp.width, pagerLp.y + paddingTop
                    + pagerLp.height);
        }

        if (mWeekPager.getVisibility() != View.GONE) {
            LayoutParams pagerLp = (LayoutParams) mWeekPager.getLayoutParams();
            mWeekPager.layout(pagerLp.x + paddingLeft, pagerLp.y, pagerLp.x + paddingLeft
                    + pagerLp.width, pagerLp.y + paddingTop + pagerLp.height);
        }

    }

    @Override
    public boolean checkCanScrollUp() {
        LayoutParams listLp = getListLayoutParams();
        return mScrollState == SCROLL_STATE_TOUCH_SCROLL || !isListOnTop() || (listLp != null && listLp.y < mRowCount * mRowHeight);
    }

    class LayoutParams extends FrameLayout.LayoutParams {

        int x;
        int y;

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    public interface OnCalendarTypeChangeListener {
        public void onCalendarTypeChange(CalendarType type);
    }

    public enum CalendarType {
        Month, Week, None
    }

}
