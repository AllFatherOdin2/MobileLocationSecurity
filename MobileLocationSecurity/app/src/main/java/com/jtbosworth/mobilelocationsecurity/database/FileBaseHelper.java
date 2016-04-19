package com.jtbosworth.mobilelocationsecurity.database;

/**
 * Created by Helios on 4/19/2016.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jtbosworth.mobilelocationsecurity.database.FileDBSchema.FileTable;

public class FileBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "FileBaseHelper";
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "fileTable.db";

    public FileBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + FileTable.NAME + "(" +
                        " _id integer primary key autoincrement, " +
                        FileTable.Cols.UUID + ", " +
                        FileTable.Cols.TITLE + ", " +
                        FileTable.Cols.LOCATION + ", " +
                        FileTable.Cols.CONTENT + ", " +
                        FileTable.Cols.FILE_TYPE +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
