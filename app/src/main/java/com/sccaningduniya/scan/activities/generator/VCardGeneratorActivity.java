package com.sccaningduniya.scan.activities.generator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sccaningduniya.scan.R;
import com.sccaningduniya.scan.activities.MainActivity;
import com.sccaningduniya.scan.handlers.GeneralHandler;
import com.google.zxing.integration.android.IntentIntegrator;

@SuppressLint("ALL")

public class VCardGeneratorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String STATE_TEXT = MainActivity.class.getName();
    Button btnGenerate;
    int format;
    String[] text2Qr = new String[16];
    EditText tfBday;
    EditText tfCity;
    EditText tfCountry;
    EditText tfEmail;
    EditText tfFirstName;
    EditText tfMobil;
    EditText tfName;
    EditText tfNote;
    EditText tfOrg;
    EditText tfPLZ;
    EditText tfPhoto;
    EditText tfState;
    EditText tfStreet;
    EditText tfTelePrivate;
    EditText tfTeleWork;
    EditText tfWeb;
    String vcardCode;

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralHandler generalHandler = new GeneralHandler(this);
        generalHandler.loadTheme();
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_barcode_generate);
        setContentView(R.layout.activity_vcard_generator);
        initializeInputFields();
        btnGenerate = (Button) findViewById(R.id.btnGenerateVCard);
        FrameLayout nativeVCard = (FrameLayout) findViewById(R.id.nativeVCard);

        LinearLayout banner = (LinearLayout) findViewById(R.id.banner_vcard);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                insertValuesIntoArray();
                if (!text2Qr[0].equals("") || !text2Qr[1].equals("")) {
                    buildVCardCode();
                    openResultActivity();
                    return;
                }
                Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_fn_or_name_first), 0).show();
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.text_formats_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(17367049);
        spinner.setAdapter((SpinnerAdapter) adapter);
        if (savedInstanceState != null) {
            text2Qr = (String[]) savedInstanceState.get(STATE_TEXT);
            recoverOldValues();
        }
    }

    @Override
    
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putStringArray(STATE_TEXT, text2Qr);
    }

    @Override 
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id2) {
        String compare = parent.getItemAtPosition(position).toString();
        if (compare.equals("AZTEC")) {
            format = 10;
        } else if (compare.equals(IntentIntegrator.QR_CODE)) {
            format = 9;
        }
    }

    @Override 
    public void onNothingSelected(AdapterView<?> parent) {
        format = 9;
    }

    private void initializeInputFields() {
        tfFirstName = (EditText) findViewById(R.id.txtFN);
        tfName = (EditText) findViewById(R.id.txtName);
        tfBday = (EditText) findViewById(R.id.txtBday);
        tfOrg = (EditText) findViewById(R.id.txtOrg);
        tfPhoto = (EditText) findViewById(R.id.txtPhotoUri);
        tfTeleWork = (EditText) findViewById(R.id.txtTeleWork);
        tfTelePrivate = (EditText) findViewById(R.id.txtTelePrivat);
        tfMobil = (EditText) findViewById(R.id.txtMobil);
        tfEmail = (EditText) findViewById(R.id.txtEmail);
        tfWeb = (EditText) findViewById(R.id.txtWeb);
        tfStreet = (EditText) findViewById(R.id.txtStreet);
        tfPLZ = (EditText) findViewById(R.id.txtPLZ);
        tfCity = (EditText) findViewById(R.id.txtCity);
        tfState = (EditText) findViewById(R.id.txtState);
        tfCountry = (EditText) findViewById(R.id.txtCountry);
        tfNote = (EditText) findViewById(R.id.txtNote);
    }

    
    public void insertValuesIntoArray() {
        text2Qr[0] = tfName.getText().toString().trim();
        text2Qr[1] = tfFirstName.getText().toString().trim();
        text2Qr[2] = tfBday.getText().toString().trim();
        text2Qr[3] = tfOrg.getText().toString().trim();
        text2Qr[4] = tfPhoto.getText().toString().trim();
        text2Qr[5] = tfWeb.getText().toString().trim();
        text2Qr[6] = tfEmail.getText().toString().trim();
        text2Qr[7] = tfMobil.getText().toString().trim();
        text2Qr[8] = tfTeleWork.getText().toString().trim();
        text2Qr[9] = tfTelePrivate.getText().toString().trim();
        text2Qr[10] = tfStreet.getText().toString().trim();
        text2Qr[11] = tfCity.getText().toString().trim();
        text2Qr[12] = tfState.getText().toString().trim();
        text2Qr[13] = tfPLZ.getText().toString().trim();
        text2Qr[14] = tfCountry.getText().toString().trim();
        text2Qr[15] = tfNote.getText().toString().trim();
    }

    private void recoverOldValues() {
        tfName.setText(text2Qr[0]);
        tfFirstName.setText(text2Qr[1]);
        tfBday.setText(text2Qr[2]);
        tfOrg.setText(text2Qr[3]);
        tfPhoto.setText(text2Qr[4]);
        tfWeb.setText(text2Qr[5]);
        tfEmail.setText(text2Qr[6]);
        tfMobil.setText(text2Qr[7]);
        tfTeleWork.setText(text2Qr[8]);
        tfTelePrivate.setText(text2Qr[9]);
        tfStreet.setText(text2Qr[10]);
        tfCity.setText(text2Qr[11]);
        tfState.setText(text2Qr[12]);
        tfPLZ.setText(text2Qr[13]);
        tfCountry.setText(text2Qr[14]);
        tfNote.setText(text2Qr[15]);
    }

    
    public void buildVCardCode() {
        vcardCode = "BEGIN:VCARD\nVERSION:2.1\nN:" + text2Qr[0] + ";" + text2Qr[1] + "\n";
        if (!text2Qr[2].equals("")) {
            vcardCode += "BDAY:" + text2Qr[2] + "\n";
        }
        if (!text2Qr[3].equals("")) {
            vcardCode += "ORG:" + text2Qr[3] + "\n";
        }
        if (!text2Qr[4].equals("")) {
            vcardCode += "PHOTO;VALUE=uri:" + text2Qr[4] + "\n";
        }
        if (!text2Qr[5].equals("")) {
            vcardCode += "URL:" + text2Qr[5] + "\n";
        }
        if (!text2Qr[6].equals("")) {
            vcardCode += "EMAIL;TYPE=INTERNET:" + text2Qr[6] + "\n";
        }
        if (!text2Qr[7].equals("")) {
            vcardCode += "TEL;CELL:" + text2Qr[7] + "\n";
        }
        if (!text2Qr[8].equals("")) {
            vcardCode += "TEL;WORK;VOICE:" + text2Qr[8] + "\n";
        }
        if (!text2Qr[9].equals("")) {
            vcardCode += "TEL;HOME;VOICE:" + text2Qr[9] + "\n";
        }
        if (!text2Qr[10].equals("") || !text2Qr[11].equals("") || !text2Qr[12].equals("") || !text2Qr[13].equals("") || !text2Qr[14].equals("")) {
            vcardCode += "ADR:;;" + text2Qr[10] + ";" + text2Qr[11] + ";" + text2Qr[12] + ";" + text2Qr[13] + ";" + text2Qr[14] + "\n";
        }
        if (!text2Qr[15].equals("")) {
            vcardCode += "NOTE:" + text2Qr[15] + "\n";
        }
        vcardCode += "END:VCARD";
    }

    
    public void openResultActivity() {
        Intent intent = new Intent(this, GeneratorResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("CODE", vcardCode);
        bundle.putInt("FORMAT", format);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}
