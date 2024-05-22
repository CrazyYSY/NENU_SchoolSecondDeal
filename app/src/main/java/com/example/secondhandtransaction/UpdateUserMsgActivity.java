package com.example.secondhandtransaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.DataBaseHelper;

import static com.example.secondhandtransaction.LoginActivity.only_userid;
import static com.example.secondhandtransaction.LoginEmailActivity.only_emailid;

import java.io.ByteArrayOutputStream;

public class UpdateUserMsgActivity extends AppCompatActivity {
    private TextView user_id;
    private EditText username;
    private EditText useremail;
    private EditText useraddress;
    private Button usersave;
    private ImageView back;
    private ImageView updateimg;
    private byte[] image;
    private String post_name = null, post_email = null, post_address = null;
    String userid;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_msg);
        user_id = (TextView) findViewById(R.id.showUser);
        username = (EditText) findViewById(R.id.name);
        useremail = (EditText) findViewById(R.id.updatemsg_email);
        useraddress = (EditText) findViewById(R.id.address);
        usersave = (Button) findViewById(R.id.save);
        updateimg = (ImageView) findViewById(R.id.update_img);
        back = (ImageView) findViewById(R.id.back);
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

        user_id.setText(userid);


        updateimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UpdateUserMsgActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpdateUserMsgActivity.this, new
                            String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    //打开系统相册
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }

            }
        });


        Bundle b = getIntent().getExtras();
        if (!b.equals("")) {
            useremail.setText(b.getString("only_email"));
        }
        if (!b.equals("")) {
            username.setText(b.getString("only_name"));
        }
        if (!b.equals("")) {
            useraddress.setText(b.getString("only_address"));
        }

        usersave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {//账号userId，密码passWord，姓名name，邮箱qq,地址address
                post_name = username.getText().toString();
                post_email = useremail.getText().toString();
                post_address = useraddress.getText().toString();


                ContentValues values = new ContentValues();
                if (!post_name.equals("")) {
                    values.put("name", post_name);
                }
                if (!post_email.equals("")) {
                    values.put("email", post_email);
                }
                if (!post_address.equals("")) {
                    values.put("address", post_address);
                }
                values.put("image", image);
                saveValues(values);
                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(UpdateUserMsgActivity.this,UserMsgActivity.class);
//                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveValues(ContentValues values) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.update("users", values, "userId=?", new String[]{userid});
        db.close();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }
    }

    //加载图片
    private void showImage(String imaePath) {
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        image = baos.toByteArray();
        updateimg.setImageBitmap(bm);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


}