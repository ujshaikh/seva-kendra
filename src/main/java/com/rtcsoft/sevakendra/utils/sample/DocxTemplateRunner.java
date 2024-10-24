package com.rtcsoft.sevakendra.utils.sample;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// Note: Working Solution
public class DocxTemplateRunner {

	private static String SRC = "src/main/resources/docs/cast-certificate.docx";
	private static String DEST = "src/main/resources/output/result.docx";
	private static String FONT1 = "src/main/resources/fonts/NotoSans/static/NotoSans-Black.ttf";
	private static String FONT2 = "src/main/resources/fonts/freesans/FreeSans.ttf";
	private static String IMG_SRC = "src/main/resources/images/rtc-logo.png";

	public static void main(String[] args) throws IOException {
		DocxTemplateService docService = new DocxTemplateService(FONT2);

		Map<String, String> placeholders = new HashMap<>();
		placeholders.put("firstName", "उस्मान शेख - पूर्ण");
		placeholders.put("middleName", "Middle Name");
		placeholders.put("age", "50");
		placeholders.put("cast", "XYZ");
		placeholders.put("occupation", "Labour");
		placeholders.put("place", "Village Name");
		placeholders.put("date", "23/10/2024");
		placeholders.put("docNumber", "123445456");
		placeholders.put("image", IMG_SRC);

		docService.generateReport(SRC, DEST, placeholders);
	}

}