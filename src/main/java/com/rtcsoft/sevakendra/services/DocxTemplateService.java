package com.rtcsoft.sevakendra.services;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rtcsoft.sevakendra.dtos.CustomerDocumentDTO;
import com.rtcsoft.sevakendra.entities.Customer;
import com.rtcsoft.sevakendra.entities.CustomerDocument;
import com.rtcsoft.sevakendra.exceptions.CustomerException;
import com.rtcsoft.sevakendra.repositories.CustomerDocumentRepository;
import com.rtcsoft.sevakendra.repositories.CustomerRepository;

/**
 * @class WordService
 * @author Ushaikh
 **/

@Service
public class DocxTemplateService {

	private static String IMG_SRC = "src/main/resources/images/rtc-logo.png";

	private static String SRC = "src/main/resources/docs/cast-certificate.docx";
	private static String DEST_PATH = "src/main/resources/output/";
	// private static String FONT1 =
	// "src/main/resources/fonts/NotoSans/static/NotoSans-Black.ttf";
	private static String FONT2 = "src/main/resources/fonts/freesans/FreeSans.ttf";

	private static final Logger LOGGER = LoggerFactory.getLogger(DocxTemplateService.class);

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	CustomerDocumentRepository customerDocumentRepository;

	public ResponseEntity<CustomerDocument> create(CustomerDocumentDTO input) throws CustomerException {
		try {
			String path = generateDocument(input);

			if (path != null) {
				CustomerDocument customerDocument = new CustomerDocument();
				customerDocument.setUserId(input.getUserId());
				customerDocument.setCustomerId(input.getCustomerId());
				customerDocument.setDocName(input.getDocName());
				customerDocument.setThumbnail(input.getThumbnail());
				customerDocument.setDocPath(path);

				CustomerDocument created = customerDocumentRepository.save(customerDocument);

				return ResponseEntity.status(HttpStatus.CREATED).body(customerDocument);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	private Optional<HashMap<String, Object>> prepareDataMap(CustomerDocumentDTO customerDocument) {
		try {
			Optional<Customer> customer = customerRepository.findById(customerDocument.getCustomerId());

			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.of("hi", "IN"));
			String formattedDate = formatter.format(calendar.getTime());

			Optional<HashMap<String, Object>> placeholders = customer.map(cust -> {
				HashMap<String, Object> map = new HashMap<>();
				map.put("firstName", cust.getFirstName());
				map.put("middleName", cust.getMiddleName());
				map.put("lastName", cust.getLastName());
				map.put("phoneNumber", cust.getPhoneNumber());
				map.put("address", cust.getAddress());
				map.put("place", cust.getPlace());
				map.put("age", cust.getAge());
				map.put("cast", cust.getCast());
				map.put("occupation", cust.getOccupation());
				map.put("aadharNumber", cust.getAadharNumber());
				map.put("image", cust.getImage());
				map.put("date", formattedDate);
				return map;
			});

			return placeholders;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Method for generating DocX report by replacing data in existing template
	 *
	 * @param docXTemplateFileWithExtension given name of docX template with
	 *                                      extension
	 * @param data                          given map of data parameters that need
	 *                                      to be replacement for docX placeholders
	 * @return
	 * @return generated report name
	 * @throws IOException input|output exception
	 **/

	public String generateDocument(CustomerDocumentDTO customerDocument) throws IOException {
		Optional<HashMap<String, Object>> data = prepareDataMap(customerDocument);
		if (!data.isEmpty()) {
			System.out.println(data.toString());
			Optional<Customer> customer = customerRepository.findById(customerDocument.getCustomerId());

			Supplier<RuntimeException> exceptionSupplier = () -> new RuntimeException("Customer not found");
			String firstName = (customer.map(Customer::getFirstName).orElseThrow(exceptionSupplier)).toLowerCase();
			String lastName = (customer.map(Customer::getLastName).orElseThrow(exceptionSupplier)).toLowerCase();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			FileInputStream fis = new FileInputStream(SRC);
			try (XWPFDocument xwpfDocument = new XWPFDocument(fis)) {
				replacePlaceholdersInParagraphs(data, xwpfDocument);
				replacePlaceholderInTables(data, xwpfDocument);

				xwpfDocument.write(outputStream);

				// Create target path using first name, last name and document name from this
				// request
				String docName = customerDocument.getDocName();
				String targetFileExt = getFileExtension(SRC);
				String targetFilePath = String.format("%s%s-%s-%s.%s", DEST_PATH, firstName, lastName, docName,
						targetFileExt);

				Path path = Paths.get(targetFilePath);
				System.out.println("File created at " + path);
				Files.write(path, outputStream.toByteArray());
				return path.toString();
			} catch (Exception e) {
				LOGGER.error("Error occurred while generating report: {}", e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Method for replacing docx placeholders with given data parameters in every
	 * docx paragraph
	 *
	 * @param data         given data to be replaced with template placeholders
	 * @param xwpfDocument docx template document
	 **/
	private void replacePlaceholdersInParagraphs(Optional<HashMap<String, Object>> data, XWPFDocument xwpfDocument) {
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
	private void replacePlaceholderInTables(Optional<HashMap<String, Object>> data, XWPFDocument xwpfDocument) {
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

	private void replaceParagraph(XWPFParagraph paragraph, Optional<HashMap<String, Object>> data)
			throws POIXMLException {
		try {
			String find, text, runsText;
			List<XWPFRun> runs;
			XWPFRun run, nextRun;
			if (data.isPresent()) {
				HashMap<String, Object> items = data.get();
				for (Map.Entry<String, Object> entry : items.entrySet()) {
					String key = entry.getKey();
					String value = (String) entry.getValue();

					System.out.println("Key-" + key + "Value-" + value);

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

							if (isImageFile(value)) {
								System.out.println("CHECKING IMG FILE" + value);
								run.setText(runsText.contains(find) ? runsText.replace(find, "") : runsText, 0);
								insertImage(run, value);
							} else {
								run.setFontFamily(FONT2);
								run.setText(runsText.contains(find) ? runsText.replace(find, value) : runsText, 0);
							}
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

	public String getFileExtension(String filePath) {
//		String originalFilename = file.getOriginalFilename();

		if (filePath != null && filePath.contains(".")) {
			return filePath.substring(filePath.lastIndexOf(".") + 1);
		} else {
			return ""; // No extension found or filename is null
		}
	}
}