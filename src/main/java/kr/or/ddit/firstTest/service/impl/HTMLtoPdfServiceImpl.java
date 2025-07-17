package kr.or.ddit.firstTest.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.ITextFontResolver;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

import kr.or.ddit.firstTest.service.HTMLtoPdfService;

@Service
public class HTMLtoPdfServiceImpl implements HTMLtoPdfService {

    private static final String DEFAULT_FONT_PATH = "C:/Windows/Fonts/malgun.ttf";
    private static final String MAC_FONT_PATH = "/System/Library/Fonts/AppleGothic.ttf";
    private static final String RESOURCE_FONT_PATH = "fonts/NanumGothic.ttf";

    /**
     * HTML과 CSS를 PDF로 변환 (Flying Saucer 사용)
     */
    @Override
    public byte[] convertHtmlToPdf(String htmlContent, String cssContent) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            // HTML 내용 검증 및 전처리
            if (htmlContent == null || htmlContent.trim().isEmpty()) {
                throw new IllegalArgumentException("HTML 내용이 비어있습니다.");
            }
            
            // 완전한 HTML 문서로 변환
            String fullHtml = buildFullHtmlDocument(htmlContent, cssContent);
            System.out.println("==============Generated HTML=================");
            System.out.println(fullHtml);
            System.out.println("=========================================");
            
            // Flying Saucer 렌더러 생성
            ITextRenderer renderer = new ITextRenderer();
            
            // 한글 폰트 설정
            setupKoreanFonts(renderer);
            
            // HTML 문서 설정
            renderer.setDocumentFromString(fullHtml);
            renderer.layout();
            
            // PDF 생성
            renderer.createPDF(baos);
            
        } catch (Exception e) {
            System.err.println("PDF 생성 중 오류: " + e.getMessage());
            e.printStackTrace();
            throw new DocumentException("PDF 생성 실패: " + e.getMessage());
        }
        
        return baos.toByteArray();
    }
    
    /**
     * HTML 전처리 메서드
     */
    @Override
    public String preprocessHtml(String htmlContent) {
        String processed = htmlContent.trim();
        
        // 완전한 HTML 문서인 경우 body 내용만 추출
        if (processed.toLowerCase().contains("<!doctype") || processed.toLowerCase().contains("<html")) {
            int bodyStart = processed.toLowerCase().indexOf("<body");
            if (bodyStart != -1) {
                int bodyContentStart = processed.indexOf(">", bodyStart) + 1;
                int bodyEnd = processed.toLowerCase().lastIndexOf("</body>");
                if (bodyEnd != -1) {
                    processed = processed.substring(bodyContentStart, bodyEnd);
                }
            }
        }
        
        // 빈 내용인 경우 기본 내용 추가
        if (processed.trim().isEmpty() || processed.trim().equals("<body></body>")) {
            processed = "<p>내용이 없습니다.</p>";
        }
        
        return processed;
    }

    /**
     * HTML 템플릿에 데이터를 바인딩하여 PDF 생성
     */
    @Override
    public byte[] generatePdfFromTemplate(String templateHtml, Map<String, Object> data, String cssContent) 
            throws DocumentException, IOException {
        
        // HTML 템플릿에 데이터 바인딩 (간단한 치환 방식)
        String processedHtml = templateHtml;
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String placeholder = "{{" + entry.getKey() + "}}";
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            processedHtml = processedHtml.replace(placeholder, value);
        }
        
        return convertHtmlToPdf(processedHtml, cssContent);
    }

    /**
     * 간단한 HTML을 PDF로 변환 (Flying Saucer 기본 기능)
     */
    @Override
    public byte[] convertSimpleHtmlToPdf(String htmlContent, String cssContent) throws DocumentException, IOException {
        // 기본 스타일과 함께 간단한 HTML 생성
        String simpleHtml = buildSimpleHtmlDocument(htmlContent, cssContent);
        return convertHtmlToPdf(simpleHtml, cssContent);
    }
    
    /**
     * 안전한 HTML to PDF 변환 (fallback 포함)
     */
    @Override
    public byte[] safeConvertHtmlToPdf(String htmlContent, String cssContent) throws DocumentException, IOException {
        try {
            return convertHtmlToPdf(htmlContent, cssContent);
        } catch (Exception e) {
            System.err.println("Flying Saucer 방식 실패, 간단한 방식으로 fallback: " + e.getMessage());
            return convertSimpleHtmlToPdf(htmlContent, cssContent);
        }
    }
    
    /**
     * 한글 폰트 지원 HTML to PDF 변환
     */
    @Override
    public byte[] convertHtmlToPdfWithKoreanFont(String htmlContent, String cssContent) 
            throws DocumentException, IOException {
        // Flying Saucer는 기본적으로 한글 폰트를 잘 지원하므로 convertHtmlToPdf와 동일
        return convertHtmlToPdf(htmlContent, cssContent);
    }
    
    /**
     * 완전한 HTML 문서 생성
     */
    private String buildFullHtmlDocument(String htmlContent, String cssContent) {
        String processedHtml = preprocessHtml(htmlContent);
        
        StringBuilder fullHtml = new StringBuilder();
        fullHtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        fullHtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        fullHtml.append("<head>");
        fullHtml.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />");
        fullHtml.append("<title>PDF Document</title>");
        fullHtml.append("<style type=\"text/css\">");
        fullHtml.append(getDefaultStyles());
        if (cssContent != null && !cssContent.trim().isEmpty()) {
            fullHtml.append(cssContent);
        }
        fullHtml.append("</style>");
        fullHtml.append("</head>");
        fullHtml.append("<body>");
        fullHtml.append(processedHtml);
        fullHtml.append("</body>");
        fullHtml.append("</html>");
        
        return fullHtml.toString();
    }
    
    /**
     * 간단한 HTML 문서 생성
     */
    private String buildSimpleHtmlDocument(String htmlContent, String cssContent) {
        String textContent = htmlContent != null ? htmlContent.replaceAll("<[^>]*>", "").trim() : "";
        if (textContent.isEmpty()) {
            textContent = "내용이 없습니다.";
        }
        
        return buildFullHtmlDocument("<p>" + textContent + "</p>", cssContent);
    }
    
    /**
     * 기본 CSS 스타일 반환
     */
    private String getDefaultStyles() {
        return """
            body { 
                font-family: 'Malgun Gothic', 'Apple Gothic', 'NanumGothic', sans-serif; 
                margin: 20px; 
                line-height: 1.6;
                color: #333;
                font-size: 12px;
            }
            h1, h2, h3, h4, h5, h6 { 
                color: #2c3e50; 
                margin-top: 0;
                margin-bottom: 10px;
            }
            h1 { font-size: 24px; }
            h2 { font-size: 20px; }
            h3 { font-size: 16px; }
            table { 
                width: 100%; 
                border-collapse: collapse; 
                margin: 20px 0;
                font-size: 11px;
            }
            th, td { 
                border: 1px solid #ddd; 
                padding: 8px; 
                text-align: left; 
            }
            th { 
                background-color: #f8f9fa; 
                font-weight: bold; 
            }
            .header { 
                text-align: center; 
                margin-bottom: 30px; 
            }
            .footer { 
                text-align: center; 
                margin-top: 30px; 
                font-size: 10px; 
                color: #666; 
            }
            p { 
                margin: 0 0 10px 0; 
            }
            .highlight { 
                background-color: #fff3cd; 
                padding: 5px; 
            }
            .text-center { text-align: center; }
            .text-right { text-align: right; }
            .bold { font-weight: bold; }
            """;
    }
    
    /**
     * 한글 폰트 설정
     */
    private void setupKoreanFonts(ITextRenderer renderer) {
        try {
            ITextFontResolver fontResolver = renderer.getFontResolver();
            
            // Windows 폰트 시도
            File windowsFont = new File(DEFAULT_FONT_PATH);
            if (windowsFont.exists()) {
                fontResolver.addFont(DEFAULT_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                System.out.println("Windows 폰트 로드 성공: " + DEFAULT_FONT_PATH);
                return;
            }
            
            // macOS 폰트 시도
            File macFont = new File(MAC_FONT_PATH);
            if (macFont.exists()) {
                fontResolver.addFont(MAC_FONT_PATH, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                System.out.println("macOS 폰트 로드 성공: " + MAC_FONT_PATH);
                return;
            }
            
            // 클래스패스 리소스 폰트 시도
            try {
                ClassPathResource resource = new ClassPathResource(RESOURCE_FONT_PATH);
                if (resource.exists()) {
                    fontResolver.addFont(resource.getFile().getAbsolutePath(), 
                                       BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                    System.out.println("리소스 폰트 로드 성공: " + RESOURCE_FONT_PATH);
                    return;
                }
            } catch (Exception e) {
                System.err.println("리소스 폰트 로드 실패: " + e.getMessage());
            }
            
            System.out.println("한글 폰트를 찾을 수 없습니다. 기본 폰트를 사용합니다.");
            
        } catch (Exception e) {
            System.err.println("폰트 설정 중 오류: " + e.getMessage());
        }
    }
}