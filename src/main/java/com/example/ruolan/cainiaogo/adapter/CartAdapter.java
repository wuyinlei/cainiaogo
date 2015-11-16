package com.example.ruolan.cainiaogo.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
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
        numAddSubView.setOnButtonClickListener(new MyOnButtonClickListener(item));

        mCheckBox = (CheckBox) viewHoder.getView(R.id.checkbox);
        mCheckBox.setChecked(item.isChecked());
    }

    /**
     * 得到购物车中总体的价格
     *
     * @return
     */
    private float getTotalPrice() {
        float sum = 0;
        if (!isNull())
            return sum;
        //对购物车中的商品进行遍历
        for (ShoppingCart cart : datas) {
            if (cart.isChecked())  //如果是选中状态就计算价格
                //对选中的物品计算价格，价格公式是商品单价*商品数量
                sum += cart.getCount() * cart.getPrice();
        }
        return sum;
    }

    /**
     * 显示总价格
     */
    public void showTotalPrice() {
        float total = getTotalPrice();
        //为总价设置需要显示的价格
        mTextView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }

    /**
     * 判断购物车中是否为空
     *
     * @return
     */

    private boolean isNull() {
        return datas != null && datas.size() > 0;
    }

    /**
     * 实现了ItemClick点击事件
     *
     * @param view
     * @param position
     */
    @Override
    public void OnItemClick(View view, int position) {
        ShoppingCart cart = getItem(position);
        cart.setIsChecked(!cart.isChecked());

        // Log.d("wuyinlei", cart.getName().toString());
        notifyItemChanged(position);

        checkListener();
        //每次都要调用此方法，来改变价格的显示
        showTotalPrice();
    }

    /**
     * 判断检查购物车中全选和购物车中的单个商品的选项是否都处于选择状态，
     * 如果是全部处于选择状态就改变全选的状态为选择状态
     */
    private void checkListener() {
        int count = 0;
        int checkNum = 0;
        if (datas != null) {   //进行判空
            count = datas.size();

            //对购物车中的ShoppingCart进行遍历
            for (ShoppingCart cart : datas) {
                if (!cart.isChecked()) {
                    mCheckBox.setChecked(false);
                    break;
                } else {
                    checkNum = checkNum + 1;
                }
            }
            //如果单个物品选择的个数和总的购物车相同，就改变全选的状态为选中状态
            if (count == checkNum) {
                mCheckBox.setChecked(true);
            }
        }
    }

    /**
     * @param isChecked
     */
    public void checkAll_None(boolean isChecked) {
        if (!isNull())
            return;
        int i = 0;
        for (ShoppingCart cart : datas) {
            cart.setIsChecked(isChecked);
            notifyItemChanged(i);
            showTotalPrice();
            i++;
        }
    }

    /**
     * 删除cart
     */
    public void delCart() {
        if (!isNull())
            return;

        /**
         * 下面的只能这样遍历，不能 for (ShoppingCart cart : datas)
         * 因为如果使用了上面的方式遍历，会出错的
         * 原因是在删除的时候我们的datas的长度是变化的，每次删除后都会减少，和原先的长度不一样
         */

        //利用过了迭代器进行遍历
        for (Iterator iterator = datas.iterator(); iterator.hasNext(); ) {
            ShoppingCart cart = (ShoppingCart) iterator.next();
            if (cart.isChecked()) {
                //获取到position，为了一下的notifyItemChanged（）中的参数做准备
                int position = datas.indexOf(cart);
                cartPrivider.delete(cart);
                iterator.remove();
                notifyItemChanged(position);
            }
        }

    }

    private class MyOnButtonClickListener implements NumAddSubView.OnButtonClickListener {
        private final ShoppingCart mItem;

        public MyOnButtonClickListener(ShoppingCart item) {
            mItem = item;
        }

        @Override
        public void onButtonAddClick(View view, int value) {
            mItem.setCount(value);
            cartPrivider.update(mItem);
            showTotalPrice();
        }

        @Override
        public void onButtonSumClick(View view, int value) {
            mItem.setCount(value);
            cartPrivider.update(mItem);
            showTotalPrice();
        }
    }
}
