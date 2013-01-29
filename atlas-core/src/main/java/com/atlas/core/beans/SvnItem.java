package com.atlas.core.beans;

import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

public class SvnItem {
	
	@Field
	private String id;
	
	@Field
	private String fileName;
	
	@Field
	private String path;
	
	@Field
	private String lastCommitter;
	
	@Field
	private long lastRevision;
	
	@Field
	private Date updatedAt;
	
	@Field
	private String type;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public String getId() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPath());
		sb.append("/");
		sb.append(getFileName());
		
		return sb.toString();
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SvnItem [id=");
		builder.append(id);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append(", path=");
		builder.append(path);
		builder.append(", lastCommitter=");
		builder.append(lastCommitter);
		builder.append(", lastRevision=");
		builder.append(lastRevision);
		builder.append(", updatedAt=");
		builder.append(updatedAt);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}
}
