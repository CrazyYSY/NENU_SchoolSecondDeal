package com.example.secondhandtransaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.database.DataBaseHelper;

public class AdminActivity extends AppCompatActivity  implements View.OnClickListener {

    DataBaseHelper databaseHelper;
    private Button mBtnQuery;
    private Button mBtnDelete;
    private Button mBtnUpdate;
    private Button mBtnDelete2;
    private EditText mEtUser;
    private EditText mEtPasswd;
    private TextView mTvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //初始化控件，设置按钮点击事件
        databaseHelper = new DataBaseHelper(this, DataBaseHelper.DB_NAME, null, 1);
        mBtnQuery=(Button)findViewById(R.id.query);
        mBtnDelete=(Button)findViewById(R.id.delete);
        mBtnUpdate=(Button)findViewById(R.id.bu_update);
        mBtnDelete2=(Button)findViewById(R.id.bu_delete);
        mEtUser=(EditText)findViewById(R.id.ed_1);
        mEtPasswd=(EditText)findViewById(R.id.ed_2);
        mTvShow=(TextView)findViewById(R.id.show);
        mBtnQuery.setOnClickListener(this);//查询
        mBtnDelete2.setOnClickListener(this);//删除单个用户
        mBtnDelete.setOnClickListener(this);//删除全部用户
        mBtnUpdate.setOnClickListener(this);//更新
    }

    @Override
    public void onClick(View v) {
        String user;
        String passwd;
        SQLiteDatabase db;
        ContentValues values;
        switch (v.getId()){
            case R.id.query://查询数据
                db=databaseHelper.getReadableDatabase();
                Cursor cursor =db.query("users",null,null,null,null,null,null);
                if(cursor.getCount()==0){
                    mTvShow.setText("");
                    Toast.makeText(this,"没有用户数据",Toast.LENGTH_SHORT).show();
                }else {
                    cursor.moveToFirst();
                    mTvShow.setText("账号:"+cursor.getString(0)+"\n"+"密码:"+cursor.getString(1));

                }while(cursor.moveToNext()){//循环获取数据，并全部显示
                mTvShow.append("\n"+"账号:"+cursor.getString(0)+"\n"+"密码:"+cursor.getString(1)+"\n");
                }
                cursor.close();
                db.close();
                break;
            case R.id.delete://删除information数据库全部用户
                db=databaseHelper.getWritableDatabase();
                //调用SQLiteDatabase的delete()方法删除数据
                db.delete("users",null,null);
                Toast.makeText(this,"用户已全部删除",Toast.LENGTH_SHORT).show();
                mTvShow.setText("");
                db.close();
                break;
            case R.id.bu_update://更新信息
                db=databaseHelper.getWritableDatabase();
                values=new ContentValues();
                values.put("passwd",passwd=mEtPasswd.getText().toString());
                //调用SQLiteDatabase的update()方法更新数据
                db.update("users",values,"userId=?",new String[]{mEtUser.getText().toString()});
                Toast.makeText(this,"信息已修改",Toast.LENGTH_SHORT).show();
                db.close();
                break;
            case R.id.bu_delete://删除单条用户信息
                db=databaseHelper.getWritableDatabase();
                values=new ContentValues();
                values.put("user",user=mEtUser.getText().toString());
                //使用SQl语句删除mEtUser获取的数据
                db.execSQL("delete from users where userId=?",new String[]{mEtUser.getText().toString()});
                //显示某用户已删除
                Toast.makeText(this,"用户"+user+"已删除",Toast.LENGTH_SHORT).show();
        }
    }
}