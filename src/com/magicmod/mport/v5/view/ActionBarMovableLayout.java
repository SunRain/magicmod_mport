
package com.magicmod.mport.v5.view;

import android.R.bool;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.OverScroller;

import com.magicmod.mport.internal.v5.widget.ActionBarLayout;
import com.miui.internal.R;

public class ActionBarMovableLayout extends ActionBarLayout {
    private static final boolean DBG = false;
    public static final int DEFAULT_SPRING_BACK_DURATION = 0x320;//800;
    public static final int STATE_DOWN = 1;
    public static final int STATE_UNKNOWN = -1;
    public static final int STATE_UP;
    private static final String TAG = ActionBarMovableLayout.class.getSimpleName();
    private int mActivePointerId;
    private Callback mCallback;
    private boolean mFlinging;
    private int mInitialMotionY = -1;
    private boolean mInitialMotionYSet;
    private boolean mIsBeingDragged;
    private boolean mIsSpringBackEnabled;
    private float mLastMotionX;
    private float mLastMotionY;
    private final int mMaximumVelocity;
    private final int mMinimumVelocity;
    private int mMotionY;
    private OnScrollListener mOnScrollListener;
    private int mOverScrollDistance;
    private int mScrollRange = -1;
    private int mScrollStart;
    private OverScroller mScroller;
    private int mState = -1;
    private View mTabScrollView;
    private int mTabViewVisibility = 8;
    private final int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public ActionBarMovableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray localTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ActionBarMovableLayout, R.attr.actionBarMovableLayoutStyle, 0);//100729022, 0);
        this.mOverScrollDistance = localTypedArray.getDimensionPixelSize(0, 0);
        this.mScrollRange = localTypedArray.getDimensionPixelSize(1, -1);
        this.mInitialMotionY = localTypedArray.getDimensionPixelSize(2, -1);
        ViewConfiguration localViewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = localViewConfiguration.getScaledTouchSlop();
        this.mScroller = new OverScroller(context);
        this.mMinimumVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
        setOverScrollMode(0);
        localTypedArray.recycle();
    }

    private boolean inChild(View child, int x, int y)
    {
        boolean flag = false;
        if (child != null) {
            int top = (int) child.getY();
            int left = (int) child.getX();
            int bottom = (int) (child.getY() + (float) child.getHeight());
            int right = (int) (child.getX() + (float) child.getWidth());
            if (child == mTabScrollView) {
                int containerTop = super.mActionBarContainer.getTop();
                top += containerTop;
                bottom += containerTop;
            }
            if (y >= top && y < bottom && x >= left && x < right)
                flag = true;
        }
        return flag;
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        else
            mVelocityTracker.clear();
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null)
            this.mVelocityTracker = VelocityTracker.obtain();
    }

    private boolean isTabViewVisibilityChanged() {
        boolean bool = false;
        ensureTabScrollView();
        if (this.mTabScrollView != null) {
            int i = this.mTabScrollView.getVisibility();
            if (i != this.mTabViewVisibility) {
                this.mTabViewVisibility = i;
                bool = true;
            }
        }
        return bool;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int i = (0xff00 & ev.getAction()) >> 8;
        if (ev.getPointerId(i) == mActivePointerId) {
            int j;
            if (i == 0)
                j = 1;
            else
                j = 0;
            mLastMotionY = (int) ev.getY(j);
            mActivePointerId = ev.getPointerId(j);
            if (mVelocityTracker != null)
                mVelocityTracker.clear();
        }
    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    protected void applyTranslationY(float motion) {
        float f = motionToTranslation(motion);
        this.mContentView.setTranslationY(f);
        ensureTabScrollView();
        if (this.mTabScrollView != null)
            this.mTabScrollView.setTranslationY(f);
    }

    public void computeScroll() {
        if (!mScroller.computeScrollOffset()) {
            if (mFlinging) {
                springBack();
                mFlinging = false;
            }
        } else {
            int oldMotionY = mMotionY;
            int motionY = mScroller.getCurrY();
            if (oldMotionY != motionY) {
                int range = getScrollRange();
                int distance = getOverScrollDistance();
                overScrollBy(0, motionY - oldMotionY, 0, mMotionY, 0, range, 0, distance, true);
            }
            postInvalidateOnAnimation();
        }
    }

    protected int computeVerticalScrollExtent() {
        return 0;
    }

    protected int computeVerticalScrollRange() {
        return getScrollRange();
    }

    protected int computeVerticalVelocity() {
        VelocityTracker localVelocityTracker = this.mVelocityTracker;
        localVelocityTracker.computeCurrentVelocity(/*1000*/0x3e8, this.mMaximumVelocity);
        return (int) localVelocityTracker.getYVelocity(this.mActivePointerId);
    }

    void ensureTabScrollView() {
        this.mTabScrollView = this.mActionBarContainer.getTabContainer();
    }

    protected void fling(int velocityY) {
        int i = getOverScrollDistance();
        int j = getScrollRange();
        this.mScroller.fling(0, this.mMotionY, 0, velocityY, 0, 0, 0, j, 0, i);
        this.mFlinging = true;
        postInvalidate();
    }

    public int getOverScrollDistance() {
        return this.mOverScrollDistance;
    }

    public int getScrollRange() {
        return this.mScrollRange;
    }

    public int getScrollStart() {
        return this.mScrollStart;
    }

    /*protected void measureChildWithMargins(View paramView, int paramInt1, int paramInt2,
            int paramInt3, int paramInt4) {
        if (paramView != this.mContentView)
            super.measureChildWithMargins(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
        while (true) {
            return;
            ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams) paramView
                    .getLayoutParams();
            paramView
                    .measure(
                            getChildMeasureSpec(
                                    paramInt1,
                                    paramInt2
                                            + (this.mPaddingLeft + this.mPaddingRight
                                                    + localMarginLayoutParams.leftMargin + localMarginLayoutParams.rightMargin),
                                    localMarginLayoutParams.width),
                            getChildMeasureSpec(paramInt3,
                                    this.mPaddingTop + this.mPaddingBottom
                                            + localMarginLayoutParams.bottomMargin
                                            + this.mActionBarView.getMeasuredHeight()
                                            - getScrollRange() - getOverScrollDistance()
                                            - this.mScrollStart, localMarginLayoutParams.height));
        }
    }*/
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int jwidthUsed,
            int kparentHeightMeasureSpec, int heightUsed) {
        if (child != mContentView) {
            super.measureChildWithMargins(child, parentWidthMeasureSpec, jwidthUsed,
                    kparentHeightMeasureSpec, heightUsed);
        } else {
            android.view.ViewGroup.MarginLayoutParams marginlayoutparams = (android.view.ViewGroup.MarginLayoutParams) child
                    .getLayoutParams();
            child.measure(
                    getChildMeasureSpec(
                            parentWidthMeasureSpec,
                            jwidthUsed
                                    + (mPaddingLeft + mPaddingRight + marginlayoutparams.leftMargin + marginlayoutparams.rightMargin),
                            ((android.view.ViewGroup.LayoutParams) (marginlayoutparams)).width),
                    getChildMeasureSpec(
                            kparentHeightMeasureSpec,
                            (mPaddingTop + mPaddingBottom + marginlayoutparams.bottomMargin + super.mActionBarView
                                    .getMeasuredHeight())
                                    - getScrollRange()
                                    - getOverScrollDistance() - mScrollStart,
                            ((android.view.ViewGroup.LayoutParams) (marginlayoutparams)).height));
        }
    }

    protected float motionToTranslation(float motion) {
        float f1 = motion - (float) mOverScrollDistance - (float) mScrollRange
                - (float) mScrollStart;
        ensureTabScrollView();
        if (mTabScrollView != null && mTabScrollView.getVisibility() == 0)
            f1 -= mTabScrollView.getHeight();
        return f1;
    }

    /*protected void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarContainer = ((ActionBarContainer) findViewById(ResourceMapper
                .resolveReference(this.mContext, 101384199)));
        this.mContentView = ((ViewGroup) findViewById(16908290));
    }*/
    protected void onFinishInflate() {
        super.onFinishInflate();
        mActionBarContainer = (ActionBarContainer) findViewById(ResourceMapper
                .resolveReference(mContext, R.id.android_action_bar_container));//0x60b0007));
        mContentView = (ViewGroup) findViewById(0x1020002);
    }

    /*public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        boolean bool = true;
        int i = paramMotionEvent.getAction();
        if ((i == 2) && (this.mIsBeingDragged))
            return bool;
        switch (i & 0xFF) {
            case 4:
            case 5:
            default:
            case 0:
            case 2:
            case 1:
            case 3:
            case 6:
        }
        while (true) {
            bool = this.mIsBeingDragged;
            break;
            this.mLastMotionY = paramMotionEvent.getY();
            this.mLastMotionX = paramMotionEvent.getX();
            this.mActivePointerId = paramMotionEvent.getPointerId(0);
            initOrResetVelocityTracker();
            this.mVelocityTracker.addMovement(paramMotionEvent);
            this.mScroller.forceFinished(bool);
            continue;
            if (shouldStartScroll(paramMotionEvent)) {
                this.mIsBeingDragged = bool;
                initVelocityTrackerIfNotExists();
                this.mVelocityTracker.addMovement(paramMotionEvent);
                onScrollBegin();
                continue;
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
                recycleVelocityTracker();
                onScrollEnd();
                continue;
                onSecondaryPointerUp(paramMotionEvent);
            }
        }
    }*/

    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean flag;
        //int action;
        flag = true;
        int action = event.getAction();
        if (action != 2 || !mIsBeingDragged) {
            switch (action & 0xFF) {
                case 0: // pswitch_1
                    mLastMotionY = event.getY();
                    mLastMotionX = event.getX();
                    mActivePointerId = event.getPointerId(0);
                    initOrResetVelocityTracker();
                    mVelocityTracker.addMovement(event);
                    mScroller.forceFinished(flag);
                    flag = mIsBeingDragged;
                    // return flag;
                    break;
                case 1: // pswitch_3
                case 3: // pswitch_3
                    mIsBeingDragged = false;
                    mActivePointerId = -1;
                    recycleVelocityTracker();
                    onScrollEnd();
                    flag = mIsBeingDragged;
                    // return flag;
                    break;
                case 2: // pswitch_2
                    if (shouldStartScroll(event)) {
                        mIsBeingDragged = flag;
                        initVelocityTrackerIfNotExists();
                        mVelocityTracker.addMovement(event);
                        onScrollBegin();
                    }
                    flag = mIsBeingDragged;
                    // return flag;
                    break;
                case 6: // pswitch_4
                    onSecondaryPointerUp(event);
                    flag = mIsBeingDragged;
                    // return flag;
                    break;
                case 4: // pswitch_0
                case 5: // pswitch_0
                default:
                    flag = mIsBeingDragged;
                    // return flag;
                    break;
            }
        }
        return flag;
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        boolean update;
        if (!mInitialMotionYSet || isTabViewVisibilityChanged())
            update = true;
        else
            update = false;
        if (!mInitialMotionYSet) {
            if (mInitialMotionY < 0)
                mInitialMotionY = mScrollRange;
            mMotionY = mInitialMotionY;
            mInitialMotionYSet = true;
        }
        if (update)
            applyTranslationY(mMotionY);
    }

    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
            boolean clampedY) {
        onScroll(scrollY);
        mMotionY = scrollY;
        if ((mMotionY == 0) && (clampedY)) {
            int i = computeVerticalVelocity();
            if ((Math.abs(i) > 2 * mMinimumVelocity) && (mOnScrollListener != null))
                mOnScrollListener.onFling(0.2F * -i, 500);
        }
    }

    protected void onScroll(float motionY) {
        applyTranslationY(motionY);
        if (mOnScrollListener != null)
            mOnScrollListener.onScroll(mState, motionY / mScrollRange);
    }

    protected void onScrollBegin() {
        if (mOnScrollListener != null)
            mOnScrollListener.onScrollBegin();
    }

    protected void onScrollEnd() {
        mState = -1;
        if (mOnScrollListener != null)
            mOnScrollListener.onScrollEnd();
    }

    /*public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    initVelocityTrackerIfNotExists();
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (0xFF & paramMotionEvent.getAction())
    {
    case 4:
    default:
    case 0:
    case 2:
    case 1:
    case 3:
    case 5:
    case 6:
    }
    while (true)
    {
      int m;
      for (boolean bool1 = true; ; bool1 = false)
      {
        return bool1;
        this.mLastMotionY = paramMotionEvent.getY();
        this.mActivePointerId = paramMotionEvent.getPointerId(0);
        break;
        if (!this.mIsBeingDragged)
          break label216;
        m = paramMotionEvent.findPointerIndex(this.mActivePointerId);
        if (m != -1)
          break label116;
      }
      label116: float f = paramMotionEvent.getY(m);
      int n = (int)(f - this.mLastMotionY);
      int i1 = getScrollRange();
      int i2 = getOverScrollDistance();
      boolean bool2 = overScrollBy(0, n, 0, this.mMotionY, 0, i1, 0, i2, true);
      this.mLastMotionY = f;
      if (bool2)
      {
        if (this.mMotionY == 0)
        {
          this.mIsBeingDragged = false;
          this.mActivePointerId = -1;
          paramMotionEvent.setAction(0);
          dispatchTouchEvent(paramMotionEvent);
        }
        this.mVelocityTracker.clear();
        continue;
        label216: if (shouldStartScroll(paramMotionEvent))
        {
          this.mIsBeingDragged = true;
          initVelocityTrackerIfNotExists();
          this.mVelocityTracker.addMovement(paramMotionEvent);
          onScrollBegin();
          continue;
          if (this.mIsBeingDragged)
          {
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
            int j = computeVerticalVelocity();
            if (Math.abs(j) > this.mMinimumVelocity)
            {
              fling(j);
            }
            else
            {
              int k = getScrollRange();
              if (this.mScroller.springBack(0, this.mMotionY, 0, 0, 0, k))
              {
                invalidate();
              }
              else
              {
                springBack();
                continue;
                int i = paramMotionEvent.getActionIndex();
                this.mLastMotionY = ((int)paramMotionEvent.getY(i));
                this.mActivePointerId = paramMotionEvent.getPointerId(i);
                continue;
                onSecondaryPointerUp(paramMotionEvent);
                this.mLastMotionY = ((int)paramMotionEvent.getY(paramMotionEvent.findPointerIndex(this.mActivePointerId)));
              }
            }
          }
        }
      }
    }
  }
*/
    
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        int action = event.getAction();

        switch (0xFF & action) {
            case 0: // pswitch_1
                mLastMotionY = event.getY();
                mActivePointerId = event.getPointerId(0);
                return true;
            case 2: // pswitch_2
                boolean flag = true;
                if (!mIsBeingDragged) {
                    if (shouldStartScroll(event)) {
                        mIsBeingDragged = true;
                        initVelocityTrackerIfNotExists();
                        mVelocityTracker.addMovement(event);
                        onScrollBegin();
                    }
                    // flag = true;
                } else {
                    int activePointerIndex = event.findPointerIndex(mActivePointerId);
                    if (activePointerIndex == -1) {
                        flag = false;
                        // return false;
                    } else {
                        float y = event.getY(activePointerIndex);
                        float lastMotionY = mLastMotionY;
                        int deltaY = (int) (y - lastMotionY);
                        int range = getScrollRange();
                        int distance = getOverScrollDistance();
                        boolean clamped = overScrollBy(0, deltaY, 0, mMotionY, 0, range, 0,
                                distance, true);
                        mLastMotionY = y;
                        if (clamped) {
                            if (mMotionY == 0) {
                                mIsBeingDragged = false;
                                mActivePointerId = -1;
                                event.setAction(0);
                                dispatchTouchEvent(event);
                            } else {
                                mVelocityTracker.clear();
                                // return true;
                            }
                        }
                    }
                }
                return flag;
            case 1: // pswitch_3
            case 3:// pswitch_3
                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    mActivePointerId = -1;
                    int initialVelocity = computeVerticalVelocity();
                    if (Math.abs(initialVelocity) > mMinimumVelocity) {
                        fling(initialVelocity);
                    } else {
                        int range = getScrollRange();
                        if (mScroller.springBack(0, mMotionY, 0, 0, 0, range))
                            invalidate();
                        else
                            springBack();
                    }
                }
                return true;
            case 5: // pswitch_4
                int index = event.getActionIndex();
                mLastMotionY = (int) event.getY(index);
                mActivePointerId = event.getPointerId(index);
                return true;
            case 6: // pswitch_5
                onSecondaryPointerUp(event);
                mLastMotionY = (int) event.getY(event.findPointerIndex(mActivePointerId));
                return true;
            case 4: // pswitch_0
            default:
                return true;
        }
    }

    /*protected boolean overScrollBy(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean)
  {
    int i = getOverScrollMode();
    int j;
    int k;
    label39: int m;
    boolean bool;
    if (computeVerticalScrollRange() > computeVerticalScrollExtent())
    {
      j = 1;
      if ((i != 0) && ((i != 1) || (j == 0)))
        break label96;
      k = 1;
      m = paramInt4 + paramInt2;
      if (k == 0)
        paramInt8 = 0;
      int n = paramInt8 + paramInt6;
      bool = false;
      if (m <= n)
        break label102;
      m = n;
      bool = true;
    }
    while (true)
    {
      onOverScrolled(0, m, false, bool);
      return bool;
      j = 0;
      break;
      label96: k = 0;
      break label39;
      label102: if (m < 0)
      {
        m = 0;
        bool = true;
      }
    }
  }*/
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
            int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
            boolean isTouchEvent) {
        int newScrollY;
        boolean clampedY;
        int overScrollMode = getOverScrollMode();
        boolean canScrollVertical;
        boolean overScrollVertical;
        int k2;
        int top;
        int buttom;
        if (computeVerticalScrollRange() > computeVerticalScrollExtent())
            canScrollVertical = true;
        else
            canScrollVertical = false;
        if (overScrollMode == 0 || overScrollMode == 1 && canScrollVertical) {
            overScrollVertical = true;
        } else {
            overScrollVertical = false;
        }
        newScrollY = scrollY + deltaY;
        //if (!overScrollVertical) {
            //maxOverScrollY = 0;
         //   top = 0;
        //}
        //if (overScrollVertical != false ) {
        if (overScrollVertical) {
            top = 0;
        } else {
            maxOverScrollY = 0;
        }
        //k2 = maxOverScrollY + scrollRangeY;
        buttom = maxOverScrollY + scrollRangeY;
        
        clampedY = false;
        if (newScrollY <= buttom) {
            if (newScrollY < 0) {
                newScrollY = 0;
                clampedY = true;
            }
            onOverScrolled(0, newScrollY, false, clampedY);
            // return flag3;
        } else {
            newScrollY = buttom;
            clampedY = true;
        }
        return clampedY;
    }

    public void requestDisallowInterceptTouchEvent(boolean paramBoolean) {
    }

    public void setCallback(Callback paramCallback) {
        this.mCallback = paramCallback;
    }

    public void setInitialMotionY(int paramInt) {
        this.mInitialMotionY = paramInt;
    }

    public void setMotionY(int paramInt) {
        this.mMotionY = paramInt;
        onScroll(paramInt);
    }

    public void setOnScrollListener(OnScrollListener paramOnScrollListener) {
        this.mOnScrollListener = paramOnScrollListener;
    }

    public void setOverScrollDistance(int paramInt) {
        this.mOverScrollDistance = paramInt;
    }

    public void setScrollRange(int paramInt) {
        this.mScrollRange = paramInt;
    }

    public void setScrollStart(int paramInt) {
        this.mScrollStart = paramInt;
    }

    public void setSpringBackEnabled(boolean paramBoolean) {
        this.mIsSpringBackEnabled = paramBoolean;
    }

    /*protected boolean shouldStartScroll(MotionEvent paramMotionEvent) {
        int i = this.mActivePointerId;
        if (i == -1)
            ;
        int j;
        for (boolean bool3 = false;; bool3 = false) {
            return bool3;
            j = paramMotionEvent.findPointerIndex(i);
            if (j != -1)
                break;
            Log.w(TAG, "invalid pointer index");
        }
        float f1 = paramMotionEvent.getX(j);
        float f2 = paramMotionEvent.getY(j);
        int k = (int) (f2 - this.mLastMotionY);
        int m = Math.abs(k);
        int n = (int) Math.abs(f1 - this.mLastMotionX);
        boolean bool1 = inChild(this.mContentView, (int) f1, (int) f2);
        boolean bool2 = inChild(this.mTabScrollView, (int) f1, (int) f2);
        int i1;
        if ((bool1) || (bool2)) {
            i1 = 1;
            label132: bool3 = false;
            if ((i1 != 0) && (m > this.mTouchSlop) && (m > n)) {
                bool3 = true;
                if (this.mMotionY == 0) {
                    if (k >= 0)
                        break label233;
                    bool3 = false;
                }
            }
            label174: if (!bool3)
                break label256;
            this.mLastMotionY = f2;
            this.mLastMotionX = f1;
            if (k <= 0)
                break label258;
        }
        label256: label258: for (int i2 = 1;; i2 = 0) {
            this.mState = i2;
            ViewParent localViewParent = getParent();
            if (localViewParent == null)
                break;
            localViewParent.requestDisallowInterceptTouchEvent(true);
            break;
            i1 = 0;
            break label132;
            label233: if ((this.mCallback == null) || (!this.mCallback.isContentVerticalScrolled()))
                break label174;
            bool3 = false;
            break label174;
            break;
        }
    }
*/
    protected boolean shouldStartScroll(MotionEvent event)
    {
        /*
    :cond_0
    :goto_0
    return v9
         */
        int activePointerId = mActivePointerId;
        boolean retval;
        if (activePointerId != -1) {
            int pointerIndex = event.findPointerIndex(activePointerId);
            if (pointerIndex != -1) {
                float x = event.getX(pointerIndex);
                float y = event.getY(pointerIndex);
                int yDiff = (int) (y - mLastMotionY);
                int absYDiff = Math.abs(yDiff);
                int absXDiff = (int) Math.abs(x - mLastMotionX);
                boolean fitContentView = inChild(mContentView, (int) x, (int) y);
                boolean fitTabScrollView = inChild(mTabScrollView, (int) x, (int) y);
                boolean flag2;
                ViewParent viewparent;
                if (fitContentView || fitTabScrollView)
                    flag2 = true;
                else
                    flag2 = false;
                retval = false;
                if (!flag2 || absYDiff <= mTouchSlop || absYDiff <= absXDiff) {
                    if (retval) {
                        mLastMotionY = y;
                        mLastMotionX = x;
                        int j1;
                        if (yDiff > 0)
                            j1 = 1;
                        else
                            j1 = 0;
                        mState = j1;
                        viewparent = getParent();
                        if (viewparent != null)
                            viewparent.requestDisallowInterceptTouchEvent(true);
                    }
                } else {
                    retval = true;
                    if (mMotionY != 0) {
                        if (retval) {
                            mLastMotionY = y;
                            mLastMotionX = x;
                            int j1;
                            if (yDiff > 0)
                                j1 = 1;
                            else
                                j1 = 0;
                            mState = j1;
                            viewparent = getParent();
                            if (viewparent != null)
                                viewparent.requestDisallowInterceptTouchEvent(true);
                        }
                    } else {
                        if (yDiff >= 0) {
                            if (mCallback != null && mCallback.isContentVerticalScrolled()) {
                                retval = false;
                            }
                            if (retval) {
                                mLastMotionY = y;
                                mLastMotionX = x;
                                int j1;
                                if (yDiff > 0)
                                    j1 = 1;
                                else
                                    j1 = 0;
                                mState = j1;
                                viewparent = getParent();
                                if (viewparent != null)
                                    viewparent.requestDisallowInterceptTouchEvent(true);
                            }
                            // goto _L7
                        } else {
                            retval = false;
                        }
                        // goto _L10; else goto _L9
                    }
                    // goto _L7; else goto _L8
                }
                // goto _L7; else goto _L6
            } else {
                Log.w(TAG, "invalid pointer index");
                retval = false;
                return retval;
                // goto _L11; else goto _L10
                // goto _L5
            }
            // goto _L4; else goto _L3
        } else {
            retval = false;
        }
        return retval;
            
/*
            //goto _L2; else goto _L1
_L1:
        boolean flag3 = false;
_L11:
        return flag3;
_L2:
        int j = motionevent.findPointerIndex(i);
        if(j != -1) goto _L4; else goto _L3
_L3:
        Log.w(TAG, "invalid pointer index");
        flag3 = false;
          goto _L5
_L4:
        float f = motionevent.getX(j);
        float f1 = motionevent.getY(j);
        int k = (int)(f1 - mLastMotionY);
        int l = Math.abs(k);
        int i1 = (int)Math.abs(f - mLastMotionX);
        boolean flag = inChild(super.mContentView, (int)f, (int)f1);
        boolean flag1 = inChild(mTabScrollView, (int)f, (int)f1);
        boolean flag2;
        ViewParent viewparent;
        if(flag || flag1)
            flag2 = true;
        else
            flag2 = false;
        flag3 = false;
        if(!flag2 || l <= mTouchSlop || l <= i1) goto _L7; else goto _L6
_L6:
        flag3 = true;
        if(mMotionY != 0) goto _L7; else goto _L8
_L8:
        if(k >= 0) goto _L10; else goto _L9
_L9:
        flag3 = false;
_L7:
        if(flag3)
        {
            mLastMotionY = f1;
            mLastMotionX = f;
            int j1;
            if(k > 0)
                j1 = 1;
            else
                j1 = 0;
            mState = j1;
            viewparent = getParent();
            if(viewparent != null)
                viewparent.requestDisallowInterceptTouchEvent(true);
        }
_L5:
        if(true) goto _L11; else goto _L10
_L10:
        if(mCallback != null && mCallback.isContentVerticalScrolled())
            flag3 = false;
          goto _L7
          */
    }
    
    
    protected void springBack() {
        if (mIsSpringBackEnabled) {
            int range = getScrollRange();
            int dy;
            if (mMotionY > range / 2)
                dy = range - mMotionY;
            else
                dy = -mMotionY;
            mScroller.startScroll(0, mMotionY, 0, dy, 800);
            postInvalidateOnAnimation();
        }
    }

    public static abstract interface Callback {
        public abstract boolean isContentVerticalScrolled();
    }

    public static abstract interface OnScrollListener {
        public abstract void onFling(float paramFloat, int paramInt);

        public abstract void onScroll(int paramInt, float paramFloat);

        public abstract void onScrollBegin();

        public abstract void onScrollEnd();
    }
}
