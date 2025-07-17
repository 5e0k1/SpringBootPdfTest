package kr.or.ddit.firstTest.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import kr.or.ddit.firstTest.UserDataDTO;
import kr.or.ddit.firstTest.service.FirstService;

@Service
public class FirstServiceImpl implements FirstService{
	
	@Override
	public byte[] generatePdf(UserDataDTO userData) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        
        document.open();
        
        // 한글 폰트 설정
        BaseFont baseFont = BaseFont.createFont("HYGoThic-Medium", "UniKS-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font titleFont = new Font(baseFont, 18, Font.BOLD);
        Font headerFont = new Font(baseFont, 12, Font.BOLD);
        Font contentFont = new Font(baseFont, 10, Font.NORMAL);
        
        // 제목 추가
        Paragraph title = new Paragraph("사용자 정보 문서", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        
        // 생성일자 추가
        Paragraph dateInfo = new Paragraph("생성일자: " + userData.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), contentFont);
        dateInfo.setAlignment(Element.ALIGN_RIGHT);
        dateInfo.setSpacingAfter(20);
        document.add(dateInfo);
        
        // 사용자 정보 테이블 생성
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        
        // 테이블 컬럼 너비 설정
        float[] columnWidths = {1f, 2f};
        table.setWidths(columnWidths);
        
        // 테이블 데이터 추가
        addTableRow(table, "이름", userData.getName(), headerFont, contentFont);
        addTableRow(table, "이메일", userData.getEmail(), headerFont, contentFont);
        addTableRow(table, "전화번호", userData.getPhone(), headerFont, contentFont);
        addTableRow(table, "주소", userData.getAddress(), headerFont, contentFont);
        
        document.add(table);
        
        // 내용 추가
        if (userData.getContent() != null && !userData.getContent().trim().isEmpty()) {
            Paragraph contentTitle = new Paragraph("내용", headerFont);
            contentTitle.setSpacingBefore(20);
            contentTitle.setSpacingAfter(10);
            document.add(contentTitle);
            
            Paragraph content = new Paragraph(userData.getContent(), contentFont);
            content.setAlignment(Element.ALIGN_LEFT);
            document.add(content);
        }
        
        document.close();
        return baos.toByteArray();
    }
    
	@Override
    public void addTableRow(PdfPTable table, String header, String value, Font headerFont, Font contentFont) {
        PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
        headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        headerCell.setPadding(10);
        table.addCell(headerCell);
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "", contentFont));
        valueCell.setPadding(10);
        table.addCell(valueCell);
    }
}
