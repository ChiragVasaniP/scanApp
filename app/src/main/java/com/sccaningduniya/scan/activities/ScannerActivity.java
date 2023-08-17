package com.sccaningduniya.scan.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.sccaningduniya.scan.R;
import com.sccaningduniya.scan.activities.generator.GeneratorResultActivity;
import com.sccaningduniya.scan.handlers.ButtonHandler;
import com.sccaningduniya.scan.handlers.GeneralHandler;
import com.sccaningduniya.scan.database.HistoryEntity;
import com.sccaningduniya.scan.repository.HistoryViewModel;
import com.bumptech.glide.load.Key;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.android.Intents;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

@SuppressWarnings("ALL")

public class ScannerActivity extends AppCompatActivity {
    private static final String STATE_QRCODEFORMAT = "format";
    private static final SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private static final String STATE_QRCODE = MainActivity.class.getName();
    final Activity activity = this;
    Bitmap bitmap;
    ImageView ivCopy;
    ImageView ivShare;
    ImageView ivWeb;
    MultiFormatWriter multiFormatWriter;
    private ImageView codeImage;
    private GeneralHandler generalHandler;
    private TextView mLabelFormat;
    private TextView mTvFormat;
    private TextView mTvInformation;
    private String qrcode = "";
    private String qrcodeFormat = "";

    @Override
    
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(STATE_QRCODE, this.qrcode);
        savedInstanceState.putString(STATE_QRCODEFORMAT, this.qrcodeFormat);
        this.generalHandler.loadTheme();
    }

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralHandler generalHandler = new GeneralHandler(this);
        this.generalHandler = generalHandler;
        generalHandler.loadTheme();
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_scanner);
        this.ivCopy = (ImageView) findViewById(R.id.ivCopy);
        this.ivWeb = (ImageView) findViewById(R.id.ivOpenInWeb);
        this.ivShare = (ImageView) findViewById(R.id.ivShare);
        LinearLayout banner = (LinearLayout) findViewById(R.id.banner_qr_scanner);

        this.mTvInformation = (TextView) findViewById(R.id.tvTxtqrcode);
        this.mTvFormat = (TextView) findViewById(R.id.tvFormat);
        this.mLabelFormat = (TextView) findViewById(R.id.labelFormat);
        ImageView imageView = (ImageView) findViewById(R.id.resultImageMain);
        this.codeImage = imageView;
        imageView.setClickable(true);
        this.codeImage.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Intent intent = new Intent(ScannerActivity.this, GeneratorResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("CODE", ScannerActivity.this.qrcode);
                int formatID = ScannerActivity.this.generalHandler.StringToBarcodeId(ScannerActivity.this.qrcodeFormat);
                bundle.putInt("FORMAT", formatID);
                intent.putExtras(bundle);
                ScannerActivity.this.startActivity(intent);
            }
        });
        if (savedInstanceState != null) {
            this.qrcode = savedInstanceState.getString(STATE_QRCODE);
            this.qrcodeFormat = savedInstanceState.getString(STATE_QRCODEFORMAT);
            if (this.qrcode.equals("")) {
                zxingScan();
                return;
            }
            this.codeImage.setVisibility(0);
            showQrImage();
            this.mTvFormat.setVisibility(0);
            this.mLabelFormat.setVisibility(0);
            this.mTvFormat.setText(this.qrcodeFormat);
            this.mTvInformation.setText(this.qrcode);
            if (this.qrcode.contains("BEGIN:VCARD") & this.qrcode.contains("END:VCARD")) {
                this.ivWeb.setVisibility(8);
                return;
            } else {
                this.ivWeb.setVisibility(0);
                return;
            }
        }
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if ("android.intent.action.SEND".equals(action) && type != null) {
            if (type.toLowerCase().startsWith("image")) {
                handleSendPicture();
                return;
            }
            return;
        }
        zxingScan();
    }

    private void showQrImage() {
        this.multiFormatWriter = new MultiFormatWriter();
        Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
        hintMap.put(EncodeHintType.CHARACTER_SET, Key.STRING_CHARSET_NAME);
        hintMap.put(EncodeHintType.MARGIN, 1);
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        try {
            BarcodeFormat format = this.generalHandler.StringToBarcodeFormat(this.qrcodeFormat);
            BitMatrix bitMatrix = this.multiFormatWriter.encode(this.qrcode, format, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION, hintMap);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap createBitmap = barcodeEncoder.createBitmap(bitMatrix);
            this.bitmap = createBitmap;
            this.codeImage.setImageBitmap(createBitmap);
            this.codeImage.setEnabled(true);
        } catch (Exception e) {
            this.codeImage.setImageResource(R.drawable.ic_baseline_error_24);
            this.codeImage.setEnabled(false);
        }
    }

    @Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                finish();
                return;
            }
            this.qrcodeFormat = result.getFormatName();
            String contents = result.getContents();
            this.qrcode = contents;
            if (!contents.equals("")) {
                this.codeImage.setVisibility(0);
                showQrImage();
                this.mTvFormat.setVisibility(0);
                this.mLabelFormat.setVisibility(0);
                this.mTvFormat.setText(this.qrcodeFormat);
                this.mTvInformation.setText(this.qrcode);
                if (this.qrcode.contains("BEGIN:VCARD") & this.qrcode.contains("END:VCARD")) {
                    this.ivWeb.setVisibility(8);
                } else {
                    this.ivWeb.setVisibility(0);
                }
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String history_setting = prefs.getString("pref_history", "");
                if (!history_setting.equals("false")) {
                    addToDatabase(this.mTvInformation.getText().toString(), this.mTvFormat.getText().toString());
                }
                Boolean auto_scan = Boolean.valueOf(prefs.getBoolean("pref_start_auto_clipboard", false));
                if (auto_scan.booleanValue()) {
                    ButtonHandler.copyToClipboard(this.mTvInformation, this.qrcode, this.activity);
                    return;
                }
                return;
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void zxingScan() {
        IntentIntegrator integrator = new IntentIntegrator(this.activity);
        integrator.addExtra(Intents.Scan.SCAN_TYPE, 2);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt((String) getResources().getText(R.string.xzing_label));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String camera_setting = prefs.getString("pref_camera", "");
        if (camera_setting.equals("1")) {
            integrator.setCameraId(1);
        } else {
            integrator.setCameraId(0);
        }
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        try {
            integrator.initiateScan();
        } catch (ArithmeticException e) {
        }
    }

    public void addToDatabase(String newCode, String format) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        HistoryEntity newEntry = new HistoryEntity(format, newCode, date.format((Date) timestamp));
        ViewModelProvider.Factory factory = (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory();
        HistoryViewModel historyViewModel = (HistoryViewModel) new ViewModelProvider(ScannerActivity.this, factory).get(HistoryViewModel.class);
        historyViewModel.insert(newEntry);
    }

    private void handleSendPicture() {
        int i;
        int i2;
        Uri imageUri = (Uri) getIntent().getExtras().get("android.intent.extra.STREAM");
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(imageUri);
        } catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_file_not_found), 1);
        }
        Bitmap bMap = BitmapFactory.decodeStream(imageStream);
        int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Reader reader = new MultiFormatReader();
        try {
            try {
                Hashtable<DecodeHintType, Object> decodeHints = new Hashtable<>();
                decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
                Result result = reader.decode(bitmap, decodeHints);
                this.qrcodeFormat = result.getBarcodeFormat().toString();
                String text = result.getText();
                this.qrcode = text;
                if (text != null) {
                    this.codeImage.setVisibility(0);
                    this.mLabelFormat.setVisibility(0);
                    this.mTvFormat.setVisibility(0);
                    this.mTvFormat.setText(this.qrcodeFormat);
                    this.mTvInformation.setText(this.qrcode);
                    showQrImage();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    String history_setting = prefs.getString("pref_history", "");
                    if (!history_setting.equals("false")) {
                        addToDatabase(this.mTvInformation.getText().toString(), this.mTvFormat.getText().toString());
                    }
                    String auto_scan = prefs.getString("pref_auto_clipboard", "");
                    if (auto_scan.equals("true")) {
                        ButtonHandler.copyToClipboard(this.mTvInformation, this.qrcode, this.activity);
                    }
                    if (this.qrcode.contains("BEGIN:VCARD") & this.qrcode.contains("END:VCARD")) {
                        this.ivWeb.setVisibility(8);
                    } else {
                        this.ivWeb.setVisibility(0);
                    }
                    return;
                }
                Activity activity = this.activity;
                Resources resources = getResources();
                i2 = R.string.error_code_not_found;
                Toast.makeText(activity, resources.getText(R.string.error_code_not_found), 1).show();
            } catch (ChecksumException e5) {
                Toast.makeText(this.activity, getResources().getText(R.string.error_code_not_found), 1).show();
            } catch (FormatException e6) {
                Toast.makeText(this.activity, getResources().getText(R.string.error_code_not_found), 1).show();
            } catch (ArrayIndexOutOfBoundsException e7) {
                Toast.makeText(this.activity, getResources().getText(R.string.error_code_not_found), 1).show();
            }
        } catch (NotFoundException e8) {
            i = 1;
        }
    }

    public void goBack(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivCopy :
                ButtonHandler.copyToClipboard(this.mTvInformation, this.qrcode, this.activity);
                return;
            case R.id.ivOpenInWeb :
                ButtonHandler.openInWeb(this.qrcode, this.qrcodeFormat, this.activity);
                return;
            case R.id.ivShare :
                ButtonHandler.shareTo(this.qrcode, this.activity);
                return;
            default:
                return;
        }
    }
}
