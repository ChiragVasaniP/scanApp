package com.sccaningduniya.scan.activities;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.viewpager2.widget.ViewPager2;

import com.sccaningduniya.scan.R;
import com.sccaningduniya.scan.adapters.ImageAdapter;
import com.sccaningduniya.scan.adapters.ImageAdapterListener;
import com.tbruyelle.rxpermissions3.Permission;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.zynksoftware.documentscanner.ScanActivity;
import com.zynksoftware.documentscanner.model.DocumentScannerErrorModel;
import com.zynksoftware.documentscanner.model.ScannerResults;
import com.zynksoftware.documentscanner.ui.components.ProgressView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.reactivex.rxjava3.functions.Consumer;
import kotlin.io.FilesKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;

@SuppressWarnings("ALL")
public final class AppScanActivity extends ScanActivity implements ImageAdapterListener {
    public static final Companion Companion = new Companion(null);
    private static final String TAG = Reflection.getOrCreateKotlinClass(AppScanActivity.class).getSimpleName();
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;

    
    
    public static final void m112checkForStoragePermissions$lambda0(AppScanActivity this$0, File image, Permission permission) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(image, "$image");
        if (permission.granted) {
            try {
                this$0.saveImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (permission.shouldShowRequestPermissionRationale) {
            this$0.onError(new DocumentScannerErrorModel(DocumentScannerErrorModel.ErrorMessage.STORAGE_PERMISSION_REFUSED_WITHOUT_NEVER_ASK_AGAIN, new Throwable()));
        } else {
            this$0.onError(new DocumentScannerErrorModel(DocumentScannerErrorModel.ErrorMessage.STORAGE_PERMISSION_REFUSED_GO_TO_SETTINGS, new Throwable()));
        }
    }

    
    
    public static final void m113initViewPager$lambda8(AppScanActivity this$0, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        ((ViewPager2) this$0.findViewById(R.id.viewPagerTwo)).setCurrentItem(((ViewPager2) this$0.findViewById(R.id.viewPagerTwo)).getCurrentItem() - 1);
        View nextButton = (ImageView) this$0.findViewById(R.id.nextButton);
        Intrinsics.checkNotNullExpressionValue(nextButton, "nextButton");
        View $this$isVisible$iv = nextButton;
        $this$isVisible$iv.setVisibility(0);
        if (((ViewPager2) this$0.findViewById(R.id.viewPagerTwo)).getCurrentItem() == 0) {
            View previousButton = (ImageView) this$0.findViewById(R.id.previousButton);
            Intrinsics.checkNotNullExpressionValue(previousButton, "previousButton");
            View $this$isVisible$iv2 = previousButton;
            $this$isVisible$iv2.setVisibility(8);
        }
    }

    
    
    public static final void m114initViewPager$lambda9(AppScanActivity this$0, ArrayList fileList, View it) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Intrinsics.checkNotNullParameter(fileList, "$fileList");
        ((ViewPager2) this$0.findViewById(R.id.viewPagerTwo)).setCurrentItem(((ViewPager2) this$0.findViewById(R.id.viewPagerTwo)).getCurrentItem() + 1);
        View previousButton = (ImageView) this$0.findViewById(R.id.previousButton);
        Intrinsics.checkNotNullExpressionValue(previousButton, "previousButton");
        View $this$isVisible$iv = previousButton;
        $this$isVisible$iv.setVisibility(0);
        if (((ViewPager2) this$0.findViewById(R.id.viewPagerTwo)).getCurrentItem() == fileList.size() - 1) {
            View nextButton = (ImageView) this$0.findViewById(R.id.nextButton);
            Intrinsics.checkNotNullExpressionValue(nextButton, "nextButton");
            View $this$isVisible$iv2 = nextButton;
            $this$isVisible$iv2.setVisibility(8);
        }
    }

    
    
    public static final void m117showAlertDialog$lambda10(DialogInterface dialog, int which) {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_scan_activity_layout);
        addFragmentContentLayout();
    }

    @Override 
    public void onError(DocumentScannerErrorModel error) {
        Intrinsics.checkNotNullParameter(error, "error");
        String string = getString(R.string.error_label);
        DocumentScannerErrorModel.ErrorMessage errorMessage = error.getErrorMessage();
        String error2 = errorMessage == null ? null : errorMessage.getError();
        String string2 = getString(R.string.ok_label);
        Intrinsics.checkNotNullExpressionValue(string2, "getString(R.string.ok_label)");
        showAlertDialog(string, error2, string2);
    }

    @Override 
    public void onSuccess(ScannerResults scannerResults) {
        Intrinsics.checkNotNullParameter(scannerResults, "scannerResults");
        initViewPager(scannerResults);
    }

    @Override 
    public void onClose() {
        Log.d(TAG, "onClose");
        finish();
    }

    @Override 
    public void onSaveButtonClicked(File image) {
        Intrinsics.checkNotNullParameter(image, "image");
        checkForStoragePermissions(image);
    }

    public final void checkForStoragePermissions(final File image) {
        Intrinsics.checkNotNullParameter(image, "image");
        new RxPermissions(this).requestEach("android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE").subscribe(new Consumer() { 
            @Override 
            public final void accept(Object obj) throws IOException {
                AppScanActivity.m112checkForStoragePermissions$lambda0(AppScanActivity.this, image, (Permission) obj);
            }
        });
    }

    private final void saveImage(File image) throws IOException {
        showProgressBar();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss:mm", Locale.getDefault());
        String dateFormatted = formatter.format(date);
        File to = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + '/' + ((Object) Environment.DIRECTORY_DCIM) + "/zynkphoto" + ((Object) dateFormatted) + ".jpg");
        InputStream inputStream = new FileInputStream(image);
        if (Build.VERSION.SDK_INT >= 29) {
            ContentResolver contentResolver = getContentResolver();
            Intrinsics.checkNotNullExpressionValue(contentResolver, "contentResolver");
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", "zynkphoto" + ((Object) dateFormatted) + ".jpg");
            contentValues.put("mime_type", "image/*");
            contentValues.put("relative_path", "DCIM");
            Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            Intrinsics.checkNotNull(imageUri);
            OutputStream out = contentResolver.openOutputStream(imageUri);
            if (out != null) {
                out.write(FilesKt.readBytes(image));
            }
            if (out != null) {
                out.flush();
            }
            if (out != null) {
                out.close();
            }
        } else {
            OutputStream out2 = new FileOutputStream(to);
            byte[] buf = new byte[1024];
            while (true) {
                int it = inputStream.read(buf);
                if (it <= 0) {
                    break;
                }
                out2.write(buf, 0, it);
            }
            inputStream.close();
            out2.flush();
            out2.close();
        }
        hideProgessBar();
        String string = getString(R.string.photo_saved);
        String string2 = getString(R.string.ok_label);
        Intrinsics.checkNotNullExpressionValue(string2, "getString(R.string.ok_label)");
        showAlertDialog(string, "", string2);
    }

    private final void showProgressBar() {
        View progressLayoutApp = (ProgressView) findViewById(R.id.progressLayoutApp);
        Intrinsics.checkNotNullExpressionValue(progressLayoutApp, "progressLayoutApp");
        View $this$isVisible$iv = progressLayoutApp;
        $this$isVisible$iv.setVisibility(0);
    }

    private final void hideProgessBar() {
        View progressLayoutApp = (ProgressView) findViewById(R.id.progressLayoutApp);
        Intrinsics.checkNotNullExpressionValue(progressLayoutApp, "progressLayoutApp");
        View $this$isVisible$iv = progressLayoutApp;
        $this$isVisible$iv.setVisibility(8);
    }

    private final void initViewPager(ScannerResults scannerResults) {
        final ArrayList fileList = new ArrayList();
        File it = scannerResults.getOriginalImageFile();
        if (it != null) {
            Log.d(TAG, Intrinsics.stringPlus("ZDCoriginalPhotoFile size ", Double.valueOf(getSizeInMb(it))));
        }
        File it2 = scannerResults.getCroppedImageFile();
        if (it2 != null) {
            Log.d(TAG, Intrinsics.stringPlus("ZDCcroppedPhotoFile size ", Double.valueOf(getSizeInMb(it2))));
        }
        File it3 = scannerResults.getTransformedImageFile();
        if (it3 != null) {
            Log.d(TAG, Intrinsics.stringPlus("ZDCtransformedPhotoFile size ", Double.valueOf(getSizeInMb(it3))));
        }
        File it4 = scannerResults.getOriginalImageFile();
        if (it4 != null) {
            fileList.add(it4);
        }
        File it5 = scannerResults.getTransformedImageFile();
        if (it5 != null) {
            fileList.add(it5);
        }
        File it6 = scannerResults.getCroppedImageFile();
        if (it6 != null) {
            fileList.add(it6);
        }
        ImageAdapter targetAdapter = new ImageAdapter(this, fileList, this);
        ((ViewPager2) findViewById(R.id.viewPagerTwo)).setAdapter(targetAdapter);
        ((ViewPager2) findViewById(R.id.viewPagerTwo)).setUserInputEnabled(false);
        ((ImageView) findViewById(R.id.previousButton)).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public final void onClick(View view) {
                AppScanActivity.m113initViewPager$lambda8(AppScanActivity.this, view);
            }
        });
        ((ImageView) findViewById(R.id.nextButton)).setOnClickListener(new View.OnClickListener() { 
            @Override 
            public final void onClick(View view) {
                AppScanActivity.m114initViewPager$lambda9(AppScanActivity.this, fileList, view);
            }
        });
    }

    private final void showAlertDialog(String title, String message, String buttonMessage) {
        this.alertDialogBuilder = new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton(buttonMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppScanActivity.m117showAlertDialog$lambda10(dialogInterface, i);
            }
        });
        AlertDialog alertDialog = this.alertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        AlertDialog.Builder builder = this.alertDialogBuilder;
        AlertDialog create = builder == null ? null : builder.create();
        this.alertDialog = create;
        if (create != null) {
            create.setCanceledOnTouchOutside(false);
        }
        AlertDialog alertDialog2 = this.alertDialog;
        if (alertDialog2 == null) {
            return;
        }
        alertDialog2.show();
    }

    public final double getSize(File $this$size) {
        Intrinsics.checkNotNullParameter($this$size, "<this>");
        if ($this$size.exists()) {
            return $this$size.length();
        }
        return 0.0d;
    }

    public final double getSizeInKb(File $this$sizeInKb) {
        Intrinsics.checkNotNullParameter($this$sizeInKb, "<this>");
        return getSize($this$sizeInKb) / 1024;
    }

    public final double getSizeInMb(File $this$sizeInMb) {
        Intrinsics.checkNotNullParameter($this$sizeInMb, "<this>");
        return getSizeInKb($this$sizeInMb) / 1024;
    }

    public static final class Companion {
        public  Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final void start(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intent intent = new Intent(context, AppScanActivity.class);
            context.startActivity(intent);
        }
    }
}
