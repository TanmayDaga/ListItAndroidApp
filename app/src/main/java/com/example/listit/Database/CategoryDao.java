package com.example.listit.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT name FROM category ORDER BY `order` ASC")
    LiveData<List<String>> loadCategories();

    @Insert
    void insertCategory(CategoryEntry categoryEntry);

    @Update
    void updateCategory(CategoryEntry categoryEntry);

    @Delete
    void deleteCategory(CategoryEntry categoryEntry);

    @Query("SELECT MAX(`order`) from category")
    int getLastColumnOrder();

}
