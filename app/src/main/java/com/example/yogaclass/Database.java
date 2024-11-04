package com.example.yogaclass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    //create db
    private static final String database_name = "Yogadb";
    private static final int database_version = 1;

    //create table
    public static final String table_classes = "Classes";
    public static final String column_class_id = "class_id";
    public static final String column_dayOfWeek = "dayOfWeek";
    public static final String column_time = "time";
    public static final String column_capacity = "capacity";
    public static final String column_duration = "duration";
    public static final String column_price = "price";
    public static final String column_type = "type";
    public static final String column_description = "desciption";
    public static final String column_teacher = "teacher";

    //SQL
    private static final String create_table_classes = "create table " + table_classes + " ("
    + column_class_id + " integer primary key autoincrement, "
    + column_dayOfWeek + " text not null, "
    + column_time + " text not null, "
    + column_capacity + " integer, "
    + column_duration + " integer, "
    + column_price + " double, "
    + column_type  + " text, "
    + column_description + " text, "
    + column_teacher + " text not null);";

    public Database(Context context) {
        super(context, database_name, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table_classes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + table_classes);
        onCreate(db);
    }

    public void addClass(Context context, String dayOfWeek, String time, int capacity, int duration, double price, String type, String description, String teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column_dayOfWeek, dayOfWeek);
        values.put(column_time, time);
        values.put(column_capacity, capacity);
        values.put(column_duration, duration);
        values.put(column_price, price);
        values.put(column_type, type);
        values.put(column_description, description);
        values.put(column_teacher, teacher);

        long result = db.insert(table_classes, null, values);
        if (result == -1) {
            db.close();
            Toast.makeText(context , "Added fail!", Toast.LENGTH_SHORT).show();
        }
        else {
            db.close();
            Toast.makeText(context , "Added ok!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(table_classes, null, null, null, null,null,null);
    }
}
