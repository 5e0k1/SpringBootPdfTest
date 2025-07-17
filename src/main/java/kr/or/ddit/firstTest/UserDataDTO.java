package kr.or.ddit.firstTest;

import java.time.LocalDate;
import java.util.Date;

public class UserDataDTO {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String content;
    private LocalDate createdDate;
    
    // 기본 생성자
    public UserDataDTO() {
        this.createdDate = LocalDate.now();
    }
    
    // 매개변수 생성자
    public UserDataDTO(String name, String email, String phone, String address, String content) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.content = content;
        this.createdDate = LocalDate.now();
    }
    
    // Getter & Setter
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    
    // JSP에서 포맷된 날짜를 사용할 수 있도록 도우미 메서드 추가
    public String getFormattedCreatedDate() {
        if (createdDate != null) {
            return createdDate.toString(); // yyyy-MM-dd 형식으로 반환
        }
        return "";
    }
}