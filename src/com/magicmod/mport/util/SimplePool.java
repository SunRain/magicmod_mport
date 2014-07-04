
package com.magicmod.mport.util;

import java.lang.reflect.Array;

public class SimplePool {
    
    public static <T> PoolInstance<T> newInsance(Manager<T> manager, int size) {
        return new PoolInstance<T>(manager, size);
    }

    public static abstract class Manager<T> {
        public abstract T createInstance();

        public void onAcquire(T element) {
        }

        public void onRelease(T element) {
        }
    }

    public static class PoolInstance<T> {
        private T[] mElements;
        private int mIndex;
        private SimplePool.Manager<T> mManager;

        public PoolInstance(SimplePool.Manager<T> manager, int size) {
            this.mManager = manager;
            mElements = (T[])(Object[])Array.newInstance(Object.class, size);
            this.mIndex = -1;
        }

        public T acquire() {
            T element = null;
            try {
                if (this.mIndex >= 0) {
                    element = this.mElements[this.mIndex];
                    Object[] arrayOfObject = this.mElements;
                    int i = this.mIndex;
                    this.mIndex = (i - 1);
                    arrayOfObject[i] = null;
                }
                if (element == null)
                    element = this.mManager.createInstance();
                this.mManager.onAcquire((T) element);
                return (T) element;
            } finally {
            }
        }

        public void release(T element) {
            //Object localObject = null;
            this.mManager.onRelease(element);
            try {
                if (1 + this.mIndex < this.mElements.length) {
                    Object[] arrayOfObject = this.mElements;
                    int i = 1 + this.mIndex;
                    this.mIndex = i;
                    arrayOfObject[i] = element;
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
