package com.example.secondhandtransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.AllCommodityAdapter;
import com.example.database.DataBaseHelper;
import com.example.po.Commodity;

import java.util.ArrayList;
import java.util.List;

import static com.example.secondhandtransaction.LoginActivity.only_userid;

public class SearchActivity extends AppCompatActivity {
    ListView SearchCommodity;
    List<Commodity> searchCommodities = new ArrayList<>();
    EditText SearchInput;
    TextView tv_search;

    DataBaseHelper dbHelper;
    AllCommodityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SearchInput = (EditText) findViewById(R.id.search_input);
        tv_search = (TextView) findViewById(R.id.search_tv);
        SearchCommodity = findViewById(R.id.list_search);
        dbHelper = new DataBaseHelper(this, DataBaseHelper.DB_NAME, null, 1);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = SearchInput.getText().toString();
                adapter = new AllCommodityAdapter(getApplicationContext());
                searchCommodities = dbHelper.searchCommodities(title);
                adapter.setData(searchCommodities);
                SearchCommodity.setAdapter(adapter);
            }
        });


        SearchCommodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Commodity commodity = (Commodity) SearchCommodity.getAdapter().getItem(position);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", position);
                bundle1.putByteArray("picture", commodity.getPicture());
                bundle1.putString("title", commodity.getTitle());
                bundle1.putString("description", commodity.getDescription());
                bundle1.putFloat("price", commodity.getPrice());
                bundle1.putString("phone", commodity.getPhone());
                bundle1.putString("stuId", only_userid);
                bundle1.putInt("id",commodity.getId());
                Intent intent = new Intent(SearchActivity.this, DetailInfoActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });

    }

    public void quit(View view) {
        SearchActivity.this.finish();
    }
}