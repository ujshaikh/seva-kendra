package com.rtcsoft.sevakendra.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("storage")
public class StorageProperties {
	@Value("${spring.customers-uploads}")
	private String customerImgUploadEnv;

	/**
	 * Folder location for storing files
	 */
	private String customerImgUploadPath = customerImgUploadEnv != null ? customerImgUploadEnv
			: "/var/sevakendra/uploads/";

	public String getCustomerImgUploadPath() {
		return customerImgUploadPath;
	}

	public void setCustomerImgUploadPath(String location) {
		this.customerImgUploadPath = location;
	}

}