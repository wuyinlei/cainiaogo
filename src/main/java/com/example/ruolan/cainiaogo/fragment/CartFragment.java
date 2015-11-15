package com.example.ruolan.cainiaogo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.ruolan.cainiaogo.R;
import com.example.ruolan.cainiaogo.activity.MainActivity;
import com.example.ruolan.cainiaogo.adapter.CartAdapter;
import com.example.ruolan.cainiaogo.adapter.Decoration.DividerItemDecoration;
import com.example.ruolan.cainiaogo.bean.ShoppingCart;
import com.example.ruolan.cainiaogo.utils.CartProvider;
import com.example.ruolan.cainiaogo.weiget.CnToolbar;

import java.util.List;

/**
 * Created by ruolan on 2015/11/11.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    private RecyclerView mRecyclerView;

    private CnToolbar mToolbar;

    private CheckBox mCheckBox;

    private Button mBtnOrder;

    private Button mBtnDel;

    public static final int ACTION_EDIT = 1;
    public static final int ACTION_CAMPLATE = 2;

    private TextView mTextTotal;

    private CartAdapter mCartAdapter;

    private CartProvider mCartProvider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        mCartProvider = new CartProvider(getContext());


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_all);
        mBtnOrder = (Button) view.findViewById(R.id.btn_order);
        mBtnDel = (Button) view.findViewById(R.id.btn_del);
        mBtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartAdapter.delCart();
            }
        });
        mTextTotal = (TextView) view.findViewById(R.id.text_total);

        showData();

        return view;
    }



    /**
     * 刷新购物车中的数据
     */
    public void refData() {
        mCartAdapter.clear();

        List<ShoppingCart> carts = mCartProvider.getAll();

        mCartAdapter.addData(carts);
        mCartAdapter.showTotalPrice();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) context;
            mToolbar = (CnToolbar) activity.findViewById(R.id.toolbar);
            mToolbar.setTitle(R.string.cart);
            changeToolbar();
        }
    }


    public void changeToolbar() {
        mToolbar.showTitleView();
        mToolbar.hideSearchView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setOnClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
    }

    private void showData() {
        List<ShoppingCart> carts = mCartProvider.getAll();

        mCartAdapter = new CartAdapter(getContext(), carts, mCheckBox, mTextTotal);

        mRecyclerView.setAdapter(mCartAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

    }

    private void showDelControl() {
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);

        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);

        mCartAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);
    }

    @Override
    public void onClick(View v) {

        int action = (int) v.getTag();
        if (ACTION_EDIT == action) {
            showDelControl();
        } else if (ACTION_CAMPLATE == action) {
            hideDelControl();
        }


    }

    private void hideDelControl() {
        mToolbar.getRightButton().setText("编辑");
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);
        mBtnDel.setVisibility(View.GONE);

        mToolbar.getRightButton().setTag(ACTION_EDIT);

        mCartAdapter.checkAll_None(true);
        mCartAdapter.showTotalPrice();
        mCheckBox.setChecked(true);

    }
}
