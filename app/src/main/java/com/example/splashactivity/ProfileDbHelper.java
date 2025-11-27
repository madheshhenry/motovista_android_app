package com.example.splashactivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfileDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "motovista_profiles.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE =
            "CREATE TABLE " + ProfileContract.ProfileEntry.TABLE_NAME + " (" +
                    ProfileContract.ProfileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ProfileContract.ProfileEntry.COLUMN_NAME_FULLNAME + " TEXT," +
                    ProfileContract.ProfileEntry.COLUMN_NAME_PHONE + " TEXT," +
                    ProfileContract.ProfileEntry.COLUMN_NAME_ADDRESS + " TEXT," +
                    ProfileContract.ProfileEntry.COLUMN_NAME_PROFILE_URI + " TEXT," +
                    ProfileContract.ProfileEntry.COLUMN_NAME_AADHAAR_FRONT + " TEXT," +
                    ProfileContract.ProfileEntry.COLUMN_NAME_AADHAAR_BACK + " TEXT," +
                    ProfileContract.ProfileEntry.COLUMN_NAME_PAN + " TEXT," +
                    ProfileContract.ProfileEntry.COLUMN_NAME_EMI_NOTIF + " INTEGER DEFAULT 1," +
                    ProfileContract.ProfileEntry.COLUMN_NAME_REG_NOTIF + " INTEGER DEFAULT 1," +
                    ProfileContract.ProfileEntry.COLUMN_NAME_DELIVERY_NOTIF + " INTEGER DEFAULT 1" +
                    ");";

    private static final String SQL_DELETE =
            "DROP TABLE IF EXISTS " + ProfileContract.ProfileEntry.TABLE_NAME;

    public ProfileDbHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }
}
