package com.example.listit.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class CategoryEntry {

    @NonNull
    @PrimaryKey
    private String  name;
    private int order;

    public CategoryEntry(String name, int order) {
        this.name = name;
        this.order = order;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


}
