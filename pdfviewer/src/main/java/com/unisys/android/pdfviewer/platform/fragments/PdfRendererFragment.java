/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.unisys.android.pdfviewer.platform.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unisys.android.pdfviewer.platform.custom.ZoomImageView;
import com.vespasoft.android.pdfviewer.R;
import com.unisys.android.pdfviewer.platform.activities.PdfViewerActivity;
import com.unisys.android.pdfviewer.presentation.DocumentViewerPresenter;
import com.unisys.android.pdfviewer.platform.views.PdfView;

import java.io.File;

/**
 * This fragment has a big {@ImageView} that shows PDF pages
 * {@link android.graphics.pdf.PdfRenderer} to render PDF pages as
 * {@link android.graphics.Bitmap}s.
 */
public class PdfRendererFragment extends Fragment implements PdfView {

    /**
     * Key string for saving the state of current page index.
     */
    private static final String STATE_CURRENT_PAGE_INDEX = "current_page_index";

    /**
     * {@link com.unisys.android.pdfviewer.platform.custom.PdfViewerView} that shows a PDF page as a {@link File}
     */
    private ZoomImageView mViewer;

    private DocumentViewerPresenter pdfViewerPresenter;

    private int mPageIndex = 0;

    public static PdfRendererFragment newInstance(int page) {
        PdfRendererFragment fragmentFirst = new PdfRendererFragment();
        Bundle args = new Bundle();
        args.putInt(STATE_CURRENT_PAGE_INDEX, page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    public PdfRendererFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // when the fragment has instancied by newInstance method
        mPageIndex = getArguments().getInt(STATE_CURRENT_PAGE_INDEX, 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdf_renderer, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PdfViewerActivity activity = (PdfViewerActivity) getActivity();
        Uri uri = activity.getIntent().getData();
        File file = new File(uri.getPath());

        // Retain view references.
        mViewer = view.findViewById(R.id.viewer);

        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 1);
        }
        // create instance of View Presenter
        pdfViewerPresenter = new DocumentViewerPresenter();
        pdfViewerPresenter.setView(this, file, mPageIndex);
        Log.i("PDF", "Page index -> " + mPageIndex);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != pdfViewerPresenter) {
            outState.putInt(STATE_CURRENT_PAGE_INDEX, pdfViewerPresenter.getCurrentPage());
        }
    }

    @Override
    public void renderPage(Bitmap bitmap) {
        // We are ready to show the Bitmap to user.
        mViewer.setImageBitmap(bitmap);
    }

    @Override
    public Context context() {
        return getContext();
    }
}
