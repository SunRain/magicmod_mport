
package com.magicmod.mport.internal.v5.widget;

import android.R;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.renderscript.Element;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.magicmod.mport.internal.v5.view.ActionModeView;
import com.magicmod.mport.internal.v5.view.menu.ActionMenuView;
import com.magicmod.mport.v5.widget.SearchModeAnimationListener;
import com.magicmod.mport.v5.widget.SimpleSearchModeAnimationListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SearchActionModeView extends FrameLayout implements AnimatorListener,
        ActionModeView, TextWatcher, IdleHandler, OnClickListener {
    private static final int ANIMATE_IDLE = 0x0;
    private static final int ANIMATE_IN = 0x1;
    private static final int ANIMATE_OUT = 0x2;
    private static final int DEFAULT_ANIMATION_DURATION = 0xc8;//200;
    private ActionBarContainer mActionBarContainer;
    private ActionBarView mActionBarView;
    private WeakReference<View> mAnchorView;
    private WeakReference<View> mAnimateView;
    private int mAnimateViewTranslationYLength;
    private int mAnimateViewTranslationYStart;
    private int mAnimateVisibility = 0x8;
    private int mAnimationMode = 0x0;
    private ImageView mBackView;
    private int mBackViewTranslationXLength;
    private int mBackViewTranslationXStart;
    private ObjectAnimator mCurrentAnimation;
    private View mDimView;
    private EditText mInputView;
    private int mInputViewTranslationYLength;
    private int mInputViewTranslationYStart;
    private int[] mLocation = new int[2];
    private boolean mRequestAnimation;
    private int mResultViewOriginMarginBottom;
    private int mResultViewOriginMarginTop;
    private boolean mResultViewSet;
    private WeakReference<View> mResusltView;
    private ArrayList<SearchModeAnimationListener> mSearchModeAnimationListeners;
    private ActionBarContainer mSplitActionBarContainer;
    private int mTextLengthBeforeChanged;

    public SearchActionModeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAlpha(0.0F);
    }

    private MessageQueue getMessageQueue() {
        return Looper.getMainLooper().getQueue();
    }

    private boolean hasAnchorAndAnimateView() {
        boolean flag;
        if (mAnchorView != null && mAnimateView != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    /*private void notifyAnimation(float paramFloat) {
        if (this.mSearchModeAnimationListeners == null)
            ;
        while (true) {
            return;
            for (int i = 0; i < this.mSearchModeAnimationListeners.size(); i++)
                ((SearchModeAnimationListener) this.mSearchModeAnimationListeners.get(i))
                        .onUpdate(paramFloat);
        }
    }*/
    private void notifyAnimation(float percent) {
        if (mSearchModeAnimationListeners != null) {
            int i = 0;
            while (i < mSearchModeAnimationListeners.size()) {
                ((SearchModeAnimationListener) mSearchModeAnimationListeners.get(i)).onUpdate(percent);
                i++;
            }
        }
    }


    private void notifyAnimationEnd() {
        if (mSearchModeAnimationListeners != null) {
            int i = 0;
            while (i < mSearchModeAnimationListeners.size()) {
                ((SearchModeAnimationListener) mSearchModeAnimationListeners.get(i)).onStop();
                i++;
            }
        }
    }

    private void notifyAnimationStart() {
        if (mSearchModeAnimationListeners != null) {
            int i = 0;
            while (i < mSearchModeAnimationListeners.size()) {
                ((SearchModeAnimationListener) mSearchModeAnimationListeners.get(i)).onStart();
                i++;
            }
        }
    }

    private void queueIdleHandler() {
        removeIdleHandler();
        getMessageQueue().addIdleHandler(this);
    }

    private void removeIdleHandler() {
        getMessageQueue().removeIdleHandler(this);
    }

    /*public void addAnimationListener(SearchModeAnimationListener paramSearchModeAnimationListener) {
        if (paramSearchModeAnimationListener == null)
            ;
        while (true) {
            return;
            if (this.mSearchModeAnimationListeners == null)
                this.mSearchModeAnimationListeners = new ArrayList();
            this.mSearchModeAnimationListeners.add(paramSearchModeAnimationListener);
        }
    }*/
    public void addAnimationListener(SearchModeAnimationListener listener) {
        if (listener != null) {
            if (mSearchModeAnimationListeners == null)
                mSearchModeAnimationListeners = new ArrayList();
            mSearchModeAnimationListeners.add(listener);
        }
    }

    /*public void afterTextChanged(Editable paramEditable) {
        int i;
        if (paramEditable == null) {
            i = 0;
            if (i != 0)
                break label41;
            if (this.mDimView != null)
                this.mDimView.setVisibility(0);
            showSoftInputPanel(true);
        }
        while (true) {
            return;
            i = paramEditable.length();
            break;
            label41: if ((this.mTextLengthBeforeChanged == 0) && (this.mDimView != null))
                this.mDimView.setVisibility(8);
        }
    }*/
    @Override
    public void afterTextChanged(Editable s)
    {
        int afterTextChangedLength;
        if(s == null)
            afterTextChangedLength = 0;
        else
            afterTextChangedLength = s.length();
        
        if(afterTextChangedLength != 0) {
            if(mTextLengthBeforeChanged == 0 && mDimView != null)
                mDimView.setVisibility(8);
            return;
        } else {
            if(mDimView != null)
                mDimView.setVisibility(0);
            showSoftInputPanel(true);
        }
    }

    protected void allowUpdateContentBorder(boolean allow) {
        View view = ((ViewGroup)getRootView()).getChildAt(0);
        if ((view instanceof IActionBarLayout))
            ((IActionBarLayout) view).setUpdateContentMarginEnabled(allow);
    }

    /*public void animateToVisibility(int paramInt) {
        if (this.mAnimateVisibility == paramInt) {
            this.mRequestAnimation = false;
            return;
        }
        int i;
        if (paramInt == 0) {
            i = 1;
            label20: this.mAnimationMode = i;
            this.mCurrentAnimation = makeAnimation(paramInt);
            createAnimationListeners();
            notifyAnimationStart();
            if (!hasAnchorAndAnimateView())
                break label79;
            if (paramInt == 0)
                allowUpdateContentBorder(false);
            requestLayout();
            this.mRequestAnimation = true;
        }
        while (true) {
            pollViews();
            break;
            i = 2;
            break label20;
            label79: this.mCurrentAnimation.start();
        }
    }*/
    public void animateToVisibility(int visibility) {
        if (mAnimateVisibility == visibility) {
            mRequestAnimation = false;
        } else {
            //int j;
            if (visibility == 0)
                mAnimationMode = 1;//j = 1;
            else
                mAnimationMode = 2;//j = 2;
            //mAnimationMode = j;
            mCurrentAnimation = makeAnimation(visibility);
            createAnimationListeners();
            notifyAnimationStart();
            if (hasAnchorAndAnimateView()) {
                if (visibility == 0)
                    allowUpdateContentBorder(false);
                requestLayout();
                mRequestAnimation = true;
            } else {
                mCurrentAnimation.start();
            }
            pollViews();
        }
    }


    /*public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2,
            int paramInt3) {
        if (paramCharSequence == null)
            ;
        for (int i = 0;; i = paramCharSequence.length()) {
            this.mTextLengthBeforeChanged = i;
            return;
        }
    }*/
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        int l;
        if(s == null)
            l = 0;
        else
            l = s.length();
        mTextLengthBeforeChanged = l;
    }

    /*public void closeMode() {
        if (this.mAnimationMode == 2)
            ;
        while (true) {
            return;
            if (this.mCurrentAnimation != null) {
                this.mCurrentAnimation.reverse();
                this.mCurrentAnimation = null;
                this.mAnimationMode = 2;
            }
        }
    }*/
    public void closeMode() {
        if (mAnimationMode != 2 && mCurrentAnimation != null) {
            mCurrentAnimation.reverse();
            mCurrentAnimation = null;
            mAnimationMode = 2;
        }
    }

    protected void createAnimationListeners() {
        if (mSearchModeAnimationListeners == null)
            mSearchModeAnimationListeners = new ArrayList();
        mSearchModeAnimationListeners.add(new DimViewSearchModeAnimationListener());
        mSearchModeAnimationListeners.add(new SearchViewSearchModeAnimationListener());
        if (hasAnchorAndAnimateView()) {
            mSearchModeAnimationListeners.add(new ContentViewSearchModeAnimationListener());
            mSearchModeAnimationListeners.add(new ActionBarSearchModeAnimationListener());
            mSearchModeAnimationListeners.add(new SplitActionBarSearchModeAnimationListener());
        }
    }

    protected void finishAnimation() {
        if (mCurrentAnimation != null) {
            mCurrentAnimation.cancel();
            mCurrentAnimation = null;
        }
    }

    protected ActionBarContainer getActionBarContainer() {
        if (mActionBarContainer == null) {
            View decorView = getRootView();
            if (decorView != null)
                mActionBarContainer = (ActionBarContainer) decorView.findViewById(ResourceMapper
                        .resolveReference(mContext,
                                com.miui.internal.R.id.android_action_bar_container// 0x60b0007
                        ));
        }
        return mActionBarContainer;
    }

    protected ActionBarView getActionBarView() {
        if (this.mActionBarView == null)
            this.mActionBarView = ActionBarView.findActionBarViewByView(this);
        return this.mActionBarView;
    }

    public EditText getSearchView() {
        return this.mInputView;
    }

    protected ActionBarContainer getSplitActionBarContainer() {
        if (mSplitActionBarContainer == null) {
            View view = getRootView();
            if (view != null)
                mSplitActionBarContainer = (ActionBarContainer) view.findViewById(ResourceMapper
                        .resolveReference(mContext, com.miui.internal.R.id.android_split_action_bar// 0x60b0027
                        ));
        }
        return mSplitActionBarContainer;
    }

    /*protected int getSplitActionBarHeight() {
        int i = 0;
        ActionBarView localActionBarView = getActionBarView();
        ActionMenuView localActionMenuView;
        if (localActionBarView.isSplitActionBar()) {
            localActionMenuView = localActionBarView.getActionMenuView();
            if (localActionMenuView.getMenuItems() <= 0)
                break label33;
        }
        label33: for (i = localActionMenuView.getPrimaryContainerHeight();; i = 0)
            return i;
    }*/
    protected int getSplitActionBarHeight() {
        int height = 0;
        ActionBarView actionBarView = getActionBarView();
        if (actionBarView.isSplitActionBar()) {
            ActionMenuView actionMenuView = actionBarView.getActionMenuView();
            if (actionMenuView.getMenuItems() > 0)
                height = actionMenuView.getPrimaryContainerHeight();
            else
                height = 0;
        }
        return height;
    }

    @Override
    public void initForMode(ActionMode actionMode) {
    }

    protected boolean isFragmentViewPagerMode() {
        return getActionBarView().getActionBar().isFragmentViewPagerMode();
    }

    public void killMode() {
        finishAnimation();
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null)
            parent.removeView(this);
        this.mActionBarContainer = null;
        this.mActionBarView = null;
        if (this.mSearchModeAnimationListeners != null) {
            this.mSearchModeAnimationListeners.clear();
            this.mSearchModeAnimationListeners = null;
        }
        this.mSplitActionBarContainer = null;
    }

    /*protected ObjectAnimator makeAnimation(int paramInt) {
        if (this.mCurrentAnimation != null) {
            this.mCurrentAnimation.cancel();
            this.mCurrentAnimation = null;
            removeIdleHandler();
        }
        int i;
        if (paramInt == 0) {
            i = 0;
            if (paramInt != 0)
                break label86;
        }
        label86: for (int j = 1;; j = 0) {
            float[] arrayOfFloat = new float[2];
            arrayOfFloat[0] = i;
            arrayOfFloat[1] = j;
            ObjectAnimator localObjectAnimator = ObjectAnimator
                    .ofFloat(this, "Value", arrayOfFloat);
            localObjectAnimator.addListener(this);
            localObjectAnimator.setDuration(200L);
            return localObjectAnimator;
            i = 1;
            break;
        }
    }*/
    protected ObjectAnimator makeAnimation(int visibility) {
        if (mCurrentAnimation != null) {
            mCurrentAnimation.cancel();
            mCurrentAnimation = null;
            removeIdleHandler();
        }
        int j;
        int k;
        float af[];
        ObjectAnimator animator;
        if (visibility == 0)
            j = 0;
        else
            j = 1;
        if (visibility == 0)
            k = 1;
        else
            k = 0;
        af = new float[2];
        af[0] = j;
        af[1] = k;
        animator = ObjectAnimator.ofFloat(this, "Value", af);
        animator.addListener(this);
        animator.setDuration(200L);
        return animator;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        /*this.mCurrentAnimation = null;
        notifyAnimationEnd();
        if (this.mAnimationMode == 1)
            ;
        for (boolean bool = true;; bool = false) {
            showSoftInputPanel(bool);
            setResultViewMargin(bool);
            if (this.mAnimationMode == 2) {
                allowUpdateContentBorder(true);
                setAlpha(0.0F);
                this.mAnimateVisibility = 8;
                if (isFragmentViewPagerMode())
                    getRootView().findViewById(16908290).requestLayout();
                killMode();
            }
            return;
        }*/
        mCurrentAnimation = null;
        notifyAnimationEnd();
        boolean enter;
        if (mAnimationMode == 1)
            enter = true;
        else
            enter = false;
        showSoftInputPanel(enter);
        setResultViewMargin(enter);
        if (mAnimationMode == 2) {
            allowUpdateContentBorder(true);
            setAlpha(0.0F);
            mAnimateVisibility = 8;
            if (isFragmentViewPagerMode()) {
                getRootView().findViewById(R.id.content/* 0x1020002 */
                ).requestLayout();
            }
            killMode();
        }
    }

    /*public void onAnimationStart(Animator paramAnimator) {
        if (this.mAnimationMode == 1) {
            if (this.mAnchorView != null)
                ((View) this.mAnchorView.get()).setAlpha(0.0F);
            setAlpha(1.0F);
            this.mAnimateVisibility = 0;
        }
        while (true) {
            return;
            if (this.mAnimationMode == 2) {
                View localView = getActionBarContainer().getTabContainer();
                if (localView != null)
                    localView.setVisibility(0);
            }
        }
    }*/
    @Override
    public void onAnimationStart(Animator animation) {
        if (mAnimationMode != 1) {
            if (mAnimationMode == 2) {
                View tabScrollView = getActionBarContainer().getTabContainer();
                if (tabScrollView != null)
                    tabScrollView.setVisibility(0);
            }
            return;
        } else {
            if (mAnchorView != null)
                ((View) mAnchorView.get()).setAlpha(0.0F);
            setAlpha(1.0F);
            mAnimateVisibility = 0;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == com.miui.internal.R.id.v5_icon_menu_bar_dim_container/* 0x60b00a2 */)
            mBackView.performClick();
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        mBackView = (ImageView) findViewById(R.id.home/* 0x102002c */);
        mInputView = (EditText) findViewById(R.id.input/* 0x1020009 */);
        ((MarginLayoutParams) mBackView.getLayoutParams()).leftMargin = -getPaddingLeft();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mRequestAnimation) {
            if ((mAnimationMode == 1) && (hasAnchorAndAnimateView()))
                ((View) mAnimateView.get()).setTranslationY(-mAnimateViewTranslationYLength);
            queueIdleHandler();
            mRequestAnimation = false;
        }
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    protected void pollViews() {
        getActionBarView();
        getActionBarContainer();
        getSplitActionBarContainer();
    }

    public boolean queueIdle() {
        mCurrentAnimation.start();
        return false;
    }

    public void removeAnimationListener(SearchModeAnimationListener listener) {
        if (listener != null && mSearchModeAnimationListeners != null)
            mSearchModeAnimationListeners.remove(listener);
    }

    public void setAnchorView(View view) {
        if (view != null)
            this.mAnchorView = new WeakReference(view);
    }

    public void setAnchorViewHint(CharSequence hint) {
        this.mInputView.setHint(hint);
    }

    public void setAnimateView(View view) {
        if (view != null)
            this.mAnimateView = new WeakReference(view);
    }

    protected void setContentViewMargin(int topMargin, int bottomMargin) {
        View view = getRootView().findViewById(R.id.content/* 0x1020002 */);
        if (view != null) {
            MarginLayoutParams lParams = (MarginLayoutParams) view.getLayoutParams();
            lParams.bottomMargin = bottomMargin;
            lParams.topMargin = topMargin;
            view.setLayoutParams(lParams);
        }
    }

    public void setOnBackClickListener(View.OnClickListener listener) {
        this.mBackView.setOnClickListener(listener);
    }

    public void setResultView(View view) {
        if (view != null) {
            this.mResusltView = new WeakReference(view);
            ViewGroup.LayoutParams lParams = view.getLayoutParams();
            if ((lParams instanceof ViewGroup.MarginLayoutParams)) {
                this.mResultViewOriginMarginTop = ((ViewGroup.MarginLayoutParams) lParams).topMargin;
                this.mResultViewOriginMarginBottom = ((ViewGroup.MarginLayoutParams) lParams).bottomMargin;
                this.mResultViewSet = true;
            }
        }
    }

    /*protected void setResultViewMargin(boolean paramBoolean) {
        int i;
        int j;
        if ((this.mResusltView != null) && (this.mResultViewSet)) {
            if (!paramBoolean)
                break label71;
            i = getHeight();
            if (!isFragmentViewPagerMode())
                break label66;
            j = getSplitActionBarHeight();
        }
        while (true) {
            ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams) ((View) this.mResusltView
                    .get()).getLayoutParams();
            localMarginLayoutParams.topMargin = i;
            localMarginLayoutParams.bottomMargin = j;
            return;
            label66: j = 0;
            continue;
            label71: i = this.mResultViewOriginMarginTop;
            j = this.mResultViewOriginMarginBottom;
        }
    }*/
    protected void setResultViewMargin(boolean enter) {
        if (mResusltView != null && mResultViewSet) {
            int topMargin;
            int bottomMargin;
            ViewGroup.MarginLayoutParams lParams;
            if (enter) {
                topMargin = getHeight();
                if (isFragmentViewPagerMode())
                    bottomMargin = getSplitActionBarHeight();
                else
                    bottomMargin = 0;
            } else {
                topMargin = mResultViewOriginMarginTop;
                bottomMargin = mResultViewOriginMarginBottom;
            }
            lParams = (MarginLayoutParams) ((View) mResusltView.get()).getLayoutParams();
            lParams.topMargin = topMargin;
            lParams.bottomMargin = bottomMargin;
        }
    }

    public void setValue(float percent) {
        notifyAnimation(percent);
    }

    /*protected void showSoftInputPanel(boolean paramBoolean) {
        InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
        if (paramBoolean) {
            localInputMethodManager.viewClicked(this.mInputView);
            localInputMethodManager.showSoftInput(this.mInputView, 0);
        }
        while (true) {
            return;
            localInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }*/
    protected void showSoftInputPanel(boolean show) {
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (show) {
            imm.viewClicked(mInputView);
            imm.showSoftInput(mInputView, 0);
        } else {
            imm.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    class ActionBarSearchModeAnimationListener extends SimpleSearchModeAnimationListener {
        ActionBarSearchModeAnimationListener() {
        }

        public void onStop() {
            if (SearchActionModeView.this.mAnimationMode == 1) {
                View tabScrollView = SearchActionModeView.this.getActionBarContainer()
                        .getTabContainer();
                if (tabScrollView != null)
                    tabScrollView.setVisibility(8);
            }
        }

        public void onUpdate(float percent) {
            ActionBarContainer actionBarContainer = SearchActionModeView.this
                    .getActionBarContainer();
            if (actionBarContainer != null)
                actionBarContainer.setTranslationY(-percent * actionBarContainer.getHeight());
        }
    }

    class ContentViewSearchModeAnimationListener extends SimpleSearchModeAnimationListener {
        ContentViewSearchModeAnimationListener() {
        }

        /*public void onStart() {
            int j;
            if (SearchActionModeView.this.mAnimationMode == 1) {
                SearchActionModeView.this.getActionBarContainer().getLocationInWindow(
                        SearchActionModeView.this.mLocation);
                int i = SearchActionModeView.this.mLocation[1];
                ((View) SearchActionModeView.this.mAnchorView.get())
                        .getLocationInWindow(SearchActionModeView.this.mLocation);
                SearchActionModeView.access$802(SearchActionModeView.this,
                        SearchActionModeView.this.mLocation[1] - i);
                SearchActionModeView.access$902(SearchActionModeView.this,
                        -SearchActionModeView.this.mAnimateViewTranslationYStart);
                SearchActionModeView.access$1002(SearchActionModeView.this,
                        SearchActionModeView.this.mLocation[1]);
                SearchActionModeView.access$1102(SearchActionModeView.this,
                        SearchActionModeView.this.mAnimateViewTranslationYLength);
                if (SearchActionModeView.this.isFragmentViewPagerMode()) {
                    j = -SearchActionModeView.this.getSplitActionBarHeight();
                    SearchActionModeView.this.setContentViewMargin(0, j);
                }
            }
            while (true) {
                return;
                j = 0;
                break;
                if ((SearchActionModeView.this.mAnimationMode == 2)
                        && (SearchActionModeView.this.isFragmentViewPagerMode()))
                    SearchActionModeView.this.setContentViewMargin(0,
                            -SearchActionModeView.this.mAnimateViewTranslationYLength);
            }
        }*/
        public void onStart() {
            if (mAnimationMode != 1) {
                if (mAnimationMode == 2 && isFragmentViewPagerMode())
                    setContentViewMargin(0, -mAnimateViewTranslationYLength);
                return;
            } else {
                getActionBarContainer().getLocationInWindow(mLocation);
                int actionBarLocation = mLocation[1];
                ((View) mAnchorView.get()).getLocationInWindow(mLocation);
                mAnimateViewTranslationYStart = mLocation[1] - actionBarLocation;
                mAnimateViewTranslationYLength = -mAnimateViewTranslationYStart;
                mInputViewTranslationYStart = mLocation[1];
                mInputViewTranslationYLength = mAnimateViewTranslationYLength;
                int bottomMargin;
                if (isFragmentViewPagerMode())
                    bottomMargin = -getSplitActionBarHeight();
                else
                    bottomMargin = 0;
                setContentViewMargin(0, bottomMargin);
            }
        }

        public void onStop() {
            if (SearchActionModeView.this.mAnimationMode == 2) {
                SearchActionModeView.this.mInputView.clearFocus();
                ((View) SearchActionModeView.this.mAnimateView.get()).setTranslationY(0.0F);
                ((View) SearchActionModeView.this.mAnchorView.get()).setAlpha(1.0F);
            }
        }

        public void onUpdate(float percent) {
            float translationY = SearchActionModeView.this.mAnimateViewTranslationYStart + percent
                    * SearchActionModeView.this.mAnimateViewTranslationYLength;
            ((View) SearchActionModeView.this.mAnimateView.get()).setTranslationY(translationY);
            SearchActionModeView.this
                    .setTranslationY(SearchActionModeView.this.mInputViewTranslationYStart
                            + percent * SearchActionModeView.this.mInputViewTranslationYLength);
        }
    }

    class DimViewSearchModeAnimationListener extends SimpleSearchModeAnimationListener {
        DimViewSearchModeAnimationListener() {
        }

        public void onStart() {
            if (SearchActionModeView.this.mAnimationMode == 1) {
                //SearchActionModeView.access$1302(SearchActionModeView.this,
                //        SearchActionModeView.this.getRootView().findViewById(0x60b00a2));
                mDimView = getRootView().findViewById(com.miui.internal.R.id.v5_icon_menu_bar_dim_container);
                SearchActionModeView.this.mDimView.setOnClickListener(SearchActionModeView.this);
                SearchActionModeView.this.mDimView.setVisibility(0);
                SearchActionModeView.this.mDimView.setAlpha(0.0F);
            }
        }

        /*public void onStop() {
            if (SearchActionModeView.this.mAnimationMode == 1)
                if (SearchActionModeView.this.mInputView.getText().length() > 0)
                    SearchActionModeView.this.mDimView.setVisibility(8);
            while (true) {
                return;
                if (SearchActionModeView.this.mAnimationMode == 2) {
                    SearchActionModeView.this.mDimView.setVisibility(8);
                    SearchActionModeView.this.mDimView.setAlpha(1.0F);
                }
            }
        }*/
        public void onStop() {
            if (mAnimationMode != 1) {
                if (mAnimationMode == 2) {
                    mDimView.setVisibility(8);
                    mDimView.setAlpha(1.0F);
                }
                return;
            } else {
                if (mInputView.getText().length() > 0)
                    mDimView.setVisibility(8);
            }
        }

        public void onUpdate(float percent) {
            SearchActionModeView.this.mDimView.setAlpha(percent);
        }
    }

    class SearchViewSearchModeAnimationListener extends SimpleSearchModeAnimationListener {
        SearchViewSearchModeAnimationListener() {
        }

        public void onStart() {
            /*if (SearchActionModeView.this.mAnimationMode == 1) {
                int i = View.MeasureSpec.makeMeasureSpec(0, 0);
                SearchActionModeView.this.mBackView.measure(i, i);
                SearchActionModeView.access$202(SearchActionModeView.this,
                        -SearchActionModeView.this.mBackView.getMeasuredWidth());
                SearchActionModeView.access$302(SearchActionModeView.this,
                        SearchActionModeView.this.mBackView.getMeasuredWidth());
                SearchActionModeView.this.mInputView.getText().clear();
                SearchActionModeView.this.mInputView
                        .addTextChangedListener(SearchActionModeView.this);
                if (!SearchActionModeView.this.hasAnchorAndAnimateView()) {
                    SearchActionModeView.this.getActionBarContainer().getLocationInWindow(
                            SearchActionModeView.this.mLocation);
                    SearchActionModeView.this
                            .setTranslationY(SearchActionModeView.this.mLocation[1]);
                }
            }*/
            if (mAnimationMode == 1) {
                int i = View.MeasureSpec.makeMeasureSpec(0, 0);
                //SearchActionModeView.access$100(SearchActionModeView.this).measure(i, i);
                mBackView.measure(i, i);
                //SearchActionModeView.access$202(SearchActionModeView.this, -SearchActionModeView
                //        .access$100(SearchActionModeView.this).getMeasuredWidth());
                mBackViewTranslationXStart = -mBackView.getMeasuredWidth();
                //SearchActionModeView.access$302(SearchActionModeView.this, SearchActionModeView
                //        .access$100(SearchActionModeView.this).getMeasuredWidth());
                mBackViewTranslationXLength = mBackView.getMeasuredWidth();
                //SearchActionModeView.access$400(SearchActionModeView.this).getText().clear();
                mInputView.getText().clear();
                //SearchActionModeView.access$400(SearchActionModeView.this).addTextChangedListener(
                //        SearchActionModeView.this);
                mInputView.addTextChangedListener(SearchActionModeView.this);
                if (!hasAnchorAndAnimateView()) {
                    getActionBarContainer().getLocationInWindow(mLocation);
                    setTranslationY(mLocation[1]);
                }
            }
        }

        public void onStop() {
            /*if (SearchActionModeView.this.mAnimationMode == 1) {
                ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams) SearchActionModeView.this.mInputView
                        .getLayoutParams();
                localMarginLayoutParams.leftMargin = (SearchActionModeView.this.mBackView
                        .getWidth() - SearchActionModeView.this.getPaddingLeft());
                SearchActionModeView.this.mInputView.setLayoutParams(localMarginLayoutParams);
                SearchActionModeView.this.mInputView.requestFocus();
            }
            while (true) {
                return;
                SearchActionModeView.this.mInputView
                        .removeTextChangedListener(SearchActionModeView.this);
            }*/
            if (mAnimationMode == 1) {
                ViewGroup.MarginLayoutParams lParams = (ViewGroup.MarginLayoutParams) mInputView
                        .getLayoutParams();
                lParams.leftMargin = (mBackView.getWidth() - getPaddingLeft());
                mInputView.setLayoutParams(lParams);
                mInputView.requestFocus();
            } else {
                mInputView.removeTextChangedListener(SearchActionModeView.this);
            }
        }

        public void onUpdate(float percent) {
            mBackView.setTranslationX(mBackViewTranslationXStart + percent
                    * mBackViewTranslationXLength);
            mInputView.setLeft((int) (percent * (mBackViewTranslationXLength - getPaddingLeft()))
                    + getPaddingLeft());
        }
    }

    class SplitActionBarSearchModeAnimationListener extends SimpleSearchModeAnimationListener {
        SplitActionBarSearchModeAnimationListener() {
        }

        public void onUpdate(float percent) {
            ActionBarContainer splitActionBarContainer = SearchActionModeView.this
                    .getSplitActionBarContainer();
            if (splitActionBarContainer != null)
                splitActionBarContainer.setTranslationY(percent
                        * splitActionBarContainer.getHeight());
        }
    }

    @Override
    public void onAnimationRepeat(Animator arg0) {
        // TODO Auto-generated method stub
        
    }
}
