package com.example.secondhandtransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.DataBaseHelper;

import static com.example.secondhandtransaction.LoginActivity.only_userid;
import static com.example.secondhandtransaction.LoginEmailActivity.only_emailid;

public class UserMsgActivity extends AppCompatActivity {
    private TextView user_id;
    private TextView username;
    private TextView useremail;
    private TextView useraddress;
    private ImageView img;
    private ImageView usermsgreturn;

    private Button userchange;
    //private Button back;
    private String name, email, address;
    String userid;
    byte[] imagedata;
    Bitmap imagebm;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg);

        user_id = (TextView) findViewById(R.id.showUser);
        username = (TextView) findViewById(R.id.name);
        useremail = (TextView) findViewById(R.id.showemail);
        useraddress = (TextView) findViewById(R.id.address);
        userchange = (Button) findViewById(R.id.changemsg);
        usermsgreturn = (ImageView) findViewById(R.id.usermsg_return);
        usermsgreturn.setOnClickListener(new View.OnClickListener() {
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

        user_id.setText(userid);
        img = (ImageView) findViewById(R.id.show_img);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            String sql = "SELECT * FROM users WHERE userId=?";
            Cursor cursor = db.rawQuery(sql, new String[]{userid});
            if (cursor.getCount() == 0) {
                Toast.makeText(getApplicationContext(), "用户不存在！", Toast.LENGTH_SHORT).show();
            } else {
                if (cursor.moveToFirst()) {

                    try {
                        //if (!imagedata.equals("")){
                        imagedata = cursor.getBlob(5);
                        imagebm = BitmapFactory.decodeByteArray(imagedata, 0, imagedata.length);
                        img.setImageBitmap(imagebm);
                        // }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    name = cursor.getString(cursor.getColumnIndex("name"));
                    email = cursor.getString(cursor.getColumnIndex("email"));
                    address = cursor.getString(cursor.getColumnIndex("address"));
                }

                username.setText(name);
                useremail.setText(email);
                useraddress.setText(address);

            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(getApplicationContext(), "无法显示个人信息", Toast.LENGTH_SHORT).show();
        }

        userchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle2 = new Bundle();
                bundle2.putString("only_email", email);
                bundle2.putString("only_name", name);
                bundle2.putString("only_address", address);
                //bundle2.putString("only_img",img);
                Intent intent = new Intent(UserMsgActivity.this, UpdateUserMsgActivity.class);
                intent.putExtras(bundle2);
                startActivity(intent);
            }
        });

    }

}