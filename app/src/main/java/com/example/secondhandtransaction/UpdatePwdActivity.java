package com.example.secondhandtransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database.DataBaseHelper;

import static com.example.secondhandtransaction.LoginActivity.only_userid;
import static com.example.secondhandtransaction.LoginEmailActivity.only_emailid;

public class UpdatePwdActivity extends AppCompatActivity {
    private EditText oldpwd;
    private EditText newpwd;
    private EditText newpwd1;
    private Button change;
    private String oldpass, newpass, newpass1, user;
    String userid;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);

        oldpwd = (EditText) findViewById(R.id.oldpwd);
        newpwd = (EditText) findViewById(R.id.newpwd);
        newpwd1 = (EditText) findViewById(R.id.newpwd1);
        change = (Button) findViewById(R.id.change);
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


        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("用户数据", userid);
                oldpass = oldpwd.getText().toString();
                newpass = newpwd.getText().toString();
                newpass1 = newpwd1.getText().toString();

                boolean flag = true;
                if (oldpass.equals("") || oldpass == null) {
                    Toast.makeText(getApplicationContext(), "请输入旧密码！", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                if (newpass.equals("") || newpass == null) {
                    Toast.makeText(getApplicationContext(), "请输入新密码！", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                if (!newpass.equals(newpass1)) {
                    Toast.makeText(getApplicationContext(), "两次密码不一致！", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                if (flag) {
                    checkpass(oldpass, newpass);
                }

            }
        });

        findViewById(R.id.back1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void checkpass(String oldpass, String newpass) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            String sql = "SELECT * FROM users WHERE userId=? and passWord=?";
            Cursor cursor = db.rawQuery(sql, new String[]{userid, oldpass});
            if (cursor.getCount() == 0) {
                Toast.makeText(getApplicationContext(), "用户旧密码错误！", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues values = new ContentValues();
                values.put("passWord", newpass);
                db.update("users", values, "userId=?", new String[]{userid});
                Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
        }
    }
}
