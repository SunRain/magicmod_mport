
package com.magicmod.mport.util;

import android.R;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.util.TypedValue;

import java.util.HashSet;

public class UiUtils {
    static final String MIUI_TYPEFACE_FAMILY = "miui";
    static HashSet<String> mFontsWhiteList;
    static int sActionBarOverlayHeight;
    static Typeface[] sCurrentTypefaces;
    private static HashSet<String> sFloatingWindowWhiteList;
    static int sSplitActionBarOverlayHeight;
    static int sSplitActionMenuHeight;
    static SparseArray<Integer> sStateAttributeIndexes;
    static int sThemeChanged;
    @SuppressWarnings("unchecked")
    static SimplePool.PoolInstance<TypedValue> sTypedValuePool = SimplePool.newInsance(
            new SimplePool.Manager() {
                public TypedValue createInstance() {
                    return new TypedValue();
                }
            }, 4);
    static SparseArray<int[]> sViewStates;

    static {
        sThemeChanged = -1;
        sSplitActionMenuHeight = -1;
        sActionBarOverlayHeight = -1;
        sSplitActionBarOverlayHeight = -1;
        sStateAttributeIndexes = new SparseArray();
        sViewStates = new SparseArray();
        //sStateAttributeIndexes.put(16842908, Integer.valueOf(1));
        //sStateAttributeIndexes.put(16842910, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(16842911, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(16842912, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(16842913, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(16842914, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(16842915, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(16842916, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(16842917, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(16842918, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(16842919, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(16842921, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(16843518, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(100728889, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(100728886, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(100728888, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        //sStateAttributeIndexes.put(100728887, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        
        
        sStateAttributeIndexes.put(R.attr.state_focused, Integer.valueOf(1));
        sStateAttributeIndexes.put(R.attr.state_enabled, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(R.attr.state_checkable, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(R.attr.state_checked, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(R.attr.state_selected, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(R.attr.state_active, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(R.attr.state_single, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(R.attr.state_first, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(R.attr.state_middle, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(R.attr.state_last, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(R.attr.state_pressed, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(R.attr.state_expanded, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(R.attr.state_empty, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        
        sStateAttributeIndexes.put(miui.R.attr.state_single, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(miui.R.attr.state_first, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(miui.R.attr.state_middle, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        sStateAttributeIndexes.put(miui.R.attr.state_last, Integer.valueOf(1 << sStateAttributeIndexes.size()));
        
        
        sFloatingWindowWhiteList = new HashSet();
        sFloatingWindowWhiteList.add("com.facebook.orca");
    }

    /*public static int getActionBarOverlayHeight(Context paramContext) {
        updateHeightValuesSinceThemeChanged(paramContext);
        return sActionBarOverlayHeight;
    }*/

    public static boolean getBoolean(Context context, int attrId, boolean defValue) {
        TypedValue localTypedValue = (TypedValue) sTypedValuePool.acquire();
        boolean bool = defValue;
        //if (context.getTheme().resolveAttribute(attrId, localTypedValue, true)) {
            //if (localTypedValue.type == 18)
            //    break label48;
            ///bool = false;
        //}
        //while (true) {
        //    sTypedValuePool.release(localTypedValue);
        //    return bool;
        //    label48: if (localTypedValue.data != 0)
        //        bool = true;
        //    else
         //       bool = false;
        //}
        if (context.getTheme().resolveAttribute(attrId, localTypedValue, true)) {
            sTypedValuePool.release(localTypedValue);
            return defValue;
        } else {
            //iget v4, v1, Landroid/util/TypedValue;->type:I
            // const/16 v5, 0x12
            //what is this?
            if (localTypedValue.type == 18) {
                if (localTypedValue.data == 0) {
                    bool = false;
                } else {
                    bool = true;
                }
                sTypedValuePool.release(localTypedValue);
            } else {
                bool = false;
            }
        }
        
        return bool;
    }

    public static int getColor(Context context, int attrId) {
        return context.getResources().getColor(resolveAttribute(context, attrId));
    }

    public static Drawable getDrawable(Context context, int attrId) {
        int i = resolveAttribute(context, attrId);
        //if (i > 0)
       //     ;
        //for (Drawable localDrawable = context.getResources().getDrawable(i);; localDrawable = null)
       //     return localDrawable;
        if (i>0) {
            return context.getResources().getDrawable(i);
        } else {
            return null;
        }
    }

    static int getIndexOfStates(int state) {
        int i = 0;
        Integer localInteger;
        /*if (paramInt != 0) {
            localInteger = (Integer) sStateAttributeIndexes.get(paramInt);
            if (localInteger != null)
                break label64;
            if (sStateAttributeIndexes.size() >= 32)
                throw new IllegalArgumentException("State attribute cannot exceed 32!");
            i = 1 << sStateAttributeIndexes.size();
            sStateAttributeIndexes.put(paramInt, Integer.valueOf(i));
        }
        while (true) {
            return i;
            label64: i = localInteger.intValue();
        }*/
        if (state != 0) {
            localInteger = (Integer) sStateAttributeIndexes.get(state);
            if (localInteger != null) {
                i = localInteger.intValue();
            }
            if (sStateAttributeIndexes.size() >= 32) { // 32? means what?
                throw new IllegalArgumentException("State attribute cannot exceed 32!");
            }
            i = 1 << sStateAttributeIndexes.size();
            sStateAttributeIndexes.put(state, Integer.valueOf(i));
        }
        return i;
        
    }

    static int getIndexOfStates(int[] paramArrayOfInt) {
        int i = 0;
        if (paramArrayOfInt != null)
            for (int j = 0; j < paramArrayOfInt.length; j++)
                i |= getIndexOfStates(paramArrayOfInt[j]);
        return i;
    }

    public static int getMiuiUiVersion(Context paramContext) {
        int i = -1;
        TypedValue localTypedValue = (TypedValue) sTypedValuePool.acquire();
        if (paramContext.getTheme().resolveAttribute(100728870, localTypedValue, true))
            i = localTypedValue.data;
        sTypedValuePool.release(localTypedValue);
        return i;
    }

    /*public static int getSplitActionBarOverlayHeight(Context paramContext) {
        updateHeightValuesSinceThemeChanged(paramContext);
        return sSplitActionBarOverlayHeight;
    }*/

    /*public static int getSplitActionMenuHeight(Context paramContext) {
        updateHeightValuesSinceThemeChanged(paramContext);
        return sSplitActionMenuHeight;
    }*/

    /*public static int[] getViewStates(int[] paramArrayOfInt, int paramInt) {
        int[] arrayOfInt = paramArrayOfInt;
        int i;
        if (paramInt != 0) {
            i = getIndexOfStates(paramArrayOfInt) | getIndexOfStates(paramInt);
            arrayOfInt = (int[]) sViewStates.get(i);
            if (arrayOfInt == null)
                if (paramArrayOfInt == null)
                    break label75;
        }
        label75: for (int j = paramArrayOfInt.length;; j = 0) {
            arrayOfInt = new int[j + 1];
            if (j != 0)
                System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, paramArrayOfInt.length);
            arrayOfInt[j] = paramInt;
            sViewStates.put(i, arrayOfInt);
            return arrayOfInt;
        }
    }*/

    /*public static boolean isFloatingWindowAllowed(String paramString) {
        return sFloatingWindowWhiteList.contains(paramString);
    }*/

    /*ublic static boolean isV5Ui(Context paramContext) {
        if (getMiuiUiVersion(paramContext) == 5)
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }*/

   /*ublic static Typeface replaceTypeface(Context paramContext, Typeface paramTypeface) {
        int i = 0;
        Typeface localTypeface = null;
        if ((usingMiuiFonts(paramContext))
                && ((paramTypeface == null) || (paramTypeface == Typeface.DEFAULT)
                        || (paramTypeface == Typeface.DEFAULT_BOLD) || (paramTypeface == Typeface.SANS_SERIF)))
            if (sCurrentTypefaces != null)
                ;
        while (true) {
            try {
                if (sCurrentTypefaces == null) {
                    Typeface[] arrayOfTypeface = new Typeface[4];
                    arrayOfTypeface[0] = Typeface.create("miui", 0);
                    arrayOfTypeface[1] = Typeface.create("miui", 1);
                    arrayOfTypeface[2] = Typeface.create("miui", 2);
                    arrayOfTypeface[3] = Typeface.create("miui", 3);
                    sCurrentTypefaces = arrayOfTypeface;
                }
                if (paramTypeface == null) {
                    localTypeface = sCurrentTypefaces[i];
                    if (localTypeface != null)
                        break label137;
                    return paramTypeface;
                }
            } finally {
            }
            i = paramTypeface.getStyle();
            continue;
            label137: paramTypeface = localTypeface;
        }
    }
*/
    public static int resolveAttribute(Context context, int resid) {
        TypedValue outValue = (TypedValue) sTypedValuePool.acquire();
        int i = -1;
        if (context.getTheme().resolveAttribute(resid, outValue, true))
            i = outValue.resourceId;
        sTypedValuePool.release(outValue);
        return i;
    }

    /*private static void updateHeightValuesSinceThemeChanged(Context paramContext) {
        if (sThemeChanged != paramContext.getResources().getConfiguration().extraConfig.themeChanged) {
            sThemeChanged = paramContext.getResources().getConfiguration().extraConfig.themeChanged;
            sSplitActionMenuHeight = 0;
            sActionBarOverlayHeight = 0;
            sSplitActionBarOverlayHeight = 0;
            if (isV5Ui(paramContext)) {
                Drawable localDrawable = getDrawable(paramContext, 100728900);
                if (localDrawable != null) {
                    Rect localRect = new Rect();
                    localDrawable.getPadding(localRect);
                    sSplitActionMenuHeight = localDrawable.getIntrinsicHeight() - localRect.bottom
                            - localRect.top;
                }
                sActionBarOverlayHeight = paramContext.getResources().getDimensionPixelSize(
                        101318684);
                sSplitActionBarOverlayHeight = paramContext.getResources().getDimensionPixelSize(
                        101318715);
            }
        }
    }*/

    /*public static boolean usingMiuiFonts(Context paramContext) {
        if (isV5Ui(paramContext))
            ;
        for (boolean bool = true;; bool = mFontsWhiteList.contains(paramContext.getPackageName())) {
            return bool;
            if (mFontsWhiteList == null) {
                mFontsWhiteList = new HashSet();
                mFontsWhiteList.add("com.tencent.mm");
                mFontsWhiteList.add("com.tencent.mobileqq");
                mFontsWhiteList.add("com.UCMobile");
                mFontsWhiteList.add("com.qzone");
                mFontsWhiteList.add("com.sina.weibo");
                mFontsWhiteList.add("com.qvod.player");
                mFontsWhiteList.add("com.qihoo360.mobilesafe");
                mFontsWhiteList.add("com.kugou.android");
                mFontsWhiteList.add("com.taobao.taobao");
                mFontsWhiteList.add("com.baidu.BaiduMap");
                mFontsWhiteList.add("com.youku.phone");
                mFontsWhiteList.add("com.sds.android.ttpod");
                mFontsWhiteList.add("com.qihoo.appstore");
                mFontsWhiteList.add("com.pplive.androidphone");
                mFontsWhiteList.add("com.tencent.minihd.qq");
                mFontsWhiteList.add("tv.pps.mobile");
                mFontsWhiteList.add("com.xiaomi.channel");
                mFontsWhiteList.add("com.shuqi.controller");
                mFontsWhiteList.add("com.storm.smart");
                mFontsWhiteList.add("com.tencent.qbx");
                mFontsWhiteList.add("com.moji.mjweather");
                mFontsWhiteList.add("com.wandoujia.phoenix2");
                mFontsWhiteList.add("com.renren.mobile.android");
                mFontsWhiteList.add("com.duokan.reader");
                mFontsWhiteList.add("com.immomo.momo");
                mFontsWhiteList.add("com.tencent.news");
                mFontsWhiteList.add("com.tencent.qqmusic");
                mFontsWhiteList.add("com.qiyi.video");
                mFontsWhiteList.add("com.baidu.video");
                mFontsWhiteList.add("com.tencent.WBlog");
                mFontsWhiteList.add("qsbk.app");
                mFontsWhiteList.add("com.netease.newsreader.activity");
                mFontsWhiteList.add("com.sohu.newsclient");
                mFontsWhiteList.add("com.tencent.mtt");
                mFontsWhiteList.add("com.baidu.tieba");
                mFontsWhiteList.add("com.wochacha");
                mFontsWhiteList.add("com.tencent.qqpimsecure");
                mFontsWhiteList.add("com.xiaomi.shop");
                mFontsWhiteList.add("com.mt.mtxx.mtxx");
                mFontsWhiteList.add("com.qihoo360.mobilesafe.opti.powerctl");
                mFontsWhiteList.add("com.dragon.android.pandaspace");
                mFontsWhiteList.add("cn.etouch.ecalendar");
                mFontsWhiteList.add("com.changba");
                mFontsWhiteList.add("com.xiaomi.xmsf");
                mFontsWhiteList.add("com.tencent.qqlive");
                mFontsWhiteList.add("com.chaozh.iReaderFree");
                mFontsWhiteList.add("com.snda.wifilocating");
                mFontsWhiteList.add("com.ijinshan.kbatterydoctor");
                mFontsWhiteList.add("com.duowan.mobile");
                mFontsWhiteList.add("com.hiapk.marketpho");
                mFontsWhiteList.add("com.qihoo360.launcher");
                mFontsWhiteList.add("com.qihoo360.mobilesafe.opti");
                mFontsWhiteList.add("cn.com.fetion");
                mFontsWhiteList.add("com.nd.android.pandahome2");
                mFontsWhiteList.add("com.youdao.dict");
                mFontsWhiteList.add("com.eg.android.AlipayGphone");
                mFontsWhiteList.add("cn.kuwo.player");
                mFontsWhiteList.add("cn.wps.moffice");
                mFontsWhiteList.add("com.alibaba.mobileim");
                mFontsWhiteList.add("com.letv.android.client");
                mFontsWhiteList.add("com.baidu.searchbox");
                mFontsWhiteList.add("com.funshion.video.mobile");
                mFontsWhiteList.add("com.gau.go.launcherex");
                mFontsWhiteList.add("cn.opda.a.phonoalbumshoushou");
                mFontsWhiteList.add("com.qq.reader");
                mFontsWhiteList.add("com.duomi.android");
                mFontsWhiteList.add("com.qihoo.browser");
                mFontsWhiteList.add("com.meitu.meiyancamera");
                mFontsWhiteList.add("com.nd.android.pandareader");
                mFontsWhiteList.add("com.kingsoft");
                mFontsWhiteList.add("com.cleanmaster.mguard");
                mFontsWhiteList.add("com.sohu.sohuvideo");
                mFontsWhiteList.add("com.jingdong.app.mall");
                mFontsWhiteList.add("bubei.tingshu");
                mFontsWhiteList.add("com.alipay.android.app");
                mFontsWhiteList.add("vStudio.Android.Camera360");
                mFontsWhiteList.add("com.androidesk");
                mFontsWhiteList.add("com.ss.android.article.news");
                mFontsWhiteList.add("org.funship.findsomething.withRK");
                mFontsWhiteList.add("com.mybook66");
                mFontsWhiteList.add("com.tencent.token");
                mFontsWhiteList.add("com.tmall.wireless");
                mFontsWhiteList.add("com.tencent.qqgame.qqlordwvga");
                mFontsWhiteList.add("com.budejie.www");
                mFontsWhiteList.add("com.sankuai.meituan");
                mFontsWhiteList.add("com.google.android.apps.maps");
                mFontsWhiteList.add("com.kascend.video");
                mFontsWhiteList.add("com.tencent.android.pad");
                mFontsWhiteList.add("com.muzhiwan.market");
                mFontsWhiteList.add("com.mymoney");
                mFontsWhiteList.add("com.baidu.browser.apps");
                mFontsWhiteList.add("com.geili.koudai");
                mFontsWhiteList.add("com.baidu.news");
                mFontsWhiteList.add("com.tencent.androidqqmail");
                mFontsWhiteList.add("com.myzaker.ZAKER_Phone");
                mFontsWhiteList.add("com.ifeng.news2");
                mFontsWhiteList.add("com.handsgo.jiakao.android");
                mFontsWhiteList.add("com.hexin.plat.android");
                mFontsWhiteList.add("com.tencent.qqphonebook");
                mFontsWhiteList.add("my.beautyCamera");
                mFontsWhiteList.add("com.autonavi.minimap");
                mFontsWhiteList.add("com.cubic.autohome");
                mFontsWhiteList.add("com.clov4r.android.nil");
                mFontsWhiteList.add("com.yangzhibin.chengrenxiaohua");
                mFontsWhiteList.add("com.dianxinos.powermanager");
                mFontsWhiteList.add("com.ijinshan.duba");
                mFontsWhiteList.add("com.wuba");
                mFontsWhiteList.add("sina.mobile.tianqitong");
                mFontsWhiteList.add("com.mandi.lol");
                mFontsWhiteList.add("com.duowan.lolbox");
                mFontsWhiteList.add("com.android.chrome");
                mFontsWhiteList.add("com.chinamworld.main");
                mFontsWhiteList.add("com.ss.android.essay.joke");
                mFontsWhiteList.add("air.com.tencent.qqpasture");
                mFontsWhiteList.add("com.kingreader.framework");
                mFontsWhiteList.add("cn.ibuka.manga.ui");
                mFontsWhiteList.add("com.ting.mp3.qianqian.android");
                mFontsWhiteList.add("com.jiubang.goscreenlock");
                mFontsWhiteList.add("com.shoujiduoduo.ringtone");
                mFontsWhiteList.add("com.lbe.security");
                mFontsWhiteList.add("com.snda.youni");
                mFontsWhiteList.add("com.jiasoft.swreader");
                mFontsWhiteList.add("com.anyview");
                mFontsWhiteList.add("com.baidu.appsearch");
                mFontsWhiteList.add("com.sohu.inputmethod.sogou");
                mFontsWhiteList.add("com.mxtech.videoplayer.ad");
                mFontsWhiteList.add("com.zdworks.android.zdclock");
                mFontsWhiteList.add("com.antutu.ABenchMark");
                mFontsWhiteList.add("dopool.player");
                mFontsWhiteList.add("com.uc.browser");
                mFontsWhiteList.add("com.ijinshan.mguard");
                mFontsWhiteList.add("bdmobile.android.app");
                mFontsWhiteList.add("com.alensw.PicFolder");
                mFontsWhiteList.add("com.xiaomi.topic");
                mFontsWhiteList.add("com.oupeng.mini.android");
                mFontsWhiteList.add("com.qihoo360.launcher.screenlock");
                mFontsWhiteList.add("com.android.vending");
                mFontsWhiteList.add("com.meilishuo");
                mFontsWhiteList.add("com.qidian.QDReader");
                mFontsWhiteList.add("com.tencent.research.drop");
                mFontsWhiteList.add("com.android.bluetooth");
                mFontsWhiteList.add("com.sinovatech.unicom.ui");
                mFontsWhiteList.add("com.dianping.v1");
                mFontsWhiteList.add("com.yx");
                mFontsWhiteList.add("com.dianxinos.dxhome");
                mFontsWhiteList.add("com.yiche.price");
                mFontsWhiteList.add("com.iBookStar.activity");
                mFontsWhiteList.add("com.android.dazhihui");
                mFontsWhiteList.add("cn.wps.moffice_eng");
                mFontsWhiteList.add("com.taobao.wwseller");
                mFontsWhiteList.add("com.icbc");
                mFontsWhiteList.add("cn.chinabus.main");
                mFontsWhiteList.add("com.ganji.android");
                mFontsWhiteList.add("com.ting.mp3.android");
                mFontsWhiteList.add("com.hy.minifetion");
                mFontsWhiteList.add("com.mogujie");
                mFontsWhiteList.add("com.baozoumanhua.android");
                mFontsWhiteList.add("com.calendar.UI");
                mFontsWhiteList.add("com.wacai365");
                mFontsWhiteList.add("com.cnvcs.junqi");
                mFontsWhiteList.add("cn.cntv");
                mFontsWhiteList.add("com.xunlei.kankan");
                mFontsWhiteList.add("com.xikang.android.slimcoach");
                mFontsWhiteList.add("com.thunder.ktvdaren");
                mFontsWhiteList.add("cn.goapk.market");
                mFontsWhiteList.add("cn.htjyb.reader");
                mFontsWhiteList.add("com.sec.android.app.camera");
                mFontsWhiteList.add("com.blovestorm");
                mFontsWhiteList.add("me.papa");
                mFontsWhiteList.add("com.when.android.calendar365");
                mFontsWhiteList.add("com.android.wallpaper.livepicker");
                mFontsWhiteList.add("com.vancl.activity");
                mFontsWhiteList.add("jp.naver.line.android");
                mFontsWhiteList.add("com.netease.mkey");
                mFontsWhiteList.add("com.youba.barcode");
                mFontsWhiteList.add("com.hupu.games");
                mFontsWhiteList.add("com.kandian.vodapp");
                mFontsWhiteList.add("com.dewmobile.kuaiya");
                mFontsWhiteList.add("com.anguanjia.safe");
                mFontsWhiteList.add("com.tudou.android");
                mFontsWhiteList.add("cmb.pb");
                mFontsWhiteList.add("com.weico.sinaweibo");
                mFontsWhiteList.add("com.ireadercity.b2");
                mFontsWhiteList.add("cn.wps.livespace");
                mFontsWhiteList.add("com.estrongs.android.pop");
                mFontsWhiteList.add("com.facebook.katana");
                mFontsWhiteList.add("com.disney.WMW");
                mFontsWhiteList.add("com.tuan800.tao800");
                mFontsWhiteList.add("com.byread.reader");
                mFontsWhiteList.add("me.imid.fuubo");
                mFontsWhiteList.add("com.lingdong.client.android");
                mFontsWhiteList.add("com.mop.activity");
                mFontsWhiteList.add("com.sina.mfweibo");
                mFontsWhiteList.add("cld.navi.mainframe");
                mFontsWhiteList.add("com.mappn.gfan");
                mFontsWhiteList.add("com.tencent.pengyou");
                mFontsWhiteList.add("com.xunlei.downloadprovider");
                mFontsWhiteList.add("com.tencent.android.qqdownloader");
                mFontsWhiteList.add("com.whatsapp");
                mFontsWhiteList.add("com.mx.browser");
            }
        }
    }*/
}
