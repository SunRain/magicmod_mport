
package com.magicmod.mport.internal.v5.widget;

public class SearchActionModeView extends FrameLayout implements Animator.AnimatorListener,
        ActionModeView, TextWatcher, View.OnClickListener, MessageQueue.IdleHandler {
    private static final int ANIMATE_IDLE = 0;
    private static final int ANIMATE_IN = 1;
    private static final int ANIMATE_OUT = 2;
    private static final int DEFAULT_ANIMATION_DURATION = 200;
    private ActionBarContainer mActionBarContainer;
    private ActionBarView mActionBarView;
    private WeakReference<View> mAnchorView;
    private WeakReference<View> mAnimateView;
    private int mAnimateViewTranslationYLength;
    private int mAnimateViewTranslationYStart;
    private int mAnimateVisibility = 8;
    private int mAnimationMode = 0;
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

    public SearchActionModeView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setAlpha(0.0F);
    }

    private MessageQueue getMessageQueue() {
        return Looper.getMainLooper().getQueue();
    }

    private boolean hasAnchorAndAnimateView() {
        if ((this.mAnchorView != null) && (this.mAnimateView != null))
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    private void notifyAnimation(float paramFloat) {
        if (this.mSearchModeAnimationListeners == null)
            ;
        while (true) {
            return;
            for (int i = 0; i < this.mSearchModeAnimationListeners.size(); i++)
                ((SearchModeAnimationListener) this.mSearchModeAnimationListeners.get(i))
                        .onUpdate(paramFloat);
        }
    }

    private void notifyAnimationEnd() {
        if (this.mSearchModeAnimationListeners == null)
            ;
        while (true) {
            return;
            for (int i = 0; i < this.mSearchModeAnimationListeners.size(); i++)
                ((SearchModeAnimationListener) this.mSearchModeAnimationListeners.get(i)).onStop();
        }
    }

    private void notifyAnimationStart() {
        if (this.mSearchModeAnimationListeners == null)
            ;
        while (true) {
            return;
            for (int i = 0; i < this.mSearchModeAnimationListeners.size(); i++)
                ((SearchModeAnimationListener) this.mSearchModeAnimationListeners.get(i)).onStart();
        }
    }

    private void queueIdleHandler() {
        removeIdleHandler();
        getMessageQueue().addIdleHandler(this);
    }

    private void removeIdleHandler() {
        getMessageQueue().removeIdleHandler(this);
    }

    public void addAnimationListener(SearchModeAnimationListener paramSearchModeAnimationListener) {
        if (paramSearchModeAnimationListener == null)
            ;
        while (true) {
            return;
            if (this.mSearchModeAnimationListeners == null)
                this.mSearchModeAnimationListeners = new ArrayList();
            this.mSearchModeAnimationListeners.add(paramSearchModeAnimationListener);
        }
    }

    public void afterTextChanged(Editable paramEditable) {
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
    }

    protected void allowUpdateContentBorder(boolean paramBoolean) {
        View localView = ((ViewGroup) getRootView()).getChildAt(0);
        if ((localView instanceof IActionBarLayout))
            ((IActionBarLayout) localView).setUpdateContentMarginEnabled(paramBoolean);
    }

    public void animateToVisibility(int paramInt) {
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
    }

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2,
            int paramInt3) {
        if (paramCharSequence == null)
            ;
        for (int i = 0;; i = paramCharSequence.length()) {
            this.mTextLengthBeforeChanged = i;
            return;
        }
    }

    public void closeMode() {
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
    }

    protected void createAnimationListeners() {
        if (this.mSearchModeAnimationListeners == null)
            this.mSearchModeAnimationListeners = new ArrayList();
        this.mSearchModeAnimationListeners.add(new DimViewSearchModeAnimationListener());
        this.mSearchModeAnimationListeners.add(new SearchViewSearchModeAnimationListener());
        if (hasAnchorAndAnimateView()) {
            this.mSearchModeAnimationListeners.add(new ContentViewSearchModeAnimationListener());
            this.mSearchModeAnimationListeners.add(new ActionBarSearchModeAnimationListener());
            this.mSearchModeAnimationListeners.add(new SplitActionBarSearchModeAnimationListener());
        }
    }

    protected void finishAnimation() {
        if (this.mCurrentAnimation != null) {
            this.mCurrentAnimation.cancel();
            this.mCurrentAnimation = null;
        }
    }

    protected ActionBarContainer getActionBarContainer() {
        if (this.mActionBarContainer == null) {
            View localView = getRootView();
            if (localView != null)
                this.mActionBarContainer = ((ActionBarContainer) localView
                        .findViewById(ResourceMapper.resolveReference(this.mContext, 101384199)));
        }
        return this.mActionBarContainer;
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
        if (this.mSplitActionBarContainer == null) {
            View localView = getRootView();
            if (localView != null)
                this.mSplitActionBarContainer = ((ActionBarContainer) localView
                        .findViewById(ResourceMapper.resolveReference(this.mContext, 101384231)));
        }
        return this.mSplitActionBarContainer;
    }

    protected int getSplitActionBarHeight() {
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
    }

    public void initForMode(ActionMode paramActionMode) {
    }

    protected boolean isFragmentViewPagerMode() {
        return getActionBarView().getActionBar().isFragmentViewPagerMode();
    }

    public void killMode() {
        finishAnimation();
        ViewGroup localViewGroup = (ViewGroup) getParent();
        if (localViewGroup != null)
            localViewGroup.removeView(this);
        this.mActionBarContainer = null;
        this.mActionBarView = null;
        if (this.mSearchModeAnimationListeners != null) {
            this.mSearchModeAnimationListeners.clear();
            this.mSearchModeAnimationListeners = null;
        }
        this.mSplitActionBarContainer = null;
    }

    protected ObjectAnimator makeAnimation(int paramInt) {
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
    }

    public void onAnimationCancel(Animator paramAnimator) {
    }

    public void onAnimationEnd(Animator paramAnimator) {
        this.mCurrentAnimation = null;
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
        }
    }

    public void onAnimationRepeat(Animator paramAnimator) {
    }

    public void onAnimationStart(Animator paramAnimator) {
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
    }

    public void onClick(View paramView) {
        if (paramView.getId() == 101384354)
            this.mBackView.performClick();
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mBackView = ((ImageView) findViewById(16908332));
        this.mInputView = ((EditText) findViewById(16908297));
        ((ViewGroup.MarginLayoutParams) this.mBackView.getLayoutParams()).leftMargin = (-getPaddingLeft());
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        if (this.mRequestAnimation) {
            if ((this.mAnimationMode == 1) && (hasAnchorAndAnimateView()))
                ((View) this.mAnimateView.get())
                        .setTranslationY(-this.mAnimateViewTranslationYLength);
            queueIdleHandler();
            this.mRequestAnimation = false;
        }
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2,
            int paramInt3) {
    }

    protected void pollViews() {
        getActionBarView();
        getActionBarContainer();
        getSplitActionBarContainer();
    }

    public boolean queueIdle() {
        this.mCurrentAnimation.start();
        return false;
    }

    public void removeAnimationListener(SearchModeAnimationListener paramSearchModeAnimationListener) {
        if (paramSearchModeAnimationListener == null)
            ;
        while (true) {
            return;
            if (this.mSearchModeAnimationListeners != null)
                this.mSearchModeAnimationListeners.remove(paramSearchModeAnimationListener);
        }
    }

    public void setAnchorView(View paramView) {
        if (paramView != null)
            this.mAnchorView = new WeakReference(paramView);
    }

    public void setAnchorViewHint(CharSequence paramCharSequence) {
        this.mInputView.setHint(paramCharSequence);
    }

    public void setAnimateView(View paramView) {
        if (paramView != null)
            this.mAnimateView = new WeakReference(paramView);
    }

    protected void setContentViewMargin(int paramInt1, int paramInt2) {
        View localView = getRootView().findViewById(16908290);
        if (localView != null) {
            ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams) localView
                    .getLayoutParams();
            localMarginLayoutParams.bottomMargin = paramInt2;
            localMarginLayoutParams.topMargin = paramInt1;
            localView.setLayoutParams(localMarginLayoutParams);
        }
    }

    public void setOnBackClickListener(View.OnClickListener paramOnClickListener) {
        this.mBackView.setOnClickListener(paramOnClickListener);
    }

    public void setResultView(View paramView) {
        if (paramView != null) {
            this.mResusltView = new WeakReference(paramView);
            ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
            if ((localLayoutParams instanceof ViewGroup.MarginLayoutParams)) {
                this.mResultViewOriginMarginTop = ((ViewGroup.MarginLayoutParams) localLayoutParams).topMargin;
                this.mResultViewOriginMarginBottom = ((ViewGroup.MarginLayoutParams) localLayoutParams).bottomMargin;
                this.mResultViewSet = true;
            }
        }
    }

    protected void setResultViewMargin(boolean paramBoolean) {
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
    }

    public void setValue(float paramFloat) {
        notifyAnimation(paramFloat);
    }

    protected void showSoftInputPanel(boolean paramBoolean) {
        InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
        if (paramBoolean) {
            localInputMethodManager.viewClicked(this.mInputView);
            localInputMethodManager.showSoftInput(this.mInputView, 0);
        }
        while (true) {
            return;
            localInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
        }
    }

    class ActionBarSearchModeAnimationListener extends SimpleSearchModeAnimationListener {
        ActionBarSearchModeAnimationListener() {
        }

        public void onStop() {
            if (SearchActionModeView.this.mAnimationMode == 1) {
                View localView = SearchActionModeView.this.getActionBarContainer()
                        .getTabContainer();
                if (localView != null)
                    localView.setVisibility(8);
            }
        }

        public void onUpdate(float paramFloat) {
            ActionBarContainer localActionBarContainer = SearchActionModeView.this
                    .getActionBarContainer();
            if (localActionBarContainer != null)
                localActionBarContainer.setTranslationY(-paramFloat
                        * localActionBarContainer.getHeight());
        }
    }

    class ContentViewSearchModeAnimationListener extends SimpleSearchModeAnimationListener {
        ContentViewSearchModeAnimationListener() {
        }

        public void onStart() {
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
        }

        public void onStop() {
            if (SearchActionModeView.this.mAnimationMode == 2) {
                SearchActionModeView.this.mInputView.clearFocus();
                ((View) SearchActionModeView.this.mAnimateView.get()).setTranslationY(0.0F);
                ((View) SearchActionModeView.this.mAnchorView.get()).setAlpha(1.0F);
            }
        }

        public void onUpdate(float paramFloat) {
            float f = SearchActionModeView.this.mAnimateViewTranslationYStart + paramFloat
                    * SearchActionModeView.this.mAnimateViewTranslationYLength;
            ((View) SearchActionModeView.this.mAnimateView.get()).setTranslationY(f);
            SearchActionModeView.this
                    .setTranslationY(SearchActionModeView.this.mInputViewTranslationYStart
                            + paramFloat * SearchActionModeView.this.mInputViewTranslationYLength);
        }
    }

    class DimViewSearchModeAnimationListener extends SimpleSearchModeAnimationListener {
        DimViewSearchModeAnimationListener() {
        }

        public void onStart() {
            if (SearchActionModeView.this.mAnimationMode == 1) {
                SearchActionModeView.access$1302(SearchActionModeView.this,
                        SearchActionModeView.this.getRootView().findViewById(101384354));
                SearchActionModeView.this.mDimView.setOnClickListener(SearchActionModeView.this);
                SearchActionModeView.this.mDimView.setVisibility(0);
                SearchActionModeView.this.mDimView.setAlpha(0.0F);
            }
        }

        public void onStop() {
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
        }

        public void onUpdate(float paramFloat) {
            SearchActionModeView.this.mDimView.setAlpha(paramFloat);
        }
    }

    class SearchViewSearchModeAnimationListener extends SimpleSearchModeAnimationListener {
        SearchViewSearchModeAnimationListener() {
        }

        public void onStart() {
            if (SearchActionModeView.this.mAnimationMode == 1) {
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
            }
        }

        public void onStop() {
            if (SearchActionModeView.this.mAnimationMode == 1) {
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
            }
        }

        public void onUpdate(float paramFloat) {
            SearchActionModeView.this.mBackView
                    .setTranslationX(SearchActionModeView.this.mBackViewTranslationXStart
                            + paramFloat * SearchActionModeView.this.mBackViewTranslationXLength);
            SearchActionModeView.this.mInputView
                    .setLeft((int) (paramFloat * (SearchActionModeView.this.mBackViewTranslationXLength - SearchActionModeView.this
                            .getPaddingLeft())) + SearchActionModeView.this.getPaddingLeft());
        }
    }

    class SplitActionBarSearchModeAnimationListener extends SimpleSearchModeAnimationListener {
        SplitActionBarSearchModeAnimationListener() {
        }

        public void onUpdate(float paramFloat) {
            ActionBarContainer localActionBarContainer = SearchActionModeView.this
                    .getSplitActionBarContainer();
            if (localActionBarContainer != null)
                localActionBarContainer.setTranslationY(paramFloat
                        * localActionBarContainer.getHeight());
        }
    }
}
