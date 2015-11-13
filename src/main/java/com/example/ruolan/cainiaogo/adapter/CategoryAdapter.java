package com.example.ruolan.cainiaogo.adapter;

import android.content.Context;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.bean.Category;

import java.util.List;

/**
 * Created by ruolan on 2015/11/13.
 */
public class CategoryAdapter extends SimpleAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_simple_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Category item) {
        viewHoder.getTextView(R.id.categoryTextView).setText(item.getName());
    }
}
