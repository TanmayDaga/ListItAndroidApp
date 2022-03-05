package com.example.listit.DetailsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.listit.AppExecutors;
import com.example.listit.Database.ListItEntry;
import com.example.listit.MainActivity.AddCategoryDialog;
import com.example.listit.R;
import com.example.listit.Utilities;
import com.example.listit.databinding.ActivityDetailsBinding;
import com.example.listit.Database.AppDataBase;


import java.util.Calendar;
import java.util.Date;


public class DetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private ActivityDetailsBinding mBinding;

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    private DetailsSpinnerAdapter mSpinnerTableAdapter;
    public Date mDateSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);


        setValuesAndListeners();


    }


    private void setValuesAndListeners() {

//        Setting Button Listeners
        mBinding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
            }
        });

        mBinding.datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "Clicked");
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(DetailsActivity.this, DetailsActivity.this, c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH) + 1,
                        c.get(Calendar.DATE));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });


//        Setting Spinners
        mSpinnerTableAdapter = new DetailsSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                Utilities.getCategoryNames(this));
        mBinding.spinnerTable.setAdapter(mSpinnerTableAdapter);
        mBinding.spinnerTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(mSpinnerTableAdapter.ifButtonPos(position)){
                    AddCategoryDialog.newInstance().show(getSupportFragmentManager(),"dialog_Alert");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<String> spinnerPriorityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        spinnerPriorityAdapter.addAll(Utilities.getPriorityList());
        mBinding.prioritySpinnerView.setAdapter(spinnerPriorityAdapter);




    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dateOfMonth) {
        mBinding.dateTextView.setText(Utilities.dateFormatter(year, monthOfYear+1, dateOfMonth));
        mDateSelected = Utilities.intToDate(year, monthOfYear+1, dateOfMonth);
    }


    private void setData() {
        String title = mBinding.titleEditText.getText().toString();
        if (title == null) {
            Toast.makeText(this, "Title Cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }
        String description = mBinding.descriptionEditText.getText().toString();
        int priority = Utilities.getPriorityFromPosition(
                mBinding.prioritySpinnerView.getSelectedItemPosition());


         String listName = mBinding.spinnerTable.getSelectedItem().toString();

        AppExecutors.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                AppDataBase.getInstance(DetailsActivity.this).listItDao().insertListIt(new ListItEntry(
                        title, description, mDateSelected, listName, priority
                ));
                finish();
            }
        });


    }

    @Override
    protected void onDestroy() {

        getSharedPreferences(Utilities.SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSharedPreferences(Utilities.SHARED_PREFERENCES_KEY,Context.MODE_PRIVATE).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(s.equals(Utilities.SHARED_PREFERENCES_CATEGORY_NAMES)){
            mSpinnerTableAdapter.setData(Utilities.getCategoryNames(this));
        }
    }
}