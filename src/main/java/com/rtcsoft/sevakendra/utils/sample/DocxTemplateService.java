package com.rtcsoft.sevakendra.utils.sample;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @class WordService
 * @author Ushaikh
 **/
public class DocxTemplateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocxTemplateService.class);

	private String font;

	public DocxTemplateService(String FONT) {
		super();
		this.font = FONT;
	}

	/**
	 * Method for generating DocX report by replacing data in existing template
	 *
	 * @param docXTemplateFileWithExtension given name of docX template with
	 *                                      extension
	 * @param data                          given map of data parameters that need
	 *                                      to be replacement for docX placeholders
	 * @return generated report name
	 * @throws IOException input|output exception
	 **/
	public Optional<String> generateReport(String docXTemplateFileWithExtension, String targetDocxPath,
			Map<String, String> data) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		FileInputStream fis = new FileInputStream(docXTemplateFileWithExtension);
		try (XWPFDocument xwpfDocument = new XWPFDocument(fis)) {
			replacePlaceholdersInParagraphs(data, xwpfDocument);
			replacePlaceholderInTables(data, xwpfDocument);

			xwpfDocument.write(outputStream);

			Path path = Paths.get(targetDocxPath);
			System.out.println("File created at " + path);
			Files.write(path, outputStream.toByteArray());

			return Optional.ofNullable(targetDocxPath);
		} catch (Exception e) {
			LOGGER.error("Error occurred while generating report: {}", e.getMessage());
			return Optional.empty();
		}
	}

	/**
	 * Method for replacing docx placeholders with given data parameters in every
	 * docx paragraph
	 *
	 * @param data         given data to be replaced with template placeholders
	 * @param xwpfDocument docx template document
	 **/
	private void replacePlaceholdersInParagraphs(Map<String, String> data, XWPFDocument xwpfDocument) {
		for (XWPFParagraph paragraph : xwpfDocument.getParagraphs()) {
			replaceParagraph(paragraph, data);
		}
	}

	/**
	 * Method for replacing docx placeholders with given data parameters in docx
	 * table
	 *
	 * @param data         given data to be replaced with template placeholders
	 * @param xwpfDocument docx template document
	 **/
	private void replacePlaceholderInTables(Map<String, String> data, XWPFDocument xwpfDocument) {
		for (XWPFTable xwpfTable : xwpfDocument.getTables()) {
			for (XWPFTableRow xwpfTableRow : xwpfTable.getRows()) {
				for (XWPFTableCell xwpfTableCell : xwpfTableRow.getTableCells()) {
					for (XWPFParagraph xwpfParagraph : xwpfTableCell.getParagraphs()) {
						replaceParagraph(xwpfParagraph, data);
					}
				}
			}
		}
	}

	private void replaceParagraph(XWPFParagraph paragraph, Map<String, String> data) throws POIXMLException {
		try {
			String find, text, runsText;
			List<XWPFRun> runs;
			XWPFRun run, nextRun;
			for (String key : data.keySet()) {
				text = paragraph.getText();
				if (!text.contains("${")) {
					return;
				}
				find = "${" + key + "}";
				if (!text.contains(find)) {
					continue;
				}
				runs = paragraph.getRuns();
				for (int i = 0; i < runs.size(); i++) {
					run = runs.get(i);
					runsText = run.getText(0);
					if (runsText.contains("${")
							|| (runsText.contains("$") && runs.get(i + 1).getText(0).substring(0, 1).equals("{"))) {
						while (!openTagCountIsEqualCloseTagCount(runsText)) {
							nextRun = runs.get(i + 1);
							runsText = runsText + nextRun.getText(0);
							paragraph.removeRun(i + 1);
						}

						if (isImageFile(data.get(key))) {
							run.setText(runsText.contains(find) ? runsText.replace(find, "") : runsText, 0);
							insertImage(run, data.get(key));
						} else {
							run.setFontFamily(this.font);
							run.setText(runsText.contains(find) ? runsText.replace(find, data.get(key)) : runsText, 0);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}

	// As the next run may has a closed tag and an open tag at
	// the same time, we have to be sure that our building string
	// has a fully completed tags
	private boolean openTagCountIsEqualCloseTagCount(String runText) {
		int openTagCount = runText.split("\\$\\{", -1).length - 1;
		int closeTagCount = runText.split("}", -1).length - 1;
		return openTagCount == closeTagCount;
	}

	// Method to check if the path contains an image file with specific extensions
	public static boolean isImageFile(String filePath) {
		// Define the image file extensions
		String[] imageExtensions = { ".png", ".jpg", ".jpeg" };

		// Get the file extension from the file path
		Path path = Paths.get(filePath);
		String fileName = path.getFileName().toString().toLowerCase();

		// Check if the file name ends with any of the allowed extensions
		for (String extension : imageExtensions) {
			if (fileName.endsWith(extension)) {
				return true;
			}
		}

		return false;
	}

	// Helper method to insert image in XWPFRun
	private static void insertImage(XWPFRun r, String imagePath) throws Exception {
		try (FileInputStream imageStream = new FileInputStream(imagePath)) {
			r.addPicture(imageStream, Document.PICTURE_TYPE_PNG, imagePath, Units.toEMU(100), Units.toEMU(100));
		}
	}
}