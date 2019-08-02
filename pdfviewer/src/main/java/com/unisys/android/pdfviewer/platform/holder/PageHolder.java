package com.unisys.android.pdfviewer.platform.holder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unisys.android.pdfviewer.domain.PageModel;
import com.vespasoft.android.pdfviewer.R;

public class PageHolder extends RecyclerView.ViewHolder {

    private ConstraintLayout mCellLayout;
    private TextView mTitleText;
    private AppCompatImageView mImageView;

    public static PageHolder newInstance(ViewGroup viewGroup){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_doc, viewGroup, false);
        return new PageHolder(view);
    }

    private PageHolder(View itemView) {
        super(itemView);
        mCellLayout = itemView.findViewById(R.id.card_item);
        mTitleText = itemView.findViewById(R.id.text_document_title);
        mImageView = itemView.findViewById(R.id.image_document);
    }

    public void render(PageModel pageModel, int cellColor) {
        mCellLayout.setBackgroundColor(cellColor);
        mTitleText.setText(" " + (pageModel.getIndex()+1) + " ");
        if (pageModel.getThumbnail()!=null) mImageView.setImageBitmap(pageModel.getThumbnail());
    }

}
