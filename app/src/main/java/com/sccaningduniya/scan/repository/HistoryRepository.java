package com.sccaningduniya.scan.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.sccaningduniya.scan.database.HistoryDao;
import com.sccaningduniya.scan.database.HistoryDatabase;
import com.sccaningduniya.scan.database.HistoryEntity;

import java.util.List;


public class HistoryRepository {
    private LiveData<List<HistoryEntity>> allItemsInHistory;
    private HistoryDao historyDao;

    public HistoryRepository(Application application) {
        HistoryDatabase database = HistoryDatabase.getInstance(application);
        HistoryDao historyDao = database.historyDao();
        this.historyDao = historyDao;
        this.allItemsInHistory = historyDao.getAllHistoryEntries();
    }

    public void insert(HistoryEntity historyEntity) {
        new InsertInHistoryAsyncTask(this.historyDao).execute(historyEntity);
    }

    public void delete(HistoryEntity historyEntity) {
        new DeleteInHistoryAsyncTask(this.historyDao).execute(historyEntity);
    }

    public void deleteAllInHistory() {
        new DeleteAllInHistoryAsyncTask(this.historyDao).execute(new Void[0]);
    }

    public LiveData<List<HistoryEntity>> getAllItemsInHistory() {
        return this.allItemsInHistory;
    }

    
    private static class InsertInHistoryAsyncTask extends AsyncTask<HistoryEntity, Void, Void> {
        private HistoryDao historyDao;

        private InsertInHistoryAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        
        @Override 
        public Void doInBackground(HistoryEntity... historyEntities) {
            this.historyDao.insert(historyEntities[0]);
            return null;
        }
    }

    
    private static class DeleteInHistoryAsyncTask extends AsyncTask<HistoryEntity, Void, Void> {
        private HistoryDao historyDao;

        private DeleteInHistoryAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        
        @Override 
        public Void doInBackground(HistoryEntity... historyEntities) {
            this.historyDao.delete(historyEntities[0]);
            return null;
        }
    }

    
    private static class DeleteAllInHistoryAsyncTask extends AsyncTask<Void, Void, Void> {
        private HistoryDao historyDao;

        private DeleteAllInHistoryAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        
        @Override 
        public Void doInBackground(Void... voids) {
            this.historyDao.deleteAllInHistory();
            return null;
        }
    }
}
