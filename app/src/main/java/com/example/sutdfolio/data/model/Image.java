package com.example.sutdfolio.data.model;

import java.time.LocalDate;
import java.util.Date;

public class Image {
    private final String filename;
    private final String compressedFile;
    private final String id;
    private final String originalname;
    private final Date uploadDate;
    private final String contentType;
    private final String url;
    private final String compress_url;

    public Image(String filename, String compressedFile, String id, String originalname, Date uploadDate, String contentType, String url, String compress_url) {
        this.filename = filename;
        this.compressedFile = compressedFile;
        this.id = id;
        this.originalname = originalname;
        this.uploadDate = uploadDate;
        this.contentType = contentType;
        this.url = url;
        this.compress_url = compress_url;
    }



    public String getFilename() {
        return filename;
    }

    public String getCompressedFile() {
        return compressedFile;
    }

    public String getId() {
        return id;
    }

    public String getOriginalname() {
        return originalname;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public String getContentType() {
        return contentType;
    }



    public String getUrl() {
        return url;
    }

    public String getCompress_url() {
        return compress_url;
    }
}
