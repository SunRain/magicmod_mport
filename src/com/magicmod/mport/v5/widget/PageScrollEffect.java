
package com.magicmod.mport.v5.widget;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;

import com.android.internal.util.Predicate;
import com.miui.internal.R;

public abstract interface PageScrollEffect {
    public abstract boolean attach(ViewGroup paramViewGroup, int[] paramArrayOfInt);

    public abstract boolean detach(ViewGroup paramViewGroup);

    public abstract void reset();

    public abstract void scroll(float paramFloat, boolean paramBoolean);

    public abstract void setStateListener(OnStateChangeListener paramOnStateChangeListener);

    public static abstract class AbsPageScrollEffect implements PageScrollEffect {
        public static final int EFFECT_ALL_CHILDREN = 0x3;
        public static final int EFFECT_ANY_CHILDREN = 0x2;
        public static final int EFFECT_NONE = 0x0;
        public static final int EFFECT_ONLY_SELF = 0x1;
        //FIXME:what is this integer?
        protected static final Integer KEY_EFFECT_TYPE = Integer.valueOf(/*0x60b008d*/R.id.tag_page_scroll_effect);
        protected final ViewGroup mRoot;

        public AbsPageScrollEffect(ViewGroup root) {
            this.mRoot = root;
        }

        /*protected static boolean chainEffectTypes(ViewGroup paramViewGroup, int[] paramArrayOfInt) {
            boolean bool;
            if ((paramViewGroup == null) || (paramArrayOfInt == null))
                bool = false;
            View localView;
            label56: while (true) {
                return bool;
                bool = true;
                int i = paramArrayOfInt.length;
                for (int j = 0;; j++) {
                    if (j >= i)
                        break label56;
                    localView = paramViewGroup.findViewById(paramArrayOfInt[j]);
                    if (localView == null) {
                        bool = false;
                        break;
                    }
                    if (localView != paramViewGroup)
                        break label58;
                }
            }
            label58: if ((localView instanceof AbsListView))
                setViewEffectType(localView, 3);
            while (true) {
                for (ViewParent localViewParent = localView.getParent(); localViewParent != paramViewGroup; localViewParent = localViewParent
                        .getParent())
                    setViewEffectType((View) localViewParent, 2);
                break;
                if (getEffectType(localView) < 1)
                    setViewEffectType(localView, 1);
            }
        }*/
        /*protected static boolean chainEffectTypes(ViewGroup viewgroup, int ai[])
        {
            if(viewgroup != null && ai != null) goto _L2; else goto _L1
_L1:
            boolean flag = false;
_L4:
            return flag;
_L2:
            flag = true;
            int i = ai.length;
            int j = 0;
            do
            {
                View view;
                if(j < i)
                {
label0:
                    {
                        view = viewgroup.findViewById(ai[j]);
                        if(view != null)
                            break label0;
                        flag = false;
                    }
                }
                if(true)
                    continue;
                if(view != viewgroup)
                {
                    ViewParent viewparent;
                    if(view instanceof AbsListView)
                        setViewEffectType(view, 3);
                    else
                    if(getEffectType(view) < 1)
                        setViewEffectType(view, 1);
                    viewparent = view.getParent();
                    while(viewparent != viewgroup) 
                    {
                        setViewEffectType((View)viewparent, 2);
                        viewparent = viewparent.getParent();
                    }
                }
                j++;
            } while(true);
            if(true) goto _L4; else goto _L3
_L3:
        }*/
        
        /*protected static boolean chainEffectTypes(ViewGroup group, int[] resIds) {
            //v10 0x1;
            if (group != null) { //cond_0 in
                if (resIds == null) { //cond_2 in
                     //cond_0
                    //v2 = 0x0
                    //cond_1
                    //goto_0
                    return false;
                } //cond_2 after
                //v2 = 0x1
                boolean chainAll = true;
                int arr[] = resIds; //v0
                int len = arr.length; //v5
                int i = 0; //v3
                //goto_1
                if (i < len) { //cond_1 in
                    int id = arr[i]; //v4
                    View c = group.findViewById(id); //v1
                    if (c == null) { //cond_3 in
                        //v2 = 0x0
                         return false;//goto :goto_0
                    } //cond_3 after
                    
                    if (c == group) { //cond_5 in
                        //cond_4
                        i++;
                        goto :goto_1
                    } //cond_5 after
                    if ((c instanceof AbsListView) == true) { //cond_7 in
                        //v8 = 0x3
                        setViewEffectType(c, 3);
                        
                        //cond_6
                        //goto_2
                        ViewParent p = c.getParent(); //v6
                        
                        //goto_3
                        
                        if (p != group) { //cond_4 in
                            setViewEffectType((View)p, 2);
                            p = p.getParent();
                            
                            goto :goto_3
                        } //cond_4 after
                        
                    } //cond_7 after
                    
                    int type = getEffectType(c); //v7
                    
                    if (type < 1) { //cond_6
                        setViewEffectType(c, 1);
                        goto :goto_2
                        
                    } //cond_6 after
                    
                    
                } //cond_1 after
                
                
                
            }//cond_0 after
            //v2 = 0x0
            //cond_1
            //goto_0
            return false;
        }*/
        
        protected static boolean chainEffectTypes(ViewGroup group, int[] resIds) {
            if (group != null) { // cond_0 in
                if (resIds == null) { // cond_2 in
                    // cond_0
                    // v2 = 0x0
                    // cond_1
                    // goto_0
                    return false;
                } // cond_2 after

                boolean chainAll = true; //v2
                int arr[] = resIds; // v0
                int len = arr.length; // v5
                // int i = 0; //v3
                for (int i = 0; i < len; i++) {
                    int id = arr[i]; // v4
                    View c = group.findViewById(id); // v1
                    if (c == null) { // cond_3 in
                        // v2 = 0x0
                        return false;//goto :goto_0
                    } // cond_3 after

                    if (c != group) { // cond_5 in
                        if ((c instanceof AbsListView) == true) { // cond_7 in
                            setViewEffectType(c, 3);
                        } else {
                            int type = getEffectType(c); // v7
                            if (type < 1) { // cond_6
                                setViewEffectType(c, 1);
                            } // cond_6 after
                        }
                        ViewParent p = c.getParent(); // v6
                        while (p != group) { // cond_4 in
                            setViewEffectType((View) p, 2);
                            p = p.getParent();
                        }
                    }
                }
                return true;
            }
            return false;
        }
        protected static void clearChain(ViewGroup group) {
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                View v = group.getChildAt(i);
                v.setTag(KEY_EFFECT_TYPE.intValue(), null);
                if ((v instanceof ViewGroup))
                    clearChain((ViewGroup) v);
            }
        }

        protected static boolean existsEffect(View view) {
            boolean bool = false;
            Object tag = view.getTag(KEY_EFFECT_TYPE.intValue());
            if ((tag != null) && (((Integer) tag).intValue() > 0))
                bool = true;
            return bool;
        }

        protected static int getEffectType(View view) {
            Object tag = view.getTag(KEY_EFFECT_TYPE.intValue());
            int i;
            if (tag != null)
                i = ((Integer) tag).intValue();
            else
                i = 0;
            return i;
        }
        
        protected static void setViewEffectType(View view, int type) {
            view.setTag(KEY_EFFECT_TYPE.intValue(), Integer.valueOf(type));
        }

        public boolean attach(ViewGroup v, int resIds[]) {
            boolean success = false;
            if (resIds != null) {
                if (chainEffectTypes(v, resIds)) {
                    setViewEffectType(v, 2);
                    success = true;
                }
            } else {
                setViewEffectType(v, 3);
                success = true;
            }
            return success;
        }

        public boolean detach(ViewGroup v) {
            clearChain(v);
            v.setTag(KEY_EFFECT_TYPE.intValue(), null);
            return true;
        }

        protected static class AllPred implements Predicate<View> {
            public boolean apply(View t) {
                return true;
            }
        }

        protected static class AnyPred implements Predicate<View> {
            public boolean apply(View t) {
                return PageScrollEffect.AbsPageScrollEffect.existsEffect(t);
            }
        }
    }

    public static abstract interface Creator {
        public abstract PageScrollEffect createPageScrollEffect();
    }

    public static abstract interface OnStateChangeListener {
        public abstract void onEffectFinished();

        public abstract void onEffectStarted();
    }
}
