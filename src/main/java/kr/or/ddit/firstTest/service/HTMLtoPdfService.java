package kr.or.ddit.firstTest.service;

import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.DocumentException;

public interface HTMLtoPdfService {

	/**
	 * HTML과 CSS를 PDF로 변환
	 */
	byte[] convertHtmlToPdf(String htmlContent, String cssContent) throws DocumentException, IOException;

	/**
	 * HTML 템플릿에 데이터를 바인딩하여 PDF 생성
	 */
	byte[] generatePdfFromTemplate(String templateHtml, Map<String, Object> data, String cssContent)
			throws DocumentException, IOException;

	/**
	 * 한글 폰트 지원을 위한 PDF 생성
	 */
	byte[] convertHtmlToPdfWithKoreanFont(String htmlContent, String cssContent) throws DocumentException, IOException;

	/**
	 * 안전한 HTML to PDF 변환 (fallback 포함)
	 */
	byte[] safeConvertHtmlToPdf(String htmlContent, String cssContent) throws DocumentException, IOException;

	/**
	 * 간단한 HTML을 PDF로 변환 (XMLWorker 없이)
	 */
	byte[] convertSimpleHtmlToPdf(String htmlContent, String cssContent) throws DocumentException, IOException;

	/**
	 * HTML 전처리 메서드
	 */
	String preprocessHtml(String htmlContent);
	
	
}
