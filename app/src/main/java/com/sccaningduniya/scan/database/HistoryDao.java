package com.sccaningduniya.scan.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    void insert(HistoryEntity historyEntity);

    @Delete
    void delete(HistoryEntity historyEntity);

    @Query("DELETE FROM history_entries")
    void deleteAllInHistory();

    @Query("SELECT * FROM history_entries")
    LiveData<List<HistoryEntity>> getAllHistoryEntries();
}
