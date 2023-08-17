package com.sccaningduniya.scan.activities.generator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sccaningduniya.scan.R;
import com.sccaningduniya.scan.activities.MainActivity;
import com.sccaningduniya.scan.handlers.GeneralHandler;
import com.google.zxing.integration.android.IntentIntegrator;

@SuppressWarnings("ALL")

public class BarcodeGenerateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String STATE_TEXT = MainActivity.class.getName();
    Button btnGenerate;
    int format;
    EditText text;
    String text2Barcode;

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralHandler generalHandler = new GeneralHandler(this);
        generalHandler.loadTheme();
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_barcode_generate);
        FrameLayout nativeFrame = (FrameLayout) findViewById(R.id.nativeFrameBarcode);

        this.text = (EditText) findViewById(R.id.tfBarcode);
        Button button = (Button) findViewById(R.id.btnGenerateBarcode);
        this.btnGenerate = button;
        button.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                text2Barcode = text.getText().toString().trim();
                if (!text2Barcode.equals("")) {
                    openResultActivity();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_text_first), 0).show();
                }
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.barcode_formats_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(17367049);
        spinner.setAdapter((SpinnerAdapter) adapter);
        if (savedInstanceState != null) {
            String str = (String) savedInstanceState.get(STATE_TEXT);
            this.text2Barcode = str;
            this.text.setText(str);
        }
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if ("android.intent.action.SEND".equals(action) && type != null && "text/plain".equals(type)) {
            handleSendText(intent);
        }
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra("android.intent.extra.TEXT");
        if (sharedText != null) {
            this.text.setText(sharedText);
            this.text2Barcode = sharedText;
        }
    }

    @Override
    
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(STATE_TEXT, this.text2Barcode);
    }

    
    @Override 
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        char c;
        String compare = adapterView.getItemAtPosition(position).toString();
        switch (compare.hashCode()) {
            case -84093723:
                if (compare.equals(IntentIntegrator.CODE_128)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 72827:
                if (compare.equals(IntentIntegrator.ITF)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 160877:
                if (compare.equals(IntentIntegrator.PDF_417)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 65737323:
                if (compare.equals(IntentIntegrator.EAN_8)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 80949962:
                if (compare.equals(IntentIntegrator.UPC_A)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 384398432:
                if (compare.equals("BARCODE")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 1659855352:
                if (compare.equals(IntentIntegrator.CODE_39)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 2037856847:
                if (compare.equals(IntentIntegrator.EAN_13)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                this.format = 1;
                return;
            case 1:
                this.format = 2;
                return;
            case 2:
                this.format = 3;
                return;
            case 3:
                this.format = 4;
                return;
            case 4:
                this.format = 5;
                return;
            case 5:
                this.format = 5;
                return;
            case 6:
                this.format = 6;
                return;
            case 7:
                this.format = 7;
                return;
            default:
                this.format = 1;
                return;
        }
    }

    @Override 
    public void onNothingSelected(AdapterView<?> adapterView) {
        this.format = 1;
    }

    
    public void openResultActivity() {
        Intent intent = new Intent(this, GeneratorResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("CODE", this.text2Barcode);
        bundle.putInt("FORMAT", this.format);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}
