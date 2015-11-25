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
import com.example.ruolan.cainiaogo.http.OkHttpHelper;
import com.example.ruolan.cainiaogo.utils.CartProvider;
import com.example.ruolan.cainiaogo.weiget.CnToolbar;

import java.util.List;

/**
 * Created by ruolan on 2015/11/11.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CartFragment";

    private RecyclerView mRecyclerView;

    private CnToolbar mToolbar;

    private CheckBox mCheckBox;

    private Button mBtnOrder;

    private Button mBtnDel;

    //定义的两个状态，来判断是否是处于编辑状态和完成状态
    public static final int ACTION_EDIT = 1;
    public static final int ACTION_CAMPLATE = 2;

    private TextView mTextTotal;

    private CartAdapter mCartAdapter;

    private CartProvider mCartProvider;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        mCartProvider = new CartProvider(getContext());


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_all);
        mBtnOrder = (Button) view.findViewById(R.id.btn_order);

        //简单的测试使用
     /*   mBtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHttpHelper.get(Contants.API.USER_DETAIL, new SpotsCallback<User>(getActivity()) {
                    @Override
                    public void onSuccess(Response response, User o) {
                        Log.d(TAG,"onSuccess===" + response.code());
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {
                        Log.d(TAG,"onError===" + response.code());
                    }
                });
            }
        });*/
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

        //得到购物车中的所有数据
        List<ShoppingCart> carts = mCartProvider.getAll();
        if (carts != null) {
            mCartAdapter.clear();
            mCartAdapter.addData(carts);
            mCartAdapter.showTotalPrice();
        }
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


    /**
     * 判断是否toolbar是否显示
     */
    public void changeToolbar() {
        mToolbar.showTitleView();   //显示标题栏
        mToolbar.hideSearchView();  //隐藏搜索框
        mToolbar.setTitle(R.string.cart);  //给标题栏设置title
        mToolbar.getRightButton().setVisibility(View.VISIBLE); //把标题栏的右侧的button按钮显示出来
        mToolbar.setRightButtonText(R.string.edit);  //给button按钮设置文字
        mToolbar.getRightButton().setOnClickListener(this); //给右侧button设置点击监听事件
        mToolbar.getRightButton().setTag(ACTION_EDIT);    //给右侧button按钮设置一个tag状态，为了以后的变化
    }

    /**
     * 显示购物车中的所有的数据
     */
    private void showData() {
        //获取购物车中的已经存在的所有的商品
        List<ShoppingCart> carts = mCartProvider.getAll();

        //初始化cartadapter
        mCartAdapter = new CartAdapter(getContext(), carts, mCheckBox, mTextTotal);

        //给recycleview设置adapter
        mRecyclerView.setAdapter(mCartAdapter);

        //设置排列方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

    }

    //显示编辑完成的界面的显示
    private void showDelControl() {
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);

        //改变button中的状态
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);

        //在是显示完成的时候，也就是在编辑的时候，把购物车中的所有商品处于为非选中状态
        mCartAdapter.checkAll_None(false);
        //全选的状态也是处于非选中状态
        mCheckBox.setChecked(false);
    }

    @Override
    public void onClick(View v) {

        //判断上面右侧标题栏中的按钮的状态
        int action = (int) v.getTag();
        if (ACTION_EDIT == action) {   //如果是编辑状态，就调用编辑方法
            showDelControl();
        } else if (ACTION_CAMPLATE == action) {   //
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
