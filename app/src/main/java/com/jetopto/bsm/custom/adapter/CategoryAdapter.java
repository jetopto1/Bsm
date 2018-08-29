package com.jetopto.bsm.custom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jetopto.bsm.R;
import com.jetopto.bsm.utils.classes.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private static final String TAG = Category.class.getSimpleName();
    private Context mContext;
    private List<Category> mCategoryList;
    private ClickListener mListener;

    public interface ClickListener {
        void onClick(Category category);
    }

    public CategoryAdapter(Context context, ArrayList<Category> list) {
        mContext = context;
        mCategoryList = list;
    }

    public void registerListener(ClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getCount() {
        return mCategoryList.size();
    }

    @Override
    public Category getItem(int position) {
        return mCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_main_category, parent, false);
        }

        final Category category = this.getItem(position);
        CardView cardView = view.findViewById(R.id.card_view_category);
        ImageView imageView = view.findViewById(R.id.image_category);
        TextView textView = view.findViewById(R.id.label_category);
        Log.i(TAG, "resource" + category.getImageId());
        int resId = category.getImageId();
        if (-1 != resId )
        imageView.setBackground(mContext.getResources().getDrawable(resId));
        textView.setText(category.getLabel());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClickonClickonClick" );
                mListener.onClick(category);
            }
        });

        return view;
    }


}

