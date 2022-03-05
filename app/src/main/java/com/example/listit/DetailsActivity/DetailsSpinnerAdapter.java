package com.example.listit.DetailsActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.listit.R;

import java.util.List;

//TODO

public class DetailsSpinnerAdapter extends ArrayAdapter<String> {

    LayoutInflater mLayoutInflater;
    List<String> mData;

    public DetailsSpinnerAdapter(@NonNull Context context, int resource, List<String> categoryNames) {
        super(context, resource,categoryNames);
        this.mData = categoryNames;
        mLayoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (ifButtonPos(position)) {
            return mLayoutInflater.inflate(R.layout.activity_details_spinner_item_button, parent, false);
        }
        View view = mLayoutInflater.inflate(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, parent, false);
        ((TextView) view.findViewById(android.R.id.text1)).setText(mData.get(position));
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (ifButtonPos(position)) {
            return mLayoutInflater.inflate(R.layout.activity_details_spinner_item_button, parent, false);
        }
        View view = mLayoutInflater.inflate(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, parent, false);
        ((TextView) view.findViewById(android.R.id.text1)).setText(mData.get(position));
        return view;



    }

    @Override
    public int getCount() {
        if (mData == null) return 1;
        return mData.size() + 1;
    }

    public boolean ifButtonPos(int pos) {
        if (mData == null) return true;
        else return pos == mData.size();
    }

    public void setData(List<String> stringList){
        mData = stringList;
        notifyDataSetChanged();
    }

}
