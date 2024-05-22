package com.example.secondhandtransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.ReviewAdapter;
import com.example.database.DataBaseHelper;
import com.example.po.Collection;
import com.example.po.Review;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import static com.example.secondhandtransaction.LoginActivity.only_userid;
import static com.example.secondhandtransaction.LoginEmailActivity.only_emailid;

public class DetailInfoActivity extends AppCompatActivity {
    TextView title, description, price, phone;
    ImageView ivCommodity;
    ListView lvReview;
    LinkedList<Review> reviews = new LinkedList<>();
    EditText etComment;
    int position, id;
    byte[] picture;
    String userid;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_info);
        dbHelper = new DataBaseHelper(this, DataBaseHelper.DB_NAME, null, 1);

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

        ivCommodity = findViewById(R.id.iv_commodity);
        title = findViewById(R.id.tv_title);
        description = findViewById(R.id.tv_description);
        price = findViewById(R.id.tv_price);
        phone = findViewById(R.id.tv_phone);
        Bundle b = getIntent().getExtras();
        picture = b.getByteArray("picture");
        id = b.getInt("id");
        Bitmap img = BitmapFactory.decodeByteArray(picture, 0, picture.length);
        ivCommodity.setImageBitmap(img);
        title.setText(b.getString("title"));
        description.setText(b.getString("description"));
        price.setText(String.valueOf(b.getFloat("price")) + "元");
        phone.setText(b.getString("phone"));
        position = b.getInt("position");

        findViewById(R.id.buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailInfoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //收藏
        findViewById(R.id.ib_my_love).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collection collection = new Collection();
                collection.setTitle(title.getText().toString());
                String price1 = price.getText().toString().substring(0, price.getText().toString().length() - 1);
                collection.setPrice(Float.parseFloat(price1));
                collection.setPhone(phone.getText().toString());
                collection.setDescription(description.getText().toString());
                collection.setPicture(picture);
                //String stuId = getIntent().getStringExtra("userId");
                collection.setStuId(userid);
                dbHelper.addMyCollection(collection);
                Toast.makeText(getApplicationContext(), "已添加至我的收藏!", Toast.LENGTH_SHORT).show();
            }
        });
        etComment = findViewById(R.id.et_comment);
        lvReview = findViewById(R.id.list_comment);
        //提交评论点击事件
        Button btnReview = findViewById(R.id.btn_submit);
        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先检查是否为空
                if (CheckInput()) {
                    Review review = new Review();
                    review.setContent(etComment.getText().toString());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
                    //获取当前时间
                    Date date = new Date(System.currentTimeMillis());
                    review.setCurrentTime(simpleDateFormat.format(date));
                    String stuId = getIntent().getStringExtra("stuId");
                    review.setStuId(stuId);
                    review.setPosition(position);
                    dbHelper.addReview(review);
                    //评论置为空
                    etComment.setText("");
                    Toast.makeText(getApplicationContext(), "留言成功!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final ReviewAdapter adapter = new ReviewAdapter(getApplicationContext());
        reviews = dbHelper.readReviews(position);
        adapter.setData(reviews);
        lvReview.setAdapter(adapter);
        addHistory();
    }

    private void addHistory() {
        Collection collection = new Collection();
        collection.setTitle(title.getText().toString());
        String price1 = price.getText().toString().substring(0, price.getText().toString().length() - 1);
        collection.setPrice(Float.parseFloat(price1));
        collection.setPhone(phone.getText().toString());
        collection.setDescription(description.getText().toString());
        collection.setPicture(picture);
        collection.setId(id);
        collection.setStuId(userid);
        dbHelper.addLookHistory(collection);
    }

    public boolean CheckInput() {
        String comment = etComment.getText().toString();
        if (comment.trim().equals("")) {
            Toast.makeText(this, "留言内容不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void quit(View view) {
        this.finish();
    }
}