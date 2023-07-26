package edu.ptit.ql_fresher.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.ptit.ql_fresher.model.Center;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ql_fresher.db";
    private static int DATABASE_VERSION = 1;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE centers(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "acronym TEXT, name TEXT, address TEXT, totalFresher INTEGER)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public long addCenter(Center i) {
        ContentValues values = new ContentValues();
        values.put("acronym", i.getAcronym());
        values.put("name", i.getName());
        values.put("address", i.getAddress());
        values.put("totalFresher", i.getTotalFresher());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("centers", null, values);
    }

    public int updateCenter(Center i) {
        ContentValues values = new ContentValues();
        values.put("acronym", i.getAcronym());
        values.put("name", i.getName());
        values.put("address", i.getAddress());
        values.put("totalFresher", i.getTotalFresher());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = " id = ?";
        String[] whereArgs = {Integer.toString(i.getId())};
        return sqLiteDatabase.update("centers", values, whereClause, whereArgs);
    }

    public int deleteCenter(String i) {
        String whereClause = " id = ?";
        String[] whereArgs = {i};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("centers", whereClause, whereArgs);
    }

    public List<Center> getAllCenter() {
        List<Center> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String order = "totalFresher DESC";
        Cursor rs = sqLiteDatabase.query("centers",
                null, null, null,
                null, null, order);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String acronym = rs.getString(1);
            String name = rs.getString(2);
            String address = rs.getString(3);
            int totalFresher = rs.getInt(4);
            list.add(new Center(id, acronym, name, address, totalFresher));
        }
        return list;
    }

    public Center getCenterById(String key) {
        Center center = new Center();
        String whereClause = "id = ? ";
        String[] whereArgs = {key};
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("centers",
                null, whereClause, whereArgs,
                null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String acronym = rs.getString(1);
            String name = rs.getString(2);
            String address = rs.getString(3);
            int total = rs.getInt(4);
            center.setId(id);
            center.setAcronym(acronym);
            center.setName(name);
            center.setAddress(address);
            center.setTotalFresher(total);
        }
        return center;
    }

    public Center getCenterByName(String key) {
        Center center = new Center();
        String whereClause = "acronym = ? ";
        String[] whereArgs = {key};
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("centers",
                null, whereClause, whereArgs,
                null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String acronym = rs.getString(1);
            String name = rs.getString(2);
            String address = rs.getString(3);
            int total = rs.getInt(4);
            center.setId(id);
            center.setAcronym(acronym);
            center.setName(name);
            center.setAddress(address);
            center.setTotalFresher(total);
        }
        return center;
    }
}