package com.jtbosworth.mobilelocationsecurity;

import java.util.UUID;

/**
 * Created by Helios on 4/19/2016.
 */
public class MyFile {
    private UUID id;
    private String title;
    private String location;
    private String content;
    private String fileType;

    public MyFile(){
        this(UUID.randomUUID());
    }

    public MyFile(UUID id) {
        this.id = id;
    }

    public void setTitle(String title){ this.title = title; }
    public void setLocation(String location){ this.location = location; }
    public void setContent(String content){ this.content = content; }
    public void setFileType(String fileType){ this.fileType = fileType; }

    public UUID getId(){ return id; }
    public String getTitle(){ return title; }
    public String getLocation(){ return location; }
    public String getContent(){ return content; }
    public String getFileType(){ return fileType; }
}
