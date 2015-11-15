package com.example.ruolan.cainiaogo.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.bean.ShoppingCart;
import com.example.ruolan.cainiaogo.utils.CartProvider;
import com.example.ruolan.numaddandsub.NumAddSubView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ruolan on 2015/11/14.
 */
public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener {


    private CartProvider cartPrivider;

    public CartAdapter(Context context, List<ShoppingCart> datas, final CheckBox checkBox, TextView textView) {
        super(context, R.layout.templete_cart, datas);
        this.mCheckBox = checkBox;
        this.mTextView = textView;

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkAll_None(checkBox.isChecked());
                showTotalPrice();
            }
        });
        setOnItemClickListener(this);

        cartPrivider = new CartProvider(context);

        showTotalPrice();
    }


    private CheckBox mCheckBox;
    private TextView mTextView;


    @Override
    protected void convert(BaseViewHolder viewHoder, final ShoppingCart item) {
        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥" + item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        NumAddSubView numAddSubView = (NumAddSubView) viewHoder.getView(R.id.num_control);
        numAddSubView.setValue(item.getCount());
        numAddSubView.setOnButtonClickListener(new NumAddSubView.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                item.setCount(value);
                cartPrivider.update(item);
                showTotalPrice();
            }

            @Override
            public void onButtonSumClick(View view, int value) {
                item.setCount(value);
                cartPrivider.update(item);
                showTotalPrice();
            }
        });

        mCheckBox = (CheckBox) viewHoder.getView(R.id.checkbox);
        mCheckBox.setChecked(item.isChecked());
    }

    private float getTotalPrice() {
        float sum = 0;
        if (!isNull())
            return sum;
        for (ShoppingCart cart : datas) {
            if (cart.isChecked())
                sum += cart.getCount() * cart.getPrice();
        }
        return sum;
    }

    public void showTotalPrice() {
        float total = getTotalPrice();
        mTextView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }

    private boolean isNull() {
        return datas != null && datas.size() > 0;
    }

    @Override
    public void OnItemClick(View view, int position) {
        ShoppingCart cart = getItem(position);
        cart.setIsChecked(!cart.isChecked());

        Log.d("wuyinlei", cart.getName().toString());
        notifyItemChanged(position);

        checkListener();
        showTotalPrice();
    }

    private void checkListener() {
        int count = 0;
        int checkNum = 0;
        if (datas != null) {
            count = datas.size();

            for (ShoppingCart cart : datas) {
                if (!cart.isChecked()) {
                    mCheckBox.setChecked(false);
                    break;
                } else {
                    checkNum = checkNum + 1;
                }
            }
            if (count == checkNum) {
                mCheckBox.setChecked(true);
            }
        }
    }

    public void checkAll_None(boolean isChecked){
        if (!isNull())
            return;
        int i = 0;
        for (ShoppingCart cart:datas){
            cart.setIsChecked(isChecked);
            notifyItemChanged(i);
            showTotalPrice();
            i++;
        }
    }

    public void delCart(){
        if (!isNull())
            return;

        for (Iterator iterator = datas.iterator();iterator.hasNext();){
            ShoppingCart cart = (ShoppingCart) iterator.next();
            if (cart.isChecked()){
                int position = datas.indexOf(cart);
                cartPrivider.delete(cart);
                iterator.remove();
                notifyItemChanged(position);
            }
        }


    }
}
