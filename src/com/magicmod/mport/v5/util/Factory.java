package com.magicmod.mport.v5.util;

import com.google.android.collect.Maps;

import java.util.Map;

public abstract interface Factory<T, F> {
    public abstract T create(F paramF);

    public static class CachedFactory<T, F> implements Factory<T, F> {
        private final Map<F, T> mCached = Maps.newHashMap();
        private final Factory<T, F> mFactory;

        public CachedFactory(Factory<T, F> factory) {
            this.mFactory = factory;
        }

        public static <T, F> CachedFactory<T, F> newFactory(Factory<T, F> factory) {
            //if (factory == null)
             //   ;
            //for (Object localObject = null;; localObject = new CachedFactory(factory))
             //   return localObject;
            if (factory == null) {
                return null;
            }
            return new CachedFactory<T, F>(factory);
        }

        public void clear() {
            this.mCached.clear();
        }

        public T create(F from) {
            //Object t = this.mCached.get(from);
            T t = this.mCached.get(from);
            if (t == null) {
                t = this.mFactory.create(from);
                if (t != null)
                    this.mCached.put(from, t);
            }
            return t;
        }
    }
}
