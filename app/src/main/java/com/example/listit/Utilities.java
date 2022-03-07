package com.example.listit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.listit.Database.AppDataBase;
import com.example.listit.Database.CategoryDao;
import com.example.listit.Database.CategoryEntry;

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


    public static final String SHARED_PREFERENCES_LIST_IT =  "listItPreferences";
    public static final String SHARED_PREFERENCES_FIRST_TIME_OPEN_KEY = "firstTime";



    public static final int WORK_DONE = 89;
    public static final int WORK_NOT_DONE = 90;

    public static final String DEFAULT_CATEGORY_NAME = "My Tasks";


    public static int getColorFromPriority(Context context, int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                return ContextCompat.getColor(context, R.color.priority_high);
            case PRIORITY_MEDIUM:
                return ContextCompat.getColor(context, R.color.priority_medium);
            case PRIORITY_LOW:
                return ContextCompat.getColor(context, R.color.priority_low);
            default:
                return ContextCompat.getColor(context, R.color.primaryDarkColor);
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
        return String.format(Locale.getDefault(), "%d/%d/%d", day, month, year);
    }

    public static String dateFormatter(Date date) {
        if (!(date == null)) {
            return new SimpleDateFormat("dd/MM/yyyy").format(date);
        } else return "Congrats \uD83D\uDE03 no due date";
    }

    public static Date intToDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day);

        return new Date(c.getTimeInMillis());
    }




    public static String[] getPriorityList() {
        return new String[]{PRIORITY_NO_STRING, PRIORITY_LOW_STRING, PRIORITY_MEDIUM_STRING, PRIORITY_HIGH_STRING};
    }

    public static void addCategory(Context context, String categoryName) {
        AppExecutors.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                int order = AppDataBase.getInstance().categoryDao().getLastColumnOrder();
                AppDataBase.getInstance().categoryDao().insertCategory(
                        new CategoryEntry(categoryName,order+1)
                );

            }
        });
        Toast.makeText(context,"Added scussefully",Toast.LENGTH_LONG).show();



    }

    public static void showCategoryDialog(Activity activity, Context mContext) {
        getCategoryDialog(activity,mContext).show();
    }

    private static Dialog getCategoryDialog(Activity activity, Context mContext) {
        Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setContentView(R.layout.add_category_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        ((Button) dialog.findViewById(R.id.categoryDialogButtonOk)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getsInstance().diskIo().execute(new Runnable() {
                    @Override
                    public void run() {

                        String name = ((TextView) dialog.findViewById(R.id.addCategoryEditText)).getText().toString();
                        if (TextUtils.isEmpty(name)) {
                            activity
                                    .runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mContext, "Empty category cannot be added", Toast.LENGTH_LONG).show();
                                        }
                                    });

                            return;
                        }

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addCategory(mContext,name);
                            }
                        });
                        dialog.dismiss();
                    }
                });
            }
        });

        ((Button) dialog.findViewById(R.id.categoryDialogButtonCancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static boolean isFirstTime(Context context){
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERENCES_LIST_IT,Context.MODE_PRIVATE);
        boolean firstTime = sp.getBoolean(SHARED_PREFERENCES_FIRST_TIME_OPEN_KEY,true);


//        First Time Opened
        if(firstTime){
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(SHARED_PREFERENCES_FIRST_TIME_OPEN_KEY,false);
            editor.apply();
        }
        return firstTime;


    }


}
