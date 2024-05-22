package com.example.secondhandtransaction;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.MyCommodityAdapter;
import com.example.database.DataBaseHelper;
import com.example.po.Commodity;

import java.util.ArrayList;
import java.util.List;

import static com.example.secondhandtransaction.LoginActivity.only_userid;
import static com.example.secondhandtransaction.LoginEmailActivity.only_emailid;

public class MyPublishActivity extends AppCompatActivity {
    ListView lvMyCommodity;
    List<Commodity> myCommodities = new ArrayList<>();
    DataBaseHelper dbHelper;
    MyCommodityAdapter adapter;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish);

        userid = only_userid;
        dbHelper = new DataBaseHelper(getApplicationContext(), DataBaseHelper.DB_NAME, null, 1);
        if (only_userid.equals("")) {
            SQLiteDatabase db1 = dbHelper.getReadableDatabase();
            String sql1 = "SELECT * FROM users WHERE email=?";
            Cursor cursor1 = db1.rawQuery(sql1, new String[]{only_emailid});
            if (cursor1.getCount() == 0) {
                Toast.makeText(getApplicationContext(), "用户不存在！", Toast.LENGTH_SHORT).show();
            } else {
                if (cursor1.moveToFirst()) {
                    try {
                        userid = cursor1.getString(cursor1.getColumnIndex("userId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        lvMyCommodity = findViewById(R.id.lv_my_commodity);
        adapter = new MyCommodityAdapter(getApplicationContext());
        myCommodities = dbHelper.readMyCommodities(userid);
        System.out.println(userid);
        adapter.setData(myCommodities);
        lvMyCommodity.setAdapter(adapter);
        //长按点击事件
        lvMyCommodity.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //注意,这里的content不能写getApplicationContent();
                AlertDialog.Builder builder = new AlertDialog.Builder(MyPublishActivity.this);
                builder.setTitle("提示:").setMessage("确认删除此商品项吗?").setIcon(R.drawable.ic_baseline_info_24).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //根据商品名称,商品描述和价格执行删除操作
                        Commodity commodity = (Commodity) adapter.getItem(position);
                        dbHelper.deleteMyCommodity(commodity.getTitle(), commodity.getDescription(), commodity.getPrice());
                        Toast.makeText(MyPublishActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                    }
                }).show();
                return false;
            }
        });
        //刷新
        ImageView imageView = findViewById(R.id.fabu_refresh);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new MyCommodityAdapter(MyPublishActivity.this);
                myCommodities = dbHelper.readMyCommodities(userid);
                adapter.setData(myCommodities);
                lvMyCommodity.setAdapter(adapter);
            }
        });
    }

    public void quit(View view) {
        MyPublishActivity.this.finish();
    }
}