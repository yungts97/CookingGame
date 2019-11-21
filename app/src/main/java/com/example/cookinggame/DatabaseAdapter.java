package com.example.cookinggame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class DatabaseAdapter {
    public static final String DATABASE_NAME = "CookingGame.db";
    public static final String TABLE_NAME1 = "tblFood";
    public static final String TABLE_NAME2 = "tblIngredient";
    public static final String TABLE_NAME3 = "tblFoodIngredient";
    public static final String TABLE_NAME4 = "tblHighScore";
    public static final String FID = "FID";
    public static final String NAME= "Name";
    public static final String IMAGE = "Image";
    public static final String IID = "IID";
    public static final String QUANTITY = "Quantity";
    public static final String CID = "CID";
    public static final String RECIPESET = "Recipe_Set";
    public static final String SID = "SID";
    public static final String SCORE = "Score";
    public static final String DATABASE_CREATE[] = {
            "create table "+ TABLE_NAME1 + " ("+
            FID +" integer primary key autoincrement, "+
            NAME + " text not null, "+
            IMAGE + " text not null);",
            "create table "+ TABLE_NAME2 + " ("+
            IID +" integer primary key autoincrement, "+
            IMAGE + " text not null);",
            "create table "+ TABLE_NAME3 + " ("+
            CID +" integer primary key autoincrement, "+
            FID +" integer not null, "+
            IID +" integer not null, "+
            QUANTITY + " integer not null,"+
            RECIPESET + " integer not null);",
            "create table "+ TABLE_NAME4 + " ("+
            SID + " integer primary key autoincrement, "+
            SCORE + " integer not null);"};

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DatabaseAdapter(Context cxt) {
        this.context = cxt;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, 1);
        }
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try{
                for(int i = 0; i < DATABASE_CREATE.length; i++)
                {
                    db.execSQL(DATABASE_CREATE[i]);
                }

            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG,"Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS tblFood");
            db.execSQL("DROP TABLE IF EXISTS tblIngredient");
            db.execSQL("DROP TABLE IF EXISTS tblFoodIngredient");
            onCreate(db);
        }
    }

    public DatabaseAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        DBHelper.close();
    }

    public long insertFood(String name, String image)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(NAME, name);
        initialValues.put(IMAGE, image);
        return db.insert(TABLE_NAME1, null, initialValues);
    }
    public long insertIngredient(String image)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(IMAGE, image);
        return db.insert(TABLE_NAME2, null, initialValues);
    }
    public long insertFoodIngredient(String fID, long iID, int qty, int set)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(FID, fID);
        initialValues.put(IID, iID);
        initialValues.put(QUANTITY, qty);
        initialValues.put(RECIPESET, set);
        return db.insert(TABLE_NAME3, null, initialValues);
    }

    public Cursor getAllFood()
    {
        Cursor mCursor = db.query(TABLE_NAME1, new String[] {FID, NAME, IMAGE}, null, null, null, null, null);
        if(mCursor !=null)
        {
            mCursor.moveToFirst();
        }
        return  mCursor;
    }

    public Cursor getAllIngredient()
    {
        Cursor mCursor = db.query(TABLE_NAME2, new String[] {IID,IMAGE}, null, null, null, null, null);
        if(mCursor !=null)
        {
            mCursor.moveToFirst();
        }
        return  mCursor;
    }
    public Cursor getIngredients(ArrayList<String> images)
    {
        String condition = "";
        for(int i = 0; i < images.size(); i++)
        {
            condition += (IMAGE +"!='"+images.get(i))+"'";
            if(i != images.size()-1)
                condition += " AND ";
        }
        Cursor mCursor = db.query(TABLE_NAME2, new String[] {IID, IMAGE},condition,null,null,null,null);
        if(mCursor !=null)
        {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    public Cursor getAllFoodIngredient()
    {
        Cursor mCursor = db.query(TABLE_NAME3, new String[] {CID, FID, IID,QUANTITY, RECIPESET}, null, null, null, null, null);
        if(mCursor !=null)
        {
            mCursor.moveToFirst();
        }
        return  mCursor;
    }

    public Cursor getFood(String fID) throws SQLException
    {
        Cursor mCursor = db.query(true, TABLE_NAME1, new String[] {FID, NAME, IMAGE},FID + "=" + fID, null, null,null,null, null);
        if(mCursor !=null)
        {
            mCursor.moveToFirst();
        }
        return  mCursor;
    }


    public Cursor getFoodIngredient(String fID) throws SQLException
    {
        Cursor mCursor = db.rawQuery("SELECT * FROM tblFoodIngredient, tblIngredient WHERE tblFoodIngredient.IID = tblIngredient.IID AND tblFoodIngredient.FID ="+fID +" ORDER BY " + RECIPESET, null);
        if(mCursor !=null)
        {
            mCursor.moveToFirst();
        }
        return  mCursor;
    }

    public long insertHighScore(int score)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(SCORE, score);
        return db.insert(TABLE_NAME4, null, initialValues);
    }

    public boolean updateHighScore(int score)
    {
        ContentValues args = new ContentValues();
        args.put(SCORE, score);
        return db.update(TABLE_NAME4, args, SID + "=" + 1, null) > 0;
    }


    public Cursor getHighScore() throws SQLException
    {
        Cursor mCursor = db.query(TABLE_NAME4, new String[] {SID, SCORE}, null, null, null, null, null);
        if(mCursor !=null)
        {
            mCursor.moveToFirst();
        }
        return  mCursor;
    }

}
