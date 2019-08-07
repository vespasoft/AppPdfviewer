package com.unisys.android.pdfviewer.platform.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.unisys.android.pdfviewer.platform.adapter.DocumentListAdapter;
import com.unisys.android.pdfviewer.platform.adapter.DocumentPageAdapter;
import com.unisys.android.pdfviewer.platform.custom.CustomAnimator;
import com.unisys.android.pdfviewer.platform.views.ReaderView;
import com.unisys.android.pdfviewer.platform.views.RecyclerItemClickListener;
import com.unisys.android.pdfviewer.presentation.DocumentListPresenter;
import com.vespasoft.android.pdfviewer.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PdfViewerActivity extends AppCompatActivity implements ReaderView, View.OnClickListener {

    private AppCompatImageButton mButtonPrevious;

    private AppCompatImageButton mButtonNext;

    private AppCompatButton mButtonIndex;

    private ProgressBar mProgressBar;

    private FloatingActionButton mFButton;

    private LinearLayout mListToolbar;

    private ConstraintLayout mBottomToolbar;

    private ViewPager mViewPager;

    private DocumentListAdapter mAdapter;

    private PagerAdapter mPagerAdapter;

    private DocumentListPresenter mDocListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        String filepath = getIntent().getStringExtra("filepath");
        File file = new File(filepath);

        // create instance of View Presenter
        mDocListPresenter = new DocumentListPresenter();
        mDocListPresenter.setView(this, file);
    }

    @Override
    public void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setUpControls() {
        mButtonIndex = findViewById(R.id.btn_index);
        mProgressBar = findViewById(R.id.progressBar);
        mFButton = findViewById(R.id.fab);
        mFButton.setVisibility(View.GONE);
        mBottomToolbar = findViewById(R.id.toolbar_bottom);
        mListToolbar = findViewById(R.id.list_toolbar);
        mListToolbar.setVisibility(View.GONE);
        mButtonPrevious = findViewById(R.id.previous);
        mButtonNext = findViewById(R.id.next);
        mButtonPrevious.setOnClickListener(this);
        mButtonNext.setOnClickListener(this);
        mFButton.setOnClickListener(this);
    }

    @Override
    public void setUpRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_document);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new DocumentListAdapter(mDocListPresenter);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(getApplicationContext(), recyclerView,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {
                            mDocListPresenter.onItemClick(position);
                        }

                        @Override public void onLongItemClick(View view, int position) {
                        }
                    })
        );
    }

    @Override
    public void refreshData() {
        mAdapter.refreshData();
        setTitle(mDocListPresenter.getTitle());
        mViewPager.setCurrentItem(mDocListPresenter.getItemSelected());
        mButtonIndex.setText(String.valueOf(mDocListPresenter.getItemSelected()+1));
    }

    @Override
    public void showLoading(boolean value) {
        if (value) {
            mProgressBar.setVisibility(View.VISIBLE);
            getSupportActionBar().hide();
            mListToolbar.setVisibility(View.GONE);
            mBottomToolbar.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mListToolbar.setVisibility(View.GONE);
            mBottomToolbar.setVisibility(View.VISIBLE);
            CustomAnimator.executeAnimation(mBottomToolbar, 100f, 0f, CustomAnimator.Property.TRANSLATIONY);
            getSupportActionBar().show();

        }
    }

    @Override
    public void setupViewPager() {
        mViewPager = findViewById(R.id.vpPager);
        mPagerAdapter = new DocumentPageAdapter(getSupportFragmentManager(), mDocListPresenter);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setClipToPadding(true);
        //mViewPager.setPageTransformer(true, new BookFlipPageTransformer());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mDocListPresenter.onItemClick(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.previous) {
            mDocListPresenter.onPrevious();
        } else if (i == R.id.next) {
            mDocListPresenter.onNext();
        } else if (i == R.id.fab) {
            showFullScreen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            goToBack();
            return true;
        } else if (i == R.id.action_view_list) {
            showViewList();
            return true;
        } else if (i == R.id.action_full_screen) {
            showFullScreen();
            return true;
        } else if (i == R.id.action_search) {
            showToolSearch();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showViewList() {
        if (mListToolbar.getVisibility() == View.GONE) {
            mListToolbar.setVisibility(View.VISIBLE);
            //CustomAnimator.executeAnimation(mListToolbar, -400f, 0f, CustomAnimator.Property.TRANSLATIONX);
            //CustomAnimator.executeAnimation(mViewPager,0f, 400f, CustomAnimator.Property.TRANSLATIONX);
            CustomAnimator.executeAnimation(mBottomToolbar, 0f, 100f, CustomAnimator.Property.TRANSLATIONY);
            //mBottomToolbar.setVisibility(View.GONE);
        } else {
            CustomAnimator.executeAnimation(mListToolbar, 0f, -400f, CustomAnimator.Property.TRANSLATIONX);
            //CustomAnimator.executeAnimation(mViewPager,400f, 0f, CustomAnimator.Property.TRANSLATIONX);
            CustomAnimator.executeAnimation(mBottomToolbar, 100f, 0f, CustomAnimator.Property.TRANSLATIONY);
            mListToolbar.setVisibility(View.GONE);
            //mBottomToolbar.setVisibility(View.VISIBLE);
        }
    }

    private void showFullScreen() {
        if (getSupportActionBar().isShowing()) {
            getSupportActionBar().hide();
            mListToolbar.setVisibility(View.GONE);
            mBottomToolbar.setVisibility(View.GONE);
            mFButton.setVisibility(View.VISIBLE);
        } else {
            getSupportActionBar().show();
            mBottomToolbar.setVisibility(View.VISIBLE);
            mFButton.setVisibility(View.GONE);
        }
    }

    private void showToolSearch() {

    }

    @Override
    public Context context() {
        return this;
    }

    public void goToBack() {
        onBackPressed();
    }
}
