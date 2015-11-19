package com.example.ruolan.cainiaogo.weiget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.internal.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ruolan.cainiaogo.R;

/**
 * Created by ruolan on 2015/11/11.
 */

/**
 * Toolbar的简单实现
 */
public class CnToolbar extends Toolbar {


    //它的作用类似于findViewById()。不同点是LayoutInflater是用来找res/layout/下的
    // xml布局文件，并且实例化

    private LayoutInflater mInflater;

    /**
     * 控件的定义
     */
    private View mView;
    private TextView mTextTitle;
    private EditText mSearchView;
    private Button mRightButton;


    /**
     * 以下的三个构造方法，依次调用下面的构造方法
     *
     * @param context
     */
    public CnToolbar(Context context) {
        this(context, null);
    }

    public CnToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CnToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


       //初始化布局文件
        initView();
        /**
         * contentInset的API文档的解释是"内容视图嵌入到封闭的滚动视图的距离"（哈，英文不是很好，翻译的不好）。
         * 可以理解为内容视图的上下左右四个边扩展出去的大小。contentInset的单位是UIEdgeInsets，
         * 默认值为UIEdgeInsetsZero，
         * 也就是没有扩展的边。下面解释一下UIEdgeInsets，它是一个结构体，定义如下：
         [cpp] view plaincopy
         typedef struct {
         CGFloat top, left, bottom, right;
         }
         分别代表着上边界，左边界，底边界，右边界，扩展出去的值。
         */
        setContentInsetsRelative(10, 10);


        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.CnToolbar, defStyleAttr, 0);

            final Drawable rightIcon = a.getDrawable(R.styleable.CnToolbar_rightButtonIcon);
            if (rightIcon != null) {
                //setNavigationIcon(navIcon);
                setRightButtonIcon(rightIcon);
            }


            boolean isShowSearchView = a.getBoolean(R.styleable.CnToolbar_isShowSearchView, false);

            /**
             * 判断isShowSerachView是否为真，如果为真就显示搜索框，隐藏标题
             */
            if (isShowSearchView) {

                showSearchView();
                hideTitleView();

            }
            //回收
            a.recycle();
        }

    }

    /**
     * 初始化布局
     */
    private void initView() {


        if (mView == null) {

            /**
             * 获得 LayoutInflater 实例的三种方式
             1. LayoutInflater inflater = getLayoutInflater();//调用Activity的getLayoutInflater()
             2. LayoutInflater inflater = LayoutInflater.from(context);
             3. LayoutInflater inflater =  (LayoutInflater)context.getSystemService
             (Context.LAYOUT_INFLATER_SERVICE);
             */
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);

            //绑定控件
            mTextTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchview);
            mRightButton = (Button) mView.findViewById(R.id.toolbar_rightButton);

            /**
             * LayoutParams相当于一个Layout的信息包，它封装了Layout的位置、高、宽等信息。假设在屏幕上一块区域是由一个Layout占领的，如果将一个View添加到一个Layout中，最好告诉Layout用户期望的布局方式，也就是将一个认可的layoutParams传递进去。
             可以这样去形容LayoutParams，在象棋的棋盘上，每个棋子都占据一个位置，也就是每个棋子都有一个位置的信息，如这个棋子在4行4列，这里的“4行4列”就是棋子的LayoutParams。
             但LayoutParams类也只是简单的描述了宽高，宽和高都可以设置成三种值：
             1，一个确定的值；
             2，FILL_PARENT，即填满（和父容器一样大小）；
             3，WRAP_CONTENT，即包裹住组件就好。
             */
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);

            addView(mView, lp);
        }


    }


    public void setRightButtonIcon(Drawable icon) {

        if (mRightButton != null) {
            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }

    }



    public void setRightButtonIcon(int icon) {

        setRightButtonIcon(getResources().getDrawable(icon));
    }

    public void setRightButtonText(CharSequence text){
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }

    public void setRightButtonText(int id){
        setRightButtonText(getResources().getString(id));
    }

    public Button getRightButton(){
        return this.mRightButton;
    }

    //对控件实施监听
    public void setRightButtonOnClickListener(OnClickListener li) {

        mRightButton.setOnClickListener(li);
    }


    @Override
    public void setTitle(int resId) {

        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {

        initView();
        if (mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    public void showSearchView() {

        if (mSearchView != null)
            mSearchView.setVisibility(VISIBLE);

    }

    public void hideSearchView() {
        if (mSearchView != null)
            mSearchView.setVisibility(GONE);
    }

    public void showTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }

    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);

    }


}
