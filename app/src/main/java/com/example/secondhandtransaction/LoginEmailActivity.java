package com.example.secondhandtransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.database.DataBaseHelper;
import com.example.mail.SendmailUtil;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener {
    DataBaseHelper dbHelper;
    private EditText LoginTwoEmail;
    private EditText YzCode;
    private Button SendCode;
    private Button LoginTwo;
    private String mailbook1 = null;
    public static String only_emailid = null;

    public int verificationCode = (int) ((Math.random() * 9 + 1) * 100000);
    String smstext = String.valueOf(verificationCode);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        dbHelper = new DataBaseHelper(this, DataBaseHelper.DB_NAME, null, 1);
        LoginTwoEmail = (EditText) findViewById(R.id.logintwo_email);
        YzCode = (EditText) findViewById(R.id.yz_code);
        SendCode = (Button) findViewById(R.id.send_code);
        LoginTwo = (Button) findViewById(R.id.logintwo);
        LoginTwo.setOnClickListener(this);
        SendCode.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_code:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            SQLiteDatabase db;
                            db = dbHelper.getReadableDatabase();
                            Cursor cursor = db.rawQuery("select * from users where email=?", new String[]{LoginTwoEmail.getText().toString()});
                            if (cursor.getCount() == 0) {
                                LoginEmailActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "该邮箱未注册", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                mailbook1 = (String) LoginTwoEmail.getText().toString();
                                //String verificationCode3=Integer.toString(verificationCode);
                                new SendmailUtil(mailbook1).sendTextEmail("您正在使用邮箱登录" + ",验证码为:" + smstext + ",三分钟内有效");
                                LoginEmailActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "验证码已发至邮箱", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                System.out.println(smstext);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            case R.id.logintwo:
                if (verificationCode == 0) {
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (YzCode.getText().toString().equals(smstext)) {
                        Intent intent = new Intent(LoginEmailActivity.this, IndexActivity.class);
                        startActivity(intent);
                    }
//                    else {
//                        Toast.makeText(this,"验证码错误",Toast.LENGTH_SHORT).show();
//                    }
                }


        }
        only_emailid = LoginTwoEmail.getText().toString();

    }

}