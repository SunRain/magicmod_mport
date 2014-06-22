
package com.magicmod.mport.internal.v5.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.internal.view.menu.MenuPresenter;
import com.magicmod.mport.internal.v5.app.ActionBarImpl;
import com.magicmod.mport.internal.v5.view.menu.ActionMenuView;
import com.magicmod.mport.util.UiUtils;
import com.miui.internal.R;

public class ActionBarView extends com.android.internal.widget.ActionBarView {
    private static final int DEFAULT_ACTION_BAR_TITLE_LAYOUT = R.layout.v5_action_bar_title_item;// 100859932;
    private ActionBarImpl mActionBar;
    private View mSeperatorView;
    private View mSubTitleContainer;
    private CharSequence mTertiaryTitle;
    private TextView mTertiaryView;
    private boolean mUseDefaultTitleLayout;

    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static ActionBarView findActionBarViewByView(View view) {
        View v = view.getRootView();
        if (v instanceof ActionBarViewHolder) {
            return ((ActionBarViewHolder) v).getActionBarView();
        } else {
            return null;
        }
    }

    protected void ensureSubtitleView() {
        if ((this.mUseDefaultTitleLayout) && (getSubtitleView() == null)) {
            TextView titleView = getTitleView();
            ViewGroup oldParent = (ViewGroup) titleView.getParent();
            oldParent.removeView(titleView);
            
            LinearLayout linearLayout = new LinearLayout(this.mContext);
            linearLayout.setOrientation(1);
            linearLayout.setGravity(19);
            
            LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(-2, -2);
            lParams.gravity = 16;
            linearLayout.setLayoutParams(lParams);
            oldParent.addView(linearLayout);
            linearLayout.addView(titleView);
            
            mSubTitleContainer = LayoutInflater.from(mContext).inflate(R.layout.v5_action_bar_sub_title_item,//100859931,
                    linearLayout, false);
            linearLayout.addView(mSubTitleContainer);
            TextView subtitleView = (TextView) mSubTitleContainer.findViewById(R.id.v5_action_bar_subtitle);//101384350);
            if (getSubtitleStyleRes() != 0)
                subtitleView.setTextAppearance(mContext, getSubtitleStyleRes());
            setSubtitleView(subtitleView);
            mTertiaryView = ((TextView) mSubTitleContainer.findViewById(R.id.v5_action_bar_tertiary_title));//101384351));
            if (getSubtitleStyleRes() != 0)
                mTertiaryView.setTextAppearance(getContext(), getSubtitleStyleRes());
            mSeperatorView = mSubTitleContainer.findViewById(R.id.separator1);//101384321);
        }
    }

    public ActionBarImpl getActionBar() {
        return mActionBar;
    }

    ActionMenuView getActionMenuView() {
        return (ActionMenuView) mMenuView;
    }

    public CharSequence getTertiaryTitle() {
        return mTertiaryTitle;
    }

    public boolean hasTitle() {
        //if ((0x1E & getDisplayOptions()) != 0)
       // /    return true;
        //return false;
        return (0x1E & getDisplayOptions()) != 0;
    }

    /*protected boolean miuiInitTitle()
    {
      boolean bool1 = true;
      Object localObject = getTitleLayout();
      int i = UiUtils.resolveAttribute(this.mContext, 100728895);
      boolean bool2;
      boolean bool3;
      label324: boolean bool4;
      label336: boolean bool5;
      label344: int j;
      if (i == 100859932)
      {
        bool2 = bool1;
        this.mUseDefaultTitleLayout = bool2;
        if (localObject == null)
        {
          LayoutInflater localLayoutInflater = LayoutInflater.from(this.mContext);
          if (i != 0)
            localObject = localLayoutInflater.inflate(i, this, false);
          if (localObject != null)
          {
            TextView localTextView1 = (TextView)((View)localObject).findViewById(101384352);
            TextView localTextView2 = (TextView)((View)localObject).findViewById(101384350);
            View localView = ((View)localObject).findViewById(101384367);
            this.mTertiaryView = ((TextView)((View)localObject).findViewById(101384351));
            this.mSeperatorView = ((View)localObject).findViewById(101384321);
            this.mSubTitleContainer = ((View)localObject).findViewById(101384349);
            setTitleLayout((ViewGroup)localObject);
            if (localTextView1 != null)
            {
              if ((this.mUseDefaultTitleLayout) && (getTitleStyleRes() != 0))
                localTextView1.setTextAppearance(this.mContext, getTitleStyleRes());
              if (getTitle() != null)
                localTextView1.setText(getTitle());
              setTitleView(localTextView1);
            }
            if (localTextView2 != null)
            {
              if ((this.mUseDefaultTitleLayout) && (getSubtitleStyleRes() != 0))
                localTextView2.setTextAppearance(this.mContext, getSubtitleStyleRes());
              if (getSubtitle() != null)
              {
                localTextView2.setText(getSubtitle());
                localTextView2.setVisibility(0);
              }
              setSubtitleView(localTextView2);
            }
            if (this.mTertiaryView != null)
            {
              if ((this.mUseDefaultTitleLayout) && (getSubtitleStyleRes() != 0))
                this.mTertiaryView.setTextAppearance(getContext(), getSubtitleStyleRes());
              if (!TextUtils.isEmpty(this.mTertiaryTitle))
              {
                this.mTertiaryView.setText(this.mTertiaryTitle);
                this.mTertiaryView.setVisibility(0);
              }
            }
            if ((0x4 & getDisplayOptions()) == 0)
              break label458;
            bool3 = bool1;
            if ((0x2 & getDisplayOptions()) == 0)
              break label464;
            bool4 = bool1;
            if (bool4)
              break label470;
            bool5 = bool1;
            if (localView != null)
            {
              if (!bool5)
                break label483;
              if (!bool3)
                break label476;
              j = 0;
              label362: localView.setVisibility(j);
              localView.setOnClickListener(getUpClickListener());
            }
            if ((!bool3) || (!bool5))
              break label490;
          }
        }
      }
      while (true)
      {
        ((View)localObject).setEnabled(bool1);
        if ((localObject != null) && (this.mUseDefaultTitleLayout) && ((getExpandedActionView() != null) || ((TextUtils.isEmpty(getTitle())) && (TextUtils.isEmpty(getSubtitle())) && (TextUtils.isEmpty(this.mTertiaryTitle)))))
          ((View)localObject).setVisibility(8);
        return super.miuiInitTitle();
        bool2 = false;
        break;
        label458: bool3 = false;
        break label324;
        label464: bool4 = false;
        break label336;
        label470: bool5 = false;
        break label344;
        label476: j = 8;
        break label362;
        label483: j = 8;
        break label362;
        label490: bool1 = false;
      }
    }*/
    protected boolean miuiInitTitle() {
        boolean flag = true;
        View titleLayout = getTitleLayout();
        int titleLayoutResId = UiUtils.resolveAttribute(
                mContext, 
                R.attr.v5_action_bar_title_layout//0x601003f
                );
        
        boolean flag1;
        if (titleLayoutResId == /* 0x603001c */R.layout.v5_action_bar_title_item)
            flag1 = flag;
        else
            flag1 = false;
        
        mUseDefaultTitleLayout = flag1;
        if (titleLayout == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            if (titleLayoutResId != 0)
                titleLayout = inflater.inflate(titleLayoutResId, this, false);
            
            if (titleLayout != null) {
                TextView titleView = (TextView) ((View) (titleLayout)).findViewById(/*0x60b00a0*/R.id.v5_action_bar_title);
                TextView subtitleView = (TextView) ((View) (titleLayout)).findViewById(/*0x60b009e*/R.id.v5_action_bar_subtitle);
                View titleUpView = ((View) (titleLayout)).findViewById(/*0x60b00af*/R.id.v5_up);
                mTertiaryView = (TextView) ((View) (titleLayout)).findViewById(/*0x60b009f*/R.id.v5_action_bar_tertiary_title);
                mSeperatorView = ((View) (titleLayout)).findViewById(/*0x60b0081*/R.id.separator1);
                mSubTitleContainer = ((View) (titleLayout)).findViewById(/*0x60b009d*/R.id.v5_action_bar_sub_title_container);
                setTitleLayout((ViewGroup) titleLayout);
                if (titleView != null) {
                    if (mUseDefaultTitleLayout && getTitleStyleRes() != 0)
                        titleView.setTextAppearance(super.mContext, getTitleStyleRes());
                    if (getTitle() != null)
                        titleView.setText(getTitle());
                    setTitleView(titleView);
                }
                if (subtitleView != null) {
                    if (mUseDefaultTitleLayout && getSubtitleStyleRes() != 0)
                        subtitleView.setTextAppearance(super.mContext, getSubtitleStyleRes());
                    if (getSubtitle() != null) {
                        subtitleView.setText(getSubtitle());
                        subtitleView.setVisibility(0);
                    }
                    setSubtitleView(subtitleView);
                }
                if (mTertiaryView != null) {
                    if (mUseDefaultTitleLayout && getSubtitleStyleRes() != 0)
                        mTertiaryView.setTextAppearance(getContext(), getSubtitleStyleRes());
                    if (!TextUtils.isEmpty(mTertiaryTitle)) {
                        mTertiaryView.setText(mTertiaryTitle);
                        mTertiaryView.setVisibility(0);
                    }
                }
                boolean homeAsUp;
                boolean showHome;
                boolean showTitleUp;
                if ((4 & getDisplayOptions()) != 0)
                    homeAsUp = flag;
                else
                    homeAsUp = false;
                if ((2 & getDisplayOptions()) != 0)
                    showHome = flag;
                else
                    showHome = false;
                if (!showHome)
                    showTitleUp = flag;
                else
                    showTitleUp = false;
                if (titleUpView != null) {
                    int j;
                    if (showTitleUp) {
                        if (homeAsUp)
                            j = 0;
                        else
                            j = 8;
                    } else {
                        j = 8;
                    }
                    titleUpView.setVisibility(j);
                    titleUpView.setOnClickListener(getUpClickListener());
                }
                if (!homeAsUp || !showTitleUp)
                    flag = false;
                ((View) (titleLayout)).setEnabled(flag);
            }
        }
        if (titleLayout != null
                && mUseDefaultTitleLayout
                && (getExpandedActionView() != null || TextUtils.isEmpty(getTitle())
                        && TextUtils.isEmpty(getSubtitle()) && TextUtils.isEmpty(mTertiaryTitle)))
            ((View) (titleLayout)).setVisibility(8);
        return super.miuiInitTitle();
    }


    protected void onChildVisibilityChanged(View child, int oldVisibility, int newVisibility) {
        super.onChildVisibilityChanged(child, oldVisibility, newVisibility);
        if ((child == getSubtitleView()) || (child == mTertiaryView)
                || (child == getTitleLayout()))
            updateTitleLayout();
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (hasEmbeddedTabs())
            getTabScrollView().setBackground(null);
    }

    public void setActionBar(ActionBarImpl actionBar) {
        this.mActionBar = actionBar;
    }

    public void setDisplayOptions(int options) {
        super.setDisplayOptions(options);
        if (!hasEmbeddedTabs()) {
            ViewParent localViewParent = getParent();
            if (localViewParent != null)
                ((ActionBarContainer) localViewParent).updateTabContainerBackground();
        }
    }

    public void setMenu(Menu menu, MenuPresenter.Callback cb) {
        super.setMenu(menu, cb);
        if ((isSplitActionBar()) && (mMenuView != null)) {
            ViewGroup.LayoutParams localLayoutParams = mMenuView.getLayoutParams();
            if (localLayoutParams != null) {
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                        localLayoutParams);
                lp.height = -2;
                lp.gravity = 80;
                mMenuView.setLayoutParams(lp);
            }
        }
    }

    /*public void setSubtitle(CharSequence paramCharSequence) {
        ensureSubtitleView();
        TextView localTextView = getSubtitleView();
        int i;
        if (localTextView != null) {
            i = localTextView.getVisibility();
            super.setSubtitle(paramCharSequence);
            if (localTextView == null)
                break label58;
        }
        label58: for (int j = localTextView.getVisibility();; j = 8) {
            if ((localTextView != null) && (i != j))
                onChildVisibilityChanged(localTextView, i, j);
            return;
            i = 8;
            break;
        }
    }*/
    public void setSubtitle(CharSequence subtitle) {
        ensureSubtitleView();
        TextView titleView = getSubtitleView();
        int oldVisibility;
        int newVisibility;
        if (titleView != null)
            oldVisibility = titleView.getVisibility();
        else
            oldVisibility = 8;
        
        super.setSubtitle(subtitle);
        
        if (titleView != null)
            newVisibility = titleView.getVisibility();
        else
            newVisibility = 8;
        if (titleView != null && oldVisibility != newVisibility)
            onChildVisibilityChanged(titleView, oldVisibility, newVisibility);
    }

    /*public void setTertiaryTitle(CharSequence paramCharSequence) {
        ensureSubtitleView();
        this.mTertiaryTitle = paramCharSequence;
        int i;
        if (this.mTertiaryView != null) {
            boolean bool = TextUtils.isEmpty(paramCharSequence);
            this.mTertiaryView.setText(paramCharSequence);
            i = this.mTertiaryView.getVisibility();
            if (bool)
                break label71;
        }
        label71: for (int j = 0;; j = 8) {
            this.mTertiaryView.setVisibility(j);
            if (i != j)
                onChildVisibilityChanged(this.mTertiaryView, i, j);
            return;
        }
    }*/
    public void setTertiaryTitle(CharSequence title) {
        ensureSubtitleView();
        mTertiaryTitle = title;
        if (mTertiaryView != null) {
            boolean isTitleEmpty = TextUtils.isEmpty(title);
            mTertiaryView.setText(title);
            int oldVisibility = mTertiaryView.getVisibility();
            int newVisibility;
            if (!isTitleEmpty)
                newVisibility = 0;
            else
                newVisibility = 8;
            mTertiaryView.setVisibility(newVisibility);
            if (oldVisibility != newVisibility)
                onChildVisibilityChanged(mTertiaryView, oldVisibility, newVisibility);
        }
    }


    /*protected void updateTitleLayout()
    {
      int i = 0;
      if (!this.mUseDefaultTitleLayout)
        return;
      int j;
      label22: int k;
      label41: int m;
      label61: int i2;
      label87: int i1;
      label120: int n;
      label161: ViewGroup localViewGroup;
      if (getTitleView().getVisibility() == 0)
      {
        j = 1;
        TextView localTextView = getSubtitleView();
        if (localTextView == null)
          break label192;
        if (localTextView.getVisibility() != 0)
          break label186;
        k = 1;
        if (this.mTertiaryView == null)
          break label204;
        if (this.mTertiaryView.getVisibility() != 0)
          break label198;
        m = 1;
        if (this.mSeperatorView != null)
        {
          View localView2 = this.mSeperatorView;
          if ((k == 0) || (m == 0))
            break label210;
          i2 = 0;
          localView2.setVisibility(i2);
        }
        if (this.mSubTitleContainer != null)
        {
          View localView1 = this.mSubTitleContainer;
          if ((k == 0) && (m == 0))
            break label217;
          i1 = 0;
          localView1.setVisibility(i1);
        }
        if ((getExpandedActionView() != null) || ((0x8 & getDisplayOptions()) == 0) || ((j == 0) && (k == 0) && (m == 0)))
          break label224;
        n = 1;
        localViewGroup = getTitleLayout();
        if (n == 0)
          break label230;
      }
      while (true)
      {
        localViewGroup.setVisibility(i);
        break;
        j = 0;
        break label22;
        label186: k = 0;
        break label41;
        label192: k = 0;
        break label41;
        label198: m = 0;
        break label61;
        label204: m = 0;
        break label61;
        label210: i2 = 8;
        break label87;
        label217: i1 = 8;
        break label120;
        label224: n = 0;
        break label161;
        label230: i = 8;
      }
    }*/
    protected void updateTitleLayout() {
        int i = 0;
        if (mUseDefaultTitleLayout) {
            boolean titleVisible;
            TextView subTitleView;
            boolean subTitleVisible;
            boolean tertiaryTitleVisible;
            boolean visible;
            if (getTitleView().getVisibility() == 0)
                titleVisible = true;
            else
                titleVisible = false;
            
            subTitleView = getSubtitleView();
            if (subTitleView != null) {
                if (subTitleView.getVisibility() == 0)
                    subTitleVisible = true;
                else
                    subTitleVisible = false;
            } else {
                subTitleVisible = false;
            }
            if (mTertiaryView != null) {
                if (mTertiaryView.getVisibility() == 0)
                    tertiaryTitleVisible = true;
                else
                    tertiaryTitleVisible = false;
            } else {
                tertiaryTitleVisible = false;
            }
            if (mSeperatorView != null) {
                //View view1 = mSeperatorView;
                //ViewGroup viewgroup;
                //View view;
                //int k;
                if (subTitleVisible && tertiaryTitleVisible) {
                    //k = 0;
                    mSeperatorView.setVisibility(0);
                } else {
                    //k = 8;
                    mSeperatorView.setVisibility(8);
                }
                //view1.setVisibility(k);
                //mSeperatorView.setVisibility(k);
            }
            if (mSubTitleContainer != null) {
                //view = mSubTitleContainer;
                //int j;
                if (subTitleVisible || tertiaryTitleVisible) {
                    //j = 0;
                    mSubTitleContainer.setVisibility(0);
                } else {
                    //j = 8;
                    mSubTitleContainer.setVisibility(8);
                }
                //view.setVisibility(j);
            }
            if (getExpandedActionView() == null && (8 & getDisplayOptions()) != 0
                    && (titleVisible || subTitleVisible || tertiaryTitleVisible))
                visible = true;
            else
                visible = false;
            //viewgroup = getTitleLayout();
            if (!visible)
                i = 8;
            //viewgroup.setVisibility(i);
            getTitleLayout().setVisibility(i);
        }
    }

    public static abstract interface ActionBarViewHolder {
        public abstract ActionBarView getActionBarView();
    }
}
