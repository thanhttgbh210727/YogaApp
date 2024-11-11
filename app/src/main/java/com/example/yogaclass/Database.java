package com.example.yogaclass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    // Database info
    private static final String DATABASE_NAME = "Yogadb";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    public static final String TABLE_CLASSES = "Classes";
    public static final String COLUMN_CLASS_ID = "class_id";
    public static final String COLUMN_DAY_OF_WEEK = "dayOfWeek";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_CAPACITY = "capacity";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TEACHER = "teacher";

    // SQL statement to create the classes table
    private static final String CREATE_TABLE_CLASSES = "CREATE TABLE " + TABLE_CLASSES + " ("
            + COLUMN_CLASS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DAY_OF_WEEK + " TEXT NOT NULL, "
            + COLUMN_TIME + " TEXT NOT NULL, "
            + COLUMN_CAPACITY + " INTEGER, "
            + COLUMN_DURATION + " INTEGER, "
            + COLUMN_PRICE + " DOUBLE, "
            + COLUMN_TYPE + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_TEACHER + " TEXT NOT NULL);";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CLASSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        onCreate(db);
    }

    // Helper method to show a toast message
    private void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void addClass(Context context, String dayOfWeek, String time, int capacity, int duration, double price, String type, String description, String teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY_OF_WEEK, dayOfWeek);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_CAPACITY, capacity);
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_TEACHER, teacher);

        try {
            long result = db.insert(TABLE_CLASSES, null, values);
            showToast(context, result == -1 ? "Add failed!" : "Added successfully!");
        } catch (Exception e) {
            showToast(context, "Error adding class: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public void updateClass(Context context, String classId, String dayOfWeek, String time, int capacity, int duration, double price, String type, String description, String teacher) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DAY_OF_WEEK, dayOfWeek);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_CAPACITY, capacity);
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_TEACHER, teacher);

        try {
            int result = db.update(TABLE_CLASSES, values, COLUMN_CLASS_ID + " = ?", new String[]{classId});
            showToast(context, result == -1 ? "Update failed!" : "Updated successfully!");
        } catch (Exception e) {
            showToast(context, "Error updating class: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public void deleteClass(Context context, String classId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int result = db.delete(TABLE_CLASSES, COLUMN_CLASS_ID + " = ?", new String[]{classId});
            showToast(context, result == -1 ? "Delete failed!" : "Deleted successfully!");
        } catch (Exception e) {
            showToast(context, "Error deleting class: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    public Cursor getClasses() {
        return this.getReadableDatabase().query(TABLE_CLASSES, null, null, null, null, null, null);
    }

    public Cursor searchClassByName(String teacher) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_TEACHER + " LIKE ?";
        String[] selectionArgs = {"%" + teacher + "%"};
        return db.query(TABLE_CLASSES, null, selection, selectionArgs, null, null, null);
    }
}
