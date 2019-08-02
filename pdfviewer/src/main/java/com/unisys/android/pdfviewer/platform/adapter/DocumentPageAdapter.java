package com.unisys.android.pdfviewer.platform.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.unisys.android.pdfviewer.platform.fragments.PdfRendererFragment;
import com.unisys.android.pdfviewer.presentation.DocumentListPresenter;


public class DocumentPageAdapter extends FragmentPagerAdapter {
    private DocumentListPresenter mPresenter;

    public DocumentPageAdapter(FragmentManager fragmentManager, DocumentListPresenter presenter) {
        super(fragmentManager);
        mPresenter = presenter;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return mPresenter.getItemsCount();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return PdfRendererFragment.newInstance(position);
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return mPresenter.getTitle();
    }

}