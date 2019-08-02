package com.unisys.android.pdfviewer.presentation;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.unisys.android.pdfviewer.domain.PageModel;
import com.unisys.android.pdfviewer.platform.holder.PageHolder;
import com.unisys.android.pdfviewer.platform.views.ReaderView;
import com.vespasoft.android.pdfviewer.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class DocumentListPresenter {

    private File mFile;

    private String mTitle = "PDF Viewer (%1$d/%2$d)";

    private int mPageIndex;

    private ArrayList<PageModel> documents = new ArrayList<>();

    private WeakReference<ReaderView> view;

    private ParcelFileDescriptor mFileDescriptor;

    private PdfRenderer mPdfRenderer;

    private PdfRenderer.Page mCurrentPage;

    public void setView(ReaderView readerView, File file){
        this.view = new WeakReference<>(readerView);
        mTitle = view.get().context().getString(R.string.app_name_with_index);
        mFile = file;
        mPageIndex = 0;
        view.get().setUpToolbar();
        view.get().setUpControls();
        view.get().showLoading(true);
        init();
        new RendererDocument().execute();

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

    private void onClosePage() {
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
    }

    public void onItemClick(int position) {
        Log.i("Click", "onItemClick -> "+position);
        mPageIndex = position;
        view.get().refreshData();
    }

    public void onNext() {
        if (getItemSelected()<getItemsCount()-1) mPageIndex++;
        view.get().refreshData();
    }

    public void onPrevious() {
        if (getItemSelected()>0) mPageIndex--;
        view.get().refreshData();
    }

    public void configureCell(PageHolder holder, int position) {
        int cellColor = view.get().context().getResources().getColor(R.color.white);
        if (getItemSelected()==position) cellColor = view.get().context().getResources().getColor(R.color.medium_gray);
        holder.render(documents.get(position), cellColor);
    }

    public int getItemsCount() {
        return mPdfRenderer.getPageCount();
    }

    public int getItemSelected() {
        return mPageIndex;
    }

    public String getTitle() {
        mTitle = mFile.getName() + " (%1$d/%2$d)";
        mTitle = mTitle.replace("%1$d", String.valueOf(getItemSelected()+1));
        mTitle = mTitle.replace("%2$d", String.valueOf(getItemsCount()));
        return mTitle;
    }

    /**
     * Shows the specified page of PDF to the screen.
     *
     * @param index The page index.
     */
    public void renderPage(int index) {
        onClosePage();
        mCurrentPage = mPdfRenderer.openPage(index);
        Bitmap bitmap = Bitmap.createBitmap(
                350,
                350,
                Bitmap.Config.ARGB_8888);
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        // We are ready to show the Bitmap to user.
        documents.add(new PageModel(index, bitmap));
    }

    private class RendererDocument extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i=0; i<mPdfRenderer.getPageCount(); i++) {
                renderPage(i);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            view.get().showLoading(false);
            view.get().setupViewPager();
            view.get().setUpRecyclerView();
            view.get().refreshData();
        }
    }

}
