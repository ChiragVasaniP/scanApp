package com.sccaningduniya.scan.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String COLUMN_SCANNED_ID = "scanned_id";
    private static final String COLUMN_SCANNED_QRCODE = "code";
    private static final String DATABASE_NAME = "SecScanQR.db";
    private static final String TABLE_SCANNED = "scanned";
    private static int DATABASE_VERSION = 1;
    private String CREATE_SCANNED_TABLE;
    private String DROP_SCANNED_TABLE;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, DATABASE_VERSION);
        this.CREATE_SCANNED_TABLE = "CREATE TABLE scanned(scanned_id INTEGER PRIMARY KEY AUTOINCREMENT,code TEXT)";
        this.DROP_SCANNED_TABLE = "DROP TABLE IF EXISTS scanned";
    }

    @Override 
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.CREATE_SCANNED_TABLE);
    }

    @Override 
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(this.DROP_SCANNED_TABLE);
        onCreate(db);
    }

    public Cursor getData() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM scanned ORDER BY scanned_id", null);
        return data;
    }
}
