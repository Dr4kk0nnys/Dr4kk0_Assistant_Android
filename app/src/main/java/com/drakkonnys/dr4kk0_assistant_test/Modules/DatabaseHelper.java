package com.drakkonnys.dr4kk0_assistant_test.Modules;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db"; // Not case sensitive
    private static final String TABLE_NAME = "database_table"; // Not case sensitive
    private static final String ID = "id"; // It could also be col 1, col 2, ...
    private static final String VALUE = "value";

    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, VALUE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    boolean InsertData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues(); // Initialize the "placeholder" for the key-values

        contentValues.put(VALUE, value); // Placing all the data into the "key holders"

        long result = db.insert(TABLE_NAME, null, contentValues); // Adding the data to the final database

        return result != -1; // db.insert returns -1 if something goes wrong
        // The method will return false, if result equals -1
    }

    Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    boolean UpdateData(String id, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, id);
        contentValues.put(VALUE, value);

        db.update(TABLE_NAME, contentValues, "ID = ?", new String[] { id });
        return true; // This code will only be reached, if the data was successfully updated
    }

    Integer DeleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME, "ID = ?", new String[] { id });
    }

    void ResetIdValue() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_NAME + "'");
    }
}
