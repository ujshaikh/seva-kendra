package com.rtcsoft.sevakendra.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@ConfigurationProperties("storage")
public class StorageProperties {
	@Value("${spring.customers-uploads}")
	private String customerImgUploadEnv;

	@Value("${spring.templates-dir}")
	private static String docTemplateDirEnv;

	@Value("${spring.generated-docs-dir}")
	private static String genDocsDirEnv;

	/**
	 * Folder location for storing files
	 */
	private String customerImgUploadPath = customerImgUploadEnv != null ? customerImgUploadEnv
			: "/var/sevakendra/uploads/";

	@Getter
	private String docTemplatePath = docTemplateDirEnv != null ? docTemplateDirEnv : "/var/sevakendra/templates/";

	@Getter
	private String genDocsPath = genDocsDirEnv != null ? genDocsDirEnv : "/var/sevakendra/generated/docs/";

	public String getCustomerImgUploadPath() {
		return customerImgUploadPath;
	}

	public void setCustomerImgUploadPath(String location) {
		this.customerImgUploadPath = location;
	}

}