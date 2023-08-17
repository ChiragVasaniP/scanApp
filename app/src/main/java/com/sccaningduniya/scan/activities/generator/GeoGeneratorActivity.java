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

import com.sccaningduniya.scan.R;
import com.sccaningduniya.scan.activities.MainActivity;
import com.sccaningduniya.scan.handlers.GeneralHandler;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.integration.android.IntentIntegrator;

@SuppressLint("ALL")

public class GeoGeneratorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String STATE_EAST = "east";
    private static final String STATE_LATITUDE = MainActivity.class.getName();
    private static final String STATE_LONGTITUDE = "";
    private static final String STATE_NORTH = "north";
    final Activity activity = this;
    Button btnGenerate;
    CheckBox cbLatitude;
    CheckBox cbLongtitude;
    int format;
    String geo;
    String latitude;
    String longtitude;
    MultiFormatWriter multiFormatWriter;
    EditText tfLatitude;
    EditText tfLongtitude;
    Boolean north = true;
    Boolean east = true;

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GeneralHandler generalHandler = new GeneralHandler(this);
        generalHandler.loadTheme();
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_geo_generator);
        FrameLayout nativeAd = (FrameLayout) findViewById(R.id.nativeFrameGeo);

        tfLatitude = (EditText) findViewById(R.id.tfLatitude);
        tfLongtitude = (EditText) findViewById(R.id.tfLongtitude);
        cbLatitude = (CheckBox) findViewById(R.id.cbLatitude);
        cbLongtitude = (CheckBox) findViewById(R.id.cbLongtitude);
        Button button = (Button) findViewById(R.id.btnGenerateGeo);
        btnGenerate = button;
        button.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                latitude = tfLatitude.getText().toString().trim();
                longtitude = tfLongtitude.getText().toString().trim();
                if (latitude.equals("") || longtitude.equals("")) {
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.error_geo_first), 0).show();
                    return;
                }
                multiFormatWriter = new MultiFormatWriter();
                try {
                    if (north.booleanValue() && east.booleanValue()) {
                        geo = "geo:" + latitude + "," + longtitude;
                    } else if (!east.booleanValue() && north.booleanValue()) {
                        geo = "geo:" + latitude + ",-" + longtitude;
                    } else if (!north.booleanValue() && east.booleanValue()) {
                        geo = "geo:-" + latitude + "," + longtitude;
                    } else {
                        geo = "geo:-" + latitude + ",-" + longtitude;
                    }
                    openResultActivity();
                } catch (Exception e) {
                    Toast.makeText(activity.getApplicationContext(), getResources().getText(R.string.error_generate), 1).show();
                }
            }
        });
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.formats_geo_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(17367049);
        spinner.setAdapter((SpinnerAdapter) adapter);
        if (savedInstanceState != null) {
            String str = (String) savedInstanceState.get(STATE_LATITUDE);
            latitude = str;
            tfLatitude.setText(str);
            String str2 = (String) savedInstanceState.get("");
            longtitude = str2;
            tfLongtitude.setText(str2);
            Boolean bool = (Boolean) savedInstanceState.get(STATE_NORTH);
            north = bool;
            if (!bool.booleanValue()) {
                cbLatitude.setChecked(false);
            }
            Boolean bool2 = (Boolean) savedInstanceState.get(STATE_EAST);
            east = bool2;
            if (!bool2.booleanValue()) {
                cbLongtitude.setChecked(false);
            }
        }
    }

    @Override
    
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(STATE_LATITUDE, latitude);
        savedInstanceState.putString("", longtitude);
        savedInstanceState.putBoolean(STATE_NORTH, north.booleanValue());
        savedInstanceState.putBoolean(STATE_EAST, east.booleanValue());
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

    public void onClickCheckboxes(View v) {
        if (cbLatitude.isChecked() && cbLongtitude.isChecked()) {
            north = true;
            east = true;
        } else if (!cbLatitude.isChecked() && cbLongtitude.isChecked()) {
            north = false;
            east = true;
        } else if (cbLatitude.isChecked() && !cbLongtitude.isChecked()) {
            north = true;
            east = false;
        } else {
            north = false;
            east = false;
        }
    }

    
    public void openResultActivity() {
        Intent intent = new Intent(this, GeneratorResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("CODE", geo);
        bundle.putInt("FORMAT", format);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}
