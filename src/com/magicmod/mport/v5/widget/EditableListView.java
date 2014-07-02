
package com.magicmod.mport.v5.widget;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.LongSparseArray;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.internal.R.integer;
import com.magicmod.mport.v5.view.EditActionMode;
import com.miui.internal.R;

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

    protected boolean isFooterOrHeader(int position) {
        int headers = getHeaderViewsCount();
        int count = getAdapter().getCount() - getFooterViewsCount();
        boolean flag;
        if (position < headers && position >= count)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isInActionMode() {
        boolean flag;
        if (mActionMode != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    public boolean isItemIdChecked(long itemId) {
        boolean retval = false;
        LongSparseArray<Integer> checkedIdStates = getCheckedIdStates();
        if (checkedIdStates != null) {
            Integer position = (Integer) checkedIdStates.get(itemId);
            if (position != null && isItemChecked(position.intValue()))
                retval = true;
            else
                retval = false;
        }
        return retval;
    }

    public void setAdapter(ListAdapter adapter) {
        this.mListAdapterWrapper = new ListAdapterWrapper();
        this.mListAdapterWrapper.setWrapped(adapter);
        super.setAdapter(this.mListAdapterWrapper);
    }

    public void setMultiChoiceModeListener(AbsListView.MultiChoiceModeListener listener) {
        mMultiChoiceModeListenerWrapper = new MultiChoiceModeListenerWrapper();
        mMultiChoiceModeListenerWrapper.setWrapped(listener);
        super.setMultiChoiceModeListener(mMultiChoiceModeListenerWrapper);
    }

    public void uncheckAll() {
        checkAllInternal(false);
    }

    /*protected void updateCheckStatus(EditActionMode paramEditActionMode)
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
  }*/
    protected void updateCheckStatus(EditActionMode actionMode) {
        if (actionMode != null) {
            int checkItemCount = getCheckedItemCount();
            Resources r = getContext().getResources();
            int j;
            //Menu menu;
            boolean flag;
            if (checkItemCount == 0) {
                actionMode.setTitle(r.getString(/*0x60c01fb*/R.string.v5_edit_mode_title_empty));
            } else {
                String format = r.getQuantityString(R.plurals.v5_edit_mode_title, // 0x6100010,
                        getCheckedItemCount());
                Object aobj[] = new Object[1];
                aobj[0] = Integer.valueOf(getCheckedItemCount());
                actionMode.setTitle(String.format(format, aobj));
            }
            if (isAllChecked())
                j = R.string.v5_deselect_all; //0x60c01fa;
            else
                j = R.string.v5_select_all; //0x60c01fd;
            actionMode.setButton(com.android.internal.R.id.button2, // 0x102001a,
                    j);
            //menu = actionMode.getMenu();
            if (getCheckedItemCount() != 0) {
                flag = true;
            } else {
                flag = false;
            }
            //menu.setGroupEnabled(0, flag);
            actionMode.getMenu().setGroupEnabled(0, flag);
        }
    }

    protected void updateOnScreenCheckedView(View view, int position, long id) {
        if (!isFooterOrHeader(position)) {
            boolean checked = getCheckedItemPositions().get(position);
            CheckBox checkBox = findCheckBoxByView(view);
            if (checkBox != null)
                checkBox.setChecked(checked);
            view.setActivated(checked);
        }
    }

    protected void updateOnScreenCheckedViews() {
        int firstPositioni = getFirstVisiblePosition();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int position = firstPositioni + i;
            updateOnScreenCheckedView(child, position, getAdapter().getItemId(position));
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

        public Object getItem(int position) {
            return this.mWrapped.getItem(position);
        }

        public long getItemId(int position) {
            return this.mWrapped.getItemId(position);
        }

        public int getItemViewType(int position) {
            return this.mWrapped.getItemViewType(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = mWrapped.getView(position, convertView, parent);
            CheckBox checkBox = findCheckBoxByView(view);
            if (checkBox != null) {
                // int j;
                if (isInActionMode()) {
                    // j = 0;
                    checkBox.setVisibility(0);
                } else {
                    // j = 8;
                    checkBox.setVisibility(8);
                }
                // checkBox.setVisibility(j);
                checkBox.setChecked(getCheckedItemPositions().get(position));
            }
            return view;
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

        public boolean isEnabled(int position) {
            return this.mWrapped.isEnabled(position);
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            this.mWrapped.registerDataSetObserver(observer);
        }

        public void setWrapped(ListAdapter wrapped) {
            this.mWrapped = wrapped;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            this.mWrapped.unregisterDataSetObserver(observer);
        }
    }

    private class MultiChoiceModeListenerWrapper implements AbsListView.MultiChoiceModeListener {
        private AbsListView.MultiChoiceModeListener mWrapped;

        public MultiChoiceModeListenerWrapper() {
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            EditActionMode editActionMode = (EditActionMode) mode;
            if (item.getItemId() != /* 0x1020019 */com.android.internal.R.id.button1) {
                if (item.getItemId() == /* 0x102001a */com.android.internal.R.id.button2)
                    if (isAllChecked())
                        uncheckAll();
                    else
                        checkAll();
                // if(true) goto _L4; else goto _L3
                // return mWrapped.onActionItemClicked(editActionMode, item);
            } else {
                editActionMode.finish();
            }
            return mWrapped.onActionItemClicked(editActionMode, item);
        }

        @Override
        /*public boolean onCreateActionMode(ActionMode paramActionMode, Menu paramMenu) {
            if (this.mWrapped.onCreateActionMode(paramActionMode, paramMenu))
                EditableListView
                        .access$002(EditableListView.this, (EditActionMode) paramActionMode);
            for (boolean bool = true;; bool = false)
                return bool;
        }*/
        public boolean onCreateActionMode(ActionMode actionmode, Menu menu) {
            boolean flag;
            if (mWrapped.onCreateActionMode(actionmode, menu)) {
                // EditableListView.access$002(EditableListView.this,
                // (EditActionMode)actionmode);
                mActionMode = (EditActionMode) actionmode;
                flag = true;
            } else {
                flag = false;
            }
            return flag;
        }
        

        @Override
        public void onDestroyActionMode(ActionMode paramActionMode) {
            this.mWrapped.onDestroyActionMode(paramActionMode);
            //EditableListView.access$002(EditableListView.this, null);
            mActionMode = null;
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
                boolean checked) {
            updateOnScreenCheckedView(getChildAt(position - getFirstVisiblePosition()), position,
                    id);
            updateCheckStatus((EditActionMode) mode);
            mWrapped.onItemCheckedStateChanged(mode, position, id, checked);
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(mode, menu);
        }

        public void setWrapped(AbsListView.MultiChoiceModeListener wrapped) {
            this.mWrapped = wrapped;
        }
    }
}

