
package com.magicmod.mport.v5.view;

import android.content.Context;
import android.view.ViewGroup;

public class EditableActionMode extends MiuiActionMode implements MenuBar.Callback,
        EditModeTitleBar {
    static final int ACTION_NUM = 2;
    private final Context mContext;
    private final IconMenuBarPresenter mIconMenuBarPresenter;
    private final IconMenuBarView mIconMenuBarView;
    final MenuBar mMenuBar;
    private final TitleBarImpl mTitleBar;

    EditableActionMode(Context paramContext, ViewGroup paramViewGroup1, ViewGroup paramViewGroup2) {
        this.mContext = paramContext;
        this.mMenuBar = new MenuBar(this.mContext);
        this.mMenuBar.setCallback(this);
        this.mIconMenuBarPresenter = new IconMenuBarPresenter(this.mContext, paramViewGroup2,
                100859945, 100859946, 100859947);
        this.mIconMenuBarPresenter.setMoreIconDrawable(UiUtils
                .getDrawable(this.mContext, 100728933));
        this.mIconMenuBarPresenter.setEditMode(true);
        this.mMenuBar.addMenuPresenter(this.mIconMenuBarPresenter);
        this.mIconMenuBarView = ((IconMenuBarView) this.mIconMenuBarPresenter
                .getMenuView(paramViewGroup2));
        this.mTitleBar = new TitleBarImpl(paramViewGroup1, this);
    }

    public void finish() {
        if (this.mCallback != null)
            this.mCallback.onDestroyActionMode(this);
        this.mTitleBar.detach();
        this.mIconMenuBarView.getPrimaryContainer().startAnimation(
                AnimationUtils.loadAnimation(this.mContext, 100925458));
        super.finish();
    }

    public CharSequence getButtonText(int paramInt) {
        return this.mTitleBar.getButtonText(paramInt);
    }

    public int getButtonVisiblity(int paramInt) {
        return this.mTitleBar.getButtonVisiblity(paramInt);
    }

    public Menu getMenu() {
        return this.mMenuBar;
    }

    public MenuInflater getMenuInflater() {
        return new MenuInflater(this.mContext);
    }

    public CharSequence getTitle() {
        return this.mTitleBar.getTitle();
    }

    public void invalidate() {
        if (this.mCallback == null)
            this.mMenuBar.clear();
        while (true) {
            return;
            this.mMenuBar.stopDispatchingItemsChanged();
            try {
                this.mCallback.onPrepareActionMode(this, this.mMenuBar);
                this.mMenuBar.startDispatchingItemsChanged();
            } finally {
                this.mMenuBar.startDispatchingItemsChanged();
            }
        }
    }

    public boolean onCreateMenuBarPanel(Menu paramMenu) {
        return true;
    }

    public void onMenuBarPanelClose(Menu paramMenu) {
        this.mMenuBar.setCallback(null);
        this.mMenuBar.removeMenuPresenter(this.mIconMenuBarPresenter);
        ((ViewGroup) this.mIconMenuBarView.getParent()).removeView(this.mIconMenuBarView);
    }

    public boolean onMenuBarPanelItemSelected(Menu paramMenu, MenuItem paramMenuItem) {
        if (this.mCallback != null)
            ;
        for (boolean bool = this.mCallback.onActionItemClicked(this, paramMenuItem);; bool = false)
            return bool;
    }

    public void onMenuBarPanelModeChange(Menu paramMenu, int paramInt) {
    }

    public void onMenuBarPanelOpen(Menu paramMenu) {
    }

    public boolean onPrepareMenuBarPanel(Menu paramMenu) {
        return true;
    }

    public void setBackground(int paramInt) {
        this.mTitleBar.setBackground(paramInt);
    }

    public void setBackground(Drawable paramDrawable) {
        this.mTitleBar.setBackground(paramDrawable);
    }

    public void setButtonBackground(int paramInt1, int paramInt2) {
        this.mTitleBar.setButtonBackground(paramInt1, paramInt2);
    }

    public void setButtonBackground(int paramInt, Drawable paramDrawable) {
        this.mTitleBar.setButtonBackground(paramInt, paramDrawable);
    }

    public void setButtonText(int paramInt1, int paramInt2) {
        this.mTitleBar.setButtonText(paramInt1, paramInt2);
    }

    public void setButtonText(int paramInt, CharSequence paramCharSequence) {
        this.mTitleBar.setButtonText(paramInt, paramCharSequence);
    }

    public void setButtonVisibility(int paramInt1, int paramInt2) {
        this.mTitleBar.setButtonVisibility(paramInt1, paramInt2);
    }

    public void setTitle(int paramInt) {
        this.mTitleBar.setTitle(paramInt);
    }

    public void setTitle(CharSequence paramCharSequence) {
        this.mTitleBar.setTitle(paramCharSequence);
    }

    public void start(ActionMode.Callback paramCallback) {
        super.start(paramCallback);
        this.mMenuBar.reopen();
        this.mTitleBar.attach();
        if (this.mCallback != null)
            this.mMenuBar.stopDispatchingItemsChanged();
        try {
            this.mCallback.onCreateActionMode(this, this.mMenuBar);
            this.mMenuBar.startDispatchingItemsChanged();
            invalidate();
            this.mIconMenuBarView.getPrimaryContainer().startAnimation(
                    AnimationUtils.loadAnimation(this.mContext, 100925457));
            return;
        } finally {
            this.mMenuBar.startDispatchingItemsChanged();
        }
    }

    public boolean tryToFinish() {
        boolean bool = false;
        if (this.mMenuBar.expand(false))
            ;
        while (true) {
            return bool;
            bool = super.tryToFinish();
        }
    }

    private static class TitleBarImpl implements EditModeTitleBar, View.OnClickListener {
        private final MenuItem[] mActionItems = new MenuItem[2];
        private final WeakReference<EditableActionMode> mActionModeRef;
        private final TextView[] mActionViews = new TextView[2];
        final ViewGroup mContainer;
        private final Context mContext;
        private final Animation.AnimationListener mDetachListener = new Animation.AnimationListener() {
            public void onAnimationEnd(Animation paramAnonymousAnimation) {
                EditableActionMode localEditableActionMode = (EditableActionMode) EditableActionMode.TitleBarImpl.this.mActionModeRef
                        .get();
                if (localEditableActionMode != null)
                    localEditableActionMode.mMenuBar.close();
                Views.detach(EditableActionMode.TitleBarImpl.this.mContainer);
            }

            public void onAnimationRepeat(Animation paramAnonymousAnimation) {
            }

            public void onAnimationStart(Animation paramAnonymousAnimation) {
            }
        };
        final ViewGroup mParent;
        private final TextView mTitleView;

        public TitleBarImpl(ViewGroup paramViewGroup, EditableActionMode paramEditableActionMode) {
            this.mActionModeRef = new WeakReference(paramEditableActionMode);
            this.mParent = paramViewGroup;
            this.mContext = paramViewGroup.getContext();
            ViewGroup localViewGroup = (ViewGroup) Views.inflate(this.mContext, 100859948,
                    paramViewGroup, false);
            this.mContainer = localViewGroup;
            this.mTitleView = ((TextView) localViewGroup.findViewById(16908310));
            this.mActionViews[0] = ((TextView) localViewGroup.findViewById(16908313));
            this.mActionViews[0].setOnClickListener(this);
            this.mActionViews[1] = ((TextView) localViewGroup.findViewById(16908314));
            this.mActionViews[1].setOnClickListener(this);
            this.mActionItems[0] = new MenuBarItem(paramEditableActionMode.mMenuBar,
                    this.mActionViews[0].getId(), 0, this.mContext.getString(17039360));
            this.mActionItems[1] = new MenuBarItem(paramEditableActionMode.mMenuBar,
                    this.mActionViews[1].getId(), 0, this.mContext.getString(101450237));
        }

        private TextView getActionView(int paramInt) {
            if (paramInt == this.mActionViews[0].getId())
                ;
            for (TextView localTextView = this.mActionViews[0];; localTextView = this.mActionViews[1]) {
                return localTextView;
                if (paramInt != this.mActionViews[1].getId())
                    break;
            }
            throw new IllegalArgumentException("No ActionView for id=" + paramInt);
        }

        public void attach() {
            if (this.mContainer.getParent() == null) {
                this.mParent.addView(this.mContainer);
                this.mContainer.startAnimation(AnimationUtils.loadAnimation(this.mContext,
                        100925459));
            }
        }

        public void detach() {
            Animation localAnimation = AnimationUtils.loadAnimation(this.mContext, 100925460);
            localAnimation.setAnimationListener(this.mDetachListener);
            this.mContainer.startAnimation(localAnimation);
        }

        public CharSequence getButtonText(int paramInt) {
            return getActionView(paramInt).getText();
        }

        public int getButtonVisiblity(int paramInt) {
            return getActionView(paramInt).getVisibility();
        }

        public CharSequence getTitle() {
            return this.mTitleView.getText();
        }

        public void onClick(View paramView) {
            EditableActionMode localEditableActionMode = (EditableActionMode) this.mActionModeRef
                    .get();
            if (localEditableActionMode == null)
                ;
            while (true) {
                return;
                if (paramView == this.mActionViews[0]) {
                    MenuItem localMenuItem2 = this.mActionItems[0];
                    localMenuItem2.setTitle(this.mActionViews[0].getText());
                    if (localEditableActionMode.mCallback != null)
                        localEditableActionMode.mCallback.onActionItemClicked(
                                localEditableActionMode, localMenuItem2);
                } else if (paramView == this.mActionViews[1]) {
                    MenuItem localMenuItem1 = this.mActionItems[1];
                    localMenuItem1.setTitle(this.mActionViews[1].getText());
                    if (localEditableActionMode.mCallback != null)
                        localEditableActionMode.mCallback.onActionItemClicked(
                                localEditableActionMode, localMenuItem1);
                }
            }
        }

        public void setBackground(int paramInt) {
            this.mContainer.setBackgroundResource(paramInt);
        }

        public void setBackground(Drawable paramDrawable) {
            this.mContainer.setBackground(paramDrawable);
        }

        public void setButtonBackground(int paramInt1, int paramInt2) {
            getActionView(paramInt1).setBackgroundResource(paramInt2);
        }

        public void setButtonBackground(int paramInt, Drawable paramDrawable) {
            getActionView(paramInt).setBackground(paramDrawable);
        }

        public void setButtonText(int paramInt1, int paramInt2) {
            getActionView(paramInt1).setText(paramInt2);
        }

        public void setButtonText(int paramInt, CharSequence paramCharSequence) {
            getActionView(paramInt).setText(paramCharSequence);
        }

        public void setButtonVisibility(int paramInt1, int paramInt2) {
            getActionView(paramInt1).setVisibility(paramInt2);
        }

        public void setTitle(int paramInt) {
            this.mTitleView.setText(paramInt);
        }

        public void setTitle(CharSequence paramCharSequence) {
            this.mTitleView.setText(paramCharSequence);
        }
    }
}
