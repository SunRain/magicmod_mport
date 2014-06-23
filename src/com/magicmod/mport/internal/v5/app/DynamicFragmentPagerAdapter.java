
package com.magicmod.mport.internal.v5.app;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.magicmod.mport.v5.view.PagerAdapter;
import com.magicmod.mport.v5.view.ViewPager;

import java.util.ArrayList;

class DynamicFragmentPagerAdapter extends PagerAdapter {
    private Context mContext;
    private FragmentTransaction mCurTransaction = null;
    private Fragment mCurrentPrimaryItem = null;
    private ArrayList<FragmentInfo> mFragmentInfos = new ArrayList();
    private FragmentManager mFragmentManager;
    private ViewPager mViewPager;

    public DynamicFragmentPagerAdapter(Context context, FragmentManager fm,
            ViewPager viewPager) {
        this.mContext = context;
        this.mFragmentManager = fm;
        this.mViewPager = viewPager;
        this.mViewPager.setAdapter(this);
    }

    private void removeAllFragmentFromManager() {
        FragmentTransaction ft = this.mFragmentManager.beginTransaction();
        int size = mFragmentInfos.size();
        for (int i = 0; i < size; i++) {
            ft.remove(getFragment(i, false));
        }
        ft.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
    }

    private void removeFragmentFromManager(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fm = fragment.getFragmentManager();
            if (fm != null) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
                fm.executePendingTransactions();
            }
        }
    }

    int addFragment(String tag, int index, Class<? extends Fragment> clazz, Bundle args,
            ActionBar.Tab tab, boolean hasActionMenu) {
        mFragmentInfos.add(index, new FragmentInfo(tag, clazz, args, tab, hasActionMenu));
        notifyDataSetChanged();
        return index;
    }

    int addFragment(String tag, Class<? extends Fragment> clazz, Bundle args, ActionBar.Tab tab,
            boolean hasActionMenu) {
        mFragmentInfos.add(new FragmentInfo(tag, clazz, args, tab, hasActionMenu));
        notifyDataSetChanged();
        return mFragmentInfos.size() - 1;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null)
            mCurTransaction = mFragmentManager.beginTransaction();
        mCurTransaction.detach((Fragment) object);
    }

    int findPositionByTag(String tag) {
        /*int i = this.mFragmentInfos.size();
        int j = 0;
        if (j < i)
            if (!((FragmentInfo) this.mFragmentInfos.get(j)).tag.equals(tag))
                ;
        while (true) {
            return j;
            j++;
            break;
            j = -1;
        }*/
        int size = mFragmentInfos.size(); //v2
        for (int i=0; i<size; i++) {
            FragmentInfo fi = (FragmentInfo)mFragmentInfos.get(i);
            if (fi.tag.equals(tag)) {
                return i;
            }
        }
        return -1;
    }

    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    public int getCount() {
        return this.mFragmentInfos.size();
    }

    Fragment getFragment(int position, boolean create) {
        FragmentInfo fi = (FragmentInfo) mFragmentInfos.get(position);
        if (fi.fragment == null) {
            fi.fragment = mFragmentManager.findFragmentByTag(fi.tag);
            if ((fi.fragment == null) && (create)) {
                fi.fragment = Fragment.instantiate(mContext, fi.clazz.getName(), fi.args);
                fi.clazz = null;
                fi.args = null;
            }
        }
        return fi.fragment;
    }

    public int getItemPosition(Object object) {
        /*int i = this.mFragmentInfos.size();
        int j = 0;
        if (j < i)
            if (paramObject != ((FragmentInfo) this.mFragmentInfos.get(j)).fragment)
                ;
        while (true) {
            return j;
            j++;
            break;
            j = -2;
        }*/
        int size = mFragmentInfos.size(); //v1
        //int i = 0; //v0
        for (int i=0; i<size; i++) {
            if (((FragmentInfo)mFragmentInfos.get(i)).fragment == object) {
                return i;
            }
        }
        return -2;
    }

    ActionBar.Tab getTabAt(int position) {
        return ((FragmentInfo) mFragmentInfos.get(position)).tab;
    }

    public boolean hasActionMenu(int position) {
        return ((FragmentInfo) mFragmentInfos.get(position)).hasActionMenu;
    }

    /*public Object instantiateItem(ViewGroup paramViewGroup, int paramInt) {
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
    }*/
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null)
            mCurTransaction = mFragmentManager.beginTransaction();
        Fragment fragment = getFragment(position, true);
        if (fragment.getFragmentManager() != null) {
            mCurTransaction.attach(fragment);
        } else {
            mCurTransaction.add(container.getId(), fragment,
                    ((FragmentInfo) mFragmentInfos.get(position)).tag);
        }
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }
        return fragment;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        boolean flag;
        if (((Fragment) object).getView() == view)
            flag = true;
        else
            flag = false;
        return flag;
    }

    void removeAllFragment() {
        removeAllFragmentFromManager();
        mFragmentInfos.clear();
        notifyDataSetChanged();
    }

    int removeFragment(ActionBar.Tab tab) {
        /*int i = this.mFragmentInfos.size();
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
        }*/
        int size = mFragmentInfos.size(); // v2
        for (int i = 0; i < size; i++) {
            FragmentInfo fi = (FragmentInfo) mFragmentInfos.get(i); // local v0
            if (fi.tab == tab) { // cond_0 in
                removeFragmentFromManager(fi.fragment);
                mFragmentInfos.remove(i);
                notifyDataSetChanged();
                return i;
            }
        }
        return -1;
    }

    int removeFragment(Fragment fragment) {
        /*int i = this.mFragmentInfos.size();
        int j = 0;
        if (j < i)
            if (getFragment(j, false) == fragment) {
                removeFragmentFromManager(fragment);
                this.mFragmentInfos.remove(j);
                notifyDataSetChanged();
            }
        while (true) {
            return j;
            j++;
            break;
            j = -1;
        }*/
        int size = mFragmentInfos.size();
        for (int i = 0; i < size; i++) {
            if (getFragment(i, false) == fragment) {
                removeFragmentFromManager(fragment);
                mFragmentInfos.remove(i);
                notifyDataSetChanged();
                return i;
            }
        }
        return -1;
    }

    void removeFragmentAt(int position) {
        removeFragmentFromManager(getFragment(position, false));
        mFragmentInfos.remove(position);
        notifyDataSetChanged();
    }

    void setFragmentActionMenuAt(int position, boolean hasActionMenu) {
        FragmentInfo fi = (FragmentInfo) mFragmentInfos.get(position);
        if (fi.hasActionMenu != hasActionMenu) {
            fi.hasActionMenu = hasActionMenu;
            notifyDataSetChanged();
        }
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    public void startUpdate(ViewGroup container) {
    }

    class FragmentInfo {
        Bundle args;
        Class<? extends Fragment> clazz;
        Fragment fragment;
        boolean hasActionMenu;
        ActionBar.Tab tab;
        String tag;

        FragmentInfo(String tag, Class<? extends Fragment> clazz, Bundle args, ActionBar.Tab tab,
                boolean hasActionMenu) {
            this.tag = tag;
            this.clazz = clazz;
            this.fragment = null;
            this.args = args;
            // Object localObject;
            this.tab = tab;
            // boolean bool;
            this.hasActionMenu = hasActionMenu;
        }
    }
}
