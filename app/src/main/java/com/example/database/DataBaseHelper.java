package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mail.ToolUtils;
import com.example.po.Collection;
import com.example.po.Commodity;
import com.example.po.Review;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


//我的收藏数据库连接类

public class DataBaseHelper extends SQLiteOpenHelper {

    //定义数据库表名
    public static final String DB_NAME = "Store.db";

    //创建商品表
    private static final String CREATE_COMMODITY_DB = "create table tb_commodity(" +
            "id integer primary key autoincrement," +
            "title text," +
            "category text," +
            "price float," +
            "phone text," +
            "description text," +
            "picture blob," +
            "stuId text)";
    /**
     * 创建收藏信息表
     **/
    private static final String CREATE_COLLECTION_DB = "create table tb_collection (" +
            "id integer primary key autoincrement," +
            "stuId text," +
            "picture blob," +
            "title text," +
            "description text," +
            "price float," +
            "phone text )";

    private static final String CREATE_LOOK_DB = "create table tb_look_history (" +
            "id integer primary key autoincrement," +
            "stuId text," +
            "picture blob," +
            "title text," +
            "description text," +
            "price float," +
            "time text," +
            "phone text )";

    /**
     * 创建评论信息表
     **/
    private static final String CREATE_REVIEW_DB = "create table tb_review (" +
            "id integer primary key autoincrement," +
            "stuId text," +
            "currentTime text," +
            "content text," +
            "position integer )";

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_COLLECTION_DB);
        db.execSQL(CREATE_LOOK_DB);
        db.execSQL(CREATE_REVIEW_DB);
        db.execSQL(CREATE_COMMODITY_DB);

        //账号/手机号userId，密码passWord，姓名name，邮箱email，地址address
        db.execSQL("create table if not exists users" +
                "(userId varchar(20) primary key," +
                "password varchar(20) not null," +
                "name varchar(20)," +

                "email varchar(20)," +
                "address varchar(50)," +
                "image blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    /**
     * 添加物品方法
     *
     * @param commodity 物品对象
     */
    public boolean AddCommodity(Commodity commodity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", commodity.getTitle());
        values.put("category", commodity.getCategory());
        values.put("price", commodity.getPrice());
        values.put("phone", commodity.getPhone());
        values.put("description", commodity.getDescription());
        values.put("picture", commodity.getPicture());
        values.put("stuId", commodity.getStuId());
        db.insert("tb_commodity", null, values);
        values.clear();
        return true;
    }

    /**
     * 查找
     */
    public List<Commodity> readMyCommodities(String stuId) {
        List<Commodity> myCommodities = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_commodity where stuId=?", new String[]{stuId});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                Commodity commodity = new Commodity();
                commodity.setTitle(title);
                commodity.setCategory(category);
                commodity.setPrice(price);
                commodity.setId(id);
                commodity.setDescription(description);
                commodity.setPhone(phone);
                commodity.setPicture(picture);
                myCommodities.add(commodity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return myCommodities;
    }

    public List<Commodity> searchCommodities(String title1) {
        List<Commodity> myCommodities = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.query(tb_commodity, null, "name like ？", new String[]{"%"+searcherFilter+"%"}, null, null, null);
        //Cursor cursor = db.rawQuery("select * from tb_commodity where title like '%\"+searcherFilter \"%'\",new String[]{title});
        String sql = "select * from tb_commodity where title like '%" + title1 + "%' order by ? desc";
        //String sql = "select * from tb_commodity where title=?";
        Cursor cursor = db.rawQuery(sql, new String[]{title1});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                Commodity commodity = new Commodity();
                commodity.setId(id);
                commodity.setTitle(title);
                commodity.setCategory(category);
                commodity.setPrice(price);
                commodity.setDescription(description);
                commodity.setPhone(phone);
                commodity.setPicture(picture);
                myCommodities.add(commodity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return myCommodities;
    }

    public List<Commodity> readAllCommodities() {
        List<Commodity> allCommodities = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_commodity order by price", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                String stuId = cursor.getString(cursor.getColumnIndex("stuId"));
                Commodity commodity = new Commodity();
                commodity.setId(id);
                commodity.setTitle(title);
                commodity.setCategory(category);
                commodity.setPrice(price);
                commodity.setDescription(description);
                commodity.setPhone(phone);
                commodity.setPicture(picture);
                commodity.setStuId(stuId);
                allCommodities.add(commodity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allCommodities;
    }

    /**
     * 根据商品名称删除商品
     */
    public void deleteMyCommodity(String title, String description, float price) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.isOpen()) {
            db.delete("tb_commodity", "title=? and description=? and price=?", new String[]{title, description, String.valueOf(price)});
            db.close();
        }
    }

    /**
     * 读取不同类别的商品信息
     */
    public List<Commodity> readCommodityType(String category) {
        List<Commodity> differentTypes = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_commodity where category=?", new String[]{category});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                Commodity commodity = new Commodity();
                commodity.setTitle(title);
                commodity.setId(id);
                commodity.setPrice(price);
                commodity.setCategory(category);
                commodity.setDescription(description);
                commodity.setPicture(picture);
                differentTypes.add(commodity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return differentTypes;
    }


    /**
     * 添加评论
     *
     * @param review 评论对象
     */
    public void addReview(Review review) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stuId", review.getStuId());
        values.put("currentTime", review.getCurrentTime());
        values.put("content", review.getContent());
        values.put("position", review.getPosition());
        db.insert("tb_review", null, values);
        values.clear();
    }

    /**
     * 根据商品项编号读取相应的评论信息
     *
     * @return 评论对象数组
     */
    public LinkedList<Review> readReviews(int position) {
        LinkedList<Review> reviews = new LinkedList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_review where position=?", new String[]{String.valueOf(position)});
        if (cursor.moveToFirst()) {
            do {
                String stuId = cursor.getString(cursor.getColumnIndex("stuId"));
                String currentTime = cursor.getString(cursor.getColumnIndex("currentTime"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                Review review = new Review();
                review.setStuId(stuId);
                review.setCurrentTime(currentTime);
                review.setContent(content);
                reviews.add(review);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reviews;
    }

    /**
     * 添加我的收藏商品
     *
     * @param collection 收藏对象
     */
    public void addMyCollection(Collection collection) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stuId", collection.getStuId());
        values.put("picture", collection.getPicture());
        values.put("title", collection.getTitle());
        values.put("description", collection.getDescription());
        values.put("price", collection.getPrice());
        values.put("phone", collection.getPhone());
        db.insert("tb_collection", null, values);
        values.clear();
    }

    /**
     * 通过当前账号获取我的收藏商品信息
     */
    public List<Collection> readMyCollections(String stuId) {
        List<Collection> collections = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_collection where stuId=?", new String[]{stuId});
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                Collection collection = new Collection();
                collection.setPicture(picture);
                collection.setTitle(title);
                collection.setDescription(description);
                collection.setPrice(price);
                collection.setPhone(phone);
                collections.add(collection);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return collections;
    }

    /**
     * 删除收藏的商品项
     */
    public void deleteMyCollection(String title, String description, float price) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.isOpen()) {
            db.delete("tb_collection", "title=? and description=? and price=?", new String[]{title, description, String.valueOf(price)});
            db.close();
        }
    }


    /**
     * 添加浏览记录
     *
     * @param collection 收藏对象
     */
    public void addLookHistory(Collection collection) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tb_look_history", "id=?", new String[]{String.valueOf(collection.getId())});
        ContentValues values = new ContentValues();
        values.put("id", collection.getId());
        values.put("stuId", collection.getStuId());
        values.put("picture", collection.getPicture());
        values.put("title", collection.getTitle());
        values.put("description", collection.getDescription());
        values.put("price", collection.getPrice());
        values.put("phone", collection.getPhone());
        values.put("time", ToolUtils.getTime());
        db.insert("tb_look_history", null, values);
        db.close();
        values.clear();
    }

    /**
     * 通过当前账号获取我的收藏商品信息
     */
    public List<Collection> getLookHistory(String stuId) {
        List<Collection> collections = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_look_history where stuId=? order by time desc", new String[]{stuId});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                float price = cursor.getFloat(cursor.getColumnIndex("price"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                byte[] picture = cursor.getBlob(cursor.getColumnIndex("picture"));
                Collection collection = new Collection();
                collection.setId(id);
                collection.setPicture(picture);
                collection.setTitle(title);
                collection.setDescription(description);
                collection.setPrice(price);
                collection.setPhone(phone);
                collection.setTime(time);
                collections.add(collection);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return collections;
    }


    /**
     * 根据id删除记录
     */
    public void deleteLookHistory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db.isOpen()) {
            db.delete("tb_look_history", "id=?", new String[]{String.valueOf(id)});
            db.close();
        }
    }

}
