package com.example.secondhandtransaction;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
//    String TABLENAME = "iteminfo";
//    Intent intent;
//    byte[] imagedata;
//    Bitmap imagebm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
//        DatabaseHelper database = new DatabaseHelper(this);
//        final SQLiteDatabase db = database.getWritableDatabase();
//        ListView listView = (ListView) findViewById(R.id.listView);
//        Map<String, Object> item;  // 列表项内容用Map存储
//        final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>(); // 列表
//        Cursor cursor = db.query(TABLENAME, null, null, null, null, null, null, null); // 数据库查询
//        if (cursor.moveToFirst()) {
//            while (!cursor.isAfterLast()) {
//                item = new HashMap<String, Object>();  // 为列表项赋值
//                item.put("id", cursor.getInt(0));
//                item.put("userid", cursor.getString(1));
//                item.put("title", cursor.getString(2));
//                item.put("kind", cursor.getString(3));
//                item.put("info", cursor.getString(4));
//                item.put("price", cursor.getString(5));
//                imagedata = cursor.getBlob(6);
//                imagebm = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length);
//                item.put("image", imagebm);
//                cursor.moveToNext();
//                data.add(item); // 加入到列表中
//            }
//        }
//        // 使用SimpleAdapter布局listview
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, data, R.layout.listitem, new String[]{"image", "title", "kind", "info", "price"},
//                new int[]{R.id.item_image, R.id.title, R.id.kind, R.id.info, R.id.price});
//        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Object data, String textRepresentation) {
//                if (view instanceof ImageView && data instanceof Bitmap) {
//                    ImageView iv = (ImageView) view;
//                    iv.setImageBitmap((Bitmap) data);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
//        listView.setAdapter(simpleAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                intent = new Intent(MainActivity.this, ReleaseActivity.class);
//                intent.putExtra("id", data.get(position).get("id").toString()); // 获取该列表项的key为id的键值，即商品的id，将其储存在Bundle传递给打开的页面
//                startActivity(intent);
//            }
//        });
//    }
}