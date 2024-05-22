package com.example.secondhandtransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.adapter.MyCollectionAdapter;
import com.example.database.DataBaseHelper;
import com.example.po.Collection;
import com.example.po.Commodity;

import java.util.ArrayList;
import java.util.List;

import static com.example.secondhandtransaction.LoginActivity.only_userid;
import static com.example.secondhandtransaction.LoginEmailActivity.only_emailid;

public class MyLookActivity extends AppCompatActivity {
    ListView lvMyCollection;
    List<Collection> myCollections = new ArrayList<>();
    String userid;
    DataBaseHelper dbHelper;
    MyCollectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_look);

        //返回
        findViewById(R.id.back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dbHelper = new DataBaseHelper(getApplicationContext(), DataBaseHelper.DB_NAME, null, 1);
        userid = only_userid;
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
        lvMyCollection = findViewById(R.id.lv_my_collection);

        adapter = new MyCollectionAdapter(getApplicationContext());
        lvMyCollection.setAdapter(adapter);

        lvMyCollection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Collection commodity = myCollections.get(position);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("position", position);
                bundle1.putByteArray("picture", commodity.getPicture());
                bundle1.putString("title", commodity.getTitle());
                bundle1.putString("description", commodity.getDescription());
                bundle1.putFloat("price", commodity.getPrice());
                bundle1.putString("phone", commodity.getPhone());
                bundle1.putString("stuId", only_userid);
                bundle1.putInt("id", commodity.getId());
                Intent intent = new Intent(MyLookActivity.this, DetailInfoActivity.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });

        //设置长按删除事件
        lvMyCollection.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyLookActivity.this);
                builder.setTitle("提示:").setMessage("确定删除此浏览记录吗?").setIcon(R.drawable.ic_baseline_info_24).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Collection collection = (Collection) adapter.getItem(position);
                        //删除收藏商品项
                        dbHelper.deleteLookHistory(collection.getId());
                        Toast.makeText(MyLookActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                        myCollections.remove(collection);
                        adapter.notifyDataSetChanged();
                    }
                }).show();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        myCollections = dbHelper.getLookHistory(userid);
        adapter.setData(myCollections);
    }
}