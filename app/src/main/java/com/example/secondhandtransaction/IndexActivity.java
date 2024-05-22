package com.example.secondhandtransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import com.example.fragmentclass.DetailFragment;
import com.example.fragmentclass.MyFragment;
import com.example.fragmentclass.PublishFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class IndexActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


        if(savedInstanceState==null){
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.fr1,new DetailFragment()).
                    commit();
        }
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment=null;

                switch (item.getItemId()){
                    case R.id.home1:
                        fragment=new DetailFragment();
                        break;
                    case R.id.fabu1:
                        fragment=new PublishFragment();
                        break;
                    case R.id.my1:
                        fragment=new MyFragment();
                        break;
                }
                getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.fr1,fragment).

                        commit();
                return  true;
            }
        });

    }
}