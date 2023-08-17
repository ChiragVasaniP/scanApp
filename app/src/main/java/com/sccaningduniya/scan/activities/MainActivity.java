package com.sccaningduniya.scan.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.sccaningduniya.scan.R;
import com.sccaningduniya.scan.activities.generator.GenerateActivity;
import com.sccaningduniya.scan.handlers.GeneralHandler;

@SuppressWarnings("ALL")

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private GeneralHandler generalHandler;
    private CardView generateCard;
    private CardView historyCard;
    private CardView scanCard;

    @Override
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         generalHandler = new GeneralHandler(this);
        generalHandler.loadTheme();
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main);

        FrameLayout nativeFrame = (FrameLayout) findViewById(R.id.nativeFrameHome);

        scanCard = (CardView) findViewById(R.id.scan_card);
        generateCard = (CardView) findViewById(R.id.generate_card);
        historyCard = (CardView) findViewById(R.id.history_card);
        scanCard.setOnClickListener(this);
        generateCard.setOnClickListener(this);
        historyCard.setOnClickListener(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean auto_scan = Boolean.valueOf(prefs.getBoolean("pref_start_auto_scan", false));
        if (auto_scan.booleanValue()) {
            startActivity(new Intent(this, ScannerActivity.class));
        }
    }

    @Override 
    public void onClick(View v) {
        int id2 = v.getId();
        if (id2 == R.id.generate_card) {
            startActivity(new Intent(MainActivity.this, GenerateActivity.class));

        } else if (id2 == R.id.history_card) {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));

        } else if (id2 == R.id.scan_card) {
            startActivity(new Intent(MainActivity.this, ScannerActivity.class));

        }
    }

    public void StartDocScan(View view) {
        AppScanActivity.Companion.start(MainActivity.this);

    }
}
