package kr.or.ddit.firstTest.controller;

import java.io.ByteArrayOutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import kr.or.ddit.firstTest.UserDataDTO;
import kr.or.ddit.firstTest.service.FirstService;
import lombok.extern.slf4j.Slf4j;


/**
 * 패키지명 firstTest
	1. 서버측에서 내용을 json형태로 입력받아 해당 내용을 pdf파일처리
	2. pdf파일처리된 내용을 스트림으로 전송 (다운로드)
	3. 서버측에서 내용을 json형태로 입력받아 해당 내용을 pdf파일처리
	4. pdf파일처리된 내용을 미리보기 가능 형태로 전송
 */
@Controller
@RequestMapping("/first")
@Slf4j
public class FirstController {
	
	@Autowired
	FirstService firstService;
	
	
	@GetMapping("/index")
	public String index() {
		
		return "first/index";
	}
	
	@PostMapping("/downloads")
	public ResponseEntity<ByteArrayResource> download(@RequestBody Map<String, Object> params){
		log.info("downloads -> params" + params);		// 사용자 입력값 테스트 { testText : wlqrktjqnfekfrqhRdmaaus }
		
		// 사용자한테 입력받은 값 꺼내오기
		String content = (String) params.get("testText");
		
		// iText의 Document 객체 생성
		Document document = new Document();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			// iText의 PdfWriter 인스턴스 받아오고 document와 byteOutput스트림 연결
			PdfWriter.getInstance(document, bos);
			// document를 오픈하고
			document.open();
			// document에 새로운 문단을 만들면서 입력받은 글을 작성
			document.add(new Paragraph(content));
			// 문서종료
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		// 위에 작업하고 나면 byteArrayOutputStream에 작성했던 pdf문서의 byte값이 담겨있음.
		// byte 배열로 변환
		byte[] pdfBytes = bos.toByteArray();
		
		// 클라이언트에 전송할 헤더 세팅
        HttpHeaders headers = new HttpHeaders();
        // Content-Type을 application/pdf로 설정하여 브라우저에서 미리보기 가능하게 함
        // attachment가 inline으로 바뀌면 다운로드냐 미리보기냐 설정
        headers.add("Content-Disposition", "attachment; filename=dynamic_itext5_pdf.pdf");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        
        // byte배열을 전송가능한 자원으로 변환
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
		
        // 전송
		return ResponseEntity.ok()
					.headers(headers)
					.contentLength(pdfBytes.length)
					.contentType(MediaType.APPLICATION_PDF)
					.body(resource);
	}
	
	
	//==============Test code by claude===============
	
    // 데이터 입력 폼 페이지
    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("userData", new UserDataDTO());
        return "first/pdf-form";
    }
    
    // 폼 데이터 처리 및 미리보기 페이지
    @PostMapping("/preview")
    public String previewData(@ModelAttribute UserDataDTO userData, Model model) {
        model.addAttribute("userData", userData);
        return "first/pdf-preview";
    }
    
    // PDF 다운로드
    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadPdf(@ModelAttribute UserDataDTO userData) {
        try {
            byte[] pdfBytes = firstService.generatePdf(userData);
            
            // 파일명 생성 (현재 날짜시간 포함)
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "사용자정보_" + timestamp + ".pdf";
            
            // 한글 파일명 인코딩
            String encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFilename);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // PDF 미리보기 (브라우저에서 직접 열기)
    @PostMapping("/view")
    public ResponseEntity<byte[]> viewPdf(@ModelAttribute UserDataDTO userData) {
        try {
            byte[] pdfBytes = firstService.generatePdf(userData);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "inline; filename=preview.pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // AJAX를 통한 PDF 미리보기 데이터 반환
    @PostMapping("/preview-data")
    @ResponseBody
    public ResponseEntity<byte[]> getPreviewData(@RequestBody UserDataDTO userData) {
        try {
            byte[] pdfBytes = firstService.generatePdf(userData);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.add("Content-Disposition", "inline; filename=preview.pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
}
