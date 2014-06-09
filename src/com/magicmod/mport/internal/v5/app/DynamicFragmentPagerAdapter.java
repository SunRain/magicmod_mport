
package com.magicmod.mport.internal.v5.app;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.magicmod.mport.v5.android.support.view.PagerAdapter;
import com.magicmod.mport.v5.android.support.view.ViewPager;

import java.util.ArrayList;

class DynamicFragmentPagerAdapter extends PagerAdapter {
    private Context mContext;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    private ArrayList<FragmentInfo> mFragmentInfos = new ArrayList();
    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;

    public DynamicFragmentPagerAdapter(Context paramContext, FragmentManager paramFragmentManager,
            ViewPager paramViewPager) {
        this.mContext = paramContext;
        this.mFragmentManager = paramFragmentManager;
        this.mViewPager = paramViewPager;
        this.mViewPager.setAdapter(this);
    }

    private void removeAllFragmentFromManager() {
        FragmentTransaction localFragmentTransaction = this.mFragmentManager.beginTransaction();
        int i = this.mFragmentInfos.size();
        for (int j = 0; j < i; j++)
            localFragmentTransaction.remove(getFragment(j, false));
        localFragmentTransaction.commitAllowingStateLoss();
        this.mFragmentManager.executePendingTransactions();
    }

    private void removeFragmentFromManager(Fragment paramFragment) {
        if (paramFragment != null) {
            FragmentManager localFragmentManager = paramFragment.getFragmentManager();
            if (localFragmentManager != null) {
                FragmentTransaction localFragmentTransaction = localFragmentManager
                        .beginTransaction();
                localFragmentTransaction.remove(paramFragment);
                localFragmentTransaction.commitAllowingStateLoss();
                localFragmentManager.executePendingTransactions();
            }
        }
    }

    int addFragment(String paramString, int paramInt, Class<? extends Fragment> paramClass,
            Bundle paramBundle, ActionBar.Tab paramTab, boolean paramBoolean) {
        this.mFragmentInfos.add(paramInt, new FragmentInfo(paramString, paramClass, paramBundle,
                paramTab, paramBoolean));
        notifyDataSetChanged();
        return paramInt;
    }

    int addFragment(String paramString, Class<? extends Fragment> paramClass, Bundle paramBundle,
            ActionBar.Tab paramTab, boolean paramBoolean) {
        this.mFragmentInfos.add(new FragmentInfo(paramString, paramClass, paramBundle, paramTab,
                paramBoolean));
        notifyDataSetChanged();
        return -1 + this.mFragmentInfos.size();
    }

    public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
        if (this.mCurTransaction == null)
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        this.mCurTransaction.detach((Fragment) paramObject);
    }

    int findPositionByTag(String paramString) {
        int i = this.mFragmentInfos.size();
        int j = 0;
        if (j < i)
            if (!((FragmentInfo) this.mFragmentInfos.get(j)).tag.equals(paramString))
                ;
        while (true) {
            return j;
            j++;
            break;
            j = -1;
        }
    }

    public void finishUpdate(ViewGroup paramViewGroup) {
        if (this.mCurTransaction != null) {
            this.mCurTransaction.commitAllowingStateLoss();
            this.mCurTransaction = null;
            this.mFragmentManager.executePendingTransactions();
        }
    }

    public int getCount() {
        return this.mFragmentInfos.size();
    }

    Fragment getFragment(int paramInt, boolean paramBoolean) {
        FragmentInfo localFragmentInfo = (FragmentInfo) this.mFragmentInfos.get(paramInt);
        if (localFragmentInfo.fragment == null) {
            localFragmentInfo.fragment = this.mFragmentManager
                    .findFragmentByTag(localFragmentInfo.tag);
            if ((localFragmentInfo.fragment == null) && (paramBoolean)) {
                localFragmentInfo.fragment = Fragment.instantiate(this.mContext,
                        localFragmentInfo.clazz.getName(), localFragmentInfo.args);
                localFragmentInfo.clazz = null;
                localFragmentInfo.args = null;
            }
        }
        return localFragmentInfo.fragment;
    }

    public int getItemPosition(Object paramObject) {
        int i = this.mFragmentInfos.size();
        int j = 0;
        if (j < i)
            if (paramObject != ((FragmentInfo) this.mFragmentInfos.get(j)).fragment)
                ;
        while (true) {
            return j;
            j++;
            break;
            j = -2;
        }
    }

    ActionBar.Tab getTabAt(int paramInt) {
        return ((FragmentInfo) this.mFragmentInfos.get(paramInt)).tab;
    }

    public boolean hasActionMenu(int paramInt) {
        return ((FragmentInfo) this.mFragmentInfos.get(paramInt)).hasActionMenu;
    }

    public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
        if (this.mCurTransaction == null)
            this.mCurTransaction = this.mFragmentManager.beginTransaction();
        Fragment localFragment = getFragment(paramInt, true);
        if (localFragment.getFragmentManager() != null)
            this.mCurTransaction.attach(localFragment);
        while (true) {
            if (localFragment != this.mCurrentPrimaryItem) {
                localFragment.setMenuVisibility(false);
                localFragment.setUserVisibleHint(false);
            }
            return localFragment;
            this.mCurTransaction.add(paramViewGroup.getId(), localFragment,
                    ((FragmentInfo) this.mFragmentInfos.get(paramInt)).tag);
        }
    }

    public boolean isViewFromObject(View paramView, Object paramObject) {
        if (((Fragment) paramObject).getView() == paramView)
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    void removeAllFragment() {
        removeAllFragmentFromManager();
        this.mFragmentInfos.clear();
        notifyDataSetChanged();
    }

    int removeFragment(ActionBar.Tab paramTab) {
        int i = this.mFragmentInfos.size();
        int j = 0;
        if (j < i) {
            FragmentInfo localFragmentInfo = (FragmentInfo) this.mFragmentInfos.get(j);
            if (localFragmentInfo.tab == paramTab) {
                removeFragmentFromManager(localFragmentInfo.fragment);
                this.mFragmentInfos.remove(j);
                notifyDataSetChanged();
            }
        }
        while (true) {
            return j;
            j++;
            break;
            j = -1;
        }
    }

    int removeFragment(Fragment paramFragment) {
        int i = this.mFragmentInfos.size();
        int j = 0;
        if (j < i)
            if (getFragment(j, false) == paramFragment) {
                removeFragmentFromManager(paramFragment);
                this.mFragmentInfos.remove(j);
                notifyDataSetChanged();
            }
        while (true) {
            return j;
            j++;
            break;
            j = -1;
        }
    }

    void removeFragmentAt(int paramInt) {
        removeFragmentFromManager(getFragment(paramInt, false));
        this.mFragmentInfos.remove(paramInt);
        notifyDataSetChanged();
    }

    void setFragmentActionMenuAt(int paramInt, boolean paramBoolean) {
        FragmentInfo localFragmentInfo = (FragmentInfo) this.mFragmentInfos.get(paramInt);
        if (localFragmentInfo.hasActionMenu != paramBoolean) {
            localFragmentInfo.hasActionMenu = paramBoolean;
            notifyDataSetChanged();
        }
    }

    public void setPrimaryItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
        Fragment localFragment = (Fragment) paramObject;
        if (localFragment != this.mCurrentPrimaryItem) {
            if (this.mCurrentPrimaryItem != null) {
                this.mCurrentPrimaryItem.setMenuVisibility(false);
                this.mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (localFragment != null) {
                localFragment.setMenuVisibility(true);
                localFragment.setUserVisibleHint(true);
            }
            this.mCurrentPrimaryItem = localFragment;
        }
    }

    public void startUpdate(ViewGroup paramViewGroup) {
    }

    class FragmentInfo {
        Bundle args;
        Class<? extends Fragment> clazz;
        Fragment fragment;
        boolean hasActionMenu;
        ActionBar.Tab tab;
        String tag;

        FragmentInfo(Class<? extends Fragment> paramBundle, Bundle paramTab,
                ActionBar.Tab paramBoolean, boolean arg5) {
            this.tag = paramBundle;
            this.clazz = paramTab;
            this.fragment = null;
            this.args = paramBoolean;
            Object localObject;
            this.tab = localObject;
            boolean bool;
            this.hasActionMenu = bool;
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return false;
    }
}
