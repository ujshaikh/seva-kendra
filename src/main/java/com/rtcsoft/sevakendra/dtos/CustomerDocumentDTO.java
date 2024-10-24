package com.rtcsoft.sevakendra.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDocumentDTO {

	private String docName;
	private String docExt;
	private String docPath;

	private long customerId;
	private long userId;

	private String thumbnail;

	private Boolean isActive;
}
