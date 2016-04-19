package com.jtbosworth.mobilelocationsecurity.database;

/**
 * Created by Helios on 4/19/2016.
 */
import android.database.Cursor;
import android.database.CursorWrapper;

import com.jtbosworth.mobilelocationsecurity.MyFile;

import java.util.UUID;

import com.jtbosworth.mobilelocationsecurity.database.FileDBSchema.FileTable;

public class FileCursorWrapper extends CursorWrapper {
    public FileCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public MyFile getFile() {
        String uuidString = getString(getColumnIndex(FileTable.Cols.UUID));
        String title = getString(getColumnIndex(FileTable.Cols.TITLE));
        String location = getString(getColumnIndex(FileTable.Cols.LOCATION));
        String content = getString(getColumnIndex(FileTable.Cols.CONTENT));
        String fileType = getString(getColumnIndex(FileTable.Cols.FILE_TYPE));

        MyFile file = new MyFile(UUID.fromString(uuidString));
        file.setTitle(title);
        file.setLocation(location);
        file.setContent(content);
        file.setFileType(fileType);

        return file;
    }
}
