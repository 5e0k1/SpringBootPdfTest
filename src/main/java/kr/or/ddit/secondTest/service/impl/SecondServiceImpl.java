package kr.or.ddit.secondTest.service.impl;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;

import kr.or.ddit.secondTest.service.SecondService;

@Service
public class SecondServiceImpl implements SecondService{

	@Override
	public byte[] convertHtmlToPdf(String htmlContent, String cssContent) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String preprocessHtml(String htmlContent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] generatePdfFromTemplate(String templateHtml, Map<String, Object> data, String cssContent)
			throws DocumentException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] convertSimpleHtmlToPdf(String htmlContent, String cssContent) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] safeConvertHtmlToPdf(String htmlContent, String cssContent) throws DocumentException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] convertHtmlToPdfWithKoreanFont(String htmlContent, String cssContent)
			throws DocumentException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
