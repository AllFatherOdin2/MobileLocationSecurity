package com.jtbosworth.mobilelocationsecurity.database;

/**
 * Created by Helios on 4/19/2016.
 */

public class FileDBSchema {
    public static final class FileTable {
        public static final String NAME = "files";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String LOCATION = "location";
            public static final String CONTENT = "content";
            public static final String FILE_TYPE = "fileType";
        }
    }
}
