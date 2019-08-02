package com.unisys.android.pdfviewer.domain;

import android.graphics.Bitmap;

public class PageModel {
    private int index;
    private Bitmap thumbnail;

    public PageModel(int index, Bitmap thumbnail) {
        this.index = index;
        this.thumbnail = thumbnail;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
