package com.sccaningduniya.scan.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.sccaningduniya.scan.R;
import com.sccaningduniya.scan.activities.generator.GeneratorResultActivity;
import com.sccaningduniya.scan.handlers.ButtonHandler;
import com.sccaningduniya.scan.database.DatabaseHelper;
import com.sccaningduniya.scan.handlers.GeneralHandler;
import com.bumptech.glide.load.Key;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings("ALL")

public class HistoryDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_FORMAT = "de.t-dankworth.secscanqr.EXTRA_FORMAT";
    public static final String EXTRA_INFORMATION = "de.t-dankworth.secscanqr.EXTRA_INFORMATION";
    final Activity activity = this;
    Bitmap bitmap;
    DatabaseHelper historyDatabaseHelper;
    ImageView ivCopy;
    ImageView ivShare;
    ImageView ivWeb;
    MultiFormatWriter multiFormatWriter;
    private ImageView codeImage;
    private GeneralHandler generalHandler;
    private String selectedCode;
    private String selectedFormat;
    private TextView tvCode;
    private TextView tvFormat;

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generalHandler = new GeneralHandler(this);
        generalHandler.loadTheme();
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_history_details);
        tvCode = (TextView) findViewById(R.id.tvTxtqrcode);
        tvFormat = (TextView) findViewById(R.id.tvFormat);
        ImageView imageView = (ImageView) findViewById(R.id.resultImageMain);
        codeImage = imageView;
        imageView.setClickable(true);
        LinearLayout banner = (LinearLayout) findViewById(R.id.banner_qr_scanner);
        historyDatabaseHelper = new DatabaseHelper(this);
        Intent receivedIntent = getIntent();
        String stringExtra = receivedIntent.getStringExtra(EXTRA_INFORMATION);
        selectedCode = stringExtra;
        tvCode.setText(stringExtra);
        String stringExtra2 = receivedIntent.getStringExtra(EXTRA_FORMAT);
        selectedFormat = stringExtra2;
        tvFormat.setText(stringExtra2);
        showQrImage();
        ivCopy = (ImageView) findViewById(R.id.ivCopy);
        ivWeb = (ImageView) findViewById(R.id.ivOpenInWeb);
        ivShare = (ImageView) findViewById(R.id.ivShare);
        if (selectedCode.contains("BEGIN:VCARD") & selectedCode.contains("END:VCARD")) {
            ivWeb.setVisibility(8);
        } else {
            ivWeb.setVisibility(0);
        }
        codeImage.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                Intent intent = new Intent(HistoryDetailsActivity.this, GeneratorResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CODE", selectedCode);
                int formatID = generalHandler.StringToBarcodeId(selectedFormat);
                bundle.putInt("FORMAT", formatID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void showQrImage() {
        multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
        hintMap.put(EncodeHintType.CHARACTER_SET, Key.STRING_CHARSET_NAME);
        hintMap.put(EncodeHintType.MARGIN, 1);
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        try {
            BarcodeFormat format = generalHandler.StringToBarcodeFormat(selectedFormat);
            BitMatrix bitMatrix = multiFormatWriter.encode(selectedCode, format, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, hintMap);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap createBitmap = barcodeEncoder.createBitmap(bitMatrix);
            bitmap = createBitmap;
            codeImage.setImageBitmap(createBitmap);
        } catch (Exception e) {
            codeImage.setVisibility(8);
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCopy :
                ButtonHandler.copyToClipboard(tvCode, selectedCode, activity);
                return;
            case R.id.ivOpenInWeb :
                ButtonHandler.openInWeb(selectedCode, activity);
                return;
            case R.id.ivShare :
                ButtonHandler.shareTo(selectedCode, activity);
                return;
            default:
                return;
        }
    }

    public void goBackk(View view) {
        super.onBackPressed();
    }
}
