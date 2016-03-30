package com.moxun.vbanner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by moxun on 16/3/29.
 */
public abstract class VBAdapter<D> extends RecyclerView.Adapter {
    public abstract
    @NonNull
    List<D> getDataSet();

    protected void next() {
        List<D> set = getDataSet();
        if (set == null) {
            throw new RuntimeException("Method 'getDataSet()' must not return null.");
        } else {
            D first = set.get(0);
            set.remove(0);
            notifyItemRemoved(0);
            set.add(first);
            notifyItemInserted(set.size());
        }
    }
}
