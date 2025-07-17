<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script>
document.addEventListener("DOMContentLoaded", function(){
	document.getElementById("subBtn").addEventListener("click", function(){
		let testText = document.getElementById("pdfText").value;
		let pdfData = {testText};
		
		console.log(pdfData);
		fetch("/first/download", {
			method:"post",
			headers:{
				"Content-type":"application/json"
			},
			body:JSON.stringify(pdfData)
		})
		.then(response=>{
			if(!response.ok){
				throw new Error("잘못된 응답");
			}
			const contentDisposition = response.headers.get('Content-Disposition');
			let filename = 'download.pdf'; // 기본 파일 이름 - 서버에서 보내준 파일이름이 정상적이지 않을 경우 대체
			// 파일 검증 단계
			// 헤더에 Content-Disposition이 존재, attachment가 존재
			if (contentDisposition && contentDisposition.indexOf('attachment') !== -1) {
				// filename= 으로 시작해야함 파일명은 큰따옴표나 작은따옴표로 감싸도되고 .pdf로 끝나야함
                const filenameRegex = /filename\s*=\s*((['"]).*?\.pdf\2|[^;\n]*\.pdf)/i;
                const matches = filenameRegex.exec(contentDisposition);
                if (matches != null && matches[1]) {
					// 따옴표가 있으면 따옴표 제거
                    filename = matches[1].replace(/['"]/g, '');
                }
            }
			// 파일명 검증 마친(잘못왔으면 디폴트 파일이름으로 변경) 파일이름과 파일의 값blob를 파싱해서 return
			return response.blob().then(blob => ({ blob, filename }));
		})
		.then(({blob, filename})=>{
			// blob를 url값으로 변경하고 해당 url에 요청하면서 다운로드
			const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = filename; // 다운로드될 파일 이름 설정
            document.body.appendChild(a); // DOM에 추가 (클릭을 위해)
            a.click(); // 클릭 이벤트 강제 실행 -> 다운로드
            
            // 메모리 관리
            document.body.removeChild(a); // 생성된 a 태그 제거
            window.URL.revokeObjectURL(url); // Blob URL 해제
		})
		.catch(error => {
                    console.error('PDF 다운로드 오류:', error);
                    alert('PDF 다운로드 중 오류가 발생했습니다: ' + error.message);
        })
	});

})


</script>
</head>
<body>

	<form action="/first/download" method="post" id="pdfFrm">
		<textarea rows="30" cols="30" name="pdfText" id="pdfText"></textarea>
		<input type="button" id="subBtn" />
	</form>


</body>
</html>