package com.example.listit.ActivityUncompleted;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.listit.AppExecutors;
import com.example.listit.Database.AppDataBase;
import com.example.listit.Database.ListItEntry;
import com.example.listit.MainActivity.ListItFragmentRecyclerAdapter;
import com.example.listit.R;
import com.example.listit.Utilities;
import com.example.listit.databinding.ActivityUncompletedBinding;

import java.util.List;

public class UncompletedActivity extends AppCompatActivity {

    private ActivityUncompletedBinding mBinding;
    private AppDataBase mDb;
    private UncompletedRecyclerAdapter mRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_uncompleted);
        mDb = AppDataBase.getInstance(this);

        initViews();



    }

    private void initViews(){
        mRecyclerAdapter = new UncompletedRecyclerAdapter(this);
        mBinding.recyclerView.setAdapter(mRecyclerAdapter);

        AppExecutors.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                LiveData<List<ListItEntry>> data = mDb.listItDao().loadAllCompletedLists();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        data.observe(UncompletedActivity.this, new Observer<List<ListItEntry>>() {
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
