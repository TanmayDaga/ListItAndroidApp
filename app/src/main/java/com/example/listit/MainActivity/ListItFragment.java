package com.example.listit.MainActivity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listit.AppExecutors;
import com.example.listit.Database.AppDataBase;
import com.example.listit.Database.ListItEntry;
import com.example.listit.R;
import com.example.listit.databinding.FragmentListItBinding;

import java.util.List;

public class ListItFragment extends Fragment {

    private static String CATEGORY;
    private ListItFragmentRecyclerAdapter mRecyclerAdapter;
    FragmentListItBinding mBinding;
    private AppDataBase mDb;
    private static final String TAG = ListItFragment.class.getSimpleName();
    LiveData<List<ListItEntry>> mEntries;

    public ListItFragment() {
        super();
    }

    public ListItFragment(String category) {
        super();
        CATEGORY = category;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDb = AppDataBase.getInstance(getContext());
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_it, container, false);
        initViews();


        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        AppExecutors.getsInstance().diskIo().execute(new Runnable() {
            @Override
            public void run() {
                mEntries = mDb.listItDao().loadListItsOfCategoryUnCompleted(CATEGORY);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getView() != null) {
                            mEntries.observe(getViewLifecycleOwner(), new Observer<List<ListItEntry>>() {
                                @Override
                                public void onChanged(List<ListItEntry> listItEntries) {
                                    mRecyclerAdapter.setData(listItEntries);
                                    Log.d(TAG, String.valueOf(listItEntries.size()));
                                    if (listItEntries.size() == 0) {
                                        mBinding.recyclerView.setVisibility(View.GONE);
                                        mBinding.emptyView.setVisibility(View.VISIBLE);
                                    } else {
                                        mBinding.recyclerView.setVisibility(View.VISIBLE);
                                        mBinding.emptyView.setVisibility(View.GONE);
                                    }

                                }
                            });
                        }
                    }
                });


            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void initViews() {
        // Inflate the layout for this fragment

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerAdapter = new ListItFragmentRecyclerAdapter(getContext());
        mBinding.recyclerView.setAdapter(mRecyclerAdapter);

/*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }



            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {


                AppExecutors.getsInstance().diskIo().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<ListItEntry> tasks = mRecyclerAdapter.getTasks();
                        mDb.listItDao().deleteListIt(tasks.get(position));


                    }
                });
            }
        }).attachToRecyclerView(mBinding.recyclerView);


    }






}