
package com.magicmod.mport.v5.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.magicmod.mport.util.UiUtils;
import com.miui.internal.R;

public class ActionModeWrapper extends FrameLayout 
implements MiuiActionMode.ActionModeListener {

    private MiuiActionMode mActionMode;
    private MiuiActionMode.ActionModeListener mActionModeListener;
    private int mBottomBarTopLineBottomMargin;
    private Drawable mBottomBarTopLineDrawable;
    private boolean mBottomBarTopLineVisible;
    private ViewGroup mBottomContainer;
    private ViewGroup mTopContainer;

    public ActionModeWrapper(Context context) {
        this(context, null);
    }

    public ActionModeWrapper(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionModeWrapper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mBottomBarTopLineDrawable = UiUtils.getDrawable(context, R.attr.v5_bottom_bar_top_line);//0x6010050);
    }

    private MiuiActionMode createEditableActionMode(View originalView,
            android.view.ActionMode.Callback callback) {
        EditableActionMode editableactionmode;
        if (originalView != null) {
            ensureViews();
            editableactionmode = new EditableActionMode(mContext, mTopContainer, mBottomContainer);
        } else {
            editableactionmode = null;
        }
        return editableactionmode;
    }

    private void ensureViews() {
        if (mTopContainer == null) {
            mTopContainer = (ViewGroup) findViewById(/*0x60b005d*/R.id.miro_top_bar_container);
            mBottomContainer = (ViewGroup) findViewById(/*0x60b005a*/R.id.miro_bottom_bar_container);
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() == 4) && (event.getAction() == 1/*flag*/) && (mActionMode != null))
            return mActionMode.tryToFinish();
        else
            return super.dispatchKeyEvent(event);
    }

    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean flag = super.drawChild(canvas, child, drawingTime);
        if (child == mBottomContainer && mBottomBarTopLineVisible
                && mBottomBarTopLineDrawable != null) {
            int lineHeight = mBottomBarTopLineDrawable.getIntrinsicHeight();
            int lineTop = getBottom() - mBottomBarTopLineBottomMargin - lineHeight;
            mBottomBarTopLineDrawable.setBounds(getLeft(), lineTop, getRight(), lineTop + lineHeight);
            mBottomBarTopLineDrawable.draw(canvas);
        }
        return flag;
    }

    public void finishActionMode() {
        if (mActionMode != null)
            mActionMode.finish();
    }

    public void initBottomBarTopLine(int bottomMargin) {
        mBottomBarTopLineBottomMargin = bottomMargin;
    }

    public boolean isActionModeActive() {
        boolean flag;
        if (mActionMode != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void onActionModeFinished(MiuiActionMode mode) {
        mActionMode = null;
        if (mActionModeListener != null)
            mActionModeListener.onActionModeFinished(mode);
    }

    public void onActionModeStarted(MiuiActionMode mode) {
        mActionMode = mode;
        if (mActionModeListener != null)
            mActionModeListener.onActionModeStarted(mode);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        ensureViews();
    }

    public void setActionModeListener(MiuiActionMode.ActionModeListener l) {
        mActionModeListener = l;
    }

    public void setBottomBarTopLineVisible(boolean visible) {
        mBottomBarTopLineVisible = visible;
    }

    public ActionMode startActionMode(MiuiActionMode mode,
            android.view.ActionMode.Callback callback) {
        if (mActionMode != null) {
            mode = null;
        } else {
            mode.setActionModeListener(this);
            mode.start(callback);
            mActionMode = mode;
        }
        return mode;
    }

    public ActionMode startActionModeForChild(View view, android.view.ActionMode.Callback callback) {
        Object obj;
        if (mActionMode != null) {
            obj = null;
        } else {
            obj = createEditableActionMode(view, callback);
            if (obj != null) {
                ((MiuiActionMode) (obj)).setActionModeListener(this);
                ((MiuiActionMode) (obj)).start(callback);
            } else {
                obj = super.startActionModeForChild(view, callback);
            }
        }
        return ((ActionMode) (obj));
    }
}
