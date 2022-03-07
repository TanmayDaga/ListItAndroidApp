package com.example.listit.CompletedActivity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listit.Database.AppDataBase;
import com.example.listit.Database.ListItEntry;
import com.example.listit.R;
import com.example.listit.databinding.ActivityCompletedBinding;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class CompletedActivity extends AppCompatActivity {

    private ActivityCompletedBinding mBinding;
    private AppDataBase mDb;
    private CompletedRecyclerAdapter mRecyclerAdapter;
    private CompletedViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_completed);
        mDb = AppDataBase.getInstance(this);
        mViewModel = ViewModelProviders.of(this).get(CompletedViewModel.class);
        initViews();


    }

    private void initViews() {
        mRecyclerAdapter = new CompletedRecyclerAdapter(this);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mRecyclerAdapter);

        getSupportActionBar().setTitle(R.string.completed_taks);

        mViewModel.getCompletedListIts().observe(this, new Observer<List<ListItEntry>>() {
            @Override
            public void onChanged(List<ListItEntry> entries) {
                mRecyclerAdapter.setData(entries);
                Log.d("Completed", String.valueOf(entries.size()));
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int position = viewHolder.getAdapterPosition();
                List<ListItEntry> tasks = mRecyclerAdapter.getData();
                switch (swipeDir) {
                    case ItemTouchHelper.LEFT:
                        mViewModel.deleteListIts(tasks.get(position));
                    case ItemTouchHelper.RIGHT:
                        mViewModel.setUnCompleted(tasks.get(position).getId());
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView
                    recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(CompletedActivity.this, android.R.color.holo_red_light))
                        .addSwipeLeftLabel("delete")
                        .setSwipeLeftLabelColor(ContextCompat.getColor(CompletedActivity.this, android.R.color.white))
                        .setSwipeLeftActionIconTint(ContextCompat.getColor(CompletedActivity.this, android.R.color.white))
                        .addCornerRadius(TypedValue.COMPLEX_UNIT_DIP, 12)
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_undo_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(CompletedActivity.this, R.color.cyan_200))
                        .addSwipeRightLabel("Move to\n ListIt")
                        .setSwipeRightLabelColor(ContextCompat.getColor(CompletedActivity.this, android.R.color.black))
                        .setSwipeRightActionIconTint(ContextCompat.getColor(CompletedActivity.this, android.R.color.black))
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).

                attachToRecyclerView(mBinding.recyclerView);


    }
}
