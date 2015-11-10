package com.mtm.ksu.mobile.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_VALUE = "value";
    static final String TAG = "DBAdapter";

    static final String DATABASE_NAME = "andromeda.db";
    static final String DATABASE_TABLE = "parameters";
    static final int DATABASE_VERSION = 2;

    static final String DATABASE_CREATE =
        "create table parameters (_id integer primary key autoincrement, "
        + "name text not null, value text not null );";

    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;
    
    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DATABASE_CREATE);
                initializeDatabase(db);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS parameters");
            onCreate(db);
        }
        
        public void initializeDatabase(SQLiteDatabase db){
        	String param1 = "INSERT INTO parameters(name, value) VALUES ('ANDROMEDA_LOCAL_ADMIN_PASSWORD','andromeda')";
        	String param2 = "INSERT INTO parameters(name, value) VALUES ('ANDROMEDA_SERVER_ADDRESS','http://10.0.2.2:80/mtech_service2/')";
        	String param3 = "INSERT INTO parameters(name, value) VALUES ('ANDROMEDA_SERVER_KODETRANSSETOR','01')";
        	String param4 = "INSERT INTO parameters(name, value) VALUES ('ANDROMEDA_SERVER_KODETRANSTARIK','02')";
        	String param5 = "INSERT INTO parameters(name, value) VALUES ('ANDROMEDA_SERVER_KODETRANSANGSURAN','002')";
        	String param6 = "INSERT INTO parameters(name, value) VALUES ('ANDROMEDA_BLUETOOTH_PRINTER_NAME','AB-320M')";
        	String param7 = "INSERT INTO parameters(name, value) VALUES ('ANDROMEDA_COMPANY_NAME','Mitra Tekno Madani')";
        	
        	
        	db.execSQL(param1);
        	db.execSQL(param2);
        	db.execSQL(param3);
        	db.execSQL(param4);
        	db.execSQL(param5);
        	db.execSQL(param6);
        	db.execSQL(param7);
        }
    }

    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---
    public void close() 
    {
        DBHelper.close();
    }

    //---insert a contact into the database---
    public long insertParameter(String name, String value) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_VALUE, value);
       return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular contact---
    public boolean deleteParameter(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    //---retrieves all the contacts---
    public Cursor getAllParameters()
    {
        return db.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_VALUE}, null, null, null, null, null);
    }

    //---retrieves a particular contact---
    public Cursor getParameter(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_NAME, KEY_VALUE}, KEY_ROWID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    public Cursor getParameterByName(String keyName) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                KEY_NAME, KEY_VALUE}, KEY_NAME + "='" + keyName+"'", null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    //---updates a contact---
    public boolean updateParameter(long rowId, String name, String value) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_VALUE, value);
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }

}
