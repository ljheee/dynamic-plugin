<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet"
	href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="container" style="margin-top: 15px;margin-bottom: 15px;">
		<ul class="nav nav-pills">
			<li role="presentation" class="active"><a
				href="plugin?action=list">已安装插件</a></li>
			<li role="presentation"><a href="plugin?action=site">安装新插件</a></li>
		</ul>
	</div>
	<div class="container">
		<table class="table">
			<tr>
				<th>插件名称</th>
				<th>版本</th>
				<th>启用</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${havePlugins}" var="plugin">
				<tr>
					<td>${plugin.name}</td>
					<td>${plugin.version}</td>
					<td><c:if test="${plugin.active == true}">
							<input type="checkbox" checked="checked"
								onchange="active(this,${plugin.id})" value="" />
						</c:if> <c:if test="${plugin.active == false}">
							<input type="checkbox" onchange="active(this,${plugin.id})"
								value="" />
						</c:if></td>

					<td><a href="#">更新</a> |<a href="#"
						onclick="uninstall('${plugin.id}')">卸载</a>| <a href="plugin?action=console&id=${plugin.id}">控制台</a></td>

				</tr>
			</c:forEach>
		</table>
	</div>
</body>

<script type="text/javascript">
	function active(checkBox,id){
		var check=$(checkBox).prop('checked');
		<!--TODO 需要加上 项目上下文 -->
		if(check){
			$.get("/plugin?action=active&id="+id,function(data,status){
			   	 	alert(data);
			  });
		}else{
			$.get("/plugin?action=disable&id="+id,function(data,status){
		   	 	alert(data);
		  });
		}
	}
	function uninstall(id){
		$.get("/plugin?action=uninstall&id="+id,function(data,status){
	   	 	alert(data);
	   	  window.location.reload();
	  });
		
	}
</script>
</html>