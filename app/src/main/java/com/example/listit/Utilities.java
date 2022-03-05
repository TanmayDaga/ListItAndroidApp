package com.example.listit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Utilities {

    public static final int PRIORITY_HIGH = 3;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_NO = 0;

    public static final String PRIORITY_HIGH_STRING = "Priority High";
    public static final String PRIORITY_MEDIUM_STRING = "Priority Medium";
    public static final String PRIORITY_LOW_STRING = "Priority Low";
    public static final String PRIORITY_NO_STRING = "No Priority";



    public static final String SHARED_PREFERENCES_CATEGORY_NAMES = "categoryNamesString";
    public static final String SHARED_PREFERENCES_KEY = "com.example.listit.sharedpreferences";

    public static final int WORK_DONE = 89;
    public static final int WORK_NOT_DONE = 90;

    public static final String DEFAULT_CATEGORY_NAME = "My Tasks";



    public static Drawable getListItemLayoutBg(Context context, int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                return ContextCompat.getDrawable(context, R.drawable.list_item_bg_priority_high);
            case PRIORITY_MEDIUM:
                return ContextCompat.getDrawable(context, R.drawable.list_item_bg_priority_medium);
            case PRIORITY_LOW:
                return ContextCompat.getDrawable(context, R.drawable.list_item_bg_priority_low);
            default:
                return ContextCompat.getDrawable(context,R.drawable.list_item_bg_priority_no);
        }
    }

    public static int getColorFromPriority(Context context,int priority){
        switch (priority) {
            case PRIORITY_HIGH:
                return ContextCompat.getColor(context, R.color.priority_high);
            case PRIORITY_MEDIUM:
                return ContextCompat.getColor(context, R.color.priority_medium);
            case PRIORITY_LOW:
                return ContextCompat.getColor(context, R.color.priority_low);
            default:
                return ContextCompat.getColor(context,R.color.primaryDarkColor);
        }
    }

    public static int getPriorityFromPosition(int position) {
        switch (position) {

            case 1:
                return PRIORITY_LOW;
            case 2:
                return PRIORITY_MEDIUM;
            case 3:
                return PRIORITY_HIGH;
            default:
                return PRIORITY_NO;
        }
    }



    public static String dateFormatter(int year, int month, int day) {
        return String.format(Locale.getDefault(),"%d/%d/%d", day, month, year);
    }
    public static  String dateFormatter(Date date){
        if(!(date == null)){
            return new SimpleDateFormat("dd/MM/yyyy").format(date);
        }
        else return "Congrats \uD83D\uDE03 no due date";
    }

    public static Date intToDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year,month-1,day);

        return new Date(c.getTimeInMillis());
    }

    public static void storeCategoryNames(Context context, List<String> listNames) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if(listNames == null){
            listNames = new ArrayList<>();
        }
        if (listNames.size() == 0) {
            listNames.add(DEFAULT_CATEGORY_NAME);
        }
        Set<String> set = new HashSet<>(listNames);
        editor.putStringSet(SHARED_PREFERENCES_CATEGORY_NAMES, set);
        editor.apply();

    }

    public static List<String> getCategoryNames(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        List<String> mainList = new ArrayList<String>();
        mainList.addAll(sp.getStringSet(SHARED_PREFERENCES_CATEGORY_NAMES,null));

        return mainList;
    }

    public static String[] getPriorityList() {
        return new String[]{PRIORITY_NO_STRING,PRIORITY_LOW_STRING,PRIORITY_MEDIUM_STRING,PRIORITY_HIGH_STRING};
    }

    public static void addCategory(Context context, String categoryName) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE);
        Set<String> set = new HashSet<>(
                sp.getStringSet(SHARED_PREFERENCES_CATEGORY_NAMES, null)
        );

        set.add(categoryName);
        SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(SHARED_PREFERENCES_CATEGORY_NAMES, set);
        editor.apply();


    }


}
