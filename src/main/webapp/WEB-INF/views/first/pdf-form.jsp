<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PDF 생성 - 데이터 입력</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 800px;
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
        .form-group {
            margin-bottom: 20px;
        }
        label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
            color: #555;
        }
        input[type="text"], input[type="email"], input[type="tel"], textarea {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            box-sizing: border-box;
        }
        textarea {
            height: 100px;
            resize: vertical;
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
        .btn-primary {
            background-color: #007bff;
            color: white;
        }
        .btn-primary:hover {
            background-color: #0056b3;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .btn-secondary:hover {
            background-color: #545b62;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>PDF 생성을 위한 데이터 입력</h1>
        
        <form:form modelAttribute="userData" method="post" action="/first/preview">
            <div class="form-group">
                <label for="name">이름 *</label>
                <form:input path="name" id="name" required="true" placeholder="이름을 입력하세요"/>
            </div>
            
            <div class="form-group">
                <label for="email">이메일 *</label>
                <form:input path="email" type="email" id="email" required="true" placeholder="이메일을 입력하세요"/>
            </div>
            
            <div class="form-group">
                <label for="phone">전화번호</label>
                <form:input path="phone" type="tel" id="phone" placeholder="전화번호를 입력하세요"/>
            </div>
            
            <div class="form-group">
                <label for="address">주소</label>
                <form:input path="address" id="address" placeholder="주소를 입력하세요"/>
            </div>
            
            <div class="form-group">
                <label for="content">내용</label>
                <form:textarea path="content" id="content" placeholder="추가 내용을 입력하세요"/>
            </div>
            
            <div class="button-group">
                <input type="submit" value="미리보기" class="btn btn-primary"/>
                <input type="reset" value="초기화" class="btn btn-secondary"/>
            </div>
        </form:form>
    </div>
</body>
</html>