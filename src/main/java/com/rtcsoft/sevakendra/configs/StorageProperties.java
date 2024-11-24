package com.rtcsoft.sevakendra.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("storage")
public class StorageProperties {
	@Value("${spring.customers-uploads}")
	private String uploadDir;

	/**
	 * Folder location for storing files
	 */
	private String location = uploadDir != null ? uploadDir : "/var/sevakendra/uploads/";

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}