package com.example.listit.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.listit.AppExecutors;
import com.example.listit.Database.AppDataBase;
import com.example.listit.Database.CategoryEntry;
import com.example.listit.DetailsActivity.DetailsActivity;
import com.example.listit.R;
import com.example.listit.CompletedActivity.CompletedActivity;
import com.example.listit.Utilities;
import com.example.listit.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{


    private ActivityMainBinding mBinding;
    private PagerAdapter mPagerAdapter;

    private MainActivityViewModel mViewModel;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), getLifecycle());
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);


//        If app opened for first time
        if(Utilities.isFirstTime(this)){
            mViewModel.AddMyTasks();

        }

        initViews();

        setmPagerAdapterCategories();



    }

    private void setmPagerAdapterCategories(){
            AppExecutors.getsInstance().diskIo().execute(new Runnable() {
                @Override
                public void run() {
                    mViewModel.getCategories().observe(MainActivity.this, new Observer<List<String>>() {
                        @Override
                        public void onChanged(List<String> stringList) {
                            mPagerAdapter.setLists(stringList);
                        }
                    });
                }
            });


    }

    private void initViews() {



//        Pager and Tab Layout
        mBinding.viewPager.setAdapter(mPagerAdapter);
        new TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager,
                (tab, position) -> tab.setText(mPagerAdapter.getTitle(position))).attach();

        mBinding.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

//        Floating Action Button
        mBinding.fab.setOnClickListener(
                view -> startActivity(new Intent(MainActivity.this, DetailsActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.action_delete_all) {
            mViewModel.deleteAllByCategory(mPagerAdapter.getCategoryByPosition(
                    mBinding.tabLayout.getSelectedTabPosition()
            ));
            return true;


        } else if (itemId == R.id.action_add_category) {
            Utilities.showCategoryDialog(this, this);
            return true;

        } else if (itemId == R.id.action_jump_activity_completed) {
            startActivity(new Intent(this, CompletedActivity.class));
            return true;

        } else return false;

    }

}