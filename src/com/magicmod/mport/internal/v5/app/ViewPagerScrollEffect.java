
package com.magicmod.mport.internal.v5.app;

import android.app.Fragment;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import com.android.internal.R;
import com.android.internal.R.integer;
import com.magicmod.mport.v5.app.MiuiActionBar;
import com.magicmod.mport.v5.view.ViewPager;
import com.magicmod.mport.v5.widget.Views;

import java.util.ArrayList;
import java.util.Iterator;

public class ViewPagerScrollEffect implements MiuiActionBar.FragmentViewPagerChangeListener {
    int mBaseItem = -1;
    boolean mBaseItemUpdated = true;
    int mIncomingPosition = -1;
    ViewGroup mListView = null;
    DynamicFragmentPagerAdapter mPagerAdapter;
    int mScrollBasePosition = -1;
    int mState = 0;
    ViewPager mViewPager;
    ArrayList<View> sList = new ArrayList();
    Rect sRect = new Rect();

    public ViewPagerScrollEffect(ViewPager viewPager,
            DynamicFragmentPagerAdapter pagerAdapter) {
        this.mViewPager = viewPager;
        this.mPagerAdapter = pagerAdapter;
    }

    void clearTranslation(ViewGroup view) {
        fillList(view, this.sList);
        if (!this.sList.isEmpty()) {
            // Iterator localIterator = this.sList.iterator();
            // while (localIterator.hasNext())
            // ((View) localIterator.next()).setTranslationX(0.0F);
            for (Iterator i = sList.iterator(); i.hasNext(); i.next()) {
                ((View) i).setTranslationX(0.0F);
            }
        }
    }

    void clearTranslation(ArrayList<View> list, ViewGroup viewGroup) {
        for (int i = 0; i < list.size(); i++) {
            View child = (View) list.get(i);
            if ((viewGroup.indexOfChild(child) == -1) && (child.getTranslationX() != 0.0F))
                child.setTranslationX(0.0F);
        }
    }

    /*int computOffset(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
        int i;
        float f2;
        if (paramInt1 < paramInt3) {
            i = paramInt1 * paramInt2 / paramInt3;
            float f1 = paramFloat * paramFloat;
            f2 = i + (0.1F - f1 / 0.9F) * paramInt2;
            if (f2 <= 0.0F)
                break label57;
        }
        label57: for (int j = (int) f2;; j = 0) {
            return j;
            i = paramInt2;
            break;
        }
    }*/
    int computOffset(int top, int width, int height, float percent) {
        int indent;
        float coeff;
        float offset;
        int i1;
        if (top < height)
            indent = (top * width) / height;
        else
            indent = width;
        coeff = percent * percent;
        offset = (float) indent + (0.1F - coeff / 0.9F) * (float) width;
        if (offset > 0.0F)
            i1 = (int) offset;
        else
            i1 = 0;
        return i1;
    }

    /*void fillList(ViewGroup paramViewGroup, ArrayList<View> paramArrayList) {
        clearTranslation(paramArrayList, paramViewGroup);
        paramArrayList.clear();
        Views.getContentRect(paramViewGroup, this.sRect);
        if (this.sRect.isEmpty())
            ;
        while (true) {
            return;
            int i = paramViewGroup.getChildCount();
            for (int j = 0; j < i; j++) {
                View localView = paramViewGroup.getChildAt(j);
                if ((localView.getVisibility() != 8) || (localView.getHeight() > 0))
                    paramArrayList.add(localView);
            }
        }
    }*/
    void fillList(ViewGroup view, ArrayList<View> list) {
        clearTranslation(list, view);
        list.clear();
        Views.getContentRect(view, sRect);
        if (!sRect.isEmpty()) {
            int count = view.getChildCount();
            int i = 0;
            while (i < count) {
                View child = view.getChildAt(i);
                if (child.getVisibility() != 8 || child.getHeight() > 0)
                    list.add(child);
                i++;
            }
        }
    }

    public void onPageScrollStateChanged(int state) {
        if (state == 0) {
            this.mBaseItem = this.mViewPager.getCurrentItem();
            this.mBaseItemUpdated = true;
        }
    }

    /*public void onPageScrolled(int paramInt, float paramFloat, boolean paramBoolean1, boolean paramBoolean2)
    {
      boolean bool = true;
      if (paramFloat == 0.0F)
      {
        this.mBaseItem = paramInt;
        this.mBaseItemUpdated = bool;
        if (this.mListView != null)
          clearTranslation(this.mListView);
      }
      if (this.mScrollBasePosition != paramInt)
      {
        if (this.mBaseItem >= paramInt)
          break label238;
        this.mBaseItem = paramInt;
        this.mScrollBasePosition = paramInt;
        this.mBaseItemUpdated = bool;
        if (this.mListView != null)
          clearTranslation(this.mListView);
      }
      float f;
      label118: ViewGroup localViewGroup;
      int i;
      int j;
      if (paramFloat > 0.0F)
      {
        f = paramFloat;
        if (this.mBaseItemUpdated)
        {
          this.mBaseItemUpdated = false;
          if (this.mBaseItem != paramInt)
            break label258;
          this.mIncomingPosition = (paramInt + 1);
          Fragment localFragment = this.mPagerAdapter.getFragment(this.mIncomingPosition, false);
          this.mListView = null;
          if (localFragment != null)
          {
            View localView = localFragment.getView().findViewById(16908298);
            if ((localView instanceof ViewGroup))
              this.mListView = ((ViewGroup)localView);
          }
        }
        if (this.mIncomingPosition == paramInt)
          f = 1.0F - paramFloat;
        if (this.mListView != null)
        {
          localViewGroup = this.mListView;
          i = this.mListView.getWidth();
          j = this.mListView.getHeight();
          if (this.mIncomingPosition == paramInt)
            break label266;
        }
      }
      while (true)
      {
        translateView(localViewGroup, i, j, f, bool);
        return;
        label238: if (this.mBaseItem <= paramInt + 1)
          break;
        this.mBaseItem = (paramInt + 1);
        break;
        label258: this.mIncomingPosition = paramInt;
        break label118;
        label266: bool = false;
      }
    }*/
    public void onPageScrolled(int position, float ratio, boolean fromHasActionMenu, boolean toHasActionMenu) {
        boolean flag2;
        flag2 = true;
        if (ratio == 0.0F) {
            mBaseItem = position;
            mBaseItemUpdated = flag2;
            if (mListView != null)
                clearTranslation(mListView);
        }
        if (mScrollBasePosition == position) { // goto _L2;
            if (ratio > 0.0F) {
                float currentRatio = ratio;
                if (mBaseItemUpdated) {
                    mBaseItemUpdated = false;
                    //ViewGroup viewgroup;
                    //int j;
                    //int k;
                    Fragment incoming;
                    if (mBaseItem == position)
                        mIncomingPosition = position + 1;
                    else
                        mIncomingPosition = position;
                    incoming = mPagerAdapter.getFragment(mIncomingPosition, false);
                    mListView = null;
                    if (incoming != null) {
                        View list = incoming.getView().findViewById(/*0x102000a*/R.id.list);
                        if (list instanceof ViewGroup)
                            mListView = (ViewGroup) list;
                    }
                }
                if (mIncomingPosition == position)
                    currentRatio = 1.0F - ratio;
                if (mListView != null) {
                    // viewgroup = mListView;
                    // j = mListView.getWidth();
                    // k = mListView.getHeight();
                    if (mIncomingPosition == position)
                        flag2 = false;
                    translateView(mListView, mListView.getWidth(), mListView.getHeight(), currentRatio, flag2);
                }
            }
            return;

        } else { // goto _L1
            if (mBaseItem >= position) { // goto _L4;
                if (mBaseItem > position + 1)
                    mBaseItem = position + 1;
                // if(true) goto _L6; else goto _L5
                mScrollBasePosition = position;
                mBaseItemUpdated = flag2;
                if (mListView != null)
                    clearTranslation(mListView);
            } else { // goto _L3
                mBaseItem = position;
            }
        }
      //      goto _L2; else goto _L1
/*_L1:
        if(mBaseItem >= i) goto _L4; else goto _L3
_L3:
        mBaseItem = i;
_L6:
        mScrollBasePosition = i;
        mBaseItemUpdated = flag2;
        if(mListView != null)
            clearTranslation(mListView);
_L2:
        if(f > 0.0F)
        {
            float f1 = f;
            if(mBaseItemUpdated)
            {
                mBaseItemUpdated = false;
                ViewGroup viewgroup;
                int j;
                int k;
                Fragment fragment;
                if(mBaseItem == i)
                    mIncomingPosition = i + 1;
                else
                    mIncomingPosition = i;
                fragment = mPagerAdapter.getFragment(mIncomingPosition, false);
                mListView = null;
                if(fragment != null)
                {
                    View view = fragment.getView().findViewById(0x102000a);
                    if(view instanceof ViewGroup)
                        mListView = (ViewGroup)view;
                }
            }
            if(mIncomingPosition == i)
                f1 = 1.0F - f;
            if(mListView != null)
            {
                viewgroup = mListView;
                j = mListView.getWidth();
                k = mListView.getHeight();
                if(mIncomingPosition == i)
                    flag2 = false;
                translateView(viewgroup, j, k, f1, flag2);
            }
        }
        return;
_L4:
        if(mBaseItem > i + 1)
            mBaseItem = i + 1;
        if(true) goto _L6; else goto _L5
_L5:*/
    }

    public void onPageSelected(int position) {
    }

    /*void translateView(ViewGroup paramViewGroup, int paramInt1, int paramInt2, float paramFloat,
            boolean paramBoolean) {
        fillList(paramViewGroup, this.sList);
        if (!this.sList.isEmpty()) {
            int i = ((View) this.sList.get(0)).getTop();
            int j = 2147483647;
            int k = 0;
            Iterator localIterator = this.sList.iterator();
            if (localIterator.hasNext()) {
                View localView = (View) localIterator.next();
                int m;
                if (j != localView.getTop()) {
                    j = localView.getTop();
                    m = computOffset(j - i, paramInt1, paramInt2, paramFloat);
                    if (!paramBoolean)
                        break label125;
                }
                label125: for (k = m;; k = -m) {
                    localView.setTranslationX(k);
                    break;
                }
            }
        }
    }*/
    void translateView(ViewGroup view, int width, int height, float percent, boolean isRight) {
        fillList(view, sList);
        if (!sList.isEmpty()) {
            int koffset = ((View) sList.get(0)).getTop();
            int lastTop = Integer.MAX_VALUE;//0x7fffffff;
            int lastDelta = 0;
            Iterator i = sList.iterator();
            while (i.hasNext()) {
                View v = (View) i.next();
                if (lastTop != v.getTop()) {
                    lastTop = v.getTop();
                    int distance = computOffset(lastTop - koffset, width, height, percent);
                    if (isRight)
                        lastDelta = distance;
                    else
                        lastDelta = -distance;
                }
                v.setTranslationX(lastDelta);
            }
        }
    }
}
