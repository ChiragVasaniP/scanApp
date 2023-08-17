package com.sccaningduniya.scan.activities.generator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.sccaningduniya.scan.R;
import com.sccaningduniya.scan.handlers.GeneralHandler;


public class GenerateActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView barcodeCard;
    private CardView contactCard;
    private GeneralHandler generalHandler;
    private CardView geoCard;
    private CardView textCard;
    private CardView wifiCard;

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        generalHandler = new GeneralHandler(this);
        generalHandler.loadTheme();
        setContentView(R.layout.activity_generate);
        barcodeCard = (CardView) findViewById(R.id.barcode_card);
        textCard = (CardView) findViewById(R.id.text_card);
        geoCard = (CardView) findViewById(R.id.geo_card);
        contactCard = (CardView) findViewById(R.id.contact_card);
        wifiCard = (CardView) findViewById(R.id.wifi_card);
        FrameLayout nativeAd = (FrameLayout) findViewById(R.id.nativeFrameGen);
        barcodeCard.setOnClickListener(this);
        textCard.setOnClickListener(this);
        geoCard.setOnClickListener(this);
        contactCard.setOnClickListener(this);
        wifiCard.setOnClickListener(this);
    }

    @Override
    
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override 
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.barcode_card :
                startActivity(new Intent(GenerateActivity.this, BarcodeGenerateActivity.class));

                return;
            case R.id.contact_card :
                startActivity(new Intent(GenerateActivity.this, VCardGeneratorActivity.class));

                return;
            case R.id.geo_card :
                startActivity(new Intent(GenerateActivity.this, GeoGeneratorActivity.class));

                return;
            case R.id.text_card :
                startActivity(new Intent(GenerateActivity.this, TextGeneratorActivity.class));

                return;
            case R.id.wifi_card :
                startActivity(new Intent(GenerateActivity.this, WifiGeneratorActivity.class));

                return;
            default:
                return;
        }
    }

    public void goBack(View view) {
        super.onBackPressed();
    }
}
