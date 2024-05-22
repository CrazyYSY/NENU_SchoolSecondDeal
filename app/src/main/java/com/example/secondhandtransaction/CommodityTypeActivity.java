package com.example.secondhandtransaction;

import android.content.Intent;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.AllCommodityAdapter;
import com.example.database.DataBaseHelper;
import com.example.po.Commodity;

import java.util.LinkedList;
import java.util.List;

public class CommodityTypeActivity extends AppCompatActivity {
    TextView tvCommodityType;
    ListView lvCommodityType;
    ListView lvAllCommodity;

    List<Commodity> commodities = new LinkedList<>();
    String stuNum =null;
    DataBaseHelper dbHelper;
    AllCommodityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_type);
        lvAllCommodity=findViewById(R.id.list_commodity);
        tvCommodityType = findViewById(R.id.tv_type);
        lvCommodityType = findViewById(R.id.list_commodity);
        dbHelper = new DataBaseHelper(getApplicationContext(),DataBaseHelper.DB_NAME,null,1);
        adapter = new AllCommodityAdapter(getApplicationContext());

        //返回
        ImageView imageView = findViewById(R.id.type_return);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //为每一个item设置点击事件
        lvAllCommodity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Commodity commodity = (Commodity) lvAllCommodity.getAdapter().getItem(position);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("position",position);
                bundle1.putByteArray("picture",commodity.getPicture());
                bundle1.putString("title",commodity.getTitle());
                bundle1.putString("description",commodity.getDescription());
                bundle1.putFloat("price",commodity.getPrice());
                bundle1.putString("phone",commodity.getPhone());
                bundle1.putString("stuId",stuNum);
                Intent intent = new Intent(CommodityTypeActivity.this, DetailInfoActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });

        //根据不同的状态显示不同的界面
        int status = this.getIntent().getIntExtra("status",0);
        if(status == 1) {
            tvCommodityType.setText("生活用品");
        }else if(status == 2) {
            tvCommodityType.setText("电子游戏");
        }else if(status == 3) {
            tvCommodityType.setText("数码外设");
        }else if(status == 4) {
            tvCommodityType.setText("体育用品");
        }
        //根据不同类别显示不同的商品信息
        commodities = dbHelper.readCommodityType(tvCommodityType.getText().toString());
        adapter.setData(commodities);
        lvCommodityType.setAdapter(adapter);
    }
}