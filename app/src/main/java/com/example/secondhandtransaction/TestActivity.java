package com.example.secondhandtransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.adapter.AllCommodityAdapter;
import com.example.database.DataBaseHelper;
import com.example.po.Commodity;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    ListView lvAllCommodity;
    List<Commodity> allCommodities = new ArrayList<>();

    DataBaseHelper dbHelper;
    AllCommodityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        lvAllCommodity = findViewById(R.id.lv_all_commodity);
        dbHelper = new DataBaseHelper(getApplicationContext(), DataBaseHelper.DB_NAME, null, 1);
        adapter = new AllCommodityAdapter(getApplicationContext());
        allCommodities = dbHelper.readAllCommodities();
        adapter.setData(allCommodities);
        lvAllCommodity.setAdapter(adapter);
        final Bundle bundle = this.getIntent().getExtras();
        //刷新界面
        ImageView tvRefresh = findViewById(R.id.re);
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allCommodities = dbHelper.readAllCommodities();
                adapter.setData(allCommodities);
                lvAllCommodity.setAdapter(adapter);
            }
        });
    }

    public void quit(View view) {
        TestActivity.this.finish();
    }
}