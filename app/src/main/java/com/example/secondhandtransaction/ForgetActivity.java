package com.example.secondhandtransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database.DataBaseHelper;
import com.example.mail.SendmailUtil;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {
    DataBaseHelper databaseHelper;
    private EditText ForgetId;
    private EditText ForgetEmail;
    private Button RetrievePwd;
    String email1 = null;
    String pwd = null;
    String mailbook = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        databaseHelper = new DataBaseHelper(this, DataBaseHelper.DB_NAME, null, 1);
        ForgetId = (EditText) findViewById(R.id.forget_id);
        ForgetEmail = (EditText) findViewById(R.id.forget_email);
        RetrievePwd = (Button) findViewById(R.id.retrievepwd);
        RetrievePwd.setOnClickListener(this);
        context = this;
    }

    public void quit(View view) {
        ForgetActivity.this.finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.retrievepwd:

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            SQLiteDatabase db;
                            db = databaseHelper.getReadableDatabase();
                            Cursor cursor = db.rawQuery("select * from users where userId=?", new String[]{ForgetId.getText().toString()});
                            if (cursor.getCount() == 0) {
                            } else {
                                cursor.moveToFirst();
                                pwd = cursor.getString(1);
                                mailbook = (String) cursor.getString(3);
                                System.out.println(pwd);
                            }
                            while (cursor.moveToNext()) {
                            }
                            cursor.close();
                            db.close();
                            if (ForgetId.getText().toString().equals("")) {
                                ForgetActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            email1 = (String) ForgetEmail.getText().toString();
                            if (email1.equals("") || email1 == null) {
                                ForgetActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "请输入邮箱", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                if (mailbook.equals(email1)) {
                                    new SendmailUtil(mailbook).sendTextEmail("尊敬的用户:" + ForgetId.getText().toString() + ",  您的密码为:" + pwd + ",  请谨记！");
                                    ForgetActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "密码已发送至您的邮箱", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    System.out.println(mailbook);
                                } else {
                                    System.out.println("账号与绑定邮箱信息不符");

                                    ForgetActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "账号与绑定邮箱信息不符", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
        }
    }
}