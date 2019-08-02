package com.unisys.android.pdfviewer.platform.views;

import android.content.Context;
import android.graphics.Bitmap;

public interface PdfView {

    void renderPage(Bitmap bitmap);

    Context context();

}
