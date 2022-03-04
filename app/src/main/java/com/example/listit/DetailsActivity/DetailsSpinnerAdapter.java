package com.example.listit.DetailsActivity;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;

//TODO

public class DetailsSpinnerAdapter extends ArrayAdapter<String> {

    LayoutInflater mLayoutInflater;
    public DetailsSpinnerAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        mLayoutInflater = LayoutInflater.from(context);

    }
}
