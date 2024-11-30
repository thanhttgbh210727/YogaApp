package com.example.yogaclass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    // Database configuration
    private static final String DATABASE_NAME = "Yogadb";
    private static final int DATABASE_VERSION = 1;

    // Table and column definitions
    public static final String TABLE_CLASSES = "Classes";
    public static final String COLUMN_CLASS_ID = "class_id";
    public static final String COLUMN_DAY_OF_WEEK = "dayOfWeek";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_CAPACITY = "capacity";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_DESCRIPTION = "desciption";
    public static final String COLUMN_TEACHER = "teacher";

    // SQL statement to create the table
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

    // Constructor
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute SQL to create the table
        db.execSQL(CREATE_TABLE_CLASSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        onCreate(db);
    }

    // Add a new class
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

        long result = db.insert(TABLE_CLASSES, null, values);
        db.close();

        if (result == -1) {
            Toast.makeText(context, "Addition Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Update an existing class
    public void updateClass(Context context, String class_id, String dayOfWeek, String time, int capacity, int duration, double price, String type, String description, String teacher) {
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

        int rowsAffected = db.update(TABLE_CLASSES, values, COLUMN_CLASS_ID + " = ?", new String[]{class_id});
        db.close();

        if (rowsAffected == -1) {
            Toast.makeText(context, "Update Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Delete a class
    public void deleteClass(Context context, String class_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_CLASSES, COLUMN_CLASS_ID + " = ?", new String[]{class_id});
        db.close();

        if (rowsDeleted == -1) {
            Toast.makeText(context, "Deletion Failed!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    // Retrieve all classes
    public Cursor getClasses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CLASSES, null, null, null, null, null, null);
    }

    // Search for classes by teacher name
    public Cursor searchClassByName(String teacher) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_TEACHER + " LIKE ?";
        String[] selectionArgs = {"%" + teacher + "%"};
        return db.query(TABLE_CLASSES, null, selection, selectionArgs, null, null, null);
    }
}
