package com.atlas.core.svn;

import java.util.Date;

public class SVNItem {
    private String id;
    private String filename;
    private String path;
    private String lastCommitter;
    private long lastRevision;
    private Date updatedAt;
    private String type;
    private String url;
    private long size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLastCommitter() {
        return lastCommitter;
    }

    public void setLastCommitter(String lastCommitter) {
        this.lastCommitter = lastCommitter;
    }

    public long getLastRevision() {
        return lastRevision;
    }

    public void setLastRevision(long lastRevision) {
        this.lastRevision = lastRevision;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "SVNItem{" +
                "id='" + id + '\'' +
                ", filename='" + filename + '\'' +
                ", path='" + path + '\'' +
                ", lastCommitter='" + lastCommitter + '\'' +
                ", lastRevision=" + lastRevision +
                ", updatedAt=" + updatedAt +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", size=" + size +
                '}';
    }
}
