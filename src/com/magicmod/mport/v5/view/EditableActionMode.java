
package com.magicmod.mport.v5.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.internal.R.color;
import com.magicmod.mport.util.UiUtils;
import com.magicmod.mport.v5.widget.Views;
import com.magicmod.mport.v5.widget.menubar.IconMenuBarPresenter;
import com.magicmod.mport.v5.widget.menubar.IconMenuBarView;
import com.magicmod.mport.v5.widget.menubar.MenuBar;
import com.magicmod.mport.v5.widget.menubar.MenuBarItem;
import com.miui.internal.R;

import java.lang.ref.WeakReference;

public class EditableActionMode extends MiuiActionMode implements MenuBar.Callback,
        EditModeTitleBar {
    static final int ACTION_NUM = 2;
    private final Context mContext;
    private final IconMenuBarPresenter mIconMenuBarPresenter;
    private final IconMenuBarView mIconMenuBarView;
    final MenuBar mMenuBar;
    private final TitleBarImpl mTitleBar;

    EditableActionMode(Context context, ViewGroup topContainer, ViewGroup bottomContainer) {
        this.mContext = context;
        this.mMenuBar = new MenuBar(mContext);
        this.mMenuBar.setCallback(this);
        this.mIconMenuBarPresenter = new IconMenuBarPresenter(mContext, bottomContainer,
                R.layout.v5_edit_mode_icon_menu_bar, // 0x6030029,
                R.layout.v5_edit_mode_icon_menu_bar_primary_item, // 0x603002a,
                R.layout.v5_edit_mode_icon_menu_bar_secondary_item // 0x603002b
        );
        this.mIconMenuBarPresenter.setMoreIconDrawable(UiUtils
                .getDrawable(mContext, /*0x6010065*/R.attr.v5_edit_mode_bottom_bar_more_icon));
        this.mIconMenuBarPresenter.setEditMode(true);
        this.mMenuBar.addMenuPresenter(mIconMenuBarPresenter);
        this.mIconMenuBarView = ((IconMenuBarView) mIconMenuBarPresenter
                .getMenuView(bottomContainer));
        this.mTitleBar = new TitleBarImpl(topContainer, this);
    }

    public void finish() {
        if (mCallback != null)
            mCallback.onDestroyActionMode(this);
        this.mTitleBar.detach();
        this.mIconMenuBarView.getPrimaryContainer().startAnimation(
                AnimationUtils.loadAnimation(mContext, /*0x6040012*/R.anim.v5_edit_mode_bottom_out));
        super.finish();
    }

    public CharSequence getButtonText(int id) {
        return this.mTitleBar.getButtonText(id);
    }

    public int getButtonVisiblity(int id) {
        return this.mTitleBar.getButtonVisiblity(id);
    }

    public Menu getMenu() {
        return this.mMenuBar;
    }

    public MenuInflater getMenuInflater() {
        return new MenuInflater(mContext);
    }

    public CharSequence getTitle() {
        return this.mTitleBar.getTitle();
    }

    public void invalidate() {
        /*if (mCallback == null)
            mMenuBar.clear();
        while (true) {
            return;
            this.mMenuBar.stopDispatchingItemsChanged();
            try {
                mCallback.onPrepareActionMode(this, this.mMenuBar);
                this.mMenuBar.startDispatchingItemsChanged();
            } finally {
                this.mMenuBar.startDispatchingItemsChanged();
            }
        }*/
        /*if (mCallback != null) {
            mMenuBar.stopDispatchingItemsChanged();
            try {
                mCallback.onPrepareActionMode(this, mMenuBar);
            } catch (Exception e) {
                mMenuBar.startDispatchingItemsChanged();
            }
            mMenuBar.startDispatchingItemsChanged();
        } else {
            mMenuBar.clear();
        }*/
        if (mCallback == null) {
            mMenuBar.clear();
        }
        mMenuBar.stopDispatchingItemsChanged();
        try {
            mCallback.onPrepareActionMode(this, mMenuBar);
        } catch (Exception e) {
            mMenuBar.startDispatchingItemsChanged();
        }
        mMenuBar.startDispatchingItemsChanged();
    }

    @Override
    public boolean onCreateMenuBarPanel(Menu menu) {
        return true;
    }

    @Override
    public void onMenuBarPanelClose(Menu menu) {
        this.mMenuBar.setCallback(null);
        this.mMenuBar.removeMenuPresenter(this.mIconMenuBarPresenter);
        ((ViewGroup) this.mIconMenuBarView.getParent()).removeView(this.mIconMenuBarView);
    }

    @Override
    public boolean onMenuBarPanelItemSelected(Menu menu, MenuItem item) {
        if (mCallback != null)
            return mCallback.onActionItemClicked(this, item);
        else
            return false;
    }

    @Override
    public void onMenuBarPanelModeChange(Menu menu, int mode) {
    }

    @Override
    public void onMenuBarPanelOpen(Menu menu) {
    }

    @Override
    public boolean onPrepareMenuBarPanel(Menu menu) {
        return true;
    }

    public void setBackground(int resId) {
        this.mTitleBar.setBackground(resId);
    }

    @Override
    public void setBackground(Drawable d) {
        this.mTitleBar.setBackground(d);
    }

    public void setButtonBackground(int id, int resId) {
        this.mTitleBar.setButtonBackground(id, resId);
    }

    @Override
    public void setButtonBackground(int id, Drawable d) {
        this.mTitleBar.setButtonBackground(id, d);
    }

    public void setButtonText(int id, int text) {
        this.mTitleBar.setButtonText(id, text);
    }

    public void setButtonText(int id, CharSequence text) {
        this.mTitleBar.setButtonText(id, text);
    }

    public void setButtonVisibility(int id, int visibility) {
        this.mTitleBar.setButtonVisibility(id, visibility);
    }

    public void setTitle(int resId) {
        this.mTitleBar.setTitle(resId);
    }

    public void setTitle(CharSequence title) {
        this.mTitleBar.setTitle(title);
    }

    public void start(ActionMode.Callback callback) {
        super.start(callback);
        mMenuBar.reopen();
        mTitleBar.attach();
        if (mCallback != null) {
            mMenuBar.stopDispatchingItemsChanged();
            try {
                mCallback.onCreateActionMode(this, this.mMenuBar);
            } catch (Exception e) {
                mMenuBar.startDispatchingItemsChanged();
            }
            mMenuBar.startDispatchingItemsChanged();
        }
        invalidate();
        mIconMenuBarView.getPrimaryContainer().startAnimation(
                AnimationUtils.loadAnimation(this.mContext, /*0x6040011*/R.anim.v5_edit_mode_bottom_in));
    }

    public boolean tryToFinish() {
        boolean flag = false;
        if (!mMenuBar.expand(false))
            flag = super.tryToFinish();
        return flag;
    }

    private static class TitleBarImpl implements EditModeTitleBar, OnClickListener {
        private final MenuItem[] mActionItems = new MenuItem[2];
        private final WeakReference<EditableActionMode> mActionModeRef;
        private final TextView[] mActionViews = new TextView[2];
        final ViewGroup mContainer;
        private final Context mContext;
        
        private final Animation.AnimationListener mDetachListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                EditableActionMode mode = (EditableActionMode) EditableActionMode.TitleBarImpl.this.mActionModeRef
                        .get();
                if (mode != null)
                    mode.mMenuBar.close();
                Views.detach(EditableActionMode.TitleBarImpl.this.mContainer);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        };
        
        final ViewGroup mParent;
        private final TextView mTitleView;

        public TitleBarImpl(ViewGroup parent, EditableActionMode mode) {
            this.mActionModeRef = new WeakReference<EditableActionMode>(mode);
            this.mParent = parent;
            this.mContext = parent.getContext();
            ViewGroup container = (ViewGroup) Views.inflate(mContext,
                    R.layout.v5_edit_mode_title_bar, // 0x603002c,
                    parent, false);
            this.mContainer = container;
            this.mTitleView = ((TextView) container
                    .findViewById(/* 0x1020016 */com.android.internal.R.id.title));
            this.mActionViews[0] = ((TextView) container
                    .findViewById(/* 0x1020019 */com.android.internal.R.id.button1));
            this.mActionViews[0].setOnClickListener(this);
            this.mActionViews[1] = ((TextView) container
                    .findViewById(/* 0x102001a */com.android.internal.R.id.button2));
            this.mActionViews[1].setOnClickListener(this);
            this.mActionItems[0] = new MenuBarItem(mode.mMenuBar, this.mActionViews[0].getId(), 0,
                    this.mContext.getString(/* 0x1040000 */com.android.internal.R.string.cancel));
            this.mActionItems[1] = new MenuBarItem(mode.mMenuBar, this.mActionViews[1].getId(), 0,
                    this.mContext.getString(/* 0x60c01fd */R.string.v5_select_all));
        }

        private TextView getActionView(int id) {
            TextView textview;
            if (id == mActionViews[0].getId())
                textview = mActionViews[0];
            else if (id == mActionViews[1].getId())
                textview = mActionViews[1];
            else
                throw new IllegalArgumentException((new StringBuilder())
                        .append("No ActionView for id=").append(id).toString());
            return textview;
        }

        public void attach() {
            if (mContainer.getParent() == null) {
                mParent.addView(mContainer);
                mContainer.startAnimation(AnimationUtils.loadAnimation(mContext, /*0x6040013*/R.anim.v5_edit_mode_top_in));
            }
        }

        public void detach() {
            Animation anim = AnimationUtils.loadAnimation(this.mContext, /*0x6040014*/R.anim.v5_edit_mode_top_out);
            anim.setAnimationListener(this.mDetachListener);
            this.mContainer.startAnimation(anim);
        }

        public CharSequence getButtonText(int id) {
            return getActionView(id).getText();
        }

        public int getButtonVisiblity(int id) {
            return getActionView(id).getVisibility();
        }

        public CharSequence getTitle() {
            return this.mTitleView.getText();
        }

        @Override
        public void onClick(View v) {
            EditableActionMode mode = (EditableActionMode) mActionModeRef.get();
            if (mode != null) {
                if (v == mActionViews[0]) {
                    MenuItem item = mActionItems[0];
                    item.setTitle(mActionViews[0].getText());
                    if (((MiuiActionMode) (mode)).mCallback != null)
                        ((MiuiActionMode) (mode)).mCallback.onActionItemClicked(mode, item);
                } else if (v == mActionViews[1]) {
                    MenuItem item = mActionItems[1];
                    item.setTitle(mActionViews[1].getText());
                    if (((MiuiActionMode) (mode)).mCallback != null)
                        ((MiuiActionMode) (mode)).mCallback.onActionItemClicked(mode, item);
                }
            }
        }

        public void setBackground(int resId) {
            this.mContainer.setBackgroundResource(resId);
        }

        public void setBackground(Drawable d) {
            this.mContainer.setBackground(d);
        }

        public void setButtonBackground(int id, int resId) {
            getActionView(id).setBackgroundResource(resId);
        }

        public void setButtonBackground(int id, Drawable d) {
            getActionView(id).setBackground(d);
        }

        public void setButtonText(int id, int text) {
            getActionView(id).setText(text);
        }

        public void setButtonText(int id, CharSequence text) {
            getActionView(id).setText(text);
        }

        public void setButtonVisibility(int id, int visibility) {
            getActionView(id).setVisibility(visibility);
        }

        public void setTitle(int text) {
            this.mTitleView.setText(text);
        }

        public void setTitle(CharSequence text) {
            this.mTitleView.setText(text);
        }
    }
}
