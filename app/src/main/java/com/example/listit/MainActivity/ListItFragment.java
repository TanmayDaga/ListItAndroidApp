package com.example.listit.MainActivity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listit.AppExecutors;
import com.example.listit.Database.AppDataBase;
import com.example.listit.Database.ListItEntry;
import com.example.listit.R;
import com.example.listit.databinding.FragmentListItBinding;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ListItFragment extends Fragment {

    private static String CATEGORY;
    private ListItFragmentRecyclerAdapter mRecyclerAdapter;
    FragmentListItBinding mBinding;
    private AppDataBase mDb;
    private static final String TAG = ListItFragment.class.getSimpleName();
    LiveData<List<ListItEntry>> mEntries;
    MainActivityViewModel mViewModel;

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
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        initViews();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        mEntries = mViewModel.getFragmentData(CATEGORY);

        mEntries.observe(getViewLifecycleOwner(), new Observer<List<ListItEntry>>() {
            @Override
            public void onChanged(List<ListItEntry> entries) {
                mRecyclerAdapter.setData(entries);
                Log.d(TAG, String.valueOf(entries.size()));
                if (entries.size() == 0) {
                    mBinding.recyclerView.setVisibility(View.GONE);
                    mBinding.emptyView.setVisibility(View.VISIBLE);
                } else {
                    mBinding.recyclerView.setVisibility(View.VISIBLE);
                    mBinding.emptyView.setVisibility(View.GONE);
                }
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


                int position = viewHolder.getAdapterPosition();
                List<ListItEntry> tasks = mRecyclerAdapter.getTasks();
                switch (swipeDir){
                    case ItemTouchHelper.LEFT:
                        mViewModel.deleteListIts(tasks.get(position));
                        break;
                    case ItemTouchHelper.RIGHT:
                        mViewModel.setCompleted(tasks.get(position).getId());
                }


            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.holo_red_light))
                        .addSwipeLeftLabel("delete")
                        .setSwipeLeftLabelColor(ContextCompat.getColor(getContext(), android.R.color.white))
                        .setSwipeLeftActionIconTint(ContextCompat.getColor(getContext(), android.R.color.white))
                        .addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 12)
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_done_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.cyan_200))
                        .addSwipeRightLabel("Completed")
                        .setSwipeRightLabelColor(ContextCompat.getColor(getContext(), android.R.color.black))
                        .setSwipeRightActionIconTint(ContextCompat.getColor(getContext(), android.R.color.black))
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(mBinding.recyclerView);


    }


}