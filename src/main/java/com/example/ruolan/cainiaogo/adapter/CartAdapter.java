package com.example.ruolan.cainiaogo.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.CheckBox;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.bean.ShoppingCart;
import com.example.ruolan.numaddandsub.NumAddSubView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by ruolan on 2015/11/14.
 */
public class CartAdapter extends SimpleAdapter<ShoppingCart>{


    public CartAdapter(Context context, List<ShoppingCart> datas) {
        super(context, R.layout.templete_cart, datas);
    }



    @Override
    protected void convert(BaseViewHolder viewHoder, ShoppingCart item) {
        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥" + item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        NumAddSubView numAddSubView = (NumAddSubView) viewHoder.getView(R.id.num_control);
        numAddSubView.setValue(item.getCount());
        CheckBox checkBox = (CheckBox) viewHoder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());
    }



}
