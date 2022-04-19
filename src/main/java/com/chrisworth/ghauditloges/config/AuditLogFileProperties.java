package com.chrisworth.ghauditloges.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("auditlog")
public class AuditLogFileProperties {

	private String filename;

	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
}
