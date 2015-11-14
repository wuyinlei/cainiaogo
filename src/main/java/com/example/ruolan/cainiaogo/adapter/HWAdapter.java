package com.example.ruolan.cainiaogo.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.bean.ShoppingCart;
import com.example.ruolan.cainiaogo.bean.Wares;
import com.example.ruolan.cainiaogo.utils.CartProvider;
import com.example.ruolan.cainiaogo.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by ruolan on 2015/11/13.
 */
public class HWAdapter extends SimpleAdapter<Wares> {

    CartProvider mProvider;

    public HWAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);
        mProvider = new CartProvider(context);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥" + wares.getPrice());

        viewHolder.getButton(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProvider.put(convertData(wares));
                ToastUtils.show(mContext,"已经添加到购物车");
            }
        });
    }

    /**
     * 把wares类转化为ShoppingCart类
     * @param item
     * @return
     */
    public ShoppingCart convertData(Wares item){
        ShoppingCart cart = new ShoppingCart();
        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }


}
