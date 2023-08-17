package com.sccaningduniya.scan.repository;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sccaningduniya.scan.database.HistoryEntity;

import java.util.List;


public class HistoryViewModel extends AndroidViewModel {
    private LiveData<List<HistoryEntity>> allItemsInHistory;
    private HistoryRepository historyRepository;

    public HistoryViewModel(Application application) {
        super(application);
        HistoryRepository historyRepository = new HistoryRepository(application);
        this.historyRepository = historyRepository;
        this.allItemsInHistory = historyRepository.getAllItemsInHistory();
    }

    public void insert(HistoryEntity historyEntity) {
        this.historyRepository.insert(historyEntity);
    }

    public void delete(HistoryEntity historyEntity) {
        this.historyRepository.delete(historyEntity);
    }

    public void deleteAllInHistory() {
        this.historyRepository.deleteAllInHistory();
    }

    public LiveData<List<HistoryEntity>> getAllItemsInHistory() {
        return this.allItemsInHistory;
    }
}
