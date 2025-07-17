package kr.or.ddit.firstTest.service;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;

import kr.or.ddit.firstTest.UserDataDTO;

public interface FirstService {
	
	byte[] generatePdf(UserDataDTO userData) throws DocumentException, IOException;
	
	void addTableRow(PdfPTable table, String header, String value, Font headerFont, Font contentFont);
	
}
