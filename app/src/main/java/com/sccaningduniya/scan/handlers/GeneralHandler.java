package com.sccaningduniya.scan.handlers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sccaningduniya.scan.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;

@SuppressLint("ALL")

public class GeneralHandler {
    private Activity activity;

    public GeneralHandler(Activity activity) {
        this.activity = activity;
    }

    public static String reverseName(String name) {
        String name2 = name.trim();
        StringBuilder reversedNameBuilder = new StringBuilder();
        StringBuilder subNameBuilder = new StringBuilder();
        for (int i = 0; i < name2.length(); i++) {
            char currentChar = name2.charAt(i);
            if (currentChar != ' ' && currentChar != '-') {
                subNameBuilder.append(currentChar);
            } else {
                reversedNameBuilder.insert(0, currentChar + subNameBuilder.toString());
                subNameBuilder.setLength(0);
            }
        }
        return reversedNameBuilder.insert(0, subNameBuilder.toString()).toString();
    }

    public void loadTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.activity);
        String history_setting = prefs.getString("pref_day_night_mode", "");
        if (history_setting.equals("1")) {
            this.activity.setTheme(R.style.darktheme);
        }
    }

    public BarcodeFormat idToBarcodeFormat(int id2) {
        switch (id2) {
            case 1:
                BarcodeFormat format = BarcodeFormat.CODABAR;
                return format;
            case 2:
                BarcodeFormat format2 = BarcodeFormat.CODE_128;
                return format2;
            case 3:
                BarcodeFormat format3 = BarcodeFormat.CODE_39;
                return format3;
            case 4:
                BarcodeFormat format4 = BarcodeFormat.EAN_13;
                return format4;
            case 5:
                BarcodeFormat format5 = BarcodeFormat.EAN_8;
                return format5;
            case 6:
                BarcodeFormat format6 = BarcodeFormat.ITF;
                return format6;
            case 7:
                BarcodeFormat format7 = BarcodeFormat.PDF_417;
                return format7;
            case 8:
                BarcodeFormat format8 = BarcodeFormat.UPC_A;
                return format8;
            case 9:
                BarcodeFormat format9 = BarcodeFormat.QR_CODE;
                return format9;
            case 10:
                BarcodeFormat format10 = BarcodeFormat.AZTEC;
                return format10;
            default:
                BarcodeFormat format11 = BarcodeFormat.CODABAR;
                return format11;
        }
    }

    
    public int StringToBarcodeId(String format) {
        char c;
        switch (format.hashCode()) {
            case -84093723:
                if (format.equals(IntentIntegrator.CODE_128)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 72827:
                if (format.equals(IntentIntegrator.ITF)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 160877:
                if (format.equals(IntentIntegrator.PDF_417)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 62792985:
                if (format.equals("AZTEC")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 65737323:
                if (format.equals(IntentIntegrator.EAN_8)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 80949962:
                if (format.equals(IntentIntegrator.UPC_A)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1310753099:
                if (format.equals(IntentIntegrator.QR_CODE)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1659855352:
                if (format.equals(IntentIntegrator.CODE_39)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1993202587:
                if (format.equals("CODBAR")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 2037856847:
                if (format.equals(IntentIntegrator.EAN_13)) {
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
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
            case 7:
                return 8;
            case '\b':
                return 9;
            case '\t':
                return 10;
            default:
                return 9;
        }
    }

    
    public BarcodeFormat StringToBarcodeFormat(String stringFormat) {
        char c;
        switch (stringFormat.hashCode()) {
            case -84093723:
                if (stringFormat.equals(IntentIntegrator.CODE_128)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 72827:
                if (stringFormat.equals(IntentIntegrator.ITF)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 160877:
                if (stringFormat.equals(IntentIntegrator.PDF_417)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 62792985:
                if (stringFormat.equals("AZTEC")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 65737323:
                if (stringFormat.equals(IntentIntegrator.EAN_8)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 80949962:
                if (stringFormat.equals(IntentIntegrator.UPC_A)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 1310753099:
                if (stringFormat.equals(IntentIntegrator.QR_CODE)) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1659855352:
                if (stringFormat.equals(IntentIntegrator.CODE_39)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1993202587:
                if (stringFormat.equals("CODBAR")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 2037856847:
                if (stringFormat.equals(IntentIntegrator.EAN_13)) {
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
                BarcodeFormat format = BarcodeFormat.CODABAR;
                return format;
            case 1:
                BarcodeFormat format2 = BarcodeFormat.CODE_128;
                return format2;
            case 2:
                BarcodeFormat format3 = BarcodeFormat.CODE_39;
                return format3;
            case 3:
                BarcodeFormat format4 = BarcodeFormat.EAN_13;
                return format4;
            case 4:
                BarcodeFormat format5 = BarcodeFormat.EAN_8;
                return format5;
            case 5:
                BarcodeFormat format6 = BarcodeFormat.ITF;
                return format6;
            case 6:
                BarcodeFormat format7 = BarcodeFormat.PDF_417;
                return format7;
            case 7:
                BarcodeFormat format8 = BarcodeFormat.UPC_A;
                return format8;
            case '\b':
                BarcodeFormat format9 = BarcodeFormat.QR_CODE;
                return format9;
            case '\t':
                BarcodeFormat format10 = BarcodeFormat.AZTEC;
                return format10;
            default:
                BarcodeFormat format11 = BarcodeFormat.CODABAR;
                return format11;
        }
    }
}
