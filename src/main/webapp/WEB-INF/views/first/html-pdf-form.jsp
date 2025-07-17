<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>HTML to PDF Test</title>
    <style>
        body {
            font-family: 'Malgun Gothic', 'Apple Gothic', 'NanumGothic', Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        textarea {
            width: 100%;
            box-sizing: border-box;
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 8px;
            font-family: 'Malgun Gothic', 'Apple Gothic', 'NanumGothic', monospace;
        }
        .btn {
            background-color: #007bff;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-right: 10px;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .btn-success {
            background-color: #28a745;
        }
        .btn-success:hover {
            background-color: #1e7e34;
        }
        .btn-preview {
            background-color: #17a2b8;
        }
        .btn-preview:hover {
            background-color: #138496;
        }
        .preview-area {
            border: 1px solid #ddd;
            border-radius: 4px;
            padding: 15px;
            margin-top: 20px;
            background-color: #f9f9f9;
        }
        .split-container {
            display: flex;
            gap: 20px;
        }
        .split-container > div {
            flex: 1;
        }
        .sample-buttons {
            margin-bottom: 15px;
        }
        .sample-btn {
            background-color: #6c757d;
            color: white;
            padding: 5px 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-right: 5px;
            font-size: 12px;
        }
        .sample-btn:hover {
            background-color: #545b62;
        }
        .loading {
            display: none;
            color: #007bff;
            font-style: italic;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>HTML to PDF 변환 테스트</h1>
        
        <form id="pdfForm" method="post">
            <div class="split-container">
                <div>
                    <div class="form-group">
                        <label for="htmlContent">HTML 내용:</label>
                        <div class="sample-buttons">
                            <button type="button" class="sample-btn" onclick="loadSample('basic')">기본 샘플</button>
                            <button type="button" class="sample-btn" onclick="loadSample('table')">테이블 샘플</button>
                            <button type="button" class="sample-btn" onclick="loadSample('form')">폼 샘플</button>
                            <button type="button" class="sample-btn" onclick="loadSample('korean')">한글 테스트</button>
                        </div>
                        <textarea id="htmlContent" name="htmlContent" rows="15" placeholder="HTML 내용을 입력하세요..."></textarea>
                    </div>
                </div>
                <div>
                    <div class="form-group">
                        <label for="cssContent">CSS 스타일:</label>
                        <div class="sample-buttons">
                            <button type="button" class="sample-btn" onclick="loadCssSample('basic')">기본 CSS</button>
                            <button type="button" class="sample-btn" onclick="loadCssSample('modern')">모던 CSS</button>
                            <button type="button" class="sample-btn" onclick="loadCssSample('print')">인쇄용 CSS</button>
                            <button type="button" class="sample-btn" onclick="loadCssSample('korean')">한글 CSS</button>
                        </div>
                        <textarea id="cssContent" name="cssContent" rows="15" placeholder="CSS 스타일을 입력하세요..."></textarea>
                    </div>
                </div>
            </div>
            
            <div class="form-group">
                <button type="button" class="btn btn-preview" onclick="previewPdf()">미리보기</button>
                <button type="button" class="btn btn-success" onclick="downloadPdf()">PDF 다운로드</button>
                <button type="button" class="btn" onclick="testKoreanPdf()">한글 PDF 테스트</button>
                <span class="loading" id="loading">처리 중...</span>
            </div>
        </form>
        
        <div id="previewArea" class="preview-area" style="display: none;">
            <h3>미리보기:</h3>
            <div id="previewContent"></div>
        </div>
    </div>

    <script>
        // 샘플 HTML 로드
        function loadSample(type) {
            const htmlTextarea = document.getElementById('htmlContent');
            
            switch(type) {
                case 'basic':
                    htmlTextarea.value = `<h1>PDF 변환 테스트</h1>
<p>이것은 <strong>기본적인</strong> HTML 문서입니다.</p>
<p>한글 테스트: 안녕하세요!</p>
<ul>
    <li>첫 번째 항목</li>
    <li>두 번째 항목</li>
    <li>세 번째 항목</li>
</ul>`;
                    break;
                    
                case 'table':
                    htmlTextarea.value = `<h1>직원 목록</h1>
<table border="1">
    <thead>
        <tr>
            <th>이름</th>
            <th>부서</th>
            <th>직급</th>
            <th>연락처</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>홍길동</td>
            <td>개발팀</td>
            <td>선임</td>
            <td>010-1234-5678</td>
        </tr>
        <tr>
            <td>김철수</td>
            <td>디자인팀</td>
            <td>주임</td>
            <td>010-9876-5432</td>
        </tr>
    </tbody>
</table>`;
                    break;
                    
                case 'form':
                    htmlTextarea.value = `<h1>휴가 신청서</h1>
<div class="form-section">
    <h2>신청자 정보</h2>
    <p><strong>이름:</strong> 홍길동</p>
    <p><strong>부서:</strong> 개발팀</p>
    <p><strong>직급:</strong> 선임</p>
</div>
<div class="form-section">
    <h2>휴가 정보</h2>
    <p><strong>휴가 종류:</strong> 연차</p>
    <p><strong>시작일:</strong> 2024-01-15</p>
    <p><strong>종료일:</strong> 2024-01-17</p>
    <p><strong>사유:</strong> 개인 사정</p>
</div>`;
                    break;
                    
                case 'korean':
                    htmlTextarea.value = `<h1>한글 폰트 테스트</h1>
<div class="korean-test">
    <h2>다양한 한글 텍스트</h2>
    <p>안녕하세요. 한글 폰트 테스트입니다.</p>
    <p><strong>굵은 글씨:</strong> 강조된 한글 텍스트</p>
    <p><em>기울임:</em> 이탤릭체 한글 텍스트</p>
    
    <h3>한글 숫자 혼합</h3>
    <p>2024년 1월 15일 오후 3시 30분</p>
    <p>ABC123 가나다라마바사</p>
    
    <h3>특수문자 포함</h3>
    <p>이메일: test@example.com</p>
    <p>전화번호: 010-1234-5678</p>
    <p>주소: 서울특별시 강남구 테헤란로 123</p>
</div>`;
                    break;
            }
        }
        
        // 샘플 CSS 로드
        function loadCssSample(type) {
            const cssTextarea = document.getElementById('cssContent');
            
            switch(type) {
                case 'basic':
                    cssTextarea.value = `body {
    font-family: 'Malgun Gothic', 'Apple Gothic', 'NanumGothic', Arial, sans-serif;
    margin: 20px;
    line-height: 1.6;
}

h1 {
    color: #333;
    border-bottom: 2px solid #007bff;
    padding-bottom: 10px;
}

table {
    width: 100%;
    border-collapse: collapse;
}

th, td {
    padding: 8px;
    text-align: left;
    border: 1px solid #ddd;
}

th {
    background-color: #f2f2f2;
}`;
                    break;
                    
                case 'modern':
                    cssTextarea.value = `body {
    font-family: 'Malgun Gothic', 'Apple Gothic', 'NanumGothic', 'Segoe UI', sans-serif;
    margin: 0;
    padding: 20px;
    background-color: #f8f9fa;
    color: #333;
}

h1 {
    color: #2c3e50;
    font-size: 28px;
    margin-bottom: 20px;
    text-align: center;
    border-bottom: 3px solid #3498db;
    padding-bottom: 15px;
}

h2 {
    color: #34495e;
    font-size: 20px;
    margin-top: 25px;
    margin-bottom: 15px;
}

.form-section {
    background-color: white;
    padding: 20px;
    margin-bottom: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 15px;
    background-color: white;
    border-radius: 8px;
    overflow: hidden;
}

th {
    background-color: #3498db;
    color: white;
    padding: 12px;
    text-align: left;
}

td {
    padding: 12px;
    border-bottom: 1px solid #ecf0f1;
}

tr:hover {
    background-color: #f8f9fa;
}`;
                    break;
                    
                case 'print':
                    cssTextarea.value = `@page {
    size: A4;
    margin: 2cm;
}

body {
    font-family: 'Malgun Gothic', 'Apple Gothic', 'NanumGothic', 'Times New Roman', serif;
    font-size: 12pt;
    line-height: 1.5;
    color: #000;
}

h1 {
    font-size: 18pt;
    text-align: center;
    margin-bottom: 20pt;
    page-break-after: avoid;
}

h2 {
    font-size: 14pt;
    margin-top: 15pt;
    margin-bottom: 10pt;
    page-break-after: avoid;
}

p {
    margin-bottom: 10pt;
    text-align: justify;
}

table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 10pt;
    margin-bottom: 10pt;
    page-break-inside: avoid;
}

th, td {
    border: 1px solid #000;
    padding: 5pt;
    text-align: left;
}

th {
    background-color: #f0f0f0;
    font-weight: bold;
}

.form-section {
    margin-bottom: 15pt;
    page-break-inside: avoid;
}`;
                    break;
                    
                case 'korean':
                    cssTextarea.value = `body {
    font-family: 'Malgun Gothic', 'Apple Gothic', 'NanumGothic', sans-serif;
    margin: 20px;
    line-height: 1.8;
    color: #333;
    font-size: 14px;
}

h1 {
    font-family: 'Malgun Gothic', 'Apple Gothic', 'NanumGothic', sans-serif;
    color: #2c3e50;
    font-size: 24px;
    font-weight: bold;
    text-align: center;
    margin-bottom: 30px;
    border-bottom: 3px solid #3498db;
    padding-bottom: 15px;
}

h2, h3 {
    font-family: 'Malgun Gothic', 'Apple Gothic', 'NanumGothic', sans-serif;
    color: #34495e;
    margin-top: 25px;
    margin-bottom: 15px;
}

.korean-test {
    background-color: #f8f9fa;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
}

p {
    font-family: 'Malgun Gothic', 'Apple Gothic', 'NanumGothic', sans-serif;
    margin-bottom: 15px;
    line-height: 1.8;
}

strong {
    font-weight: bold;
    color: #2c3e50;
}

em {
    font-style: italic;
    color: #e74c3c;
}`;
                    break;
            }
        }
        
        // 로딩 상태 표시
        function showLoading() {
            document.getElementById('loading').style.display = 'inline';
        }
        
        function hideLoading() {
            document.getElementById('loading').style.display = 'none';
        }
        
        // PDF 미리보기 - AJAX 방식으로 변경
        function previewPdf() {
            showLoading();
            
            const formData = new FormData(document.getElementById('pdfForm'));
            let ht =formData.get('htmlContent');
            let cs =formData.get('cssContent');
            console.log("html : ", ht);
            console.log("css : ", cs);
            
            
            fetch('/pdf/preview', {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('미리보기 요청 실패');
                }
                return response.blob();
            })
            .then(blob => {
                const url = window.URL.createObjectURL(blob);
                
                // PDF URL에 줌 파라미터 추가
                const pdfUrlWithZoom = url + '#zoom=75';  // 75% 크기로 설정
                
                // 창 크기도 PDF 크기에 맞게 조절
                const windowWidth = 900;   // PDF 75% 크기에 맞는 창 너비
                const windowHeight = 700;  // PDF 75% 크기에 맞는 창 높이
                
                // 화면 중앙 위치 계산
                const left = (screen.width - windowWidth) / 2;
                const top = (screen.height - windowHeight) / 2;
                
                const windowFeatures = `width=\${windowWidth},height=\${windowHeight},left=\${left},top=\${top},scrollbars=yes,resizable=yes,toolbar=no,location=no,status=no`;
                
                // 새 창에서 PDF 열기
                const previewWindow = window.open(pdfUrlWithZoom, 'pdfPreview', windowFeatures);
                
                if (!previewWindow) {
                    // 팝업이 차단된 경우 현재 창에서 열기
                    window.open(pdfUrlWithZoom, '_blank');
                }
                
                hideLoading();
            })
            .catch(error => {
                console.error('미리보기 오류:', error);
                alert('PDF 미리보기 중 오류가 발생했습니다: ' + error.message);
                hideLoading();
            });
        }
        
        // PDF 다운로드 - form 제출 방식 유지
        function downloadPdf() {
            showLoading();
            
            const form = document.getElementById('pdfForm');
            form.action = '/pdf/generate';
            form.target = '_self';
            form.submit();
            
            // 다운로드 후 로딩 숨기기 (약간의 지연)
            setTimeout(() => {
                hideLoading();
            }, 2000);
        }
        
        // 한글 PDF 테스트
        function testKoreanPdf() {
            showLoading();
            
            const form = document.getElementById('pdfForm');
            form.action = '/pdf/generate-korean';
            form.target = '_self';
            form.submit();
            
            setTimeout(() => {
                hideLoading();
            }, 2000);
        }
        
        // 페이지 로드 시 기본 샘플 로드
        window.onload = function() {
            loadSample('korean');
            loadCssSample('korean');
        };
    </script>
</body>
</html>