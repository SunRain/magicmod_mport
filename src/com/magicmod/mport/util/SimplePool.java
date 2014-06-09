
package com.magicmod.mport.util;

import java.lang.reflect.Array;

public class SimplePool {
    public static <T> PoolInstance<T> newInsance(Manager<T> paramManager, int paramInt) {
        return new PoolInstance(paramManager, paramInt);
    }

    public static abstract class Manager<T> {
        public abstract T createInstance();

        public void onAcquire(T paramT) {
        }

        public void onRelease(T paramT) {
        }
    }

    public static class PoolInstance<T> {
        private T[] mElements;
        private int mIndex;
        private SimplePool.Manager<T> mManager;

        @SuppressWarnings("unchecked")
        public PoolInstance(SimplePool.Manager<T> paramManager, int paramInt) {
            this.mManager = paramManager;
            // this.mElements = ((Object[])Array.newInstance(Object.class,
            // paramInt));
            this.mElements = (T[]) Array.newInstance(Object.class, paramInt);
            this.mIndex = -1;
        }

        public T acquire() {
            Object localObject1 = null;
            try {
                if (this.mIndex >= 0) {
                    localObject1 = this.mElements[this.mIndex];
                    Object[] arrayOfObject = this.mElements;
                    int i = this.mIndex;
                    this.mIndex = (i - 1);
                    arrayOfObject[i] = null;
                }
                if (localObject1 == null)
                    localObject1 = this.mManager.createInstance();
                this.mManager.onAcquire((T) localObject1);
                return (T) localObject1;
            } finally {
            }
        }

        public void release(T paramT) {
            Object localObject = null;
            this.mManager.onRelease(paramT);
            try {
                if (1 + this.mIndex < this.mElements.length) {
                    Object[] arrayOfObject = this.mElements;
                    int i = 1 + this.mIndex;
                    this.mIndex = i;
                    arrayOfObject[i] = paramT;
                }
                return;
            } finally {
            }
            // finally
            // {
            // localObject = finally;
            // throw localObject;
            // }
        }
    }
}
