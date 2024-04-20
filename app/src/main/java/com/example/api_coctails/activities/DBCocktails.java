package com.example.api_coctails.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.api_coctails.model.Cocktail;

import java.util.ArrayList;

public class DBCocktails {
    private static final String DATABASE_NAME = "coctails.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tableFavorite";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "Title";
    private static final String COLUMN_PICTURE = "PictureUrl";
    private static final String COLUMN_CATEGORY = "Category";
    private static final String COLUMN_INSTRUCTION = "Instruction";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_TITLE = 1;
    private static final int NUM_COLUMN_PICTURE = 2;
    private static final int NUM_COLUMN_CATEGORY = 3;
    private static final int NUM_COLUMN_INSTRUCTION = 4;

    private SQLiteDatabase сDataBase;


    public DBCocktails(Context context) {
        OpenHelper сOpenHelper = new OpenHelper(context);
        сDataBase = сOpenHelper.getWritableDatabase(); // чтобы открыть и вернуть экземпляр БД
    }

    public long insert(String title, String picture, String category, String instruction) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_PICTURE, picture);
        cv.put(COLUMN_CATEGORY, category);
        cv.put(COLUMN_INSTRUCTION, instruction);
        return сDataBase.insert(TABLE_NAME, null, cv);
    }
    public long insert(Cocktail cocktail) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_TITLE, cocktail.getTitle());
        cv.put(COLUMN_PICTURE, cocktail.getPictureUrl());
        cv.put(COLUMN_CATEGORY, cocktail.getCategory());
        cv.put(COLUMN_INSTRUCTION, cocktail.getInstruction());
        return сDataBase.insert(TABLE_NAME, null, cv);
    }

    public int update(Cocktail md) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_TITLE, md.getTitle());
        cv.put(COLUMN_PICTURE, md.getPictureUrl());
        cv.put(COLUMN_CATEGORY, md.getCategory());
        cv.put(COLUMN_INSTRUCTION,md.getInstruction());
        return сDataBase.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[] { String.valueOf(md.getId())});
    }

    public void deleteAll() {
        сDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        сDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public Cocktail select(long id) {
        Cursor сCursor = сDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        сCursor.moveToFirst();
        String title = сCursor.getString(NUM_COLUMN_TITLE);
        String picture = сCursor.getString(NUM_COLUMN_PICTURE);
        String category = сCursor.getString(NUM_COLUMN_CATEGORY);
        String instruction = сCursor.getString(NUM_COLUMN_INSTRUCTION);
        return new Cocktail(id, title, picture, category, instruction);
    }
    public Boolean isSelect(String title) {
        String query = "Select * from " + TABLE_NAME + " where " + COLUMN_TITLE + " = " + title.toString();
        Cursor сCursor = сDataBase.rawQuery(query, null);
        if (сCursor.getCount() > 0) {
            Log.i("<<< DBase >>>", "Find " + query);
            сCursor.close();
            return true;
        }
        Log.i("<<< DBase >>>", "No find " + query);
        сCursor.close();
        return false;
    }

    public ArrayList<Cocktail> selectAll() {
        Cursor сCursor = сDataBase.query(TABLE_NAME, null, null, null, null, null, null);

        ArrayList<Cocktail> arr = new ArrayList<Cocktail>();
        сCursor.moveToFirst();
        if (!сCursor.isAfterLast()) {
            do {
                long id = сCursor.getLong(NUM_COLUMN_ID);
                String title = сCursor.getString(NUM_COLUMN_TITLE);
                String picture = сCursor.getString(NUM_COLUMN_PICTURE);
                String category = сCursor.getString(NUM_COLUMN_CATEGORY);
                String instruction = сCursor.getString(NUM_COLUMN_INSTRUCTION);
                arr.add(new Cocktail(id, title, picture, category, instruction));
            } while (сCursor.moveToNext());
        }
        return arr;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_PICTURE + " TEXT, " +
                    COLUMN_CATEGORY + " TEXT,"+
                    COLUMN_INSTRUCTION +" TEXT);";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
