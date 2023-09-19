package com.example.benchsquatdl2.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.example.benchsquatdl2.R;
import com.example.benchsquatdl2.model.ItemViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FragmentMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    ArrayList<Integer> arrayList;
    private ItemViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mainactivity);

        bottomNavigationView = findViewById(R.id.topNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.start_squad);

        model = new ViewModelProvider(this).get(ItemViewModel.class);

        // Create the observer which updates the UI.
        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.

            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.

        model.getCurrentName().observe(this, nameObserver);


        receiveData();





    }

    private void receiveData() {

        try{
            // Recieve data
            Intent intent = getIntent();
            String exercise = intent.getExtras().getString("exercise");


            if(!exercise.isEmpty()){
                getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).commit();
                secondFragment.receiver(exercise);
            }



        }catch(Exception error1) {

            error1.printStackTrace();
        }


    }


    FirstFragment firstFragment = new FirstFragment();
    SecondFragment secondFragment = new SecondFragment();
    ThirdFragment thirdFragment = new ThirdFragment();
    benchFragment xx = new benchFragment();





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.start_squad:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).commit();
                return true;

            case R.id.training:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, secondFragment).commit();
                return true;

            case R.id.feed:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, thirdFragment).commit();
                return true;

            case R.id.squad_card:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, firstFragment).commit();

                return true;

            case R.id.bench_card:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, xx).commit();
                return true;

        }


        return false;
    }
}
