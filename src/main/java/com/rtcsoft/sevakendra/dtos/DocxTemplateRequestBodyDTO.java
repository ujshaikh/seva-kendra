package com.rtcsoft.sevakendra.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocxTemplateRequestBodyDTO {

	private List<Integer> ids;
	private List<Integer> customerIds;
	private List<DocTemplate> docTemplates;

	@Getter
	@Setter
	public static class DocTemplate {
		private boolean active;
		private boolean checked;
		private int id;
		private String title;
	}
}
