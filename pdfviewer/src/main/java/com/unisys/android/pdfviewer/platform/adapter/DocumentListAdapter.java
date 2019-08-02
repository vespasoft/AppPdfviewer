package com.unisys.android.pdfviewer.platform.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.unisys.android.pdfviewer.platform.holder.PageHolder;
import com.unisys.android.pdfviewer.presentation.DocumentListPresenter;

public class DocumentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private DocumentListPresenter mPresenter;

    public DocumentListAdapter(DocumentListPresenter presenter){
        this.mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewGroup instanceof RecyclerView ) {
            return PageHolder.newInstance(viewGroup);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mPresenter.configureCell((PageHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getItemsCount();
    }

    public void refreshData() {
        notifyDataSetChanged();
    }

}
