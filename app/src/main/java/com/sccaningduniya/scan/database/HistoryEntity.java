package com.sccaningduniya.scan.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history_entries")
public class HistoryEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "format")
    private String format;

    @ColumnInfo(name = "information")
    private String information;

    @ColumnInfo(name = "date")
    private String date;

    public HistoryEntity(String format, String information, String date) {
        this.format = format;
        this.information = information;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormat() {
        return format;
    }

    public String getInformation() {
        return information;
    }

    public String getDate() {
        return date;
    }
}
