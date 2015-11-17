package com.example.ruolan.cainiaogo.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.bean.Wares;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by ruolan on 2015/11/13.
 */
public class WareAdapter extends SimpleAdapter<Wares> {


    public WareAdapter(Context context,  List<Wares> datas) {
        super(context, R.layout.template_grid_wares, datas);
        Fresco.initialize(context);

    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Wares item) {

        viewHoder.getTextView(R.id.text_title).setText(item.getName());
        viewHoder.getTextView(R.id.text_price).setText("￥"+item.getPrice());
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
    }
}
