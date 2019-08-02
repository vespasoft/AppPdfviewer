package com.unisys.android.pdfviewer.platform.views;

import android.content.Context;

public interface ReaderView {

    void setUpToolbar();
    
    void setUpControls();

    void setUpRecyclerView();

    void setupViewPager();

    void refreshData();

    void showLoading(boolean value);

    Context context();
}
