package com.unisys.android.pdfviewer.presentation;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import com.unisys.android.pdfviewer.platform.views.PdfView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class DocumentViewerPresenter {

    private int mPageIndex;

    private File mFile;

    private ParcelFileDescriptor mFileDescriptor;

    private PdfRenderer mPdfRenderer;

    private PdfRenderer.Page mCurrentPage;

    private WeakReference<PdfView> view;

    public DocumentViewerPresenter() {
    }

    public void setView(PdfView pdfView, File file, int index){
        this.view = new WeakReference<>(pdfView);
        mFile = file;
        mPageIndex = index;

        init();
        showPage(index);
    }

    private void init() {
        try {
            mFileDescriptor = ParcelFileDescriptor.open(mFile, ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPage(int index) {
        if (getItemsCount() <= index) {
            return;
        }
        onClosePage();
        // Use `openPage` to open a specific page in PDF.
        mCurrentPage = mPdfRenderer.openPage(index);

        Bitmap bitmap = Bitmap.createBitmap(
                view.get().context().getResources().getDisplayMetrics().densityDpi * mCurrentPage.getWidth() / 72,
                view.get().context().getResources().getDisplayMetrics().densityDpi * mCurrentPage.getHeight() / 72,
                Bitmap.Config.ARGB_8888);

        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        view.get().renderPage(bitmap);
    }

    public int getCurrentPage() {
        return mPageIndex;
    }

    private void onClosePage() {
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
    }

    public int getItemsCount() {
        return mPdfRenderer.getPageCount();
    }

}
