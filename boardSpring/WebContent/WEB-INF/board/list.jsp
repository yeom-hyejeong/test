<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!-- list.jsp -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html> 
<head>
	<title>게시글보기</title>
	<link rel="stylesheet" type="text/css" href="style.css">
</head>	
<body>
<div align="center"> 
	<b>글 목 록</b>
	<table border="0" width="50%">
		<tr bgcolor="yellow">
			<td align="right">
				<a href="index.do">메인으로</a>
				<a href="writeForm_board.do">글쓰기</a>
			</td>
		</tr>
	</table>
	<table border="1" width="50%">
		<tr bgcolor="green">
			<th>번호</th>
			<th width="30%">제 목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회</th>
			<th>IP</th>
		</tr>
	<c:if test="${empty listBoard}">
		<tr>
			<td colspan="6">등록된 게시글이 없습니다.</td>
		</tr>
	</c:if>	
	<c:set var="num" value="${requestScope.num}"/>
	<c:forEach var="dto" items="${listBoard}">
		<tr>
			<td>
				<c:out value="${num}"/>
				<c:set var="num" value="${num-1}"/>
			</td>
			<td>
				<a href="content_board.do?num=${dto.num}">
					<c:if test="${dto.re_level>0}">
						<img src="img/level.gif" width="${dto.re_level*10}">
						<img src="img/re.gif">
					</c:if>	
					${dto.subject}
				</a>
				<c:if test="${dto.readcount>10}">
					<img src="img/hot.gif">
				</c:if>	
			</td>
			<td>${dto.writer}</td>
			<td>${dto.reg_date}</td>
			<td>${dto.readcount}</td>
			<td>${dto.ip}</td>
		</tr>
	</c:forEach>
	</table>
</div>
</body>
</html>










