package com.jtbosworth.mobilelocationsecurity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jtbosworth.mobilelocationsecurity.database.FileBaseHelper;
import com.jtbosworth.mobilelocationsecurity.database.FileCursorWrapper;
import com.jtbosworth.mobilelocationsecurity.database.FileDBSchema;
import com.jtbosworth.mobilelocationsecurity.database.FileDBSchema.FileTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Helios on 4/19/2016.
 */
public class FileQueryManager {
    private Context context;
    private SQLiteDatabase database;

    private FileQueryManager(Context context) {
        this.context = context.getApplicationContext();
        this.database = new FileBaseHelper(context).getWritableDatabase();
    }


    public void addFile(MyFile file) {
        ContentValues values = getContentValues(file);

        database.insert(FileDBSchema.FileTable.NAME, null, values);
    }

    public List<MyFile> getFiles() {
        List<MyFile> files = new ArrayList<>();

        FileCursorWrapper cursor = queryFiles(null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            files.add(cursor.getFile());
            cursor.moveToNext();
        }
        cursor.close();

        return files;
    }

    public void updateFile(MyFile file) {
        String uuidString = file.getId().toString();
        ContentValues values = getContentValues(file);

        database.update(FileTable.NAME, values,
                FileTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }


    private static ContentValues getContentValues(MyFile file) {
        ContentValues values = new ContentValues();
        values.put(FileTable.Cols.UUID, file.getId().toString());
        values.put(FileTable.Cols.TITLE, file.getTitle());
        values.put(FileTable.Cols.LOCATION, file.getLocation());
        values.put(FileTable.Cols.CONTENT, file.getContent());
        values.put(FileTable.Cols.FILE_TYPE, file.getFileType());

        return values;
    }

    private FileCursorWrapper queryFiles(String whereClause, String[] whereArgs) {
        Cursor cursor = database.query(
                FileTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new FileCursorWrapper(cursor);
    }
}
