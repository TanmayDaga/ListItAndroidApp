package com.example.listit.DetailsActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.listit.AppExecutors;
import com.example.listit.Database.AppDataBase;
import com.example.listit.Database.ListItEntry;
import com.example.listit.R;
import com.example.listit.Utilities;
import com.example.listit.databinding.ActivityDetailsBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ActivityDetailsBinding mBinding;

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    private DetailsSpinnerAdapter mSpinnerTableAdapter;
    public Date mDateSelected;
    private DetailsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);
        mViewModel = ViewModelProviders.of(this).get(DetailsViewModel.class);

        setValuesAndListeners();

        setSpinnerData();
    }

    private void setSpinnerData() {

        mViewModel.getCategoriesLiveData().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> categoryEntries) {
                if(categoryEntries == null || categoryEntries.size() == 0){
                    categoryEntries = new ArrayList<>();
                    categoryEntries.add("My tasks");
                }
                mSpinnerTableAdapter.setData(categoryEntries);
            }
        });


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
        List<String> arr = new ArrayList<>();
        arr.add("My tasks");
        mSpinnerTableAdapter = new DetailsSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                arr);
        mBinding.spinnerTable.setAdapter(mSpinnerTableAdapter);
        mBinding.spinnerTable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (mSpinnerTableAdapter.ifButtonPos(position)) {
                    Utilities.showCategoryDialog(DetailsActivity.this, DetailsActivity.this);
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
        mBinding.dateTextView.setText(Utilities.dateFormatter(year, monthOfYear + 1, dateOfMonth));
        mDateSelected = Utilities.intToDate(year, monthOfYear + 1, dateOfMonth);
    }


    private void setData() {
        String title = mBinding.titleEditText.getText().toString();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Title Cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }
        String description = mBinding.descriptionEditText.getText().toString();
        int priority = Utilities.getPriorityFromPosition(
                mBinding.prioritySpinnerView.getSelectedItemPosition());


        String listName = mBinding.spinnerTable.getSelectedItem().toString();

        mViewModel.insertListIt(title,description,mDateSelected,listName,priority);


    }


}