package com.example.ruolan.cainiaogo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

/**
 * Created by ruolan on 2015/11/13.
 */
public abstract class BaseAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected List<T> datas;

    protected LayoutInflater mInflater;

    protected Context mContext;

    protected int mLayoutResId;

    protected OnItemClickListener mListener = null;

    public BaseAdapter(Context context, int layoutResId, List<T> datas) {
        this.mContext = context;
        this.datas = datas;
        this.mLayoutResId = layoutResId;
        mInflater = LayoutInflater.from(context);
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public BaseAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Fresco.initialize(parent.getContext());
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutResId, parent, false);

        return new BaseViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        T t = getItem(position);
        convert((H) holder, t);
        //bindData(holder,t);
    }


    @Override
    public int getItemCount() {
        if (datas == null || datas.size() <= 0)
            return 0;

        return datas.size();
    }


    public T getItem(int position) {
        if (position >= datas.size()) return null;
        return datas.get(position);
    }


    public void clear() {
        int itemCount = datas.size();
        if (itemCount != 0) {
            datas.clear();
            this.notifyItemRangeRemoved(0, itemCount);
        }
    }

    public List<T> getDatas() {

        return datas;
    }

    public void addData(List<T> datas) {

        addData(0, datas);
    }

    public void addData(int position, List<T> datas) {
        if (datas != null && datas.size() > 0) {

            this.datas.addAll(datas);
            this.notifyItemRangeChanged(position, datas.size());
        }
    }

    public void refreshData(List<T> list){

        if(list !=null && list.size()>0){

            clear();
            int size = list.size();
            for (int i=0;i<size;i++){
                datas.add(i,list.get(i));
                notifyItemInserted(i);
            }

        }
    }

    public void loadMoreData(List<T> list){

        if(list !=null && list.size()>0){

            int size = list.size();
            int begin = datas.size();
            for (int i=0;i<size;i++){
                datas.add(list.get(i));
                notifyItemInserted(i+begin);
            }

        }

    }

    protected abstract void convert(H viewHoder, T item);
    // public abstract void bindData(BaseViewHolder viewHolder,T t);
}
