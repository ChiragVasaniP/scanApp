package com.sccaningduniya.scan;

import android.app.Application;
import android.graphics.Bitmap;

import com.sccaningduniya.scan.database.HistoryDatabase;
import com.zynksoftware.documentscanner.ui.DocumentScanner;


public final class BaseApp extends Application {
    private static final int FILE_QUALITY = 100;
    private static final long FILE_SIZE = 1000000;
    private static final Bitmap.CompressFormat FILE_TYPE = Bitmap.CompressFormat.JPEG;


    @Override 
    public void onCreate() {
        super.onCreate();
        HistoryDatabase.getInstance(BaseApp.this);
        DocumentScanner.Configuration configuration = new DocumentScanner.Configuration();
        configuration.setImageQuality(100);
        configuration.setImageType(FILE_TYPE);
        DocumentScanner.INSTANCE.init(this, configuration);
    }
}
