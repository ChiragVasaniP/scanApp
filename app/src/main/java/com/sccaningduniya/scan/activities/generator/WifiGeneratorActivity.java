package com.sccaningduniya.scan.activities.generator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.sccaningduniya.scan.R;
import com.sccaningduniya.scan.handlers.GeneralHandler;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.integration.android.IntentIntegrator;

@SuppressLint("ALL")

public class WifiGeneratorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String STATE_ENCRYPT = "";
    private static final String STATE_HIDDEN = "";
    private static final String STATE_PASSWORD = "";
    private static final String STATE_SSID = "";
    final Activity activity = this;
    Button btnGenerate;
    CheckBox cbHidden;
    String encrypt;
    int format;
    MultiFormatWriter multiFormatWriter;
    CardView passCard;
    String password;
    String result;
    String ssid;
    EditText tfPassword;
    EditText tfSSID;
    String hidden = "false";

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralHandler generalHandler = new GeneralHandler(this);
        generalHandler.loadTheme();
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_barcode_generate);
        setContentView(R.layout.activity_wifi_generator);
        FrameLayout nativeAd = (FrameLayout) findViewById(R.id.nativeFrameWifi);

        passCard = (CardView) findViewById(R.id.passCard);
        tfSSID = (EditText) findViewById(R.id.tfSSID);
        tfPassword = (EditText) findViewById(R.id.tfPassword);
        cbHidden = (CheckBox) findViewById(R.id.cbHidden);
        Button button = (Button) findViewById(R.id.btnGenerateWifi);
        btnGenerate = button;
        button.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                ssid = tfSSID.getText().toString().trim();
                password = tfPassword.getText().toString().trim();
                if (ssid.equals("") || ((encrypt.equals("WEP") && password.equals("")) || (encrypt.equals("WPA") && password.equals("")))) {
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_geo_first), 0).show();
                    return;
                }
                multiFormatWriter = new MultiFormatWriter();
                try {
                    if (!hidden.equals("true") || !encrypt.equals("nopass")) {
                        if (!hidden.equals("false") || !encrypt.equals("nopass")) {
                            if (hidden.matches("false")) {
                                result = "WIFI:S:" + ssid + ";T:" + encrypt + ";P:" + password + ";;";
                            } else {
                                result = "WIFI:S:" + ssid + ";T:" + encrypt + ";P:" + password + ";H:true;";
                            }
                        } else {
                            result = "WIFI:S:" + ssid + ";T:" + encrypt + ";P:;;";
                        }
                    } else {
                        result = "WIFI:S:" + ssid + ";T:" + encrypt + ";P:;H:true;";
                    }
                    openResultActivity();
                } catch (Exception e) {
                    Toast.makeText(activity.getApplicationContext(), getResources().getText(R.string.error_generate), 1).show();
                }
            }
        });
        Spinner formatSpinner = (Spinner) findViewById(R.id.spinner);
        Spinner encrypSpinner = (Spinner) findViewById(R.id.spinnerWifi);
        formatSpinner.setOnItemSelectedListener(this);
        encrypSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.text_formats_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(17367049);
        formatSpinner.setAdapter((SpinnerAdapter) adapter);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.text_formats_encryption, R.layout.spinner_item);
        adapter2.setDropDownViewResource(17367049);
        encrypSpinner.setAdapter((SpinnerAdapter) adapter2);
        if (savedInstanceState != null) {
            String str = (String) savedInstanceState.get("");
            ssid = str;
            tfSSID.setText(str);
            String str2 = (String) savedInstanceState.get("");
            password = str2;
            tfPassword.setText(str2);
            String str3 = (String) savedInstanceState.get("");
            encrypt = str3;
            if (str3.equals("nopass")) {
                tfPassword.setVisibility(8);
                passCard.setVisibility(8);
            }
            String str4 = (String) savedInstanceState.get("");
            hidden = str4;
            if (str4.equals("false")) {
                cbHidden.setChecked(false);
            }
        }
    }

    @Override
    
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("", ssid);
        savedInstanceState.putString("", password);
        savedInstanceState.putString("", encrypt);
        savedInstanceState.putString("", hidden);
    }

    @Override 
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id2) {
        String compare = parent.getItemAtPosition(position).toString();
        if (compare.toUpperCase().equals("AZTEC")) {
            format = 10;
        } else if (compare.toUpperCase().equals(IntentIntegrator.QR_CODE)) {
            format = 9;
        } else if (compare.toUpperCase().equals("NONE")) {
            encrypt = "nopass";
            tfPassword.setVisibility(8);
            passCard.setVisibility(8);
        } else if (compare.toUpperCase().equals("WEP")) {
            encrypt = "WEP";
            tfPassword.setVisibility(0);
        } else if (compare.toUpperCase().equals("WPA/WPA2")) {
            encrypt = "WPA";
            tfPassword.setVisibility(0);
        }
    }

    @Override 
    public void onNothingSelected(AdapterView<?> parent) {
        format = 9;
        encrypt = "nopass";
    }

    public void onClickCheckboxes(View v) {
        if (cbHidden.isChecked()) {
            hidden = "true";
        } else {
            hidden = "false";
        }
    }

    
    public void openResultActivity() {
        Intent intent = new Intent(this, GeneratorResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("CODE", result);
        bundle.putInt("FORMAT", format);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}
