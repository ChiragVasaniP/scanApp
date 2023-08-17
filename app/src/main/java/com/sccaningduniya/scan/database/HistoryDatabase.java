package com.sccaningduniya.scan.database;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Database(entities = HistoryEntity.class, version = 1)
public abstract class HistoryDatabase extends RoomDatabase {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    private static DatabaseHelper historyDatabaseHelper;
    private static HistoryDatabase instance;
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            new MigrateOldDbAsyncTask().execute();
        }
    };

    public static synchronized HistoryDatabase getInstance(Context context) {
        if (instance == null) {
            historyDatabaseHelper = new DatabaseHelper(context);
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    HistoryDatabase.class,
                    "history_database"
            ).fallbackToDestructiveMigration().addCallback(roomCallback).build();
        }
        return instance;
    }

    public abstract HistoryDao historyDao();

    private static class MigrateOldDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private HistoryDao historyDao;

        private MigrateOldDbAsyncTask() {
            historyDao = instance.historyDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor data = historyDatabaseHelper.getData();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            while (data.moveToNext()) {
                historyDao.insert(
                        new HistoryEntity("UNKNOWN", data.getString(1), dateFormat.format((Date) timestamp))
                );
            }
            return null;
        }
    }
}
