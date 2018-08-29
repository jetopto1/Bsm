package com.jetopto.bsm.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.jetopto.bsm.R;
import com.jetopto.bsm.custom.adapter.CategoryAdapter;
import com.jetopto.bsm.utils.classes.Category;

import java.util.ArrayList;

public class CategoryFragment extends Fragment implements CategoryAdapter.ClickListener {

    private static final String TAG = CategoryFragment.class.getSimpleName();

    public interface OnCategoryClick {
        void onClick(int id);
    }

    private OnCategoryClick mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        attach();
        initView(view);
        return view;
    }

    private void attach() {
        try {
            this.mListener = (OnCategoryClick) getContext();
        } catch (final ClassCastException e) {
            e.printStackTrace();
            throw new ClassCastException(getContext().toString() + " must implement OnCompleteListener");
        }
    }

    private void initView(View view) {
        GridView gridView = view.findViewById(R.id.grid_category);
        CategoryAdapter adapter = new CategoryAdapter(getContext(), getData());
        adapter.registerListener(this);
        gridView.setAdapter(adapter);
    }

    private ArrayList<Category> getData() {
        ArrayList<Category> categoryList = new ArrayList<>();
        TypedArray ar = getContext().getResources().obtainTypedArray(R.array.category_icon);
        String[] labels = getResources().getStringArray(R.array.category_label_name);
        int min = ar.length() < labels.length ? ar.length() : labels.length;

        for (int index = 0; index < min; index++) {
            Log.d(TAG, "labels: " + labels[index] + " icons " + ar.getResourceId(index, -1));
            categoryList.add(new Category(labels[index], ar.getResourceId(index, -1)));
        }
        ar.recycle();
        return categoryList;
    }

    @Override
    public void onClick(Category category) {
        if (null != mListener) {
            mListener.onClick(category.getImageId());
        }
    }
}
