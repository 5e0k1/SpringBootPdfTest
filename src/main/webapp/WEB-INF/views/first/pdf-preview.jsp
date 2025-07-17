<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PDF 미리보기</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background-color: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 30px;
        }
        .preview-section {
            display: flex;
            gap: 30px;
            margin-bottom: 30px;
        }
        .data-preview {
            flex: 1;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 6px;
        }
        .pdf-preview {
            flex: 1;
            text-align: center;
        }
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        .data-table th, .data-table td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        .data-table th {
            background-color: #e9ecef;
            font-weight: bold;
            width: 30%;
        }
        .content-section {
            margin-top: 20px;
            padding: 15px;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .content-title {
            font-weight: bold;
            margin-bottom: 10px;
            color: #333;
        }
        .button-group {
            text-align: center;
            margin-top: 30px;
        }
        .btn {
            padding: 12px 30px;
            margin: 0 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            text-decoration: none;
            display: inline-block;
        }
        .btn-success {
            background-color: #28a745;
            color: white;
        }
        .btn-success:hover {
            background-color: #218838;
        }
        .btn-info {
            background-color: #17a2b8;
            color: white;
        }
        .btn-info:hover {
            background-color: #138496;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .btn-secondary:hover {
            background-color: #545b62;
        }
        #pdf-viewer {
            width: 100%;
            height: 600px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .loading {
            text-align: center;
            padding: 50px;
            color: #666;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>PDF 미리보기</h1>
        
        <div class="preview-section">
            <div class="data-preview">
                <h3>입력된 데이터</h3>
                <table class="data-table">
                    <tr>
                        <th>이름</th>
                        <td>${userData.name}</td>
                    </tr>
                    <tr>
                        <th>이메일</th>
                        <td>${userData.email}</td>
                    </tr>
                    <tr>
                        <th>전화번호</th>
                        <td>${userData.phone}</td>
                    </tr>
                    <tr>
                        <th>주소</th>
                        <td>${userData.address}</td>
                    </tr>
                    <tr>
                        <th>생성일자</th>
                        <td>${userData.formattedCreatedDate}</td>
                    </tr>
                </table>
                
                <c:if test="${not empty userData.content}">
                    <div class="content-section">
                        <div class="content-title">내용</div>
                        <div>${userData.content}</div>
                    </div>
                </c:if>
            </div>
            
            <div class="pdf-preview">
                <h3>PDF 미리보기</h3>
                <div id="pdf-loading" class="loading">PDF를 생성중입니다...</div>
                <iframe id="pdf-viewer" src="" style="display: none;"></iframe>
            </div>
        </div>
        
        <div class="button-group">
            <form id="download-form" method="post" action="/first/download" style="display: inline;">
                <input type="hidden" name="name" value="${userData.name}"/>
                <input type="hidden" name="email" value="${userData.email}"/>
                <input type="hidden" name="phone" value="${userData.phone}"/>
                <input type="hidden" name="address" value="${userData.address}"/>
                <input type="hidden" name="content" value="${userData.content}"/>
                <input type="submit" value="PDF 다운로드" class="btn btn-success"/>
            </form>
            
            <form id="view-form" method="post" action="/first/view" target="_blank" style="display: inline;">
                <input type="hidden" name="name" value="${userData.name}"/>
                <input type="hidden" name="email" value="${userData.email}"/>
                <input type="hidden" name="phone" value="${userData.phone}"/>
                <input type="hidden" name="address" value="${userData.address}"/>
                <input type="hidden" name="content" value="${userData.content}"/>
                <input type="submit" value="새 탭에서 보기" class="btn btn-info"/>
            </form>
            
            <a href="/first/form" class="btn btn-secondary">다시 입력</a>
        </div>
    </div>
    
    <script>
        // 페이지 로드 시 PDF 미리보기 생성
        window.onload = function() {
            generatePdfPreview();
        };
        
        function generatePdfPreview() {
            const userData = {
                name: '${userData.name}',
                email: '${userData.email}',
                phone: '${userData.phone}',
                address: '${userData.address}',
                content: '${userData.content}'
            };
            
            fetch('/first/preview-data', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData)
            })
            .then(response => response.blob())
            .then(blob => {
                const url = window.URL.createObjectURL(blob);
                const pdfViewer = document.getElementById('pdf-viewer');
                const pdfLoading = document.getElementById('pdf-loading');
                
                pdfViewer.src = url;
                pdfViewer.style.display = 'block';
                pdfLoading.style.display = 'none';
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('pdf-loading').innerHTML = 'PDF 생성 중 오류가 발생했습니다.';
            });
        }
        
        function openPdfInNewTab() {
            const form = document.createElement('form');
            form.method = 'post';
            form.action = '/first/view';
            form.target = '_blank';
            
            // 각 필드별로 직접 값 설정
            const userData = {
                name: '${userData.name}',
                email: '${userData.email}',
                phone: '${userData.phone}',
                address: '${userData.address}',
                content: '${userData.content}'
            };
            
            Object.keys(userData).forEach(field => {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = field;
                input.value = userData[field];
                form.appendChild(input);
            });
            
            document.body.appendChild(form);
            form.submit();
            document.body.removeChild(form);
        }
    </script>
</body>
</html>