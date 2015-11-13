package com.example.ruolan.cainiaogo.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.bean.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by ruolan on 2015/11/13.
 */
public class HWAdapter extends SimpleAdapter<Wares> {

    public HWAdapter(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares,datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText(wares.getPrice().toString());
    }

   /* @Override
    public void bindData(BaseViewHolder viewHolder, Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText(wares.getPrice().toString());
    }*/
}
