package kr.or.ddit.secondTest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.secondTest.service.SecondService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/second")
public class SecondController {
	
	@Autowired
	SecondService secondService;
	
    @GetMapping("/form")
    public String showForm() {
        return "second/html-pdf-form";
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generatePdf(@RequestParam("htmlContent") String htmlContent,
                                            @RequestParam(value = "cssContent", required = false) String cssContent,
                                            HttpServletResponse response) {
        
        log.info("PDF 생성 요청 - HTML 길이: {}, CSS 길이: {}", 
                htmlContent != null ? htmlContent.length() : 0, 
                cssContent != null ? cssContent.length() : 0);
        
        try {
            // Flying Saucer 기반 안전한 변환
            byte[] pdfBytes = secondService.safeConvertHtmlToPdf(htmlContent, cssContent);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "document.pdf");
            headers.setContentLength(pdfBytes.length);
            
            log.info("PDF 생성 성공 - 파일 크기: {} bytes", pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("PDF 생성 실패", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/preview")
    @ResponseBody
    public ResponseEntity<byte[]> previewPdf(@RequestParam("htmlContent") String htmlContent,
                                           @RequestParam(value = "cssContent", required = false) String cssContent) {
        
        log.info("PDF 미리보기 요청 - HTML 길이: {}, CSS 길이: {}", 
                htmlContent != null ? htmlContent.length() : 0, 
                cssContent != null ? cssContent.length() : 0);
        
        try {
            // Flying Saucer 기반 안전한 변환
            byte[] pdfBytes = secondService.safeConvertHtmlToPdf(htmlContent, cssContent);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "preview.pdf");
            headers.setContentLength(pdfBytes.length);
            
            log.info("PDF 미리보기 성공 - 파일 크기: {} bytes", pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("PDF 미리보기 실패", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 한글 폰트 지원 PDF 생성
     */
    @PostMapping("/generate-korean")
    public ResponseEntity<byte[]> generateKoreanPdf(@RequestParam("htmlContent") String htmlContent,
                                                   @RequestParam(value = "cssContent", required = false) String cssContent) {
        
        log.info("한글 PDF 생성 요청 - HTML 길이: {}, CSS 길이: {}", 
                htmlContent != null ? htmlContent.length() : 0, 
                cssContent != null ? cssContent.length() : 0);
        
        try {
            // 한글 폰트 지원 PDF 생성
            byte[] pdfBytes = secondService.convertHtmlToPdfWithKoreanFont(htmlContent, cssContent);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "korean-document.pdf");
            headers.setContentLength(pdfBytes.length);
            
            log.info("한글 PDF 생성 성공 - 파일 크기: {} bytes", pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("한글 PDF 생성 실패", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * 간단한 HTML을 PDF로 변환
     */
    @PostMapping("/simple")
    public ResponseEntity<byte[]> generateSimplePdf(@RequestParam("htmlContent") String htmlContent,
                                                   @RequestParam(value = "cssContent", required = false) String cssContent) {
        
        log.info("간단한 PDF 생성 요청 - HTML 길이: {}", htmlContent != null ? htmlContent.length() : 0);
        
        try {
            // 간단한 HTML to PDF 변환
            byte[] pdfBytes = secondService.convertSimpleHtmlToPdf(htmlContent, cssContent);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "simple-document.pdf");
            headers.setContentLength(pdfBytes.length);
            
            log.info("간단한 PDF 생성 성공 - 파일 크기: {} bytes", pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("간단한 PDF 생성 실패", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
