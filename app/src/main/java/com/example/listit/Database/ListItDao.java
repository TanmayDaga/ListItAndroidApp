package com.example.listit.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ListItDao {

    @Query("SELECT * FROM listIt WHERE category = :categoryName AND completed = 0")
    LiveData<List<ListItEntry>> loadListItsOfCategoryUnCompleted(String categoryName);

    @Insert
    void insertListIt(ListItEntry listItEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateListIt(ListItEntry newListItEntry);

    @Delete
    void deleteListIt(ListItEntry ListItEntry);

    @Query("SELECT * FROM listIt WHERE id = :Id")
    ListItEntry loadListItById(int Id);

    @Query("SELECT DISTINCT category from listIt")
    LiveData<List<String>> loadCategoryNames();

    @Query("SELECT * FROM listIt")
    List<ListItEntry> loadAllListIts();

    @Query("UPDATE listIt SET completed = :completed where id = :id")
    void setCompletedById(boolean completed,int id);

    @Query("SELECT * FROM listIt WHERE completed = 1")
    LiveData<List<ListItEntry>> loadAllCompletedLists();



    @Query("DELETE FROM listIt where category = :category")
    void deleteAllByCategory(String category);



}
