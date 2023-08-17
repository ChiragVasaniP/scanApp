package com.sccaningduniya.scan.activities.generator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sccaningduniya.scan.R;
import com.sccaningduniya.scan.handlers.GeneralHandler;
import com.bumptech.glide.load.Key;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.EnumMap;
import java.util.Map;

@SuppressLint("ALL")

public class GeneratorResultActivity extends AppCompatActivity {
    final int REQ_EXTERNAL_STORAGE_PERMISSION = 97;
    Bitmap bitmap;
    Button btnSave;
    ImageView codeImage;
    MultiFormatWriter multiFormatWriter;
    private BarcodeFormat format;
    private int formatInt;
    private String text2Code;

    public static Uri getImageUri(Bitmap bm, Context ctx) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bm, "image", (String) null);
        return Uri.parse(path);
    }

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralHandler generalHandler = new GeneralHandler(this);
        generalHandler.loadTheme();
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_barcode_generate);
        setContentView(R.layout.activity_generator_result);
        this.codeImage = (ImageView) findViewById(R.id.resultImage);
        this.btnSave = (Button) findViewById(R.id.btnSave);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        this.text2Code = intent.getStringExtra("CODE");
        int i = bundle.getInt("FORMAT");
        this.formatInt = i;
        this.format = generalHandler.idToBarcodeFormat(i);
        this.multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
        hintMap.put(EncodeHintType.CHARACTER_SET, Key.STRING_CHARSET_NAME);
        hintMap.put(EncodeHintType.MARGIN, 1);
        int i2 = C08552.$SwitchMap$com$google$zxing$BarcodeFormat[this.format.ordinal()];
        if (i2 == 1) {
            hintMap.put(EncodeHintType.ERROR_CORRECTION, 25);
        } else {
            if (i2 == 2) {
                hintMap.put(EncodeHintType.ERROR_CORRECTION, 0);
            }
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        }
        try {
            BitMatrix bitMatrix = this.multiFormatWriter.encode(this.text2Code, this.format, 1000, 1000, hintMap);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap createBitmap = barcodeEncoder.createBitmap(bitMatrix);
            this.bitmap = createBitmap;
            this.codeImage.setImageBitmap(createBitmap);
        } catch (WriterException e) {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.toast_error_data_to_big), 0).show();
            finish();
        } catch (Exception e2) {
            Log.d("ERROR:", e2.toString());
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_generate), 0).show();
            finish();
        }
        this.btnSave.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Bitmap bitmap = ((BitmapDrawable) codeImage.getDrawable()).getBitmap();
                Uri uri = getImageUri(bitmap, GeneratorResultActivity.this);
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("image/jpeg");
                intent2.putExtra("android.intent.extra.STREAM", uri);
                intent2.putExtra("android.intent.extra.TEXT", "My QR Code, created with #" + getString(R.string.app_name));
                startActivity(Intent.createChooser(intent2, "Share on..."));
            }
        });
    }

    @Override 
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void goBack(View view) {
        super.onBackPressed();
    }

    
    
    static  class C08552 {
        static final  int[] $SwitchMap$com$google$zxing$BarcodeFormat;

        static {
            int[] iArr = new int[BarcodeFormat.values().length];
            $SwitchMap$com$google$zxing$BarcodeFormat = iArr;
            try {
                iArr[BarcodeFormat.AZTEC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$google$zxing$BarcodeFormat[BarcodeFormat.PDF_417.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }
}
