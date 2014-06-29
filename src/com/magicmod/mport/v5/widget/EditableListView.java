
package com.magicmod.mport.v5.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.internal.R.integer;
import com.magicmod.mport.v5.view.EditActionMode;

public class EditableListView extends ListView {
    private static final int KEY_CHECKBOX = 0x7fffffff;
    private EditActionMode mActionMode;
    private ListAdapterWrapper mListAdapterWrapper;
    private MultiChoiceModeListenerWrapper mMultiChoiceModeListenerWrapper;

    public EditableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected boolean canCheckAll() {
        boolean flag;
        if (getAdapter() != null && mActionMode != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public void checkAll() {
        checkAllInternal(true);
    }

    /*protected void checkAllInternal(boolean paramBoolean) {
        if ((!canCheckAll()) || (isAllChecked() == paramBoolean))
            return;
        if (paramBoolean) {
            ListAdapter localListAdapter = getAdapter();
            int i = getHeaderViewsCount();
            int j = localListAdapter.getCount() - getFooterViewsCount();
            SparseBooleanArray localSparseBooleanArray = getCheckedItemPositions();
            LongSparseArray localLongSparseArray = getCheckedIdStates();
            int k;
            int m;
            int n;
            if ((localLongSparseArray != null) && (localListAdapter.hasStableIds())) {
                k = 1;
                m = getCheckedItemCount();
                n = i;
                label81: if (n >= j)
                    break label198;
                if (localListAdapter.isEnabled(n))
                    break label111;
            }
            label182: while (true) {
                n++;
                break label81;
                k = 0;
                break;
                label111: if (paramBoolean) {
                    if (!localSparseBooleanArray.get(n))
                        m++;
                } else {
                    while (true) {
                        localSparseBooleanArray.put(n, paramBoolean);
                        if (k == 0)
                            break;
                        if (!paramBoolean)
                            break label182;
                        localLongSparseArray.put(localListAdapter.getItemId(n), Integer.valueOf(n));
                        break;
                        if (!localSparseBooleanArray.get(n))
                            break;
                        m--;
                    }
                    localLongSparseArray.delete(localListAdapter.getItemId(n));
                }
            }
            label198: setCheckedItemCount(m);
        }
        while (true) {
            updateOnScreenCheckedViews();
            updateCheckStatus(this.mActionMode);
            break;
            clearChoices();
        }
    }*/
    /*protected void checkAllInternal(boolean checked) {
        if ((!canCheckAll()) || (isAllChecked() == checked)) { //cond_0  cond_1 in
            //goto_0
            return;
        } //cond_0 after ,cond_1 after
        if (!checked) { //cond_8 in
            ListAdapter adapter = getAdapter(); //v0
            int headers = getHeaderViewsCount(); //v6
            int count = adapter.getCount() - getFooterViewsCount(); //v4
            SparseBooleanArray checkStates = getCheckedItemPositions(); //v1
            LongSparseArray<integer> checkedIdStates = getCheckedIdStates(); //v2
            if ((checkedIdStates != null) && adapter.hasStableIds()) { //cond_3 in
                boolean hasStableIds = true; //local v5
                //goto_1
                int checkedItemCount = getCheckedItemCount(); //local v3
                int i = headers; //v7
                //goto_2
                if (i < count) { //cond_7 in
                    if (adapter.isEnabled(i) == false) { //cond_4 in
                        //cond_2
                        //goto3
                        i++;
                        //goto goto_2
                    }  else {//cond_4 after
                        if (checked != false) { //cond_5 in
                            if (checkStates.get(i) == false) { //cond_2 in
                                checkedItemCount++;
                                //goto_4
                                checkStates.put(i, checked);
                                
                                if (hasStableIds != false) { //cond_2 in
                                    if (checked != false) { //cond_6 in
                                        checkedIdStates.put(adapter.getItemId(i), Integer.valueOf(i));
                                        //goto :goto_3
                                    } else {//cond_6 after
                                        checkedIdStates.delete(adapter.getItemId(i));
                                        //goto :goto_3
                                    }
                                } //cond_2 after
                            } //cond_2  after
                        }  else {//cond_5 after
                            if (checkStates.get(i) != false) { //cond_2 in
                                checkedItemCount--;
                                //goto :goto_4
                            } //cond_2 after
                        }
                    }
                } //cond_7 after
                setCheckedItemCount(checkedItemCount);
                
                //goto_5
                updateOnScreenCheckedViews();
                updateCheckStatus(mActionMode);
                //goto :goto_0
                
            } //cond_3 after
            hasStableIds = false;
            goto :goto_1
            
        } //cond_8 after
        clearChoices();
        goto :goto_5
    }*/

    protected void checkAllInternal(boolean checked) {
        if ((!canCheckAll()) || (isAllChecked() == checked)) { // cond_0 cond_1
                                                               // in
            // goto_0
            return;
        }

        if (!checked) {
            ListAdapter adapter = getAdapter(); // v0
            int headers = getHeaderViewsCount(); // v6
            int count = adapter.getCount() - getFooterViewsCount(); // v4
            SparseBooleanArray checkStates = getCheckedItemPositions(); // v1
            LongSparseArray<integer> checkedIdStates = getCheckedIdStates(); // v2
            boolean hasStableIds;// local v5
            if ((checkedIdStates != null) && adapter.hasStableIds()) { // cond_3in
                hasStableIds = true; // local v5
            } else {
                hasStableIds = false; // local v5
            }
            int checkedItemCount = getCheckedItemCount(); // local v3
            // int i = headers; //v7
            for (int i = headers; i < count; i++) {
                if (adapter.isEnabled(i) != false) {

                    if (checked != false) { // cond_5 in
                        if (checkStates.get(i) == false) { // cond_2 in
                            checkedItemCount++;
                        } else {
                            checkedItemCount--;
                        }
                        // goto_4
                        checkStates.put(i, checked);

                        if (hasStableIds != false) { // cond_2 in
                            if (checked != false) { // cond_6 in
                                checkedIdStates.put(adapter.getItemId(i), Integer.valueOf(i));
                                // goto :goto_3
                            } else {// cond_6 after
                                checkedIdStates.delete(adapter.getItemId(i));
                                // goto :goto_3
                            }
                        } // cond_2 after
                    } // cond_2 after
                }
            }
            setCheckedItemCount(checkedItemCount);
        } else {
            clearChoices();
        }
        updateOnScreenCheckedViews();
        updateCheckStatus(mActionMode);
    }
    
    
    
    protected CheckBox findCheckBoxByView(View view) {
        CheckBox checkbox = (CheckBox) view.getTag(/*0x7fffffff*/KEY_CHECKBOX);
        if (checkbox == null) {
            checkbox = (CheckBox) view.findViewById(/*0x1020001*/com.android.internal.R.id.checkbox);
            if (checkbox != null)
                view.setTag(/*0x7fffffff*/KEY_CHECKBOX, checkbox);
        }
        return checkbox;
    }

    public ListAdapter getAdapter() {
        ListAdapter listadapter;
        if (mListAdapterWrapper != null)
            listadapter = mListAdapterWrapper.getWrapped();
        else
            listadapter = null;
        return listadapter;
    }

    /*protected int getEnabledItemCount() {
        ListAdapter localListAdapter = getAdapter();
        int k;
        if (localListAdapter == null)
            k = 0;
        while (true) {
            return k;
            int i = getHeaderViewsCount();
            int j = localListAdapter.getCount() - getFooterViewsCount();
            k = 0;
            for (int m = i; m < j; m++)
                if (localListAdapter.isEnabled(m))
                    k++;
        }
    }*/
    protected int getEnabledItemCount() {
        ListAdapter adapter = getAdapter();
        if (adapter == null) {
            //k = 0;
            return 0;
        } 
        int start = getHeaderViewsCount();
        int end = adapter.getCount() - getFooterViewsCount();
        int count = 0;
        int i = start;
        while (i < end) {
            if (adapter.isEnabled(i))
                count++;
            i++;
        }
        return count;
    }

    public boolean isAllChecked() {
        boolean bool = false;
        if ((getAdapter() != null) && (getCheckedItemCount() == getEnabledItemCount()))
            bool = true;
        return bool;
    }

    protected boolean isFooterOrHeader(int paramInt) {
        int i = getHeaderViewsCount();
        int j = getAdapter().getCount() - getFooterViewsCount();
        if ((paramInt < i) && (paramInt >= j))
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    public boolean isInActionMode() {
        if (this.mActionMode != null)
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    public boolean isItemIdChecked(long paramLong) {
        boolean bool = false;
        LongSparseArray localLongSparseArray = getCheckedIdStates();
        if (localLongSparseArray != null) {
            Integer localInteger = (Integer) localLongSparseArray.get(paramLong);
            if ((localInteger == null) || (!isItemChecked(localInteger.intValue())))
                break label45;
        }
        label45: for (bool = true;; bool = false)
            return bool;
    }

    public void setAdapter(ListAdapter paramListAdapter) {
        this.mListAdapterWrapper = new ListAdapterWrapper();
        this.mListAdapterWrapper.setWrapped(paramListAdapter);
        super.setAdapter(this.mListAdapterWrapper);
    }

    public void setMultiChoiceModeListener(
            AbsListView.MultiChoiceModeListener paramMultiChoiceModeListener) {
        this.mMultiChoiceModeListenerWrapper = new MultiChoiceModeListenerWrapper();
        this.mMultiChoiceModeListenerWrapper.setWrapped(paramMultiChoiceModeListener);
        super.setMultiChoiceModeListener(this.mMultiChoiceModeListenerWrapper);
    }

    public void uncheckAll() {
        checkAllInternal(false);
    }

    protected void updateCheckStatus(EditActionMode paramEditActionMode)
  {
    Resources localResources;
    int j;
    label42: Menu localMenu;
    if (paramEditActionMode != null)
    {
      int i = getCheckedItemCount();
      localResources = getContext().getResources();
      if (i != 0)
        break label77;
      paramEditActionMode.setTitle(localResources.getString(101450235));
      if (!isAllChecked())
        break label120;
      j = 101450234;
      paramEditActionMode.setButton(16908314, j);
      localMenu = paramEditActionMode.getMenu();
      if (getCheckedItemCount() == 0)
        break label127;
    }
    label77: label120: label127: for (boolean bool = true; ; bool = false)
    {
      localMenu.setGroupEnabled(0, bool);
      return;
      String str = localResources.getQuantityString(101711888, getCheckedItemCount());
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(getCheckedItemCount());
      paramEditActionMode.setTitle(String.format(str, arrayOfObject));
      break;
      j = 101450237;
      break label42;
    }
  }

    protected void updateOnScreenCheckedView(View paramView, int paramInt, long paramLong) {
        if (isFooterOrHeader(paramInt))
            ;
        while (true) {
            return;
            boolean bool = getCheckedItemPositions().get(paramInt);
            CheckBox localCheckBox = findCheckBoxByView(paramView);
            if (localCheckBox != null)
                localCheckBox.setChecked(bool);
            paramView.setActivated(bool);
        }
    }

    protected void updateOnScreenCheckedViews() {
        int i = getFirstVisiblePosition();
        int j = getChildCount();
        for (int k = 0; k < j; k++) {
            View localView = getChildAt(k);
            int m = i + k;
            updateOnScreenCheckedView(localView, m, getAdapter().getItemId(m));
        }
    }

    private class ListAdapterWrapper implements ListAdapter {
        private ListAdapter mWrapped;

        public ListAdapterWrapper() {
        }

        public boolean areAllItemsEnabled() {
            return this.mWrapped.areAllItemsEnabled();
        }

        public int getCount() {
            return this.mWrapped.getCount();
        }

        public Object getItem(int paramInt) {
            return this.mWrapped.getItem(paramInt);
        }

        public long getItemId(int paramInt) {
            return this.mWrapped.getItemId(paramInt);
        }

        public int getItemViewType(int paramInt) {
            return this.mWrapped.getItemViewType(paramInt);
        }

        public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
            View localView = this.mWrapped.getView(paramInt, paramView, paramViewGroup);
            CheckBox localCheckBox = EditableListView.this.findCheckBoxByView(localView);
            if (localCheckBox != null)
                if (!EditableListView.this.isInActionMode())
                    break label69;
            label69: for (int i = 0;; i = 8) {
                localCheckBox.setVisibility(i);
                localCheckBox.setChecked(EditableListView.this.getCheckedItemPositions().get(
                        paramInt));
                return localView;
            }
        }

        public int getViewTypeCount() {
            return this.mWrapped.getViewTypeCount();
        }

        public ListAdapter getWrapped() {
            return this.mWrapped;
        }

        public boolean hasStableIds() {
            return this.mWrapped.hasStableIds();
        }

        public boolean isEmpty() {
            return this.mWrapped.isEmpty();
        }

        public boolean isEnabled(int paramInt) {
            return this.mWrapped.isEnabled(paramInt);
        }

        public void registerDataSetObserver(DataSetObserver paramDataSetObserver) {
            this.mWrapped.registerDataSetObserver(paramDataSetObserver);
        }

        public void setWrapped(ListAdapter paramListAdapter) {
            this.mWrapped = paramListAdapter;
        }

        public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver) {
            this.mWrapped.unregisterDataSetObserver(paramDataSetObserver);
        }
    }

    private class MultiChoiceModeListenerWrapper implements AbsListView.MultiChoiceModeListener {
        private AbsListView.MultiChoiceModeListener mWrapped;

        public MultiChoiceModeListenerWrapper() {
        }

        public boolean onActionItemClicked(ActionMode paramActionMode, MenuItem paramMenuItem) {
            EditActionMode localEditActionMode = (EditActionMode) paramActionMode;
            if (paramMenuItem.getItemId() == 16908313)
                localEditActionMode.finish();
            while (true) {
                return this.mWrapped.onActionItemClicked(localEditActionMode, paramMenuItem);
                if (paramMenuItem.getItemId() == 16908314)
                    if (EditableListView.this.isAllChecked())
                        EditableListView.this.uncheckAll();
                    else
                        EditableListView.this.checkAll();
            }
        }

        public boolean onCreateActionMode(ActionMode paramActionMode, Menu paramMenu) {
            if (this.mWrapped.onCreateActionMode(paramActionMode, paramMenu))
                EditableListView
                        .access$002(EditableListView.this, (EditActionMode) paramActionMode);
            for (boolean bool = true;; bool = false)
                return bool;
        }

        public void onDestroyActionMode(ActionMode paramActionMode) {
            this.mWrapped.onDestroyActionMode(paramActionMode);
            EditableListView.access$002(EditableListView.this, null);
        }

        public void onItemCheckedStateChanged(ActionMode paramActionMode, int paramInt,
                long paramLong, boolean paramBoolean) {
            EditableListView.this
                    .updateOnScreenCheckedView(
                            EditableListView.this.getChildAt(paramInt
                                    - EditableListView.this.getFirstVisiblePosition()), paramInt,
                            paramLong);
            EditableListView.this.updateCheckStatus((EditActionMode) paramActionMode);
            this.mWrapped.onItemCheckedStateChanged(paramActionMode, paramInt, paramLong,
                    paramBoolean);
        }

        public boolean onPrepareActionMode(ActionMode paramActionMode, Menu paramMenu) {
            return this.mWrapped.onPrepareActionMode(paramActionMode, paramMenu);
        }

        public void setWrapped(AbsListView.MultiChoiceModeListener paramMultiChoiceModeListener) {
            this.mWrapped = paramMultiChoiceModeListener;
        }
    }
}

