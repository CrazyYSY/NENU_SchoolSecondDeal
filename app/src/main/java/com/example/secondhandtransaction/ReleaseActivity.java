package com.example.secondhandtransaction;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.DataBaseHelper;
import com.example.po.Commodity;


import java.io.ByteArrayOutputStream;

//将用户唯一ID导入
import static com.example.secondhandtransaction.LoginActivity.only_userid;
import static com.example.secondhandtransaction.LoginEmailActivity.only_emailid;

//发布商品页面
public class ReleaseActivity extends AppCompatActivity {
    TextView tvStuId;
    ImageButton ivPhoto;
    EditText etTitle, etPrice, etPhone, etDescription;
    Spinner spType;
    String userid;
    private Uri uri;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
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

        ivPhoto = findViewById(R.id.iv_photo);
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
            }
        });
        etTitle = findViewById(R.id.et_title);
        etPrice = findViewById(R.id.et_price);
        etPhone = findViewById(R.id.et_phone);
        etDescription = findViewById(R.id.et_description);
        spType = findViewById(R.id.spn_type);
        Button btnPublish = findViewById(R.id.btn_publish);
        //发布按钮点击事件
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先检查合法性
                if (CheckInput()) {
                    Commodity commodity = new Commodity();
                    //把图片先转化成bitmap格式
                    BitmapDrawable drawable = (BitmapDrawable) ivPhoto.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    //二进制数组输出流
                    ByteArrayOutputStream byStream = new ByteArrayOutputStream();
                    //将图片压缩成质量为100的PNG格式图片
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byStream);

                    int options = 100;
                    while (byStream.toByteArray().length / 1024 > 320) {
                        byStream.reset();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, options, byStream);
                        options -= 10;
                    }

                    //把输出流转换为二进制数组
                    byte[] byteArray = byStream.toByteArray();
                    commodity.setPicture(byteArray);
                    commodity.setTitle(etTitle.getText().toString());
                    commodity.setCategory(spType.getSelectedItem().toString());
                    commodity.setPrice(Float.parseFloat(etPrice.getText().toString()));
                    commodity.setPhone(etPhone.getText().toString());
                    commodity.setDescription(etDescription.getText().toString());
                    commodity.setStuId(userid);
                    if (dbHelper.AddCommodity(commodity)) {
                        Toast.makeText(getApplicationContext(), "商品信息发布成功!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "商品信息发布失败!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //从相册返回的数据
            if (data != null) {
                //得到图片的全路径
                uri = data.getData();
                ivPhoto.setImageURI(uri);
            }
        }
    }

    /**
     * 检查输入是否合法
     */
    public boolean CheckInput() {
        String title = etTitle.getText().toString();
        String price = etPrice.getText().toString();
        String type = spType.getSelectedItem().toString();
        String phone = etPhone.getText().toString();
        String description = etDescription.getText().toString();
        if (title.trim().equals("")) {
            Toast.makeText(this, "商品标题不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (price.trim().equals("")) {
            Toast.makeText(this, "商品价格不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (type.trim().equals("请选择类别")) {
            Toast.makeText(this, "商品类别未选择!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.trim().equals("")) {
            Toast.makeText(this, "手机号码不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (description.trim().equals("")) {
            Toast.makeText(this, "商品描述不能为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (uri == null) {
            Toast.makeText(this, "请选择发布图片!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void quit(View view) {
        this.finish();
    }
}