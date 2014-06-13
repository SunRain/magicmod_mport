
package com.magicmod.mport.v5.widget.menubar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class IconMenuBarPresenter implements MenuBarPresenter {
    private static final boolean DBG = true;
    private static final int DEFAULT_CLOSE_ANIMATOR_DURATION = 0xc8;//200;
    private static final int DEFAULT_OPEN_ANIMATOR_DURATION = 0xc8;//200;
    private static final int MAX_ITEMS = 0x4;//4;
    private static final String TAG = "IconMenuBarPresenter";
    MenuBarPresenter.Callback mCallback;
    private Animator mCloseMenuBarAnimator;
    private ViewGroup mContainer;
    private Context mContext;
    private int mIconMenuBarLayoutResId;
    private int mIconMenuBarPrimaryItemResId;
    private int mIconMenuBarSecondaryItemResId;
    private boolean mIsEditMode;
    private int mMaxItems = 4;
    private MenuBar mMenu;
    private MenuBarAnimatorListener mMenuBarAnimatorListener;
    private IconMenuBarView mMenuView;
    private Drawable mMoreIconBackgroundDrawable;
    private Drawable mMoreIconDrawable;
    private IconMenuBarPrimaryItemView mMoreView;
    private Animator mOpenMenuBarAnimator;
    private Drawable mPrimaryMaskDrawable;

    public IconMenuBarPresenter(Context context, ViewGroup container, int iconMenuBarLayoutResId,
            int iconMenuBarPrimaryItemResid, int iconMenuBarSecondaryItemResId) {
        this.mContext = context;
        this.mContainer = container;
        this.mIconMenuBarLayoutResId = iconMenuBarLayoutResId;
        this.mIconMenuBarPrimaryItemResId = iconMenuBarPrimaryItemResid;
        this.mIconMenuBarSecondaryItemResId = iconMenuBarSecondaryItemResId;
        this.mMenuBarAnimatorListener = new MenuBarAnimatorListener();
    }

    private void cancelPendingAnimatior() {
        if (this.mOpenMenuBarAnimator != null)
            this.mOpenMenuBarAnimator.cancel();
        if (this.mCloseMenuBarAnimator != null)
            this.mCloseMenuBarAnimator.cancel();
    }

    protected void addItemView(ViewGroup parent, View itemView, int childIndex) {
        ViewGroup localViewGroup = (ViewGroup) itemView.getParent();
        if (localViewGroup != null)
            localViewGroup.removeView(itemView);
        parent.addView(itemView, childIndex);
    }

    public boolean collapseItemActionView(MenuBar menu, MenuBarItem item) {
        return false;
    }

    protected MenuBarView.ItemView createPrimaryItemView() {
        return (MenuBarView.ItemView) View.inflate(this.mContext,
                this.mIconMenuBarPrimaryItemResId, null);
    }

    protected MenuBarView.ItemView createSecondaryItemView() {
        return (MenuBarView.ItemView) View.inflate(this.mContext,
                this.mIconMenuBarSecondaryItemResId, null);
    }

    protected void ensureMenuView() {
        if (this.mMenuView == null)
            getMenuView(this.mContainer);
    }

    public boolean expandItemActionView(MenuBar menu, MenuBarItem item) {
        return false;
    }

    protected boolean filterLeftoverView(ViewGroup parent, int childIndex) {
        boolean flag;
        if (parent.getChildAt(childIndex) != mMoreView) {
            parent.removeViewAt(childIndex);
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }

    public boolean flagActionItems() {
        return false;
    }

    public int getId() {
        return 0;
    }

    public int getMaxItems() {
        return this.mMaxItems;
    }

    @Override
    public MenuBarView getMenuView(ViewGroup root) {
        if (this.mMenuView == null) {
            this.mMenuView = ((IconMenuBarView) View.inflate(this.mContext,
                    this.mIconMenuBarLayoutResId, null));
            this.mMenuView.initialize(this.mMenu);
            this.mMenuView.setPrimaryMaskDrawable(this.mPrimaryMaskDrawable);
            root.addView(this.mMenuView);
            this.mMenuView.setVisibility(8);
        }
        return this.mMenuView;
    }

    protected View getPrimaryItemView(MenuBarItem item, View convertView) {
        /*if ((paramView instanceof MenuBarView.ItemView))
            ;
        for (MenuBarView.ItemView localItemView = (MenuBarView.ItemView) paramView;; localItemView = createPrimaryItemView()) {
            localItemView.initialize(paramMenuBarItem, 0);
            paramMenuBarItem.setTag(localItemView);
            return (View) localItemView;
        }*/
        MenuBarView.ItemView itemView;
        if (convertView instanceof MenuBarView.ItemView)
            itemView = (MenuBarView.ItemView) convertView;
        else
            itemView = createPrimaryItemView();
        itemView.initialize(item, 0);
        item.setTag(itemView);
        return (View) itemView;
    }

    public View getSecondaryItemView(MenuBarItem item, View convertView) {
        MenuBarView.ItemView itemView;
        if (convertView instanceof MenuBarView.ItemView)
            itemView = (MenuBarView.ItemView) convertView;
        else
            itemView = createSecondaryItemView();
        itemView.initialize(item, 0);
        item.setTag(itemView);
        return (View) itemView;
    }

    @Override
    public void initForMenu(Context context, MenuBar menu) {
        this.mContext = context;
        this.mMenu = menu;
    }

    /*public void onCloseMenu(MenuBar paramMenuBar, boolean paramBoolean) {
        if (this.mMenuView == null)
            ;
        while (true) {
            return;
            this.mMenuView.requestExpand(false);
            cancelPendingAnimatior();
            if (paramBoolean) {
                if (this.mMenuView.getVisibility() == 0)
                    playCloseMenuBarAnimator();
            } else {
                this.mMenuView.setVisibility(8);
                if (this.mCallback != null)
                    this.mCallback.onCloseMenu(this.mMenu, true);
            }
        }
    }*/

    public void onCloseMenu(MenuBar menu, boolean animate) {
        if (mMenuView != null) {// goto _L2
            mMenuView.requestExpand(false);
            cancelPendingAnimatior();
            if (animate) {
                if (mMenuView.getVisibility() == 0)
                    playCloseMenuBarAnimator();
            } else {
                mMenuView.setVisibility(8);
                if (mCallback != null)
                    mCallback.onCloseMenu(mMenu, true);
            }
        }
    }
    

    public boolean onExpandMenu(MenuBar menu, boolean expand) {
        boolean flag1;
        if (mMenuView != null)
            flag1 = mMenuView.requestExpand(expand);
        else
            flag1 = false;
        return flag1;
    }

    /*public void onOpenMenu(MenuBar paramMenuBar, boolean paramBoolean) {
        ensureMenuView();
        if (this.mMenuView == null)
            ;
        while (true) {
            return;
            updateMenuView(true);
            this.mMenuView.setVisibility(0);
            cancelPendingAnimatior();
            if (paramBoolean)
                playOpenMenuBarAnimator();
            else if (this.mCallback != null)
                this.mCallback.onOpenMenu(this.mMenu, true);
        }
    }*/
    public void onOpenMenu(MenuBar menu, boolean animate) {
        ensureMenuView();
        if (mMenuView != null) { // goto _L2;
            updateMenuView(true);
            mMenuView.setVisibility(0);
            cancelPendingAnimatior();
            if (animate)
                playOpenMenuBarAnimator();
            else if (mCallback != null)
                mCallback.onOpenMenu(mMenu, true);
        }
/*_L1:
        return;
_L2:
        updateMenuView(true);
        mMenuView.setVisibility(0);
        cancelPendingAnimatior();
        if(flag)
            playOpenMenuBarAnimator();
        else
        if(mCallback != null)
            mCallback.onOpenMenu(mMenu, true);
        if(true) goto _L1; else goto _L3
_L3:*/
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
    }

    public Parcelable onSaveInstanceState() {
        return null;
    }

    public void onScroll(float percent, boolean fromHasMenuBar, boolean toHasMenuBar) {
        if (this.mMenuView != null)
            this.mMenuView.onScroll(percent, fromHasMenuBar, toHasMenuBar);
    }

    public void onScrollStateChanged(int state) {
        if (this.mMenuView != null)
            this.mMenuView.onScrollStateChanged(state);
    }

    protected void playCloseMenuBarAnimator() {
        cancelPendingAnimatior();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this.mMenuView, "Alpha",
                new float[] {
                        1.0F, 0.0F
                });
        LinearLayout primaryContainer = this.mMenuView.getPrimaryContainer();
        float[] arrayOfFloat = new float[2];
        arrayOfFloat[0] = 0.0F;
        arrayOfFloat[1] = this.mMenuView.getPrimaryContainerHeight();
        ObjectAnimator transite = ObjectAnimator.ofFloat(primaryContainer,
                "TranslationY", arrayOfFloat);
        AnimatorSet localAnimatorSet = new AnimatorSet();
        localAnimatorSet.playTogether(new Animator[] {
                alpha, transite
        });
        localAnimatorSet.setDuration(200L);
        localAnimatorSet.addListener(this.mMenuBarAnimatorListener);
        localAnimatorSet.start();
        this.mCloseMenuBarAnimator = localAnimatorSet;
    }

    protected void playOpenMenuBarAnimator() {
        cancelPendingAnimatior();
        LinearLayout primaryContainer = this.mMenuView.getPrimaryContainer();
        int count = primaryContainer.getChildCount();
        for (int j = 0; j < count; j++)
            primaryContainer.getChildAt(j).setTranslationY(0.0F);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this.mMenuView, "Alpha",
                new float[] {
                        0.0F, 1.0F
                });
        float[] arrayOfFloat = new float[2];
        arrayOfFloat[0] = this.mMenuView.getPrimaryContainerHeight();
        arrayOfFloat[1] = 0.0F;
        ObjectAnimator transite = ObjectAnimator.ofFloat(primaryContainer,
                "TranslationY", arrayOfFloat);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(new Animator[] {
                alpha, transite
        });
        set.setDuration(200L);
        set.addListener(this.mMenuBarAnimatorListener);
        set.start();
        this.mOpenMenuBarAnimator = set;
    }

    public void setCallback(MenuBarPresenter.Callback cb) {
        this.mCallback = cb;
    }

    public void setEditMode(boolean isEditMode) {
        this.mIsEditMode = isEditMode;
    }

    public void setMaxItems(int maxItems) {
        if(maxItems >= 4)
            mMaxItems = maxItems;
    }

    public void setMoreIconBackgroundDrawable(Drawable drawable) {
        this.mMoreIconBackgroundDrawable = drawable;
    }

    public void setMoreIconDrawable(Drawable drawable) {
        this.mMoreIconDrawable = drawable;
    }

    public void setPrimaryMaskDrawable(Drawable drawable) {
        this.mPrimaryMaskDrawable = drawable;
    }

    /*public void updateMenuView(boolean paramBoolean)
{
  IconMenuBarView localIconMenuBarView = this.mMenuView;
  if (localIconMenuBarView == null);
  label51: label595: label600: 
  while (true)
  {
    return;
    if (this.mMaxItems < 0)
      this.mMaxItems = localIconMenuBarView.getMaxItems();
    ArrayList localArrayList = this.mMenu.getVisibleItems();
    int i = localArrayList.size();
    boolean bool;
    int j;
    label116: LinearLayout localLinearLayout1;
    int k;
    MenuBarItem localMenuBarItem3;
    View localView4;
    if (i > this.mMaxItems)
    {
      bool = true;
      if (this.mMenuView != null)
      {
        Log.d("IconMenuBarPresenter", "updateMenuView itemCount = " + i + " needsMore = " + bool);
        if (this.mMenu == null)
          continue;
        if (!bool)
          break label256;
        j = -1 + this.mMaxItems;
        localLinearLayout1 = this.mMenuView.getPrimaryContainer();
        k = 0;
        if (k >= j)
          break label271;
        localMenuBarItem3 = (MenuBarItem)localArrayList.get(k);
        localMenuBarItem3.setIsSecondary(false);
        localView4 = localLinearLayout1.getChildAt(k);
        if (!(localView4 instanceof MenuBarView.ItemView))
          break label265;
      }
    }
    else
    {
      for (MenuBarItem localMenuBarItem4 = ((MenuBarView.ItemView)localView4).getItemData(); ; localMenuBarItem4 = null)
      {
        View localView5 = getPrimaryItemView(localMenuBarItem3, localView4);
        localView5.setOnClickListener(null);
        if (localMenuBarItem3 != localMenuBarItem4)
        {
          localView5.setPressed(false);
          localView5.jumpDrawablesToCurrentState();
        }
        if (localView5 != localView4)
        {
          addItemView(localLinearLayout1, localView5, k);
          ((IconMenuBarPrimaryItemView)localView5).setItemInvoker(this.mMenuView);
        }
        k++;
        break label128;
        bool = false;
        break label51;
        break;
        j = localArrayList.size();
        break label116;
      }
      LinearLayout localLinearLayout3;
      int n;
      if (bool)
      {
        View localView1 = localLinearLayout1.getChildAt(k);
        if ((localView1 != this.mMoreView) || (this.mMoreView == null))
        {
          this.mMoreView = this.mMenuView.createMoreItemView((IconMenuBarPrimaryItemView)localView1, this.mIconMenuBarPrimaryItemResId, this.mMoreIconDrawable, this.mMoreIconBackgroundDrawable);
          if (this.mMoreView != localView1)
            addItemView(localLinearLayout1, this.mMoreView, k);
        }
        localLinearLayout3 = this.mMenuView.getSecondaryContainer();
        n = 0;
        if (k >= i)
          break label595;
        MenuBarItem localMenuBarItem1 = (MenuBarItem)localArrayList.get(k);
        localMenuBarItem1.setIsSecondary(true);
        int i2 = n + 1;
        View localView2 = localLinearLayout3.getChildAt(n);
        if ((localView2 instanceof MenuBarView.ItemView));
        for (MenuBarItem localMenuBarItem2 = ((MenuBarView.ItemView)localView2).getItemData(); ; localMenuBarItem2 = null)
        {
          View localView3 = getSecondaryItemView(localMenuBarItem1, localView2);
          if (localMenuBarItem1 != localMenuBarItem2)
          {
            localView3.setPressed(false);
            localView3.jumpDrawablesToCurrentState();
          }
          if (localView3 != localView2)
          {
            addItemView(localLinearLayout3, localView3, k - j);
            ((IconMenuBarSecondaryItemView)localView3).setItemInvoker(this.mMenuView);
          }
          k++;
          n = i2;
          break;
        }
      }
      while (true)
      {
        int i1;
        if (i1 >= localLinearLayout3.getChildCount())
          break label600;
        if (!filterLeftoverView(localLinearLayout3, i1))
        {
          i1++;
          continue;
          this.mMoreView = null;
          while (k < localLinearLayout1.getChildCount())
            if (!filterLeftoverView(localLinearLayout1, k))
              k++;
          int m = 0;
          LinearLayout localLinearLayout2 = this.mMenuView.getSecondaryContainer();
          while (m < localLinearLayout2.getChildCount())
            if (!filterLeftoverView(localLinearLayout2, m))
              m++;
          break;
          i1 = n;
        }
      }
    }
  }
}*/
    
    public void updateMenuView(boolean cleared)
    {
        IconMenuBarView menuView = mMenuView; //v13
        if(menuView == null) {
            //goto _L2; else goto _L1
            return;
        } //else cond_1
        //cond_1 start
        LinearLayout linearlayout;
        int k;
        LinearLayout linearlayout2;
        int i1;
        if(mMaxItems < 0) {
            mMaxItems = menuView.getMaxItems();
        }
        ArrayList visibleItems = mMenu.getVisibleItems(); //v19
        int itemCount = visibleItems.size(); //v11
        boolean needsMore = true; //v15
        int j;
        int primaryCount; // v18
        int i = 0; //v9
        if(itemCount > mMaxItems) { //cond_5标签内容
            needsMore = true;
            //goto_0 start
            ViewGroup parent = mMenuView; //v17
            if (parent != null) { //cond_0 内容
                Log.d("IconMenuBarPresenter", (new StringBuilder()).append("updateMenuView itemCount = ").append(itemCount).append(" needsMore = ").append(needsMore).toString());
                
                if(mMenu != null) { //cond_0内容
                    if (needsMore != false) { //cond_6内容
                        primaryCount = -1 + mMaxItems;
                    }else {
                        primaryCount = visibleItems.size();
                    }
                    LinearLayout container = mMenuView.getPrimaryContainer(); //v7
                    
                    //if (i < primaryCount) { //cond_8内容
                    while(i < primaryCount) {
                        MenuBarItem item = (MenuBarItem)visibleItems.get(i); //v10
                        item.setIsSecondary(false);
                        View convertView = container.getChildAt(i); //v8
                        MenuBarItem oldItem = null;
                        if ((convertView instanceof MenuBarView.ItemView) == true) { //cond_7 in
                            oldItem = ((MenuBarView.ItemView)convertView).getItemData(); //v16
                        } else {
                            oldItem = null;
                        }
                        View itemView = getPrimaryItemView(item, convertView); //v12
                        itemView.setOnClickListener(null);
                        
                        if (item != oldItem) { //cond_3 in
                            itemView.setPressed(false);
                            itemView.jumpDrawablesToCurrentState();
                        } //else { //cond_3 after
                        if (itemView != convertView) { // cond_4 in 
                            addItemView(container, itemView, i);
                            ((IconMenuBarPrimaryItemView)itemView).setItemInvoker(mMenuView);
                        }
                        i++;
                        
                    } //else { //cond_8 after
                    if (needsMore == true) { //cond_f in
                        View moreView; //v14
                        moreView = container.getChildAt(i);
                        //if (moreView == mMoreView) { //cond_9 in
                        //    if (mMoreView == null) { //cond_a in
                        //        mMoreView = mMenuView.createMoreItemView((IconMenuBarPrimaryItemView)moreView, mIconMenuBarPrimaryItemResId, mMoreIconDrawable, mMoreIconBackgroundDrawable);
                        //        if (mMoreView != moreView) {//cond_a in
                        //            addItemView(container, mMoreView, i);
                         ////       } ////cond_a after
                         //       container = mMenuView.getSecondaryContainer();
                         //   } //else cond_a after
                        if (moreView != mMoreView || mMoreView == null) {
                            mMoreView = mMenuView.createMoreItemView((IconMenuBarPrimaryItemView)moreView, mIconMenuBarPrimaryItemResId, mMoreIconDrawable, mMoreIconBackgroundDrawable);
                            if (mMoreView != moreView) {//cond_a in
                                addItemView(container, mMoreView, i);
                            } ////cond_a after
                            container = mMenuView.getSecondaryContainer();
                        
                        
                        
                        
                        
                        } //else cond_9 after
                    } //else /cond_f after
                    //v20, 0x0
                    mMoreView = null;
                    
                } //else { //cond_0 后
                return; //return-void
            } // else cond_0 after
            return; //return-void
        } else { // cond_5 以后内容
            needsMore = false; //v15
            //goto/16 :goto_0
        }
        
        
        
        
        
        
        
        
        if(mMenuView == null)
            continue; /* Loop/switch isn't completed */
        Log.d("IconMenuBarPresenter", (new StringBuilder()).append("updateMenuView itemCount = ").append(itemCount).append(" needsMore = ").append(needsMore).toString());
        if(mMenu == null)
            continue; /* Loop/switch isn't completed */
        if(needsMore)
            j = -1 + mMaxItems;
        else
            j = visibleItems.size();
        linearlayout = mMenuView.getPrimaryContainer();
        k = 0;
        while(k < j) 
        {
            MenuBarItem menubaritem2 = (MenuBarItem)visibleItems.get(k);
            menubaritem2.setIsSecondary(false);
            View view3 = linearlayout.getChildAt(k);
            MenuBarItem menubaritem3;
            View view4;
            if(view3 instanceof MenuBarView.ItemView)
                menubaritem3 = ((MenuBarView.ItemView)view3).getItemData();
            else
                menubaritem3 = null;
            view4 = getPrimaryItemView(menubaritem2, view3);
            view4.setOnClickListener(null);
            if(menubaritem2 != menubaritem3)
            {
                view4.setPressed(false);
                view4.jumpDrawablesToCurrentState();
            }
            if(view4 != view3)
            {
                addItemView(linearlayout, view4, k);
                ((IconMenuBarPrimaryItemView)view4).setItemInvoker(mMenuView);
            }
            k++;
        }
        if(!needsMore)
            break; /* Loop/switch isn't completed */
        View view = linearlayout.getChildAt(k);
        if(view != mMoreView || mMoreView == null)
        {
            mMoreView = mMenuView.createMoreItemView((IconMenuBarPrimaryItemView)view, mIconMenuBarPrimaryItemResId, mMoreIconDrawable, mMoreIconBackgroundDrawable);
            if(mMoreView != view)
                addItemView(linearlayout, mMoreView, k);
        }
        linearlayout2 = mMenuView.getSecondaryContainer();
        i1 = 0;
        while(k < itemCount) 
        {
            MenuBarItem menubaritem = (MenuBarItem)visibleItems.get(k);
            menubaritem.setIsSecondary(true);
            int k1 = i1 + 1;
            View view1 = linearlayout2.getChildAt(i1);
            MenuBarItem menubaritem1;
            View view2;
            if(view1 instanceof MenuBarView.ItemView)
                menubaritem1 = ((MenuBarView.ItemView)view1).getItemData();
            else
                menubaritem1 = null;
            view2 = getSecondaryItemView(menubaritem, view1);
            if(menubaritem != menubaritem1)
            {
                view2.setPressed(false);
                view2.jumpDrawablesToCurrentState();
            }
            if(view2 != view1)
            {
                addItemView(linearlayout2, view2, k - j);
                ((IconMenuBarSecondaryItemView)view2).setItemInvoker(mMenuView);
            }
            k++;
            i1 = k1;
        }
        break MISSING_BLOCK_LABEL_595;
        if(true) goto _L4; else goto _L3

_L1:
        return;
_L2:
        LinearLayout linearlayout;
        int k;
        LinearLayout linearlayout2;
        int i1;
        if(mMaxItems < 0)
            mMaxItems = menuView.getMaxItems();
        ArrayList arraylist = mMenu.getVisibleItems();
        int i = arraylist.size();
        boolean flag1;
        int j;
        if(i > mMaxItems)
            flag1 = true;
        else
            flag1 = false;
        if(mMenuView == null)
            continue; /* Loop/switch isn't completed */
        Log.d("IconMenuBarPresenter", (new StringBuilder()).append("updateMenuView itemCount = ").append(i).append(" needsMore = ").append(flag1).toString());
        if(mMenu == null)
            continue; /* Loop/switch isn't completed */
        if(flag1)
            j = -1 + mMaxItems;
        else
            j = arraylist.size();
        linearlayout = mMenuView.getPrimaryContainer();
        k = 0;
        while(k < j) 
        {
            MenuBarItem menubaritem2 = (MenuBarItem)arraylist.get(k);
            menubaritem2.setIsSecondary(false);
            View view3 = linearlayout.getChildAt(k);
            MenuBarItem menubaritem3;
            View view4;
            if(view3 instanceof MenuBarView.ItemView)
                menubaritem3 = ((MenuBarView.ItemView)view3).getItemData();
            else
                menubaritem3 = null;
            view4 = getPrimaryItemView(menubaritem2, view3);
            view4.setOnClickListener(null);
            if(menubaritem2 != menubaritem3)
            {
                view4.setPressed(false);
                view4.jumpDrawablesToCurrentState();
            }
            if(view4 != view3)
            {
                addItemView(linearlayout, view4, k);
                ((IconMenuBarPrimaryItemView)view4).setItemInvoker(mMenuView);
            }
            k++;
        }
        if(!flag1)
            break; /* Loop/switch isn't completed */
        View view = linearlayout.getChildAt(k);
        if(view != mMoreView || mMoreView == null)
        {
            mMoreView = mMenuView.createMoreItemView((IconMenuBarPrimaryItemView)view, mIconMenuBarPrimaryItemResId, mMoreIconDrawable, mMoreIconBackgroundDrawable);
            if(mMoreView != view)
                addItemView(linearlayout, mMoreView, k);
        }
        linearlayout2 = mMenuView.getSecondaryContainer();
        i1 = 0;
        while(k < i) 
        {
            MenuBarItem menubaritem = (MenuBarItem)arraylist.get(k);
            menubaritem.setIsSecondary(true);
            int k1 = i1 + 1;
            View view1 = linearlayout2.getChildAt(i1);
            MenuBarItem menubaritem1;
            View view2;
            if(view1 instanceof MenuBarView.ItemView)
                menubaritem1 = ((MenuBarView.ItemView)view1).getItemData();
            else
                menubaritem1 = null;
            view2 = getSecondaryItemView(menubaritem, view1);
            if(menubaritem != menubaritem1)
            {
                view2.setPressed(false);
                view2.jumpDrawablesToCurrentState();
            }
            if(view2 != view1)
            {
                addItemView(linearlayout2, view2, k - j);
                ((IconMenuBarSecondaryItemView)view2).setItemInvoker(mMenuView);
            }
            k++;
            i1 = k1;
        }
        break MISSING_BLOCK_LABEL_595;
        if(true) goto _L4; else goto _L3
_L4:
        break; /* Loop/switch isn't completed */
_L3:
        mMoreView = null;
        do
        {
            if(k >= linearlayout.getChildCount())
                break;
            if(!filterLeftoverView(linearlayout, k))
                k++;
        } while(true);
        int l = 0;
        LinearLayout linearlayout1 = mMenuView.getSecondaryContainer();
        while(l < linearlayout1.getChildCount()) 
            if(!filterLeftoverView(linearlayout1, l))
                l++;
        if(true) goto _L1; else goto _L5
_L5:
        j1 = i1;
        while(j1 < linearlayout2.getChildCount()) 
            if(!filterLeftoverView(linearlayout2, j1))
                j1++;
        continue; /* Loop/switch isn't completed */
    }
    private class MenuBarAnimatorListener implements Animator.AnimatorListener {
        public MenuBarAnimatorListener() {
        }

        public void onAnimationCancel(Animator paramAnimator) {
            if (paramAnimator == IconMenuBarPresenter.this.mOpenMenuBarAnimator) {
                IconMenuBarPresenter.this.mMenuView.setAlpha(1.0F);
                LinearLayout localLinearLayout = IconMenuBarPresenter.this.mMenuView
                        .getPrimaryContainer();
                localLinearLayout.setTranslationY(0.0F);
                int i = localLinearLayout.getChildCount();
                for (int j = 0; j < i; j++)
                    localLinearLayout.getChildAt(j).setTranslationY(0.0F);
            }
        }

        public void onAnimationEnd(Animator paramAnimator) {
            if (IconMenuBarPresenter.this.mCallback != null) {
                if (paramAnimator != IconMenuBarPresenter.this.mOpenMenuBarAnimator)
                    break label62;
                IconMenuBarPresenter.this.mMenuView.setVisibility(0);
                IconMenuBarPresenter.this.mCallback.onOpenMenu(IconMenuBarPresenter.this.mMenu,
                        true);
                IconMenuBarPresenter.access$002(IconMenuBarPresenter.this, null);
            }
            while (true) {
                return;
                label62: IconMenuBarPresenter.this.mMenuView.setVisibility(8);
                IconMenuBarPresenter.this.mCallback.onCloseMenu(IconMenuBarPresenter.this.mMenu,
                        true);
                IconMenuBarPresenter.access$302(IconMenuBarPresenter.this, null);
            }
        }

        public void onAnimationRepeat(Animator paramAnimator) {
        }

        public void onAnimationStart(Animator paramAnimator) {
        }
    }
}
