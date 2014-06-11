
package com.magicmod.mport.v5.view;

import android.R;
import android.R.attr;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;

import com.magicmod.mport.v5.android.support.view.ViewPager.ItemInfo;

import java.util.ArrayList;
import java.util.Comparator;

public class ViewPager extends ViewGroup {
    
    static abstract interface Decor {
    }

    static class ItemInfo {
        boolean hasActionMenu;
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;
    }

    private static final int CLOSE_ENOUGH = 2;
    
    private static final Comparator<ItemInfo> COMPARATOR = new Comparator<ItemInfo>(){
        @Override
        public int compare(ItemInfo lhs, ItemInfo rhs) {
            return lhs.position - rhs.position;
        }
    };

    static final float CURRENT_PAGE_DETERMIN_FACTOR = 0.05F;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 3;
    private static final int INVALID_POINTER = -1;
    private static final int[] LAYOUT_ATTRS = new int[]{
        attr.layout_gravity
    };////{
    //    16842931//
    //};
    private static final int MAX_SETTLE_DURATION = 800;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int PAGE_SETTLE_DURATION = 250;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE;
    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float paramAnonymousFloat) {
            float f = paramAnonymousFloat - 1.0F;
            return 1.0F + f * (f * (f * (f * f)));
        }
    };
    private int mActivePointerId = -1;
    private PagerAdapter mAdapter;
    private OnAdapterChangeListener mAdapterChangeListener;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    private int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    boolean mDragEnabled = true;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout = true;
    private float mFirstOffset = -3.402824E+38F;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mIgnoreGutter;
    private boolean mInLayout;
    private float mInitialMotionX;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems = new ArrayList();
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset = 3.4028235E+38F;
    float mLastPageOffset = 0.0F;
    private EdgeEffect mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets = false;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit = 3;
    private OnPageChangeListener mOnPageChangeListener;
    private int mPageMargin;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private int mRestoredCurItem = -1;
    private EdgeEffect mRightEdge;
    private int mScrollState = 0;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private final ItemInfo mTempItem = new ItemInfo();
    private final Rect mTempRect = new Rect();
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public ViewPager(Context paramContext) {
        super(paramContext);
        initViewPager();
    }

    public ViewPager(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        initViewPager();
    }

    private void calculatePageOffsets(ItemInfo paramItemInfo1, int paramInt, ItemInfo paramItemInfo2)
  {
    int i = this.mAdapter.getCount();
    int j = getWidth();
    float f1;
    int i5;
    int i8;
    float f7;
    if (j > 0)
    {
      f1 = this.mPageMargin / j;
      if (paramItemInfo2 == null)
        break label371;
      i5 = paramItemInfo2.position;
      if (i5 < paramItemInfo1.position)
      {
        i8 = 0;
        f7 = f1 + (paramItemInfo2.offset + paramItemInfo2.widthFactor);
      }
    }
    else
    {
      for (int i9 = i5 + 1; ; i9++)
      {
        if ((i9 > paramItemInfo1.position) || (i8 >= this.mItems.size()))
          break label371;
        ItemInfo localItemInfo4 = (ItemInfo)this.mItems.get(i8);
        while (true)
          if ((i9 > localItemInfo4.position) && (i8 < -1 + this.mItems.size()))
          {
            i8++;
            localItemInfo4 = (ItemInfo)this.mItems.get(i8);
            continue;
            f1 = 0.0F;
            break;
          }
        while (i9 < localItemInfo4.position)
        {
          f7 += f1 + this.mAdapter.getPageWidth(i9);
          i9++;
        }
        localItemInfo4.offset = f7;
        f7 += f1 + localItemInfo4.widthFactor;
      }
    }
    if (i5 > paramItemInfo1.position)
    {
      int i6 = -1 + this.mItems.size();
      float f6 = paramItemInfo2.offset;
      for (int i7 = i5 - 1; (i7 >= paramItemInfo1.position) && (i6 >= 0); i7--)
      {
        for (ItemInfo localItemInfo3 = (ItemInfo)this.mItems.get(i6); (i7 < localItemInfo3.position) && (i6 > 0); localItemInfo3 = (ItemInfo)this.mItems.get(i6))
          i6--;
        while (i7 > localItemInfo3.position)
        {
          f6 -= f1 + this.mAdapter.getPageWidth(i7);
          i7--;
        }
        f6 -= f1 + localItemInfo3.widthFactor;
        localItemInfo3.offset = f6;
      }
    }
    label371: int k = this.mItems.size();
    float f2 = paramItemInfo1.offset;
    int m = -1 + paramItemInfo1.position;
    float f3;
    float f4;
    label437: int n;
    if (paramItemInfo1.position == 0)
    {
      f3 = paramItemInfo1.offset;
      this.mFirstOffset = f3;
      if (paramItemInfo1.position != i - 1)
        break label518;
      f4 = paramItemInfo1.offset + paramItemInfo1.widthFactor - 1.0F;
      this.mLastOffset = f4;
      n = paramInt - 1;
    }
    while (true)
    {
      if (n < 0)
        break label568;
      ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(n);
      while (true)
        if (m > localItemInfo2.position)
        {
          PagerAdapter localPagerAdapter2 = this.mAdapter;
          int i4 = m - 1;
          f2 -= f1 + localPagerAdapter2.getPageWidth(m);
          m = i4;
          continue;
          f3 = -3.402824E+38F;
          break;
          label518: f4 = 3.4028235E+38F;
          break label437;
        }
      f2 -= f1 + localItemInfo2.widthFactor;
      localItemInfo2.offset = f2;
      if (localItemInfo2.position == 0)
        this.mFirstOffset = f2;
      n--;
      m--;
    }
    label568: float f5 = f1 + (paramItemInfo1.offset + paramItemInfo1.widthFactor);
    int i1 = 1 + paramItemInfo1.position;
    int i2 = paramInt + 1;
    while (i2 < k)
    {
      ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(i2);
      while (i1 < localItemInfo1.position)
      {
        PagerAdapter localPagerAdapter1 = this.mAdapter;
        int i3 = i1 + 1;
        f5 += f1 + localPagerAdapter1.getPageWidth(i1);
        i1 = i3;
      }
      if (localItemInfo1.position == i - 1)
        this.mLastOffset = (f5 + localItemInfo1.widthFactor - 1.0F);
      localItemInfo1.offset = f5;
      f5 += f1 + localItemInfo1.widthFactor;
      i2++;
      i1++;
    }
    this.mNeedCalculatePageOffsets = false;
  }

    private void completeScroll() {
        if (this.mScrollState == 2)
            ;
        for (int i = 1;; i = 0) {
            if (i != 0) {
                setScrollingCacheEnabled(false);
                this.mScroller.abortAnimation();
                int k = getScrollX();
                int m = getScrollY();
                int n = this.mScroller.getCurrX();
                int i1 = this.mScroller.getCurrY();
                if ((k != n) || (m != i1))
                    scrollTo(n, i1);
                setScrollState(0);
            }
            this.mPopulatePending = false;
            for (int j = 0; j < this.mItems.size(); j++) {
                ItemInfo localItemInfo = (ItemInfo) this.mItems.get(j);
                if (localItemInfo.scrolling) {
                    i = 1;
                    localItemInfo.scrolling = false;
                }
            }
        }
        if (i != 0)
            populate();
    }

    private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3) {
        int i;
        if ((Math.abs(paramInt3) > this.mFlingDistance)
                && (Math.abs(paramInt2) > this.mMinimumVelocity))
            if (paramInt2 > 0)
                i = paramInt1;
        while (true) {
            if (this.mItems.size() > 0) {
                ItemInfo localItemInfo1 = (ItemInfo) this.mItems.get(0);
                ItemInfo localItemInfo2 = (ItemInfo) this.mItems.get(-1 + this.mItems.size());
                i = Math.max(localItemInfo1.position, Math.min(i, localItemInfo2.position));
            }
            return i;
            i = paramInt1 + 1;
            continue;
            i = (int) (0.5F + (paramFloat + paramInt1));
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private Rect getChildRectInPagerCoordinates(Rect paramRect, View paramView) {
        if (paramRect == null)
            paramRect = new Rect();
        if (paramView == null)
            paramRect.set(0, 0, 0, 0);
        while (true) {
            return paramRect;
            paramRect.left = paramView.getLeft();
            paramRect.right = paramView.getRight();
            paramRect.top = paramView.getTop();
            paramRect.bottom = paramView.getBottom();
            ViewGroup localViewGroup;
            for (ViewParent localViewParent = paramView.getParent(); ((localViewParent instanceof ViewGroup))
                    && (localViewParent != this); localViewParent = localViewGroup.getParent()) {
                localViewGroup = (ViewGroup) localViewParent;
                paramRect.left += localViewGroup.getLeft();
                paramRect.right += localViewGroup.getRight();
                paramRect.top += localViewGroup.getTop();
                paramRect.bottom += localViewGroup.getBottom();
            }
        }
    }

    private ItemInfo infoForCurrentScrollPosition() {
        float f1 = 0.0F;
        int i = getWidth();
        float f2;
        int j;
        float f3;
        float f4;
        int k;
        Object localObject;
        if (i > 0) {
            f2 = getScrollX() / i;
            if (i > 0)
                f1 = this.mPageMargin / i;
            j = -1;
            f3 = 0.0F;
            f4 = 0.0F;
            k = 1;
            localObject = null;
        }
        for (int m = 0;; m++) {
            ItemInfo localItemInfo;
            float f5;
            if (m < this.mItems.size()) {
                localItemInfo = (ItemInfo) this.mItems.get(m);
                if ((k == 0) && (localItemInfo.position != j + 1)) {
                    localItemInfo = this.mTempItem;
                    localItemInfo.offset = (f1 + (f3 + f4));
                    localItemInfo.position = (j + 1);
                    localItemInfo.widthFactor = this.mAdapter.getPageWidth(localItemInfo.position);
                    m--;
                }
                f5 = localItemInfo.offset;
                float f6 = f1 + (f5 + localItemInfo.widthFactor);
                if ((k != 0) || (f2 >= f5)) {
                    if ((f2 >= f6) && (m != -1 + this.mItems.size()))
                        break label205;
                    localObject = localItemInfo;
                }
            }
            return localObject;
            f2 = 0.0F;
            break;
            label205: k = 0;
            j = localItemInfo.position;
            f3 = f5;
            f4 = localItemInfo.widthFactor;
            localObject = localItemInfo;
        }
    }

    private boolean isGutterDrag(float paramFloat1, float paramFloat2) {
        if (((paramFloat1 < this.mGutterSize) && (paramFloat2 > 0.0F))
                || ((paramFloat1 > getWidth() - this.mGutterSize) && (paramFloat2 < 0.0F)))
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
        int i = paramMotionEvent.getActionIndex();
        if (paramMotionEvent.getPointerId(i) == this.mActivePointerId)
            if (i != 0)
                break label56;
        label56: for (int j = 1;; j = 0) {
            this.mLastMotionX = paramMotionEvent.getX(j);
            this.mActivePointerId = paramMotionEvent.getPointerId(j);
            if (this.mVelocityTracker != null)
                this.mVelocityTracker.clear();
            return;
        }
    }

    private boolean pageScrolled(int paramInt, boolean paramBoolean) {
        boolean bool = false;
        int k;
        float f2;
        int n;
        int i1;
        if (this.mItems.size() == 0) {
            this.mCalledSuper = false;
            onPageScrolled(0, 0.0F, 0);
            if (!this.mCalledSuper)
                throw new IllegalStateException(
                        "onPageScrolled did not call superclass implementation");
        } else {
            ItemInfo localItemInfo = infoForCurrentScrollPosition();
            int i = getWidth();
            int j = i + this.mPageMargin;
            float f1 = this.mPageMargin / i;
            k = localItemInfo.position;
            f2 = (paramInt / i - localItemInfo.offset) / (f1 + localItemInfo.widthFactor);
            int m = (int) (f2 * j);
            this.mCalledSuper = false;
            onPageScrolled(k, f2, m);
            if (!this.mCalledSuper)
                throw new IllegalStateException(
                        "onPageScrolled did not call superclass implementation");
            if (paramBoolean) {
                n = 0;
                i1 = k;
                if ((this.mLastPageOffset >= f2) || (f2 <= 0.55F))
                    break label259;
                n = 1;
                i1 = k + 1;
                this.mLastPageOffset = f2;
                if (n != 0) {
                    if (i1 >= 0)
                        break label288;
                    i1 = 0;
                }
            }
        }
        while (true) {
            if (i1 != this.mCurItem) {
                populate(i1);
                if (this.mOnPageChangeListener != null)
                    this.mOnPageChangeListener.onPageSelected(i1);
                if (this.mInternalPageChangeListener != null)
                    this.mInternalPageChangeListener.onPageSelected(i1);
            }
            bool = true;
            return bool;
            label259: if ((this.mLastPageOffset <= f2) || (f2 >= 0.45F))
                break;
            n = 1;
            i1 = k;
            break;
            label288: if (i1 >= this.mAdapter.getCount())
                i1 = -1 + this.mAdapter.getCount();
        }
    }

    private boolean performDrag(float paramFloat) {
        boolean bool = false;
        float f1 = this.mLastMotionX - paramFloat;
        this.mLastMotionX = paramFloat;
        float f2 = f1 + getScrollX();
        int i = getWidth();
        float f3 = i * this.mFirstOffset;
        float f4 = i * this.mLastOffset;
        int j = 1;
        int k = 1;
        ItemInfo localItemInfo1 = (ItemInfo) this.mItems.get(0);
        ItemInfo localItemInfo2 = (ItemInfo) this.mItems.get(-1 + this.mItems.size());
        if (localItemInfo1.position != 0) {
            j = 0;
            f3 = localItemInfo1.offset * i;
        }
        if (localItemInfo2.position != -1 + this.mAdapter.getCount()) {
            k = 0;
            f4 = localItemInfo2.offset * i;
        }
        if (f2 < f3) {
            if (j != 0) {
                float f6 = f3 - f2;
                this.mLeftEdge.onPull(Math.abs(f6) / i);
                bool = true;
            }
            f2 = f3;
        }
        while (true) {
            this.mLastMotionX += f2 - (int) f2;
            scrollTo((int) f2, getScrollY());
            pageScrolled((int) f2, true);
            return bool;
            if (f2 > f4) {
                if (k != 0) {
                    float f5 = f2 - f4;
                    this.mRightEdge.onPull(Math.abs(f5) / i);
                    bool = true;
                }
                f2 = f4;
            }
        }
    }

    private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        if ((paramInt2 > 0) && (!this.mItems.isEmpty())) {
            int j = paramInt1 + paramInt3;
            int k = paramInt2 + paramInt4;
            int m = (int) (getScrollX() / k * j);
            scrollTo(m, getScrollY());
            if (!this.mScroller.isFinished()) {
                int n = this.mScroller.getDuration() - this.mScroller.timePassed();
                ItemInfo localItemInfo2 = infoForPosition(this.mCurItem);
                this.mScroller.startScroll(m, 0, (int) (localItemInfo2.offset * paramInt1), 0, n);
            }
            return;
        }
        ItemInfo localItemInfo1 = infoForPosition(this.mCurItem);
        if (localItemInfo1 != null)
            ;
        for (float f = Math.min(localItemInfo1.offset, this.mLastOffset);; f = 0.0F) {
            int i = (int) (f * paramInt1);
            if (i == getScrollX())
                break;
            completeScroll();
            scrollTo(i, getScrollY());
            break;
        }
    }

    private void removeNonDecorViews() {
        for (int i = 0; i < getChildCount(); i++)
            if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
                removeViewAt(i);
                i--;
            }
    }

    private void setScrollState(int paramInt) {
        if (this.mScrollState == paramInt)
            ;
        while (true) {
            return;
            this.mScrollState = paramInt;
            if (this.mOnPageChangeListener != null)
                this.mOnPageChangeListener.onPageScrollStateChanged(paramInt);
            if (this.mInternalPageChangeListener != null)
                this.mInternalPageChangeListener.onPageScrollStateChanged(paramInt);
        }
    }

    private void setScrollingCacheEnabled(boolean paramBoolean) {
        if (this.mScrollingCacheEnabled != paramBoolean)
            this.mScrollingCacheEnabled = paramBoolean;
    }

    public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2) {
        int i = paramArrayList.size();
        int j = getDescendantFocusability();
        if (j != 393216)
            for (int k = 0; k < getChildCount(); k++) {
                View localView = getChildAt(k);
                if (localView.getVisibility() == 0) {
                    ItemInfo localItemInfo = infoForChild(localView);
                    if ((localItemInfo != null) && (localItemInfo.position == this.mCurItem))
                        localView.addFocusables(paramArrayList, paramInt1, paramInt2);
                }
            }
        if (((j == 262144) && (i != paramArrayList.size())) || (!isFocusable()))
            ;
        while (true) {
            return;
            if ((((paramInt2 & 0x1) != 1) || (!isInTouchMode()) || (isFocusableInTouchMode()))
                    && (paramArrayList != null))
                paramArrayList.add(this);
        }
    }

    ItemInfo addNewItem(int paramInt1, int paramInt2) {
        ItemInfo localItemInfo = new ItemInfo();
        localItemInfo.position = paramInt1;
        localItemInfo.object = this.mAdapter.instantiateItem(this, paramInt1);
        localItemInfo.widthFactor = this.mAdapter.getPageWidth(paramInt1);
        localItemInfo.hasActionMenu = this.mAdapter.hasActionMenu(paramInt1);
        if ((paramInt2 < 0) || (paramInt2 >= this.mItems.size()))
            this.mItems.add(localItemInfo);
        while (true) {
            return localItemInfo;
            this.mItems.add(paramInt2, localItemInfo);
        }
    }

    public void addTouchables(ArrayList<View> paramArrayList) {
        for (int i = 0; i < getChildCount(); i++) {
            View localView = getChildAt(i);
            if (localView.getVisibility() == 0) {
                ItemInfo localItemInfo = infoForChild(localView);
                if ((localItemInfo != null) && (localItemInfo.position == this.mCurItem))
                    localView.addTouchables(paramArrayList);
            }
        }
    }

    public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
        if (!checkLayoutParams(paramLayoutParams))
            paramLayoutParams = generateLayoutParams(paramLayoutParams);
        LayoutParams localLayoutParams = (LayoutParams) paramLayoutParams;
        localLayoutParams.isDecor |= paramView instanceof Decor;
        if (this.mInLayout) {
            if ((localLayoutParams != null) && (localLayoutParams.isDecor))
                throw new IllegalStateException("Cannot add pager decor view during layout");
            localLayoutParams.needsMeasure = true;
            addViewInLayout(paramView, paramInt, paramLayoutParams);
        }
        while (true) {
            return;
            super.addView(paramView, paramInt, paramLayoutParams);
        }
    }

    public boolean arrowScroll(int paramInt) {
        View localView1 = findFocus();
        if (localView1 == this)
            localView1 = null;
        boolean bool = false;
        View localView2 = FocusFinder.getInstance().findNextFocus(this, localView1, paramInt);
        if ((localView2 != null) && (localView2 != localView1))
            if (paramInt == 17) {
                int k = getChildRectInPagerCoordinates(this.mTempRect, localView2).left;
                int m = getChildRectInPagerCoordinates(this.mTempRect, localView1).left;
                if ((localView1 != null) && (k >= m))
                    bool = pageLeft();
            }
        while (true) {
            if (bool)
                playSoundEffect(SoundEffectConstants.getContantForFocusDirection(paramInt));
            return bool;
            bool = localView2.requestFocus();
            continue;
            if (paramInt == 66) {
                int i = getChildRectInPagerCoordinates(this.mTempRect, localView2).left;
                int j = getChildRectInPagerCoordinates(this.mTempRect, localView1).left;
                if ((localView1 != null) && (i <= j)) {
                    bool = pageRight();
                } else {
                    bool = localView2.requestFocus();
                    continue;
                    if ((paramInt == 17) || (paramInt == 1))
                        bool = pageLeft();
                    else if ((paramInt == 66) || (paramInt == 2))
                        bool = pageRight();
                }
            }
        }
    }

    public boolean beginFakeDrag() {
        boolean bool = false;
        if ((this.mIsBeingDragged) || (!this.mDragEnabled))
            return bool;
        this.mFakeDragging = true;
        setScrollState(1);
        this.mLastMotionX = 0.0F;
        this.mInitialMotionX = 0.0F;
        if (this.mVelocityTracker == null)
            this.mVelocityTracker = VelocityTracker.obtain();
        while (true) {
            long l = SystemClock.uptimeMillis();
            MotionEvent localMotionEvent = MotionEvent.obtain(l, l, 0, 0.0F, 0.0F, 0);
            this.mVelocityTracker.addMovement(localMotionEvent);
            localMotionEvent.recycle();
            this.mFakeDragBeginTime = l;
            bool = true;
            break;
            this.mVelocityTracker.clear();
        }
    }

    protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2,
            int paramInt3) {
        int k;
        boolean bool;
        if ((paramView instanceof ViewGroup)) {
            ViewGroup localViewGroup = (ViewGroup) paramView;
            int i = paramView.getScrollX();
            int j = paramView.getScrollY();
            k = -1 + localViewGroup.getChildCount();
            if (k >= 0) {
                View localView = localViewGroup.getChildAt(k);
                if ((paramInt2 + i >= localView.getLeft())
                        && (paramInt2 + i < localView.getRight())
                        && (paramInt3 + j >= localView.getTop())
                        && (paramInt3 + j < localView.getBottom())
                        && (canScroll(localView, true, paramInt1,
                                paramInt2 + i - localView.getLeft(),
                                paramInt3 + j - localView.getTop())))
                    bool = true;
            }
        }
        while (true) {
            return bool;
            k--;
            break;
            if ((paramBoolean) && (paramView.canScrollHorizontally(-paramInt1)))
                bool = true;
            else
                bool = false;
        }
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
        if (((paramLayoutParams instanceof LayoutParams))
                && (super.checkLayoutParams(paramLayoutParams)))
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    public void computeScroll() {
        if ((!this.mScroller.isFinished()) && (this.mScroller.computeScrollOffset())) {
            int i = getScrollX();
            int j = getScrollY();
            int k = this.mScroller.getCurrX();
            int m = this.mScroller.getCurrY();
            if ((i != k) || (j != m)) {
                scrollTo(k, m);
                if (!pageScrolled(k, false)) {
                    this.mScroller.abortAnimation();
                    scrollTo(0, m);
                }
            }
            postInvalidateOnAnimation();
        }
        while (true) {
            return;
            completeScroll();
        }
    }

    void dataSetChanged()
  {
    int i;
    int j;
    int k;
    int m;
    int n;
    label50: ItemInfo localItemInfo;
    int i3;
    boolean bool2;
    if ((this.mItems.size() < 1 + 2 * this.mOffscreenPageLimit) && (this.mItems.size() < this.mAdapter.getCount()))
    {
      i = 1;
      j = this.mCurItem;
      k = 0;
      m = 0;
      n = 0;
      if (n >= this.mItems.size())
        break label330;
      localItemInfo = (ItemInfo)this.mItems.get(n);
      i3 = this.mAdapter.getItemPosition(localItemInfo.object);
      if (i3 != -1)
        break label153;
      if (localItemInfo.hasActionMenu != this.mAdapter.hasActionMenu(localItemInfo.position))
      {
        if (localItemInfo.hasActionMenu)
          break label147;
        bool2 = true;
        label127: localItemInfo.hasActionMenu = bool2;
        k = 1;
      }
    }
    label147: label153: label247: 
    do
    {
      while (true)
      {
        n++;
        break label50;
        i = 0;
        break;
        bool2 = false;
        break label127;
        if (i3 != -2)
          break label247;
        this.mItems.remove(n);
        n--;
        if (m == 0)
        {
          this.mAdapter.startUpdate(this);
          m = 1;
        }
        this.mAdapter.destroyItem(this, localItemInfo.position, localItemInfo.object);
        i = 1;
        if (this.mCurItem == localItemInfo.position)
        {
          j = Math.max(0, Math.min(this.mCurItem, -1 + this.mAdapter.getCount()));
          i = 1;
        }
      }
      if (localItemInfo.position != i3)
      {
        if (localItemInfo.position == this.mCurItem)
          j = i3;
        localItemInfo.position = i3;
        i = 1;
      }
    }
    while (localItemInfo.hasActionMenu == this.mAdapter.hasActionMenu(localItemInfo.position));
    if (!localItemInfo.hasActionMenu);
    for (boolean bool1 = true; ; bool1 = false)
    {
      localItemInfo.hasActionMenu = bool1;
      k = 1;
      break;
    }
    label330: if (m != 0)
      this.mAdapter.finishUpdate(this);
    Collections.sort(this.mItems, COMPARATOR);
    if (i != 0)
    {
      int i1 = getChildCount();
      for (int i2 = 0; i2 < i1; i2++)
      {
        LayoutParams localLayoutParams = (LayoutParams)getChildAt(i2).getLayoutParams();
        if (!localLayoutParams.isDecor)
          localLayoutParams.widthFactor = 0.0F;
      }
      setCurrentItemInternal(j, false, true);
      requestLayout();
      k = 0;
    }
    if (k != 0)
      requestLayout();
  }

    public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
        if ((super.dispatchKeyEvent(paramKeyEvent)) || (executeKeyEvent(paramKeyEvent)))
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
        int i = getChildCount();
        int j = 0;
        if (j < i) {
            View localView = getChildAt(j);
            if (localView.getVisibility() == 0) {
                ItemInfo localItemInfo = infoForChild(localView);
                if ((localItemInfo == null) || (localItemInfo.position != this.mCurItem)
                        || (!localView.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent)))
                    ;
            }
        }
        for (boolean bool = true;; bool = false) {
            return bool;
            j++;
            break;
        }
    }

    float distanceInfluenceForSnapDuration(float paramFloat) {
        return (float) Math.sin((float) (0.47123891676382D * (paramFloat - 0.5F)));
    }

    public void draw(Canvas paramCanvas) {
        super.draw(paramCanvas);
        boolean bool = false;
        int i = getOverScrollMode();
        if ((i == 0) || ((i == 1) && (this.mAdapter != null) && (this.mAdapter.getCount() > 1))) {
            if (!this.mLeftEdge.isFinished()) {
                int n = paramCanvas.save();
                int i1 = getHeight() - getPaddingTop() - getPaddingBottom();
                int i2 = getWidth();
                paramCanvas.rotate(270.0F);
                paramCanvas.translate(-i1 + getPaddingTop(), this.mFirstOffset * i2);
                this.mLeftEdge.setSize(i1, i2);
                bool = false | this.mLeftEdge.draw(paramCanvas);
                paramCanvas.restoreToCount(n);
            }
            if (!this.mRightEdge.isFinished()) {
                int j = paramCanvas.save();
                int k = getWidth();
                int m = getHeight() - getPaddingTop() - getPaddingBottom();
                paramCanvas.rotate(90.0F);
                paramCanvas.translate(-getPaddingTop(), -(1.0F + this.mLastOffset) * k);
                this.mRightEdge.setSize(m, k);
                bool |= this.mRightEdge.draw(paramCanvas);
                paramCanvas.restoreToCount(j);
            }
        }
        while (true) {
            if (bool)
                postInvalidateOnAnimation();
            return;
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable localDrawable = this.mMarginDrawable;
        if ((localDrawable != null) && (localDrawable.isStateful()))
            localDrawable.setState(getDrawableState());
    }

    public void endFakeDrag() {
        if (!this.mFakeDragging)
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        VelocityTracker localVelocityTracker = this.mVelocityTracker;
        localVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        int i = (int) localVelocityTracker.getXVelocity(this.mActivePointerId);
        this.mPopulatePending = true;
        int j = getWidth();
        int k = getScrollX();
        ItemInfo localItemInfo = infoForCurrentScrollPosition();
        setCurrentItemInternal(
                determineTargetPage(localItemInfo.position, (k / j - localItemInfo.offset)
                        / localItemInfo.widthFactor, i,
                        (int) (this.mLastMotionX - this.mInitialMotionX)), true, true, i);
        endDrag();
        this.mFakeDragging = false;
    }

    public boolean executeKeyEvent(KeyEvent paramKeyEvent) {
        boolean bool = false;
        if (paramKeyEvent.getAction() == 0)
            switch (paramKeyEvent.getKeyCode()) {
                default:
                case 21:
                case 22:
                case 61:
            }
        while (true) {
            return bool;
            bool = arrowScroll(17);
            continue;
            bool = arrowScroll(66);
            continue;
            if (Build.VERSION.SDK_INT >= 11)
                if (paramKeyEvent.hasNoModifiers())
                    bool = arrowScroll(2);
                else if (paramKeyEvent.hasModifiers(1))
                    bool = arrowScroll(1);
        }
    }

    public void fakeDragBy(float paramFloat) {
        if (!this.mFakeDragging)
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        this.mLastMotionX = (paramFloat + this.mLastMotionX);
        float f1 = getScrollX() - paramFloat;
        int i = getWidth();
        float f2 = i * this.mFirstOffset;
        float f3 = i * this.mLastOffset;
        ItemInfo localItemInfo1 = (ItemInfo) this.mItems.get(0);
        ItemInfo localItemInfo2 = (ItemInfo) this.mItems.get(-1 + this.mItems.size());
        if (localItemInfo1.position != 0)
            f2 = localItemInfo1.offset * i;
        if (localItemInfo2.position != -1 + this.mAdapter.getCount())
            f3 = localItemInfo2.offset * i;
        if (f1 < f2)
            f1 = f2;
        while (true) {
            this.mLastMotionX += f1 - (int) f1;
            scrollTo((int) f1, getScrollY());
            pageScrolled((int) f1, true);
            long l = SystemClock.uptimeMillis();
            MotionEvent localMotionEvent = MotionEvent.obtain(this.mFakeDragBeginTime, l, 2,
                    this.mLastMotionX, 0.0F, 0);
            this.mVelocityTracker.addMovement(localMotionEvent);
            localMotionEvent.recycle();
            return;
            if (f1 > f3)
                f1 = f3;
        }
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
        return new LayoutParams(getContext(), paramAttributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
        return generateDefaultLayoutParams();
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    boolean hasSplitActionBar() {
        return ActionBarView.findActionBarViewByView(this).isSplitActionBar();
    }

    ItemInfo infoForAnyChild(View paramView) {
        ViewParent localViewParent = paramView.getParent();
        if (localViewParent != this)
            if ((localViewParent != null) && ((localViewParent instanceof View)))
                ;
        for (ItemInfo localItemInfo = null;; localItemInfo = infoForChild(paramView)) {
            return localItemInfo;
            paramView = (View) localViewParent;
            break;
        }
    }

    ItemInfo infoForChild(View paramView) {
        int i = 0;
        ItemInfo localItemInfo;
        if (i < this.mItems.size()) {
            localItemInfo = (ItemInfo) this.mItems.get(i);
            if (!this.mAdapter.isViewFromObject(paramView, localItemInfo.object))
                ;
        }
        while (true) {
            return localItemInfo;
            i++;
            break;
            localItemInfo = null;
        }
    }

    ItemInfo infoForPosition(int paramInt) {
        int i = 0;
        ItemInfo localItemInfo;
        if (i < this.mItems.size()) {
            localItemInfo = (ItemInfo) this.mItems.get(i);
            if (localItemInfo.position != paramInt)
                ;
        }
        while (true) {
            return localItemInfo;
            i++;
            break;
            localItemInfo = null;
        }
    }

    void initViewPager() {
        setWillNotDraw(false);
        setDescendantFocusability(262144);
        setFocusable(true);
        Context localContext = getContext();
        this.mScroller = new Scroller(localContext, sInterpolator);
        ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
        this.mTouchSlop = localViewConfiguration.getScaledPagingTouchSlop();
        this.mMinimumVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect(localContext);
        this.mRightEdge = new EdgeEffect(localContext);
        float f = localContext.getResources().getDisplayMetrics().density;
        this.mFlingDistance = ((int) (25.0F * f));
        this.mCloseEnough = ((int) (2.0F * f));
        this.mDefaultGutterSize = ((int) (16.0F * f));
        if (getImportantForAccessibility() == 0)
            setImportantForAccessibility(1);
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int i;
    int j;
    float f1;
    int k;
    ItemInfo localItemInfo;
    float f2;
    int m;
    int n;
    int i1;
    if ((this.mPageMargin > 0) && (this.mMarginDrawable != null) && (this.mItems.size() > 0) && (this.mAdapter != null))
    {
      i = getScrollX();
      j = getWidth();
      f1 = this.mPageMargin / j;
      k = 0;
      localItemInfo = (ItemInfo)this.mItems.get(0);
      f2 = localItemInfo.offset;
      m = this.mItems.size();
      n = localItemInfo.position;
      i1 = ((ItemInfo)this.mItems.get(m - 1)).position;
    }
    for (int i2 = n; ; i2++)
    {
      float f4;
      if (i2 < i1)
      {
        while ((i2 > localItemInfo.position) && (k < m))
        {
          ArrayList localArrayList = this.mItems;
          k++;
          localItemInfo = (ItemInfo)localArrayList.get(k);
        }
        if (i2 != localItemInfo.position)
          break label272;
        f4 = (localItemInfo.offset + localItemInfo.widthFactor) * j;
      }
      label272: float f3;
      for (f2 = f1 + (localItemInfo.offset + localItemInfo.widthFactor); ; f2 += f3 + f1)
      {
        if (f4 + this.mPageMargin > i)
        {
          this.mMarginDrawable.setBounds((int)f4, this.mTopPageBounds, (int)(0.5F + (f4 + this.mPageMargin)), this.mBottomPageBounds);
          this.mMarginDrawable.draw(paramCanvas);
        }
        if (f4 <= i + j)
          break;
        return;
        f3 = this.mAdapter.getPageWidth(i2);
        f4 = (f2 + f3) * j;
      }
    }
  }

    public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
        super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
        paramAccessibilityEvent.setClassName(ViewPager.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
        int i = 1;
        super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
        paramAccessibilityNodeInfo.setClassName(ViewPager.class.getName());
        if ((this.mAdapter != null) && (this.mAdapter.getCount() > i))
            ;
        while (true) {
            paramAccessibilityNodeInfo.setScrollable(i);
            if ((this.mAdapter != null) && (this.mCurItem >= 0)
                    && (this.mCurItem < -1 + this.mAdapter.getCount()))
                paramAccessibilityNodeInfo.addAction(4096);
            if ((this.mAdapter != null) && (this.mCurItem > 0)
                    && (this.mCurItem < this.mAdapter.getCount()))
                paramAccessibilityNodeInfo.addAction(8192);
            return;
            int j = 0;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        boolean bool;
        if (!this.mDragEnabled)
            bool = false;
        int i;
        while (true) {
            return bool;
            i = 0xFF & paramMotionEvent.getAction();
            if ((i == 3) || (i == 1)) {
                this.mIsBeingDragged = false;
                this.mIsUnableToDrag = false;
                this.mActivePointerId = -1;
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                }
                bool = false;
            } else {
                if (i == 0)
                    break;
                if (this.mIsBeingDragged) {
                    bool = true;
                } else {
                    if (!this.mIsUnableToDrag)
                        break;
                    bool = false;
                }
            }
        }
        switch (i) {
            default:
            case 2:
            case 0:
            case 6:
        }
        while (true) {
            if (this.mVelocityTracker == null)
                this.mVelocityTracker = VelocityTracker.obtain();
            this.mVelocityTracker.addMovement(paramMotionEvent);
            bool = this.mIsBeingDragged;
            break;
            int j = this.mActivePointerId;
            if (j != -1) {
                int k = paramMotionEvent.findPointerIndex(j);
                if (k >= 0) {
                    float f2 = paramMotionEvent.getX(k);
                    float f3 = f2 - this.mLastMotionX;
                    float f4 = Math.abs(f3);
                    float f5 = paramMotionEvent.getY(k);
                    float f6 = Math.abs(f5 - this.mLastMotionY);
                    if ((f3 != 0.0F) && (!isGutterDrag(this.mLastMotionX, f3))
                            && (canScroll(this, false, (int) f3, (int) f2, (int) f5))) {
                        this.mLastMotionX = f2;
                        this.mInitialMotionX = f2;
                        this.mLastMotionY = f5;
                        this.mIsUnableToDrag = true;
                        bool = false;
                        break;
                    }
                    float f7;
                    if ((f4 > this.mTouchSlop) && (f4 > f6)) {
                        this.mIsBeingDragged = true;
                        setScrollState(1);
                        if (f3 > 0.0F) {
                            f7 = this.mInitialMotionX + this.mTouchSlop;
                            label345: this.mLastMotionX = f7;
                            setScrollingCacheEnabled(true);
                        }
                    }
                    while ((this.mIsBeingDragged) && (performDrag(f2))) {
                        postInvalidateOnAnimation();
                        break;
                        f7 = this.mInitialMotionX - this.mTouchSlop;
                        break label345;
                        if (f6 > this.mTouchSlop)
                            this.mIsUnableToDrag = true;
                    }
                    float f1 = paramMotionEvent.getX();
                    this.mInitialMotionX = f1;
                    this.mLastMotionX = f1;
                    this.mLastMotionY = paramMotionEvent.getY();
                    this.mActivePointerId = paramMotionEvent.getPointerId(0);
                    this.mIsUnableToDrag = false;
                    this.mScroller.computeScrollOffset();
                    if ((this.mScrollState == 2)
                            && (Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough)) {
                        this.mScroller.abortAnimation();
                        this.mPopulatePending = false;
                        populate();
                        this.mIsBeingDragged = true;
                        setScrollState(1);
                    } else {
                        completeScroll();
                        this.mIsBeingDragged = false;
                        continue;
                        onSecondaryPointerUp(paramMotionEvent);
                    }
                }
            }
        }
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mInLayout = true;
    populate();
    this.mInLayout = false;
    int i = getChildCount();
    int j = paramInt3 - paramInt1;
    int k = paramInt4 - paramInt2;
    int m = getPaddingLeft();
    int n = getPaddingTop();
    int i1 = getPaddingRight();
    int i2 = getPaddingBottom();
    int i3 = getScrollX();
    int i4 = 0;
    int i5 = 0;
    if (i5 < i)
    {
      View localView2 = getChildAt(i5);
      int i13;
      label172: int i14;
      if (localView2.getVisibility() != 8)
      {
        LayoutParams localLayoutParams2 = (LayoutParams)localView2.getLayoutParams();
        if (localLayoutParams2.isDecor)
        {
          int i11 = 0x7 & localLayoutParams2.gravity;
          int i12 = 0x70 & localLayoutParams2.gravity;
          switch (i11)
          {
          case 2:
          case 4:
          default:
            i13 = m;
            switch (i12)
            {
            default:
              i14 = n;
            case 48:
            case 16:
            case 80:
            }
            break;
          case 3:
          case 1:
          case 5:
          }
        }
      }
      while (true)
      {
        int i15 = i13 + i3;
        localView2.layout(i15, i14, i15 + localView2.getMeasuredWidth(), i14 + localView2.getMeasuredHeight());
        i4++;
        i5++;
        break;
        i13 = m;
        m += localView2.getMeasuredWidth();
        break label172;
        i13 = Math.max((j - localView2.getMeasuredWidth()) / 2, m);
        break label172;
        i13 = j - i1 - localView2.getMeasuredWidth();
        i1 += localView2.getMeasuredWidth();
        break label172;
        i14 = n;
        n += localView2.getMeasuredHeight();
        continue;
        i14 = Math.max((k - localView2.getMeasuredHeight()) / 2, n);
        continue;
        i14 = k - i2 - localView2.getMeasuredHeight();
        i2 += localView2.getMeasuredHeight();
      }
    }
    for (int i6 = 0; i6 < i; i6++)
    {
      View localView1 = getChildAt(i6);
      if (localView1.getVisibility() != 8)
      {
        LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
        if (!localLayoutParams1.isDecor)
        {
          ItemInfo localItemInfo = infoForChild(localView1);
          if (localItemInfo != null)
          {
            int i7 = m + (int)(j * localItemInfo.offset);
            int i8 = n;
            if (localLayoutParams1.needsMeasure)
            {
              localLayoutParams1.needsMeasure = false;
              int i9 = View.MeasureSpec.makeMeasureSpec((int)((j - m - i1) * localLayoutParams1.widthFactor), 1073741824);
              int i10 = 0;
              if ((localItemInfo.hasActionMenu) && (hasSplitActionBar()))
                i10 = UiUtils.getSplitActionMenuHeight(getContext()) - UiUtils.getSplitActionBarOverlayHeight(getContext());
              localView1.measure(i9, View.MeasureSpec.makeMeasureSpec(k - n - i2 - i10, 1073741824));
            }
            localView1.layout(i7, i8, i7 + localView1.getMeasuredWidth(), i8 + localView1.getMeasuredHeight());
          }
        }
      }
    }
    this.mTopPageBounds = n;
    this.mBottomPageBounds = (k - i2);
    this.mDecorChildCount = i4;
    this.mFirstLayout = false;
  }

    protected void onMeasure(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(getDefaultSize(0, paramInt1), getDefaultSize(0, paramInt2));
    int i = getMeasuredWidth();
    this.mGutterSize = Math.min(i / 10, this.mDefaultGutterSize);
    int j = i - getPaddingLeft() - getPaddingRight();
    int k = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    int m = getChildCount();
    int n = 0;
    if (n < m)
    {
      View localView2 = getChildAt(n);
      int i8;
      int i9;
      label167: int i10;
      if (localView2.getVisibility() != 8)
      {
        LayoutParams localLayoutParams2 = (LayoutParams)localView2.getLayoutParams();
        if ((localLayoutParams2 != null) && (localLayoutParams2.isDecor))
        {
          int i5 = 0x7 & localLayoutParams2.gravity;
          int i6 = 0x70 & localLayoutParams2.gravity;
          int i7 = -2147483648;
          i8 = -2147483648;
          if ((i6 != 48) && (i6 != 80))
            break label302;
          i9 = 1;
          if ((i5 != 3) && (i5 != 5))
            break label308;
          i10 = 1;
          label182: if (i9 == 0)
            break label314;
          i7 = 1073741824;
          label192: int i11 = j;
          int i12 = k;
          if (localLayoutParams2.width != -2)
          {
            i7 = 1073741824;
            if (localLayoutParams2.width != -1)
              i11 = localLayoutParams2.width;
          }
          if (localLayoutParams2.height != -2)
          {
            i8 = 1073741824;
            if (localLayoutParams2.height != -1)
              i12 = localLayoutParams2.height;
          }
          localView2.measure(View.MeasureSpec.makeMeasureSpec(i11, i7), View.MeasureSpec.makeMeasureSpec(i12, i8));
          if (i9 == 0)
            break label327;
          k -= localView2.getMeasuredHeight();
        }
      }
      while (true)
      {
        n++;
        break;
        label302: i9 = 0;
        break label167;
        label308: i10 = 0;
        break label182;
        label314: if (i10 == 0)
          break label192;
        i8 = 1073741824;
        break label192;
        label327: if (i10 != 0)
          j -= localView2.getMeasuredWidth();
      }
    }
    this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(j, 1073741824);
    this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(k, 1073741824);
    this.mInLayout = true;
    populate();
    this.mInLayout = false;
    int i1 = getChildCount();
    for (int i2 = 0; i2 < i1; i2++)
    {
      View localView1 = getChildAt(i2);
      if (localView1.getVisibility() != 8)
      {
        LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
        if ((localLayoutParams1 == null) || (!localLayoutParams1.isDecor))
        {
          int i3 = View.MeasureSpec.makeMeasureSpec((int)(j * localLayoutParams1.widthFactor), 1073741824);
          int i4 = k;
          if ((infoForChild(localView1).hasActionMenu) && (hasSplitActionBar()))
            i4 -= UiUtils.getSplitActionMenuHeight(getContext()) - UiUtils.getSplitActionBarOverlayHeight(getContext());
          localView1.measure(i3, View.MeasureSpec.makeMeasureSpec(i4, 1073741824));
        }
      }
    }
  }

    protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
        if (this.mDecorChildCount > 0) {
            int i = getScrollX();
            int j = getPaddingLeft();
            int k = getPaddingRight();
            int m = getWidth();
            int n = getChildCount();
            int i1 = 0;
            while (i1 < n) {
                View localView = getChildAt(i1);
                LayoutParams localLayoutParams = (LayoutParams) localView.getLayoutParams();
                if (!localLayoutParams.isDecor) {
                    i1++;
                } else {
                    int i2;
                    switch (0x7 & localLayoutParams.gravity) {
                        case 2:
                        case 4:
                        default:
                            i2 = j;
                        case 3:
                        case 1:
                        case 5:
                    }
                    while (true) {
                        int i3 = i2 + i - localView.getLeft();
                        if (i3 == 0)
                            break;
                        localView.offsetLeftAndRight(i3);
                        break;
                        i2 = j;
                        j += localView.getWidth();
                        continue;
                        i2 = Math.max((m - localView.getMeasuredWidth()) / 2, j);
                        continue;
                        i2 = m - k - localView.getMeasuredWidth();
                        k += localView.getMeasuredWidth();
                    }
                }
            }
        }
        if (this.mOnPageChangeListener != null)
            this.mOnPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
        if (this.mInternalPageChangeListener != null)
            this.mInternalPageChangeListener.onPageScrolled(paramInt1, paramFloat, paramInt2);
        this.mCalledSuper = true;
    }

    protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect) {
        int i = getChildCount();
        int j;
        int k;
        int m;
        int n;
        if ((paramInt & 0x2) != 0) {
            j = 0;
            k = 1;
            m = i;
            n = j;
            label24: if (n == m)
                break label112;
            View localView = getChildAt(n);
            if (localView.getVisibility() != 0)
                break label102;
            ItemInfo localItemInfo = infoForChild(localView);
            if ((localItemInfo == null) || (localItemInfo.position != this.mCurItem)
                    || (!localView.requestFocus(paramInt, paramRect)))
                break label102;
        }
        label102: label112: for (boolean bool = true;; bool = false) {
            return bool;
            j = i - 1;
            k = -1;
            m = -1;
            break;
            n += k;
            break label24;
        }
    }

    public void onRestoreInstanceState(Parcelable paramParcelable) {
        if (!(paramParcelable instanceof SavedState))
            super.onRestoreInstanceState(paramParcelable);
        while (true) {
            return;
            SavedState localSavedState = (SavedState) paramParcelable;
            super.onRestoreInstanceState(localSavedState.getSuperState());
            if (this.mAdapter != null) {
                this.mAdapter.restoreState(localSavedState.adapterState, null);
                setCurrentItemInternal(localSavedState.position, false, true);
            } else {
                this.mRestoredCurItem = localSavedState.position;
                this.mRestoredAdapterState = localSavedState.adapterState;
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState localSavedState = new SavedState(super.onSaveInstanceState());
        localSavedState.position = this.mCurItem;
        if (this.mAdapter != null)
            localSavedState.adapterState = this.mAdapter.saveState();
        return localSavedState;
    }

    protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
        if (paramInt1 != paramInt3)
            recomputeScrollPosition(paramInt1, paramInt3, this.mPageMargin, this.mPageMargin);
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        boolean bool1;
        if (!this.mDragEnabled)
            bool1 = true;
        while (true) {
            return bool1;
            if (this.mFakeDragging) {
                bool1 = true;
            } else if ((paramMotionEvent.getAction() == 0)
                    && (paramMotionEvent.getEdgeFlags() != 0)) {
                bool1 = false;
            } else {
                if ((this.mAdapter != null) && (this.mAdapter.getCount() != 0))
                    break;
                bool1 = false;
            }
        }
        if (this.mVelocityTracker == null)
            this.mVelocityTracker = VelocityTracker.obtain();
        this.mVelocityTracker.addMovement(paramMotionEvent);
        int i = paramMotionEvent.getAction();
        boolean bool2 = false;
        switch (i & 0xFF) {
            case 4:
            default:
            case 0:
            case 2:
            case 1:
            case 3:
            case 5:
            case 6:
        }
        while (true) {
            if (bool2)
                postInvalidateOnAnimation();
            bool1 = true;
            break;
            this.mScroller.abortAnimation();
            this.mPopulatePending = false;
            populate();
            this.mIsBeingDragged = true;
            setScrollState(1);
            float f5 = paramMotionEvent.getX();
            this.mInitialMotionX = f5;
            this.mLastMotionX = f5;
            this.mActivePointerId = paramMotionEvent.getPointerId(0);
            continue;
            if (!this.mIsBeingDragged) {
                int i1 = paramMotionEvent.findPointerIndex(this.mActivePointerId);
                float f1 = paramMotionEvent.getX(i1);
                float f2 = Math.abs(f1 - this.mLastMotionX);
                float f3 = Math.abs(paramMotionEvent.getY(i1) - this.mLastMotionY);
                if ((f2 > this.mTouchSlop) && (f2 > f3)) {
                    this.mIsBeingDragged = true;
                    if (f1 - this.mInitialMotionX <= 0.0F)
                        break label357;
                }
            }
            label357: for (float f4 = this.mInitialMotionX + this.mTouchSlop;; f4 = this.mInitialMotionX
                    - this.mTouchSlop) {
                this.mLastMotionX = f4;
                setScrollState(1);
                setScrollingCacheEnabled(true);
                if (!this.mIsBeingDragged)
                    break;
                bool2 = false | performDrag(paramMotionEvent.getX(paramMotionEvent
                        .findPointerIndex(this.mActivePointerId)));
                break;
            }
            if (this.mIsBeingDragged) {
                VelocityTracker localVelocityTracker = this.mVelocityTracker;
                localVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
                int k = (int) localVelocityTracker.getXVelocity(this.mActivePointerId);
                this.mPopulatePending = true;
                int m = getWidth();
                int n = getScrollX();
                ItemInfo localItemInfo = infoForCurrentScrollPosition();
                setCurrentItemInternal(
                        determineTargetPage(
                                localItemInfo.position,
                                (n / m - localItemInfo.offset) / localItemInfo.widthFactor,
                                k,
                                (int) (paramMotionEvent.getX(paramMotionEvent
                                        .findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)),
                        true, true, k);
                this.mActivePointerId = -1;
                endDrag();
                this.mLeftEdge.onRelease();
                this.mRightEdge.onRelease();
                bool2 = this.mLeftEdge.isFinished() | this.mRightEdge.isFinished();
                continue;
                if (this.mIsBeingDragged) {
                    setCurrentItemInternal(this.mCurItem, true, true);
                    this.mActivePointerId = -1;
                    endDrag();
                    this.mLeftEdge.onRelease();
                    this.mRightEdge.onRelease();
                    bool2 = this.mLeftEdge.isFinished() | this.mRightEdge.isFinished();
                    continue;
                    int j = paramMotionEvent.getActionIndex();
                    this.mLastMotionX = paramMotionEvent.getX(j);
                    this.mActivePointerId = paramMotionEvent.getPointerId(j);
                    continue;
                    onSecondaryPointerUp(paramMotionEvent);
                    this.mLastMotionX = paramMotionEvent.getX(paramMotionEvent
                            .findPointerIndex(this.mActivePointerId));
                }
            }
        }
    }

    boolean pageLeft() {
        boolean bool = true;
        if (this.mCurItem > 0)
            setCurrentItem(-1 + this.mCurItem, bool);
        while (true) {
            return bool;
            bool = false;
        }
    }

    boolean pageRight() {
        boolean bool = true;
        if ((this.mAdapter != null) && (this.mCurItem < -1 + this.mAdapter.getCount()))
            setCurrentItem(1 + this.mCurItem, bool);
        while (true) {
            return bool;
            bool = false;
        }
    }

    public boolean performAccessibilityAction(int paramInt, Bundle paramBundle) {
        boolean bool = true;
        if (super.performAccessibilityAction(paramInt, paramBundle))
            ;
        while (true) {
            return bool;
            switch (paramInt) {
                default:
                    bool = false;
                    break;
                case 4096:
                    if ((this.mAdapter != null) && (this.mCurItem >= 0)
                            && (this.mCurItem < -1 + this.mAdapter.getCount()))
                        setCurrentItem(1 + this.mCurItem);
                    else
                        bool = false;
                    break;
                case 8192:
                    if ((this.mAdapter != null) && (this.mCurItem > 0)
                            && (this.mCurItem < this.mAdapter.getCount()))
                        setCurrentItem(-1 + this.mCurItem);
                    else
                        bool = false;
                    break;
            }
        }
    }

    void populate() {
        populate(this.mCurItem);
    }

    void populate(int paramInt) {
        ItemInfo localItemInfo1 = null;
        if (this.mCurItem != paramInt) {
            localItemInfo1 = infoForPosition(this.mCurItem);
            this.mCurItem = paramInt;
        }
        if (this.mAdapter == null)
            break label31;
        label31: label210: label476: label1012: label1018: while (true) {
            return;
            if ((!this.mPopulatePending) && (getWindowToken() != null)) {
                this.mAdapter.startUpdate(this);
                int i = this.mOffscreenPageLimit;
                int j = Math.max(0, this.mCurItem - i);
                int k = this.mAdapter.getCount();
                int m = Math.min(k - 1, i + this.mCurItem);
                Object localObject1 = null;
                int n = 0;
                if (n < this.mItems.size()) {
                    ItemInfo localItemInfo8 = (ItemInfo) this.mItems.get(n);
                    if (localItemInfo8.position < this.mCurItem)
                        break label464;
                    if (localItemInfo8.position == this.mCurItem)
                        localObject1 = localItemInfo8;
                }
                if ((localObject1 == null) && (k > 0))
                    localObject1 = addNewItem(this.mCurItem, n);
                float f1;
                int i5;
                ItemInfo localItemInfo5;
                int i6;
                float f3;
                int i7;
                ItemInfo localItemInfo6;
                int i8;
                PagerAdapter localPagerAdapter1;
                int i1;
                if (localObject1 != null) {
                    f1 = 0.0F;
                    i5 = n - 1;
                    if (i5 >= 0) {
                        localItemInfo5 = (ItemInfo) this.mItems.get(i5);
                        float f2 = 2.0F - ((ItemInfo) localObject1).widthFactor;
                        i6 = -1 + this.mCurItem;
                        if (i6 >= 0) {
                            if ((f1 < f2) || (i6 >= j))
                                break label568;
                            if (localItemInfo5 != null)
                                break label476;
                        }
                        f3 = ((ItemInfo) localObject1).widthFactor;
                        i7 = n + 1;
                        if (f3 < 2.0F) {
                            if (i7 >= this.mItems.size())
                                break label681;
                            localItemInfo6 = (ItemInfo) this.mItems.get(i7);
                            i8 = 1 + this.mCurItem;
                            if (i8 < k) {
                                if ((f3 < 2.0F) || (i8 <= m))
                                    break label780;
                                if (localItemInfo6 != null)
                                    break label687;
                            }
                        }
                        calculatePageOffsets((ItemInfo) localObject1, n, localItemInfo1);
                    }
                } else {
                    localPagerAdapter1 = this.mAdapter;
                    i1 = this.mCurItem;
                    if (localObject1 == null)
                        break label905;
                }
                for (Object localObject2 = ((ItemInfo) localObject1).object;; localObject2 = null) {
                    localPagerAdapter1.setPrimaryItem(this, i1, localObject2);
                    this.mAdapter.finishUpdate(this);
                    int i2 = getChildCount();
                    for (int i3 = 0; i3 < i2; i3++) {
                        View localView3 = getChildAt(i3);
                        LayoutParams localLayoutParams = (LayoutParams) localView3
                                .getLayoutParams();
                        if ((!localLayoutParams.isDecor) && (localLayoutParams.widthFactor == 0.0F)) {
                            ItemInfo localItemInfo4 = infoForChild(localView3);
                            if (localItemInfo4 != null)
                                localLayoutParams.widthFactor = localItemInfo4.widthFactor;
                        }
                    }
                    n++;
                    break;
                    localItemInfo5 = null;
                    break label210;
                    int i13 = localItemInfo5.position;
                    if ((i6 == i13) && (!localItemInfo5.scrolling)) {
                        this.mItems.remove(i5);
                        PagerAdapter localPagerAdapter3 = this.mAdapter;
                        Object localObject4 = localItemInfo5.object;
                        localPagerAdapter3.destroyItem(this, i6, localObject4);
                        i5--;
                        n--;
                        if (i5 < 0)
                            break label562;
                    }
                    for (localItemInfo5 = (ItemInfo) this.mItems.get(i5);; localItemInfo5 = null) {
                        i6--;
                        break;
                    }
                    if (localItemInfo5 != null) {
                        int i12 = localItemInfo5.position;
                        if (i6 == i12) {
                            f1 += localItemInfo5.widthFactor;
                            i5--;
                            if (i5 >= 0)
                                ;
                            for (localItemInfo5 = (ItemInfo) this.mItems.get(i5);; localItemInfo5 = null)
                                break;
                        }
                    }
                    int i11 = i5 + 1;
                    f1 += addNewItem(i6, i11).widthFactor;
                    n++;
                    if (i5 >= 0)
                        ;
                    for (localItemInfo5 = (ItemInfo) this.mItems.get(i5);; localItemInfo5 = null)
                        break;
                    localItemInfo6 = null;
                    break label298;
                    int i10 = localItemInfo6.position;
                    if ((i8 == i10) && (!localItemInfo6.scrolling)) {
                        this.mItems.remove(i7);
                        PagerAdapter localPagerAdapter2 = this.mAdapter;
                        Object localObject3 = localItemInfo6.object;
                        localPagerAdapter2.destroyItem(this, i8, localObject3);
                        if (i7 >= this.mItems.size())
                            break label774;
                    }
                    for (localItemInfo6 = (ItemInfo) this.mItems.get(i7);; localItemInfo6 = null) {
                        i8++;
                        break;
                    }
                    if (localItemInfo6 != null) {
                        int i9 = localItemInfo6.position;
                        if (i8 == i9) {
                            f3 += localItemInfo6.widthFactor;
                            i7++;
                            if (i7 < this.mItems.size())
                                ;
                            for (localItemInfo6 = (ItemInfo) this.mItems.get(i7);; localItemInfo6 = null)
                                break;
                        }
                    }
                    ItemInfo localItemInfo7 = addNewItem(i8, i7);
                    i7++;
                    f3 += localItemInfo7.widthFactor;
                    if (i7 < this.mItems.size())
                        ;
                    for (localItemInfo6 = (ItemInfo) this.mItems.get(i7);; localItemInfo6 = null)
                        break;
                }
                if (!hasFocus())
                    break;
                View localView1 = findFocus();
                if (localView1 != null)
                    ;
                for (ItemInfo localItemInfo2 = infoForAnyChild(localView1);; localItemInfo2 = null) {
                    if ((localItemInfo2 != null) && (localItemInfo2.position == this.mCurItem))
                        break label1018;
                    for (int i4 = 0;; i4++) {
                        if (i4 >= getChildCount())
                            break label1012;
                        View localView2 = getChildAt(i4);
                        ItemInfo localItemInfo3 = infoForChild(localView2);
                        if ((localItemInfo3 != null) && (localItemInfo3.position == this.mCurItem)
                                && (localView2.requestFocus(2)))
                            break;
                    }
                    break label31;
                }
            }
        }
    }

    public void setAdapter(PagerAdapter paramPagerAdapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate(this);
            for (int i = 0; i < this.mItems.size(); i++) {
                ItemInfo localItemInfo = (ItemInfo) this.mItems.get(i);
                this.mAdapter.destroyItem(this, localItemInfo.position, localItemInfo.object);
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            removeNonDecorViews();
            this.mCurItem = 0;
            scrollTo(0, 0);
        }
        PagerAdapter localPagerAdapter = this.mAdapter;
        this.mAdapter = paramPagerAdapter;
        if (this.mAdapter != null) {
            if (this.mObserver == null)
                this.mObserver = new PagerObserver(null);
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            this.mFirstLayout = true;
            if (this.mRestoredCurItem < 0)
                break label227;
            this.mAdapter.restoreState(this.mRestoredAdapterState, null);
            setCurrentItemInternal(this.mRestoredCurItem, false, true);
            this.mRestoredCurItem = -1;
            this.mRestoredAdapterState = null;
        }
        while (true) {
            if ((this.mAdapterChangeListener != null) && (localPagerAdapter != paramPagerAdapter))
                this.mAdapterChangeListener.onAdapterChanged(localPagerAdapter, paramPagerAdapter);
            return;
            label227: populate();
        }
    }

    public void setCurrentItem(int paramInt) {
        this.mPopulatePending = false;
        if (!this.mFirstLayout)
            ;
        for (boolean bool = true;; bool = false) {
            setCurrentItemInternal(paramInt, bool, false);
            return;
        }
    }

    public void setCurrentItem(int paramInt, boolean paramBoolean) {
        this.mPopulatePending = false;
        setCurrentItemInternal(paramInt, paramBoolean, false);
    }

    void setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
        setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0);
    }

    void setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2,
            int paramInt2) {
        boolean bool = true;
        if ((this.mAdapter == null) || (this.mAdapter.getCount() <= 0))
            setScrollingCacheEnabled(false);
        while (true) {
            return;
            if ((!paramBoolean2) && (this.mCurItem == paramInt1) && (this.mItems.size() != 0)) {
                setScrollingCacheEnabled(false);
            } else {
                if (paramInt1 < 0)
                    paramInt1 = 0;
                while (true) {
                    int i = this.mOffscreenPageLimit;
                    if ((paramInt1 <= i + this.mCurItem) && (paramInt1 >= this.mCurItem - i))
                        break;
                    for (int j = 0; j < this.mItems.size(); j++)
                        ((ItemInfo) this.mItems.get(j)).scrolling = bool;
                    if (paramInt1 >= this.mAdapter.getCount())
                        paramInt1 = -1 + this.mAdapter.getCount();
                }
                if (this.mCurItem != paramInt1)
                    ;
                int k;
                while (true) {
                    populate(paramInt1);
                    ItemInfo localItemInfo = infoForPosition(paramInt1);
                    k = 0;
                    if (localItemInfo != null)
                        k = (int) (getWidth() * Math.max(this.mFirstOffset,
                                Math.min(localItemInfo.offset, this.mLastOffset)));
                    if (!paramBoolean1)
                        break label274;
                    smoothScrollTo(k, 0, paramInt2);
                    if ((bool) && (this.mOnPageChangeListener != null))
                        this.mOnPageChangeListener.onPageSelected(paramInt1);
                    if ((!bool) || (this.mInternalPageChangeListener == null))
                        break;
                    this.mInternalPageChangeListener.onPageSelected(paramInt1);
                    break;
                    bool = false;
                }
                label274: if ((bool) && (this.mOnPageChangeListener != null))
                    this.mOnPageChangeListener.onPageSelected(paramInt1);
                if ((bool) && (this.mInternalPageChangeListener != null))
                    this.mInternalPageChangeListener.onPageSelected(paramInt1);
                completeScroll();
                scrollTo(k, 0);
            }
        }
    }

    public void setDraggable(boolean paramBoolean) {
        this.mDragEnabled = paramBoolean;
    }

    public OnPageChangeListener setInternalPageChangeListener(
            OnPageChangeListener paramOnPageChangeListener) {
        OnPageChangeListener localOnPageChangeListener = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = paramOnPageChangeListener;
        return localOnPageChangeListener;
    }

    public void setOffscreenPageLimit(int paramInt) {
        if (paramInt < 3) {
            Log.w("ViewPager", "Requested offscreen page limit " + paramInt
                    + " too small; defaulting to " + 3);
            paramInt = 3;
        }
        if (paramInt != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = paramInt;
            populate();
        }
    }

    void setOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener) {
        this.mAdapterChangeListener = paramOnAdapterChangeListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener) {
        this.mOnPageChangeListener = paramOnPageChangeListener;
    }

    public void setPageMargin(int paramInt) {
        int i = this.mPageMargin;
        this.mPageMargin = paramInt;
        int j = getWidth();
        recomputeScrollPosition(j, j, paramInt, i);
        requestLayout();
    }

    public void setPageMarginDrawable(int paramInt) {
        setPageMarginDrawable(getContext().getResources().getDrawable(paramInt));
    }

    public void setPageMarginDrawable(Drawable paramDrawable) {
        this.mMarginDrawable = paramDrawable;
        if (paramDrawable != null)
            refreshDrawableState();
        if (paramDrawable == null)
            ;
        for (boolean bool = true;; bool = false) {
            setWillNotDraw(bool);
            invalidate();
            return;
        }
    }

    void smoothScrollTo(int paramInt1, int paramInt2) {
        smoothScrollTo(paramInt1, paramInt2, 0);
    }

    void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3) {
        if (getChildCount() == 0)
            setScrollingCacheEnabled(false);
        int i;
        int j;
        int k;
        int m;
        while (true) {
            return;
            i = getScrollX();
            j = getScrollY();
            k = paramInt1 - i;
            m = paramInt2 - j;
            if ((k != 0) || (m != 0))
                break;
            completeScroll();
            populate();
            setScrollState(0);
        }
        setScrollingCacheEnabled(true);
        setScrollState(2);
        int n = getWidth();
        int i1 = n / 2;
        float f1 = Math.min(1.0F, 1.0F * Math.abs(k) / n);
        float f2 = i1 + i1 * distanceInfluenceForSnapDuration(f1);
        int i2 = Math.abs(paramInt3);
        if (i2 > 0)
            ;
        float f3;
        for (int i3 = 4 * Math.round(1000.0F * Math.abs(f2 / i2));; i3 = (int) (250.0F * (1.0F + Math
                .abs(k) / (f3 + this.mPageMargin)))) {
            int i4 = Math.min(i3, 800);
            this.mScroller.startScroll(i, j, k, m, i4);
            postInvalidateOnAnimation();
            break;
            f3 = n * this.mAdapter.getPageWidth(this.mCurItem);
        }
    }

    protected boolean verifyDrawable(Drawable paramDrawable) {
        if ((super.verifyDrawable(paramDrawable)) || (paramDrawable == this.mMarginDrawable))
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int gravity;
        public boolean isDecor;
        public boolean needsMeasure;
        public float widthFactor = 0.0F;

        public LayoutParams() {
            super(-1);
        }

        public LayoutParams(Context paramContext, AttributeSet paramAttributeSet) {
            super(paramAttributeSet);
            TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet,
                    ViewPager.LAYOUT_ATTRS);
            this.gravity = localTypedArray.getInteger(0, 48);
            localTypedArray.recycle();
        }
    }

    static abstract interface OnAdapterChangeListener {
        public abstract void onAdapterChanged(PagerAdapter paramPagerAdapter1,
                PagerAdapter paramPagerAdapter2);
    }

    public static abstract interface OnPageChangeListener {
        public abstract void onPageScrollStateChanged(int paramInt);

        public abstract void onPageScrolled(int paramInt1, float paramFloat, int paramInt2);

        public abstract void onPageSelected(int paramInt);
    }

    private class PagerObserver extends DataSetObserver {
        private PagerObserver() {
        }

        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator() {
            public ViewPager.SavedState createFromParcel(Parcel paramAnonymousParcel) {
                return new ViewPager.SavedState(paramAnonymousParcel);
            }

            public ViewPager.SavedState[] newArray(int paramAnonymousInt) {
                return new ViewPager.SavedState[paramAnonymousInt];
            }
        };
        Parcelable adapterState;
        int position;

        SavedState(Parcel paramParcel) {
            super();
            this.position = paramParcel.readInt();
            this.adapterState = paramParcel.readParcelable(null);
        }

        public SavedState(Parcelable paramParcelable) {
            super();
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this))
                    + " position=" + this.position + "}";
        }

        public void writeToParcel(Parcel paramParcel, int paramInt) {
            super.writeToParcel(paramParcel, paramInt);
            paramParcel.writeInt(this.position);
            paramParcel.writeParcelable(this.adapterState, paramInt);
        }
    }

    public static class SimpleOnPageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int paramInt) {
        }

        public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
        }

        public void onPageSelected(int paramInt) {
        }
    }
}
