package com.example.secondhandtransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.DataBaseHelper;
import com.example.mail.SPUtils;
import com.example.mail.ToolUtils;

public class LoginActivity extends AppCompatActivity {
    private TextView RegisterPage;
    private EditText UserName;
    private EditText PassWord;
    private Button Btn_Login;
    private TextView ForgetPwd;
    private RelativeLayout toLoginEmail;
    String username = null;
    String password = null;
    public static String only_userid;
    private boolean isSave;
    DataBaseHelper dbHelper;

    private ImageView ivunameClear, ivpwdClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UserName = (EditText) findViewById(R.id.username);
        PassWord = (EditText) findViewById(R.id.password);
        Btn_Login = (Button) findViewById(R.id.login);
        ForgetPwd = (TextView) findViewById(R.id.forgetpwd);
        RegisterPage = (TextView) findViewById(R.id.registerpage);
        toLoginEmail = (RelativeLayout) findViewById(R.id.tologin_email);
        ivpwdClear = (ImageView) findViewById(R.id.iv_pwdClear);
        ivunameClear = (ImageView) findViewById(R.id.iv_unameClear);
        toLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginEmailActivity.class);
                startActivity(intent);
            }
        });
        RegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
        ForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetActivity.class);
                startActivity(intent);
            }
        });
        Btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获得前端Ed控件的值
                username = UserName.getText().toString();
                password = PassWord.getText().toString();
                if (username.equals("") || username == null) {
                    Toast.makeText(getApplicationContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
                } else if (password.equals("") || password == null) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (username.equals("admin") && password.equals("admin")) {
                    Intent intent1 = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent1);
                    Toast.makeText(getApplicationContext(), "正在登录管理员", Toast.LENGTH_SHORT).show();
                } else {
                    checkUser(username, password);
                }

            }
        });

        addClearListener(UserName, ivunameClear);
        addClearListener(PassWord, ivpwdClear);

        // 查询SharedPreferences 是否有保存账号密码
        String tempUserName = SPUtils.getString(LoginActivity.this, "userName", "");
        UserName.setText(tempUserName);
        PassWord.setText(SPUtils.getString(LoginActivity.this, "password", ""));

        //记住密码
        CheckBox checkBox = findViewById(R.id.save_pwd);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSave = true;
                } else {
                    isSave = false;
                }
            }
        });
        if (!ToolUtils.isNullOrEmpty(tempUserName)) {
            checkBox.setChecked(true);
        }

        dbHelper = new DataBaseHelper(this, DataBaseHelper.DB_NAME, null, 1);
    }

    private void checkUser(String user, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            String sql = "SELECT * FROM users WHERE userId=? and password=?";
            Cursor cursor = db.rawQuery(sql, new String[]{username, password});
            if (cursor.getCount() == 0) {
                Toast.makeText(getApplicationContext(), "账号密码错误", Toast.LENGTH_SHORT).show();
            } else {

                // 密码保存到本地 SharedPreferences中
                if (isSave) {
                    SPUtils.saveString(LoginActivity.this, "userName", username);
                    SPUtils.saveString(LoginActivity.this, "password", password);
                } else {
                    SPUtils.removeAllShared(LoginActivity.this);
                }
                only_userid = user;
                Intent intent = new Intent(LoginActivity.this, IndexActivity.class);
                startActivity(intent);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void addClearListener(final EditText et, final ImageView iv) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //如果有输入内容长度大于0那么显示clear按钮
                String str = s + "";
                if (s.length() > 0) {
                    iv.setVisibility(View.VISIBLE);
                } else {
                    iv.setVisibility(View.INVISIBLE);
                }
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        only_userid = "";
    }
}