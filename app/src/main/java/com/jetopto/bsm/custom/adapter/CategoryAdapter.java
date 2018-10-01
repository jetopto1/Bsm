package com.jetopto.bsm.custom.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
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

    private static final String TAG = CategoryAdapter.class.getSimpleName();
    private int mFocusedPosition = 0;
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

    public int getFocusedPosition() {
        return mFocusedPosition;
    }

    public void setFocusedPosition(int position) {
        this.mFocusedPosition = position;
        notifyDataSetChanged();
    }

    public void performClick() {
        mListener.onClick(getItem(mFocusedPosition));
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
        ViewHolder holder;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_main_category, parent, false);
            holder = new ViewHolder();
            holder.cardView = view.findViewById(R.id.card_view_category);
            holder.imageView = view.findViewById(R.id.image_category);
            holder.textView = view.findViewById(R.id.label_category);
            holder.background = view.findViewById(R.id.item_background);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Category category = this.getItem(position);
        int resId = category.getImageId();
        if (-1 != resId) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.imageView.setBackground(mContext.getResources().getDrawable(resId));
            } else {
                holder.imageView.setImageDrawable(mContext.getResources().getDrawable(resId));
            }
        }

        holder.textView.setText(category.getLabel());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(category);
            }
        });

        holder.background.setFocusable(false);
        holder.background.clearFocus();
        if (position == mFocusedPosition) {
            holder.background.setFocusable(true);
            holder.background.requestFocus();

        }
        return view;
    }

    private static class ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView textView;
        View background;
    }
}

