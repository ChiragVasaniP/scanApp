package com.sccaningduniya.scan.activities.generator;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@SuppressLint("ALL")

public class TextGeneratorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String STATE_TEXT = MainActivity.class.getName();
    Button btnGenerate;
    int format;
    EditText text;
    String text2Qr;

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralHandler generalHandler = new GeneralHandler(this);
        generalHandler.loadTheme();
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_text_generator);
        FrameLayout nativeFrame = (FrameLayout) findViewById(R.id.nativeFrameBarcode);

        this.text = (EditText) findViewById(R.id.txtQR);
        Button button = (Button) findViewById(R.id.btnGenerateText);
        this.btnGenerate = button;
        button.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                TextGeneratorActivity textGeneratorActivity = TextGeneratorActivity.this;
                textGeneratorActivity.text2Qr = textGeneratorActivity.text.getText().toString().trim();
                if (!TextGeneratorActivity.this.text2Qr.equals("")) {
                    TextGeneratorActivity.this.openResultActivity();
                } else {
                    Toast.makeText(TextGeneratorActivity.this.getApplicationContext(), TextGeneratorActivity.this.getResources().getText(R.string.error_text_first), 0).show();
                }
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.text_formats_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(17367049);
        spinner.setAdapter((SpinnerAdapter) adapter);
        if (savedInstanceState != null) {
            String str = (String) savedInstanceState.get(STATE_TEXT);
            this.text2Qr = str;
            this.text.setText(str);
        }
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if ("android.intent.action.SEND".equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
            } else if ("text/x-vcard".equals(type)) {
                handleSendContact(intent);
            }
        }
    }

    @Override
    
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(STATE_TEXT, this.text2Qr);
    }

    private void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra("android.intent.extra.TEXT");
        if (sharedText != null) {
            this.text.setText(sharedText);
            this.text2Qr = sharedText;
        }
    }

    private void handleSendContact(Intent intent) {
        Uri uri = (Uri) intent.getExtras().get("android.intent.extra.STREAM");
        ContentResolver cr = getContentResolver();
        InputStream stream = null;
        try {
            stream = cr.openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringBuffer fileContent = new StringBuffer("");
        while (true) {
            try {
                int ch = stream.read();
                if (ch == -1) {
                    break;
                }
                fileContent.append((char) ch);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        String data = new String(fileContent);
        this.text.setText(data);
        this.text2Qr = data;
    }

    @Override 
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id2) {
        String compare = parent.getItemAtPosition(position).toString();
        if (compare.equals("AZTEC")) {
            this.format = 10;
        } else if (compare.equals(IntentIntegrator.QR_CODE)) {
            this.format = 9;
        }
    }

    @Override 
    public void onNothingSelected(AdapterView<?> parent) {
        this.format = 9;
    }

    
    public void openResultActivity() {
        Intent intent = new Intent(this, GeneratorResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("CODE", this.text2Qr);
        bundle.putInt("FORMAT", this.format);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}
