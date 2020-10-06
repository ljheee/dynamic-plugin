<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
<link rel="stylesheet"
	href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	 <h3>插件控制台:${name}</h3>
	<div class="container" style="margin-top: 15px; margin-bottom: 15px;">
		<ul class="nav nav-pills">
			<li role="presentation"><a href="plugin?action=list">插件列表</a></li>
		</ul>
	</div>
	
	<div class="container">
		<textarea class="form-control" rows="50" >
			${statusText}
		</textarea>
	</div>
</body>


</html>