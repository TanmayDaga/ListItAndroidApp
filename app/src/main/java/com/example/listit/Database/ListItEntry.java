package com.example.listit.Database;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "listIt")
public class ListItEntry {


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private Date dueDate;


    private String category;
    private int priority;
    private boolean completed;
    public ListItEntry(String title, String description, Date dueDate, String category, int priority) {

        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = (category == null) ? "My Tasks" : category;
        this.priority = priority;
        this.completed = false;


    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setCompleted(boolean completed){
        this.completed = completed;
    }

    public boolean getCompleted(){
        return completed;
    }
}
