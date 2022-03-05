package com.example.listit.ActivityCompleted;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.listit.AppExecutors;
import com.example.listit.Database.AppDataBase;
import com.example.listit.Database.ListItEntry;
import com.example.listit.R;
import com.example.listit.databinding.ActivityCompletedBinding;


import java.util.List;

public class CompletedActivity extends AppCompatActivity {

    private ActivityCompletedBinding mBinding;
    private AppDataBase mDb;
    private CompletedRecyclerAdapter mRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_completed);
        mDb = AppDataBase.getInstance(this);

        initViews();



    }

    private void initViews(){
        mRecyclerAdapter = new CompletedRecyclerAdapter(this);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mRecyclerAdapter);

        getSupportActionBar().setTitle(R.string.completed_taks);
        AppExecutors.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                LiveData<List<ListItEntry>> data = mDb.listItDao().loadAllCompletedLists();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        data.observe(CompletedActivity.this, new Observer<List<ListItEntry>>() {
                            @Override
                            public void onChanged(List<ListItEntry> listItEntries) {
                                mRecyclerAdapter.setData(listItEntries);
                            }
                        });
                    }
                });
            }
        });

    }
}
