package com.sccaningduniya.scan.handlers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.exifinterface.media.ExifInterface;

import com.sccaningduniya.scan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;

@SuppressLint("ALL")

public class ButtonHandler {
    public static void resetScreenInformation(TextView tvInformation, TextView tvFormat, TextView mLabelInformation, TextView mLabelFormat, BottomNavigationView buttonContainer, ImageView codeImage) {
        tvInformation.setText(R.string.default_text_main_activity);
        tvFormat.setText("");
        tvFormat.setVisibility(8);
        mLabelInformation.setVisibility(8);
        mLabelFormat.setVisibility(8);
        buttonContainer.setVisibility(4);
        codeImage.setVisibility(8);
    }

    public static void copyToClipboard(TextView tv, String qrcode, Activity activity) {
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService("clipboard");
        ClipData clip = ClipData.newPlainText(tv.getText(), qrcode);
        clipboard.setPrimaryClip(clip);
        Toast toast = Toast.makeText(activity, activity.getResources().getText(R.string.notice_clipoard), 1);
        toast.setGravity(81, 0, 300);
        toast.show();
    }

    public static void shareTo(String qrcode, Activity activity) {
        Intent sendIntent = new Intent();
        sendIntent.setAction("android.intent.action.SEND");
        sendIntent.putExtra("android.intent.extra.TEXT", qrcode);
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent, activity.getResources().getText(R.string.send_to)));
    }

    
    
    
    public static void openInWeb(String qrcode, Activity activity) {
        String tempUrl;
        char c = 0;
        if (qrcode.equals("")) {
            Toast.makeText(activity.getApplicationContext(), activity.getResources().getText(R.string.error_scan_first), 0).show();
            return;
        }
        try {
            if (qrcode.startsWith("URL:")) {
                qrcode = qrcode.replace("URL:", "");
            }
            Uri uri = Uri.parse(qrcode);
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            activity.startActivity(intent);
        } catch (Exception e) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
            String search_engine = prefs.getString("pref_search_engine", "");
            switch (search_engine.hashCode()) {
                case 49:
                    break;
                case 50:
                    if (search_engine.equals(ExifInterface.GPS_MEASUREMENT_2D)) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 51:
                    if (search_engine.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 52:
                    if (search_engine.equals("4")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 53:
                    if (search_engine.equals("5")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 54:
                    if (search_engine.equals("6")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 55:
                    if (search_engine.equals("7")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 56:
                    if (search_engine.equals("8")) {
                        c = 7;
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
                    tempUrl = "https://www.bing.com/search?q=";
                    break;
                case 1:
                    tempUrl = "https://duckduckgo.com/?q=";
                    break;
                case 2:
                    tempUrl = "https://www.google.com/search?q=";
                    break;
                case 3:
                    tempUrl = "https://www.qwant.com/?q=";
                    break;
                case 4:
                    tempUrl = "https://lite.qwant.com/?q=";
                    break;
                case 5:
                    tempUrl = "https://www.startpage.com/do/dsearch?query=";
                    break;
                case 6:
                    tempUrl = "https://search.yahoo.com/search?p=";
                    break;
                case 7:
                    tempUrl = "https://www.yandex.ru/search/?text=";
                    break;
                default:
                    tempUrl = "https://www.google.com/search?q=";
                    break;
            }
            Uri uri2 = Uri.parse(tempUrl + qrcode);
            Intent intent2 = new Intent("android.intent.action.VIEW", uri2);
            activity.startActivity(intent2);
        }
    }

    
    
    
    
    public static void openInWeb(String qrcode, String format, Activity activity) {
        char c;
        String tempUrl = "";
        boolean z;
        char c2;
        String qrcode2 = qrcode;
        if (qrcode2.equals("")) {
            Toast.makeText(activity.getApplicationContext(), activity.getResources().getText(R.string.error_scan_first), 0).show();
            return;
        }
        try {
            if (qrcode2.startsWith("URL:")) {
                qrcode2 = qrcode2.replace("URL:", "");
            }
            try {
                Uri uri = Uri.parse(qrcode2);
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                activity.startActivity(intent);
            } catch (Exception e) {
                e = e;
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
                String search_engine = prefs.getString("pref_search_engine", "");
                String barcode_engine = prefs.getString("pref_barcode_search_engine", "");
                if (barcode_engine.equals("0")) {
                    switch (search_engine.hashCode()) {
                        case 49:
                            if (search_engine.equals("1")) {
                                c2 = 0;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 50:
                            if (search_engine.equals(ExifInterface.GPS_MEASUREMENT_2D)) {
                                c2 = 1;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 51:
                            if (search_engine.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                                c2 = 2;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 52:
                            if (search_engine.equals("4")) {
                                c2 = 3;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 53:
                            if (search_engine.equals("5")) {
                                c2 = 4;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 54:
                            if (search_engine.equals("6")) {
                                c2 = 5;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 55:
                            if (search_engine.equals("7")) {
                                c2 = 6;
                                break;
                            }
                            c2 = 65535;
                            break;
                        case 56:
                            if (search_engine.equals("8")) {
                                c2 = 7;
                                break;
                            }
                            c2 = 65535;
                            break;
                        default:
                            c2 = 65535;
                            break;
                    }
                    switch (c2) {
                        case 0:
                            tempUrl = "https://www.bing.com/search?q=";
                            break;
                        case 1:
                            tempUrl = "https://duckduckgo.com/?q=";
                            break;
                        case 2:
                            tempUrl = "https://www.google.com/search?q=";
                            break;
                        case 3:
                            tempUrl = "https://www.qwant.com/?q=";
                            break;
                        case 4:
                            tempUrl = "https://lite.qwant.com/?q=";
                            break;
                        case 5:
                            tempUrl = "https://www.startpage.com/do/dsearch?query=";
                            break;
                        case 6:
                            tempUrl = "https://search.yahoo.com/search?p=";
                            break;
                        case 7:
                            tempUrl = "https://www.yandex.ru/search/?text=";
                            break;
                        default:
                            tempUrl = "https://www.google.com/search?q=";
                            break;
                    }
                } else if (format.equals(IntentIntegrator.QR_CODE) || format.equals("AZTEC")) {
                    switch (search_engine.hashCode()) {
                        case 49:
                            if (search_engine.equals("1")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case 50:
                            if (search_engine.equals(ExifInterface.GPS_MEASUREMENT_2D)) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        case 51:
                            if (search_engine.equals(ExifInterface.GPS_MEASUREMENT_3D)) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        case 52:
                            if (search_engine.equals("4")) {
                                c = 3;
                                break;
                            }
                            c = 65535;
                            break;
                        case 53:
                            if (search_engine.equals("5")) {
                                c = 4;
                                break;
                            }
                            c = 65535;
                            break;
                        case 54:
                            if (search_engine.equals("6")) {
                                c = 5;
                                break;
                            }
                            c = 65535;
                            break;
                        case 55:
                            if (search_engine.equals("7")) {
                                c = 6;
                                break;
                            }
                            c = 65535;
                            break;
                        case 56:
                            if (search_engine.equals("8")) {
                                c = 7;
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
                            tempUrl = "https://www.bing.com/search?q=";
                            break;
                        case 1:
                            tempUrl = "https://duckduckgo.com/?q=";
                            break;
                        case 2:
                            tempUrl = "https://www.google.com/search?q=";
                            break;
                        case 3:
                            tempUrl = "https://www.qwant.com/?q=";
                            break;
                        case 4:
                            tempUrl = "https://lite.qwant.com/?q=";
                            break;
                        case 5:
                            tempUrl = "https://www.startpage.com/do/dsearch?query=";
                            break;
                        case 6:
                            tempUrl = "https://search.yahoo.com/search?p=";
                            break;
                        case 7:
                            tempUrl = "https://www.yandex.ru/search/?text=";
                            break;
                        default:
                            tempUrl = "https://www.google.com/search?q=";
                            break;
                    }
                } else {
                    int hashCode = barcode_engine.hashCode();
                    if (hashCode != 49) {
                        if (hashCode == 50 && barcode_engine.equals(ExifInterface.GPS_MEASUREMENT_2D)) {
                            z = true;
                            if (z) {
                                tempUrl = "https://world.openfoodfacts.org/cgi/search.pl?search_terms=";
                            } else if (z) {
                                tempUrl = "https://www.codecheck.info/product.search?q=";
                            } else {
                                tempUrl = "https://www.google.com/search?q=";
                            }
                        }
                        z = true;
                        if (z) {
                        }
                    } else {
                        if (barcode_engine.equals("1")) {
                            z = false;
                            if (z) {
                            }
                        }
                        z = true;
                        if (z) {
                        }
                    }
                }
                Uri uri2 = Uri.parse(tempUrl + qrcode2);
                Intent intent2 = new Intent("android.intent.action.VIEW", uri2);
                activity.startActivity(intent2);
            }
        } catch (Exception e2) {

        }
    }

    public static void createContact(String qrcode, Activity activity) {
        Intent intent = new Intent("android.intent.action.INSERT");
        intent.setType("vnd.android.cursor.dir/raw_contact");
        String[] information = qrcode.split("\\r?\\n");
        String notes = "";
        for (int i = 0; i < information.length; i++) {
            if (information[i].contains("N:")) {
                String name = information[i].split(":")[1].replace(";", " ");
                new GeneralHandler(activity);
                intent.putExtra("name", GeneralHandler.reverseName(name));
            } else if (information[i].contains("BDAY")) {
                notes = notes + "\n" + information[i];
            } else if (information[i].contains("ORG")) {
                intent.putExtra("company", information[i].split(":")[1]);
            } else if (information[i].contains("URL")) {
                notes = notes + "\n" + information[i].split(":")[1];
            } else if (information[i].contains("EMAIL")) {
                intent.putExtra("email", information[i].split(":")[1]);
            } else if (information[i].contains("TEL")) {
                String[] separeted = information[i].split(":");
                if (separeted[0].contains("CELL")) {
                    intent.putExtra("phone_type", 2);
                    intent.putExtra("secondary_phone", separeted[1]);
                } else if (separeted[0].contains("WORK")) {
                    intent.putExtra("phone_type", 3);
                    intent.putExtra("tertiary_phone", separeted[1]);
                } else if (separeted[0].contains("HOME")) {
                    intent.putExtra("phone_type", 1);
                    intent.putExtra("phone", separeted[1]);
                } else if (separeted[0].contains("VOICE")) {
                    intent.putExtra("phone_type", 7);
                    intent.putExtra("phone", separeted[1]);
                }
            } else if (information[i].contains("ADR")) {
                String[] adr = information[i].split(":")[1].split(";");
                notes = notes + "\n" + adr[2] + "\n" + adr[3] + "\n" + adr[4] + "\n" + adr[5] + "\n" + adr[6];
            } else if (information[i].contains("NOTE")) {
                notes = notes + "\n" + information[i];
            }
            intent.putExtra("notes", notes);
        }
        activity.startActivity(intent);
    }
}
