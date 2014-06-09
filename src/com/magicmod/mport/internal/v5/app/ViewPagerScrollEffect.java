
package com.magicmod.mport.internal.v5.app;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import com.magicmod.mport.v5.app.MiuiActionBar;

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

    public ViewPagerScrollEffect(ViewPager paramViewPager,
            DynamicFragmentPagerAdapter paramDynamicFragmentPagerAdapter) {
        this.mViewPager = paramViewPager;
        this.mPagerAdapter = paramDynamicFragmentPagerAdapter;
    }

    void clearTranslation(ViewGroup paramViewGroup) {
        fillList(paramViewGroup, this.sList);
        if (!this.sList.isEmpty()) {
            Iterator localIterator = this.sList.iterator();
            while (localIterator.hasNext())
                ((View) localIterator.next()).setTranslationX(0.0F);
        }
    }

    void clearTranslation(ArrayList<View> paramArrayList, ViewGroup paramViewGroup) {
        for (int i = 0; i < paramArrayList.size(); i++) {
            View localView = (View) paramArrayList.get(i);
            if ((paramViewGroup.indexOfChild(localView) == -1)
                    && (localView.getTranslationX() != 0.0F))
                localView.setTranslationX(0.0F);
        }
    }

    int computOffset(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
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
    }

    void fillList(ViewGroup paramViewGroup, ArrayList<View> paramArrayList) {
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
    }

    public void onPageScrollStateChanged(int paramInt) {
        if (paramInt == 0) {
            this.mBaseItem = this.mViewPager.getCurrentItem();
            this.mBaseItemUpdated = true;
        }
    }

    public void onPageScrolled(int paramInt, float paramFloat, boolean paramBoolean1, boolean paramBoolean2)
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
    }

    public void onPageSelected(int paramInt) {
    }

    void translateView(ViewGroup paramViewGroup, int paramInt1, int paramInt2, float paramFloat,
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
    }
}
