package com.unisys.android.pdfviewer.presentation;

import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class BasePresenter {

    private ParcelFileDescriptor mFileDescriptor;

    private PdfRenderer mPdfRenderer;

    public void init(File file) {
        try {
            mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void renderPage(int index);

}
